package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import dagger.Lazy;
import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.Listener;
import gov.ismonnet.event.listeners.SyncListener;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.packets.PuckPositionPacket;

import javax.inject.Inject;
import java.util.Set;

@AutoFactory
public class PuckEntity extends CircleEntity {

    private static final float MOTION_STEP = 0.1f;
    private static final float MOTION_CAP = 30F;

    private final Table table;

    @Inject PuckEntity(float startX, float startY,
                       float radius,
                       float motionX, float motionY,
                       @Provided Table table,
                       @Provided Lazy<Set<Entity>> collidingEntitiesLazy,
                       @Provided NetService netService) {
        super(startX, startY, radius, collidingEntitiesLazy);

        this.table = table;

        this.posX = startX;
        this.posY = startY;

        this.motionX = motionX;
        this.motionY = motionY;

        netService.registerObj(this);
    }

    @Override
    public void tick() {

        motionX = Math.min(MOTION_CAP, Math.max(motionX, -MOTION_CAP));
        motionY = Math.min(MOTION_CAP, Math.max(motionY, -MOTION_CAP));

        setPosX(getPosX() + motionX);
        motionX = motionX > 0 ?
                Math.max(0, motionX - MOTION_STEP) :
                Math.min(0, motionX + MOTION_STEP);

        setPosY(getPosY() + motionY);
        motionY = motionY > 0 ?
                Math.max(0, motionY - MOTION_STEP) :
                Math.min(0, motionY + MOTION_STEP);
    }

    @Override
    protected void setPosY(float posYIn) {
        super.setPosY(posYIn);

        if(posY - radius < 0)
            posY = radius;
        if(posY + radius > table.getHeight())
            posY = table.getHeight() - radius;
    }

    @Listener
    protected EventListener<PuckPositionPacket> onPuckPos = new SyncListener<>(packet -> {
        this.posX = packet.getPosX();
        this.posY = packet.getPosY();
        this.motionX = packet.getMotionX();
        this.motionY = packet.getMotionY();
    });

    public void reset(float posX, float posY, float motionX, float motionY) {
        this.posX = posX;
        this.posY = posY;
        this.motionX = motionX;
        this.motionY = motionY;
    }

    @Override
    public String toString() {
        return "PuckEntity{} " + super.toString();
    }
}
