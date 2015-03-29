/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import net.enkeys.projects.models.Client;
import net.enkeys.projects.models.Task;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.NewMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 * TODO LIST
 * Changer add&suppr developer en se calquant sur DevelopersController (dynamisme)
 * Changer add&suppr microtask --/--
 * Régler le problème de la priority sur la Macrotâche ET sur le Tâche
 *      --> Peut-être une nouvelle vue pour la tâche à part entière ?
 * Créer les routes dans WebService
 *      --> Creer macrotache dans /projects/?
 *      --> Ajout/suppr dev dans /projects?? /macrotask serrai mieux ?
 *      --> Ajout/suppr tache dans /tasks/new && tasks/delete
 * Créer les doigts dans le Model correspondant (Macrotask ?)
 */
public class NewMacrotaskController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final NewMacrotaskView view = (NewMacrotaskView)super.view;
    private final HashMap<String, String> project;

    public NewMacrotaskController(EApplication app, EView view, HashMap<String, String> project) 
    {
        super(app, view);
        addModel(new User());
      
        this.project = project;
        
        this.view.getBack().addActionListener(backButtonListener());
        
        this.view.getAddDeveloperButton().addActionListener(addDeveloperListener());
        this.view.getSupprDeveloperButton().addActionListener(supprDeveloperListener());
        
        this.view.getAddMicrotaskButton().addActionListener(addTaskListener());
        this.view.getSupprMicrotaskButton().addActionListener(supprTaskListener());
        
        initView();
    }
    
    private void initView()
    {
        Map<String, String> errors = new HashMap<>();
        User user = (User)getModel("User");
        
        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
        
        //Remplissage ComboBox référent
        for(int i = 0; i < 3; i++) 
        {
            if(i == 0)
                user.addData("data[User][role]", "leaddev");
            else if(i == 1)
                user.addData("data[User][role]", "admin");
            else 
                user.addData("data[User][role]", "developer");
            
            if(user.validate("SELECT", user.getData(), errors))
            {
                String json = user.execute("SELECT", errors);
                Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());

                if(values != null && values.get("users") != null)
                {
                    ArrayList<HashMap<String, String>> users = values.get("users");

                    for(HashMap<String, String> u : users)
                    {
                        view.getDevelopers().addItem(u.get("firstname") + " " + u.get("lastname"));
                    }
                }
                else
                    System.err.println(json);
            }
            else
                System.err.println(errors);
        }
    }

    private ActionListener backButtonListener() {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }   

    private ActionListener addDeveloperListener() {
        return (ActionEvent e) -> {
            if(!this.view.getSelectedDevData().contains(this.view.getDevelopers().getSelectedItem()))
                this.view.getSelectedDevData().addElement(this.view.getDevelopers().getSelectedItem());
        };
    }

    private ActionListener addTaskListener() 
    {
        return (ActionEvent e) -> {
            this.view.getSelectedTaskData().addElement(this.view.getMicrotaskName().getText());
        };
    }

    private ActionListener supprDeveloperListener() 
    {
        return (ActionEvent e) -> {
            List selection = view.getSelectedDevList().getSelectedValuesList();
            
            for(int i = 0; i < selection.size(); i++)
            {
                view.getSelectedDevData().remove(view.getSelectedDevData().indexOf(selection.get(i)));
            }
        };
    }

    private ActionListener supprTaskListener() 
    {
        return (ActionEvent e) -> {
            List selection = view.getSelectedTaskList().getSelectedValuesList();
            
            for(int i = 0; i < selection.size(); i++)
            {
                view.getSelectedTaskData().remove(view.getSelectedTaskData().indexOf(selection.get(i)));
            }
        };    
    }
}
