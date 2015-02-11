package net.enkeys.projects;

import net.enkeys.projects.controllers.LoginController;
import net.enkeys.projects.views.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JMenuBar;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EFrame;
import net.enkeys.projects.controllers.HomeController;
import net.enkeys.projects.views.HomeView;

public class MainFrame extends EFrame
{
    public MainFrame(EApplication app, String title, int width, int height)
    {
        super(app, title, width, height);
        setResizable(false);
        
        //setContent(new LoginController(getApp(), new LoginView()));
        setSize(940, 580);
        setContent(new HomeController(getApp(), new HomeView()));
    }

    @Override
    protected void initMenu(JMenuBar menuBar)
    {
        
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
}
