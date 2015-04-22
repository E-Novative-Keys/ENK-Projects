package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class ProfileView extends EView
{
    private final JLabel userLabel          = new JLabel("Nom d'utilisateur :");
    private final JLabel userValue          = new JLabel("");
    private final JLabel mailLabel          = new JLabel("E-mail :");
    private final JLabel mailValue          = new JLabel("");
    private final JLabel dateLabel          = new JLabel("Dernière connexion :");
    private final JLabel dateValue          = new JLabel("");
    private final JLabel ipLabel            = new JLabel("Dernière IP :");
    private final JLabel ipValue            = new JLabel("");
    private final JLabel oldLabel           = new JLabel("Ancien mot de passe :");
    private final JPasswordField oldPass    = new JPasswordField(20);
    private final JLabel newLabel           = new JLabel("Nouveau mot de passe :");
    private final JPasswordField newPass    = new JPasswordField(20);
    private final JLabel confirmLabel       = new JLabel("Confirmation :");
    private final JPasswordField confirm    = new JPasswordField(20);
    private final JButton majButton         = new JButton("Mettre à jour");
    private final JButton backButton        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel         = new JLabel("");

    public ProfileView()
    {
        super();
        
        add(profilePanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private ETable profilePanel()
    {
        ETable table        = new ETable();
        ETable leftTable    = new ETable();
        ETable rightTable   = new ETable();
        
        GridBagConstraints constraints = leftTable.getConstraints();
        
        constraints.fill    = GridBagConstraints.CENTER;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        leftTable.add(userLabel, constraints, 0, 0);
        leftTable.add(userValue, constraints, 1, 0);
        leftTable.add(mailLabel, constraints, 0, 1);
        leftTable.add(mailValue, constraints, 1, 1);
        leftTable.add(dateLabel, constraints, 0, 2);
        leftTable.add(dateValue, constraints, 1, 2);
        leftTable.add(ipLabel, constraints, 0, 3);
        leftTable.add(ipValue, constraints, 1, 3);
        
        constraints         = rightTable.getConstraints();
        
        constraints.fill    = GridBagConstraints.CENTER;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        rightTable.add(oldLabel, constraints, 0, 0);
        rightTable.add(oldPass, constraints, 1, 0);
        rightTable.add(newLabel, constraints, 0, 1);
        rightTable.add(newPass, constraints, 1, 1);
        rightTable.add(confirmLabel, constraints, 0, 2);
        rightTable.add(confirm, constraints, 1, 2);
        
        constraints         = table.getConstraints();
        
        constraints.fill    = GridBagConstraints.CENTER;
        
        table.add(leftTable, constraints, 0, 0);
        table.add(rightTable, constraints, 1, 0);
        
        return table;
    }
    
    private JPanel bottomPanel()
    {    
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.red);
        table.add(errorLabel, constraints, 0, 0, 0);
        panel.add(table, "Center");
        
        majButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(majButton, "East");
        
        return panel; 
    }

    public JLabel getUserValue()
    {
        return userValue;
    }

    public JLabel getMailValue()
    {
        return mailValue;
    }

    public JLabel getDateValue()
    {
        return dateValue;
    }

    public JLabel getIpValue()
    {
        return ipValue;
    }

    public JPasswordField getOldPass()
    {
        return oldPass;
    }

    public JPasswordField getNewPass()
    {
        return newPass;
    }

    public JPasswordField getConfirm()
    {
        return confirm;
    }

    public JButton getMajButton()
    {
        return majButton;
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
