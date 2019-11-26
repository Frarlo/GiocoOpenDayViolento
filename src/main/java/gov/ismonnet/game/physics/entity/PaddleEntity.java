package gov.ismonnet.game.physics.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import dagger.Lazy;
import gov.ismonnet.game.physics.table.Table;

import javax.inject.Inject;
import java.util.Set;

@AutoFactory
public class PaddleEntity extends CircleEntity {

    private final Table table;

    private float prevPosX;
    private float prevPosY;

    @Inject PaddleEntity(float startX, float startY,
                         float radius,
                         @Provided Table table,
                         @Provided Lazy<Set<Entity>> collidingEntitiesLazy) {
        super(startX, startY, radius, collidingEntitiesLazy);

        this.table = table;
    }

    @Override
    public void tick() {
        this.prevPosX = posX;
        this.prevPosY = posY;
    }

    @Override
    public void setPosX(float posXIn) {
        super.setPosX(posXIn);

        if(posX - radius < 0)
            posX = radius;
        if(posX + radius > table.getWidth())
            posX = table.getWidth() - radius;
    }

    @Override
    public void setPosY(float posYIn) {
        super.setPosY(posYIn);

        if(posY - radius < 0)
            posY = radius;
        if(posY + radius > table.getHeight())
            posY = table.getHeight() - radius;
    }

    @Override
    public void setPos(float posXIn, float posYIn) {
        super.setPos(posXIn, posYIn);

        if(posX - radius < 0)
            posX = radius;
        if(posX + radius > table.getWidth())
            posX = table.getWidth() - radius;

        if(posY - radius < 0)
            posY = radius;
        if(posY + radius > table.getHeight())
            posY = table.getHeight() - radius;
    }

    public void reset(float posX, float posY) {
        this.prevPosX = this.posX = posX;
        this.prevPosY = this.posY = posY;
    }

    @Override
    public float getMotionX() {
        return posX - prevPosX;
    }

    @Override
    public float getMotionY() {
        return posY - prevPosY;
    }

    @Override
    public String toString() {
        return "PaddleEntity{" +
                "prevPosX=" + prevPosX +
                ", prevPosY=" + prevPosY +
                "} " + super.toString();
    }
}
