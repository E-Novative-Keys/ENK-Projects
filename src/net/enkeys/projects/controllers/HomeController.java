package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.views.HomeView;

public class HomeController extends EController
{
    private final HomeView view = (HomeView)super.view;
    
    public HomeController(EApplication app, EView view)
    {
        super(app, view);
        
        this.view.getNewClientButton().addActionListener(newClientListener());
    }
    
    private ActionListener newClientListener()
    {
        return (ActionEvent e) -> {
            app.getLogger().warning("COUCOU");
        };
    }
}
