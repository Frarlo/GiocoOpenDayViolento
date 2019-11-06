package gov.ismonnet.client.table;

import gov.ismonnet.client.entity.*;

import javax.inject.Inject;
import java.util.*;

class TableImpl implements Table {

    private static final float WALL_THICKNESS = 10;

    private float width;
    private float height;

    private final List<Entity> entities;
    private final List<Entity> unmodifiableEntities;

    private final WallEntity[] walls;

    @Inject
    TableImpl(WallEntityFactory wallFactory) {

        this.width = 750;
        this.height = 500;

        walls = new WallEntity[] {
                wallFactory.create(0, 0, WALL_THICKNESS, height),
                wallFactory.create(0, 0, width, WALL_THICKNESS),
                wallFactory.create(0, height - WALL_THICKNESS, width, WALL_THICKNESS),
                wallFactory.create(width - WALL_THICKNESS, 0, WALL_THICKNESS, height)
        };

        this.entities = new ArrayList<>();
        Collections.addAll(entities, walls);

        this.unmodifiableEntities = Collections.unmodifiableList(entities);
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
    public Collection<Entity> getEntities() {
        return unmodifiableEntities;
    }

    @Override
    public void spawnEntity(Entity entity) {
        this.entities.add(entity);
    }
}
