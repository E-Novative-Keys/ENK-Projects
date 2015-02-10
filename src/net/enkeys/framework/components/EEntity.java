package net.enkeys.framework.components;

import java.util.ArrayList;

public abstract class EEntity
{
    public static final ArrayList<Integer> attributed = new ArrayList<>();
    public final int id;
    
    public EEntity()
    {
        this.id = getNewEntityID();
    }
    
    
    private int getNewEntityID()
    {
        int i;
        
        for(i = 0 ; i < EEntity.attributed.size() ; i++)
        {
            if(EEntity.attributed.indexOf(i) == -1)
            {
                EEntity.attributed.add(i);
                return i;
            }
        }
            
        EEntity.attributed.add(i);
        return i;
    }
}
