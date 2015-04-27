package net.enkeys.framework.components.rules;

/**
 * Règle de validation : la donnée validée doit être contenue dans le message de la règle.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EscapeRule extends Rule
{
    public EscapeRule(String message)
    {
        super(message);
    }

    @Override
    public boolean validate(Object object)
    {
        return (object instanceof String && getMessage().contains((String)object));
    }
}
