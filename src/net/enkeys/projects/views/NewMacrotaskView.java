package net.enkeys.projects.views;

import com.toedter.calendar.JDateChooser;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.EComboBox;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class NewMacrotaskView extends EView
{
    //Colonne gauche
    private final JLabel hoursLabel                     = new JLabel("Heures : ");
    private final SpinnerNumberModel hoursSpinner       = new SpinnerNumberModel(10, 0, 23, 1);
    private final JSpinner hours                        = new JSpinner(hoursSpinner);
    private final JLabel priorityLabel                  = new JLabel("Priorité : ");
    private final SpinnerNumberModel prioritySpinner    = new SpinnerNumberModel(10, 1, 100, 1);
    private final JSpinner priority                     = new JSpinner(prioritySpinner);
    private final JLabel developersLabel                = new JLabel("Développeur ->");
    private final JLabel macrotaskNameLabel             = new JLabel("Nom de la macrotâche : ");
    private final JTextField macrotaskName              = new JTextField(20);  
    private final JButton addDeveloperButton            = new JButton("Ajouter un développeur");
    private final EComboBox developers                  = new EComboBox();  
    private final JLabel selectedDevLabel               = new JLabel("Développeurs sélectionnés");
    private final DefaultListModel selectedDevData      = new DefaultListModel();
    private final JList selectedDevList                 = new JList(selectedDevData);
    private final JScrollPane selectedDevScroller       = new JScrollPane(selectedDevList);
    private final JButton supprDeveloperButton          = new JButton("Supprimer la sélection");
    
    //Colonne droite
    private final JLabel minutesLabel                       = new JLabel("Minutes : ");
    private final SpinnerNumberModel minutesSpinner         = new SpinnerNumberModel(42, 0, 59, 1);
    private final JSpinner minutes                          = new JSpinner(minutesSpinner);
    private final JLabel priorityTaskLabel                  = new JLabel("Priorité : ");
    private final SpinnerNumberModel priorityTaskSpinner    = new SpinnerNumberModel(10, 1, 100, 1);
    private final JSpinner priorityTask                     = new JSpinner(priorityTaskSpinner);
    private final JLabel taskLabel                          = new JLabel("Nouvelle tâche :");
    private final JLabel deadlineLabel                      = new JLabel("Deadline : ");
    private final JDateChooser deadline                     = new JDateChooser();
    private final JButton addMicrotaskButton                = new JButton("Ajouter une tâche");
    private final JTextField microtaskName                  = new JTextField(20);
    private final JLabel selectedTaskLabel                  = new JLabel("Liste des tâches");
    private final DefaultListModel selectedTaskData         = new DefaultListModel();
    private final JList selectedTaskList                    = new JList(selectedTaskData);
    private final JScrollPane selectedTaskScroller          = new JScrollPane(selectedTaskList);
    private final JButton supprMicrotaskButton              = new JButton("Supprimer la selection");
    
    private final JButton save                      = new JButton(EResources.loadImageIcon("bouton_enregister.png", 180, 50));
    private final JButton back                      = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel                 = new JLabel("");
    
    public NewMacrotaskView() 
    {
        super();
        
        add(newMacrotaskTable(), "Center");
        add(buttonsPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Nouvelle Macrotâche"));
    }

    private ETable newMacrotaskTable() {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
       
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(10, 10, 10, 10);
        
        //Gauche
        table.add(macrotaskNameLabel,       constraints, 0, 0);
        table.add(macrotaskName,            constraints, 1, 0);
        table.add(hoursLabel,               constraints, 0, 1);
        table.add(hours,                    constraints, 1, 1);
        table.add(priorityLabel,            constraints, 0, 2);
        table.add(priority,                 constraints, 1, 2);
        table.add(selectedDevLabel,         constraints, 0, 3);
        table.add(selectedDevScroller,      constraints, 1, 3, 1, 1, 1);
        table.add(developersLabel,          constraints, 0, 4);
        developers.setEditable(true);
        table.add(developers,               constraints, 1, 4);
        table.add(addDeveloperButton,       constraints, 1, 5);
        table.add(supprDeveloperButton,     constraints, 1, 6);
        
        //Droite
        table.add(deadlineLabel,            constraints, 2, 0);
        table.add(deadline,                 constraints, 3, 0);
        table.add(minutesLabel,             constraints, 2, 1);
        table.add(minutes,                  constraints, 3, 1);
        table.add(taskLabel,                constraints, 2, 2);
        table.add(microtaskName,            constraints, 3, 2);
        table.add(selectedTaskLabel,        constraints, 2, 3);
        table.add(selectedTaskScroller,     constraints, 3, 3, 1, 1, 1);
        table.add(priorityTaskLabel,        constraints, 2, 4);
        table.add(priorityTask,             constraints, 3, 4);
        table.add(addMicrotaskButton,       constraints, 3, 5);
        table.add(supprMicrotaskButton,     constraints, 3, 6);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        panel.add(table, constraints, 0, 0);
        
        return panel;
    }

    private JPanel buttonsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setOpaque(false);
        back.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(back, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.red);
        table.add(errorLabel, constraints, 0, 0, 0);
        panel.add(table, "Center");
        
        save.setBorderPainted(false);
        save.setContentAreaFilled(false);
        save.setFocusPainted(false);
        save.setOpaque(false);
        save.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(save, "East");
        
        return panel;
    }
     
    public JLabel getMacrotaskNameLabel()
    {
        return macrotaskNameLabel;
    }

    public JTextField getMacrotaskName()
    {
        return macrotaskName;
    }

    public JButton getAddDeveloperButton()
    {
        return addDeveloperButton;
    }

    public EComboBox getDevelopers()
    {
        return developers;
    }

    public JLabel getSelectedDevLabel()
    {
        return selectedDevLabel;
    }

    public JLabel getDeadlineLabel()
    {
        return deadlineLabel;
    }

    public JDateChooser getDeadline()
    {
        return deadline;
    }

    public JButton getAddMicrotaskButton()
    {
        return addMicrotaskButton;
    }

    public JTextField getMicrotaskName()
    {
        return microtaskName;
    }

    public JLabel getSelectedMicrotasksLabel() 
    {
        return selectedTaskLabel;
    }

    public JButton getSave()
    {
        return save;
    }

    public JButton getBack()
    {
        return back;
    }

    public JLabel getErrorLabel()
    {
        return errorLabel;
    }

    public DefaultListModel getSelectedDevData() {
        return selectedDevData;
    }

    public JList getSelectedDevList() {
        return selectedDevList;
    }

    public JScrollPane getSelectedDevScroller() {
        return selectedDevScroller;
    }

    public JLabel getSelectedTaskLabel() {
        return selectedTaskLabel;
    }

    public DefaultListModel getSelectedTaskData() {
        return selectedTaskData;
    }

    public JList getSelectedTaskList() {
        return selectedTaskList;
    }

    public JScrollPane getSelectedTaskScroller() {
        return selectedTaskScroller;
    }

    public JButton getSupprDeveloperButton() {
        return supprDeveloperButton;
    }

    public JButton getSupprMicrotaskButton() {
        return supprMicrotaskButton;
    }

    public JLabel getDevelopersLabel() {
        return developersLabel;
    }

    public JLabel getPriorityLabel() {
        return priorityLabel;
    }

    public JSpinner getPriority() {
        return priority;
    }

    public JLabel getTaskLabel() {
        return taskLabel;
    }

    public SpinnerNumberModel getPrioritySpinner() {
        return prioritySpinner;
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

    public JLabel getHoursLabel() {
        return hoursLabel;
    }

    public JLabel getMinutesLabel() {
        return minutesLabel;
    }

    public SpinnerNumberModel getHoursSpinner() {
        return hoursSpinner;
    }

    public JSpinner getHours() {
        return hours;
    }

    public SpinnerNumberModel getMinutesSpinner() {
        return minutesSpinner;
    }

    public JSpinner getMinutes() {
        return minutes;
    }
    
}
