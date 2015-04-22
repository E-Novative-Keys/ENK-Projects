package net.enkeys.projects.models;

import java.util.HashMap;

public class ScheduleTable extends TableModel 
{
    @Override
    protected String[] setKeys() 
    {
        return new String[] {"priority", "name", "progress"};
    }

    @Override
    protected String[] setColumns() 
    {
        return new String[] {"Priorité", "Nom macrotâche", "Avancement (%)"};
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    public HashMap<String, String> getMacrotaskByID(int id)
    {
        for(HashMap<String, String> m : values)
        {
            if(Integer.parseInt(m.get("id")) == id)
                return m;
        }
        return null;
    }
}
