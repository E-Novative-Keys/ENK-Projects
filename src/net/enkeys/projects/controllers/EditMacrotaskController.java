
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Macrotask;
import net.enkeys.projects.models.MacrotasksUser;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.EditMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 * Contrôlleur EditMacrotaskController.
 * Gestion de l'édtion d'une macrotâche.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
class EditMacrotaskController extends EController 
{
    private final ENKProjects app           = (ENKProjects) super.app;
    private final EditMacrotaskView view    = (EditMacrotaskView) super.view;
    private final HashMap<String, String> project;
    private final HashMap<String, String> data;

    public EditMacrotaskController(ENKProjects              app, 
                                   EditMacrotaskView        view, 
                                   HashMap<String, String>  project, 
                                   HashMap<String, String>  data) 
    {
        super(app, view);
        addModel(new Macrotask());
        addModel(new User());
        addModel(new MacrotasksUser());
        
        this.project    = project;
        this.data       = data;
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
        
        //Ajout / Suppression de développeurs
        this.view.getAddDeveloperButton().addActionListener(addDeveloperListener());
        this.view.getSupprDeveloperButton().addActionListener(supprDeveloperListener());
        
        initView();
    }
    
    private void initView() 
    {
        User user                   = (User)getModel("User");
        Map<String, String> errors  = new HashMap<>();
        
        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
        user.addData("data[User][project_id]", project.get("id"));
        user.addData("data[User][getdev]", true);
        
        this.view.setMacrotaskName(this.data.get("name"));
        this.view.setMacrotaskHour(this.data.get("hour"));
        this.view.setMacrotaskMinute(this.data.get("minute"));
        this.view.setMacrotaskPriority(this.data.get("priority"));
        
        try
        {
            view.getDeadline().setDate(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.data.get("deadline"))
            );
        }
        catch (ParseException ex)
        {
            app.getLogger().warning(ex.getMessage());
        }
        
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
        
        
        //Reste le chargement des devs actuels, récupérer depuis le web service
        //à partir de la table macrotasks_users
        MacrotasksUser macrotasksUser = (MacrotasksUser)getModel("MacrotasksUser");
        
        macrotasksUser.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        macrotasksUser.addData("data[Token][fields]", app.getUser().get("token"));
        macrotasksUser.addData("data[Macrotask][id]", data.get("id"));
        
        if(macrotasksUser.validate("SELECT", macrotasksUser.getData(), errors))
        {
            String jsonMacroUser = macrotasksUser.execute("SELECT", errors); 
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(
                jsonMacroUser, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
            );
            
            if(values != null && values.get("users") != null)
            {
                ArrayList<HashMap<String, String>> macrotasksUsers = values.get("users");
                
                for(HashMap<String, String> u : macrotasksUsers)
                    view.getSelectedDevData().addElement(u.get("firstname") + " " + u.get("lastname"));
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

    //Sauvegarde des données via le webservice
    //Validation des models associés
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
            
            String hours    = new String(Float.parseFloat(view.getHours().getValue().toString()) + "");
            String minutes  = new String(Float.parseFloat(view.getMinutes().getValue().toString()) + "");    
            
            macrotask.addData("data[Macrotask][project_id]",    this.project.get("id"));
            macrotask.addData("data[Macrotask][name]",          view.getMacrotaskName().getText());
            macrotask.addData("data[Macrotask][deadline]",      df.format(view.getDeadline().getDate()));
            macrotask.addData("data[Macrotask][priority]",      (Integer)view.getPriority().getValue());
            macrotask.addData("data[Macrotask][hour]",          hours);
            macrotask.addData("data[Macrotask][minute]",        minutes);
            macrotask.addData("data[Macrotask][id]",            data.get("id"));
            macrotask.addData("data[Token][link]",              ECrypto.base64(app.getUser().get("email")));
            macrotask.addData("data[Token][fields]",            app.getUser().get("token"));
           
            try
            {
                if(macrotask.validate("UPDATE", macrotask.getData(), errors))
                {
                    String json = macrotask.execute("UPDATE");
                    
                    if(json.contains("macrotask"))
                    {
                        Map<String, String> values      = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
                        MacrotasksUser macrotaskUser    = (MacrotasksUser)getModel("MacrotasksUser");
                        String macrotask_id             = values.get("macrotask_id");
                        
                        //Ajouter les développeurs ajoutés a la macrotache
                        int numDevelopers = view.getSelectedDevData().getSize();
                        
                        for(int i = 0; i < numDevelopers; i++)
                        {
                            //Ajouter les développeurs affectés à une macrotâche
                            String developersSelection = view.getSelectedDevData().get(i).toString();
                            
                            macrotaskUser.clearData();
                            macrotaskUser.addData("data[MacrotasksUser][macrotask_id]", macrotask_id);
                            macrotaskUser.addData("data[MacrotasksUser][user_name]",    developersSelection);
                            macrotaskUser.addData("data[Token][link]",                  ECrypto.base64(app.getUser().get("email")));
                            macrotaskUser.addData("data[Token][fields]",                app.getUser().get("token"));
                            //Indication de suppresion de liste en début d'insert
                            macrotaskUser.addData("data[MacrotasksUser][purge]",        i==0 ? "true" : "false");
                            
                            if(macrotaskUser.validate("UPDATE", macrotaskUser.getData(), errors))
                            {
                                String jsonUser = macrotaskUser.execute("UPDATE");
                                
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
                        app.message("La macrotâche a été correctement éditée");
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
    
    //Ajout d'un développeur à la liste scrollable
    private ActionListener addDeveloperListener() 
    {
        return (ActionEvent e) -> {
            if(!this.view.getSelectedDevData().contains(this.view.getDevelopers().getSelectedItem()))
                this.view.getSelectedDevData().addElement(this.view.getDevelopers().getSelectedItem());
        };
    }
    
    //SUppresion d'un développeur à la liste scrollable
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
    
    private void setError(String err)
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
