package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.CurrentProjectsView;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListClientsView;
import net.enkeys.projects.views.ListProjectsView;
import net.enkeys.projects.views.NewClientView;
import net.enkeys.projects.views.NewProjectView;
import net.enkeys.projects.views.UsersManagerView;

/**
 * Contrôlleur HomeController.
 * Gestion du panel d'acceuil.
 * et redirection vers les vues adaptées
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
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
    
    //Vue d'ajout de client
    private ActionListener newClientListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewClientController(app, new NewClientView()));
        };
    }

    //Vue de liste des clients
    private ActionListener listClientsListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ListClientsController(app, new ListClientsView()));
        };
    }

    //Vue de gestion des utilisateurs
    private ActionListener UsersManagerListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
        };
    }
    
    //Vue d'ajout de projet
    private ActionListener newProjectListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewProjectController(app, new NewProjectView()));
        };
    }

    //Vue de liste des projets
    private ActionListener listProjectsListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ListProjectsController(app, new ListProjectsView()));
        };
    }
    
    //Vue de selection d'un projet à gérer
    private ActionListener currentProjectsListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectsController(app, new CurrentProjectsView()));
        };
    }
}
