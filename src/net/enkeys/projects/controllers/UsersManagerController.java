package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.UsersManagerView;
import net.enkeys.projects.views.ListUsersView;
import net.enkeys.projects.views.NewUserView;

/**
 * Controller UsersManagerController
 * Evenements de la gestion utilisateur
 * @extends EController
 * @author E-Novative Keys
 */
public class UsersManagerController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final UsersManagerView view = (UsersManagerView)super.view;
    
    public UsersManagerController(EApplication app, EView view)
    {
        super(app, view);
        
        this.view.getNewUserButton().addActionListener(newUserListener());
        this.view.getListUsersButton().addActionListener(listUsersListener());
        this.view.getBackButton().addActionListener(backButtonListener());
    }
    
    //Aller à la vue d'ajout d'utilisateur
    private ActionListener newUserListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewUserController(app, new NewUserView()));
        };
    }

    //Aller à la vue de liste d'utilisateurs
    private ActionListener listUsersListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ListUsersController(app, new ListUsersView()));
        };
    }
    
    //Revenir à la vue du menu principal
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
}
