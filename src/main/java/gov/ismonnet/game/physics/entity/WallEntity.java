package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import gov.ismonnet.game.physics.collider.QuadCollider;

import javax.inject.Inject;

@AutoFactory
public class WallEntity extends BaseEntity {

    private final float width;
    private final float height;

    @Inject WallEntity(float posX, float posY, float width, float height) {
        this.collider = new QuadCollider(posX, posY, width, height);

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.isImmovable = true;
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

    @Override
    public String toString() {
        return "WallEntity{" +
                "width=" + width +
                ", height=" + height +
                "} " + super.toString();
    }
}
