package net.enkeys;

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
        
        setContent(new RequesterController(getApp(), new RequesterView()));
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

    @Override
    public void onWindowClosed(WindowEvent we)
    {
        app.getLogger().info("Good bye!");
    }
}
