package gov.ismonnet.client.rink;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.entity.WallEntity;

import java.util.Collection;

public interface Rink {

    float getWidth();

    float getHeight();

    WallEntity[] getWalls();

    Collection<Entity> getEntities();

    void spawnEntity(Entity entity);
}
