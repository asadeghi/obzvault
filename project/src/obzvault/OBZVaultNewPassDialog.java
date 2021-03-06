/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import org.jdesktop.application.Action;

import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JRootPane;
import javax.swing.JComponent;
import javax.swing.InputMap;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 *
 * @author duncan
 */
public class OBZVaultNewPassDialog extends javax.swing.JDialog {

    private char[] _rgchPw;

    public char[] getPassword() {
        return _rgchPw;
    }

    /** Creates new form OBZVaultNewPassDialog */
    public OBZVaultNewPassDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        JRootPane rootpn = this.getRootPane();
        rootpn.setDefaultButton(btnOk);

        //  Handle escape key to close the dialog
        InputMap tmap = rootpn.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        tmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        rootpn.getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnOk.setToolTipText(null);
        btnCancel.setToolTipText(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPassword = new javax.swing.JLabel();
        lblPasswordRepeat = new javax.swing.JLabel();
        pw = new javax.swing.JPasswordField();
        pwRepeat = new javax.swing.JPasswordField();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        lblInfo = new javax.swing.JLabel();
        lblInfo2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(obzvault.OBZVaultApp.class).getContext().getResourceMap(OBZVaultNewPassDialog.class);
        lblPassword.setText(resourceMap.getString("lblPassword.text")); // NOI18N
        lblPassword.setName("lblPassword"); // NOI18N

        lblPasswordRepeat.setText(resourceMap.getString("lblPasswordRepeat.text")); // NOI18N
        lblPasswordRepeat.setName("lblPasswordRepeat"); // NOI18N

        pw.setText(resourceMap.getString("pw.text")); // NOI18N
        pw.setName("pw"); // NOI18N
        pw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pwFocusGained(evt);
            }
        });
        pw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pwKeyReleased(evt);
            }
        });

        pwRepeat.setText(resourceMap.getString("pwRepeat.text")); // NOI18N
        pwRepeat.setName("pwRepeat"); // NOI18N
        pwRepeat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pwRepeatFocusGained(evt);
            }
        });
        pwRepeat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pwRepeatKeyReleased(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(obzvault.OBZVaultApp.class).getContext().getActionMap(OBZVaultNewPassDialog.class, this);
        btnCancel.setAction(actionMap.get("cancel")); // NOI18N
        btnCancel.setText(resourceMap.getString("btnCancel.text")); // NOI18N
        btnCancel.setMaximumSize(new java.awt.Dimension(80, 29));
        btnCancel.setMinimumSize(new java.awt.Dimension(80, 29));
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.setPreferredSize(new java.awt.Dimension(80, 29));

        btnOk.setAction(actionMap.get("ok")); // NOI18N
        btnOk.setText(resourceMap.getString("btnOk.text")); // NOI18N
        btnOk.setMaximumSize(new java.awt.Dimension(80, 29));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 29));
        btnOk.setName("btnOk"); // NOI18N
        btnOk.setPreferredSize(new java.awt.Dimension(80, 29));

        lblInfo.setText(resourceMap.getString("lblInfo.text")); // NOI18N
        lblInfo.setName("lblInfo"); // NOI18N

        lblInfo2.setText(resourceMap.getString("lblInfo2.text")); // NOI18N
        lblInfo2.setName("lblInfo2"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblInfo2)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblPasswordRepeat)
                            .add(lblPassword))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(pw, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .add(pwRepeat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(lblInfo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblInfo2)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPassword)
                    .add(pw, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPasswordRepeat)
                    .add(pwRepeat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwKeyReleased
        checkRepeat();
    }//GEN-LAST:event_pwKeyReleased

    private void pwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwFocusGained
        pw.selectAll();
    }//GEN-LAST:event_pwFocusGained

    private void pwRepeatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwRepeatFocusGained
        pwRepeat.selectAll();
    }//GEN-LAST:event_pwRepeatFocusGained

    private void pwRepeatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwRepeatKeyReleased
        checkRepeat();
    }//GEN-LAST:event_pwRepeatKeyReleased

    private void checkRepeat() {
        String strPw = new String(pw.getPassword());
        String strPwRepeat = new String(pwRepeat.getPassword());
        
        btnOk.setEnabled(strPw.equals(strPwRepeat) && strPw.length() > 0 && !strPw.contains(" "));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                OBZVaultNewPassDialog dialog = new OBZVaultNewPassDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    @Action
    public void ok() {
        _rgchPw = pw.getPassword();
        setVisible(false);
    }

    @Action
    public void cancel() {
        _rgchPw = null;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblInfo2;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordRepeat;
    private javax.swing.JPasswordField pw;
    private javax.swing.JPasswordField pwRepeat;
    // End of variables declaration//GEN-END:variables
}
