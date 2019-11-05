package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import java.awt.*;

public class CollisionBoxRenderer implements Renderer<SwingRenderContext, Entity> {

    @Inject CollisionBoxRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, Entity toRender) {
        ctx.setColor(Color.blue);
        toRender.getCollisionBoxes().forEach(c -> ctx.fillRect(
                (int) c.getX(),
                (int) c.getY(),
                (int) c.getWidth(),
                (int) c.getHeight()));
    }
}
