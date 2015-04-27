package net.enkeys.framework.exceptions;

/**
 * Classe d'exception relative aux requêtes HTTP.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EHttpRequestException extends EException
{
    public EHttpRequestException(String str)
    {
        super(str);
    }
    
    public EHttpRequestException(String str, Throwable thr)
    {
        super(str, thr);
    }
}
