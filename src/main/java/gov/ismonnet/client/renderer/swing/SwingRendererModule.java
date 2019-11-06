package gov.ismonnet.client.renderer.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.client.entity.PuckEntity;
import gov.ismonnet.client.renderer.EmptyRenderer;
import gov.ismonnet.client.renderer.RenderContext;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.resource.ResourceService;
import gov.ismonnet.client.table.Table;

import javax.inject.Named;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;

@Module
public abstract class SwingRendererModule {

    private static final boolean DRAW_COLLISION_BOXES = false;

    @Binds @Singleton
    abstract RenderService renderService(SwingRenderService swingRenderService);

    @Binds @Singleton
    abstract Renderer<RenderContext, Object> fallbackRenderer(EmptyRenderer emptyRenderer);

    @Provides @Singleton
    static Renderer axisAlignedBBsRenderer(AxisAlignedBBsRenderer renderer,
                                           EmptyRenderer emptyRenderer) {
        return DRAW_COLLISION_BOXES ? renderer : emptyRenderer;
    }

    @Binds @IntoMap @ClassKey(PuckEntity.class)
    abstract Renderer puckRenderer(PuckRenderer puckRenderer);

    @Provides @Named("puck_texture")
    static BufferedImage puckTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/puck.png");
    }

    @Binds @IntoMap @ClassKey(Table.class)
    abstract Renderer tableRenderer(TableRenderer tableRenderer);

    @Provides @Named("table_texture")
    static BufferedImage tableTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/table.png");
    }

    // Side walls

    @Provides @Named("side_wall_texture")
    static BufferedImage sideWallTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/side_walls/wall.png");
    }

    @Provides @Named("side_corner_texture")
    static BufferedImage sideCornerTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/side_walls/corner.png");
    }

    // Goal wall

    @Provides @Named("goal_external_corner_texture")
    static BufferedImage goalExternalCornerTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/external_corner.png");
    }

    @Provides @Named("goal_wall_texture")
    static BufferedImage goalWallTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/wall.png");
    }

    @Provides @Named("goal_internal_corner_texture")
    static BufferedImage goalInternalCornerTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/internal_corner.png");
    }

    @Provides @Named("goal_goal_texture")
    static BufferedImage goalTexture(ResourceService resourceService) {
        return resourceService.getImageResources("ripped/goal_wall/goal.png");
    }
}
