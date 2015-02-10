package net.enkeys.framework.utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import net.enkeys.framework.exceptions.EResourceException;

public class EResources
{
    private static String resourcesPackage = "/resources/";

    public static void setPackage(String pkg)
    {
        if(!pkg.startsWith("/"))
            pkg = "/".concat(pkg);
        if(!pkg.endsWith("/"))
            pkg = pkg.concat("/");
        
        resourcesPackage = pkg;
    }
    
    public static URL getResource(String name)
    {
        return EResources.class.getResource(resourcesPackage + name);
    }
    
    public static InputStream getStreamResource(String name)
    {
        return EResources.class.getResourceAsStream(name);
    }
    
    public static boolean resourceExists(String name)
    {
        return getResource(name) != null;
    }
    
    public static Image loadImage(String name) throws EResourceException
    {
        try
        {
            return resourceExists(name) ? ImageIO.read(getResource(name)) : null;
        }
        catch (IOException e)
        {
            throw new EResourceException("An error occured while loading image resource " + name, e);
        }
    }
    
    public static ImageIcon loadImageIcon(String name)
    {
        return resourceExists(name) ? new ImageIcon(getResource(name)) : null;
    }
    
    public static AudioClip loadSound(String name) throws EResourceException
    {
        try
        {
            return resourceExists(name) ? Applet.newAudioClip(new File(getResource(name).toURI()).toURI().toURL()) : null;
        }
        catch(URISyntaxException | MalformedURLException e)
        {
            throw new EResourceException("An error occured while loading sound resource " + name, e);
        }
    }
    
    public static Font loadFont(String name, int style, float size) throws EResourceException
    {
        try
        {
            if(resourceExists(name))
            {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(getResource(name).toURI()));
                return font.deriveFont(style, size);
            }
            else
                return null;
        }
        catch(URISyntaxException | FontFormatException | IOException e)
        {
            throw new EResourceException("An error occured while loading font resource " + name, e);
        }
    }
}
