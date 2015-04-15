package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class QuotationView extends EView
{
    private final JLabel devLabel                           = new JLabel("Salaire horaire développeurs (€) : ");
    private final JLabel leaddevLabel                       = new JLabel("Salaire horaire Lead développeurs (€) : ");
    private final JLabel devPercentLabel                    = new JLabel("Charge de travail développeurs (%) : ");
    private final JLabel leaddevPercentLabel                = new JLabel("Charge de travail Lead développeurs (%) : ");
    
    private final SpinnerNumberModel devModel               = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
    private final SpinnerNumberModel leaddevModel           = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
    private final SpinnerNumberModel devPercentModel        = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
    private final SpinnerNumberModel leaddevPercentModel    = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
    
    private final JSpinner devSpinner                       = new JSpinner(devModel);
    private final JSpinner leaddevSpinner                   = new JSpinner(leaddevModel);
    private final JSpinner devPercentSpinner                = new JSpinner(devPercentModel);
    private final JSpinner leaddevPercentSpinner            = new JSpinner(leaddevPercentModel);
    
    private final JButton backButton                        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton generateButton                    = new JButton("Générer");
    private final JLabel errorLabel                         = new JLabel("");
    
    public QuotationView()
    {
        super();
        
        add(quotationTable(), "Center");
        add(bottomPanel(), "South");
        setBorder(new TitledBorder(new EtchedBorder(), "Estimation de devis"));
    }
    
    private ETable quotationTable()
    {
        ETable panel = new ETable();
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        constraints.fill = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 15, 15, 15);
        
        devSpinner.setName("dev_spinner");
        table.add(devLabel, constraints, 0, 0);
        table.add(devSpinner, constraints, 1, 0);
        
        devSpinner.setName("dev_percent_spinner");
        table.add(devPercentLabel, constraints, 2, 0);
        table.add(devPercentSpinner, constraints, 3, 0);
        
        devSpinner.setName("leaddev_spinner");
        table.add(leaddevLabel, constraints, 0, 1);
        table.add(leaddevSpinner, constraints, 1, 1);
        
        devSpinner.setName("leaddev_percent_spinner");
        table.add(leaddevPercentLabel, constraints, 2, 1);
        table.add(leaddevPercentSpinner, constraints, 3, 1);
        
        constraints = panel.getConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        panel.add(table, constraints, 0, 0);
        
        return panel;
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        ETable buttons = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.red);
        table.add(errorLabel, constraints, 0, 0, 0);
        panel.add(table, "Center");
        
        constraints = buttons.getConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.CENTER;
        buttons.add(generateButton, constraints, 0, 0);
        panel.add(buttons, "East");
        
        return panel;
    }

    public JSpinner getDevSpinner()
    {
        return devSpinner;
    }

    public JSpinner getLeaddevSpinner()
    {
        return leaddevSpinner;
    }

    public JSpinner getDevPercentSpinner()
    {
        return devPercentSpinner;
    }

    public JSpinner getLeaddevPercentSpinner()
    {
        return leaddevPercentSpinner;
    }

    public JButton getGenerateButton()
    {
        return generateButton;
    }

    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
    
    public JButton getBackButton()
    {
        return backButton;
    }
}
