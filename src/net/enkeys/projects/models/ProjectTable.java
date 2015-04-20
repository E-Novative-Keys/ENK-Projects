
package net.enkeys.projects.models;

import java.util.HashMap;

public class ProjectTable extends TableModel
{
    @Override
    protected String[] setKeys() 
    {
        return new String[] {"name", "client_name", "deadline", "estimation", "budget", "discount", "created"};
    }

    @Override
    protected String[] setColumns() 
    {
        return new String[] {"Nom", "Client", "Deadline", "Estimation", "Budget", "Remise (%)", "Créé le"};
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    public HashMap<String, String> getProjectByID(int id)
    {
        for(HashMap<String, String> p : values)
        {
            if(Integer.parseInt(p.get("id")) == id)
                return p;
        }
        return null;
    }
}
