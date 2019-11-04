package gov.ismonnet.client.rink;

import gov.ismonnet.client.entity.DiskEntity;
import gov.ismonnet.client.entity.DiskEntityFactory;
import gov.ismonnet.client.entity.WallEntity;
import gov.ismonnet.client.entity.WallEntityFactory;

import javax.inject.Inject;

class RinkImpl implements Rink {

    private final DiskEntity diskEntity;
    private final WallEntity[] walls;

    @Inject RinkImpl(DiskEntityFactory diskFactory,
                     WallEntityFactory wallFactory) {

        diskEntity = diskFactory.create(500, 500, 10, 20, 20);
        walls = new WallEntity[] {
                wallFactory.create(0, 0, 1, 500),
                wallFactory.create(0, 0, 500, 1),
                wallFactory.create(0, 499, 500, 1),
                wallFactory.create(499, 0, 1, 500)
        };
    }

    @Override
    public DiskEntity getDisk() {
        return diskEntity;
    }

    @Override
    public WallEntity[] getWalls() {
        return walls;
    }
}
