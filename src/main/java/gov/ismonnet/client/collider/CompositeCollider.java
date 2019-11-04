package gov.ismonnet.client.collider;

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
                .map(Collider::getCollisionBoxes)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean collidesWith(Collider collider) {
        for(Collider internalCollider : colliders)
            if(internalCollider.collidesWith(collider))
                return true;
        return false;
    }

    @Override
    public Collection<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }
}
