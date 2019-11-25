package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import dagger.Lazy;
import gov.ismonnet.game.physics.collider.CircleCollider;
import gov.ismonnet.game.physics.table.Table;

import javax.inject.Inject;
import java.awt.geom.Rectangle2D;
import java.util.Set;

@AutoFactory
public class PaddleEntity extends BaseEntity {

    private final Table table;

    private final float radius;

    private float prevPosX;
    private float prevPosY;

    private final Lazy<Set<Entity>> collidingEntitiesLazy;

    @Inject PaddleEntity(float startX, float startY,
                         float radius,
                         @Provided Table table,
                         @Provided Lazy<Set<Entity>> collidingEntitiesLazy) {
        this.collider = new CircleCollider(this::getPosX, this::getPosY, () -> radius);

        this.table = table;
        this.collidingEntitiesLazy = collidingEntitiesLazy;

        this.posX = startX;
        this.posY = startY;
        this.radius = radius;
    }

    @Override
    public void tick() {
        this.prevPosX = posX;
        this.prevPosY = posY;
    }

    public void reset(float posX, float posY) {
        setPosX(posX);
        this.prevPosX = this.posX;
        setPosY(posY);
        this.prevPosY = this.posY;
    }

    public float getRadius() {
        return radius;
    }

    public void setPosX(float posXIn) {
        final boolean isRightwards = posXIn > posX;
        this.posX = posXIn;

        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;

            final Rectangle2D collision = getCollision(entity);
            if(collision == null)
                continue;

            if(entity.isImmovable()) {
                final float refX = (float) (isRightwards ?
                        collision.getX() :
                        collision.getMaxX());

                final float dist;
                if(posY >= collision.getY() && posY <= collision.getMaxY())
                    dist = radius - Math.abs(posX - refX);
                else {
                    final float refY = (float) (Math.abs(posY - collision.getY()) < Math.abs(posY - collision.getMaxY()) ?
                            collision.getY() :
                            collision.getMaxY());
                    // (x - xc)^2 + (refY - yc)^2 = r^2
                    // (x - xc)^2 = r^2 - (refY - yc)^2
                    // x - xc = sqrt(r^2 - (refY - yc)^2)
                    // x = sqrt(r^2 - (refY - yc)^2) + xc
                    final float uncenteredX = (float) Math.sqrt(radius * radius - Math.pow(refY - posY, 2));
                    final float x = (isRightwards ? -uncenteredX : uncenteredX) + posX;
                    dist = Math.abs(posX  - x) - Math.abs(posX  - refX);
                }

                this.posX = isRightwards ?
                        posX - dist :
                        posX + dist;
            }
        }

        if(posX - radius < 0)
            posX = radius;
        if(posX + radius > table.getWidth())
            posX = table.getWidth() - radius;
    }

    public void setPosY(float posYIn) {
        final boolean isDownwards = posYIn > posY;
        this.posY = posYIn;

        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;

            final Rectangle2D collision = getCollision(entity);
            if(collision == null)
                continue;

            if(entity.isImmovable()) {
                final float refY = (float) (isDownwards ?
                        collision.getY() :
                        collision.getMaxY());

                final float dist;
                if(posX >= collision.getX() && posX <= collision.getMaxX())
                    dist = radius - Math.abs(posY - refY);
                else {
                    final float refX = (float) (Math.abs(posX - collision.getX()) < Math.abs(posX - collision.getMaxX()) ?
                            collision.getX() :
                            collision.getMaxX());
                    // (refX - xc)^2 + (y - yc)^2 = r^2
                    // (y - yc)^2 = r^2 - (refX - xc)^2
                    // y - yc = sqrt(r^2 - (refX - xc)^2)
                    // y = sqrt(r^2 - (refX - xc)^2) + yc
                    final float uncenteredY = (float) Math.sqrt(radius * radius - Math.pow(refX - posX, 2));
                    final float y = (isDownwards ? -uncenteredY : uncenteredY) + posY;
                    dist = Math.abs(posY - y) - Math.abs(posY - refY);
                }

                this.posY = isDownwards ?
                        posY - dist :
                        posY + dist;
            }
        }

        if(posY - radius < 0)
            posY = radius;
        if(posY + radius > table.getHeight())
            posY = table.getHeight() - radius;
    }

    @Override
    public float getMotionX() {
        return posX - prevPosX;
    }

    @Override
    public float getMotionY() {
        return posY - prevPosY;
    }

    @Override
    public String toString() {
        return "PaddleEntity{" +
                "radius=" + radius +
                ", prevPosX=" + prevPosX +
                ", prevPosY=" + prevPosY +
                "} " + super.toString();
    }
}
