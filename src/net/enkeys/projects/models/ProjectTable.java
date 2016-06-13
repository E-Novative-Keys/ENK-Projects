
package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modèle de données ProjectTable.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
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
     * Récupération d'une ligne projet par id.
     * @param id
     * @return 
     */
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
