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
    public MainFrame(EApplication app, String title, int width, int height)
    {
        super(app, title, width, height);
        setMinimumSize(width, height);
        
        setContent(new LoginController(getApp(), new LoginView()));
    }

    @Override
    protected void initMenu(JMenuBar menuBar)
    {
        JMenu file = new JMenu("File");
        
        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.addActionListener(fileExitListener());
        file.add(fileExit);
        
        menuBar.add(file);
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
