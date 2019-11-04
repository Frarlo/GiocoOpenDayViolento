package gov.ismonnet.client.renderer;

public interface Renderer<T extends RenderContext, R> {

    void render(T ctx, R toRender);
}
