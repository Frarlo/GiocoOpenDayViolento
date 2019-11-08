package gov.ismonnet.game.physics.table;

import dagger.Binds;
import dagger.Module;
import gov.ismonnet.game.GameSession;

@Module
public abstract class TableModule {

    @Binds @GameSession
    abstract Table table(TableImpl impl);
}
