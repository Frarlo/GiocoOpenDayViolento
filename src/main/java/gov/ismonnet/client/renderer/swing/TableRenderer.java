package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.table.Table;

import javax.inject.Inject;
import java.awt.*;

class TableRenderer implements Renderer<SwingRenderContext, Table> {

    @Inject TableRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, Table toRender) {
        ctx.setColor(Color.black);
        ctx.fillRect(0, 0, toRender.getWidth(), toRender.getHeight());
    }
}
