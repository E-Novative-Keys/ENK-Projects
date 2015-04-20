
package net.enkeys.projects.views;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.EComboBox;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class NewProjectView extends EView
{
    //Colonne gauche
    private final JLabel clientLabel            = new JLabel("Client : ");
    private final EComboBox client              = new EComboBox();
    private final JLabel projectNameLabel       = new JLabel("Nom du projet : ");
    private final JTextField projectName        = new JTextField(20);
    private final JLabel descriptionLabel       = new JLabel("Description");
    private final JTextArea description         = new JTextArea(9, 20);
    private final JScrollPane descriptionPane   = new JScrollPane(description);
    
    //Colonne droite
    private final JLabel leadLabel                          = new JLabel("Référent : ");
    private final EComboBox lead                            = new EComboBox();
    private final JLabel deadlineLabel                      = new JLabel("Deadline : ");
    private final JDateChooser deadline                     = new JDateChooser();
    private final JLabel estimationLabel                    = new JLabel("Estimation (€) : "); 
    private final JTextField estimation                     = new JTextField(20);
    private final JLabel budgetLabel                        = new JLabel("Budget (€) :");
    private final JTextField budget                         = new JTextField(20);
    private final JLabel discountLabel                      = new JLabel("Remise (%) : ");
    private final SpinnerNumberModel discountSpinner        = new SpinnerNumberModel(0, 0, 100, 0.2);
    private final JSpinner discount                         = new JSpinner(discountSpinner);
    
    private final JButton save                  = new JButton(EResources.loadImageIcon("bouton_enregister.png", 180, 50));
    private final JButton back                  = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel             = new JLabel("");
          
    public NewProjectView() 
    {
        super();
        
        add(newProjectTable(), "Center");
        add(buttonsPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Nouveau Projet"));
    }
    
    private ETable newProjectTable()
    {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
       
        constraints.fill    = GridBagConstraints.HORIZONTAL;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        //Gauche
        table.add(clientLabel,      constraints, 0, 0);
        client.setEditable(true);
        table.add(client,           constraints, 1, 0);
        table.add(projectNameLabel, constraints, 0, 1);
        table.add(projectName,      constraints, 1, 1);
        table.add(descriptionLabel, constraints, 0, 2);
        description.setLineWrap(true);
        table.add(descriptionPane,  constraints, 1, 2, 1, 3);
        
        //Droite
        table.add(leadLabel,        constraints, 2, 0);
        lead.setEditable(true);
        table.add(lead,             constraints, 3, 0);
        table.add(deadlineLabel,    constraints, 2, 1);
        table.add(deadline,         constraints, 3, 1);
        table.add(estimationLabel,  constraints, 2, 2);
        table.add(estimation,       constraints, 3, 2);
        table.add(budgetLabel,      constraints, 2, 3);
        table.add(budget,           constraints, 3, 3);
        table.add(discountLabel,    constraints, 2, 4);
        table.add(discount,         constraints, 3, 4);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        panel.add(table, constraints, 0, 0);
        
        return panel;
    }
    
    private JPanel buttonsPanel()
    {
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
    
    public JButton getBack()
    {
        return back;
    }
    
    public JLabel getClientLabel() {
        return clientLabel;
    }
    
    public JComboBox getClient() 
    {
        return client;
    }
    
    public JLabel getProjectNameLabel() {
        return projectNameLabel;
    }
    
    public JTextField getProjectName() 
    {
        return projectName;
    }
    
    public JLabel getDescriptionLabel() {
        return descriptionLabel;
    }
 
    public JTextArea getDescription() 
    {
        return description;
    }
   
    public JScrollPane getDescriptionPane() {
        return descriptionPane;
    }
   
    public JLabel getLeadLabel() {
        return leadLabel;
    }

    public JComboBox getLead() 
    {
        return lead;
    }

    public JLabel getDeadlineLabel() {
        return deadlineLabel;
    }
    
    public JDateChooser getDeadline() 
    {
        return deadline;
    }
 
    public JLabel getEstimationLabel() {
        return estimationLabel;
    }
    
    public JTextField getEstimation() 
    {
        return estimation;
    }
 
    public JLabel getBudgetLabel() {
        return budgetLabel;
    }
  
    public JTextField getBudget() 
    {
        return budget;
    }
  
    public JLabel getDiscountLabel() {
        return discountLabel;
    }
 
    public JSpinner getDiscount() 
    {
        return discount;
    }
    
    public JButton getSave()
    {
        return save;
    }
    
    public JLabel getErrorLabel()
    {
        return errorLabel;
    }

    public SpinnerNumberModel getDiscountSpinner() {
        return discountSpinner;
    } 
}
