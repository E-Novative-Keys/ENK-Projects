package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;

public class HomeView extends EView
{
    private final JButton newClientButton = new JButton("Nouveau Client", EResources.loadImageIcon("profile.png"));
    private final JButton listClientsButton = new JButton("Liste des Clients", EResources.loadImageIcon("profile.png"));
    private final JButton currentProjectsButton = new JButton("Projets en cours", EResources.loadImageIcon("profile.png"));
    private final JButton newProjectButton = new JButton("Nouveau Projet", EResources.loadImageIcon("profile.png"));
    private final JButton listProjectsButton = new JButton("Liste des Projets", EResources.loadImageIcon("profile.png"));
    private final JButton configButton = new JButton("Configuration", EResources.loadImageIcon("profile.png"));
    
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
        
        newClientButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newClientButton.setHorizontalTextPosition(SwingConstants.CENTER);
        listClientsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        listClientsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        currentProjectsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        currentProjectsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newProjectButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
        listProjectsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        listProjectsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        configButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        configButton.setHorizontalTextPosition(SwingConstants.CENTER);
        
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
