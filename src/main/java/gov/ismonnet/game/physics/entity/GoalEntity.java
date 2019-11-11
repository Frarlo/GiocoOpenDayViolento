package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.game.physics.table.Table;

import javax.inject.Inject;

@AutoFactory
public class GoalEntity extends WallEntity {

    private final Table table;
    private final PuckEntity puck;

    @Inject GoalEntity(float posX, float posY,
                       float width, float height,
                       @Provided Table table,
                       @Provided PuckEntity puck) {
        super(posX, posY, width, height);

        this.table = table;
        this.puck = puck;
    }

    @Override
    public void tick() {
        super.tick();

        if(!collidesWith(puck) && puck.getPosX() < 0) {
            System.out.println("Scored");
            puck.reset(
                    table.getWidth() / 2F,
                    table.getHeight() / 2F,
                    5, 0);
        }
    }
}
