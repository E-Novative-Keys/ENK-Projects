package net.enkeys.projects.controllers;

import java.util.HashMap;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.NewMailView;

public class NewMailController extends EController
{
    private final ENKProjects app     = (ENKProjects)super.app;
    private final NewMailView view    = (NewMailView)super.view;
    private final HashMap<String, String> project;
    
    public NewMailController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        
        this.project = project;
    }
    
}
