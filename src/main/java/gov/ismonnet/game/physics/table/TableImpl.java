package gov.ismonnet.game.physics.table;

import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.physics.entity.*;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import java.util.*;

class TableImpl implements Table, LifeCycle {

    private final float wallThickness;
    private final float goalHeight;

    private float width;
    private float height;

    private final PhysicsService physicsService;

    private final WallEntityFactory wallFactory;
    private final GoalEntityFactory goalFactory;
    private final PuckEntityFactory puckFactory;

    private WallEntity[] walls;
    private GoalEntity goal;
    private PuckEntity puck;

    @Inject TableImpl(PhysicsService physicsService,
                      WallEntityFactory wallFactory,
                      GoalEntityFactory goalFactory,
                      PuckEntityFactory puckFactory,
                      LifeCycleService lifeCycleService) {

        this.wallThickness = 25;
        // Image ratio is 243/282
        this.width = 729 + wallThickness;
        this.height = 846 + 2 * wallThickness;
        // Goal/height is 128/307
        this.goalHeight = 384;

        this.physicsService = physicsService;

        this.wallFactory = wallFactory;
        this.goalFactory = goalFactory;
        this.puckFactory = puckFactory;

        lifeCycleService.register(this);
    }

    public void start() {
        goal = goalFactory.create(
                0,
                (height - goalHeight) / 2F,
                wallThickness,
                goalHeight);
        walls = new WallEntity[] {
                // Top wall
                wallFactory.create(0, 0, width, wallThickness),
                // Bottom wall
                wallFactory.create(0, height - wallThickness, width, wallThickness),
                // Left wall
                wallFactory.create(0, 0, wallThickness, goal.getPosY()),
                wallFactory.create(0,
                        goal.getPosY() + getGoal().getHeight(),
                        wallThickness,
                        height - goal.getPosY() - goal.getHeight()),
                // Right wall
                wallFactory.create(width - wallThickness, 0, wallThickness, height),
                // Temp left wall
                // TODO: temp
//                wallFactory.create(0, 0, wallThickness, height)
        };

        physicsService.spawnEntity(goal);
        Arrays.stream(walls).forEach(physicsService::spawnEntity);

        // TODO: temp

        puck = puckFactory.create(
                getWidth() / 2F,
                getHeight() / 2F,
                50,
                50, 50);
        physicsService.spawnEntity(puck);
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
    public float getWallThickness() {
        return wallThickness;
    }

    @Override
    public float getGoalHeight() {
        return goalHeight;
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
}
