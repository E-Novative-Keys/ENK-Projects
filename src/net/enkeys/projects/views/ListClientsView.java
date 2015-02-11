package net.enkeys.projects.views;

import java.awt.GridBagConstraints;
import javax.swing.JTable;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;

public class ListClientsView extends EView
{
    private final String[] columns = {"Nom", "Prénom", "Téléphone", "Email", "Entreprise"};
    private final JTable listClients = new JTable(null, columns);
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
}
