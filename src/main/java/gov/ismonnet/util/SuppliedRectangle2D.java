package gov.ismonnet.util;

import java.awt.geom.Rectangle2D;
import java.util.function.DoubleSupplier;

public class SuppliedRectangle2D extends Rectangle2D {

    private final DoubleSupplier xSupplier;
    private final DoubleSupplier ySupplier;
    private final DoubleSupplier widthSupplier;
    private final DoubleSupplier heightSupplier;

    public SuppliedRectangle2D(DoubleSupplier xSupplier,
                               DoubleSupplier ySupplier,
                               DoubleSupplier widthSupplier,
                               DoubleSupplier heightSupplier) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.widthSupplier = widthSupplier;
        this.heightSupplier = heightSupplier;
    }

    @Override
    public double getX() {
        return xSupplier.getAsDouble();
    }

    @Override
    public double getY() {
        return ySupplier.getAsDouble();
    }

    @Override
    public double getWidth() {
        return widthSupplier.getAsDouble();
    }

    @Override
    public double getHeight() {
        return heightSupplier.getAsDouble();
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
