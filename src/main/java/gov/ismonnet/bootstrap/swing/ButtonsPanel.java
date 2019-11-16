/*
 * Created by JFormDesigner on Sat Nov 16 17:00:54 CET 2019
 */

package gov.ismonnet.bootstrap.swing;

import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

class ButtonsPanel extends JPanel {

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

    @SuppressWarnings("ALL")
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
