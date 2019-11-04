package gov.ismonnet.client.renderer;

import gov.ismonnet.client.renderer.RenderService;

public interface RenderServiceFactory {
    RenderService create(Runnable onTick);
}
