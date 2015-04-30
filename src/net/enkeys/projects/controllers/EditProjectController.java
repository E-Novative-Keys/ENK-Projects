package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.ListProjectsView;
import net.enkeys.projects.views.NewProjectView;

/**
 * Contrôlleur EditProjectController.
 * Gestion de l'édition d'un projet.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
class EditProjectController extends EController
{
    private final ENKProjects app       = (ENKProjects) super.app;
    private final NewProjectView view   = (NewProjectView) super.view;
    private final HashMap<String, String> data;
    private final boolean flag; //False si atteint depuis la liste des projets, true si depuis le Manager du projet
    
    public EditProjectController(EApplication app, EView view, HashMap<String, String> data)
    {
        this(app, view, data, false);
    }
    
    public EditProjectController(EApplication app, EView view, HashMap<String, String> data, boolean flag) 
    {
        super(app, view);
        addModel(new Project());
        addModel(new User());
        addModel(new Client());
        
        this.data = data;
        this.flag = flag;
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
        
        initView();
    }

    private void initView() {
        Client client   = (Client)getModel("Client");
        User user       = (User)getModel("User");
        Map<String, String> errors = new HashMap<>();
        
        client.addData("data[Token][link]",     ECrypto.base64(app.getUser().get("email")));
        client.addData("data[Token][fields]",   app.getUser().get("token"));
        
        user.addData("data[Token][link]",       ECrypto.base64(app.getUser().get("email")));
        user.addData("data[Token][fields]",     app.getUser().get("token"));
        
        //Remplissage ComboBox client
        if(client.validate("SELECT", client.getData(), errors))
        {
            String json = client.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(
                json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
            );
            
            if(values != null && values.get("clients") != null)
            {
                ArrayList<HashMap<String, String>> clients = values.get("clients");
                
                for(HashMap<String, String> c : clients)
                {
                    view.getClient().addItem(c.get("firstname") + " " + c.get("lastname") + " [" + c.get("enterprise") + "]" );
                
                    //Si le référent du projet est cet utilisateur
                    if(c.get("id").equals(this.data.get("client_id")))
                    {
                        view.getClient().setSelectedIndex(view.getClient().getItemCount()-1);
                        view.getClient().setEnabled(false);
                    }
                }
            }
            else
                setError("Une erreur inattendue est survenue");
        }
        else
            setError("Une erreur inattendue est survenue");
        
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
                Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(
                    json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
                );

                if(values != null && values.get("users") != null)
                {
                    ArrayList<HashMap<String, String>> users = values.get("users");

                    for(HashMap<String, String> u : users)
                    {
                        view.getLead().addItem(u.get("firstname") + " " + u.get("lastname"));
                        
                        //Si le référent du projet est cet utilisateur
                        if(u.get("id").equals(this.data.get("lead_id")))
                        {
                            view.getLead().setSelectedIndex(view.getLead().getItemCount()-1);
                        }
                            
                    }
                }
                else
                    setError("Une erreur inattendue est survenue");
            }
            else
                setError("Une erreur inattendue est survenue");
        }
        
        //Remplissage informations récupérées
        view.getProjectName().setText(this.data.get("name"));
        view.getDescription().setText(this.data.get("description"));
        view.getBudget().setText(this.data.get("budget"));
        
        try
        {
            view.getDeadline().setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(this.data.get("deadline")));
        }
        catch (ParseException ex)
        {
            app.getLogger().warning(ex.getMessage());
        }
        
        view.getEstimation().setText(this.data.get("estimation"));
        view.getDiscount().setValue(Double.parseDouble(this.data.get("discount")));
    }

    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            if(this.flag)
                app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.data));
            else
                app.getFrame(0).setContent(new ListProjectsController(app, new ListProjectsView()));                
        };
    }

    //Sauvegarde des données entrées en vue via le webservice
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            Project project                 = (Project) getModel("Project");
            DateFormat df                   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> errors      = new HashMap<>();
            
            String discount                 = new String(Float.parseFloat(view.getDiscount().getValue().toString()) + "");
                      
            project.addData("data[Project][id]",            this.data.get("id"));
            project.addData("data[Project][client_name]",   view.getClient().getSelectedItem());
            project.addData("data[Project][name]",          view.getProjectName().getText());
            project.addData("data[Project][description]",   view.getDescription().getText());
            project.addData("data[Project][lead_name]",     view.getLead().getSelectedItem());
            project.addData("data[Project][deadline]",      df.format(view.getDeadline().getDate()));
            project.addData("data[Project][estimation]",    view.getEstimation().getText());
            project.addData("data[Project][budget]",        view.getBudget().getText());
            project.addData("data[Project][discount]",      discount);
            
            project.addData("data[Token][link]",            ECrypto.base64(app.getUser().get("email")));
            project.addData("data[Token][fields]",          app.getUser().get("token"));
            
            try
            {
                if(project.validate("UPDATE", project.getData(), errors))
                {
                    String json = project.execute("UPDATE");
                    
                    if(json.contains("projects"))
                    {
                        if(this.flag)
                        {
                            //On récupère et on réenregistre les nouvelles données du projet
                            HashMap<String, ArrayList<HashMap<String, String>>> dataProjects = new Gson().fromJson(
                                json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType()
                            );
                            
                            if(dataProjects != null && dataProjects.get("projects") != null)
                            {
                                ArrayList<HashMap<String, String>> projects = dataProjects.get("projects");
                                
                                for(HashMap<String, String> p : projects)
                                {
                                    if(p.get("id").equals(this.data.get("id")))
                                    {
                                        for(Entry<String, String> entry : p.entrySet())
                                        {
                                            String key = entry.getKey();
                                            this.data.put(key, p.get(key));
                                        }
                                        break;
                                    }
                                }
                                
                                app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.data));
                            }
                            else
                                setError("Les nouvelles données n'ont pas été correctement réceptionnées");
                        }
                        else
                        {
                            app.getFrame(0).setContent(new ListProjectsController(app, new ListProjectsView())); 
                            app.message("Le projet a été correctement mis à jour");
                        }
                    }
                    else if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(
                            json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType()
                        );
                        
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
