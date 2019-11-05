package gov.ismonnet.client;

import gov.ismonnet.client.entity.DiskEntityFactory;
import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.rink.Rink;
import gov.ismonnet.client.util.Timer;

import javax.inject.Inject;

public class ClientImpl implements Client {

    private static final int TPS = 64;

    private final Rink rink;
    private final Timer ticksTimer;

    @Inject ClientImpl(RenderServiceFactory factory,
                       Rink rink,
                       DiskEntityFactory diskFactory) {

        this.rink = rink;
        this.ticksTimer = new Timer();

        rink.spawnEntity(diskFactory.create(
                rink.getWidth() / 2F,
                rink.getHeight() / 2F,
                25,
                50, 50));
        factory.create(this::handleTicks);
    }

    private void handleTicks() {
        final long ticks = ticksTimer.getTimePassed() / (1000 / TPS);
        for(int i = 0; i < ticks; i++)
            onTick();

        if(ticks > 0)
            ticksTimer.reset();
    }

    private void onTick() {
        rink.getEntities().forEach(Entity::tick);
    }
}
