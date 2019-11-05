package gov.ismonnet.client.renderer;

public interface RenderServiceFactory {
    RenderService create(Runnable ticksHandler);
}
