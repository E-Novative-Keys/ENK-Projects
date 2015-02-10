package net.enkeys.framework.exceptions;

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
