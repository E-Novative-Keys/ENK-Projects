package net.enkeys.framework.exceptions;

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
