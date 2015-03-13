package net.enkeys.projects.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.DeveloppersView;

public class DeveloppersController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final DeveloppersView view = (DeveloppersView)super.view;
    private final HashMap<String, String> project;
    
    public DeveloppersController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new User());
        
        this.project = project;
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
}
