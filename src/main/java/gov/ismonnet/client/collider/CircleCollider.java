package gov.ismonnet.client.collider;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.function.DoubleSupplier;

public class CircleCollider implements Collider {

    private final DoubleSupplier xPos;
    private final DoubleSupplier yPos;
    private final DoubleSupplier radius;

    private float oldRadius;
    private Collider currCollider;

    public CircleCollider(DoubleSupplier xPos,
                          DoubleSupplier yPos,
                          DoubleSupplier radius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;

        makeCollider();
    }

    private void makeCollider() {
        this.currCollider = new CompositeCollider(

        );
        this.oldRadius = (float) radius.getAsDouble();
    }

    private Collider getCurrCollider() {
        if(oldRadius != (float) radius.getAsDouble())
            makeCollider();
        return currCollider;
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return false;
    }

    @Override
    public Collection<Rectangle2D> getCollisionBoxes() {
        return null;
    }
}
