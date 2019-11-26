package gov.ismonnet.game.physics.entity;

import dagger.Lazy;
import gov.ismonnet.game.physics.collider.CircleCollider;

import java.awt.geom.Rectangle2D;
import java.util.Set;

abstract class CircleEntity extends BaseEntity implements MovableEntity {

    protected final Lazy<Set<Entity>> collidingEntitiesLazy;
    protected final float radius;

    CircleEntity(float startX, float startY,
                 float radius,
                 Lazy<Set<Entity>> collidingEntitiesLazy) {
        this.collider = new CircleCollider(this::getPosX, this::getPosY, () -> radius);

        this.collidingEntitiesLazy = collidingEntitiesLazy;

        this.posX = startX;
        this.posY = startY;
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    protected void setPosX(float posX) {
        setPos0(posX, posY);
    }

    protected void setPosY(float posY) {
        setPos0(posX, posY);
    }

    protected void setPos(float posX, float posY) {
        setPos0(posX, posY);
    }

    private void setPos0(float posXIn, float posYIn) {

        final boolean isMotionX = posX != posXIn;
        final boolean isMotionY = posY != posYIn;

        if(!isMotionX && !isMotionY)
            return;

        final boolean isRightwards = posXIn > posX;
        final boolean isDownwards = posYIn > posY;

        final float xDiff = posX - posXIn;
        final float yDiff = posY - posYIn;

        this.posX = posXIn;
        this.posY = posYIn;

        boolean hasCollidedHorizontally = false;
        boolean hasCollidedVertically = false;

        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;

            boolean changeMotionX = false;
            boolean changeMotionY = false;

            int failsafe = 0;
            Rectangle2D collision;
            while((collision = getCollision(entity)) != null) {
                if(++failsafe >= 10)
                    break;
                // Hit horizontally
                if(isMotionX && posY >= collision.getY() && posY <= collision.getMaxY()) {
                    changeMotionX = true;

                    final double refX = (float) (isRightwards ?
                            collision.getX() :
                            collision.getMaxX());
                    final double dist = radius - Math.abs(posX - refX);
                    this.posX = (float) (isRightwards ?
                            posX - dist :
                            posX + dist);
                    continue;
                }
                // Hit vertically
                if(isMotionY && posX >= collision.getX() && posX <= collision.getMaxX()) {
                    changeMotionY = true;
                    final double refY = isDownwards ?
                            collision.getY() :
                            collision.getMaxY();
                    final double dist = radius - Math.abs(posY - refY);
                    this.posY = (float) (isDownwards ?
                            posY - dist :
                            posY + dist);
                    continue;
                }
                // Hit an angle
                changeMotionX = changeMotionY = true;

                final float distX = adjustAngleX(isRightwards, collision);
                final float distY = adjustAngleY(isDownwards, collision);

                if(distX == 0 && distY == 0)
                    break;

                if(distX != 0 && (distY == 0 || Math.abs(xDiff) >= Math.abs(yDiff))) {
                    this.posX = isRightwards ?
                            posX - distX :
                            posX + distX;
                } else {
                    this.posY = isDownwards ?
                            posY - distY :
                            posY + distY;
                }
            }
            // If first collision, revert motion dir
            if(changeMotionX) {
                if(!hasCollidedHorizontally)
                    setMotionX(-getMotionX());
                hasCollidedHorizontally = true;
            }
            if(changeMotionY) {
                if (!hasCollidedVertically)
                    setMotionY(-getMotionY());
                hasCollidedVertically = true;
            }
            // Add motion if the other entity was also moving
            if(!(entity instanceof MovableEntity))
                continue;
            final MovableEntity other = (MovableEntity) entity;

            if(changeMotionX) {
                final float currMotionX = getMotionX();
                setMotionX(getMotionX() + other.getMotionX());
                other.setMotionX(other.getMotionX() + currMotionX);
            }
            if(changeMotionY) {
                final float currMotionY = getMotionY();
                setMotionY(getMotionY() + other.getMotionY());
                other.setMotionY(other.getMotionY() + currMotionY);
            }
        }
    }

    private float adjustAngleX(boolean isRightwards, Rectangle2D collision) {
        final float refX = (float) (isRightwards ?
                collision.getX() :
                collision.getMaxX());
        // Closest point with x = refX
        final float refY = (float) (Math.abs(posY - collision.getY()) < Math.abs(posY - collision.getMaxY()) ?
                collision.getY() :
                collision.getMaxY());
        // (x - xc)^2 + (refY - yc)^2 = r^2
        // (x - xc)^2 = r^2 - (refY - yc)^2
        // x - xc = sqrt(r^2 - (refY - yc)^2)
        // x = sqrt(r^2 - (refY - yc)^2) + xc
        final float uncenteredX = (float) Math.sqrt(radius * radius - Math.pow(refY - posY, 2));
        final float x = (isRightwards ? -uncenteredX : uncenteredX) + posX;
        return Math.abs(posX  - x) - Math.abs(posX  - refX);
    }

    private float adjustAngleY(boolean isDownwards, Rectangle2D collision) {
        final float refY = (float) (isDownwards ?
                collision.getY() :
                collision.getMaxY());
        // Hit an angle
        // Closest point with y = refY
        final float refX = (float) (Math.abs(posX - collision.getX()) < Math.abs(posX - collision.getMaxX()) ?
                collision.getX() :
                collision.getMaxX());
        // (refX - xc)^2 + (y - yc)^2 = r^2
        // (y - yc)^2 = r^2 - (refX - xc)^2
        // y - yc = sqrt(r^2 - (refX - xc)^2)
        // y = sqrt(r^2 - (refX - xc)^2) + yc
        final float uncenteredY = (float) Math.sqrt(radius * radius - Math.pow(refX - posX, 2));
        final float y = (isDownwards ? -uncenteredY : uncenteredY) + posY;
        return Math.abs(posY - y) - Math.abs(posY - refY);
    }

    @Override
    public String toString() {
        return "CircleEntity{" +
                "radius=" + radius +
                "} " + super.toString();
    }
}
