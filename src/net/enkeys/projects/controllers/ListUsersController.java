package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ListUsersView;

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
        
        
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private TableModelListener dataTableListener() {
        return (TableModelEvent e) -> {
    
        };
    }
    
    private ActionListener deleteButtonListener() {
        return (ActionEvent e) -> {
    
        };
    }
    
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            
        };
    }

    private KeyListener searchFieldListener() {
        return new KeyAdapter()
        {
            
        };
    }
}
