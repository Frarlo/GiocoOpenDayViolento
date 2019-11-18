package gov.ismonnet.game.renderer.swing.renderer;

import gov.ismonnet.game.physics.entity.PuckEntity;
import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderer;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;

class PuckRenderer implements SwingRenderer<PuckEntity> {

    private final BufferedImage puckTexture;

    @Inject PuckRenderer(@Named("puck_texture") BufferedImage puckTexture) {
        this.puckTexture = puckTexture;
    }

    @Override
    public void render(SwingRenderContext ctx, PuckEntity toRender) {
        ctx.drawImage(
                puckTexture,
                toRender.getPosX() - toRender.getRadius(),
                toRender.getPosY() - toRender.getRadius(),
                toRender.getRadius() * 2F,
                toRender.getRadius() * 2F,
                null);
    }
}
