package gov.ismonnet.game.physics.entity;

import gov.ismonnet.game.physics.collider.Collider;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

public abstract class BaseEntity implements Entity {

    protected Collider collider;

    protected float posX, posY;

    @Override
    public float getPosX() {
        return posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    public abstract void tick();

    @Override
    public boolean collidesWith(Collider collider) {
        return this.collider.collidesWith(collider);
    }

    @Override
    public Rectangle2D getCollision(Collider collider) {
        return this.collider.getCollision(collider);
    }

    @Override
    public Collection<Rectangle2D> getAxisAlignedBBs() {
        return collider.getAxisAlignedBBs();
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
