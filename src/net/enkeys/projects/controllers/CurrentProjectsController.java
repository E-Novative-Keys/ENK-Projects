package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.framework.utils.EResources;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Project;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.CurrentProjectsView;
import net.enkeys.projects.views.HomeView;

/**
 * Controller CurrentProjectsController
 * Redirige vers la gestion d'un projet selectionné
 * @extends EController
 * @author E-Novative Keys
 */
public class CurrentProjectsController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final CurrentProjectsView view = (CurrentProjectsView)super.view;
    private ArrayList<HashMap<String, String>> projectData = new ArrayList<>();
    
    public CurrentProjectsController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Project());
        
        this.view.getBackButton().addActionListener(backButtonListener());
        initView();    
    }
    
    private void initView()
    {
        Project project = (Project)getModel("Project");
        Map<String, String> errors = new HashMap<>();
        
        project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        project.addData("data[Token][fields]", app.getUser().get("token"));
        
        String json = project.execute("CURRENT");
        Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());

        if(values != null && values.get("projects") != null)
        {
            ArrayList<HashMap<String, String>> projects = values.get("projects");

            for(HashMap<String, String> p : projects)
            {
                JButton projectButton = new JButton(p.get("name"), EResources.loadImageIcon("project.png", 75, 75));
                projectButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                projectButton.setHorizontalTextPosition(SwingConstants.CENTER);
                projectButton.setIconTextGap(15);
                projectButton.addActionListener(projectsButtonListener());
                view.getListCurrentProjectsButton().add(projectButton);
                projectData.add(p);
            }
            view.buildlistProjectsPanel();
        }
        else
            System.err.println(json);
    }
    
    private ActionListener projectsButtonListener()
    {
        return (ActionEvent e) -> {
            int index = view.getListCurrentProjectsButton().indexOf(e.getSource());
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), projectData.get(index)));
        };
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
}
