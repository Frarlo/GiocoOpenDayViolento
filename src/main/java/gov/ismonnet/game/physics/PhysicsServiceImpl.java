package gov.ismonnet.game.physics;

import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.util.Timer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class PhysicsServiceImpl implements PhysicsService {

    private static final int TPS = 64;

    private final Timer ticksTimer;

    private final List<Entity> entities;
    private final List<Entity> unmodifiableEntities;

    @Inject PhysicsServiceImpl() {
        this.ticksTimer = new Timer();

        this.entities = new ArrayList<>();
        this.unmodifiableEntities = Collections.unmodifiableList(entities);
    }

    @Override
    public void handleTicks() {
        final long ticks = ticksTimer.getTimePassed() / (1000 / TPS);
        for(int i = 0; i < ticks; i++)
            onTick();

        if(ticks > 0)
            ticksTimer.reset();
    }

    private void onTick() {
        entities.forEach(Entity::tick);
    }

    @Override
    public Collection<Entity> getEntities() {
        return unmodifiableEntities;
    }

    @Override
    public void spawnEntity(Entity entity) {
        this.entities.add(entity);
    }

    @Override
    public void despawnEntity(Entity entity) {
        this.entities.remove(entity);
    }
}
