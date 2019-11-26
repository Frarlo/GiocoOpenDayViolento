package gov.ismonnet.game.renderer.swing.screen;

import gov.ismonnet.bootstrap.Bootstrap;
import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderService;
import gov.ismonnet.game.renderer.swing.SwingScreen;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.ScaledResolution;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static gov.ismonnet.swing.SwingGraphics.HorizontalAlignment;
import static gov.ismonnet.swing.SwingGraphics.VerticalAlignment;

public class PauseScreen extends BaseScreen {

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 100);

    private final SwingScreen gameScreen;

    private final List<CenteredButton> buttons;

    private int mouseX;
    private int mouseY;

    @Inject PauseScreen(SwingWindow window,
                        SwingRenderService renderService,
                        @GameScreen.Qualifier SwingScreen gameScreen,
                        LifeCycleService gameLifecycle,
                        @Bootstrap LifeCycleService bootstrapLifecycle) {
        super(window, renderService);

        this.gameScreen = gameScreen;

        this.buttons = new ArrayList<>();
        this.buttons.add(new CenteredButton("Riprendi", () -> renderService.setScreen(Type.GAME)));
        this.buttons.add(new CenteredButton("Torna al menu", () -> SneakyThrow.runUnchecked(gameLifecycle::stop)));
        this.buttons.add(new CenteredButton("Esci", () -> SneakyThrow.runUnchecked(bootstrapLifecycle::stop)));
    }

    @Override
    public void onInit(ScaledResolution scaledResolution) {
        buttons.forEach(b -> b.x = scaledResolution.getWidth() / 2F);

        final float bHeight = 27;
        final float gap = 10;
        final float height = (bHeight + gap) * buttons.size();

        final float[] y = { (scaledResolution.getHeight() - height) / 2F };
        buttons.forEach(b -> {
            b.y = y[0];
            y[0] += bHeight + gap;
        });
    }

    @Override
    public void render(SwingRenderContext ctx) {
        gameScreen.render(ctx);

        ctx.setColor(TRANSPARENT_BLACK);
        ctx.fillRect(0, 0, getWidth(), getHeight());

        buttons.forEach(b -> b.render(ctx));
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        buttons.stream()
                .filter(b -> b.isInButton(e.getX(), e.getY()))
                .findFirst()
                .ifPresent(b -> b.onClick.run());
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            renderService.setScreen(Type.GAME);
    }

    @Override
    public void onMouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    class CenteredButton {

        private float x;
        private float y;

        private final String text;
        private final Runnable onClick;

        private FontMetrics fontMetrics;

        CenteredButton(String text, Runnable onClick) {
            this.text = text;
            this.onClick = onClick;
        }

        public void render(SwingRenderContext ctx) {
            ctx.textAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            ctx.textSize(27);
            this.fontMetrics = ctx.getFontMetrics();

            ctx.setColor(!isHovered() ? Color.white : Color.orange);
            ctx.drawString(text, x, y);
        }

        public boolean isHovered() {
            return isInButton(mouseX, mouseY);
        }

        public boolean isInButton(float x, float y) {
            if(fontMetrics == null)
                return false;

            final float width = fontMetrics.stringWidth(text);
            final float height = fontMetrics.getHeight();

            return x >= this.x - width / 2F &&
                    x <= this.x + width / 2F &&
                    y >= this.y - height / 2F &&
                    y <= this.y + height / 2F;
        }
    }
}
