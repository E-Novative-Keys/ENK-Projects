package net.enkeys.projects.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Project;
import net.enkeys.projects.views.CurrentProjectsView;

public class CurrentProjectsController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final CurrentProjectsView view = (CurrentProjectsView)super.view;
    
    public CurrentProjectsController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Project());
        
        initView();
    }
    
    private void initView()
    {
        Project project = (Project)getModel("Project");
        Map<String, String> errors = new HashMap<>();
        
        project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        project.addData("data[Token][fields]", app.getUser().get("token"));
        
        String json = project.execute("CURRENT");
        System.out.println(json);
        Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
        
        if(values != null && values.get("projects") != null)
        {
            ArrayList<HashMap<String, String>> projects = values.get("projects");

            for(HashMap<String, String> p : projects)
                view.getListCurrentProjectsButton().add(new JButton(p.get("name")));
        }
        else
            System.err.println(json);
    }
    
}
