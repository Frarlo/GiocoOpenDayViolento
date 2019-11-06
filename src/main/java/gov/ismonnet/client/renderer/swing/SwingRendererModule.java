package gov.ismonnet.client.renderer.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.client.entity.GoalEntity;
import gov.ismonnet.client.entity.PuckEntity;
import gov.ismonnet.client.entity.WallEntity;
import gov.ismonnet.client.renderer.EmptyRenderer;
import gov.ismonnet.client.renderer.RenderContext;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.table.Table;

import javax.inject.Singleton;

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

    @Binds @IntoMap @ClassKey(Table.class)
    abstract Renderer tableRenderer(TableRenderer tableRenderer);

    @Binds @IntoMap @ClassKey(WallEntity.class)
    abstract Renderer wallRenderer(WallRenderer wallRenderer);

    @Binds @IntoMap @ClassKey(GoalEntity.class)
    abstract Renderer goalRenderer(GoalRenderer wallRenderer);

    @Binds @IntoMap @ClassKey(PuckEntity.class)
    abstract Renderer puckRenderer(PuckRenderer puckRenderer);
}
