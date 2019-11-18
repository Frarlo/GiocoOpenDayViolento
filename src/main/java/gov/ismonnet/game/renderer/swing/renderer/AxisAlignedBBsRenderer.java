package gov.ismonnet.game.renderer.swing.renderer;

import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderer;

import javax.inject.Inject;
import java.awt.*;

class AxisAlignedBBsRenderer implements SwingRenderer<Entity> {

    @Inject AxisAlignedBBsRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, Entity toRender) {
        toRender.getAxisAlignedBBs().forEach(c -> ctx.fillBorderedRect(
                (int) c.getX(),
                (int) c.getY(),
                (int) c.getWidth(),
                (int) c.getHeight(),
                Color.white, Color.blue));
    }
}
