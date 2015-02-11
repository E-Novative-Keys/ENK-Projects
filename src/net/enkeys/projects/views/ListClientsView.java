package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import javax.swing.JTable;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.models.ClientTable;

public class ListClientsView extends EView
{
    private final ClientTable dataTable = new ClientTable();
    private final JTable listClients = new JTable(dataTable);
    
    public ListClientsView()
    {
        super();
        add(listClientsTable());
    }

    private ETable listClientsTable()
    {
       ETable table = new ETable();
       
       GridBagConstraints constraints = table.getConstraints();
       constraints.fill = GridBagConstraints.CENTER;
       
       table.add(listClients, constraints, 0, 0);
       return table; 
    }

    public ClientTable getDataTable()
    {
        return dataTable;
    }
}
