package gov.ismonnet.game.physics.entity;

import gov.ismonnet.game.physics.collider.Collider;

public interface Entity extends Collider {

    float getPosX();

    float getPosY();

    void tick();
}
