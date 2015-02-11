package net.enkeys.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import net.enkeys.framework.exceptions.ECryptoException;

public class ECrypto
{
    private ECrypto(){};
    
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
    
    public static String bytesToString(byte[] bytes)
    {
        String result = "";
        
        for (int i = 0 ; i < bytes.length ; i++)
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        
        return result;
    }
}
