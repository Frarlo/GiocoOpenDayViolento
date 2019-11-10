package gov.ismonnet.bootstrap;

import dagger.Module;
import gov.ismonnet.game.GameComponent;
import gov.ismonnet.netty.client.ClientComponent;
import gov.ismonnet.netty.server.ServerComponent;

@Module(subcomponents = { ServerComponent.class, ClientComponent.class, GameComponent.class })
public abstract class BootstrapModule {
}
