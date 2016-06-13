package net.enkeys.framework.exceptions;

/**
 * Classe d'exception relative aux ressources.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EResourceException extends EException
{
    public EResourceException(String str)
    {
        super(str);
    }
    
    public EResourceException(String str, Throwable thr)
    {
        super(str, thr);
    }
}
