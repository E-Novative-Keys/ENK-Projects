package net.enkeys.projects.controllers;

import net.enkeys.projects.views.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EDataException;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.projects.models.Requester;

public class LoginController extends EController
{
    private final LoginView view = (LoginView)super.view;
    
    public LoginController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Requester());
        
        this.view.getAddButton().addActionListener(addButtonListener());
        this.view.getResetButton().addActionListener(resetButtonListener());
        this.view.getGetButton().addActionListener(getButtonListener());
        this.view.getPostButton().addActionListener(postButtonListener());
    }
    
    private ActionListener addButtonListener()
    {
        return (ActionEvent e) -> {
            Requester request = (Requester)getModel("Requester");
            String key = view.getKeyField().getText();
            String value = view.getValueField().getText();
            
            if(!key.isEmpty())
            {
                try
                {
                    request.addData(key, value);
                
                    if(!value.isEmpty())
                        view.getValuesArea().append(key + "=" + value + '\n');
                    else
                        view.getValuesArea().append(key + '\n');

                    view.getKeyField().setText("");
                    view.getValueField().setText("");
                    setError("");
                }
                catch(EDataException ex)
                {
                    setError(ex.getMessage());
                }
            }
            else
                setError("Key cannot be emtpy");
        };
    }
    
    private ActionListener resetButtonListener()
    {
        return (ActionEvent e) -> {
            Requester request = (Requester)getModel("Requester");
            
            request.clearData();
            view.getKeyField().setText("");
            view.getValueField().setText("");
            view.getValuesArea().setText("");
            view.getTerminal().setText("");
            setError("");
        };
    }
    
    private ActionListener getButtonListener()
    {
        return (ActionEvent e) -> {
            execute("GET");
        };
    }
    
    private ActionListener postButtonListener()
    {
        return (ActionEvent e) -> {
            execute("POST");
        };
    }
    
    private void setError(String err)
    {
        view.getError().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
    
    private void execute(String method)
    {
        Requester request = (Requester)getModel("Requester");
        
        try
        {
            String value = request.execute();
            view.getTerminal().setText(value);
            setError("");
        }
        catch (EDataException | EHttpRequestException ex)
        {
            setError(ex.getMessage());
        }
    }
}
