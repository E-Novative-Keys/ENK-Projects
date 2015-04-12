
package net.enkeys.projects.models;

import java.util.HashMap;

public class TaskTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"id", "name", "priority", "progress"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"#", "Nom", "Priorité", "Terminé"};
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
