package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modèle de données ScheduleTable.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class ScheduleTable extends TableModel 
{
    @Override
    protected String[] setKeys() 
    {
        return new String[] {"name","priority", "progress"};
    }

    @Override
    protected String[] setColumns() 
    {
        return new String[] {"Nom macrotâche", "Priorité", "Avancement (%)"};
    }
    
    /**
     * Edition de la table.
     * @param rowIndex
     * @param columnIndex
     * @return false 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    /**
     * Récupération d'une ligne macrotâche par id.
     * @param id
     * @return Macrotask 
     */
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
