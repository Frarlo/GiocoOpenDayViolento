package gov.ismonnet.client;

import gov.ismonnet.client.entity.DiskEntityFactory;
import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.rink.Rink;
import gov.ismonnet.client.util.Timer;

import javax.inject.Inject;

public class ClientImpl implements Client {

    private static final int TPS = 64;

    private final RenderServiceFactory renderServiceFactory;
    private final Timer ticksTimer;

    private final Rink rink;
    private final DiskEntityFactory diskFactory;

    private RenderService renderService;

    @Inject ClientImpl(RenderServiceFactory renderServiceFactory,
                       Rink rink,
                       DiskEntityFactory diskFactory) {

        this.renderServiceFactory = renderServiceFactory;
        this.ticksTimer = new Timer();

        this.rink = rink;
        this.diskFactory = diskFactory;
    }

    @Override
    public void start() {
        renderService = renderServiceFactory.create(this::handleTicks);

        rink.spawnEntity(diskFactory.create(
                rink.getWidth() / 2F,
                rink.getHeight() / 2F,
                25,
                50, 50));
    }

    @Override
    public void stop() {
        renderService.stop();
        renderService = null;
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
