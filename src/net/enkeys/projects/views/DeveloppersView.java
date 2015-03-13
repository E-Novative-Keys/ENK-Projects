package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class DeveloppersView extends EView
{
    private final DefaultListModel usersData    = new DefaultListModel();
    private final DefaultListModel devData      = new DefaultListModel();
    private final JList usersList               = new JList(usersData);
    private final JList devList                 = new JList(devData);
    private final JScrollPane userScroller      = new JScrollPane(usersList);
    private final JScrollPane devScroller       = new JScrollPane(devList);
    private final JButton adddev                = new JButton("Ajouter");
    private final JButton deldev                = new JButton("Retirer");
    private final JButton backButton            = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    
    public DeveloppersView()
    {
        super();
        add(devPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private JPanel devPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        ETable buttons = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Affectation des développeurs"));
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(0, 10, 0, 10);
        
        usersList.setLayoutOrientation(JList.VERTICAL);
        usersList.setVisibleRowCount(-1);
        devList.setLayoutOrientation(JList.VERTICAL);
        devList.setVisibleRowCount(-1);
        
        table.add(userScroller, constraints, 0, 0, 1, 1, 1);
        table.add(devScroller, constraints, 1, 0, 1, 1, 1);
        panel.add(table, "Center");
        
        buttons.add(adddev, constraints, 0, 0);
        buttons.add(deldev, constraints, 1, 0);
        panel.add(buttons, "South");
        
        
        return panel;
    }
    
    private JPanel bottomPanel()
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

    public DefaultListModel getUsersData() {
        return usersData;
    }

    public DefaultListModel getDevData() {
        return devData;
    }

    public JList getUsersList() {
        return usersList;
    }

    public JList getDevList() {
        return devList;
    }

    public JScrollPane getUserScroller() {
        return userScroller;
    }

    public JScrollPane getDevScroller() {
        return devScroller;
    }

    public JButton getAdddev() {
        return adddev;
    }

    public JButton getDeldev() {
        return deldev;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
