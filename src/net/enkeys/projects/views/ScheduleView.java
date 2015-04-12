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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.models.ScheduleTable;

public class ScheduleView extends EView
{
    private final ScheduleTable dataTable   = new ScheduleTable();
    private final JTable listMacrotasks     = new JTable(dataTable);
    private final JScrollPane listScroller  = new JScrollPane(listMacrotasks);
    private final JButton backButton        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton deleteButton      = new JButton("Supprimer la sélection");
    private final JButton editButton        = new JButton("Modifier une macrotâche");
    private final JButton editTaskButton    = new JButton("Modifier les sous-tâches");
    private final JButton addButton         = new JButton("Ajouter une macrotâche");
    
    public ScheduleView()
    {
        super();
        
        add(listScroller, "Center");
        add(bottomPanel(), "South");
    }

    private Component bottomPanel() 
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
        
        buttons.add(addButton, constraints, 0, 0);
        buttons.add(editButton, constraints, 1, 0);
        buttons.add(editTaskButton, constraints, 2, 0);
        buttons.add(deleteButton, constraints, 3, 0);
        panel.add(buttons, "East");
       
        return panel;
    }
    
    
    public ScheduleTable getDataTable()
    {
        return dataTable;
    }

    public JTable getListMacrotasks()
    {
        return listMacrotasks;
    }

    public JScrollPane getListScroller()
    {
        return listScroller;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JButton getDeleteButton()
    {
        return deleteButton;
    }

    public JButton getEditButton()
    {
        return editButton;
    }

    public JButton getAddButton()
    {
        return addButton;
    }

    public JButton getEditTaskButton()
    {
        return editTaskButton;
    }
}
