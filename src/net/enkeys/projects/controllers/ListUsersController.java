package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListUsersView;

public class ListUsersController extends EController
{
    private ENKProjects app = (ENKProjects)super.app;
    private ListUsersView view = (ListUsersView)super.view;
    
    public ListUsersController(EApplication app, EView view)
    {
        super(app, view);
        
        this.view.getBackButton().addActionListener(backButtonListener());
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
}
