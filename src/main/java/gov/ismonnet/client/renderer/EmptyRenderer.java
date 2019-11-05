package gov.ismonnet.client.renderer;

import javax.inject.Inject;

public class EmptyRenderer implements Renderer<RenderContext, Object> {

    @Inject EmptyRenderer() {}

    @Override
    public void render(RenderContext ctx, Object toRender) {
    }
}
