package net.enkeys.projects.controllers;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
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
    
    private final ArrayList<Integer> updated;
    
    public ListClientsController(EApplication app, EView view)
    {
        super(app, view);
        this.updated = new ArrayList<>();
        addModel(new Client());
        
        this.view.getDataTable().addTableModelListener(dataTableListener());
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDeleteButton().addActionListener(deleteButtonListener());
        this.view.getSaveButton().addActionListener(saveButtonListener());
        this.view.getSearchField().addKeyListener(searchFieldListener());
        
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
                {
                    view.getDataTable().addClient(c);
                    view.getDataTable().addOrigin(c);
                }
                view.getListClients().setAutoCreateRowSorter(true);
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
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(e.getFirstRow(), 0));
                    
                    if(!updated.contains(id))
                        updated.add(id);
                    break;
                    
                case TableModelEvent.DELETE:
                    break;
            }
        };
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            if(updated.size() > 0)
            {
                switch(app.confirm("Souhaitez-vous appliquer les modifications effectuées ?", ENKProjects.YES_NO_CANCEL))
                {
                    case ENKProjects.YES:
                        if(saveUpdatedClients())
                            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                        break;
                        
                    case ENKProjects.NO:
                        app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                        break;
                        
                    case ENKProjects.CANCEL:
                        break;
                }
            }
            else
                app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    private ActionListener deleteButtonListener()
    {
        return (ActionEvent e) -> {
            Client client = (Client)getModel("Client");
            int[] rows = view.getListClients().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer les clients sélectionnés ?") == ENKProjects.YES)
            {                
                client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
                client.addData("data[Token][fields]", app.getToken());
                        
                for(int i = 0 ; i < rows.length ; i++)
                {
                    int modelID = view.getListClients().convertRowIndexToModel(rows[i]-i);
                    int id = Integer.parseInt((String)view.getDataTable().getValueAt(modelID, 0));
                    
                    client.addData("data[Client][id]", id);
                
                    try
                    {
                        if(client.validate("DELETE"))
                        {
                            String json = client.execute("DELETE");

                            if(json.contains("clients"))
                                view.getDataTable().removeClient(modelID);
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
            if(updated.size() > 0 &&app.confirm("Appliquer toutes les modifications ?") == ENKProjects.YES)
                saveUpdatedClients();
        };
    }
    
    private boolean saveUpdatedClients()
    {
        Client client = (Client)getModel("Client");

        client.addData("data[Token][link]", ECrypto.base64(app.getUser()));
        client.addData("data[Token][fields]", app.getToken());
                
        for(int i : updated)
        {
            Map<String, String> c = view.getDataTable().getClientByID(i);
            Map<String, String> errors = new HashMap<>();

            if(c != null)
            {
                client.addData("data[Client][id]", c.get("id"));
                client.addData("data[Client][firstname]", c.get("firstname"));
                client.addData("data[Client][lastname]", c.get("lastname"));
                client.addData("data[Client][phonenumber]", c.get("phonenumber"));
                client.addData("data[Client][email]", c.get("email"));
                client.addData("data[Client][enterprise]", c.get("enterprise"));
                client.addData("data[Client][siret]", c.get("siret"));
                client.addData("data[Client][address]", c.get("address"));

                try
                {
                    if(client.validate("UPDATE", client.getData(), errors))
                    {
                        String json = client.execute("UPDATE", errors);

                        if(json != null && !json.isEmpty())
                        {
                            if(json.contains("clients"))
                                return true;
                            else if(json.contains("error"))
                            {
                                Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                                if((errors = values.get("error")) != null)
                                    setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                                else
                                    setError("Une erreur inattendue est survenue");
                                break;
                            }
                            else
                            {
                                setError("#" + i + " : " + json);
                                break;
                            }
                        }
                        else
                        {
                            setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                            break;
                        }
                    }
                    else
                    {
                        setError("#" + i + " : " + errors.get(errors.keySet().toArray()[0].toString()));
                        break;
                    }
                }
                catch(ERuleException | EHttpRequestException ex)
                {
                    app.getLogger().warning(ex.getMessage());
                }
            }
        }
        
        return false;
    }
    
    private KeyListener searchFieldListener()
    {
        return new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                try
                {
                    String search = view.getSearchField().getText().toLowerCase();

                    if(view.getListClients().getAutoCreateRowSorter())
                    {
                        view.getListClients().setAutoCreateRowSorter(false);
                        view.getListClients().setRowSorter(null);
                    }
                    if(view.getDataTable().getClients().size() > 0)
                        view.getDataTable().clear();

                    for(HashMap<String, String> c : view.getDataTable().getOrigin())
                    {
                        if(c.get("id").toLowerCase().contains(search)
                        || c.get("firstname").toLowerCase().contains(search)
                        || c.get("lastname").toLowerCase().contains(search)
                        || c.get("phonenumber").toLowerCase().contains(search)
                        || c.get("email").toLowerCase().contains(search)
                        || c.get("enterprise").toLowerCase().contains(search)
                        || c.get("siret").toLowerCase().contains(search)
                        || c.get("address").toLowerCase().contains(search))
                            view.getDataTable().addClient(c);
                    }

                    if(view.getDataTable().getClients().size() > 0)
                        view.getListClients().setAutoCreateRowSorter(true);
                }
                catch(IndexOutOfBoundsException ex)
                {
                    app.getLogger().warning(ex.getMessage());
                }
            }
        };
    }
    
    private void setError(String err)
    {
        app.message(err, JOptionPane.ERROR_MESSAGE);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
