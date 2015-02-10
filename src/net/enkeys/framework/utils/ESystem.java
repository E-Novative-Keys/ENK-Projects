package net.enkeys.framework.utils;

import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.enkeys.framework.exceptions.ESystemException;

public enum ESystem
{
    LINUX("linux", new String[] {"linux", "unix"}), 
    WINDOWS("windows", new String[] {"win", "windows"}), 
    OSX("osx", new String[] {"mac", "osx"}), 
    UNKNOWN("unknown", new String[0]);

    private final String name;
    private final String[] aliases;
    
    private ESystem(String name, String[] aliases)
    {
        this.name = name;
	this.aliases = (aliases == null ? new String[0] : aliases);
    }
    
    /**
     * Renvoie une instance énumérée du système courant.
     * @return 
     */
    public static ESystem getCurrentPlatform()
    {
        String osName = System.getProperty("os.name").toLowerCase();

        for(ESystem os : values())
        {
            for(String alias : os.getAliases())
            {
                if (osName.contains(alias))
                   return os;
            }
        }

        return UNKNOWN;
    }
    
    /**
     * Renvoie le chemin d'accès vers le dossier d'installation Java.
     * @return 
     */
    public String getJavaDir()
    {
        String separator = System.getProperty("file.separator");
        String path = System.getProperty("java.home") + separator + "bin" + separator;

        if((getCurrentPlatform() == WINDOWS) && (new File(path + "javaw.exe").isFile()))
            return path + "javaw.exe";
        else
            return path + "java";
    }

    /**
     * Ouvre un lien local au système.
     * @param link
     * @throws ESystemException 
     */
    public static void open(URI link) throws ESystemException
    {
        try
        {
            Class<?> desktopClass = Class.forName("java.awt.Desktop");
            Object o = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            desktopClass.getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { link });
        }
        catch(ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new ESystemException("An error occured while opening link: " + link.toString(), e);
        }
    }
    
    /**
     * Lit sur l'entrée standard jusqu'à la saisie d'un \n.
     * @param stream
     * @return
     * @throws IOException 
     */
    public static String read(InputStream stream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.readLine();
    }
    
    /**
     * Renvoie le nom du système courant.
     * @return 
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Renvoie les alias du système courant.
     * @return 
     */
    public String[] getAliases()
    {
        return this.aliases;
    }
            
    /**
     * Renvoie le presse-papier du système.
     * @return 
     */
    public static Clipboard getClipboard()
    {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
    
    /**
     * Renvoie la largeur de l'écran pixels.
     * @return 
     */
    public static int getScreenWidth()
    {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }
    
    /**
     * Renvoie la hauteur de l'écran en pixels.
     * @return 
     */
    public static int getScreenHeight()
    {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
    
    /**
     * Renvoie la résolution de l'écran en pixels.
     * @return 
     */
    public static int getScreenResolution()
    {
        return Toolkit.getDefaultToolkit().getScreenResolution();
    }
    
    /**
     * Renvoie vrai si l'environnement graphique courant supporte la transparence.
     * Renvoie faux sinon.
     * @return 
     */
    public static boolean supportTranslucency()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
    }
    
    /**
     * Renvoie le curseur prédéfini passé en paramètre.
     * @param cursor
     * @return 
     */
    public static Cursor getCursor(int cursor)
    {
        return Cursor.getPredefinedCursor(cursor);
    }
    
    /**
     * Crée un nouveau curseur à partir de l'image donnée.
     * Les coordonnées x et y corresponde au pixel symbolisant la pointe de la
     * souris sur l'image.
     * @param image
     * @param x
     * @param y
     * @return 
     */
    public static Cursor getCustomCursor(Image image, int x, int y)
    {
        return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(x, y), image.toString());
    }
    
    /**
     * Renvoie la date selon le format passé en paramètre.
     * @param format
     * @return 
     */
    public static String getTime(String format)
    {
        return new SimpleDateFormat(format).format(new Date());
    }
}
