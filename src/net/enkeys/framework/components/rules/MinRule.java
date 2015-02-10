package net.enkeys.framework.components.rules;

import net.enkeys.framework.exceptions.ERuleException;

public class MinRule extends Rule
{
    protected final int min;
    
    public MinRule(int min, String message)
    {
        super(message);
        this.min = min;
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof Integer)
        {
            return (int)object > min;
        }
        else if(object instanceof Short)
        {
            return (short)object > (short)min;
        }
        else if(object instanceof Long)
        {
            return (long)object > (long)min;
        }
        else if(object instanceof Float)
        {
            return (float)object > (float)min;
        }
        else if(object instanceof Double)
        {
            return (double)object > (double)min;
        }
        else if(object instanceof String)
        {
            return ((String)object).length() > min;
        }
        else
            throw new ERuleException("Unsupported object");
    }
}
