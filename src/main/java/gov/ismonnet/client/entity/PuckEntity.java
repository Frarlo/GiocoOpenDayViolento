package gov.ismonnet.client.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.client.table.Table;
import gov.ismonnet.client.collider.CircleCollider;
import gov.ismonnet.client.collider.Collider;

import javax.inject.Inject;

@AutoFactory
public class PuckEntity extends BaseEntity {

    private static final float MOTION_STEP = 0.1f;

    private final Table table;

    private final float radius;

    private final Collider prevPosXCollider;
    private final Collider prevPosYCollider;
    private float prevPosX;
    private float prevPosY;

    @Inject PuckEntity(@Provided Table table,
                       float startX, float startY,
                       float radius,
                       float motionX, float motionY) {
        this.table = table;

        this.collider = new CircleCollider(this::getPosX, this::getPosY, () -> radius);
        this.prevPosXCollider = new CircleCollider(() -> prevPosX, this::getPosY, () -> radius);
        this.prevPosYCollider = new CircleCollider(this::getPosX, () -> prevPosY, () -> radius);

        this.posX = startX;
        this.posY = startY;
        this.radius = radius;

        this.motionX = motionX;
        this.motionY = motionY;
    }

    @Override
    public void tick() {

        prevPosX = posX;
        prevPosY = posY;

        posX += motionX;
        posY += motionY;

        boolean collidesHorizontally = false;
        boolean collidesVertically = false;

        for(WallEntity wall : table.getWalls()) {
            if(!collidesHorizontally && !prevPosXCollider.collidesWith(wall) && collider.collidesWith(wall))
                collidesHorizontally = true;
            if(!collidesVertically && !prevPosYCollider.collidesWith(wall) && collider.collidesWith(wall))
                collidesVertically = true;
            if(collidesHorizontally && collidesVertically)
                break;
        }

        if(collidesHorizontally)
            motionX = -motionX;
        if(collidesVertically)
            motionY = -motionY;

        motionX = motionX > 0 ?
                Math.max(0, motionX - MOTION_STEP) :
                Math.min(0, motionX + MOTION_STEP);
        motionY = motionY > 0 ?
                Math.max(0, motionY - MOTION_STEP) :
                Math.min(0, motionY + MOTION_STEP);
    }

    public void reset(float posX, float posY, float motionX, float motionY) {
        this.posX = this.prevPosX = posX;
        this.posY = this.prevPosY = posY;
        this.motionX = motionX;
        this.motionY = motionY;
    }

    public void addMotion(float motionX, float motionY) {
        this.motionX += motionX;
        this.motionY += motionY;
    }

    public float getRadius() {
        return radius;
    }
}
