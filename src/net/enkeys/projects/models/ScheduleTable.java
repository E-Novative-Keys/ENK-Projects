/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.models;

import java.util.HashMap;

public class ScheduleTable extends TableModel 
{
    @Override
    protected String[] setKeys() 
    {
        return new String[] {"id", "name", "progress"};
    }

    @Override
    protected String[] setColumns() 
    {
        return new String[] {"#", "Nom macrotâche", "Avancement (%)"};
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
