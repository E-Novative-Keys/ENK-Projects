package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modele ScheduleTable
 * Table schedule non éditable
 * -> vue des macrotâches associées
 *    au projet
 * @extends EModel
 * @author E-Novative Keys
 */
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
    
    /**
     * Edition de la table
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
     * Récupération d'une ligne macrotâche par id
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
