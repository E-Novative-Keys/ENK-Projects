package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

/**
 * Vue NewClientView
 * Vue d'ajout de client
 * @extends EView
 * @author E-Novative Keys
 */
public class NewClientView extends EView
{
    private final JButton back              = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel lastnameLabel      = new JLabel("Nom de famille : ");
    private final JTextField lastname       = new JTextField(20);
    private final JLabel firstnameLabel     = new JLabel("Prénom : ");
    private final JTextField firstname      = new JTextField(20);
    private final JLabel phonenumberLabel   = new JLabel("Téléphone : ");
    private final JTextField phonenumber    = new JTextField(20);
    private final JLabel emailLabel         = new JLabel("Email : ");
    private final JTextField email          = new JTextField(20);
    private final JLabel enterpriseLabel    = new JLabel("Entreprise : ");
    private final JTextField enterprise     = new JTextField(20);
    private final JLabel addressLabel       = new JLabel("Adresse de l'entreprise : ");
    private final JTextField address        = new JTextField(20);
    private final JLabel siretLabel         = new JLabel("Siret : ");
    private final JTextField siret          = new JTextField(20);
    private final JButton save              = new JButton(EResources.loadImageIcon("bouton_enregister.png", 180, 50));
    private final JCheckBox newProject      = new JCheckBox("Créer un nouveau projet");
    private final JLabel errorLabel         = new JLabel("");
    
    public NewClientView() 
    {
        super();
        
        add(newClientTable(), "Center");
        add(buttonsPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Nouveau Client"));
    }
    
    private ETable newClientTable()
    {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
       
        constraints.fill    = GridBagConstraints.CENTER;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        table.add(lastnameLabel, constraints, 0, 0);
        table.add(lastname, constraints, 1, 0);
        table.add(firstnameLabel, constraints, 0, 1);
        table.add(firstname, constraints, 1, 1);
        table.add(phonenumberLabel, constraints, 0, 2);
        table.add(phonenumber, constraints, 1, 2);
        table.add(emailLabel, constraints, 0, 3);
        table.add(email, constraints, 1, 3);
        table.add(enterpriseLabel, constraints, 2, 0);
        table.add(enterprise, constraints, 3, 0);
        table.add(addressLabel, constraints, 2, 1);
        table.add(address, constraints, 3, 1);
        table.add(siretLabel, constraints, 2, 2);
        table.add(siret, constraints, 3, 2);
        table.add(newProject, constraints, 2, 3, 0);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        panel.add(table, constraints, 0, 0);
        
        return panel;
    }
    
    private JPanel buttonsPanel()
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

    public JButton getBack()
    {
        return back;
    }

    public JTextField getLastname()
    {
        return lastname;
    }

    public JTextField getFirstname()
    {
        return firstname;
    }

    public JTextField getPhonenumber()
    {
        return phonenumber;
    }

    public JTextField getEmail()
    {
        return email;
    }

    public JTextField getEnterprise()
    {
        return enterprise;
    }

    public JTextField getAddress()
    {
        return address;
    }

    public JTextField getSiret()
    {
        return siret;
    }

    public JButton getSave()
    {
        return save;
    }

    public JCheckBox getNewProject()
    {
        return newProject;
    }
    
    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
}
