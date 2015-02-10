package net.enkeys.framework.exceptions;

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
