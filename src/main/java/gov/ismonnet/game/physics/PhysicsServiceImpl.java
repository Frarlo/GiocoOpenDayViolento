package gov.ismonnet.game.physics;

import gov.ismonnet.game.physics.entity.*;
import gov.ismonnet.util.Timer;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

class PhysicsServiceImpl implements PhysicsService, LifeCycle {

    private static final int TPS = 64;

    private final Timer ticksTimer;

    private final List<Entity> entities;
    private final List<Entity> unmodifiableEntities;

    // Break cyclic dependencies
    private final Provider<Set<WallEntity>> walls;
    private final Provider<GoalEntity> goal;
    private final Provider<MiddleLineEntity> middleLine;
    private final Provider<PuckEntity> puck;
    private final Provider<PaddleEntity> paddle;

    @Inject PhysicsServiceImpl(Provider<Set<WallEntity>> walls,
                               Provider<GoalEntity> goal,
                               Provider<MiddleLineEntity> middleLine,
                               Provider<PuckEntity> puck,
                               Provider<PaddleEntity> paddle,
                               LifeCycleService lifeCycleService) {
        this.ticksTimer = new Timer();

        this.entities = new ArrayList<>();
        this.unmodifiableEntities = Collections.unmodifiableList(entities);

        this.walls = walls;
        this.goal = goal;
        this.middleLine = middleLine;
        this.puck = puck;
        this.paddle = paddle;

        lifeCycleService.register(this);
    }

    @Override
    public void start() {
        walls.get().forEach(this::spawnEntity);
        spawnEntity(goal.get());
        spawnEntity(middleLine.get());
        spawnEntity(puck.get());
        spawnEntity(paddle.get());
    }

    @Override
    public void stop() {
    }

    @Override
    public void handleTicks() {
        final long ticks = ticksTimer.getTimePassed() / (1000 / TPS);
        for(int i = 0; i < ticks; i++)
            onTick();

        if(ticks > 0)
            ticksTimer.reset();
    }

    @Override
    public void handleMouse(float motionX, float motionY) {
        final PaddleEntity paddle = this.paddle.get();

        paddle.setPosX(paddle.getPosX() + motionX);
        paddle.setPosY(paddle.getPosY() + motionY);
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
