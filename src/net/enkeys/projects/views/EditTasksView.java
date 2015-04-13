
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.models.TaskTable;

public class EditTasksView extends EView
{
    private final TaskTable dataTable                       = new TaskTable();
    private final JTable listTasks                          = new JTable(dataTable);
    private final JScrollPane listScroller                  = new JScrollPane(listTasks);
    private final JLabel nameLabel                          = new JLabel("Nom :");
    private final JTextField nameField                      = new JTextField(20);
    private final JLabel priorityTaskLabel                  = new JLabel("Priorité : ");
    private final SpinnerNumberModel priorityTaskSpinner    = new SpinnerNumberModel(10, 1, 100, 1); 
    private final JSpinner priorityTask                     = new JSpinner(priorityTaskSpinner);
    private final JButton deleteButton                      = new JButton("Supprimer la sélection");
    private final JButton addButton                         = new JButton("Ajouter");
    private final JButton saveButton                        = new JButton(" Enregistrer");
    private final JButton backButton                        = new JButton("", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel                         = new JLabel("");
    
    public EditTasksView() 
    {
        super();
        
        add(listScroller, "Center");
        add(bottomPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Edition des tâches"));
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
        
        buttons.add(nameLabel,          constraints, 0, 0);
        buttons.add(nameField,          constraints, 1, 0);
        buttons.add(priorityTaskLabel,  constraints, 2, 0);
        buttons.add(priorityTask,       constraints, 3, 0);
        buttons.add(addButton,          constraints, 4, 0);
        buttons.add(deleteButton,       constraints, 5, 0);
        buttons.add(saveButton,         constraints, 6, 0);
        panel.add(buttons, "East");
       
        return panel; 
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JLabel getErrorLabel()
    {
        return errorLabel;
    } 

    public TaskTable getDataTable() {
        return dataTable;
    }

    public JTable getListTasks() {
        return listTasks;
    }

    public JScrollPane getListScroller() {
        return listScroller;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JLabel getPriorityTaskLabel() {
        return priorityTaskLabel;
    }

    public SpinnerNumberModel getPriorityTaskSpinner() {
        return priorityTaskSpinner;
    }

    public JSpinner getPriorityTask() {
        return priorityTask;
    }

    public JButton getAddButton() {
        return addButton;
    }
}
