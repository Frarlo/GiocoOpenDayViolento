package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.WallEntity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import java.awt.*;

class WallRenderer implements Renderer<SwingRenderContext, WallEntity> {

    @Inject WallRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, WallEntity toRender) {
        ctx.setColor(Color.yellow);
        ctx.fillRect(
                toRender.getPosX(),
                toRender.getPosY(),
                toRender.getWidth(),
                toRender.getHeight());
    }
}
