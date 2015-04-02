package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class CloudView extends EView
{
    private final DefaultListModel clientsData  = new DefaultListModel();
    private final DefaultListModel devData      = new DefaultListModel();
    private final JList clientsList             = new JList(clientsData);
    private final JList devList                 = new JList(devData);
    private final JScrollPane clientsScroller   = new JScrollPane(clientsList);
    private final JScrollPane devScroller       = new JScrollPane(devList);
    private final JButton backButton            = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel             = new JLabel("");
    
    public CloudView()
    {
        super();
        add(cloudPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private JPanel cloudPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        //ETable buttons = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Cloud"));
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(0, 10, 0, 10);
        
        clientsList.setLayoutOrientation(JList.VERTICAL);
        clientsList.setVisibleRowCount(-1);
        
        devList.setLayoutOrientation(JList.VERTICAL);
        devList.setVisibleRowCount(-1);
        
        table.add(devScroller, constraints, 0, 0, 1, 1, 1);
        table.add(clientsScroller, constraints, 1, 0, 1, 1, 1);
        panel.add(table, "Center");
        
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
        
        errorLabel.setForeground(Color.red);
        panel.add(errorLabel, "Center");
        
        return panel;
    }

    public DefaultListModel getClientsData()
    {
        return clientsData;
    }

    public DefaultListModel getDevData()
    {
        return devData;
    }

    public JList getClientsList()
    {
        return clientsList;
    }

    public JList getDevList()
    {
        return devList;
    }

    public JScrollPane getClientsScroller()
    {
        return clientsScroller;
    }

    public JScrollPane getDevScroller()
    {
        return devScroller;
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
