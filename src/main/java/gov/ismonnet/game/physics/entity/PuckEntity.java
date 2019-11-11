package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.Listener;
import gov.ismonnet.event.listeners.SyncListener;
import gov.ismonnet.game.physics.collider.CircleCollider;
import gov.ismonnet.game.physics.collider.Collider;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.packets.PuckPositionPacket;

import javax.inject.Inject;
import java.util.Set;

@AutoFactory
public class PuckEntity extends BaseEntity {

    private static final float MOTION_STEP = 0.1f;

    private final Set<WallEntity> walls;

    private final float radius;

    private final Collider prevPosXCollider;
    private final Collider prevPosYCollider;
    private float prevPosX;
    private float prevPosY;

    @Inject PuckEntity(float startX, float startY,
                       float radius,
                       float motionX, float motionY,
                       @Provided Set<WallEntity> walls,
                       @Provided NetService netService) {

        this.walls = walls;

        this.collider = new CircleCollider(this::getPosX, this::getPosY, () -> radius);
        this.prevPosXCollider = new CircleCollider(() -> prevPosX, this::getPosY, () -> radius);
        this.prevPosYCollider = new CircleCollider(this::getPosX, () -> prevPosY, () -> radius);

        this.posX = startX;
        this.posY = startY;
        this.radius = radius;

        this.motionX = motionX;
        this.motionY = motionY;

        netService.registerObj(this);
    }

    @Override
    public void tick() {

        prevPosX = posX;
        prevPosY = posY;

        posX += motionX;
        posY += motionY;

        boolean collidesHorizontally = false;
        boolean collidesVertically = false;

        for(WallEntity wall : walls) {
            if(!collidesHorizontally && !prevPosXCollider.collidesWith(wall) && collidesWith(wall))
                collidesHorizontally = true;
            if(!collidesVertically && !prevPosYCollider.collidesWith(wall) && collidesWith(wall))
                collidesVertically = true;
            if(collidesHorizontally && collidesVertically)
                break;
        }

        if(collidesHorizontally)
            motionX = -motionX;
        if(collidesVertically)
            motionY = -motionY;

//        motionX = motionX > 0 ?
//                Math.max(0, motionX - MOTION_STEP) :
//                Math.min(0, motionX + MOTION_STEP);
//        motionY = motionY > 0 ?
//                Math.max(0, motionY - MOTION_STEP) :
//                Math.min(0, motionY + MOTION_STEP);
    }

    @Listener
    protected EventListener<PuckPositionPacket> onPuckPos = new SyncListener<>(packet -> {
        this.posX = packet.getPosX();
        this.posY = packet.getPosY();
        this.motionX = packet.getMotionX();
        this.motionY = packet.getMotionY();
    });

    public void reset(float posX, float posY, float motionX, float motionY) {
        this.posX = this.prevPosX = posX;
        this.posY = this.prevPosY = posY;
        this.motionX = motionX;
        this.motionY = motionY;
    }

    public float getRadius() {
        return radius;
    }
}
