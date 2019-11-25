package gov.ismonnet.game.physics;

import gov.ismonnet.game.physics.entity.Entity;

import java.util.Collection;

public interface PhysicsService {

    void handleTicks();

    void handleMouse(float motionX, float motionY);

    Collection<Entity> getEntities();

    void spawnEntity(Entity entity);

    void despawnEntity(Entity entity);
}
