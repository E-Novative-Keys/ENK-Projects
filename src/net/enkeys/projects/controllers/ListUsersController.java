package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.ListUsersView;
import net.enkeys.projects.views.UsersManagerView;

public class ListUsersController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final ListUsersView view = (ListUsersView)super.view;
    
    private final ArrayList<Integer> updated;
    
    public ListUsersController(EApplication app, EView view)
    {
        super(app, view);
        this.updated = new ArrayList<>();
        addModel(new User());
        
        this.view.getDataTable().addTableModelListener(dataTableListener());
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getSaveButton().addActionListener(saveButtonListener());
        this.view.getSearchField().addKeyListener(searchFieldListener());
        
        initView();
    }
    
    private void initView() {
        User user = (User)getModel("User");
        Map<String, String> errors = new HashMap<>();
        
        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
        
        if(user.validate("SELECT", user.getData(), errors))
        {
            String json = user.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("users") != null)
            {
                ArrayList<HashMap<String, String>> users = values.get("users");
                
                for(HashMap<String, String> u : users)
                {
                    view.getDataTable().addValue(u);
                    view.getDataTable().addOrigin(u);
                }
                view.getListUsers().setAutoCreateRowSorter(true);
            }
            else
                System.err.println(json);
        }
        else
            System.err.println(errors);
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
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            if(updated.size() > 0)
            {
                switch(app.confirm("Souhaitez-vous appliquer les modifications effectuées ?", ENKProjects.YES_NO_CANCEL))
                {
                    case ENKProjects.YES:
                        if(saveUpdatedUsers())
                            app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
                        break;
                        
                    case ENKProjects.NO:
                        app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
                        break;
                        
                    case ENKProjects.CANCEL:
                        break;
                }
            }
            else
                app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
        };
    }
    
    private ActionListener deleteButtonListener() {
        return (ActionEvent e) -> {
            User user = (User)getModel("User");
            int[] rows = view.getListUsers().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les utilisateurs sélectionnés ?") == ENKProjects.YES)
            {                
                user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                user.addData("data[Token][fields]", app.getUser().get("token"));
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    int modelID = view.getListUsers().convertRowIndexToModel(rows[i]-i);
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(modelID, 0));
                    
                    user.addData("data[User][id]", id);
                    
                    try
                    {
                        if(user.validate("DELETE"))
                        {
                            String json = user.execute("DELETE");
                            
                            if(json.contains("users"))
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
    
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            if(updated.size() > 0 && app.confirm("Appliquer toutes les modifications ?") == ENKProjects.YES)
                saveUpdatedUsers();
        };
    }
    
    private boolean saveUpdatedUsers()
    {
        User user = (User)getModel("User");

        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
                
        for(int i : updated)
        {
            Map<String, String> u = view.getDataTable().getUserByID(i);
            Map<String, String> errors = new HashMap<>();

            if(u != null)
            {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                user.addData("data[User][id]", u.get("id"));
                user.addData("data[User][firstname]", u.get("firstname"));
                user.addData("data[User][lastname]", u.get("lastname"));
                user.addData("data[User][email]", u.get("email"));
                user.addData("data[User][role]", u.get("role"));
                user.addData("data[User][validated]", u.get("validated"));
                user.addData("data[User][updated]", df.format(new Date()));

                try
                {
                    if(user.validate("UPDATE", user.getData(), errors))
                    {
                        String json = user.execute("UPDATE", errors);

                        if(json != null && !json.isEmpty())
                        {
                            if(json.contains("users"))
                            {
                                updated.clear();
                                return true;
                            }
                            else if(json.contains("error"))
                            {
                                Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                                if((errors = values.get("error")) != null)
                                    setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                                else
                                    setError("Une erreur inattendue est survenue");
                                break;
                            }
                            else
                            {
                                setError("#" + i + " : " + json);
                                break;
                            }
                        }
                        else
                        {
                            setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                            break;
                        }
                    }
                    else
                    {
                        setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                        break;
                    }
                }
                catch(ERuleException | EHttpRequestException ex)
                {
                    app.getLogger().warning(ex.getMessage());
                }
            }
        }
        
        return false;
    }

    private KeyListener searchFieldListener() {
        return new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                try
                {
                    String search = view.getSearchField().getText().toLowerCase();

                    if(view.getListUsers().getAutoCreateRowSorter())
                    {
                        view.getListUsers().setAutoCreateRowSorter(false);
                        view.getListUsers().setRowSorter(null);
                    }
                    if(view.getDataTable().getValues().size() > 0)
                        view.getDataTable().clear();

                    for(HashMap<String, String> u : view.getDataTable().getOrigin())
                    {
                        if(u.get("id").toLowerCase().contains(search)
                        || u.get("firstname").toLowerCase().contains(search)
                        || u.get("lastname").toLowerCase().contains(search)
                        || u.get("email").toLowerCase().contains(search)
                        || u.get("role").toLowerCase().contains(search)
                        || u.get("validated").toLowerCase().contains(search))
                            view.getDataTable().addValue(u);
                    }

                    if(view.getDataTable().getValues().size() > 0)
                        view.getListUsers().setAutoCreateRowSorter(true);
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
