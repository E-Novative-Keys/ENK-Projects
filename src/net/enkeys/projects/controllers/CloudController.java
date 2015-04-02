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
import javax.swing.JFileChooser;
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
        
        this.view.getDevList().addKeyListener(devKeyListener());
        this.view.getDevList().addMouseListener(devMouseListener());
        
        this.view.getClientsList().addKeyListener(clientsKeyListener());
        this.view.getClientsList().addMouseListener(clientsMouseListener());
        
        this.view.getUploadDevButton().addActionListener(UploadDevListener());
        this.view.getUploadClientButton().addActionListener(UploadClientListener());
        
        this.view.getPrevDevButton().addActionListener(PrevDevListener());
        this.view.getPrevClientButton().addActionListener(PrevClientListener());
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
                
        cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        cloud.addData("data[Token][fields]", app.getUser().get("token"));
        
        path[0].append("/");
        path[1].append("/");
        
        listFiles(cloud, 0);
        listFiles(cloud, 1);
    }
    
    private KeyListener devKeyListener()
    {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE)
                {
                    Cloud cloud = (Cloud)getModel("Cloud");
                    
                    cloud.clearData();
                    
                    cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                    cloud.addData("data[Token][fields]", app.getUser().get("token"));
                    
                    deleteFile(cloud, 0);
                }
            } 
        };
    }
        
    private KeyListener clientsKeyListener()
    {
        return new KeyAdapter() {
            
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE)
                {
                    Cloud cloud = (Cloud)getModel("Cloud");
                    
                    cloud.clearData();
                    
                    cloud.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                    cloud.addData("data[Token][fields]", app.getUser().get("token"));
                    
                    deleteFile(cloud, 1);
                }
            }      
        };
    }
    
    private MouseListener devMouseListener()
    {
        return new MouseAdapter() {
            Cloud cloud                 = (Cloud)getModel("Cloud");
            
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
                    if(directories.get(0).get(view.getDevList().getSelectedIndex()))
                    {
                        path[0].append(view.getDevList().getSelectedValue() + "/");
                        listFiles(cloud, 0);
                    }
                    else
                        downloadFile(cloud, 0);
                }
            }
        };
    }
    
    private MouseListener clientsMouseListener()
    {
        return new MouseAdapter() {
            Cloud cloud = (Cloud)getModel("Cloud");          
        
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
                        listFiles(cloud, 1);
                    }
                    else
                        downloadFile(cloud, 1);                
                }
            } 
        };
    }
    
    private ActionListener UploadDevListener()
    {
        return (ActionEvent e) -> {
            uploadFile(0);
        };
    }
    
    private ActionListener UploadClientListener()
    {
        return (ActionEvent e) -> {
            uploadFile(1);
        };
    }
    
    private ActionListener PrevDevListener()
    {
        return (ActionEvent e) -> {
            System.out.println(path[0].toString().split("/"));
            //path[0].substring(0, path[0].length()-1);
            //System.out.println(path[0].substring(0, path[0].length()-1).toString());
        };
    }
    
    private ActionListener PrevClientListener()
    {
        return (ActionEvent e) -> {
            System.out.println(path[1].toString());
        };
    }
    private void listFiles(Cloud cloud, int index)
    {
        Map<String, String> errors  = new HashMap<>();
        String[] actions            = new String[]{"LIST_DEV", "LIST_CLIENT"};
         
        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id")));
        cloud.addData("data[Cloud][directory]", ECrypto.base64(path[index].toString()));

        if(cloud.validate(actions[index], cloud.getData(), errors))
        {
            String json = cloud.execute(actions[index], errors, true);
            Map<String, ArrayList<HashMap<String, String>>> values  = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());

            if(values != null && values.get("content") != null)
            {
                ArrayList<HashMap<String, String>> files = values.get("content");

                directories.get(index).clear();

                if(index == 0)
                    view.getDevData().clear();
                else
                    view.getClientsData().clear();

                for(Map<String, String> f : files)
                {
                    if(index == 0)
                        view.getDevData().addElement(f.get("filename"));
                    else
                        view.getClientsData().addElement(f.get("filename"));
                    directories.get(index).add((f.get("isDir") == "true") ? Boolean.TRUE : Boolean.FALSE);
                }
            }
            else
                System.err.println(json);
        }
    }
    
    private void deleteFile(Cloud cloud, int index)
    {
        Map<String, String> errors  = new HashMap<>();
        String[] users = new String[]{"dev", "client"};
        
        if(app.confirm("Êtes-vous certain de vouloir supprimer le fichier/dossier ?") == ENKProjects.YES)
        {
            cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id")));
            
            if(index == 0)
            {
                cloud.addData("data[Cloud][directory]", 
                        ECrypto.base64(path[index].toString() + 
                        (directories.get(index).get(view.getDevList().getSelectedIndex())
                        ? view.getDevList().getSelectedValue() : ""))
                );
                cloud.addData("data[Cloud][name]", 
                    (directories.get(index).get(view.getDevList().getSelectedIndex())
                    ? "" : view.getDevList().getSelectedValue())
                );
            }
            else
            {
                cloud.addData("data[Cloud][directory]", 
                    ECrypto.base64(path[index].toString() + 
                    (directories.get(index).get(view.getClientsList().getSelectedIndex())
                    ? view.getClientsList().getSelectedValue() : ""))
                );
                cloud.addData("data[Cloud][name]", 
                    (directories.get(index).get(view.getClientsList().getSelectedIndex())
                    ? "" : view.getClientsList().getSelectedValue())
                );
            }
            cloud.addData("data[Cloud][user]", users[index]);

            if(cloud.validate("DEL_FILE", cloud.getData(), errors))
            {
                String json = cloud.execute("DEL_FILE", errors, true);
                HashMap<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

                if(values != null)
                {
                    if(values.get("file") != null)
                    {
                        if(index == 0)
                            view.getDevData().remove(view.getDevList().getSelectedIndex());
                        else
                            view.getClientsData().remove(view.getClientsList().getSelectedIndex());
                    }
                    else if(values.get("error") != null)
                        setError(values.get("error"));
                }
            }
        }
    }
    
    private void downloadFile(Cloud cloud, int index)
    {
        Map<String, String> errors  = new HashMap<>();
        EHttpRequest request        = null;
        String[] users              = new String[]{"dev", "client"};
        
        if(index == 0)
            cloud.addData("data[Cloud][path]", ECrypto.base64(
                path[index].toString() + File.separator + view.getDevList().getSelectedValue())
            );
        else
            cloud.addData("data[Cloud][path]", ECrypto.base64(
                path[index].toString() + File.separator + view.getClientsList().getSelectedValue())
            );
        
        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id"))); 
        cloud.addData("data[Cloud][user]", users[index]);

        String json = cloud.execute("DOWNLOAD", errors, true);
        HashMap<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

        if(values != null && values.get("token") != null)
        {                      
            try
            {
                request = new EHttpRequest(new URL("http://enkwebservice.com/cloud/files/download/" + values.get("token")));
            }
            catch(MalformedURLException ex)
            {
                setError("Request uninitialized !");
            }

            try
            {
                request.download(System.getProperty("user.home") + File.separator + "Téléchargements");
            }
            catch(ESystemException ex)
            {
                setError("Le fichier a bien été téléchargé cependant, il ne peut être ouvert.");
            }
        }
    }
    
    private void uploadFile(int index)
    {
        JFileChooser filechooser    = new JFileChooser(System.getProperty("user.home"));
        int returnVal               = filechooser.showOpenDialog(filechooser);
        String[] users              = new String[]{"dev", "client"};

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            try {
                EHttpRequest request = new EHttpRequest(new URL("http://enkwebservice.com/cloud/files/add"));
                HashMap<String, String> data = new HashMap<>();

                data.put("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                data.put("data[Token][fields]", app.getUser().get("token"));
                data.put("data[Cloud][project]", ECrypto.base64(project.get("id")));
                data.put("data[Cloud][directory]", ECrypto.base64(path[index].toString()));
                data.put("data[Cloud][user]", users[index]);

                String json = request.upload(data, filechooser.getSelectedFile());
                HashMap<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

                if(values != null && values.get("upload") != null)
                {
                    if(index == 0)
                        view.getDevData().addElement(filechooser.getSelectedFile().getName());
                    else
                        view.getClientsData().addElement(filechooser.getSelectedFile().getName());
                    directories.get(index).add(Boolean.FALSE);
                    app.message("Votre fichier a bien été uploadé");
                }
            }
            catch(MalformedURLException ex)
            {
                setError("Upload failed !");
            }    
        }
    }
    
    private void setError(String err) 
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
