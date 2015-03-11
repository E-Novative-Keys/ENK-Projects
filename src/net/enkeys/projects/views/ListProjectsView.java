/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
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
import net.enkeys.projects.models.ProjectTable;

/**
 *
 * @author Worker
 */
public class ListProjectsView extends EView
{
    private final ProjectTable dataTable    = new ProjectTable();
    private final JTable listProjects       = new JTable(dataTable);
    private final JScrollPane listScroller  = new JScrollPane(listProjects);
    private final JLabel searchLabel        = new JLabel("Recherche :");
    private final JTextField searchField    = new JTextField(20);
    private final JButton backButton        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton deleteButton      = new JButton("Supprimer la sélection");
    private final JButton saveButton        = new JButton("Enregistrer les modifications");
    
    public ListProjectsView()
    {
        super();
        
        add(listScroller, "Center");
        add(bottomPanel(), "South");
    }

    private JPanel bottomPanel() 
    {
        JPanel panel    = new JPanel(new BorderLayout());
        ETable buttons  = new ETable();
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
     
    public ProjectTable getDataTable() 
    {
        return dataTable;
    }

    public JTable getListProjects() 
    {
        return listProjects;
    }

    public JScrollPane getListScroller() 
    {
        return listScroller;
    }

    public JLabel getSearchLabel() 
    {
        return searchLabel;
    }

    public JTextField getSearchField() 
    {
        return searchField;
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
