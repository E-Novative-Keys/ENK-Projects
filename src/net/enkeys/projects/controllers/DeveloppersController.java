package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Project;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.DeveloppersView;

/**
 * Controller DevelopersControllers
 * Gestion de l'assignation des développeurs
 * @extends EController
 * @author E-Novative Keys
 */
public class DeveloppersController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final DeveloppersView view = (DeveloppersView)super.view;
    private final HashMap<String, String> project;
    private final ArrayList<ArrayList<Map<String, String>>> users;
    
    public DeveloppersController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new User());
        addModel(new Project());
        
        this.project    = project;
        this.users      = new ArrayList<>();

        this.view.getAdddev().addActionListener(addDevListener());
        this.view.getDeldev().addActionListener(delDevListener());
        this.view.getBackButton().addActionListener(backButtonListener());
        
        initView();
    }
     
    private void initView()
    {
        User user = (User)getModel("User");
        
        user.addData("data[User][project_id]", project.get("id"));
        
        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
        
        for(int i = 0; i < 2; i++)
        {
            user.addData("data[User][getdev]", (i != 0));
            if(user.validate("SELECT"))
            {
                String json = user.execute("SELECT");

                if(json != null && json.contains("users"))
                {
                    Map<String, ArrayList<Map<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<Map<String, String>>>>(){}.getType());

                    if(values != null && values.get("users") != null)
                    {
                       ArrayList<Map<String, String>> data = values.get("users");
                       users.add(data);
                       
                       for(Map<String, String> u : data)
                       {
                           if(i == 0)
                               view.getUsersData().addElement(u.get("firstname") + " " + u.get("lastname"));
                           else
                               view.getDevData().addElement(u.get("firstname") + " " + u.get("lastname"));
                       }
                    }
                }
            }
        }
    }
    
    //Ajout d'un developpeurs via le webservice
    private ActionListener addDevListener()
    {
        return (ActionEvent e) -> {
            Project project = (Project)getModel("Project");
            List selection = view.getUsersList().getSelectedValuesList();
            
            project.clearData();
            
            project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            project.addData("data[Token][fields]", app.getUser().get("token"));
            
            for(int i = 0; i < selection.size(); i++)
            {
                project.addData("data[UsersProject][project_id]", this.project.get("id"));
                
                for(Map<String, String> u : users.get(0))
                {
                    if(new String(u.get("firstname") + " " + u.get("lastname")).contains((String)selection.get(i)))
                    {
                        project.addData("data[UsersProject][user_id]", u.get("id"));
                        users.get(1).add(u);
                        users.get(0).remove(u);
                        break;
                    }
                }
                
                String json = project.execute("AFFECT");
                Map<String, String> values = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
                
                if(values != null && values.get("affect") != null)
                {
                    view.getDevData().addElement(selection.get(i));
                    view.getUsersData().remove(view.getUsersData().indexOf(selection.get(i)));
                }
            }
        };
    }
    
    //Suppression d'un développeur via le webservice
    private ActionListener delDevListener()
    {
        return (ActionEvent e) -> {
            Project project = (Project)getModel("Project");
            List selection = view.getDevList().getSelectedValuesList();
            
            project.clearData();
            
            project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            project.addData("data[Token][fields]", app.getUser().get("token"));
            
            for(int i = 0; i < selection.size(); i++)
            {
                project.addData("data[UsersProject][project_id]", this.project.get("id"));
                
                for(Map<String, String> u : users.get(1))
                {
                    if(new String(u.get("firstname") + " " + u.get("lastname")).contains((String)selection.get(i)))
                    {
                        project.addData("data[UsersProject][user_id]", u.get("id"));
                        users.get(0).add(u);
                        users.get(1).remove(u);
                        break;
                    }
                }
                
                String json = project.execute("DISAFFECT");
                
                Map<String, String> values = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
                
                if(values != null && values.get("disaffect") != null)
                {
                    if(values.get("disaffect") == "true")
                    {
                        view.getUsersData().addElement(selection.get(i));
                        view.getDevData().remove(view.getDevData().indexOf(selection.get(i)));
                    }
                    else
                        setError("La personne que vous souhaitez retirer est un référent sur ce projet. Il ne peut donc pas etre retirer.");
                }
            }
        };
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private void setError(String err) 
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
