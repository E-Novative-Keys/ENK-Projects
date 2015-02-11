package net.enkeys.framework.components.rules;

import net.enkeys.framework.exceptions.ERuleException;

public class NotEmptyRule extends Rule
{
    public NotEmptyRule(String message)
    {
        super(message);
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof String)
            return !((String)object).isEmpty();
        else if(object instanceof char[])
            return ((char[])object).length > 0;
        else
            throw new ERuleException("Unsupported object");
    }
    
}
