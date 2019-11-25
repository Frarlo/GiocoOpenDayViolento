package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import dagger.Lazy;
import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.Listener;
import gov.ismonnet.event.listeners.SyncListener;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.packets.GoalPacket;

import javax.inject.Inject;

@AutoFactory
public class GoalEntity extends WallEntity {

    private final Table table;
    private final NetService netService;

    private final Lazy<PuckEntity> lazyPuck;
    private final Lazy<PaddleEntity> lazyPaddle;

    @Inject GoalEntity(float posX, float posY,
                       float width, float height,
                       @Provided Table table,
                       @Provided Lazy<PuckEntity> lazyPuck,
                       @Provided Lazy<PaddleEntity> lazyPaddle,
                       @Provided NetService netService) {
        super(posX, posY, width, height);

        this.table = table;
        this.netService = netService;

        this.lazyPuck = lazyPuck;
        this.lazyPaddle = lazyPaddle;

        netService.registerObj(this);
    }

    @Override
    public void tick() {
        super.tick();

        final PuckEntity puck = lazyPuck.get();
        final PaddleEntity paddle = lazyPaddle.get();

        if(!collidesWith(puck) && puck.getPosX() < 0) {
            netService.sendPacket(new GoalPacket());

            puck.reset(
                    table.getWidth() / 3F * 2F,
                    table.getHeight() / 2F,
                    5, 0);
            paddle.reset(
                    table.getWidth() / 3F,
                    table.getHeight() / 2F);
        }
    }

    @Listener
    protected EventListener<GoalPacket> onGoal = new SyncListener<>(packet -> {
        final Table table = GoalEntity.this.table;
        final PaddleEntity paddle = GoalEntity.this.lazyPaddle.get();

        paddle.reset(
                table.getWidth() / 3F,
                table.getHeight() / 2F);
    });

    @Override
    public String toString() {
        return "GoalEntity{} " + super.toString();
    }
}
