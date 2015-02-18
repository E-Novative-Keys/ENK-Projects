package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
    private final JLabel searchLabel = new JLabel("Recherche :");
    private final JTextField searchField = new JTextField(20);
    private final JButton backButton = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton deleteButton = new JButton("Supprimer la sélection");
    private final JButton saveButton = new JButton("Enregistrer les modifications");
    
    public ListClientsView()
    {
        super();
        
        add(listScroller, "Center");
        add(bottomPanel(), "South");
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
        
        buttons.add(searchLabel, constraints, 0, 0);
        buttons.add(searchField, constraints, 1, 0);
        buttons.add(deleteButton, constraints, 2, 0);
        buttons.add(saveButton, constraints, 3, 0);
        panel.add(buttons, "East");
       
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
    
    public JTextField getSearchField()
    {
        return searchField;
    }
}
