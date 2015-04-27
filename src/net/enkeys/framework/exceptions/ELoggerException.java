package net.enkeys.framework.exceptions;

/**
 * Classe d'exception relative au Logger.
 * @author E-Novative Keys
 * @version 1.0
 */
public class ELoggerException extends EException
{
    public ELoggerException(String str)
    {
        super(str);
    }
    
    public ELoggerException(String str, Throwable thr)
    {
        super(str, thr);
    }
}
