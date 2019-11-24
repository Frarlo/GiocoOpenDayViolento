package gov.ismonnet.game.renderer.swing.screen;

import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.SwingRenderContext;
import gov.ismonnet.game.renderer.swing.SwingRenderService;
import gov.ismonnet.game.renderer.swing.SwingRenderer;
import gov.ismonnet.util.ScaledResolution;
import gov.ismonnet.swing.SwingWindow;

import javax.inject.Inject;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class GameScreen extends BaseScreen {

    private final PhysicsService physicsService;
    private final Table table;

    private final Map<Class, SwingRenderer> renderers;
    private final SwingRenderer<Object> fallbackRenderer;
    private final SwingRenderer<? super Entity> axisAlignedBBsRenderer;

    @Inject GameScreen(SwingWindow window,
                       SwingRenderService renderService,
                       PhysicsService physicsService,
                       Table table,
                       Map<Class<?>, SwingRenderer> renderers,
                       SwingRenderer<Object> fallbackRenderer,
                       SwingRenderer<? super Entity> axisAlignedBBsRenderer) {
        super(window, renderService);

        this.physicsService = physicsService;
        this.table = table;

        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));
        this.fallbackRenderer = fallbackRenderer;
        this.axisAlignedBBsRenderer = axisAlignedBBsRenderer;
    }

    @Override
    public void onInit(ScaledResolution scaledResolution) {
        window.setMouseGrabbed(true);
    }

    @Override
    public void onDestroy() {
        window.setMouseGrabbed(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(SwingRenderContext ctx) {
        physicsService.handleTicks();

        setupScaling(ctx, true);

        renderers.getOrDefault(Table.class, fallbackRenderer).render(ctx, table);
        physicsService.getEntities().forEach(entity -> {
            renderers.getOrDefault(entity.getClass(), fallbackRenderer).render(ctx, entity);
            axisAlignedBBsRenderer.render(ctx, entity);
        });

        setupScaling(ctx, false);
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            renderService.setScreen(Type.PAUSE);
    }

    @Override
    public void onFocusLost(FocusEvent e) {
        renderService.setScreen(Type.PAUSE);
    }

    private void setupScaling(SwingRenderContext ctx, boolean enable) {
        if (enable) {
            if(getSide() == RenderService.Side.RIGHT) {
                ctx.scale(-1, 1);
                ctx.translate(-getWidth(), 0);
            }

            ctx.translate(
                    getScaledResolution().getWidthDifference(),
                    getScaledResolution().getHeightDifference() / 2);
            ctx.scale(
                    getScaledResolution().getWidthScaleFactor(),
                    getScaledResolution().getHeightScaleFactor());
        } else {
            ctx.scale(
                    1 / getScaledResolution().getWidthScaleFactor(),
                    1 / getScaledResolution().getHeightScaleFactor());
            ctx.translate(
                    -getScaledResolution().getWidthDifference(),
                    -getScaledResolution().getHeightDifference() / 2);

            if(getSide() == RenderService.Side.RIGHT) {
                ctx.translate(getWidth(), 0);
                ctx.scale(-1, 1);
            }
        }
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
    @interface Qualifier {
    }
}
