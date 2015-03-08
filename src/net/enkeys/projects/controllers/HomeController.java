package net.enkeys.projects.controllers;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListClientsView;
import net.enkeys.projects.views.NewClientView;
import net.enkeys.projects.views.UsersManagerView;

public class HomeController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final HomeView view = (HomeView)super.view;
    
    public HomeController(EApplication app, EView view)
    {
        super(app, view);
        
        this.view.getNewClientButton().addActionListener(newClientListener());
        this.view.getListClientsButton().addActionListener(listClientsListener());
        this.view.getUsersManagerButton().addActionListener(UsersManagerListener());
    }
    
    private ActionListener newClientListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewClientController(app, new NewClientView()));
        };
    }

    private ActionListener listClientsListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ListClientsController(app, new ListClientsView()));
        };
    }

    private ActionListener UsersManagerListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
        };
    }
}
