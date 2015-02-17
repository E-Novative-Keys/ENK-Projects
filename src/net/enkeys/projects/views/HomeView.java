package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;

public class HomeView extends EView
{
    private final JButton newClientButton = new JButton(EResources.loadImageIcon("newClient.png"));
    private final JButton listClientsButton = new JButton(EResources.loadImageIcon("listClients.png"));
    private final JButton currentProjectsButton = new JButton(EResources.loadImageIcon("currentProjects.png"));
    private final JButton newProjectButton = new JButton(EResources.loadImageIcon("newProject.png"));
    private final JButton listProjectsButton = new JButton(EResources.loadImageIcon("listProjects.png"));
    private final JButton configButton = new JButton(EResources.loadImageIcon("configurations.png"));
    
    public HomeView()
    {
        super();
        add(homePanel());
    }
    
    private JPanel homePanel()
    {
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Home"));
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(15, 15, 15, 15);
        
        table.add(newClientButton, constraints, 0, 0);
        table.add(listClientsButton, constraints, 1, 0);
        table.add(currentProjectsButton, constraints, 2, 0);
        table.add(newProjectButton, constraints, 0, 1);
        table.add(listProjectsButton, constraints, 1, 1);
        table.add(configButton, constraints, 2, 1);
        
        return table;
    }

    public JButton getNewClientButton()
    {
        return newClientButton;
    }

    public JButton getListClientsButton()
    {
        return listClientsButton;
    }

    public JButton getCurrentProjectsButton()
    {
        return currentProjectsButton;
    }

    public JButton getNewProjectButton()
    {
        return newProjectButton;
    }

    public JButton getListProjectsButton()
    {
        return listProjectsButton;
    }

    public JButton getConfigButton()
    {
        return configButton;
    }
}
