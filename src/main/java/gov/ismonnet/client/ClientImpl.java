package gov.ismonnet.client;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.entity.PuckEntityFactory;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.table.Table;
import gov.ismonnet.client.util.Timer;

import javax.inject.Inject;

public class ClientImpl implements Client {

    private static final int TPS = 64;

    private final RenderServiceFactory renderServiceFactory;
    private final Timer ticksTimer;

    private final Table table;
    private final PuckEntityFactory puckFactory;

    private RenderService renderService;

    @Inject ClientImpl(RenderServiceFactory renderServiceFactory,
                       Table table,
                       PuckEntityFactory puckFactory) {

        this.renderServiceFactory = renderServiceFactory;
        this.ticksTimer = new Timer();

        this.table = table;
        this.puckFactory = puckFactory;
    }

    @Override
    public void start() {
        renderService = renderServiceFactory.create(this::handleTicks);

        table.spawnEntity(puckFactory.create(
                table.getWidth() / 2F,
                table.getHeight() / 2F,
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
        table.getEntities().forEach(Entity::tick);
    }
}
