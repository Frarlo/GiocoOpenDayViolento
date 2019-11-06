package gov.ismonnet.client;

import dagger.Component;
import gov.ismonnet.client.entity.EntityModule;
import gov.ismonnet.client.renderer.swing.SwingRendererModule;
import gov.ismonnet.client.table.TableModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = { ClientModule.class, EntityModule.class, TableModule.class, SwingRendererModule.class })
public interface GameComponent {

    Client getClient();

    @Component.Builder
    interface Builder {
        GameComponent build();
    }
}
