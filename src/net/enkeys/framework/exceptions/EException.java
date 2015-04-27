package net.enkeys.framework.exceptions;

/**
 * Classe mère de toutes les Exceptions de l'application.
 * Classe étendue de RuntimeException, permettant l'omission du bloc try catch.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EException extends RuntimeException
{
    /**
     * Crée une nouvelle instance de type EException à partir d'une chaîne de
     * caractères.
     * @param str 
     */
    public EException(String str)
    {
        super(str);
    }

    /**
     * Crée une nouvelle instance de type EException à partir d'une chaîne de
     * caractères et d'un objet Throwable.
     * @param str
     * @param thr 
     */
    public EException(String str, Throwable thr)
    {
        super(str + " [" + thr.getMessage() + "]", thr);
    }

    /**
     * Crée une nouvelle instance de type EException à partir d'un objet
     * Throwable.
     * @param thr 
     */
    public EException(Throwable thr)
    {
        super(thr);
    }
}