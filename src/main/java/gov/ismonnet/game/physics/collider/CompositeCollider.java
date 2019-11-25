package gov.ismonnet.game.physics.collider;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeCollider implements Collider {

    private final List<Collider> colliders;
    private final List<Rectangle2D> collisionBoxes;

    public CompositeCollider(Collection<Collider> colliders) {
        this.colliders = new ArrayList<>(colliders);
        this.collisionBoxes = colliders.stream()
                .map(Collider::getAxisAlignedBBs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return getCollision(collider) != null;
    }

    @Override
    public Rectangle2D getCollision(Collider collider) {
        for(Collider internalCollider : colliders) {
            final Rectangle2D res = internalCollider.getCollision(collider);
            if(res != null)
                return res;
        }
        return null;
    }

    @Override
    public Collection<Rectangle2D> getAxisAlignedBBs() {
        return collisionBoxes;
    }

    @Override
    public String toGeogebra() {
        return "ESEGUI({\"" + colliders.stream()
                .map(Collider::toGeogebra)
                .collect(Collectors.joining("\", \"")) + "\"})";
    }

    @Override
    public String toString() {
        return "CompositeCollider{" +
                "colliders=" + colliders +
                '}';
    }
}
