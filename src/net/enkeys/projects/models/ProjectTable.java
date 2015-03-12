/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.models;

import java.util.HashMap;

/**
 *
 * @author Worker
 */
public class ProjectTable extends TableModel
{
    @Override
    protected String[] setKeys() 
    {
        return new String[] {"id", "client_id", "name", "deadline", "estimation", "budget", "discount", "created"};
    }

    @Override
    protected String[] setColumns() 
    {
        return new String[] {"#", "#Client", "Nom", "Deadline", "Estimation", "Budget", "Remise (%)", "Créé le"};
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
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
