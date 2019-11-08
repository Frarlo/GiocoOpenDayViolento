package gov.ismonnet.game.physics.table;

import gov.ismonnet.game.physics.entity.GoalEntity;
import gov.ismonnet.game.physics.entity.PuckEntity;
import gov.ismonnet.game.physics.entity.WallEntity;

public interface Table {

    float getWidth();

    float getHeight();

    float getWallThickness();

    float getGoalHeight();

    WallEntity[] getWalls();

    GoalEntity getGoal();

    PuckEntity getPuck();
}
