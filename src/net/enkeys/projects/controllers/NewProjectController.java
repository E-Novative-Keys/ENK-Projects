package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import net.enkeys.projects.models.Client;
import net.enkeys.projects.models.Project;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.NewProjectView;

public class NewProjectController extends EController 
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final NewProjectView view = (NewProjectView) super.view;
    private final int id;
    
    public NewProjectController(EApplication app, EView view) 
    {
        this(app, view, -1);
    }
    
    public NewProjectController(EApplication app, EView view, int id) 
    {
        super(app, view);
        addModel(new Project());
        addModel(new Client());
        addModel(new User());
        this.id = id;
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
        
        initView();
    }
    
    private void initView()
    {
        Client client = (Client)getModel("Client");
        User user = (User)getModel("User");
        Map<String, String> errors = new HashMap<>();
        
        client.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        client.addData("data[Token][fields]", app.getUser().get("token"));
        
        user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]", app.getUser().get("token"));
        
        //Remplissage ComboBox client
        if(client.validate("SELECT", client.getData(), errors))
        {
            String json = client.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("clients") != null)
            {
                ArrayList<HashMap<String, String>> clients = values.get("clients");
                
                for(HashMap<String, String> c : clients)
                {
                    view.getClient().addItem(c.get("firstname") + " " + c.get("lastname") + " [" + c.get("enterprise") + "]" );
                
                    if(this.id != -1 && Integer.parseInt(c.get("id")) == (this.id))
                    {
                        view.getClient().setSelectedIndex(view.getClient().getItemCount()-1);
                        view.getClient().setEnabled(false);
                    }
                }
            }
            else
                System.err.println(json);
        }
        else
            System.err.println(errors);
        
        //Remplissage ComboBox référent
        for(int i = 0; i < 2; i++) 
        {
            if(i==0)
                user.addData("data[User][role]", "leaddev");
            else
                user.addData("data[User][role]", "admin");
            
            if(user.validate("SELECT", user.getData(), errors))
            {
                String json = user.execute("SELECT", errors);
                Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());

                if(values != null && values.get("users") != null)
                {
                    ArrayList<HashMap<String, String>> users = values.get("users");

                    for(HashMap<String, String> u : users)
                    {
                        view.getLead().addItem(u.get("firstname") + " " + u.get("lastname"));
                    }
                }
                else
                    System.err.println(json);
            }
            else
                System.err.println(errors);
        }
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            if(this.id == -1 || app.confirm("Êtes-vous certain de vouloir annuler la création du projet pour ce nouveau client ?") == ENKProjects.YES)
                app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            Project project     = (Project) getModel("Project");
            DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> errors = new HashMap<>();
            
            String discount = new String(Float.parseFloat(view.getDiscount().getValue().toString()) + "");
            
            if(view.getDeadline().getDate() == null)
            {
                setError("Veuillez préciser une deadline");
                return;
            }
            if(view.getDeadline().getDate().getTime() <= new Date().getTime())
            {
                setError("Veuillez saisir une deadline ne précédant pas la date actuelle");
                return;
            }
                      
            project.addData("data[Project][client_name]", view.getClient().getSelectedItem());
            project.addData("data[Project][name]", view.getProjectName().getText());
            project.addData("data[Project][description]", view.getDescription().getText());
            project.addData("data[Project][lead_name]", view.getLead().getSelectedItem());
            project.addData("data[Project][estimation]", view.getEstimation().getText());
            project.addData("data[Project][budget]", view.getBudget().getText());
            project.addData("data[Project][discount]", discount);
            project.addData("data[Project][deadline]", df.format(view.getDeadline().getDate()));
     
            project.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            project.addData("data[Token][fields]", app.getUser().get("token"));
            
            try
            {
                if(project.validate("INSERT", project.getData(), errors))
                {
                    String json = project.execute("INSERT");
                    
                    if(json.contains("projects"))
                    {
                        app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                        
                        if(this.id == -1)
                            app.message("Le projet a été correctement créé");
                        else
                            app.message("Le client et son projet ont été correctement créés");
                    }
                    else if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                        
                        if((errors = values.get("error")) != null)
                            setError(errors.get(errors.keySet().toArray()[0].toString()));
                        else
                            setError("Une erreur inattendue est survenue");
                    }
                    else
                        setError(json);
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
