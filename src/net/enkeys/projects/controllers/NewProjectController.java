/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
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
import net.enkeys.projects.models.Project;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.NewProjectView;

/**
 *
 * @author Worker
 */
public class NewProjectController extends EController 
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final NewProjectView view = (NewProjectView) super.view;
    
    public NewProjectController(EApplication app, EView view) 
    {
        super(app, view);
        addModel(new Project());
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            Project project = (Project) getModel("Project");
            Map<String, String> errors = new HashMap<>();
            
            project.addData("data[Project][client_name]", view.getClient().getSelectedItem());
            project.addData("data[Project][name]", view.getProjectName().getText());
            project.addData("data[Project][description]", view.getDescription().getText());
            //project.addData("data[Project][lead]", view.getLead().getSelectedItem());
            //deadline (a changer en vraie date)
            project.addData("data[Project][estimation]", view.getEstimation().getText());
            //project.addData("data[Project][budget]", view.getBudget().getText());
            project.addData("data[Project][discount]", view.getDiscount().getText());
            project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            project.addData("data[Token][fields]", app.getUser().get("token"));
            
            try 
            {
                if(project.validate("INSERT", project.getData(), errors)) 
                {
                    String json = project.execute("INSERT");
                    System.out.println(json);
                    
                    /*if(json.contains("error")) 
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                        
                        if((errors = values.get("error")) != null)
                            setError(errors.get(errors.keySet().toArray()[0].toString()));
                        else
                            setError("Une erreur inattendue est survenue");
                    }
                    else if(json.contains("clients")) 
                    {
                        app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                        app.message("Le projet a été correctement créé");
                    }
                    else 
                        setError(json);
                    */
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
