package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;

public class CurrentProjectManagerView extends EView
{
    private final JButton scheduleButton = new JButton(EResources.loadImageIcon("planification.png"));
    private final JButton messengerButton = new JButton(EResources.loadImageIcon("messagerie.png"));
    private final JButton cloudButton = new JButton(EResources.loadImageIcon("cloud.png"));
    private final JButton developpersButton = new JButton(EResources.loadImageIcon("affectation_dvlp.png"));
    private final JButton quotationButton = new JButton(EResources.loadImageIcon("devis.png"));
    private final JButton editProjectButton = new JButton(EResources.loadImageIcon("editer_projet.png"));

    public CurrentProjectManagerView()
    {
        super();
        add(projectPanel());
    }
    
    private ETable projectPanel()
    {
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Home"));
        
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        table.add(scheduleButton, constraints, 0, 0);
        table.add(messengerButton, constraints, 1, 0);
        table.add(cloudButton, constraints, 2, 0);
        table.add(developpersButton, constraints, 0, 1);
        table.add(quotationButton, constraints, 1, 1);
        table.add(editProjectButton, constraints, 2, 1);
        
        return table;
    }
    
    public JButton getScheduleButton()
    {
        return scheduleButton;
    }

    public JButton getMessengerButton()
    {
        return messengerButton;
    }

    public JButton getCloudButton()
    {
        return cloudButton;
    }

    public JButton getDeveloppersButton()
    {
        return developpersButton;
    }

    public JButton getQuotationButton()
    {
        return quotationButton;
    }

    public JButton getEditProjectButton()
    {
        return editProjectButton;
    }
}
