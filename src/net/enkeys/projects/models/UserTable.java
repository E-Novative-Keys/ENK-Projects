package net.enkeys.projects.models;

import java.util.HashMap;

public class UserTable extends TableModel
{
    @Override
    protected String[] setKeys()
    {
        return new String[] {"id", "lastname", "firstname", "email", "role", "validated", "lastlogin", "lastip"};
    }
    
    @Override
    protected String[] setColumns()
    {
        return new String[] {"#", "Nom", "Prénom", "Email", "Rôle", "Validé", "Dernière connexion", "Dernière IP"};
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex != 0 && columnIndex != 6 && columnIndex != 7);
    }
    
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
