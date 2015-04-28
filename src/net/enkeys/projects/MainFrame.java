package net.enkeys.projects;

import net.enkeys.projects.controllers.LoginController;
import net.enkeys.projects.views.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EFrame;
import net.enkeys.projects.controllers.HomeController;
import net.enkeys.projects.controllers.ProfileController;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ProfileView;

/**
 * Fenêtre de l'application.
 * @author E-Novative Keys
 * @version 1.0
 */
public class MainFrame extends EFrame
{
    private final ENKProjects app = (ENKProjects)super.app;
    private JMenuItem disconnect;
    private JMenuItem home;
    private JMenuItem profile;
    
    /**
     * Crée une nouvelle istance de type MainFrame.
     * @param app
     * @param title
     * @param width
     * @param height 
     */
    public MainFrame(EApplication app, String title, int width, int height)
    {
        super(app, title, width, height);
        setResizable(false);
    }

    /**
     * Initialisation des objets composant le menu de la fenêtre.
     * @param menuBar 
     */
    @Override
    protected void initMenu(JMenuBar menuBar)
    {
        JMenu file      = new JMenu("Fichier");
        disconnect      = new JMenuItem("Déconnexion");
        home            = new JMenuItem("Accueil");
        profile         = new JMenuItem("Profil");
        JMenuItem exit  = new JMenuItem("Quitter");
        
        home.addActionListener(fileHomeListener());
        home.setVisible(false);
        disconnect.addActionListener(fileDisconnectListener());
        disconnect.setVisible(false);
        profile.addActionListener(profileListener());
        profile.setVisible(false);
        exit.addActionListener(fileExitListener());
        
        file.add(home);
        file.add(profile);
        file.add(disconnect);
        file.add(exit);
        
        menuBar.add(file);
    }
    
    /**
     * Menu de déconnexion.
     * @return 
     */
    private ActionListener fileDisconnectListener()
    {
        return (ActionEvent e) -> {
            if(app.getUser() != null)
            {
                app.resetUser();
                setSize(440, 380);
                setLocationRelativeTo(null);
                setContent(new LoginController(app, new LoginView()));
            }
        };
    }
    
    /**
     * Menu de fermeture.
     * @return 
     */
    private ActionListener fileExitListener()
    {
        return (ActionEvent e) -> {
            close();
        };
    }
    
    /**
     * Menu accueil.
     * @return 
     */
    private ActionListener fileHomeListener()
    {
        return (ActionEvent e) -> {
            if(app.getFrame(0).getJMenuBar().getMenuCount() == 2)
                app.getFrame(0).getJMenuBar().remove(1);
            if(app.getUser() != null)
                setContent(new HomeController(app, new HomeView()));
        };
    }
    
    /**
     * Menu profil.
     * @return 
     */
    private ActionListener profileListener()
    {
        return (ActionEvent e) -> {
            setContent(new ProfileController(app, new ProfileView()));
        };
    }

    /**
     * Événement de fermeture de la fenêtre.
     * @param we 
     */
    @Override
    public void onWindowClosing(WindowEvent we)
    {
        //Si on essaie de fermer la fenêtre (crois, alt+F4...), on appel close()
        close();
    }

    /**
     * Quand la fenêtre est fermée, on déconnecte l'utilisateur.
     * @param we 
     */
    @Override
    public void onWindowClosed(WindowEvent we)
    {
        ((ENKProjects)app).resetUser();
    }
    
    /**
     * Renvoie l'objet de menu Déconnexion.
     * @return 
     */
    public JMenuItem getDisconnectItem()
    {
        return disconnect;
    }
    
    /**
     * Renvoie l'objet de menu Accueil.
     * @return 
     */
    public JMenuItem getHomeItem()
    {
        return home;
    }

    /**
     * Renvoie l'objet de menu Profil.
     * @return 
     */
    public JMenuItem getProfileItem()
    {
        return profile;
    }
}
