package net.enkeys.framework.components.rules;

import java.util.regex.Pattern;
import net.enkeys.framework.exceptions.ERuleException;

/**
 * Règle de validation : la donnée validée doit être une adresse e-mail.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EmailRule extends Rule
{
    protected String EMAIL_REGEX = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$";
    
    public EmailRule(String message)
    {
        super(message);
    }    

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof String)
            return Pattern.matches(EMAIL_REGEX, (String)object);
        else
            throw new ERuleException("Unsupported object");
    }
}
