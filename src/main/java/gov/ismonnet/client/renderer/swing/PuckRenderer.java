package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.entity.PuckEntity;
import gov.ismonnet.client.renderer.Renderer;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;

class PuckRenderer implements Renderer<SwingRenderContext, PuckEntity> {

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
