package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

/**
 * Vue ForgotView.
 * Vue d'oublie des credentials.
 * @extends EView
 * @author E-Novative Keys
 * @version 1.0
 */
public class ForgotView extends EView
{
    private final ImageIcon logo                = EResources.loadImageIcon("logo.png", 340, 100);
    private final JLabel emailLabel             = new JLabel("E-Mail :");
    private final JTextField emailField         = new JTextField(20);
    private final JButton tokenButton            = new JButton("Générer");
    private final JLabel passwordLabel          = new JLabel("Mot de passe :");
    private final JPasswordField passwordField  = new JPasswordField(20);
    private final JLabel confirmLabel           = new JLabel("Confirmation :");
    private final JPasswordField confirmField   = new JPasswordField(20);
    private final JLabel tokenLabel             = new JLabel("Code de sécurité :");
    private final JTextField tokenField         = new JTextField(20);
    private final JButton sendButton            = new JButton("Enregistrer");
    private final JLabel errorLabel             = new JLabel();
    private final JButton backButton            = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    
    public ForgotView()
    {
        super();
        add(forgotPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private ETable forgotPanel()
    {
        ETable panel = new ETable();
        GridBagConstraints constraints;
        
        constraints         = panel.getConstraints();
        constraints.insets  = new Insets(5, 10, 5, 5);
        constraints.fill    = GridBagConstraints.CENTER;
        
        errorLabel.setForeground(Color.red);
        
        panel.add(new JLabel(logo), constraints, 0, 0, 0);
        panel.add(emailLabel, constraints, 0, 1);
        panel.add(emailField, constraints, 1, 1);
        panel.add(tokenButton, constraints, 2, 1);
        panel.add(passwordLabel, constraints, 0, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, constraints, 1, 2, 0);
        constraints.fill = GridBagConstraints.CENTER;
        panel.add(confirmLabel, constraints, 0, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmField, constraints, 1, 3, 0);
        constraints.fill = GridBagConstraints.CENTER;
        panel.add(tokenLabel, constraints, 0, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(tokenField, constraints, 1, 4, 0);
        
        return panel;
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable buttons = new ETable();
        GridBagConstraints constraints = buttons.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        buttons.add(errorLabel, constraints, 0, 0);
        buttons.add(sendButton, constraints, 1, 0);
        panel.add(buttons, "East");
        
        return panel;
    }

    public JTextField getEmailField()
    {
        return emailField;
    }

    public JButton getTokenButton()
    {
        return tokenButton;
    }

    public JPasswordField getPasswordField()
    {
        return passwordField;
    }

    public JPasswordField getConfirmField()
    {
        return confirmField;
    }

    public JTextField getTokenField()
    {
        return tokenField;
    }

    public JButton getSendButton()
    {
        return sendButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }
    
    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
}
