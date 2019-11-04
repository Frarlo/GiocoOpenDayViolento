package gov.ismonnet.client.entity;

import gov.ismonnet.client.collider.Collider;
import gov.ismonnet.client.renderer.Renderer;

public abstract class BaseEntity<T extends BaseEntity> implements Entity {

    protected final Collider collider;
    protected final Renderer<T> renderer;

    protected float posX, posY;

    protected BaseEntity(Collider collider, Renderer<T> renderer) {
        this.collider = collider;
        this.renderer = renderer;
    }

    @Override
    public float getPosX() {
        return posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render() {
        renderer.render((T) this);
    }

    public abstract void tick();

    @Override
    public boolean collidesWith(Collider collider) {
        return collider.collidesWith(collider);
    }
}
