package net.enkeys.framework.components;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe abstraite mère de toutes les applications ENK.
 * Permet la gestion aisé de fenêtres, des arguments, d'un logger d'application,
 * ainsi que de divers outils standards.
 * @author E-Novative Keys
 * @version 1.0
 */
public abstract class EApplication implements Runnable
{
    //Redéfinition des constantes de fenêtres confirm & message
    public static final int YES = JOptionPane.YES_OPTION;
    public static final int NO = JOptionPane.NO_OPTION;
    public static final int CANCEL = JOptionPane.CANCEL_OPTION;
    public static final int YES_NO = JOptionPane.YES_NO_OPTION;
    public static final int YES_NO_CANCEL = JOptionPane.YES_NO_CANCEL_OPTION;
    
    //Attributs principaux de l'application
    protected final String name;    //Le nom de l'application
    protected final String version; //La version actuelle de l'application
    protected final String dev;     //Les noms des développeurs de l'application
    protected final String contact; //Une adresse de contact
    protected final String[] args;  //La liste d'arguments passées au démarrage de l'application
    
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
    
    /**
     * Méthode abstraite symbolisant le démarrage de l'application.
     * Les initialisations se font dans le constructeurs, les appels se font
     * dans le run().
     */
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
    public final boolean removeFrame(EFrame frame)
    {
        return frames.remove(frame);
    }
    
    /**
     * Supprime la fenêtre située à l'indice donnée de la liste des fenêtres de
     * l'application.
     * @param i
     * @return 
     */
    public final boolean removeFrame(int i)
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
    public final EFrame getFrame(String name)
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
    public final EFrame getFrame(int i)
    {
        return frames.get(i);
    }
    
    public final void message(EFrame frame, String msg)
    {
        this.message(frame, msg, frame.getName(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    public final void message(String msg)
    {
        this.message(null, msg, getName(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    public final void message(String msg, int type)
    {
        this.message(null, msg, getName(), type);
    }
    
    public final void message(String msg, String title)
    {
        this.message(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public final void message(String msg, String title, int type)
    {
        this.message(null, msg, title, type);
    }
    
    public final void message(EFrame frame, String msg, String title)
    {
        this.message(frame, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Affichage d'une fenêtre de dialogue contenant un message simple.
     * @param frame
     * @param msg
     * @param title
     * @param type 
     */
    public final void message(EFrame frame, String msg, String title, int type)
    {
        JOptionPane.showMessageDialog(frame, msg, title, type);
    }
    
    public final int confirm(EFrame frame, String msg)
    {
        return this.confirm(frame, msg, frame.getName(), JOptionPane.YES_NO_OPTION);
    }
    
    public final int confirm(String msg)
    {
        return this.confirm(null, msg, getName(), JOptionPane.YES_NO_OPTION);
    }
    
    public final int confirm(String msg, int options)
    {
        return this.confirm(null, msg, getName(), options);
    }
    
    public final int confirm(String msg, String title)
    {
        return this.confirm(null, msg, title, JOptionPane.YES_NO_OPTION);
    }
    
    public final int confirm(String msg, String title, int options)
    {
        return this.confirm(null, msg, title, options);
    }
    
    public final int confirm(EFrame frame, String msg, String title)
    {
        return this.confirm(frame, msg, title, JOptionPane.YES_NO_OPTION);
    }
    
    /**
     * Affichage d'une fenêtre de dialogue permettant à l'utilisateur d'effectuer
     * un choix.
     * @param frame
     * @param msg
     * @param title
     * @param options 
     * @return  
     */
    public final int confirm(EFrame frame, String msg, String title, int options)
    {
        return JOptionPane.showConfirmDialog(frame, msg, title, options);
    }
    
    public final String input(EFrame frame, String msg)
    {
        return this.input(frame, msg, frame.getName(), JOptionPane.PLAIN_MESSAGE);
    }
    
    public final String input(String msg)
    {
        return this.input(null, msg, getName(), JOptionPane.PLAIN_MESSAGE);
    }
    
    public final String input(String msg, String title)
    {
        return this.input(null, msg, title, JOptionPane.PLAIN_MESSAGE);
    }
    
    public final String input(String msg, String title, int options)
    {
        return this.input(null, msg, title, options);
    }
    
    public final String input(EFrame frame, String msg, String title)
    {
        return this.input(frame, msg, title, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Affichage d'une fenêtre de dialogue contenant un champs de texte.
     * @param frame
     * @param msg
     * @param title
     * @param type
     * @return 
     */
    public final String input(EFrame frame, String msg, String title, int type)
    {
        return JOptionPane.showInputDialog(frame, msg, title, type);
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
