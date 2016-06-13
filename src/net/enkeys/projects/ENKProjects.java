package net.enkeys.projects;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.framework.utils.EResources;
import net.enkeys.projects.controllers.LoginController;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.LoginView;

/**
 * Application ENK-Projects.
 * @author E-Novative Keys
 * @version 1.0
 */
public class ENKProjects extends EApplication
{
    //SALT et PEPPER utilisés pour transmettre des données au WebService
    public static final String SALT     = "$$-;-GQ^ TdD/-)7;_Kls+Q/Z<w+RI^],f6/bL 8=>ou!Hx~N/T-I| ~n@lOp6+t";
    public static final String PEPPER   = "kwl:mxn+>Du2g}mXH$Yq|V{G-uo5yAY-:!%3G.38vR-Z<Rq@K/H-73SV>T RWFQK";
    
    //Données de l'utilisateur
    private Map<String, String> user    = null;
    
    /**
     * Crée une nouvelle instance de type ENKProjects.
     * @param name
     * @param version
     * @param dev
     * @param contact
     * @param args 
     */
    public ENKProjects(String name, String version, String dev, String contact, String[] args)
    {
        super(name, version, dev, contact, args);
        
        EResources.setPackage("net.enkeys.projects.resources"); //On définit le package dans lequel se trouvent les ressources de l'application
        addFrame(new MainFrame(this, getName(), 440, 380));     //On crée une nouvelle fenêtre
    }
    
    /**
     * Définition des données de l'utilisateur.
     * Activation des menus Accueil, Profil & Déconnexion.
     * @param user 
     */
    public void setUser(Map<String, String> user)
    {
        resetUser();
        
        this.user = user;
        
        ((MainFrame)getFrame(0)).getDisconnectItem().setVisible(true);
        ((MainFrame)getFrame(0)).getHomeItem().setVisible(true);
        ((MainFrame)getFrame(0)).getProfileItem().setVisible(true);
    }
    
    /**
     * Déconnexion de l'utilisateur.
     * Envoie d'une requête au WebService effaçant son token en base de données.
     */
    public void resetUser()
    {
        if(this.user != null)
        {
            User user = new User();

            user.addData("data[Token][link]", ECrypto.base64(getUser().get("email")));
            user.addData("data[Token][fields]", getUser().get("token"));

            try
            {
                String json = user.execute("LOGOUT");

                if(json != null && json.contains("error"))
                {
                    Map<String, String> value = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
                    message(value.get("error"), JOptionPane.WARNING_MESSAGE);
                }
            }
            catch(ERuleException | EHttpRequestException ex)
            {
                getLogger().warning(ex.getMessage());
                message("Une erreur survenue lors de la connexion au serveur.", JOptionPane.ERROR_MESSAGE);
            }
            finally
            {
                this.user = null;
                
                ((MainFrame)getFrame(0)).getDisconnectItem().setVisible(false);
                ((MainFrame)getFrame(0)).getHomeItem().setVisible(false);
                ((MainFrame)getFrame(0)).getProfileItem().setVisible(false);
            }
        }
    }
    
    /**
     * Renvoie les données de l'utilisateur authentifié.
     * @return 
     */
    public Map<String, String> getUser()
    {
        return user;
    }

    @Override
    public void run()
    {
        logger.log(getName() + " v" + getVersion());
        User user       = new User();
        MainFrame frame = (MainFrame)getFrame(0);
        
        //On définit le contenu de la fenêtre et on l'affiche
        frame.setContent(new LoginController(this, new LoginView()));
        frame.setVisible(true);
        
        //Si le WebService est injoignable, on clos l'application
        try
        {
            user.addData("data[Token][link]", "test");
            String json = user.execute("VERIFY");
            
            if(json != null && json.contains("error"))
            {
                Map<String, String> value = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
                message(value.get("error"));
            }
        }
        catch(ERuleException | EHttpRequestException ex)
        {
            getLogger().warning(ex.getMessage());
            message("Une erreur survenue lors de la connexion au serveur.", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
    
    /**
     * Main.
     * @param args 
     */
    public static void main(String[] args)
    {
        new ENKProjects("ENK-Projects", "1.0", "E-Novative Keys", "contact@enkeys.com", args).run();
    }
}
