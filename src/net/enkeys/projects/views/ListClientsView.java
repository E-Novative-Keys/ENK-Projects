package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.models.ClientTable;

public class ListClientsView extends EView
{
    private final ClientTable dataTable = new ClientTable();
    private final JTable listClients = new JTable(dataTable);
    private final JScrollPane listScroller = new JScrollPane(listClients);
    private final JButton backButton = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton deleteButton = new JButton("Supprimer la sélection");
    private final JButton saveButton = new JButton("Enregistrer les modifications");
    
    public ListClientsView()
    {
        super();
        
        add(listScroller, "Center");
        add(buttonsPanel(), "South");
    }

    private JPanel buttonsPanel()
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
        
        table.add(deleteButton, constraints, 1, 0);
        table.add(saveButton, constraints, 2, 0);
        panel.add(table, "East");
       
        return panel; 
    }

    public ClientTable getDataTable()
    {
        return dataTable;
    }
    
    public JTable getListClients()
    {
        return listClients;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JButton getDeleteButton()
    {
        return deleteButton;
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }
}
