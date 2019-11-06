package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.PuckEntity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import java.awt.*;

class PuckRenderer implements Renderer<SwingRenderContext, PuckEntity> {

    @Inject PuckRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, PuckEntity toRender) {
        ctx.fillBorderedCircle(
                toRender.getPosX(),
                toRender.getPosY(),
                toRender.getRadius(),
                Color.black, Color.red);
    }
}
