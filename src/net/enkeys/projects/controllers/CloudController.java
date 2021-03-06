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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

/**
 * Contrôlleur CloudController.
 * Gestion des actions du Cloud.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class CloudController extends EController
{
    private final ENKProjects app   = (ENKProjects)super.app;
    private final CloudView view    = (CloudView)super.view;
    private StringBuilder path[]    = new StringBuilder[2];
    private ArrayList<ArrayList<Boolean>> directories;
    private final HashMap<String, String> project;
    private JMenu cloudMenu;
    
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
        
        //Liste des développeurs
        this.view.getDevList().addKeyListener(devKeyListener());
        this.view.getDevList().addMouseListener(devMouseListener());
        
        //Liste des clients
        this.view.getClientsList().addKeyListener(clientsKeyListener());
        this.view.getClientsList().addMouseListener(clientsMouseListener());
        
        //Upload
        this.view.getUploadDevButton().addActionListener(UploadDevListener());
        this.view.getUploadClientButton().addActionListener(UploadClientListener());
        
        //Circulation dans les fichiers
        this.view.getFolderDevButton().addActionListener(FolderDevListener());
        this.view.getFolderClientButton().addActionListener(FolderClientListener());
        
        this.view.getPrevDevButton().addActionListener(PrevDevListener());
        this.view.getPrevClientButton().addActionListener(PrevClientListener());
        this.view.getBackButton().addActionListener(backListener());
        
        //Obtention du commentaire
        this.view.getCommentButton().addActionListener(commentListener());
        
        initView();
    }
    
    private ActionListener backListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).getJMenuBar().remove(cloudMenu);
            app.getFrame(0).setJMenuBar(app.getFrame(0).getJMenuBar());
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private void initView()
    {
        Cloud cloud = (Cloud)getModel("Cloud");
        
        cloudMenu           = new JMenu("Cloud");
        JMenuItem refresh   = new JMenuItem("Rafraichir");
       
        refresh.addActionListener(refreshListener());
        
        cloudMenu.add(refresh);
        
        app.getFrame(0).getJMenuBar().add(cloudMenu);
        
        view.getCommentField().setVisible(false);
        view.getCommentButton().setVisible(false);
        
        cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
        cloud.addData("data[Token][fields]",    app.getUser().get("token"));
        
        path[0].append("/");
        path[1].append("/");
        
        listFiles(cloud, 0);
        listFiles(cloud, 1);
    }
    
    private ActionListener refreshListener()
    {
        return (ActionEvent e) -> {
            Cloud cloud = (Cloud)getModel("Cloud");
            
            cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
            cloud.addData("data[Token][fields]",    app.getUser().get("token"));
            
            listFiles(cloud, 0);
            listFiles(cloud, 1);
        };
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
                    
                    cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
                    cloud.addData("data[Token][fields]",    app.getUser().get("token"));
                    
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
                if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE
                && (app.getUser().get("role").equals("admin") || app.getUser().get("role").equals("leaddev")))
                {
                    Cloud cloud = (Cloud)getModel("Cloud");
                    
                    cloud.clearData();
                    
                    cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
                    cloud.addData("data[Token][fields]",    app.getUser().get("token"));
                    
                    deleteFile(cloud, 1);
                }
            }      
        };
    }
    
    private MouseListener devMouseListener()
    {
        return new MouseAdapter() {
            Cloud cloud = (Cloud)getModel("Cloud");
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                cloud.clearData();
                
                cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
                cloud.addData("data[Token][fields]",    app.getUser().get("token"));
                
                // Si double click gauche de la souris
                if(e.getButton() == 1)
                {
                    if(e.getClickCount() == 2)
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
                    else
                    {
                        view.getCommentField().setText(
                            view.getDevData().getValue(view.getDevList().getSelectedIndex()).get("comment")
                        );
                        
                        view.getCommentField().setVisible(true);
                        view.getCommentButton().setVisible(true);
                        
                        view.getCommentButton().setName("dev");
                    }
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
                
                cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
                cloud.addData("data[Token][fields]",    app.getUser().get("token"));
                               
                // Si double click gauche de la souris
                if(e.getButton() == 1)
                {
                    if(e.getClickCount() == 2)
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
                    else
                    {
                        view.getCommentField().setText(
                            view.getClientsData().getValue(view.getClientsList().getSelectedIndex()).get("comment")
                        );
                        
                        view.getCommentField().setVisible(true);
                        view.getCommentButton().setVisible(true);
                        
                        view.getCommentButton().setName("client");
                    }
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
    
    private ActionListener FolderDevListener()
    {
        return (ActionEvent e) -> {
            createFolder(0);
        };
    }
    
    private ActionListener FolderClientListener()
    {
        return (ActionEvent e) -> {
            createFolder(1);
        };
    }
    
    private ActionListener PrevDevListener()
    {
        return (ActionEvent e) -> {
            Cloud cloud         = (Cloud)getModel("Cloud");
            String[] explode    = path[0].substring(1).split("/");
            
            cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
            cloud.addData("data[Token][fields]",    app.getUser().get("token"));
            
            if(explode.length == 0 || explode.length == 1)
                path[0].replace(0, path[0].length(), "/");
            else
            {
                StringBuilder str = new StringBuilder();
                
                for(int i = 0; i < explode.length - 1; i++)
                {
                    str.append("/");
                    str.append(explode[i]);
                }
                str.append("/");
                
                path[0].replace(0, path[0].length(), str.toString());
            }
            listFiles(cloud, 0);
        };
    }
    
    private ActionListener PrevClientListener()
    {
        return (ActionEvent e) -> {
            Cloud cloud         = (Cloud)getModel("Cloud");
            String[] explode    = path[1].substring(1).split("/");
            
            cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
            cloud.addData("data[Token][fields]",    app.getUser().get("token"));
            
            if(explode.length == 0 || explode.length == 1)
                path[1].replace(0, path[1].length(), "/");
            else
            {
                StringBuilder str = new StringBuilder();
                
                for(int i = 0; i < explode.length - 1; i++)
                {
                    str.append("/");
                    str.append(explode[i]);
                }
                str.append("/");
                
                path[1].replace(0, path[1].length(), str.toString());
            }
            listFiles(cloud, 1);
        };
    }
    
    private ActionListener commentListener()
    {
        return (ActionEvent e) -> {
            Cloud cloud  = (Cloud)getModel("Cloud");
            HashMap<String, String> file = null;
            Map<String, String> errors = new HashMap<>();
            
            if(view.getCommentButton().getName().equals("dev"))
            {
                cloud.addData("data[Cloud][user]", "dev");
                cloud.addData("data[Cloud][file]", ECrypto.base64(path[0].toString() + view.getDevList().getSelectedValue()));
                
                file = view.getDevData().getValue(view.getDevList().getSelectedIndex());
            }
            else if(view.getCommentButton().getName().equals("client"))
            {
                cloud.addData("data[Cloud][user]", "client");
                cloud.addData("data[Cloud][file]", ECrypto.base64(path[1].toString() + view.getClientsList().getSelectedValue()));
                
                file = view.getClientsData().getValue(view.getClientsList().getSelectedIndex());
            }
            
            if(file != null)
            {
                cloud.addData("data[Cloud][project]",   ECrypto.base64(project.get("id")));
                cloud.addData("data[Cloud][comment]",   view.getCommentField().getText());
                cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
                cloud.addData("data[Token][fields]",    app.getUser().get("token"));
            
                String json = cloud.execute("COMMENT", errors, true);
                
                if(json.contains("comment"))
                    file.put("comment", view.getCommentField().getText());
            }
        };
    }
    
    private void listFiles(Cloud cloud, int index)
    {
        Map<String, String> errors  = new HashMap<>();
        String[] actions            = new String[]{"LIST_DEV", "LIST_CLIENT"};
         
        cloud.addData("data[Cloud][project]",   ECrypto.base64(project.get("id")));
        cloud.addData("data[Cloud][directory]", ECrypto.base64(path[index].toString()));

        if(cloud.validate(actions[index], cloud.getData(), errors))
        {
            String json = cloud.execute(actions[index], errors, true);
            Map<String, ArrayList<HashMap<String, String>>> values  = new Gson().fromJson(
                json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
            );

            if(values != null && values.get("content") != null)
            {
                ArrayList<HashMap<String, String>> files = values.get("content");

                directories.get(index).clear();

                if(index == 0)
                {
                    view.getDevRenderer().directories.clear();
                    view.getDevData().clear();
                    view.getDevData().setValues(files);
                }
                else
                {
                    view.getClientRenderer().directories.clear();
                    view.getClientsData().clear();
                    view.getClientsData().setValues(files);
                }

                for(Map<String, String> f : files)
                { 
                    if(index == 0)
                    {
                        view.getDevRenderer().directories.add(
                            (f.get("isDir").equals("true")) ? Boolean.TRUE : Boolean.FALSE
                        );
                        
                        view.getDevData().addElement(f.get("filename"));
                    }                        
                    else
                    {
                        view.getClientRenderer().directories.add(
                            (f.get("isDir").equals("true")) ? Boolean.TRUE : Boolean.FALSE
                        );
                        
                        view.getClientsData().addElement(f.get("filename"));
                    }
                    
                    directories.get(index).add(
                        (f.get("isDir").equals("true")) ? Boolean.TRUE : Boolean.FALSE
                    );
                }
            }
            else
                setError("Une erreur inattendue est survenue");
        }
    }
    
    private void deleteFile(Cloud cloud, int index)
    {
        Map<String, String> errors  = new HashMap<>();
        String[] users              = new String[]{"dev", "client"};
        
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
                String json                     = cloud.execute("DEL_FILE", errors, true);
                HashMap<String, String> values  = new Gson().fromJson(
                    json, new TypeToken<HashMap<String, String>>(){}.getType()
                );

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
                path[index].toString() + view.getDevList().getSelectedValue())
            );
        else
            cloud.addData("data[Cloud][path]", ECrypto.base64(
                path[index].toString() + view.getClientsList().getSelectedValue())
            );

        cloud.addData("data[Cloud][project]", ECrypto.base64(project.get("id"))); 
        cloud.addData("data[Cloud][user]", users[index]);

        String json                     = cloud.execute("DOWNLOAD", errors, true);
        HashMap<String, String> values  = new Gson().fromJson(
            json, new TypeToken<HashMap<String, String>>(){}.getType()
        );

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
                request.download("GET", System.getProperty("user.home") + File.separator + "Téléchargements");
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
            try
            {
                EHttpRequest request            = new EHttpRequest(new URL("http://enkwebservice.com/cloud/files/add"));
                HashMap<String, String> data    = new HashMap<>();

                data.put("data[Token][link]",       ECrypto.base64(app.getUser().get("email")));
                data.put("data[Token][fields]",     app.getUser().get("token"));
                data.put("data[Cloud][project]",    ECrypto.base64(project.get("id")));
                data.put("data[Cloud][directory]",  ECrypto.base64(path[index].toString()));
                data.put("data[Cloud][user]",       users[index]);

                String json = request.upload(data, filechooser.getSelectedFile());
                
                if(json.contains("upload"))
                {
                    HashMap<String, String> values = new Gson().fromJson(
                        json, new TypeToken<HashMap<String, String>>(){}.getType()
                    );

                    if(values != null && values.get("upload") != null)
                    {
                        values.remove("upload");
                        values.put("comment", "");
                        values.put("isDir", "false");
                        values.put("filename", filechooser.getSelectedFile().getName());
                        
                        if(index == 0)
                        {
                            view.getDevData().addElement(filechooser.getSelectedFile().getName());
                            view.getDevData().addValue(values);
                            view.getDevRenderer().directories.add(Boolean.FALSE);
                        }
                        else
                        {
                            view.getClientsData().addElement(filechooser.getSelectedFile().getName());
                            view.getClientsData().addValue(values);
                            view.getClientRenderer().directories.add(Boolean.FALSE);
                        }
                        directories.get(index).add(Boolean.FALSE);
                        app.message("Votre fichier a bien été uploadé");
                    }
                }
                else
                    setError("Ce fichier n'a pas pu être mis en ligne");
            }
            catch(MalformedURLException ex)
            {
                setError("Upload failed !");
            }    
        }
    }
    
    private void createFolder(int index)
    {
        Cloud cloud                 = (Cloud)getModel("Cloud");
        Map<String, String> errors  = new HashMap<>();
        String[] users              = new String[]{"dev", "client"};
        String dir;
        
        if((dir = app.input("Entrez un nom de dossier")) != null)
        {
            cloud.addData("data[Token][link]",      ECrypto.base64(app.getUser().get("email")));
            cloud.addData("data[Token][fields]",    app.getUser().get("token"));

            cloud.addData("data[Cloud][project]",   ECrypto.base64(project.get("id")));
            cloud.addData("data[Cloud][directory]", ECrypto.base64(path[index].toString()));
            cloud.addData("data[Cloud][name]",      dir);
            cloud.addData("data[Cloud][user]",      users[index]);

            if(cloud.validate("ADD_FOLDER", cloud.getData(), errors))
            {
                String json = cloud.execute("ADD_FOLDER", errors, true);
                
                if(json.contains("addFolder"))
                {
                    HashMap<String, String> values = new Gson().fromJson(
                        json, new TypeToken<HashMap<String, String>>(){}.getType()
                    );

                    if(values != null && values.get("addFolder") != null)
                    {
                        values.remove("addFolder");
                        values.put("comment", "");
                        values.put("isDir", "true");
                        values.put("filename", dir);
                        
                        if(index == 0)
                        {
                            view.getDevData().addElement(dir);
                            view.getDevData().addValue(values);
                            view.getDevRenderer().directories.add(Boolean.TRUE);
                        }
                        else
                        {
                            view.getClientsData().addElement(dir);
                            view.getClientsData().addValue(values);
                            view.getClientRenderer().directories.add(Boolean.TRUE);
                        }
                        directories.get(index).add(Boolean.TRUE);
                    }
                    else if(values != null && values.get("error") != null)
                        setError(values.get("error"));
                    else
                        setError("Une erreur inattendue est survenue");
                }
                else
                    setError("Ce dossier n'a pas pu être créé");
            }
        }
    }
    
    private void setError(String err) 
    {
        app.message(err, JOptionPane.ERROR_MESSAGE);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
