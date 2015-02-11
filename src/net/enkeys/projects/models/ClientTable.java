package net.enkeys.projects.models;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ClientTable extends AbstractTableModel
{
    private final String[] columns = new String[] {"Nom", "Prénom", "Téléphone", "Email", "Entreprise", "SIRET", "Adresse"};
    private final ArrayList<Client> clients = new ArrayList<>();
    
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
        switch(columnIndex)
        {
            case 0:
                return clients.get(rowIndex).getData("data[Client][lastname]");
            case 1:
                return clients.get(rowIndex).getData("data[Client][firstname]");
            case 2:
                return clients.get(rowIndex).getData("data[Client][phonenumber]");
            case 3:
                return clients.get(rowIndex).getData("data[Client][email]");
            case 4:
                return clients.get(rowIndex).getData("data[Client][enterprise]");
            case 5:
                return clients.get(rowIndex).getData("data[Client][siret]");
            case 6:
                return clients.get(rowIndex).getData("data[Client][address]");
            default:
                throw new IllegalArgumentException();
        }
    }  
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    public boolean addClient(Client client)
    {
        boolean flag =  clients.add(client);
        
        fireTableCellUpdated(clients.size(), 0);
        return flag;
    }
    public boolean removeClient(Client client)
    {
        boolean flag =  clients.remove(client);
        
        fireTableCellUpdated(clients.size(), 1);
        return flag;
    }
}
