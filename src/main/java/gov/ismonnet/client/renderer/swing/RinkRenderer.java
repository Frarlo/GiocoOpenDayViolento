package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.rink.Rink;

import javax.inject.Inject;
import java.awt.*;

class RinkRenderer implements Renderer<SwingRenderContext, Rink> {

    @Inject RinkRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, Rink toRender) {
        ctx.setColor(Color.black);
        ctx.fillRect(0, 0, (int) toRender.getWidth(), (int) toRender.getHeight());
    }
}
