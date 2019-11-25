package gov.ismonnet.game.physics.collider;

import gov.ismonnet.util.SuppliedRectangle2D;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleSupplier;

public class QuadCollider implements Collider {

    private final DoubleSupplier xPos;
    private final DoubleSupplier yPos;
    private final DoubleSupplier width;
    private final DoubleSupplier height;

    private final Rectangle2D collisionBox;
    private final List<Rectangle2D> collisionBoxList;

    public QuadCollider(DoubleSupplier xPos,
                        DoubleSupplier yPos,
                        DoubleSupplier width,
                        DoubleSupplier height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        collisionBox = new SuppliedRectangle2D(xPos, yPos, width, height);
        collisionBoxList = Collections.singletonList(collisionBox);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public QuadCollider(DoubleSupplier xPos,
                        DoubleSupplier yPos,
                        DoubleSupplier width) {
        this(xPos, yPos, width, width);
    }

    public QuadCollider(float xPos,
                        float yPos,
                        float width,
                        float height) {
        this(() -> xPos, () -> yPos, () -> width, () -> height);
    }

    public QuadCollider(float xPos,
                        float yPos,
                        float width) {
        this(() -> xPos, () -> yPos, () -> width);
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return getCollision(collider) != null;
    }

    @Override
    public Rectangle2D getCollision(Collider collider) {
        for(Rectangle2D rect : collider.getAxisAlignedBBs())
            if(rect.intersects(collisionBox))
                return rect;
        return null;
    }

    @Override
    public Collection<Rectangle2D> getAxisAlignedBBs() {
        return collisionBoxList;
    }

    @Override
    public String toGeogebra() {
        return "Polygon(" +
                "(" +
                xPos.getAsDouble() + "," +
                yPos.getAsDouble() +
                "), " +
                "(" +
                (xPos.getAsDouble() + width.getAsDouble()) + "," +
                yPos.getAsDouble() +
                "), " +
                "(" +
                (xPos.getAsDouble() + width.getAsDouble()) + "," +
                (yPos.getAsDouble() + height.getAsDouble()) +
                "), " +
                "(" +
                xPos.getAsDouble() + "," +
                (yPos.getAsDouble() + height.getAsDouble()) +
                ")" +
                ")";
    }

    @Override
    public String toString() {
        return "QuadCollider{" +
                "xPos=" + xPos.getAsDouble() +
                ", yPos=" + yPos.getAsDouble() +
                ", width=" + width.getAsDouble() +
                ", height=" + height.getAsDouble() +
                ", collisionBox=" + collisionBox +
                '}';
    }
}
