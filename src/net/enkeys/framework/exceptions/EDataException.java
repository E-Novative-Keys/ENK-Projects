package net.enkeys.framework.exceptions;

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
