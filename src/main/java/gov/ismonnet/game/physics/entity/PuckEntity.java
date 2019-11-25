package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import dagger.Lazy;
import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.Listener;
import gov.ismonnet.event.listeners.SyncListener;
import gov.ismonnet.game.physics.collider.CircleCollider;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.packets.PuckPositionPacket;

import javax.inject.Inject;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoFactory
public class PuckEntity extends BaseEntity {

    private static final float MOTION_STEP = 0.1f;
    private static final float MOTION_CAP = 25F;

    private static final byte HORIZONTAL_COLLISION_MASK = 0x1;
    private static final byte VERTICAL_COLLISION_MASK = 0x2;

    private final Lazy<Set<Entity>> collidingEntitiesLazy;
    private final Map<Entity, Byte> prevCollisions;

    private final float radius;

    @Inject PuckEntity(float startX, float startY,
                       float radius,
                       float motionX, float motionY,
                       @Provided Lazy<Set<Entity>> collidingEntitiesLazy,
                       @Provided NetService netService) {

        this.collidingEntitiesLazy = collidingEntitiesLazy;
        this.prevCollisions = new HashMap<>();

        this.collider = new CircleCollider(this::getPosX, this::getPosY, () -> radius);

        this.posX = startX;
        this.posY = startY;
        this.radius = radius;

        this.motionX = motionX;
        this.motionY = motionY;

        netService.registerObj(this);
    }

    @Override
    public void tick() {

        motionX = Math.min(MOTION_CAP, Math.max(motionX, -MOTION_CAP));
        motionY = Math.min(MOTION_CAP, Math.max(motionY, -MOTION_CAP));

        // X axis

        final boolean wasMotionXPositive = motionX > 0;
        posX += motionX;

        boolean hasCollidedHorizontally = false;
        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;
            byte mask = prevCollisions.computeIfAbsent(entity, k -> (byte) 0);
            // Check if there is a new collision
            final Rectangle2D collision = getCollision(entity);

            final boolean collides = collision != null;
            final boolean didCollide = (mask & HORIZONTAL_COLLISION_MASK) != 0;

            if(!didCollide && collides) {
                // If first collision, revert motion dir
                if(!hasCollidedHorizontally)
                    motionX = -motionX;
                hasCollidedHorizontally = true;
                // Adjust position so that we don't go over it
//                if(wasMotionXPositive)
                // Add motion if the other entity was also moving
                if(!entity.isImmovable())
                    motionX += entity.getMotionX();
            }
            // Update bit mask
            if(collides)
                mask |= HORIZONTAL_COLLISION_MASK;
            else
                mask &= ~HORIZONTAL_COLLISION_MASK;
            prevCollisions.put(entity, mask);
        }

        motionX = motionX > 0 ?
                Math.max(0, motionX - MOTION_STEP) :
                Math.min(0, motionX + MOTION_STEP);

        // Y Axis

        final boolean wasMotionYPositive = motionY > 0;
        posY += motionY;

        boolean hasCollidedVertically = false;
        for(Entity entity : collidingEntitiesLazy.get()) {
            if(entity == this)
                continue;
            byte mask = prevCollisions.computeIfAbsent(entity, k -> (byte) 0);
            // Check if there is a new collision
            final Rectangle2D collision = getCollision(entity);

            final boolean collides = collision != null;
            final boolean didCollide = (mask & HORIZONTAL_COLLISION_MASK) != 0;

            if(!didCollide && collides) {
                // If first collision, revert motion dir
                if(!hasCollidedVertically)
                    motionY = -motionY;
                hasCollidedVertically = true;
                // Adjust position so that we don't go over it
//                if(wasMotionYPositive)
                // Add motion if the other entity was also moving
                if(!entity.isImmovable())
                    motionY += entity.getMotionY();
            }
            // Update bit mask
            if(collides)
                mask |= VERTICAL_COLLISION_MASK;
            else
                mask &= ~VERTICAL_COLLISION_MASK;
            prevCollisions.put(entity, mask);
        }

        motionY = motionY > 0 ?
                Math.max(0, motionY - MOTION_STEP) :
                Math.min(0, motionY + MOTION_STEP);
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

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "PuckEntity{" +
                "radius=" + radius +
                "} " + super.toString();
    }
}
