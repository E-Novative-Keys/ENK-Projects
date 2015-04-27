package net.enkeys.framework.components.rules;

import java.util.regex.Pattern;
import net.enkeys.framework.exceptions.ERuleException;

/**
 * Règle de validation : la donnée validée doit correspondre à la regex donnée.
 * @author E-Novative Keys
 * @version 1.0
 */
public class RegexRule extends Rule
{
    protected final Pattern regex;
            
    public RegexRule(String regex, String message)
    {
        super(message);
        
        if(!regex.startsWith("^"));
            regex = "^" + regex;
        if(!regex.endsWith("$"))
            regex = regex + "$";
        
        this.regex = Pattern.compile(regex);
    }

    @Override
    public boolean validate(Object object)
    {
        if(object instanceof String)
            return regex.matcher((String)object).matches();
        else
            throw new ERuleException("Unsupported object");
    }
    
}
