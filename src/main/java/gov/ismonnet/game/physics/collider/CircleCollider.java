package gov.ismonnet.game.physics.collider;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public CircleCollider(float xPos, float yPos, float radius) {
        this(() -> xPos, () -> yPos, () -> radius);
    }

    private void makeCollider() {
        float radius = (float) this.radius.getAsDouble();
        final float diameter = 2F * radius;
        final float radiusStep = diameter / 50;

        final List<Collider> colliders = new ArrayList<>();
        for(int i = 0; (i + 1) * radiusStep < diameter; i++) {
            {
                final float yRelPos = i * radiusStep;
                final float height = radiusStep;
                // Circle equation => pow(x – h, 2) + pow(y – k, 2) = pow(r, 2)
                // where center(h, k), radius is r
                // If we use (0, 0) as center we can substitute (yRelPos + height) as y
                // So pow(x, 2) + pow(y, 2) = pow(r, 2)
                // x = +- sqrt(pow(r, 2) - pow(y, 2))
                final float xRelPos = (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(yRelPos + height, 2));
                final float width = 2 * xRelPos;

                colliders.add(new QuadCollider(
                        () -> xPos.getAsDouble() - xRelPos,
                        () -> yPos.getAsDouble() + yRelPos,
                        () -> width,
                        () -> height));
                colliders.add(new QuadCollider(
                        () -> xPos.getAsDouble() - xRelPos,
                        () -> yPos.getAsDouble() - yRelPos - height,
                        () -> width,
                        () -> height));
            }

            final float xRelPos = i * radiusStep;
            final float width = radiusStep;

            final float yRelPos = (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(xRelPos + width, 2));
            final float height = 2 * yRelPos;

            colliders.add(new QuadCollider(
                    () -> xPos.getAsDouble() + xRelPos,
                    () -> yPos.getAsDouble() - yRelPos,
                    () -> width,
                    () -> height));
            colliders.add(new QuadCollider(
                    () -> xPos.getAsDouble() - xRelPos - width,
                    () -> yPos.getAsDouble() - yRelPos,
                    () -> width,
                    () -> height));
        }

        this.currCollider = new CompositeCollider(colliders);
        this.oldRadius = radius;
    }

    private Collider getCurrCollider() {
        if(oldRadius != (float) radius.getAsDouble())
            makeCollider();
        return currCollider;
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return getCurrCollider().collidesWith(collider);
    }

    @Override
    public Rectangle2D getCollision(Collider collider) {
        return getCurrCollider().getCollision(collider);
    }

    @Override
    public Collection<Rectangle2D> getAxisAlignedBBs() {
        return getCurrCollider().getAxisAlignedBBs();
    }

    @Override
    public String toGeogebra() {
        return getCurrCollider().toGeogebra();
    }

    @Override
    public String toString() {
        return "CircleCollider{" +
                "xPos=" + xPos.getAsDouble() +
                ", yPos=" + yPos.getAsDouble() +
                ", radius=" + radius.getAsDouble() +
                ", actualCollider=" + getCurrCollider() +
                '}';
    }
}
