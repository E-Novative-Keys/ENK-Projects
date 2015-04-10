
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Macrotask;
import net.enkeys.projects.views.EditMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

class EditMacrotaskController extends EController 
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final EditMacrotaskView view = (EditMacrotaskView) super.view;
    private final HashMap<String, String> project;
    private final HashMap<String, String> data;

    public EditMacrotaskController(ENKProjects              app, 
                                   EditMacrotaskView        view, 
                                   HashMap<String, String>  project, 
                                   HashMap<String, String>  data) 
    {
        super(app, view);
        addModel(new Macrotask());
        
        this.project = project;
        this.data = data;
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
        
        initView();
    }
    
    private void initView() 
    {
        System.out.println(data.toString());
        this.view.setMacrotaskName(this.data.get("name"));
        this.view.setMacrotaskHour(this.data.get("hour"));
        this.view.setMacrotaskMinute(this.data.get("minute"));
        this.view.setMacrotaskPriority(this.data.get("priority"));
        try
        {
            view.getDeadline().setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.data.get("deadline")));
        }
        catch (ParseException ex)
        {
            app.getLogger().warning(ex.getMessage());
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
            macrotask.addData("data[Macrotask][hour]", view.getHours().getValue());
            macrotask.addData("data[Macrotask][minute]", view.getMinutes().getValue());
            macrotask.addData("data[Macrotask][id]", data.get("id"));
            macrotask.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            macrotask.addData("data[Token][fields]", app.getUser().get("token"));
           
            try
            {
                if(macrotask.validate("UPDATE", macrotask.getData(), errors))
                {
                    String json = macrotask.execute("UPDATE");
                    
                    if(json.contains("macrotask"))
                    {
                        Map<String, ArrayList<Map<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<Map<String, String>>>>(){}.getType());
                        
                        app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
                        app.message("La macrotâche a été correctement créée");
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
    
    private void setError(String err)
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
