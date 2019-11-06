package gov.ismonnet.client.table;

import gov.ismonnet.client.entity.*;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import java.util.*;

class TableImpl implements Table, LifeCycle {

    private static final float WALL_THICKNESS = 10;
    private static final float GOAL_WALL_RATIO = 1F / 6F;

    private float width;
    private float height;
    private Side side;

    private final WallEntityFactory wallFactory;
    private final GoalEntityFactory goalFactory;
    private final PuckEntityFactory puckFactory;

    private final List<Entity> entities;
    private final List<Entity> unmodifiableEntities;

    private WallEntity[] walls;
    private GoalEntity goal;
    private PuckEntity puck;

    @Inject TableImpl(Side side,
                      WallEntityFactory wallFactory,
                      GoalEntityFactory goalFactory,
                      PuckEntityFactory puckFactory,
                      LifeCycleService lifeCycleService) {

        this.width = 750;
        this.height = 500;
        this.side = side;

        this.wallFactory = wallFactory;
        this.goalFactory = goalFactory;
        this.puckFactory = puckFactory;

        this.entities = new ArrayList<>();
        this.unmodifiableEntities = Collections.unmodifiableList(entities);

        lifeCycleService.register(this);
    }

    public void start() {
        goal = goalFactory.create(
                0,
                (height - height * GOAL_WALL_RATIO) / 2F,
                WALL_THICKNESS + 2,
                height * GOAL_WALL_RATIO);
        walls = new WallEntity[] {
                // Top wall
                wallFactory.create(0, 0, width, WALL_THICKNESS),
                // Bottom wall
                wallFactory.create(0, height - WALL_THICKNESS, width, WALL_THICKNESS),
                // Left wall
                wallFactory.create(0, 0, WALL_THICKNESS, goal.getPosY()),
                wallFactory.create(0,
                        goal.getPosY() + getGoal().getHeight(),
                        WALL_THICKNESS,
                        height - goal.getPosY() - goal.getHeight()),
                // Right wall
                wallFactory.create(width - WALL_THICKNESS, 0, WALL_THICKNESS, height)
        };

        spawnEntity(goal);
        Arrays.stream(walls).forEach(this::spawnEntity);

        // TODO: temp

        puck = puckFactory.create(
                getWidth() / 2F,
                getHeight() / 2F,
                getHeight() / 24F,
                50, 0);
        spawnEntity(puck);
    }

    @Override
    public void stop() {
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public WallEntity[] getWalls() {
        return walls;
    }

    @Override
    public GoalEntity getGoal() {
        return goal;
    }

    @Override
    public PuckEntity getPuck() {
        return puck;
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

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void switchSide() {
        side = (side == Side.LEFT) ? Side.RIGHT : Side.LEFT;
    }
}
