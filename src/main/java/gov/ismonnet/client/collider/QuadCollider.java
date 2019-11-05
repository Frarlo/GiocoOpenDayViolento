package gov.ismonnet.client.collider;

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

        collisionBox = new SuppliedRectangle2D();
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
        for(Rectangle2D rect : collider.getCollisionBoxes())
            if(rect.intersects(collisionBox))
                return true;
        return false;
    }

    @Override
    public Collection<Rectangle2D> getCollisionBoxes() {
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

    class SuppliedRectangle2D extends Rectangle2D {

        @Override
        public double getX() {
            return QuadCollider.this.xPos.getAsDouble();
        }

        @Override
        public double getY() {
            return QuadCollider.this.yPos.getAsDouble();
        }

        @Override
        public double getWidth() {
            return QuadCollider.this.width.getAsDouble();
        }

        @Override
        public double getHeight() {
            return QuadCollider.this.height.getAsDouble();
        }

        @Override
        public boolean isEmpty() {
            return (getWidth() <= 0.0f) || (getHeight() <= 0.0f);
        }

        @Override
        public void setRect(double x, double y, double w, double h) {
            throw new UnsupportedOperationException("This quad is read only!");
        }

        @Override
        public void setRect(Rectangle2D r) {
            throw new UnsupportedOperationException("This quad is read only!");
        }

        @Override
        public int outcode(double x, double y) {
            int out = 0;
            if (getWidth() <= 0) {
                out |= OUT_LEFT | OUT_RIGHT;
            } else if (x < getX()) {
                out |= OUT_LEFT;
            } else if (x > getY() + getWidth()) {
                out |= OUT_RIGHT;
            }
            if (getHeight() <= 0) {
                out |= OUT_TOP | OUT_BOTTOM;
            } else if (y < getY()) {
                out |= OUT_TOP;
            } else if (y > getY() + getHeight()) {
                out |= OUT_BOTTOM;
            }
            return out;
        }

        @Override
        public Rectangle2D getBounds2D() {
            return new Double(getX(), getY(), getWidth(), getHeight());
        }

        @Override
        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dest;
            if (r instanceof Float) {
                dest = new Rectangle2D.Float();
            } else {
                dest = new Rectangle2D.Double();
            }
            Rectangle2D.intersect(this, r, dest);
            return dest;
        }

        @Override
        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dest;
            if (r instanceof Float) {
                dest = new Rectangle2D.Float();
            } else {
                dest = new Rectangle2D.Double();
            }
            Rectangle2D.union(this, r, dest);
            return dest;
        }

        @Override
        public String toString() {
            return "SuppliedRectangle2D{" +
                    "x=" + getX() +
                    ", y=" + getY() +
                    ", width=" + getWidth() +
                    ", height=" + getHeight() +
                    "}";
        }
    }
}
