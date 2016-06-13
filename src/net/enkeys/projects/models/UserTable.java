package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modèle de données UserTable.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class UserTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"lastname", "firstname", "email", "role", "validated", "lastlogin", "lastip"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"Nom", "Prénom", "Email", "Rôle", "Validé", "Dernière connexion", "Dernière IP"};
    }

    /**
     * Récupération d'une ligne projet par id.
     * @param id
     * @return boolean
     * 
     * Pas d'edit sur lastlogin & lastip
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex != 6 && columnIndex != 7);
    }
    
    /**
     * Récupération d'une ligne utilisateur par id.
     * @param id
     * @return User
     */
    public HashMap<String, String> getUserByID(int id)
    {
        for(HashMap<String, String> v : values)
        {
            if(Integer.parseInt(v.get("id")) == id)
                return v;
        }
        return null;
    }
}
