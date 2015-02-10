package net.enkeys.framework.exceptions;

public class EException extends RuntimeException
{
    public EException(String str)
    {
        super(str);
    }

    public EException(String str, Throwable thr)
    {
        super(str + " [" + thr.getMessage() + "]", thr);
    }

    public EException(Throwable thr)
    {
        super(thr);
    }
}