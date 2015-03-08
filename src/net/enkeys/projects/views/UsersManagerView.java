package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class UsersManagerView extends EView
{
    private final JButton newUserButton = new JButton(EResources.loadImageIcon("newClient.png"));
    private final JButton listUsersButton = new JButton(EResources.loadImageIcon("listClients.png"));
    private final JButton backButton = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    
    public UsersManagerView()
    {
        super();
        add(UsersManagerPanel(), "Center");
        add(backPanel(), "South");
    }
    
    private JPanel UsersManagerPanel()
    {
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Users Manager"));
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(15, 15, 15, 15);
 
        table.add(newUserButton, constraints, 0, 0);
        table.add(listUsersButton, constraints, 1, 0);
        
        return table; 
    }
    
    private JPanel backPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        
        panel.add(backButton, "West");
        
        return panel;
    }

    public JButton getNewUserButton() {
        return newUserButton;
    }

    public JButton getListUsersButton() {
        return listUsersButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
