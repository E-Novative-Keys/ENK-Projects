package net.enkeys.framework.components.rules;

/**
 * Classe abstraite mère des règles de validation appliquées aux modèles
 * @author E-Novative Keys
 * @version 1.0
 */
public abstract class Rule
{
    protected final String message;
    
    /**
     * Crée une nouvelle instance de type Rule.
     * Initialisation du message d'erreur de la règle.
     * @param message 
     */
    public Rule(String message)
    {
        this.message = message;
    }
    
    /**
     * Méthode abstraite de validation d'un objet quelconque.
     * @param object
     * @return 
     */
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
