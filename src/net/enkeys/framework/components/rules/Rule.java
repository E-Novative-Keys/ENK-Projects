package net.enkeys.framework.components.rules;

public abstract class Rule
{
    protected final String message;
    
    public Rule(String message)
    {
        this.message = message;
    }
    
    public abstract boolean validate(Object object);
    
    /**
     * Renvoie le message d'erreur de la règle de validation.
     * @return 
     */
    public final String getMessage()
    {
        return message;
    }
    
    /**
     * Renvoie le nom de la règle de validation.
     * @return 
     */
    public final String getName()
    {
        return this.getClass().getSimpleName();
    }
}
