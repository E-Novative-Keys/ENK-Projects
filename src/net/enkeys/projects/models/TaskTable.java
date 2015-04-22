
package net.enkeys.projects.models;

import java.util.HashMap;

public class TaskTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"priority", "name", "priority", "progress", "hours"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"Priorité", "Nom", "Priorité", "Terminé" , "Heures effectuées"};
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex > 0);
    }
    
    public HashMap<String, String> getTaskByID(int id)
    {
        for(HashMap<String, String> t : values)
        {
            if(Integer.parseInt(t.get("id")) == id)
                return t;
        }
        return null;
    }
}
