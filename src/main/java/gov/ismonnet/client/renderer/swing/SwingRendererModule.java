package gov.ismonnet.client.renderer.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.client.entity.DiskEntity;
import gov.ismonnet.client.entity.WallEntity;
import gov.ismonnet.client.renderer.EmptyRenderer;
import gov.ismonnet.client.renderer.RenderContext;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.rink.Rink;

import javax.inject.Singleton;

@Module
public abstract class SwingRendererModule {

    private static final boolean DRAW_COLLISION_BOXES = false;

    @Binds @Singleton
    abstract RenderServiceFactory renderServiceFactory(SwingRenderServiceFactory swingRenderServiceFactory);

    @Binds @Singleton
    abstract Renderer<RenderContext, Object> fallbackRenderer(EmptyRenderer emptyRenderer);

    @Provides @Singleton
    static Renderer collisionRenderer(CollisionBoxRenderer renderer,
                                      EmptyRenderer emptyRenderer) {
        return DRAW_COLLISION_BOXES ? renderer : emptyRenderer;
    }

    @Binds @IntoMap @ClassKey(Rink.class)
    abstract Renderer rinkRenderer(RinkRenderer rinkRenderer);

    @Binds @IntoMap @ClassKey(WallEntity.class)
    abstract Renderer wallRenderer(WallRenderer wallRenderer);

    @Binds @IntoMap @ClassKey(DiskEntity.class)
    abstract Renderer diskRenderer(DiskRenderer diskRenderer);
}
