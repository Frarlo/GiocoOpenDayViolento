package gov.ismonnet.game.physics.table;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import gov.ismonnet.game.GameSession;
import gov.ismonnet.game.physics.entity.*;

import javax.inject.Named;

@Module
public abstract class TableModule {

    @Binds @GameSession
    abstract Table table(TableImpl impl);

    @Provides @GameSession
    static PuckEntity puck(PuckEntityFactory puckFactory,
                           @Named("spawn_puck") boolean spawnPuck,
                           Table table) {
        return !spawnPuck ?
                puckFactory.create(
                        table.getWidth() + 50 + 10,
                        0,
                        50, 0, 0) :
                puckFactory.create(
                        table.getWidth() / 2F,
                        table.getHeight() / 2F,
                        50, 50, 50);
    }

    @Provides @GameSession
    static GoalEntity goal(GoalEntityFactory goalFactory, Table table) {
        return goalFactory.create(
                0,
                (table.getHeight() - table.getGoalHeight()) / 2F,
                table.getWallThickness(),
                table.getGoalHeight());
    }

    @Provides @GameSession
    static MiddleLineEntity middleLine(MiddleLineEntityFactory middleLineFactory, Table table) {
        return middleLineFactory.create(
                table.getWidth() - 1,
                0,
                1,
                table.getHeight());
    }

    @Provides @IntoSet @GameSession
    static WallEntity topWall(WallEntityFactory wallFactory, Table table) {
        return wallFactory.create(
                0,
                0,
                table.getWidth(),
                table.getWallThickness());
    }

    @Provides @IntoSet @GameSession
    static WallEntity bottomWall(WallEntityFactory wallFactory, Table table) {
        return wallFactory.create(
                0,
                table.getHeight() - table.getWallThickness(),
                table.getWidth(),
                table.getWallThickness());
    }

    @Provides @IntoSet @GameSession
    static WallEntity topLeftWall(WallEntityFactory wallFactory, GoalEntity goal, Table table) {
        return wallFactory.create(
                0,
                0,
                table.getWallThickness(),
                goal.getPosY());
    }

    @Provides @IntoSet @GameSession
    static WallEntity bottomLeftWall(WallEntityFactory wallFactory, GoalEntity goal, Table table) {
        return wallFactory.create(
                0,
                goal.getPosY() + table.getGoalHeight(),
                table.getWallThickness(),
                table.getHeight() - (goal.getPosY() + goal.getHeight()));
    }

    // TODO: temp

//    @Provides @IntoSet @GameSession
//    static WallEntity rightWall(WallEntityFactory wallFactory, Table table) {
//        return wallFactory.create(
//                table.getWidth() - table.getWallThickness(),
//                0,
//                table.getWallThickness(),
//                table.getHeight());
//    }

    // TODO: temp

    @Provides @IntoSet @GameSession
    static WallEntity goalWall(WallEntityFactory wallFactory, Table table) {
        return wallFactory.create(
                0,
                0,
                table.getWallThickness(),
                table.getHeight());
    }
}
