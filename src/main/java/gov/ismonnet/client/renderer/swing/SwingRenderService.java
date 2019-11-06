package gov.ismonnet.client.renderer.swing;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.client.Client;
import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.RenderContext;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.table.Table;
import gov.ismonnet.client.util.ScaledResolution;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AutoFactory(implementing = RenderServiceFactory.class)
public class SwingRenderService extends JPanel implements RenderService {

    private static final Color BACKGROUND_COLOR = new Color(33, 33, 33);

    private final JFrame frame;
    private ScaledResolution scaledResolution;

    private final Table table;

    private final Map<Class, Renderer> renderers;
    private final Renderer<RenderContext, Object> fallbackRenderer;
    private final Renderer<SwingRenderContext, Entity> axisAlignedBBsRenderer;

    private final Runnable ticksHandler;

    private volatile boolean stopClient = true;

    @SuppressWarnings("unchecked")
    @Inject SwingRenderService(@Provided Client client,
                               @Provided Table table,
                               @Provided Map<Class<?>, Renderer> renderers,
                               @Provided Renderer<RenderContext, Object> fallbackRenderer,
                               @Provided Renderer axisAlignedBBsRenderer,
                               Runnable ticksHandler) {
        this.table = table;

        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));
        this.fallbackRenderer = fallbackRenderer;
        this.axisAlignedBBsRenderer = axisAlignedBBsRenderer;

        this.ticksHandler = ticksHandler;

        // Swing swong

        this.frame = new JFrame("SwingGame") {
            @Override
            public void dispose() {
                if(stopClient)
                    client.stop();
                super.dispose();
            }
        };
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize((int) Math.ceil(table.getWidth()), (int) Math.ceil(table.getHeight()));
        this.frame.setResizable(true);
        this.frame.setLocationRelativeTo(null);
        this.frame.setContentPane(this);

        this.scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        addComponentListener(new ResizeHandler());

        this.frame.setVisible(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a 2d graphics component :O");

        ticksHandler.run();

        final SwingRenderContext ctx = new SwingRenderContext((Graphics2D) g);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setBackground(BACKGROUND_COLOR);
        ctx.clearRect(0, 0, getWidth(), getHeight());

        setupScaling(ctx, true);

        renderers.getOrDefault(Table.class, fallbackRenderer).render(ctx, table);
        table.getEntities().forEach(entity -> {
            renderers.getOrDefault(entity.getClass(), fallbackRenderer).render(ctx, entity);
            axisAlignedBBsRenderer.render(ctx, entity);
        });

        setupScaling(ctx, false);

        repaint();
    }

    private void setupScaling(SwingRenderContext ctx, boolean enable) {
        if (enable) {
            ctx.translate(scaledResolution.getWidthDifference() / 2, scaledResolution.getHeightDifference() / 2);
            ctx.scale(scaledResolution.getWidthScaleFactor(), scaledResolution.getHeightScaleFactor());
        } else {
            ctx.scale(1 / scaledResolution.getWidthScaleFactor(), 1 / scaledResolution.getHeightScaleFactor());
            ctx.translate(-scaledResolution.getWidthDifference() / 2, -scaledResolution.getHeightDifference() / 2);
        }
    }

    @Override
    public void stop() {
        frame.setVisible(false);

        stopClient = false;
        frame.dispose();
    }

    private class ResizeHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        }
    }
}
