package net.enkeys.projects.views;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
    private final JLabel macrotaskNameLabel         = new JLabel("Nom de la macrotâche : ");
    private final JTextField macrotaskName          = new JTextField(20);  
    private final JButton addDeveloperButton        = new JButton("Ajouter un développeur");
    private final EComboBox developers              = new EComboBox();  
    private final JLabel selectedDevLabel           = new JLabel("Développeurs sélectionnés");
    private final DefaultListModel selectedDevData  = new DefaultListModel();
    private final JList selectedDevList             = new JList(selectedDevData);
    private final JScrollPane selectedDevScroller   = new JScrollPane(selectedDevList);
    
    //Colonne droite
    private final JLabel deadlineLabel              = new JLabel("Deadline : ");
    private final JDateChooser deadline             = new JDateChooser();
    private final JButton addMicrotaskButton        = new JButton("Ajouter une sous-tâche");
    private final JTextField microtaskName          = new JTextField(20);
    private final JLabel selectedTaskLabel          = new JLabel("Liste sous-tâches");
    private final DefaultListModel selectedTaskData = new DefaultListModel();
    private final JList selectedTaskList            = new JList(selectedTaskData);
    private final JScrollPane selectedTaskScroller  = new JScrollPane(selectedTaskList);
    
    private final JButton save                      = new JButton(EResources.loadImageIcon("bouton_enregister.png", 180, 50));
    private final JButton back                      = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel                 = new JLabel("");
    
    public NewMacrotaskView() 
    {
        super();
        
        add(newMacrotaskTable(), "Center");
        add(buttonsPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Nouveau Projet"));
    }

    private ETable newMacrotaskTable() {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
       
        constraints.fill    = GridBagConstraints.HORIZONTAL;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        //Gauche
        table.add(macrotaskNameLabel,       constraints, 0, 0);
        table.add(macrotaskName,            constraints, 1, 0);
        table.add(addDeveloperButton,       constraints, 0, 1);
        developers.setEditable(true);
        table.add(developers,               constraints, 1, 1);
        table.add(selectedDevLabel,         constraints, 0, 2);
        table.add(selectedDevScroller,      constraints, 1, 2, 1, 1, 1);
        
        //Droite
        table.add(deadlineLabel,            constraints, 2, 0);
        table.add(deadline,                 constraints, 3, 0);
        table.add(addMicrotaskButton,       constraints, 2, 1);
        table.add(microtaskName,            constraints, 3, 1);
        table.add(selectedTaskLabel,        constraints, 2, 2);
        table.add(selectedTaskScroller,     constraints, 3, 2, 1, 1, 1);
        
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
}
