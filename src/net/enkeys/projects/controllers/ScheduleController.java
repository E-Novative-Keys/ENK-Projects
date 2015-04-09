/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Cloud;
import net.enkeys.projects.models.Macrotask;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.EditMacrotaskView;
import net.enkeys.projects.views.NewMacrotaskView;
import net.enkeys.projects.views.ScheduleView;

/**
 * TODO LIST
 * Revoir model Macrotask (min && max -> chiffre compris ou non ?)
 * + revoir les models Task && MacrotasksUser (tests uni)
 * 
 * Delete macrotask -> contraintes sur clé étrangères à regarder
 * 
 * EditMacrotaskController ->
 *  Empêcher la modification de plusieurs tâches en même temps
 *  Régler le souci sur la deadline (setter + bdd)
 *  Améliorer le front
 */
public class ScheduleController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final ScheduleView view = (ScheduleView)super.view;
    private final HashMap<String, String> project;
    
    public ScheduleController(EApplication app, EView view, HashMap<String, String> project) {
        super(app, view);
        addModel(new Macrotask());
        
        this.project = project;
        
        this.view.getAddButton().addActionListener(addButtonListener());
        this.view.getEditButton().addActionListener(editButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getBackButton().addActionListener(backButtonListener());
        
        this.view.getListMacrotasks().addMouseListener(taskMouseListener());
        
        initView();
    }
    
    private void initView() 
    {
        Macrotask macrotask = (Macrotask)getModel("Macrotask");
        Map<String, String> errors = new HashMap<>();
        
        macrotask.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        macrotask.addData("data[Token][fields]", app.getUser().get("token"));
        macrotask.addData("data[Project][id]", this.project.get("id"));
        
        if(macrotask.validate("SELECT", macrotask.getData(), errors))
        {
            String json = macrotask.execute("SELECT", errors);
            
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("macrotasks") != null)
            {
                ArrayList<HashMap<String, String>> macrotasks = values.get("macrotasks");
                
                for(HashMap<String, String> m : macrotasks)
                {
                    view.getDataTable().addValue(m);
                    view.getDataTable().addOrigin(m);
                }
                view.getListMacrotasks().setAutoCreateRowSorter(true);
            }
            else
                System.err.println(json);
        }
        else
            System.err.println(errors);
    }

    private ActionListener backButtonListener() 
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private ActionListener addButtonListener() 
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewMacrotaskController(app, new NewMacrotaskView(), this.project));
        };
    }

    private ActionListener editButtonListener() 
    {
        return (ActionEvent e) -> {
            if(view.getListMacrotasks().getSelectedRow() > -1) 
            {
                int modelID = view.getListMacrotasks().convertRowIndexToModel(view.getListMacrotasks().getSelectedRow());
                int id = Integer.parseInt((String)view.getDataTable().getValueAt(modelID, 0));
                
                app.getFrame(0).setContent(new EditMacrotaskController(app, new EditMacrotaskView(), this.project, view.getDataTable().getMacrotaskByID(id)));
            }
        };
    }

    private ActionListener deleteButtonListener() 
    {
        return (ActionEvent e) -> {
            Macrotask macrotask = (Macrotask)getModel("Macrotask");
            int[] rows = view.getListMacrotasks().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les projets sélectionnés ?") == ENKProjects.YES)
            {                
                macrotask.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                macrotask.addData("data[Token][fields]", app.getUser().get("token"));
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    int modelID = view.getListMacrotasks().convertRowIndexToModel(rows[i]-i);
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(modelID, 0));
                    Map<String, String> dataMacrotask = view.getDataTable().getMacrotaskByID(id);
                    
                    if(app.getUser().get("role").equalsIgnoreCase("admin") ||
                       app.getUser().get("id").equals(dataMacrotask.get("lead_id")))
                    {
                        macrotask.addData("data[Macrotask][id]", id);
                
                        try
                        {
                            if(macrotask.validate("DELETE"))
                            {
                                String json = macrotask.execute("DELETE");

                                if(json.contains("macrotasks"))
                                    view.getDataTable().removeValue(modelID);
                                else
                                    app.getLogger().warning("Error: " + json);
                            }
                        }
                        catch(ERuleException | EHttpRequestException ex)
                        {
                            app.getLogger().warning(ex.getMessage());
                        }
                    }
                    else
                        app.message("Vous n'êtes pas autorisé à supprimer la macrotâche " + dataMacrotask.get("name"), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private MouseListener taskMouseListener()
    {
        return new MouseAdapter() {
            //Cloud cloud = (Cloud)getModel("Cloud");
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                //cloud.clearData();
                
                //cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                //cloud.addData("data[Token][fields]", app.getUser().get("token"));
                
                // Si double click gauche de la souris
                if(e.getButton() == 1 && e.getClickCount() == 2)
                {
                    // Si l'élément sélectionné est un répertoire
                    if(/*directories.get(0).get(view.getDevList().getSelectedIndex())*/true)
                    {
                        //path[0].append(view.getDevList().getSelectedValue() + "/");
                        //listFiles(cloud, 0);
                    }
                    else return;
                        //downloadFile(cloud, 0);
                }
            }
        };
    }
}
