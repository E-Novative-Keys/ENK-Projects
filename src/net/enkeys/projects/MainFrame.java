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

public class MainFrame extends EFrame
{
    private final ENKProjects app = (ENKProjects)super.app;
    private JMenuItem disconnect;
    
    public MainFrame(EApplication app, String title, int width, int height)
    {
        super(app, title, width, height);
        setResizable(false);
    }

    @Override
    protected void initMenu(JMenuBar menuBar)
    {
        JMenu file = new JMenu("Fichier");
        disconnect = new JMenuItem("Déconnexion");
        JMenuItem exit = new JMenuItem("Quitter");
        
        disconnect.addActionListener(fileDisconnectListener());
        disconnect.setVisible(false);
        exit.addActionListener(fileExitListener());
        
        file.add(disconnect);
        file.add(exit);
        
        menuBar.add(file);
    }
    
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
    
    private ActionListener fileExitListener()
    {
        return (ActionEvent e) -> {
            close();
        };
    }

    @Override
    public void onWindowClosing(WindowEvent we)
    {
        close();
    }

    @Override
    public void onWindowClosed(WindowEvent we)
    {
        ((ENKProjects)app).resetUser();
    }
    
    public JMenuItem getDisconnectItem()
    {
        return disconnect;
    }
}
