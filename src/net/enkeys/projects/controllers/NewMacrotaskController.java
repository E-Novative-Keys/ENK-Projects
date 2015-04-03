/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Client;
import net.enkeys.projects.models.Macrotask;
import net.enkeys.projects.models.MacrotaskUser;
import net.enkeys.projects.models.Project;
import net.enkeys.projects.models.Task;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.NewMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 * TODO LIST
 * Ajouter Heures/Minutes en dessous de deadline
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
        addModel(new Task());
        addModel(new Macrotask());
        addModel(new MacrotaskUser());
      
        this.project = project;
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
        
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

    private ActionListener backButtonListener() 
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }
    
    private ActionListener saveButtonListener() 
    {
        return (ActionEvent e) -> {       
            Macrotask macrotask         = (Macrotask)getModel("Macrotask");
            DateFormat df               = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> errors  = new HashMap<>();
            
            if(view.getDeadline() != null && view.getDeadline().getDate().getTime() <= new Date().getTime())
            {
                setError("Veuillez saisir une deadline ne précédant pas la date actuelle");
                return;
            }
          
            macrotask.addData("data[Macrotask][project_id]", this.project.get("id"));
            macrotask.addData("data[Macrotask][name]", view.getMacrotaskName().getText());
            macrotask.addData("data[Macrotask][deadline]", df.format(view.getDeadline().getDate()));
            macrotask.addData("data[Macrotask][priority]", view.getPriority().getValue());
            macrotask.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            macrotask.addData("data[Token][fields]", app.getUser().get("token"));
           
            try
            {
                if(macrotask.validate("INSERT", macrotask.getData(), errors))
                {
                    String json = macrotask.execute("INSERT");
                    //System.out.println("Json : "+json);

                    if(json.contains("macrotask"))
                    {
                        //La macrotache est bien crée, il faut récupérer son id et 
                        // hydrater le reste en fonction de l'id trouvée
                        Map<String, ArrayList<Map<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<Map<String, String>>>>(){}.getType());
                        Task task                       = (Task)getModel("Task");
                        MacrotaskUser macrotaskUser     = (MacrotaskUser)getModel("MacrotaskUser");
                        String macrotask_id             = values.get("macrotask_id").get(0).get("id");
                        
                        //Ajouter les tasks ajoutées à la macrotache
                        int numTasks = view.getSelectedTaskData().getSize();
                        
                        //Ajouter les développeurs ajoutés a la macrotache
                        int numDevelopers = view.getSelectedDevData().getSize();
                    /*
                        for(int i = 0; i < numTasks; i++)
                        {
                            //Ajouter les taches ajoutées a la macrotache
                            String tasksSelection = view.getSelectedTaskData().get(i).toString();
                            String taskName = tasksSelection.split(" // ")[0];
                            String taskPriority = tasksSelection.split(" // ")[1].substring(1, tasksSelection.split(" // ")[1].length() - 1);
                     
                            task.clearData();
                            task.addData("data[Task][macrotask_id]", macrotask_id);
                            task.addData("data[Task][name]", taskName);
                            task.addData("data[Task][priority]", taskPriority);
                            task.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                            task.addData("data[Token][fields]", app.getUser().get("token"));
                        
                            if(task.validate("INSERT", task.getData(), errors))
                            {
                                String jsonTask = task.execute("INSERT");
                                
                                if(json.contains("task"))
                                {
                                    System.out.println("Datas task bien rentrées en bdd");
                                }
                                else if(json.contains("error"))
                                {
                                    Map<String, Map<String, String>> newValues = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                                    if((errors = newValues.get("error")) != null)
                                        setError(errors.get(errors.keySet().toArray()[0].toString()));
                                    else
                                        setError("Une erreur inattendue est survenue");
                                }
                                else
                                    setError(json);
                            }
                            else 
                                setError(errors.get(errors.keySet().toArray()[0].toString()));
                        }
                    */
                        for(int i = 0; i < numDevelopers; i++)
                        {
                            //Ajouter les développeurs affectés à une macrotâche
                            String developersSelection = view.getSelectedDevData().get(i).toString();
                            System.out.println(developersSelection);
                            macrotaskUser.clearData();
                            macrotaskUser.addData("data[MacrotaskUser][macrotask_id]", macrotask_id);
                            macrotaskUser.addData("data[MacrotaskUser][user_name]", developersSelection);
                            macrotaskUser.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                            macrotaskUser.addData("data[Token][fields]", app.getUser().get("token"));
                            
                            if(macrotaskUser.validate("INSERT", macrotaskUser.getData(), errors))
                            {
                                String jsonUser = macrotaskUser.execute("INSERT");
                                System.out.println("jsonUser: "+jsonUser);
                                if(json.contains("macrotaskUser"))
                                {
                                    System.out.println("Datas macrotaskUser bien rentrées en bdd");
                                }
                                else if(json.contains("error"))
                                {
                                    Map<String, Map<String, String>> newDevValues = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                                    if((errors = newDevValues.get("error")) != null)
                                        setError(errors.get(errors.keySet().toArray()[0].toString()));
                                    else
                                        setError("Une erreur inattendue est survenue");
                                }
                                else
                                    setError(json);
                            }
                            else
                                setError(errors.get(errors.keySet().toArray()[0].toString()));
                        }
                    } //End if(json.contains("macrotask"))
                    else if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                        if((errors = values.get("error")) != null)
                            setError(errors.get(errors.keySet().toArray()[0].toString()));
                        else
                            setError("Une erreur inattendue est survenue");
                    }
                    else
                        setError(json);
                }  
                else 
                    setError(errors.get(errors.keySet().toArray()[0].toString()));
            }
            catch(ERuleException | EHttpRequestException ex)
            {
                setError(ex.getMessage());
            }
        };
    }

    private ActionListener addDeveloperListener() 
    {
        return (ActionEvent e) -> {
            if(!this.view.getSelectedDevData().contains(this.view.getDevelopers().getSelectedItem()))
                this.view.getSelectedDevData().addElement(this.view.getDevelopers().getSelectedItem());
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

    private ActionListener addTaskListener() 
    {
        return (ActionEvent e) -> {
            if(!this.view.getSelectedTaskData().contains(this.view.getMicrotaskName().getText()))
                this.view.getSelectedTaskData().addElement(this.view.getMicrotaskName().getText()+" // ("+this.view.getPriorityTask().getValue()+")");
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
    
    private void setError(String err)
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
