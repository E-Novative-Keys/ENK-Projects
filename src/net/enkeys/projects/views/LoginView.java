package net.enkeys.projects.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class LoginView extends EView
{
    private final ImageIcon logo = EResources.loadImageIcon("logo.png", 340, 100);
    private final JLabel emailLabel = new JLabel("E-Mail :");
    private final JTextField emailField = new JTextField(20);
    private final JLabel passwordLabel = new JLabel("Mot de passe :");
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel forgotLabel = new JLabel("Mot de passe oublié ?");
    private final JButton loginButton = new JButton("Connexion");
    private final JLabel errorLabel = new JLabel();

    public LoginView()
    {
        super();
        add(loginPanel());
    }
    
    private ETable loginPanel()
    {
        ETable panel = new ETable();
        GridBagConstraints constraints;
        
        constraints = panel.getConstraints();
        constraints.insets = new Insets(5, 10, 5, 5);
        constraints.fill = GridBagConstraints.CENTER;
        
        forgotLabel.setForeground(Color.blue);
        forgotLabel.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        errorLabel.setForeground(Color.red);
        
        panel.add(new JLabel(logo), constraints, 0, 0, 0);
        panel.add(emailLabel, constraints, 0, 1);
        panel.add(emailField, constraints, 1, 1);
        panel.add(passwordLabel, constraints, 0, 2);
        panel.add(passwordField, constraints, 1, 2);
        panel.add(forgotLabel, constraints, 0, 3, 0);
        panel.add(loginButton, constraints, 0, 4, 0);
        panel.add(errorLabel, constraints, 0, 5, 0);
        
        return panel;
    }
    
    public JTextField getEmailField()
    {
        return emailField;
    }

    public JPasswordField getPasswordField()
    {
        return passwordField;
    }

    public JLabel getForgotLabel()
    {
        return forgotLabel;
    }

    public JButton getLoginButton()
    {
        return loginButton;
    }
    
    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
}
