package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import java.awt.*;

public class AxisAlignedBBsRenderer implements Renderer<SwingRenderContext, Entity> {

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
