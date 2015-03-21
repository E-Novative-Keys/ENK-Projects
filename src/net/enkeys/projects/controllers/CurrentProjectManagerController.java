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
        this.view.getCloudButton().addActionListener(cloudListener());
    }
    
    private ActionListener editProjectListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new EditProjectController(app, new NewProjectView(), this.project, true));
        };
    }
    
    private ActionListener developpersListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new DeveloppersController(app, new DeveloppersView(), this.project));
        };
    }
    
    private ActionListener cloudListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CloudController(app, new CloudView(), this.project));
        };
    }
}
