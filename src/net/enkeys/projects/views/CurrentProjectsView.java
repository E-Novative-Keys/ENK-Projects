package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;

public class CurrentProjectsView extends EView
{
    private ArrayList<JButton> listCurrentProjectsButton = new ArrayList<>();
    
    public CurrentProjectsView()
    {
        super();
        add(listProjectsPanel());
    }
    
    private ETable listProjectsPanel()
    {
        byte j = 0;
        ETable table = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        table.setBorder(new TitledBorder(new EtchedBorder(), "Current Projects"));
        
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(15, 15, 15, 15);
        
        for(int i = 0; i < listCurrentProjectsButton.size(); i++)
        {
            if(i % 3 == 0 && i != 0)
                j++;
            table.add(listCurrentProjectsButton.get(i), constraints, i, j);
        }
            
        return table;
    }

    public ArrayList<JButton> getListCurrentProjectsButton()
    {
        return listCurrentProjectsButton;
    }
}
