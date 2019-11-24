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
    private final Consumer<ActionEvent> onExit;

    ButtonsPanel(Consumer<ActionEvent> onServer, Consumer<ActionEvent> onClient, Consumer<ActionEvent> onExit) {
        this.onServer = onServer;
        this.onClient = onClient;
        this.onExit = onExit;

        initComponents();
    }

    private void clientBtnActionPerformed(ActionEvent e) {
        onClient.accept(e);
    }

    private void serverBtnActionPerformed(ActionEvent e) {
        onServer.accept(e);
    }

    private void exitBtnActionPerformed(ActionEvent e) {
        onExit.accept(e);
    }

    @SuppressWarnings("ALL")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        JPanel innerPanel = new JPanel();
        JButton serverBtn = new JButton();
        JButton clientBtn = new JButton();
        exitBtn = new JButton();

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

            //---- exitBtn ----
            exitBtn.setText("Esci");
            exitBtn.addActionListener(e -> exitBtnActionPerformed(e));
            innerPanel.add(exitBtn);
        }
        add(innerPanel);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton exitBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
