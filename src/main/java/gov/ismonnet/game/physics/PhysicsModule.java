package gov.ismonnet.game.physics;

import dagger.Binds;
import dagger.Module;
import gov.ismonnet.game.GameSession;

@Module
public abstract class PhysicsModule {

    @Binds @GameSession
    abstract PhysicsService physicsService(PhysicsServiceImpl impl);
}
