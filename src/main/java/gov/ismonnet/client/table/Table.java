package gov.ismonnet.client.table;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.entity.GoalEntity;
import gov.ismonnet.client.entity.PuckEntity;
import gov.ismonnet.client.entity.WallEntity;

import java.util.Collection;

public interface Table {

    float getWidth();

    float getHeight();

    float getWallThickness();

    float getGoalHeight();

    WallEntity[] getWalls();

    GoalEntity getGoal();

    PuckEntity getPuck();

    Collection<Entity> getEntities();

    void spawnEntity(Entity entity);

    void despawnEntity(Entity entity);

    Side getSide();

    void switchSide();

    enum Side { LEFT, RIGHT }
}
