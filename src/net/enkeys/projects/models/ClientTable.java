package net.enkeys.projects.models;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

public class ClientTable extends AbstractTableModel
{
    private final String[] keys = new String[] {"id", "lastname", "firstname", "phonenumber", "email", "enterprise", "siret", "address"};
    private final String[] columns = new String[] {"#", "Nom", "Prénom", "Téléphone", "Email", "Entreprise", "SIRET", "Adresse"};
    private final ArrayList<HashMap<String, String>> origin = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> clients = new ArrayList<>();
    
    @Override
    public int getRowCount()
    {
        return clients.size();
    }

    @Override
    public int getColumnCount()
    {
        return columns.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws IllegalArgumentException
    {        
        if(columnIndex > -1 && columnIndex < keys.length)
            return clients.get(rowIndex).get(keys[columnIndex]);
        else
            throw new IllegalArgumentException();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return (columnIndex > 0);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        clients.get(rowIndex).put(keys[columnIndex], (String)value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    public boolean addOrigin(HashMap<String, String> client)
    {
        return origin.add(client);
    }
    
    public boolean addClient(HashMap<String, String> client)
    {
        boolean flag = clients.add(client);
        
        fireTableRowsInserted(clients.size()-1, clients.size()-1);
        return flag;
    }
    
    public boolean removeClient(int index)
    {
        boolean flag = (clients.remove(index) != null);
        
        fireTableRowsDeleted(index, index);
        return flag;
    }
    
    public void clear()
    {
        fireTableRowsDeleted(0, clients.size()-1);
        clients.clear();
    }
    
    public HashMap<String, String> getClient(int i)
    {
        return clients.get(i);
    }
    
    public HashMap<String, String> getClientByID(int id)
    {
        for(HashMap<String, String> c : clients)
        {
            if(Integer.parseInt(c.get("id")) == id)
                return c;
        }
        return null;
    }
    
    public ArrayList<HashMap<String, String>> getClients()
    {
        return clients;
    }
    
    public ArrayList<HashMap<String, String>> getOrigin()
    {
        return origin;
    }
}
