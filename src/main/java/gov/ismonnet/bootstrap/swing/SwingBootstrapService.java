package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.bootstrap.BootstrapService;
import gov.ismonnet.swing.SwingGraphics;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.SneakyThrow;
import org.jdesktop.swingx.VerticalLayout;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SwingBootstrapService extends JPanel implements BootstrapService {

    // Constants

    // These are magic numbers referring to a background img of size 1764x2093

    private static final int BACKGROUND_IMG_WIDTH = 1764;
    private static final int BACKGROUND_IMG_HEIGHT = 2093;

    private static final int BUTTONS_BOX_X = 125;
    private static final int BUTTONS_BOX_Y = 850;
    private static final int BUTTONS_BOX_WIDTH = 800;
    private static final int BUTTONS_BOX_HEIGHT = 1150;

    // Attributes

    private final SwingWindow window;

    private final Image backgroundImg;
    private final ButtonsPanel buttonsPanel;

    private AtomicReference<CompletableFuture<NetSide>> choiceFuture = new AtomicReference<>();

    private float imgX, imgY, imgWidth, imgHeight;

    @Inject SwingBootstrapService(SwingWindow window, Image backgroundImg) {
        this.window = window;
        this.backgroundImg = backgroundImg;

        // Make the panel background transparent
        // as I'm gonna render an image on it
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));

        buttonsPanel = new ButtonsPanel(
                e -> {
                    final CompletableFuture<NetSide> choiceFuture0;
                    if((choiceFuture0 = choiceFuture.getAndSet(null)) != null)
                        choiceFuture0.complete(NetSide.SERVER);
                },
                e -> {
                    final CompletableFuture<NetSide> choiceFuture0;
                    if((choiceFuture0 = choiceFuture.getAndSet(null)) != null)
                        choiceFuture0.complete(NetSide.CLIENT);
                });
        add(buttonsPanel);

        setLayout(null);
        addComponentListener(new ResizeHandler());
    }

    @Override
    public NetSide chooseNetSide() {
        window.setScreen(this);

        final CompletableFuture<NetSide> choiceFuture0 = new CompletableFuture<>();
        choiceFuture.set(choiceFuture0);
        return SneakyThrow.callUnchecked(choiceFuture0::get);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a Graphics2D object");

        final SwingGraphics g2d = new SwingGraphics((Graphics2D) g);
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, 0, 0);
        g2d.drawImage(backgroundImg, imgX, imgY, imgWidth, imgHeight, null);

        // Draw the actual component
        super.paintComponent(g);
    }

    private class ResizeHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            final float imgRatio = getWidth() < getHeight() ?
                    (float) backgroundImg.getWidth(null) / getWidth() :
                    (float) backgroundImg.getHeight(null) / getHeight();
            imgWidth = backgroundImg.getWidth(null) / imgRatio;
            imgHeight = backgroundImg.getHeight(null) / imgRatio;
            imgX = (getWidth() - imgWidth) / 2F;
            imgY = (getHeight() - imgHeight) / 2F;

            // box : img = actualBox : actualImg
            // actualBox = box * actualImg / img
            buttonsPanel.setBounds(
                    (int) (imgX + BUTTONS_BOX_X * imgWidth / BACKGROUND_IMG_WIDTH),
                    (int) (imgY + BUTTONS_BOX_Y * imgHeight / BACKGROUND_IMG_HEIGHT),
                    (int) (BUTTONS_BOX_WIDTH * imgWidth / BACKGROUND_IMG_WIDTH),
                    (int) (BUTTONS_BOX_HEIGHT * imgHeight / BACKGROUND_IMG_HEIGHT));
            buttonsPanel.revalidate();
        }
    }

    /*
     * Created by JFormDesigner on Sat Nov 16 16:06:32 CET 2019
     */
    static class ButtonsPanel extends JPanel {

        private final Consumer<ActionEvent> onServer;
        private final Consumer<ActionEvent> onClient;

        ButtonsPanel(Consumer<ActionEvent> onServer, Consumer<ActionEvent> onClient) {
            this.onServer = onServer;
            this.onClient = onClient;

            initComponents();
        }

        private void clientBtnActionPerformed(ActionEvent e) {
            onClient.accept(e);
        }

        private void serverBtnActionPerformed(ActionEvent e) {
            onServer.accept(e);
        }

        @SuppressWarnings("all")
        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - unknown
            JPanel innerPanel = new JPanel();
            JButton serverBtn = new JButton();
            JButton clientBtn = new JButton();

            //======== this ========
            setBorder(new LineBorder(Color.darkGray, 1, true));
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            //======== innerPanel ========
            {
                innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                innerPanel.setLayout(new VerticalLayout(5));

                //---- serverBtn ----
                serverBtn.setText("Ospita");
                serverBtn.addActionListener(e -> serverBtnActionPerformed(e));
                innerPanel.add(serverBtn);

                //---- clientBtn ----
                clientBtn.setText("Connettiti");
                clientBtn.addActionListener(e -> clientBtnActionPerformed(e));
                innerPanel.add(clientBtn);
            }
            add(innerPanel);
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - unknown
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}
