package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.NewClientView;
import net.enkeys.projects.views.NewProjectView;

/**
 * Controller NewClientController
 * Gestion de la création d'un client
 * @extends EController
 * @author E-Novative Keys
 */
public class NewClientController extends EController
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final NewClientView view = (NewClientView) super.view;
    
    public NewClientController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Client());
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    //Sauvegarde du client entré en vue via le webservice
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            Client client = (Client) getModel("Client");
            Map<String, String> errors = new HashMap<>();
            
            client.addData("data[Client][firstname]", view.getFirstname().getText());
            client.addData("data[Client][lastname]", view.getLastname().getText());
            client.addData("data[Client][phonenumber]", view.getPhonenumber().getText());
            client.addData("data[Client][email]", view.getEmail().getText());
            client.addData("data[Client][enterprise]", view.getEnterprise().getText());
            client.addData("data[Client][address]", view.getAddress().getText());
            client.addData("data[Client][siret]", view.getSiret().getText());
            
            client.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            client.addData("data[Token][fields]", app.getUser().get("token"));
        
            try
            {
                if(client.validate("INSERT", client.getData(), errors))
                {
                    String json = client.execute("INSERT");
                    
                    if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                        
                        if((errors = values.get("error")) != null)
                            setError(errors.get(errors.keySet().toArray()[0].toString()));
                        else
                            setError("Une erreur inattendue est survenue");
                    }
                    else if(json.contains("clients"))
                    {
                        if(view.getNewProject().isSelected() && json.contains("newClient"))
                        {
                            String newClient = "{" + json.substring(json.indexOf("\"newClient\""));
                            Map<String, Map<String, String>> values = new Gson().fromJson(newClient, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                            app.getFrame(0).setContent(new NewProjectController(app, new NewProjectView(), Integer.parseInt(values.get("newClient").get("id"))));
                        }
                        else
                        {
                            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                            app.message("Le client a été correctement créé");
                        }
                    }
                    else
                    {
                        /*Map<String, String> values = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
                        setError(values.get(values.keySet().toArray()[0].toString()));*/
                        setError(json);
                    }
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
