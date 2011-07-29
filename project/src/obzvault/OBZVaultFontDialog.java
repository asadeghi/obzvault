/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import org.jdesktop.application.Action;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JRootPane;
import javax.swing.JComponent;

/**
 *
 * @author asadeghi
 */
public class OBZVaultFontDialog extends javax.swing.JDialog {

    private Font _font;
    private Map<String, Integer> _mapFontStyles;
    private Integer[] _rgnFontSizes = {6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 26, 28, 32, 36, 40, 48, 56, 64, 72};
    private boolean _fInitComplete;

    /** Creates new form OBZVaultFontDialog */
    public OBZVaultFontDialog(java.awt.Frame parent, boolean modal, Font fontOriginal) {
        super(parent, modal);
        initComponents();

        // Remember original font, only update on 'ok'.
        _font = fontOriginal;

        // Populate font family
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] rgstrFontnames = e.getAvailableFontFamilyNames();
        for (int i = 0; i < rgstrFontnames.length; i++)
        {
            comboFontFamily.addItem(rgstrFontnames[i]);
            if (rgstrFontnames[i].equals(_font.getFamily()))
                comboFontFamily.setSelectedIndex(i);
        }

        // Populate font style
        _mapFontStyles = new HashMap<String, Integer>();
        _mapFontStyles.put("PLAIN", Font.PLAIN);
        _mapFontStyles.put("BOLD", Font.BOLD);
        _mapFontStyles.put("ITALIC", Font.ITALIC);
        _mapFontStyles.put("BOLD+ITALIC", Font.BOLD + Font.ITALIC);
        for (String strKey : _mapFontStyles.keySet())
        {
            comboFontStyle.addItem(strKey);
            if (_mapFontStyles.get(strKey) == _font.getStyle())
                comboFontStyle.setSelectedItem(strKey);
        }

        // Populate font size
        for (int i = 0; i < _rgnFontSizes.length; i++)
        {
            comboFontSize.addItem(_rgnFontSizes[i].toString());
            if (_font.getSize() == _rgnFontSizes[i])
                comboFontSize.setSelectedIndex(i);
        }

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

        _fInitComplete = true;
        this.updateSampleTextFont();
    }

    public Font getSelectedFont()
    {
        return _font;
    }

    public String getSelectedFontString()
    {
        String strFont = null;
        String strStyle = null;

        for (String strKey : _mapFontStyles.keySet())
        {
            if (_mapFontStyles.get(strKey) == _font.getStyle())
            {
                strStyle = strKey;
                break;
            }
        }
        if (strStyle != null)
            strFont = _font.getFamily() + "-" + strStyle + "-" + new Integer(_font.getSize()).toString();
        
        return strFont;
    }

    @Action
    public void cancel() {
        setVisible(false);
    }

    @Action
    public void ok() {
        Font fontNew = this.getCurrentFont();
        if (fontNew != null)
            _font = fontNew;

        setVisible(false);
    }

    private Font getCurrentFont()
    {
        String strFontFamily = (String)comboFontFamily.getSelectedItem();
        Integer style = _mapFontStyles.get(comboFontStyle.getSelectedItem());
        Integer size = Integer.parseInt((String)comboFontSize.getSelectedItem());
        Font fontNew = new Font(strFontFamily, style, size);

        return fontNew;
    }

    private void updateSampleTextFont()
    {
        if (_fInitComplete)
            textfieldSample.setFont(this.getCurrentFont());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboFontFamily = new javax.swing.JComboBox();
        comboFontStyle = new javax.swing.JComboBox();
        comboFontSize = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        textfieldSample = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(obzvault.OBZVaultApp.class).getContext().getResourceMap(OBZVaultFontDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setResizable(false);

        comboFontFamily.setMaximumRowCount(7);
        comboFontFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFontFamilyActionPerformed(evt);
            }
        });

        comboFontStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFontStyleActionPerformed(evt);
            }
        });

        comboFontSize.setMaximumRowCount(6);
        comboFontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFontSizeActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(obzvault.OBZVaultApp.class).getContext().getActionMap(OBZVaultFontDialog.class, this);
        btnCancel.setAction(actionMap.get("cancel")); // NOI18N
        btnCancel.setText(resourceMap.getString("btnCancel.text")); // NOI18N
        btnCancel.setMaximumSize(new java.awt.Dimension(80, 29));
        btnCancel.setMinimumSize(new java.awt.Dimension(80, 29));
        btnCancel.setPreferredSize(new java.awt.Dimension(80, 29));

        btnOk.setAction(actionMap.get("ok")); // NOI18N
        btnOk.setText(resourceMap.getString("btnOk.text")); // NOI18N
        btnOk.setMaximumSize(new java.awt.Dimension(80, 29));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 29));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 29));

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N

        textfieldSample.setEditable(false);
        textfieldSample.setText(resourceMap.getString("textfieldSample.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel4)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(comboFontFamily, 0, 299, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(comboFontStyle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(comboFontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(textfieldSample, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(comboFontFamily, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(comboFontStyle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel2))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(comboFontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel3)))
                .add(38, 38, 38)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(textfieldSample, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboFontFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFontFamilyActionPerformed
        this.updateSampleTextFont();
    }//GEN-LAST:event_comboFontFamilyActionPerformed

    private void comboFontStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFontStyleActionPerformed
        this.updateSampleTextFont();
    }//GEN-LAST:event_comboFontStyleActionPerformed

    private void comboFontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFontSizeActionPerformed
        this.updateSampleTextFont();
    }//GEN-LAST:event_comboFontSizeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox comboFontFamily;
    private javax.swing.JComboBox comboFontSize;
    private javax.swing.JComboBox comboFontStyle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField textfieldSample;
    // End of variables declaration//GEN-END:variables

}
