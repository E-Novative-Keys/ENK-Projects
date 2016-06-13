package net.enkeys.framework.components.rules;

import net.enkeys.framework.exceptions.ERuleException;

/**
 * Règle de validation : la donnée validée doit être inférieure à la borne max.
 * @author E-Novative Keys
 * @version 1.0
 */
public class MaxRule extends Rule
{
    protected final int max;
    
    public MaxRule(int max, String message)
    {
        super(message);
        this.max = max;
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof Integer)
        {
            return (int)object < max;
        }
        else if(object instanceof Short)
        {
            return (short)object < (short)max;
        }
        else if(object instanceof Long)
        {
            return (long)object < (long)max;
        }
        else if(object instanceof Float)
        {
            return (float)object < (float)max;
        }
        else if(object instanceof Double)
        {
            return (double)object < (double)max;
        }
        else if(object instanceof String)
        {
            return ((String)object).length() < max;
        }
        else 
            throw new ERuleException("Unsupported object");
    }
}
