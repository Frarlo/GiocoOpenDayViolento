package gov.ismonnet.bootstrap.swing;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class LoadingScreen extends JPanel {

    private final ImageIcon luca;
    private final JLabel lucaLabel;

    @Inject LoadingScreen(ImageIcon luca) {
        this.luca = luca;

        lucaLabel = new ResizedLabel(luca);
        lucaLabel.setBounds(0, 0, getWidth(), getHeight());
        add(lucaLabel);

        addComponentListener(new ResizeHandler());
    }

    private class ResizeHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            lucaLabel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    private class ResizedLabel extends JLabel {

        ResizedLabel(Icon image) {
            super(image);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(luca.getImage(), 0,0, getWidth(), getHeight(),this);
        }
    }
}
