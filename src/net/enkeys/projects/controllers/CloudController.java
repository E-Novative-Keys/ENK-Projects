package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Cloud;
import net.enkeys.projects.views.CloudView;
import net.enkeys.projects.views.CurrentProjectManagerView;

public class CloudController extends EController
{
    private final ENKProjects app   = (ENKProjects)super.app;
    private final CloudView view    = (CloudView)super.view;
    private Map<String, String> directories;
    private final HashMap<String, String> project;
    
    
    public CloudController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new Cloud());
        
        this.project = project;
        
        this.view.getBackButton().addActionListener(backListener());
        this.view.getClientsList().addKeyListener(clientsKeyListener());
        this.view.getClientsList().addMouseListener(clientsMouseListener());
        
        initView();
    }
    
    private ActionListener backListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private void initView()
    {
        Cloud cloud = (Cloud)getModel("Cloud");
        Map<String, String> errors = new HashMap<>();
        
        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id")));
        cloud.addData("data[Cloud][directory]", ECrypto.base64("/"));
        
        cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        cloud.addData("data[Token][fields]", app.getUser().get("token"));
        
        if(cloud.validate("LIST_DEV", cloud.getData(), errors))
        {
            String json = cloud.execute("LIST_DEV", errors, true);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("content") != null)
            {
                ArrayList<HashMap<String, String>> files = values.get("content");
                
                for(Map<String, String> f : files)
                    view.getDevData().addElement(/*EResources.loadImageIcon("back_dark.png") + */f.get("filename"));
            }
            else
                System.err.println(json);
            
            json    = cloud.execute("LIST_CLIENT", errors, true);
            values  = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("content") != null)
            {
                ArrayList<HashMap<String, String>> files = values.get("content");
                
                for(Map<String, String> f : files)
                    view.getClientsData().addElement(f.get("filename"));
            }
            else
                System.err.println(json);
        }
        else
            System.err.println(errors);
    }
        
    private KeyListener clientsKeyListener()
    {
        return new KeyAdapter() {
            
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE)
                    System.out.println("supp listener");
            }      
        };
    }
    
    private MouseListener clientsMouseListener()
    {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                if(e.getButton() == 1)
                {
                    
                }
                else if(e.getButton() == 3)
                    System.out.println("right");
            } 
        };
    }
}
