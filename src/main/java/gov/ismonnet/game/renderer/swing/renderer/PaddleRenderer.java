package gov.ismonnet.game.renderer.swing.renderer;

import gov.ismonnet.game.physics.entity.PaddleEntity;
import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderer;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;

class PaddleRenderer implements SwingRenderer<PaddleEntity> {

    private final BufferedImage paddleTexture;

    @Inject PaddleRenderer(@Named("paddle_texture") BufferedImage paddleTexture) {
        this.paddleTexture = paddleTexture;
    }

    @Override
    public void render(SwingRenderContext ctx, PaddleEntity toRender) {
        ctx.drawImage(
                paddleTexture,
                toRender.getPosX() - toRender.getRadius(),
                toRender.getPosY() - toRender.getRadius(),
                toRender.getRadius() * 2F,
                toRender.getRadius() * 2F,
                null);
    }
}
