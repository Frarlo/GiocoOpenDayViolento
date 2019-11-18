package gov.ismonnet.game.renderer.swing.screen;

import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderService;
import gov.ismonnet.game.renderer.swing.SwingScreen;
import gov.ismonnet.swing.SwingWindow;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScreen extends BaseScreen {

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 100);

    private final SwingScreen gameScreen;

    @Inject PauseScreen(SwingWindow window,
                        SwingRenderService renderService,
                        @GameScreen.Qualifier SwingScreen gameScreen) {
        super(window, renderService);

        this.gameScreen = gameScreen;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            renderService.setScreen(Type.GAME);
    }

    @Override
    public void render(SwingRenderContext ctx) {
        gameScreen.render(ctx);

        ctx.setColor(TRANSPARENT_BLACK);
        ctx.fillRect(0, 0, getWidth(), getHeight());

        // TODO
    }
}
