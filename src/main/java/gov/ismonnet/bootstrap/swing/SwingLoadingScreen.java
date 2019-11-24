package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingGraphics;
import gov.ismonnet.swing.SwingGraphics.HorizontalAlignment;
import gov.ismonnet.swing.SwingGraphics.VerticalAlignment;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class SwingLoadingScreen extends JPanel {

    private final ImageIcon luca;
    private final CancelButton cancelButton;

    protected String message;

    @Inject SwingLoadingScreen(@BackgroundColor Color backgroundColor,
                               ImageIcon luca) {
        this.luca = luca;
        this.message = "";
        this.cancelButton = new CancelButton("Annulla", this::cancel);

        addMouseListener(cancelButton);
        setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a Graphics2D object");

        final SwingGraphics g2d = new SwingGraphics((Graphics2D) g);
        g2d.setColor(Color.white);

        final float lineY = getHeight() / 8F * 7F;
        final float xBorder = 50;
        g2d.drawLine(xBorder, lineY, getWidth() - xBorder, lineY);

        g2d.textSize(12);
        g2d.textAlign(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        g2d.drawString(message, xBorder, lineY - 2);

        final Image img = luca.getImage();
        final float imgY = lineY / 16F;
        final float imgHeight = lineY / 16F * 14F;

        final float imgWidth = imgHeight / img.getHeight(null) * img.getWidth(null);
        final float imgX = getWidth() - xBorder - imgWidth;

        g2d.drawImage(img, imgX, imgY, imgWidth, imgHeight, this);

        g2d.textAlign(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);

        g2d.textSize(12);

        cancelButton.width = g2d.getFontMetrics().stringWidth(cancelButton.text);
        cancelButton.height = 12;
        cancelButton.x = getWidth() - xBorder - cancelButton.width;
        cancelButton.y = lineY - 2 - cancelButton.height;

        cancelButton.render(g2d);
    }

    public void cancel() {
    }

    class CancelButton extends MouseAdapter {

        private final String text;
        private final Runnable onClick;

        private float x;
        private float y;
        private float width;
        private float height;

        CancelButton(String text, Runnable onClick) {
            this.text = text;
            this.onClick = onClick;
        }

        public void render(SwingGraphics g2d) {
            g2d.textAlign(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
            g2d.textSize(height);
            g2d.setColor(!isHovered() ? Color.white : Color.orange);
            g2d.drawString(text, x, y);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(!isInButton(e.getX(), e.getY()))
                return;
            onClick.run();
        }

        public boolean isHovered() {
            return getMousePosition() != null && isInButton(
                    (float) getMousePosition().getX(),
                    (float) getMousePosition().getY());
        }

        public boolean isInButton(float x, float y) {
            return x >= this.x &&
                    x <= this.x + width &&
                    y >= this.y &&
                    y <= this.y + height;
        }
    }
}
