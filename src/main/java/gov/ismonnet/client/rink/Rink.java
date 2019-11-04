package gov.ismonnet.client.rink;

import gov.ismonnet.client.entity.DiskEntity;
import gov.ismonnet.client.entity.WallEntity;

public interface Rink {

    DiskEntity getDisk();

    WallEntity[] getWalls();
}
