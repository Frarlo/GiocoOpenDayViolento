package gov.ismonnet.game.renderer.swing.renderer;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.game.GameSession;
import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.physics.entity.PuckEntity;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.game.renderer.swing.SwingRenderer;
import gov.ismonnet.resource.ResourceService;
import gov.ismonnet.resource.Ripped;

import javax.inject.Named;
import java.awt.image.BufferedImage;

@Module
public abstract class SwingRendererModule {

    private static final boolean DRAW_COLLISION_BOXES = false;

    @Binds @GameSession
    abstract SwingRenderer<Object> fallbackRenderer(EmptyRenderer emptyRenderer);

    @Provides @GameSession
    static SwingRenderer<? super Entity> axisAlignedBBsRenderer(AxisAlignedBBsRenderer renderer,
                                                                EmptyRenderer emptyRenderer) {
        return DRAW_COLLISION_BOXES ? renderer : emptyRenderer;
    }

    @Binds @IntoMap
    @ClassKey(PuckEntity.class)
    abstract SwingRenderer puckRenderer(PuckRenderer puckRenderer);

    @Provides @Named("puck_texture")
    static BufferedImage puckTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/puck.png");
    }

    @Binds @IntoMap @ClassKey(Table.class)
    abstract SwingRenderer tableRenderer(TableRenderer tableRenderer);

    @Provides @Named("table_texture")
    static BufferedImage tableTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/table.png");
    }

    // Side walls

    @Provides @Named("side_wall_texture")
    static BufferedImage sideWallTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/side_walls/wall.png");
    }

    @Provides @Named("side_corner_texture")
    static BufferedImage sideCornerTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/side_walls/corner.png");
    }

    // Goal wall

    @Provides @Named("goal_external_corner_texture")
    static BufferedImage goalExternalCornerTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/external_corner.png");
    }

    @Provides @Named("goal_wall_texture")
    static BufferedImage goalWallTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/wall.png");
    }

    @Provides @Named("goal_internal_corner_texture")
    static BufferedImage goalInternalCornerTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/internal_corner.png");
    }

    @Provides @Named("goal_goal_texture")
    static BufferedImage goalTexture(@Ripped ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/goal.png");
    }
}
