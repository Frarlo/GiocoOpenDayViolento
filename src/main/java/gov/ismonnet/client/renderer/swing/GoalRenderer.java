package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.GoalEntity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import java.awt.*;

class GoalRenderer implements Renderer<SwingRenderContext, GoalEntity> {

    @Inject GoalRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, GoalEntity toRender) {
        ctx.setColor(ctx.getBackground());
        ctx.fillRect(
                toRender.getPosX(),
                toRender.getPosY(),
                toRender.getWidth(),
                toRender.getHeight());

        ctx.setColor(Color.white);
        ctx.drawLine(
                (int) toRender.getPosX(),
                (int) toRender.getPosY(),
                (int) toRender.getPosX(),
                (int) (toRender.getPosY() + toRender.getHeight()));
    }
}
