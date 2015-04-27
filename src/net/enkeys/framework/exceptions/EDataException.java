package net.enkeys.framework.exceptions;

/**
 * Classe d'exception relative aux données
 * @author E-Novative Keys
 * @version 1.0
 */
public class EDataException extends EException
{
    public EDataException(String str)
    {
        super(str);
    }
    
    public EDataException(String str, Throwable thr)
    {
        super(str, thr);
    }
}
