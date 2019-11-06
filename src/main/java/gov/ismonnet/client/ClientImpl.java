package gov.ismonnet.client;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.table.Table;
import gov.ismonnet.client.util.Timer;

import javax.inject.Inject;

public class ClientImpl implements Client {

    private static final int TPS = 64;

    private final Timer ticksTimer;
    private final Table table;

    @Inject ClientImpl(Table table) {
        this.ticksTimer = new Timer();
        this.table = table;
    }

    @Override
    public void handleTicks() {
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
