
package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modele TaskTable
 * Table tâches éditables
 * @extends EModel
 * @author E-Novative Keys
 */
public class TaskTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"name", "priority", "progress", "hours"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"Nom", "Priorité", "Terminé" , "Heures effectuées"};
    }

    /**
     * Edition de la table
     * @param rowIndex
     * @param columnIndex
     * @return true
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
    
    /**
     * Récupération d'une ligne projet par id
     * @param id
     * @return Task
     */
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
