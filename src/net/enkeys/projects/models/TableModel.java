package net.enkeys.projects.models;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import net.enkeys.framework.exceptions.EDataException;

public abstract class TableModel extends AbstractTableModel
{
    protected final String[] keys;
    protected final String[] columns;
    protected final ArrayList<HashMap<String, String>> origin = new ArrayList<>();
    public final ArrayList<HashMap<String, String>> values = new ArrayList<>();
    
    public TableModel() throws EDataException
    {
        this.keys = setKeys();
        this.columns = setColumns();
        
        if(keys == null || columns == null || keys.length != columns.length)
            throw new EDataException("Incompatible set of Keys/Columns");
    }
    
    protected abstract String[] setKeys();
    protected abstract String[] setColumns();
    
    @Override
    public int getRowCount()
    {
        return this.values.size();
    }

    @Override
    public int getColumnCount()
    {
        return this.columns.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return this.columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws IllegalArgumentException
    {        
        if(columnIndex > -1 && columnIndex < this.keys.length)
            return this.values.get(rowIndex).get(this.keys[columnIndex]);
        else
            throw new IllegalArgumentException();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        this.values.get(rowIndex).put(this.keys[columnIndex], (String)value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    public boolean addOrigin(HashMap<String, String> client)
    {
        return this.origin.add(client);
    }
    
    public boolean addValue(HashMap<String, String> client)
    {
        boolean flag = this.values.add(client);
        
        fireTableRowsInserted(this.values.size()-1, this.values.size()-1);
        return flag;
    }
    
    public boolean removeValue(int index)
    {
        boolean flag = (this.values.remove(index) != null);
        
        fireTableRowsDeleted(index, index);
        return flag;
    }
    
    public void clear()
    {
        fireTableRowsDeleted(0, this.values.size()-1);
        this.values.clear();
    }
    
    public HashMap<String, String> getValue(int i)
    {
        return this.values.get(i);
    }
    
    public ArrayList<HashMap<String, String>> getValues()
    {
        return this.values;
    }
    
    public ArrayList<HashMap<String, String>> getOrigin()
    {
        return this.origin;
    }
}
