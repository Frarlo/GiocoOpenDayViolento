package gov.ismonnet.game.renderer.swing.renderer;

import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderer;

import javax.inject.Inject;

class EmptyRenderer implements SwingRenderer<Object> {

    @Inject EmptyRenderer() {}

    @Override
    public void render(SwingRenderContext ctx, Object toRender) {
    }
}
