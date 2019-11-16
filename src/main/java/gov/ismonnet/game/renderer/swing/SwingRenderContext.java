package gov.ismonnet.game.renderer.swing;

import gov.ismonnet.game.renderer.RenderContext;
import gov.ismonnet.swing.SwingGraphics;

import java.awt.*;

class SwingRenderContext extends SwingGraphics implements RenderContext {

    SwingRenderContext(Graphics2D g2d) {
        super(g2d);
    }
}
