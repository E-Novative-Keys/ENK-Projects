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
import net.enkeys.projects.models.Macrotask;
import net.enkeys.projects.models.MacrotasksUser;
import net.enkeys.projects.models.Task;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.NewMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 * Contrôlleur NewMacrotaskController.
 * Gestion de la création d'une macrotâche.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class NewMacrotaskController extends EController
{
    private final ENKProjects app       = (ENKProjects)super.app;
    private final NewMacrotaskView view = (NewMacrotaskView)super.view;
    private final HashMap<String, String> project;

    public NewMacrotaskController(EApplication app, EView view, HashMap<String, String> project) 
    {
        super(app, view);
        addModel(new User());
        addModel(new Task());
        addModel(new Macrotask());
        addModel(new MacrotasksUser());
      
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
        User user                   = (User)getModel("User");
        Map<String, String> errors  = new HashMap<>();
        
        user.addData("data[Token][link]",       ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]",     app.getUser().get("token"));
        user.addData("data[User][project_id]",  project.get("id"));
        user.addData("data[User][getdev]",      true);
        
        //Remplissage ComboBox référent
        if(user.validate("SELECT", user.getData(), errors))
        {
            String json = user.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(
                json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
            );

            if(values != null && values.get("users") != null)
            {
                ArrayList<HashMap<String, String>> users = values.get("users");

                for(HashMap<String, String> u : users)
                {
                    view.getDevelopers().addItem(u.get("firstname") + " " + u.get("lastname"));
                }
            }
            else
                setError("Une erreur inattendue est survenue");
        }
        else
            setError("Une erreur inattendue est survenue");
    }

    private ActionListener backButtonListener() 
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }
    
    //Sauvegarde d'une macrotâche entrée en vue via le webservice
    private ActionListener saveButtonListener() 
    {
        return (ActionEvent e) -> {       
            Macrotask macrotask         = (Macrotask)getModel("Macrotask");
            DateFormat df               = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> errors  = new HashMap<>();
            
            if(view.getDeadline().getDate() == null)
            {
                setError("Veuillez préciser une deadline");
                return;
            }
            if(view.getDeadline().getDate().getTime() <= new Date().getTime())
            {
                setError("Veuillez saisir une deadline ne précédant pas la date actuelle");
                return;
            }
            
            String hours = new String(Float.parseFloat(view.getHours().getValue().toString()) + "");
            String minutes = new String(Float.parseFloat(view.getMinutes().getValue().toString()) + "");    
            
            macrotask.addData("data[Macrotask][project_id]",    this.project.get("id"));
            macrotask.addData("data[Macrotask][name]",          view.getMacrotaskName().getText());
            macrotask.addData("data[Macrotask][deadline]",      df.format(view.getDeadline().getDate()));
            macrotask.addData("data[Macrotask][priority]",      view.getPriority().getValue());
            macrotask.addData("data[Macrotask][hour]",          hours);
            macrotask.addData("data[Macrotask][minute]",        minutes);
            macrotask.addData("data[Token][link]",              ECrypto.base64(app.getUser().get("email")));
            macrotask.addData("data[Token][fields]",            app.getUser().get("token"));
           
            try
            {
                if(macrotask.validate("INSERT", macrotask.getData(), errors))
                {
                    String json = macrotask.execute("INSERT");
                    
                    if(json.contains("macrotask"))
                    {
                        //La macrotache est bien crée, il faut récupérer son id et 
                        // hydrater le reste en fonction de l'id trouvée
                        Map<String, ArrayList<Map<String, String>>> values = new Gson().fromJson(
                                json, new TypeToken<HashMap<String, ArrayList<Map<String, String>>>>(){}.getType()
                        );
                        Task task                       = (Task)getModel("Task");
                        MacrotasksUser macrotaskUser    = (MacrotasksUser)getModel("MacrotasksUser");
                        String macrotask_id             = values.get("macrotask_id").get(0).get("id");
                        
                        //Ajouter les tasks ajoutées à la macrotache
                        int numTasks = view.getSelectedTaskData().getSize();
                        
                        //Ajouter les développeurs ajoutés a la macrotache
                        int numDevelopers = view.getSelectedDevData().getSize();
                    
                        for(int i = 0; i < numTasks; i++)
                        {
                            //Ajouter les taches ajoutées a la macrotache
                            String tasksSelection   = view.getSelectedTaskData().get(i).toString();
                            String taskName         = tasksSelection.split(" // ")[0];
                            String taskPriority     = tasksSelection.split(" // ")[1].substring(1, tasksSelection.split(" // ")[1].length() - 1);
                     
                            task.clearData();
                            task.addData("data[Task][macrotask_id]",    macrotask_id);
                            task.addData("data[Task][name]",            taskName);
                            task.addData("data[Task][priority]",        taskPriority);
                            task.addData("data[Task][progress]",        "0");
                            task.addData("data[Token][link]",           ECrypto.base64(app.getUser().get("email")));
                            task.addData("data[Token][fields]",         app.getUser().get("token"));
                        
                            if(task.validate("INSERT", task.getData(), errors))
                            {
                                String jsonTask = task.execute("INSERT");
                                
                                if(jsonTask.contains("error"))
                                {
                                    Map<String, Map<String, String>> newValues = new Gson().fromJson(
                                        jsonTask, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType()
                                    );

                                    if((errors = newValues.get("error")) != null)
                                        setError(errors.get(errors.keySet().toArray()[0].toString()));
                                    else
                                        setError("Une erreur inattendue est survenue");
                                }
                                else
                                    setError(jsonTask);
                            }
                            else 
                                setError(errors.get(errors.keySet().toArray()[0].toString()));
                        }
                    
                        for(int i = 0; i < numDevelopers; i++)
                        {
                            //Ajouter les développeurs affectés à une macrotâche
                            String developersSelection = view.getSelectedDevData().get(i).toString();
                            
                            macrotaskUser.clearData();
                            macrotaskUser.addData("data[MacrotasksUser][macrotask_id]", macrotask_id);
                            macrotaskUser.addData("data[MacrotasksUser][user_name]",    developersSelection);
                            macrotaskUser.addData("data[Token][link]",                  ECrypto.base64(app.getUser().get("email")));
                            macrotaskUser.addData("data[Token][fields]",                app.getUser().get("token"));
                            
                            if(macrotaskUser.validate("INSERT", macrotaskUser.getData(), errors))
                            {
                                String jsonUser = macrotaskUser.execute("INSERT");
                                
                                if(jsonUser.contains("error"))
                                {
                                    Map<String, Map<String, String>> newDevValues = new Gson().fromJson(
                                        jsonUser, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType()
                                    );

                                    if((errors = newDevValues.get("error")) != null)
                                        setError(errors.get(errors.keySet().toArray()[0].toString()));
                                    else
                                        setError("Une erreur inattendue est survenue");
                                }
                                else
                                    setError(jsonUser);
                            }
                            else
                                setError(errors.get(errors.keySet().toArray()[0].toString()));
                        }
                        
                        app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
                        app.message("La macrotâche a été correctement créée");
                    } //End if(json.contains("macrotask"))
                    else if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(
                            json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType()
                        );

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

    //Ajout de développeur à la liste scrollable
    private ActionListener addDeveloperListener() 
    {
        return (ActionEvent e) -> {
            if(!this.view.getSelectedDevData().contains(this.view.getDevelopers().getSelectedItem()))
                this.view.getSelectedDevData().addElement(this.view.getDevelopers().getSelectedItem());
        };
    }
    
    //Suppression de développeur(s) à la liste scrollable
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

    //Ajout de tâche à la liste scrollable
    private ActionListener addTaskListener() 
    {
        return (ActionEvent e) -> {
            if(!this.view.getMicrotaskName().getText().isEmpty()
            && !this.view.getSelectedTaskData().contains(this.view.getMicrotaskName().getText()))
                this.view.getSelectedTaskData().addElement(this.view.getMicrotaskName().getText()+" // ("+this.view.getPriorityTask().getValue()+")");
        };
    }

    //Suppression de tâche à la liste scrollable
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
