package net.enkeys.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import net.enkeys.framework.exceptions.ECryptoException;

/**
 * Classe statique utilitaire implémentant divers algorithmes de chiffrement,
 * hashage et encodage.
 * @author E-Novative Keys
 * @version 1.0
 */
public class ECrypto
{
    /**
     * Classe Constructeur privé empêchant l'instanciation de la classe.
     * Classe purement statique.
     */
    private ECrypto(){};
    
    /**
     * Encode la chaîne de caractères passée en paramètre en base64.
     * @param str
     * @return
     * @throws ECryptoException 
     */
    public static String base64(String str) throws ECryptoException
    {
        try
        {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new ECryptoException("[Base64] An error occured while encoding string : " + str, e);
        }
    }
    
    /**
     * Renvoie le hash md5 de la chaîne de caractères passée en paramètre.
     * @param str
     * @return
     * @throws ECryptoException 
     */
    public static String md5(String str) throws ECryptoException
    {
        try
        {
            return ECrypto.bytesToString(MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8")));
        }
        catch(UnsupportedEncodingException | NoSuchAlgorithmException e)
        {
            throw new ECryptoException("[MD5] An error occured while hashing string " + str, e);
        }
    }
    
    /**
     * Renvoie le hash sh1 de la chaîne de caractères passée en paramètre.
     * @param str
     * @return
     * @throws ECryptoException 
     */
    public static String sha1(String str) throws ECryptoException
    {
        try
        {
            return ECrypto.bytesToString(MessageDigest.getInstance("SHA-1").digest(str.getBytes("UTF-8")));
        }
        catch(UnsupportedEncodingException | NoSuchAlgorithmException e)
        {
            throw new ECryptoException("[SHA-1] An error occured while hashing string " + str, e);
        }
    }
    
    /**
     * Renvoie une chaîne de caractères à partir du tableau de bytes passé en
     * paramètre.
     * @param bytes
     * @return 
     */
    public static String bytesToString(byte[] bytes)
    {
        String result = "";
        
        for (int i = 0 ; i < bytes.length ; i++)
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        
        return result;
    }
}
