
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import net.enkeys.projects.models.Project;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListProjectsView;
import net.enkeys.projects.views.NewProjectView;

public class ListProjectsController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final ListProjectsView view = (ListProjectsView)super.view;
    
    public ListProjectsController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Project());
        
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getEditButton().addActionListener(editButtonListener());
        this.view.getSearchField().addKeyListener(searchFieldListener());
        
        initView();
    }
    
    private void initView() 
    {
        Project project = (Project)getModel("Project");
        Map<String, String> errors = new HashMap<>();
        
        project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        project.addData("data[Token][fields]", app.getUser().get("token"));
        
        if(project.validate("SELECT", project.getData(), errors))
        {
            String json = project.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("projects") != null)
            {
                ArrayList<HashMap<String, String>> projects = values.get("projects");
                
                for(HashMap<String, String> p : projects)
                {
                    view.getDataTable().addValue(p);
                    view.getDataTable().addOrigin(p);
                }
                view.getListProjects().setAutoCreateRowSorter(true);
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
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private ActionListener deleteButtonListener() 
    {
        return (ActionEvent e) -> {
            Project project = (Project)getModel("Project");
            int[] rows = view.getListProjects().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les projets sélectionnés ?") == ENKProjects.YES)
            {                
                project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                project.addData("data[Token][fields]", app.getUser().get("token"));
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    int modelID = view.getListProjects().convertRowIndexToModel(rows[i]-i);
                    int id = Integer.parseInt((String)view.getDataTable().getValue(modelID).get("id"));
                    
                    Map<String, String> dataProject = view.getDataTable().getProjectByID(id);
                    
                    if(app.getUser().get("role").equalsIgnoreCase("admin") ||
                       app.getUser().get("id").equals(dataProject.get("lead_id")))
                    {
                        project.addData("data[Project][id]", id);
                
                        try
                        {
                            if(project.validate("DELETE"))
                            {
                                String json = project.execute("DELETE");

                                if(json.contains("projects"))
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
                        app.message("Vous n'êtes pas autorisé à supprimer le projet " + dataProject.get("name"), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private ActionListener editButtonListener() 
    {
        return (ActionEvent e) -> {
            if(view.getListProjects().getSelectedRow() > -1) 
            {
                int modelID = view.getListProjects().convertRowIndexToModel(view.getListProjects().getSelectedRow());
                int id = Integer.parseInt((String)view.getDataTable().getValue(modelID).get("id"));
                    
                app.getFrame(0).setContent(new EditProjectController(app, new NewProjectView(), view.getDataTable().getProjectByID(id)));
            }
        };
    }
    
    private KeyListener searchFieldListener() 
    {
        return new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                try
                {
                    String search = view.getSearchField().getText().toLowerCase();

                    if(view.getListProjects().getAutoCreateRowSorter())
                    {
                        view.getListProjects().setAutoCreateRowSorter(false);
                        view.getListProjects().setRowSorter(null);
                    }
                    if(view.getDataTable().getValues().size() > 0)
                        view.getDataTable().clear();

                    for(HashMap<String, String> p : view.getDataTable().getOrigin())
                    {
                        if(p.get("id").toLowerCase().contains(search)
                        || p.get("client_id").toLowerCase().contains(search)
                        || p.get("name").toLowerCase().contains(search)
                        || p.get("description").toLowerCase().contains(search)
                        || p.get("deadline").toLowerCase().contains(search)
                        || p.get("estimation").toLowerCase().contains(search)
                        || p.get("budget").toLowerCase().contains(search)
                        || p.get("discount").toLowerCase().contains(search))
                            view.getDataTable().addValue(p);
                    }

                    if(view.getDataTable().getValues().size() > 0)
                        view.getListProjects().setAutoCreateRowSorter(true);
                }
                catch(IndexOutOfBoundsException ex)
                {
                    app.getLogger().warning(ex.getMessage());
                }
            }
        };
    }

    private void setError(String err) 
    {
        app.message(err, JOptionPane.ERROR_MESSAGE);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
