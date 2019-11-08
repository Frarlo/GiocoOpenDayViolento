package gov.ismonnet.game.physics.collider;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

public interface Collider {

    boolean collidesWith(Collider collider);

    Collection<Rectangle2D> getAxisAlignedBBs();

    String toGeogebra();
}
