package net.enkeys.framework.components.rules;

import java.util.regex.Pattern;
import net.enkeys.framework.exceptions.ERuleException;

public class PhoneNumberRule extends Rule
{
    protected String PHONENUMBER_REGEX = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$";
    
    public PhoneNumberRule(String message)
    {
        super(message);
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof String)
            return Pattern.matches(PHONENUMBER_REGEX, (String)object);
        else
            throw new ERuleException("Unsupported object"); 
    }
    
}
