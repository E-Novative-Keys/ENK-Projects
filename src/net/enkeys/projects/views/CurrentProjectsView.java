package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class CurrentProjectsView extends EView
{
    private final ArrayList<JButton> listCurrentProjectsButton = new ArrayList<>();
    private final ETable panel          = new ETable();
    private final JScrollPane scroller  = new JScrollPane(panel);
    private final JButton backButton    = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    
    public CurrentProjectsView()
    {
        super();
        scroller.setBorder(new TitledBorder(new EtchedBorder(), "Liste des Projets"));
        add(scroller, "Center");
        add(bottomPanel(), "South");
    }
    
    public void buildlistProjectsPanel()
    {
        byte j = 0;
        GridBagConstraints constraints = panel.getConstraints();
               
        constraints.fill    = GridBagConstraints.CENTER;
        constraints.insets  = new Insets(12, 15, 12, 15);
        
        if(listCurrentProjectsButton.size() > 2)
        {   
            for(int i = 0; i < listCurrentProjectsButton.size(); i++)
            {
                if(i % 3 == 0 && i != 0)
                    j++;
                listCurrentProjectsButton.get(i).setPreferredSize(new Dimension(250, 200));
                panel.add(listCurrentProjectsButton.get(i), constraints, i % 3, j, 1, 1, 1, GridBagConstraints.FIRST_LINE_START);
            }
        }
        else if(listCurrentProjectsButton.size() == 2)
        {
            listCurrentProjectsButton.get(0).setPreferredSize(new Dimension(250, 200));
            listCurrentProjectsButton.get(1).setPreferredSize(new Dimension(250, 200));
            panel.add(listCurrentProjectsButton.get(0), constraints, 0, 0, 1, 1, 1, GridBagConstraints.CENTER);
            panel.add(listCurrentProjectsButton.get(1), constraints, 1, 0, 1, 1, 1, GridBagConstraints.CENTER);
        }
        else if(listCurrentProjectsButton.size() == 1)
        {
            listCurrentProjectsButton.get(0).setPreferredSize(new Dimension(250, 200));
            panel.add(listCurrentProjectsButton.get(0), constraints, 0, 0, 1, 1, 1, GridBagConstraints.CENTER);
        }
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel    = new JPanel(new BorderLayout());
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");   
        
        return panel; 
    }

    public ArrayList<JButton> getListCurrentProjectsButton()
    {
        return listCurrentProjectsButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }
}
