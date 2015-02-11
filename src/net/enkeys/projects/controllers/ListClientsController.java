package net.enkeys.projects.controllers;


import java.util.HashMap;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Client;
import net.enkeys.projects.views.ListClientsView;

public class ListClientsController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final ListClientsView view = (ListClientsView)super.view;
    
    public ListClientsController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new Client());
        
        this.view.getDataTable().addTableModelListener(dataTableListener());
        
        initView();
    }
    
    private void initView()
    {
        Client client = (Client)getModel("Client");
        Map<String, String> errors = new HashMap<>();
        Map<String, String> values;
        
        client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
        client.addData("data[Token][fields]", app.getToken());
        
        if(client.validate("SELECT", client.getData(), errors))
        {
            values = client.execute("SELECT", errors);
            System.out.println(values);
            System.err.println(errors);
        }
        else
            System.err.println(errors);       
    }
    
    private TableModelListener dataTableListener()
    {
        return (TableModelEvent e) -> {
            System.out.println(e.getType());
        };
    }
}
