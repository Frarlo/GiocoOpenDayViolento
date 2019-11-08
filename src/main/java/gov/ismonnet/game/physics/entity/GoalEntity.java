package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.physics.collider.QuadCollider;
import gov.ismonnet.game.physics.table.Table;

import javax.inject.Inject;

@AutoFactory
public class GoalEntity extends BaseEntity {

    private final Table table;

    private final float width;
    private final float height;

    private boolean wasPuckScoring;

    @Inject GoalEntity(@Provided PhysicsService physicsService,
                       @Provided Table table,
                       float startX, float startY,
                       float width, float height) {
        this.table = table;

        this.collider = new QuadCollider(startX, startY, width, height);

        this.posX = startX;
        this.posY = startY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick() {
        motionX = motionY = 0;

        final PuckEntity puck = table.getPuck();
        if(puck == null) {
            wasPuckScoring = false;
            return;
        }

        final boolean isPuckScoring = table.getGoal().collidesWith(puck);

        if(wasPuckScoring && !isPuckScoring && puck.getPosX() < 0) {
            System.out.println("Scored");
            puck.reset(
                    table.getWidth() / 2F,
                    table.getHeight() / 2F,
                    5, 0);
        }

        wasPuckScoring = isPuckScoring;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
