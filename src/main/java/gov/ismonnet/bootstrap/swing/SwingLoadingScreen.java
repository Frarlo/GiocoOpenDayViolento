package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingGraphics;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

class SwingLoadingScreen extends JPanel {

    private final ImageIcon luca;
    private final String message;

    @Inject SwingLoadingScreen(@BackgroundColor Color backgroundColor,
                               ImageIcon luca, String message) {
        this.luca = luca;
        this.message = message;

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
        g2d.textAlign(SwingGraphics.HorizontalAlignment.LEFT, SwingGraphics.VerticalAlignment.BOTTOM);
        g2d.drawString(message, xBorder, lineY - 2);

        final Image img = luca.getImage();
        final float imgY = lineY / 16F;
        final float imgHeight = lineY / 16F * 14F;

        final float imgWidth = imgHeight / img.getHeight(null) * img.getWidth(null);
        final float imgX = getWidth() - xBorder - imgWidth;

        g2d.drawImage(img, imgX, imgY, imgWidth, imgHeight, this);
    }
}
