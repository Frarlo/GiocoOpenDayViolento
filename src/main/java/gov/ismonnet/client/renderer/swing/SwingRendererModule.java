package gov.ismonnet.client.renderer.swing;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.client.entity.DiskEntity;
import gov.ismonnet.client.entity.WallEntity;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.rink.Rink;

@Module
public abstract class SwingRendererModule {

    @Binds abstract RenderServiceFactory renderServiceFactory(SwingRenderServiceFactory swingRenderServiceFactory);

    @Binds @IntoMap @ClassKey(Rink.class)
    abstract Renderer rinkRenderer(RinkRenderer rinkRenderer);

    @Binds @IntoMap @ClassKey(WallEntity.class)
    abstract Renderer wallRenderer(WallRenderer wallRenderer);

    @Binds @IntoMap @ClassKey(DiskEntity.class)
    abstract Renderer diskRenderer(DiskRenderer diskRenderer);
}
