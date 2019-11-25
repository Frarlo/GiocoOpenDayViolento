package gov.ismonnet.game.physics;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoSet;
import gov.ismonnet.game.GameSession;
import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.physics.entity.PaddleEntity;
import gov.ismonnet.game.physics.entity.PuckEntity;
import gov.ismonnet.game.physics.entity.WallEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Module
public abstract class PhysicsModule {

    @Binds @GameSession
    abstract PhysicsService physicsService(PhysicsServiceImpl impl);

    // Colliding entities

    @Provides @GameSession @ElementsIntoSet
    static Set<Entity> wallsInCollidingEntities(Set<WallEntity> wallEntities) {
        return wallEntities.stream()
                .map(Entity.class::cast)
                .collect(Collectors.toSet());
    }

    @Provides @GameSession @IntoSet
    static Entity paddleInCollidingEntities(PaddleEntity paddle) {
        return paddle;
    }

    @Provides @GameSession @IntoSet
    static Entity puckInCollidingEntities(PuckEntity puck) {
        return puck;
    }
}
