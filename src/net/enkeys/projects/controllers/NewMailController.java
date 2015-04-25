package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Mailbox;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.NewMailView;
import net.enkeys.projects.views.ReceivedMailsView;

/**
 * Controller NewMailController
 * Gestion de la création d'un mail à envoyer
 * @extends EController
 * @author E-Novative Keys
 */
public class NewMailController extends EController
{
    private final ENKProjects app     = (ENKProjects)super.app;
    private final NewMailView view    = (NewMailView)super.view;
    private final HashMap<String, String> project;
    
    public NewMailController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new Mailbox());
        
        this.project = project;
        this.view.getSendButton().addActionListener(sendListener());
        this.view.getBackButton().addActionListener(backListener());
    }
    
    //Envoi de mail après validation par le modèle
    private ActionListener sendListener()
    {
        return (ActionEvent e) -> {
            Map<String, String> values = null;
            
            if(!this.view.getObject().getText().isEmpty() && !this.view.getMail().getText().isEmpty())
            {
                Mailbox mail = (Mailbox)getModel("Mailbox");
                Map<String, String> errors = new HashMap<>();
                
                mail.addData("data[Mail][object]", this.view.getObject().getText());
                mail.addData("data[Mail][content]", this.view.getMail().getText());
                mail.addData("data[Mail][project]", ECrypto.base64(this.project.get("id")));
                mail.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                mail.addData("data[Token][fields]", app.getUser().get("token"));
                
                    String json = mail.execute("SEND", errors, true);
                    values = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
                
                    if(values != null && values.get("email") != null)
                    {
                        app.message("Votre email a bien été envoyé", JOptionPane.INFORMATION_MESSAGE);
                        app.getFrame(0).setContent(new ReceivedMailsController(app, new ReceivedMailsView(), this.project));
                    }
                    else if(values != null && values.get("error") != null)
                        setError(values.get("error"));
                    else
                        System.err.println(json);
                
            }
            else
                setError("Tous les champs doivent etre remplis");
        };
    }
    
    private ActionListener backListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).getJMenuBar().remove(1);
            app.getFrame(0).setJMenuBar(app.getFrame(0).getJMenuBar());
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private void setError(String err) 
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
    
}
