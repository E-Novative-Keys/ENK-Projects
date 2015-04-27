package net.enkeys.projects.controllers;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.CloudView;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.DeveloppersView;
import net.enkeys.projects.views.NewProjectView;
import net.enkeys.projects.views.QuotationView;
import net.enkeys.projects.views.ReceivedMailsView;
import net.enkeys.projects.views.ScheduleView;

/**
 * Contrôlleur CurrentProjectManagerController.
 * Gestion des actions du menu de gestion d'un projet.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class CurrentProjectManagerController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final CurrentProjectManagerView view = (CurrentProjectManagerView)super.view;
    private final HashMap<String, String> project;
    
    public CurrentProjectManagerController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        this.project = project;
        
        this.view.getEditProjectButton().addActionListener(editProjectListener());
        this.view.getDeveloppersButton().addActionListener(developpersListener());
        this.view.getScheduleButton().addActionListener(scheduleListener());
        this.view.getMessengerButton().addActionListener(mailListener());
        this.view.getCloudButton().addActionListener(cloudListener());
        this.view.getQuotationButton().addActionListener(quotationListener());
    }
    
    //Aller sur la vue d'édition de projet
    private ActionListener editProjectListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new EditProjectController(app, new NewProjectView(), this.project, true));
        };
    }
    
    //Aller sur la vue d'assignation des développeurs
    private ActionListener developpersListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new DeveloppersController(app, new DeveloppersView(), this.project));
        };
    } 

    //Aller sur la vue de gestion des macrotâches associées au projet
    private ActionListener scheduleListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }
    
    //Aller sur la vue mail
    private ActionListener mailListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ReceivedMailsController(app, new ReceivedMailsView(), this.project));
        };
    }
    
    //Aller sur la vue cloud
    private ActionListener cloudListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CloudController(app, new CloudView(), this.project));
        };
    }
    
    //Aller sur la création de devis
    private ActionListener quotationListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new QuotationController(app, new QuotationView(), this.project));
        };
    }
}
