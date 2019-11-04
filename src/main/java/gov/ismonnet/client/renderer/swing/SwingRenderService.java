package gov.ismonnet.client.renderer.swing;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.rink.Rink;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AutoFactory(implementing = RenderServiceFactory.class)
public class SwingRenderService extends JFrame implements RenderService {

    private final Rink rink;
    private final Map<Class, Renderer> renderers;

    private final Runnable onTick;

    public SwingRenderService(@Provided Rink rink,
                              @Provided Map<Class<?>, Renderer> renderers,
                              Runnable onTick) {
        this.rink = rink;
        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));

        this.onTick = onTick;

        setTitle("V good game");
        setContentPane(new GamePanel());
        setVisible(true);
    }

    private class GamePanel extends JPanel {

    }
}
