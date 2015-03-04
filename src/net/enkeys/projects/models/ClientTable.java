package net.enkeys.projects.models;

import java.util.HashMap;

public class ClientTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"id", "lastname", "firstname", "phonenumber", "email", "enterprise", "siret", "address"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"#", "Nom", "Prénom", "Téléphone", "Email", "Entreprise", "SIRET", "Adresse"};
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex > 0);
    }
    
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
