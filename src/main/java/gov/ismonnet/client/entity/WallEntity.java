package gov.ismonnet.client.entity;

import com.google.auto.factory.AutoFactory;
import gov.ismonnet.client.collider.QuadCollider;

import javax.inject.Inject;

@AutoFactory
public class WallEntity extends BaseEntity {

    @Inject WallEntity(float startX, float startY, float width, float height) {
        this.collider = new QuadCollider(startX, startY, width, height);

        this.posX = startX + width / 2F;
        this.posX = startY + height / 2F;
    }

    @Override
    public void tick() {
        this.motionX = 0;
        this.motionY = 0;
    }
}
