package net.enkeys.projects.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class NewClientView extends EView
{
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
    private final JButton save              = new JButton("Enregistrer");
    private final JButton exit              = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JCheckBox newProject      = new JCheckBox("Créer un nouveau projet en même temps");
    
    public NewClientView() 
    {
        super();
        add(newClientTable());
    }
    
    private ETable newClientTable()
    {
        ETable panel = new ETable();
        ETable table = new ETable();
        
        GridBagConstraints constraints = panel.getConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Nouveau Client"));
        panel.add(table, constraints, 0, 0);
        constraints = table.getConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 15, 15, 15);
        
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setOpaque(false);
        exit.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        
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
        save.setPreferredSize(new Dimension(200, 50));
        table.add(save, constraints, 0, 4, 0);
        table.add(newProject, constraints, 2, 3, 0);
        table.add(exit, constraints, 0, 5);       
        
        return panel;
    }

    public JButton getExit()
    {
        return exit;
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
            
}
