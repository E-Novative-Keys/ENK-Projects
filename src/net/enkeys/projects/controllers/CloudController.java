package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.ESystemException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.framework.utils.EHttpRequest;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Cloud;
import net.enkeys.projects.views.CloudView;
import net.enkeys.projects.views.CurrentProjectManagerView;

public class CloudController extends EController
{
    private final ENKProjects app   = (ENKProjects)super.app;
    private final CloudView view    = (CloudView)super.view;
    private ArrayList<ArrayList<Boolean>> directories;
    private final HashMap<String, String> project;
    private StringBuilder path[] = new StringBuilder[2];
    
    
    public CloudController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new Cloud());
        
        this.project        = project;
        this.path[0]        = new StringBuilder();
        this.path[1]        = new StringBuilder();
        
        this.directories    = new ArrayList<>(2);
        
        this.directories.add(new ArrayList<>());
        this.directories.add(new ArrayList<>());
        
        //this.view.getDevList().addKeyListener(devKeyListener());
        //this.view.getDevList().addMouseListener(devMouseListener());
        this.view.getClientsList().addKeyListener(clientsKeyListener());
        this.view.getClientsList().addMouseListener(clientsMouseListener());
        this.view.getBackButton().addActionListener(backListener());
        
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
                {
                    view.getDevData().addElement(/*EResources.loadImageIcon("back_dark.png") + */f.get("filename"));
                    this.directories.get(0).add((f.get("isDir") == "true") ? Boolean.TRUE : Boolean.FALSE);
                }
                
                path[0].append("/");
            }
            else
                System.err.println(json);
            
            json    = cloud.execute("LIST_CLIENT", errors, true);
            values  = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("content") != null)
            {
                ArrayList<HashMap<String, String>> files = values.get("content");
                
                for(Map<String, String> f : files)
                {
                    view.getClientsData().addElement(f.get("filename"));
                    this.directories.get(1).add((f.get("isDir") == "true") ? Boolean.TRUE : Boolean.FALSE);
                }
                
                path[1].append("/");
            }
            else
                System.err.println(json);
        }
        else
            System.err.println(errors);
    }
    
    /*private KeyListener devKeyListener()
    {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE)
                    System.out.println("supp listener");
            } 
        };
    }*/
        
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
    
    /*private MouseListener devMouseListener()
    {
        return new MouseAdapter() {
            Cloud cloud                 = (Cloud)getModel("Cloud");
            Map<String, String> errors  = new HashMap<>();
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                cloud.clearData();
                
                cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                cloud.addData("data[Token][fields]", app.getUser().get("token"));
                
                // Si double click gauche de la souris
                if(e.getButton() == 1 && e.getClickCount() == 2)
                {
                    // Si l'élément sélectionné est un répertoire
                    if(directories.get(0).get(view.getClientsList().getSelectedIndex()))
                    {
                        path[0].append(view.getDevList().getSelectedValue() + "/");
                        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id")));
                        cloud.addData("data[Cloud][directory]", ECrypto.base64(path[0].toString()));
                        
                        if(cloud.validate("LIST_DEV", cloud.getData(), errors))
                        {
                            
                            String json = cloud.execute("LIST_DEV", errors, true);
                            Map<String, ArrayList<HashMap<String, String>>> values  = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
                            if(values != null && values.get("content") != null)
                            {
                                ArrayList<HashMap<String, String>> files = values.get("content");
                                
                                view.getClientsData().clear();
                                directories.get(0).clear();
                                
                                for(Map<String, String> f : files)
                                {
                                    view.getClientsData().addElement(f.get("filename"));
                                    directories.get(0).add((f.get("isDir") == "true") ? Boolean.TRUE : Boolean.FALSE);
                                }
                            }
                            else
                                System.err.println(json);
                        }
                        else
                            setError(errors);
                    }
                    else
                    {
                        EHttpRequest request = null;
                        cloud.addData("data[Cloud][path]", ECrypto.base64(
                            path[0].toString() + File.separator + view.getDevList().getSelectedValue())
                        );
                        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id"))); 
                        cloud.addData("data[Cloud][user]", "dev");

                        String json = cloud.execute("DOWNLOAD", errors, true);
                        HashMap<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

                        if(values != null && values.get("token") != null)
                        {                      
                            try
                            {
                                request = new EHttpRequest(new URL("http://enkwebservice.com/cloud/files/download/" + values.get("token")));
                            }
                            catch (MalformedURLException ex)
                            {
                                setError("Request uninitialized !");
                            }

                            try
                            {
                                request.download("/home/esp010/Téléchargements");
                            }
                            catch(ESystemException ex)
                            {
                                setError("Le fichier a bien été téléchargé cependant, il ne peut être ouvert.");
                            }
                        }
                    }
                }
            }
        };
    }*/
    
    private MouseListener clientsMouseListener()
    {
        return new MouseAdapter() {
            Cloud cloud                 = (Cloud)getModel("Cloud");
            Map<String, String> errors  = new HashMap<>();
            
        
            @Override
            public void mouseClicked(MouseEvent e)
            {
                cloud.clearData();
                
                cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                cloud.addData("data[Token][fields]", app.getUser().get("token"));
                               
                // Si double click gauche de la souris
                if(e.getButton() == 1 && e.getClickCount() == 2)
                {
                    // Si l'élément sélectionné est un répertoire
                    if(directories.get(1).get(view.getClientsList().getSelectedIndex()))
                    {
                        path[1].append(view.getClientsList().getSelectedValue() + "/");
                        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id")));
                        cloud.addData("data[Cloud][directory]", ECrypto.base64(path[1].toString()));
                        
                        if(cloud.validate("LIST_CLIENT", cloud.getData(), errors))
                        {
                            
                            String json = cloud.execute("LIST_CLIENT", errors, true);
                            Map<String, ArrayList<HashMap<String, String>>> values  = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
                            if(values != null && values.get("content") != null)
                            {
                                ArrayList<HashMap<String, String>> files = values.get("content");
                                
                                view.getClientsData().clear();
                                directories.get(1).clear();
                                
                                for(Map<String, String> f : files)
                                {
                                    view.getClientsData().addElement(f.get("filename"));
                                    directories.get(1).add((f.get("isDir") == "true") ? Boolean.TRUE : Boolean.FALSE);
                                }
                            }
                            else
                                System.err.println(json);
                        }
                        else
                            setError(errors);
                    }
                    else
                    {
                        EHttpRequest request = null;
                        cloud.addData("data[Cloud][path]", ECrypto.base64(
                            path[1].toString() + File.separator + view.getClientsList().getSelectedValue())
                        );
                        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id"))); 
                        cloud.addData("data[Cloud][user]", "client");

                        String json = cloud.execute("DOWNLOAD", errors, true);
                        HashMap<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

                        if(values != null && values.get("token") != null)
                        {                      
                            try
                            {
                                request = new EHttpRequest(new URL("http://enkwebservice.com/cloud/files/download/" + values.get("token")));
                            }
                            catch (MalformedURLException ex)
                            {
                                setError("Request uninitialized !");
                            }

                            try
                            {
                                request.download("/home/esp010/Téléchargements");
                            }
                            catch(ESystemException ex)
                            {
                                setError("Le fichier a bien été téléchargé cependant, il ne peut être ouvert.");
                            }
                        }
                    }
                }
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
