package net.enkeys.framework.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.enkeys.framework.exceptions.ELoggerException;
import net.enkeys.framework.utils.ESystem;

/**
 * Logger personnalisé permettant la gestion console et fichier.
 * @author E-Novative Keys
 * @version 1.0
 */
public class ELogger extends Logger
{
    /**
     * Format par défaut des messages de log.
     */
    protected class ELogFormatter extends Formatter
    {
        private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

        @Override
        public String format(LogRecord record)
        {
            return "[" + date.format(new Date(record.getMillis())) + "] [" + record.getLevel().toString() + "] " + record.getMessage() + "\n";
        }
    }
    
    /**
     * Énumération définissant le comportement du Logger.
     */
    public static enum Behavior
    {
        ALL, CONSOLE, FILE
    };
    
    private final ConsoleHandler handler;        //Gestionnaire permettant la redéfinition du Formatter de log
    private String folder = "log/", name = null; //Le nom du dossier et du fichier de log en cas de définition behavior = FILE
    private File file           = null;          //Le fichier contenant les messages loggés
    private Behavior behavior   = null;          //Le comportement adopté par le Logger
    
    protected ELogger(String name, Behavior behavior)
    {
        super(name, null);
        this.handler    = new ConsoleHandler();
        this.name       = name;
        
        setUseParentHandlers(false);
        setBehavior(behavior);
        setFormatter(new ELogFormatter());
    }
    
    /**
     * Renvoie une instance d'un nouveau Logger avec le nom souhaité.
     * @param name
     * @return 
     */
    public static ELogger getLogger(String name)
    {
        return getLogger(name, Behavior.CONSOLE);
    }
    
    /**
     * Renvoie une instance d'un nouveau Logger avec le nom et le comportement
     * souhaité.
     * @param name
     * @param behavior
     * @return 
     */
    public static ELogger getLogger(String name, Behavior behavior)
    {
        LogManager manager = LogManager.getLogManager();
        manager.addLogger(new ELogger(name, behavior));
        
        return (ELogger)manager.getLogger(name);
    }
    
    /**
     * Modification du chemin d'accès au dossier contenant les fichiers de log.
     * @param path 
     */
    public final void setFolder(String path)
    {
        this.folder = path;
    }
    
    /**
     * Mise à jour du comportement du Logger.
     * @param behavior 
     */
    public final void setBehavior(Behavior behavior)
    {
        this.behavior = behavior;
    }
    
    /**
     * Mise à jour du format des messages.
     * @param formatter 
     */
    public final void setFormatter(Formatter formatter)
    {
        handler.setFormatter(formatter);
        addHandler(handler);
    }
    
    /**
     * Écriture du message de log dans le fichier du Logger.
     * @param level
     * @param message
     * @throws ELoggerException 
     */
    protected void logToFile(Level level, String message) throws ELoggerException
    {
        BufferedWriter writer;
        
        try
        {
            if(file == null || !file.exists())
            {
                if(file == null)
                    file = new File(folder + getName() + ".log");
                file.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(ESystem.getTime("[dd/MM/yyyy HH:mm:ss.SSS] [") + level.toString() + "] " + message + "\n");
            writer.close();
        }
        catch(IOException e) {
            throw new ELoggerException("Unable to write log in file " + file.getPath(), e);
        }
    }
    
    /**
     * Dépréciation de la méthode pour soucis de performance.
     * @param level
     * @param message
     * @param object
     * @throws ELoggerException
     * @deprecated
     */
    @Deprecated
    @Override
    public void log(Level level, String message, Object object) throws ELoggerException
    {
        throw new ELoggerException("Unsupported method due to performance issues.");
    }
    
    /**
     * Enregistrement du nouveau message de log.
     * @param level
     * @param message
     * @throws ELoggerException 
     */
    @Override
    public void log(Level level, String message) throws ELoggerException
    {
        switch(behavior)
        {
            case ALL:
                super.log(level, message);
                logToFile(level, message);
                break;
            case CONSOLE:
                super.log(level, message);
                break;
            case FILE:
                logToFile(level, message);
                break;
        }
    }
    
    /**
     * Message par défaut, de type Info.
     * @param message 
     */
    public final void log(String message)
    {
        log(Level.INFO, message);
    }
    
    /**
     * Message de type Config.
     * @param message 
     */
    @Override
    public final void config(String message)
    {
        log(Level.CONFIG, message);
    }
    
    /**
     * Message de type Fine.
     * @param message 
     */
    @Override
    public final void fine(String message)
    {
        log(Level.FINE, message);
    }
    
    /**
     * Message de type Finer.
     * @param message 
     */
    @Override
    public final void finer(String message)
    {
        log(Level.FINER, message);
    }
    
    /**
     * Message de type Finest.
     * @param message 
     */
    @Override
    public final void finest(String message)    
    {
        log(Level.FINEST, message);
    }
    
    /**
     * Message de type Info.
     * @param message 
     */
    @Override
    public final void info(String message)    
    {
        log(Level.INFO, message);
    }
    
    /**
     * Message de type Severe.
     * @param message 
     */
    @Override
    public final void severe(String message)    
    {
        log(Level.SEVERE, message);
    }
    
    /**
     * Message de type Warning.
     * @param message 
     */
    @Override
    public final void warning(String message)    
    {
        log(Level.WARNING, message);
    }
}
