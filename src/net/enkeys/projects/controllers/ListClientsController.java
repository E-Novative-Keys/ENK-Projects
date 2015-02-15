package net.enkeys.projects.controllers;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getSaveButton().addActionListener(saveButtonListener());
        
        initView();
    }
    
    private void initView()
    {
        Client client = (Client)getModel("Client");
        Map<String, String> errors = new HashMap<>();
        
        client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
        client.addData("data[Token][fields]", app.getToken());
        
        if(client.validate("SELECT", client.getData(), errors))
        {
            String json = client.execute("SELECT", errors);
            Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());
            
            if(values != null && values.get("clients") != null)
            {
                ArrayList<HashMap<String, String>> clients = values.get("clients");
                for(HashMap<String, String> c : clients)
                    view.getDataTable().addClient(c);
                
                //Non triable car ne met pas à jour l'ordre des lignes et bug les id lors de la suppression
                //view.getListClients().setAutoCreateRowSorter(true);
            }
        }
        else
            System.err.println(errors);       
    }
    
    private TableModelListener dataTableListener()
    {
        return (TableModelEvent e) -> {            
            switch(e.getType())
            {
                case TableModelEvent.INSERT:
                    break;
                    
                case TableModelEvent.UPDATE:
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    String columnName = view.getDataTable().getColumnName(column);
                    String data = (String)view.getDataTable().getValueAt(row, column);
                    System.out.println(columnName + ": row " + row + ", col " + column + ", value = " + data);
                    break;
                    
                case TableModelEvent.DELETE:
                    break;
            }
        };
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private ActionListener deleteButtonListener()
    {
        return (ActionEvent e) -> {
            Client client = (Client)getModel("Client");
            int[] rows = view.getListClients().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les clients sélectionnés ?") == ENKProjects.CONFIRM_YES)
            {
                client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
                client.addData("data[Token][fields]", app.getToken());
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    client.addData("data[Client][id]", view.getDataTable().getClient(rows[i]-i).get("id"));
                
                    try
                    {
                        if(client.validate("DELETE"))
                        {
                            String json = client.execute("DELETE");

                            if(json.contains("clients"))
                                view.getDataTable().removeClient(rows[i]-i);
                            else
                                app.getLogger().warning("Error: " + json);
                        }
                    }
                    catch(ERuleException | EHttpRequestException ex)
                    {
                        app.getLogger().warning(ex.getMessage());
                    }
                }
            }
        };
    }
    
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            Client client = (Client)getModel("Client");
            
            client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
            client.addData("data[Token][fields]", app.getToken());
            
            if(app.confirm("Appliquer toutes les modifications ?") == ENKProjects.CONFIRM_YES)
            {
                for(Map<String, String> c : view.getDataTable().getClients())
                {
                    client.addData("data[Client][id]", c.get("id"));
                    client.addData("data[Client][firstname]", c.get("firstname"));
                    client.addData("data[Client][lastname]", c.get("lastname"));
                    client.addData("data[Client][phonenumber]", c.get("phonenumber"));
                    client.addData("data[Client][email]", c.get("email"));
                    client.addData("data[Client][enterprise]", c.get("enterprise"));
                    client.addData("data[Client][siret]", c.get("siret"));
                    client.addData("data[Client][address]", c.get("address"));
                    System.out.println(client.getData());

                    try
                    {
                        if(client.validate("UPDATE"))
                        {
                            String json = client.execute("UPDATE");

                            if(!json.contains("clients"))
                                app.getLogger().warning("Error: " + json);
                        }
                    }
                    catch(ERuleException | EHttpRequestException ex)
                    {
                        app.getLogger().warning(ex.getMessage());
                    }
                }
            }
        };
    }
}
