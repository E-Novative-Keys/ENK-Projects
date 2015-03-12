package net.enkeys.projects.controllers;

import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;

public class CurrentProjectManagerController extends EController
{
    private Map<String, String> project;
    
    public CurrentProjectManagerController(EApplication app, EView view, Map<String, String> project)
    {
        super(app, view);
        this.project = project;
    }
    
}
