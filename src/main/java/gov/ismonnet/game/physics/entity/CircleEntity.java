package gov.ismonnet.game.physics.entity;

import dagger.Lazy;
import gov.ismonnet.game.physics.collider.CircleCollider;

import java.awt.geom.Rectangle2D;
import java.util.Set;

abstract class CircleEntity extends BaseEntity {

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

    protected void setPosX(float posXIn) {
        final boolean isRightwards = posXIn > posX;
        this.posX = posXIn;

        boolean hasCollidedHorizontally = false;
        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;

            boolean collided = false;
            Rectangle2D collision;
            while((collision = getCollision(entity)) != null) {
                collided = true;

                final double refX = (float) (isRightwards ?
                        collision.getX() :
                        collision.getMaxX());

                final double dist;
                if(posY >= collision.getY() && posY <= collision.getMaxY())
                    dist = radius - Math.abs(posX - refX);
                else {
                    final double refY = Math.abs(posY - collision.getY()) < Math.abs(posY - collision.getMaxY()) ?
                            collision.getY() :
                            collision.getMaxY();
                    // (x - xc)^2 + (refY - yc)^2 = r^2
                    // (x - xc)^2 = r^2 - (refY - yc)^2
                    // x - xc = sqrt(r^2 - (refY - yc)^2)
                    // x = sqrt(r^2 - (refY - yc)^2) + xc
                    final double uncenteredX = Math.sqrt(radius * radius - Math.pow(refY - posY, 2));
                    final double x = (isRightwards ? -uncenteredX : uncenteredX) + posX;
                    dist = Math.abs(posX  - x) - Math.abs(posX  - refX);
                }

                this.posX = (float) (isRightwards ?
                        posX - dist :
                        posX + dist);
            }

            if(collided) {
                // If first collision, revert motion dir
                if(!hasCollidedHorizontally)
                    motionX = -motionX;
                hasCollidedHorizontally = true;
                // Add motion if the other entity was also moving
                if(!entity.isImmovable()) {
                    motionX += entity.getMotionX();

                    if(entity instanceof BaseEntity)
                        ((BaseEntity) entity).motionX += getMotionX();
                }
            }
        }
    }

    protected void setPosY(float posYIn) {
        final boolean isDownwards = posYIn > posY;
        this.posY = posYIn;

        boolean hasCollidedVertically = false;
        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;

            boolean collided = false;
            Rectangle2D collision;
            while((collision = getCollision(entity)) != null) {
                collided = true;

                final double refY = isDownwards ?
                        collision.getY() :
                        collision.getMaxY();

                final double dist;
                if(posX >= collision.getX() && posX <= collision.getMaxX())
                    dist = radius - Math.abs(posY - refY);
                else {
                    final double refX = (Math.abs(posX - collision.getX()) < Math.abs(posX - collision.getMaxX()) ?
                            collision.getX() :
                            collision.getMaxX());
                    // (refX - xc)^2 + (y - yc)^2 = r^2
                    // (y - yc)^2 = r^2 - (refX - xc)^2
                    // y - yc = sqrt(r^2 - (refX - xc)^2)
                    // y = sqrt(r^2 - (refX - xc)^2) + yc
                    final double uncenteredY = Math.sqrt(radius * radius - Math.pow(refX - posX, 2));
                    final double y = (isDownwards ? -uncenteredY : uncenteredY) + posY;
                    dist = Math.abs(posY - y) - Math.abs(posY - refY);
                }

                this.posY = (float) (isDownwards ?
                        posY - dist :
                        posY + dist);
            }

            if(collided) {
                // If first collision, revert motion dir
                if(!hasCollidedVertically)
                    motionY = -motionY;
                hasCollidedVertically = true;
                // Add motion if the other entity was also moving
                if(!entity.isImmovable()) {
                    motionY += entity.getMotionY();

                    if(entity instanceof BaseEntity)
                        ((BaseEntity) entity).motionY += getMotionY();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CircleEntity{" +
                "radius=" + radius +
                "} " + super.toString();
    }
}
