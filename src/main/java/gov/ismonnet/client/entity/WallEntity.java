package gov.ismonnet.client.entity;

import com.google.auto.factory.AutoFactory;
import gov.ismonnet.client.collider.QuadCollider;

import javax.inject.Inject;

@AutoFactory
public class WallEntity extends BaseEntity {

    private final float width;
    private final float height;

    @Inject WallEntity(float startX, float startY, float width, float height) {
        this.collider = new QuadCollider(startX, startY, width, height);

        this.posX = startX;
        this.posY = startY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick() {
        this.motionX = this.motionY = 0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
