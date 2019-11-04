package gov.ismonnet.client.entity;

import gov.ismonnet.client.collider.Collider;

public interface Entity extends Collider {

    float getPosX();

    float getPosY();

    void render();

    void tick();
}
