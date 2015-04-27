package net.enkeys.projects.models;

import java.util.HashMap;

/**
 * Modèle de données pour tables de Clients.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class ClientTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"lastname", "firstname", "phonenumber", "email", "enterprise", "siret", "address"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"Nom", "Prénom", "Téléphone", "Email", "Entreprise", "SIRET", "Adresse"};
    }

    /**
     * Edition de la table.
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
     * Récupération d'une ligne client par id.
     * @param id
     * @return Client
     */
    public HashMap<String, String> getClientByID(int id)
    {
        for(HashMap<String, String> c : values)
        {
            if(Integer.parseInt(c.get("id")) == id)
                return c;
        }
        return null;
    }
}
