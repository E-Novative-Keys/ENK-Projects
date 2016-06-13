package net.enkeys.framework.components.rules;

import net.enkeys.framework.exceptions.ERuleException;

/**
 * Règle de validation : la donnée validée doit être contenue entre les bornes
 * min et max.
 * @author E-Novative Keys.
 * @version 1.0
 */
public class BetweenRule extends Rule
{
    protected final int min, max;
    
    public BetweenRule(int min, int max, String message)
    {
        super(message);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof Integer)
        {
            return (int)object < max && (int)object > min;
        }
        else if(object instanceof Short)
        {
            return (short)object < (short)max && (short)object > (short)min;
        }
        else if(object instanceof Long)
        {
            return (long)object < (long)max && (long)object > (long)min;
        }
        else if(object instanceof Float)
        {
            return (float)object < (float)max && (float)object > (float)min;
        }
        else if(object instanceof Double)
        {
            return (double)object < (double)max && (double)object > (double)min;
        }
        else if(object instanceof String)
        {
            return ((String)object).length() < max && ((String)object).length() > min;
        }
        else
            throw new ERuleException("Unsupported object");
    }
    
}
