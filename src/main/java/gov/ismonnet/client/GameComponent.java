package gov.ismonnet.client;

import dagger.BindsInstance;
import dagger.Component;
import gov.ismonnet.client.entity.EntityModule;
import gov.ismonnet.client.renderer.swing.SwingRendererModule;
import gov.ismonnet.client.table.Table;
import gov.ismonnet.client.table.TableModule;
import gov.ismonnet.lifecycle.EagerSingleton;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
@Component(modules = { ClientModule.class, EntityModule.class, TableModule.class, SwingRendererModule.class })
public interface GameComponent {

    Set<EagerSingleton> eagerSingletons();

    LifeCycleService lifeCycle();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder injectSide(Table.Side side);

        GameComponent build();
    }
}
