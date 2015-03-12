package net.enkeys.projects.controllers;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.CurrentProjectsView;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListClientsView;
import net.enkeys.projects.views.ListProjectsView;
import net.enkeys.projects.views.NewClientView;
import net.enkeys.projects.views.NewProjectView;
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
        this.view.getNewProjectButton().addActionListener(newProjectListener());
        this.view.getListProjectsButton().addActionListener(listProjectsListener());
        this.view.getCurrentProjectsButton().addActionListener(currentProjectsListener());
        
        if(!this.app.getUser().get("role").equalsIgnoreCase("admin") && 
           !this.app.getUser().get("role").equalsIgnoreCase("leaddev"))
            this.view.getUsersManagerButton().setEnabled(false);
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
    
    private ActionListener newProjectListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewProjectController(app, new NewProjectView()));
        };
    }

    private ActionListener listProjectsListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ListProjectsController(app, new ListProjectsView()));
        };
    }
    
    private ActionListener currentProjectsListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectsController(app, new CurrentProjectsView()));
        };
    }
}
