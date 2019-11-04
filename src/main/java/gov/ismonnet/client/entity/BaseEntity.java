package gov.ismonnet.client.entity;

import gov.ismonnet.client.collider.Collider;
import gov.ismonnet.client.renderer.Renderer;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

public abstract class BaseEntity implements Entity {

    protected Collider collider;

    protected float posX, posY;
    protected float motionX, motionY;

    @Override
    public float getPosX() {
        return posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    @Override
    public float getMotionX() {
        return motionX;
    }

    @Override
    public float getMotionY() {
        return motionY;
    }

    public abstract void tick();

    @Override
    public boolean collidesWith(Collider collider) {
        return collider.collidesWith(collider);
    }

    @Override
    public Collection<Rectangle2D> getCollisionBoxes() {
        return collider.getCollisionBoxes();
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "collider=" + collider +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }

    @Override
    public String toGeogebra() {
        return collider.toGeogebra();
    }
}
