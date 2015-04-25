package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

/**
 * Vue NewUserView
 * Vue d'ajout d'utilisateur
 * @extends EView
 * @author E-Novative Keys
 */
public class NewUserView extends EView
{
    private final JLabel lastnameLabel      = new JLabel("Nom de famille : ");
    private final JTextField lastname       = new JTextField(20);
    private final JLabel firstnameLabel     = new JLabel("Prénom : ");
    private final JTextField firstname      = new JTextField(20);
    private final JLabel emailLabel         = new JLabel("Email : ");
    private final JTextField email          = new JTextField(20);
    private final JLabel passwordLabel      = new JLabel("Mot de passe : ");
    private final JPasswordField password   = new JPasswordField(20);
    private final JLabel confirmLabel       = new JLabel("Confirmation : ");
    private final JPasswordField confirm    = new JPasswordField(20);
    private final JLabel roleLabel          = new JLabel("Rôle : ");
    private final JComboBox role            = new JComboBox(new String[]{"employee", "trainee", "developer","leaddev", "admin"});
    private final JCheckBox validated       = new JCheckBox("Validé ? ");
    private final JButton save              = new JButton(EResources.loadImageIcon("bouton_enregister.png", 180, 50));
    private final JButton back              = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel         = new JLabel("");
    
    public NewUserView()
    {
        super();
        
        add(newUserTable(), "Center");
        add(backPanel(),    "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Nouvel Utilisateur"));
    }
    
    private ETable newUserTable()
    {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        constraints.fill = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 15, 15, 15);
        
        table.add(lastnameLabel,    constraints, 0, 0);
        table.add(lastname,         constraints, 1, 0);
        table.add(firstnameLabel,   constraints, 0, 1);
        table.add(firstname,        constraints, 1, 1);
        table.add(roleLabel,        constraints, 0, 2);
        table.add(role,             constraints, 1, 2);
        
        validated.setSelected(true);
        table.add(validated,        constraints, 0, 3, 0);
        
        table.add(emailLabel,       constraints, 2, 0);
        table.add(email,            constraints, 3, 0);
        table.add(passwordLabel,    constraints, 2, 1);
        table.add(password,         constraints, 3, 1);
        table.add(confirmLabel,     constraints, 2, 2);
        table.add(confirm,          constraints, 3, 2);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        panel.add(table, constraints, 0, 0);
        
        return panel;
    }
    
    private JPanel backPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setOpaque(false);
        back.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(back, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.red);
        table.add(errorLabel, constraints, 0, 0, 0);
        panel.add(table, "Center");
        
        save.setBorderPainted(false);
        save.setContentAreaFilled(false);
        save.setFocusPainted(false);
        save.setOpaque(false);
        save.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(save, "East");
        
        return panel;
    }

    public JLabel getLastnameLabel()
    {
        return lastnameLabel;
    }

    public JTextField getLastname()
    {
        return lastname;
    }

    public JLabel getFirstnameLabel()
    {
        return firstnameLabel;
    }

    public JTextField getFirstname()
    {
        return firstname;
    }

    public JLabel getEmailLabel()
    {
        return emailLabel;
    }

    public JTextField getEmail()
    {
        return email;
    }

    public JLabel getPasswordLabel()
    {
        return passwordLabel;
    }

    public JPasswordField getPassword()
    {
        return password;
    }

    public JLabel getConfirmLabel()
    {
        return confirmLabel;
    }

    public JPasswordField getConfirm()
    {
        return confirm;
    }

    public JLabel getRoleLabel()
    {
        return roleLabel;
    }

    public JComboBox getRole()
    {
        return role;
    }

    public JCheckBox getValidated()
    {
        return validated;
    }

    public JButton getSave()
    {
        return save;
    }

    public JButton getBack()
    {
        return back;
    }

    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
    
}
