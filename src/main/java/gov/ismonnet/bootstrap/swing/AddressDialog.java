/*
 * Created by JFormDesigner on Sat Nov 16 17:05:33 CET 2019
 */

package gov.ismonnet.bootstrap.swing;

import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.CompletableFuture;

class AddressDialog extends JDialog {

    private final CompletableFuture<String> addressFuture;

    private AddressDialog(Window owner, CompletableFuture<String> addressFuture) {
        super(owner);

        this.addressFuture = addressFuture;
        initComponents();

        setMinimumSize(new Dimension(getWidth(), getHeight()));
    }

    static CompletableFuture<String> getAddress(Window owner) {
        final CompletableFuture<String> addressFuture = new CompletableFuture<>();
        new AddressDialog(owner, addressFuture).setVisible(true);
        return addressFuture;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if(!addressTxtField.getText().equals("") && addressTxtField.getText() != null) {
            addressFuture.complete(addressTxtField.getText());
            dispose();
        }
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();

        if(!addressFuture.isDone())
            addressFuture.complete(null);
    }

    @SuppressWarnings("ALL")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        JPanel dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        JLabel addressLabel = new JLabel();
        addressTxtField = new JTextField();
        JPanel buttonBar = new JPanel();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Indirizzo");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new VerticalLayout(5));

                //---- addressLabel ----
                addressLabel.setText("Inserisci l'indirizzo a cui connettersi:");
                contentPanel.add(addressLabel);

                //---- addressTxtField ----
                addressTxtField.setMinimumSize(new Dimension(200, 26));
                addressTxtField.setPreferredSize(new Dimension(200, 26));
                contentPanel.add(addressTxtField);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTextField addressTxtField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
