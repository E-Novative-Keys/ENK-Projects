package net.enkeys.framework.components;

import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main application class.
 * @author E-Novative Keys
 * @version 0.0.0.1
 */
public abstract class EApplication implements Runnable
{
    protected final String name; //Le nom de l'application
    protected final String version; //La version actuelle de l'application
    protected final String dev; //Les noms des développeurs de l'application
    protected final String contact; //Une adresse de contact
    protected final String[] args; //La liste d'arguments passées au démarrage de l'application
    
    protected ELogger logger = null;
    protected ArrayList<EFrame> frames = null;
    
    /**
     * Crée une nouvelle instance de type EApplication.
     * Ne doit être utilisé que de manière unique et central : c'est autour de
     * cette classe que va s'architecturer toute l'application.
     * @param name
     * @param version
     * @param dev
     * @param contact
     * @param args 
     */
    public EApplication(String name, String version, String dev, String contact, String[] args)
    {
        this.name = name;
        this.version = version;
        this.dev = dev;
        this.contact = contact;
        this.args = args;
             
        setDefaultLogger();
        setEncoding("UTF-8");
        setSystemUI(true);   
    }
    
    @Override
    public abstract void run();
    
    /**
     * Définit si oui ou non le style système par défaut doit être appliqué
     * aux interfaces utilisateur de l'application.
     * @param flag 
     */
    public final void setSystemUI(boolean flag)
    {
        try
        {
            if(flag)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            else
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            logger.config("Unable to load system UI.");
        }
    }
    
    /**
     * Recherche le style UI {name} parmi les class systèmes et le charge s'il
     * existe.
     * @param name 
     */
    public final void setUI(String name)
    {
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        
        try
        {
            for(UIManager.LookAndFeelInfo info : infos)
            {
                if (info.getName().equals(name))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            logger.config("Unable to load " + name + " UI");
        }
    }
    
    /**
     * Définit l'encodage de caractères utilisé par l'application.
     * @param encoding 
     */
    public final void setEncoding(String encoding)
    {
        System.setProperty("file.encoding", encoding);
    }
    
    /**
     * Renvoie vrai si l'argument a bien été passé au programme lore de son
     * exécution. Renvoie faux sinon.
     * @param arg
     * @return 
     */
    public final boolean hasArgument(String arg)
    {
        return getArgument(arg) != null;
    }
    
    /**
     * Renvoie l'argument situé à un index donné.
     * @param index
     * @return 
     */
    public final String getArgument(int index)
    {
        return args[index];
    }
    
    /**
     * Renvoie l'argument complet à partir de son "titre".
     * Exemple d'argument :-[arg]=[data]
     * @param arg
     * @return 
     */
    public final String getArgument(String arg)
    {
        for(String current : args)
        {
            if (current.contains("=")
             && current.substring(1, current.indexOf("=") - 1).equals(arg)
             || current.equals("-" + arg))
                return current;
        }
        return null;
    }
    
    /**
     * Renvoie la partie data d'un argument, s'il en possède une.
     * Exemple d'argument :-[arg]=[data]
     * @param arg
     * @return 
     */
    public final String getArgData(String arg)
    {
        String fullArg = getArgument(arg);
        
        if(fullArg != null && fullArg.contains("="))
            return fullArg.substring(fullArg.indexOf("=")+1);
        
        return null;
    }
    
    /**
     * Définit le Logger console par défaut de l'application.
     */
    public final void setDefaultLogger()
    {
        this.logger = ELogger.getLogger(getName());
    }
    
    /**
     * Définit un Logger personnalisé pour l'application.
     * @param logger 
     */
    public final void setLogger(ELogger logger)
    {
        this.logger = logger;
    }
    
    /**
     * Renvoie le Logger de l'application.
     * @return 
     */
    public final ELogger getLogger()
    {
        return this.logger;
    }
    
    /**
     * Ajoute une nouvelle fenêtre à la liste des fenêtre de l'application.
     * @param frame
     * @return 
     */
    public final boolean addFrame(EFrame frame)
    {
        if(frames == null)
            frames = new ArrayList<>();
        return frames.add(frame);
    }
    
    /**
     * Supprime la fenêtre donnée de la liste des fenêtres de l'application.
     * @param frame
     * @return 
     */
    protected final boolean removeFrame(EFrame frame)
    {
        return frames.remove(frame);
    }
    
    /**
     * Supprime la fenêtre située à l'indice donnée de la liste des fenêtres de
     * l'application.
     * @param i
     * @return 
     */
    protected final boolean removeFrame(int i)
    {
        EFrame frame = frames.get(i);
        
        if(frame != null)
            return frames.remove(frame);
        else
            return false;
    }
    
    /**
     * Renvoie la fenêtre possédant le nom donné.
     * @param name
     * @return 
     */
    protected final EFrame getFrame(String name)
    {
        for(EFrame frame : frames)
        {
            if(frame.getName().equals(name))
                return frame;
        }
        
        return null;
    }
    
    /**
     * Renvoie la fenêtre située à l'indice donné.
     * @param i
     * @return 
     */
    protected final EFrame getFrame(int i)
    {
        return frames.get(i);
    }
    
    /**
     * Renvoie le nom de l'application.
     * @return 
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Renvoie la version de l'application.
     * @return 
     */
    public final String getVersion()
    {
        return version;
    }

    /**
     * Renvoie le nom de l'équipe de développement ou des développeurs de
     * l'application.
     * @return 
     */
    public final String getDev()
    {
        return dev;
    }

    /**
     * Renvoie l'adresse de contact défini pour l'application.
     * @return 
     */
    public final String getContact()
    {
        return contact;
    }
}