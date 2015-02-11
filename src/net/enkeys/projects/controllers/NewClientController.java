package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Client;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.NewClientView;

public class NewClientController extends EController
{
    private final ENKProjects app = (ENKProjects) super.app;
    private final NewClientView view = (NewClientView) super.view;
    
    public NewClientController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Client());
        
        this.view.getExit().addActionListener(exitButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
    }
    
    private ActionListener exitButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
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
            client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
            client.addData("data[Token][fields]", app.getToken());
        
            try
            {
                if(client.validate(client.getData(), errors))
                {
                    System.out.println(client.getJsonData());
                    Map<String, String> values = client.execute("INSERT");
                    System.out.println(values);
                }
                else
                {
                    System.out.println(errors.get(errors.keySet().toArray()[0].toString()));
                }
            }
            catch(ERuleException | EHttpRequestException ex)
            {
                System.out.println(ex.getMessage());
            }
        };
    }
}
