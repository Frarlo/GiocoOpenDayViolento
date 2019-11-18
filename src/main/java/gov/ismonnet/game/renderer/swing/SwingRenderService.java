package gov.ismonnet.game.renderer.swing;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.Screen;
import gov.ismonnet.game.util.ScaledResolution;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingWindow;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

@AutoFactory
public class SwingRenderService extends JPanel implements RenderService<SwingScreen>, LifeCycle {

    private Side side;

    private final SwingWindow window;
    private final Color backgroundColor;

    private ScaledResolution scaledResolution;

    private final Map<Screen.Type, Provider<SwingScreen>> typeToScreen;
    private Screen.Type screenType;
    private SwingScreen screen;

    @Inject SwingRenderService(@Provided SwingWindow window,
                               @Provided @BackgroundColor Color backgroundColor,
                               @Provided Side side,
                               @Provided Table table,
                               @Provided Map<Screen.Type, Provider<SwingScreen>> screens,
                               @Provided LifeCycleService lifeCycleService) {
        this.window = window;
        this.backgroundColor = backgroundColor;

        this.side = side;
        this.typeToScreen = screens;

        this.scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scaledResolution = new ScaledResolution(
                        getWidth(),
                        getHeight(),
                        table.getWidth(),
                        table.getHeight());
            }
        });

        lifeCycleService.register(this);
    }

    @Override
    public void start() {
        setScreen(Screen.Type.GAME);
        window.setScreen(this);
    }

    @Override
    public void stop() {
        if(screen != null)
            screen.destroy();
    }

    @Override
    public void setScreen(Screen.Type screen) {

        if(screen == screenType)
            return;

        if(this.screen != null)
            this.screen.destroy();

        this.screenType = screen;
        this.screen = typeToScreen.get(screen).get();
        this.screen.init(scaledResolution);
    }

    @Override
    public Screen.Type getScreenType() {
        return screenType;
    }

    @Override
    public SwingScreen getScreen(Screen.Type type) {
        return typeToScreen.get(type).get();
    }

    @Override
    public SwingScreen getCurrentScreen() {
        return screen;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a 2d graphics component :O");

        final SwingRenderContext ctx = new SwingRenderContext((Graphics2D) g);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setBackground(backgroundColor);
        ctx.clearRect(0, 0, getWidth(), getHeight());

        screen.render(ctx);

        repaint();
    }

    @Override
    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void switchSide() {
        side = (side == Side.LEFT) ? Side.RIGHT : Side.LEFT;
    }
}
