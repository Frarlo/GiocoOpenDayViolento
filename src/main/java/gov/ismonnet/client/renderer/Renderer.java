package gov.ismonnet.client.renderer;

import gov.ismonnet.client.entity.Entity;

public interface Renderer<T extends Entity> {

    void render(T entity);
}
