
package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Task;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.EditTasksView;
import net.enkeys.projects.views.ScheduleView;

public class EditTasksController extends EController
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final EditTasksView view = (EditTasksView)super.view;
    private final HashMap<String, String> project;
    private final HashMap<String, String> data;
    
    private final ArrayList<Integer> updated;
    
    public EditTasksController(EApplication app, EView view, HashMap<String, String>  project, HashMap<String, String>  data) 
    {
        super(app, view);
        this.updated = new ArrayList<>();
        addModel(new Task());
        //addModel(new User());
        
        this.project = project;
        this.data = data;
        
        this.view.getDataTable().addTableModelListener(dataTableListener());
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getSaveButton().addActionListener(saveButtonListener());
        this.view.getAddButton().addActionListener(addTaskButtonListener());
        
        initView();
    }   
    
    private void initView()
    {
        Task task = (Task)getModel("Task");
        Map<String, String> errors = new HashMap<>();
        
        task.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        task.addData("data[Token][fields]", app.getUser().get("token"));
        task.addData("data[Macrotask][id]", data.get("id"));
        
        if(task.validate("SELECT", task.getData(), errors))
        {
            String json = task.execute("SELECT", errors);
            System.out.println("jsontask : "+json);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("tasks") != null)
            {
                ArrayList<HashMap<String, String>> tasks = values.get("tasks");
                
                for(HashMap<String, String> t : tasks)
                {
                    view.getDataTable().addValue(t);
                    view.getDataTable().addOrigin(t);
                }
                view.getListTasks().setAutoCreateRowSorter(true);
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
            app.getFrame(0).setContent(new ScheduleController(app, new ScheduleView(), this.project));
        };
    }

    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            if(updated.size() > 0 && app.confirm("Appliquer toutes les modifications ?") == ENKProjects.YES)
                saveUpdatedTasks();
        };
    }
    
    private boolean saveUpdatedTasks()
    {
        boolean success = false;
        Task task = (Task)getModel("Task");

        task.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        task.addData("data[Token][fields]", app.getUser().get("token"));
                
        for(int i : updated)
        {
            Map<String, String> t = view.getDataTable().getTaskByID(i);
            Map<String, String> errors = new HashMap<>();

            if(t != null)
            {
                task.addData("data[Task][id]", t.get("id"));
                task.addData("data[Task][priority]", t.get("priority"));
                task.addData("data[Task][name]", t.get("name"));
                task.addData("data[Task][progress]", t.get("progress"));

                try
                {
                    if(task.validate("UPDATE", task.getData(), errors))
                    {
                        String json = task.execute("UPDATE", errors);
                        
                        if(json != null && !json.isEmpty())
                        {
                            if(json.contains("tasks"))
                                success = true;
                            else if(json.contains("error"))
                            {
                                Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                                if((errors = values.get("error")) != null)
                                    setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                                else
                                    setError("Une erreur inattendue est survenue");
                                success = false;
                                break;
                            }
                            else
                            {
                                setError("#" + i + " : " + json);
                                success = false;
                                break;
                            }
                        }
                        else
                        {
                            setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                            success = false;
                            break;
                        }
                    }
                    else
                    {
                        setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                        success = false;
                        break;
                    }
                }
                catch(ERuleException | EHttpRequestException ex)
                {
                    app.getLogger().warning(ex.getMessage());
                }
            }
        }
        
        if(success)
            updated.clear();
        return success;
    }

    private ActionListener deleteButtonListener() {
        return (ActionEvent e) -> {
            Task task = (Task)getModel("Task");
            int[] rows = view.getListTasks().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les tâches sélectionnés ?") == ENKProjects.YES)
            {                
                task.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                task.addData("data[Token][fields]", app.getUser().get("token"));
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    int modelID = view.getListTasks().convertRowIndexToModel(rows[i]-i);
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(modelID, 0));
                    
                    task.addData("data[Task][id]", id);
                    
                    try
                    {
                        if(task.validate("DELETE"))
                        {
                            String json = task.execute("DELETE");
                            System.out.println("json : "+json);
                            
                            if(json.contains("tasks"))
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
            }
        };
    }

    private TableModelListener dataTableListener()
    {
        return (TableModelEvent e) -> {            
            switch(e.getType())
            {
                case TableModelEvent.INSERT:
                    break;
                    
                case TableModelEvent.UPDATE:
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(e.getFirstRow(), 0));
                    
                    if(!updated.contains(id))
                        updated.add(id);
                    break;
                    
                case TableModelEvent.DELETE:
                    break;
            }
        };
    }

    private ActionListener addTaskButtonListener()
    {
        return (ActionEvent e) -> {
            String newName = this.view.getNameField().getText();
            String newPriority = this.view.getPriorityTask().getValue().toString();
            String newProgress = "0";
            System.out.println("New task : "+newName+" "+newPriority+" "+newProgress);
            
            HashMap<String, String> newTask = new HashMap<>();
            newTask.put("name", newName);
            newTask.put("priority", newPriority);
            newTask.put("progress", newProgress);
            newTask.put("id", "//");

            ArrayList<HashMap<String, String>> tasks = new ArrayList<HashMap<String, String>>();
            tasks.add(newTask);

            for(HashMap<String, String> t : tasks)
            {
                view.getDataTable().addValue(t);
                view.getDataTable().addOrigin(t);
            }
            view.getListTasks().setAutoCreateRowSorter(true);  
        }; 
    }
    
    private void setError(String err)
    {
        app.message(err, JOptionPane.ERROR_MESSAGE);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
