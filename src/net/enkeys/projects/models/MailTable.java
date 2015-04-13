package net.enkeys.projects.models;

import net.enkeys.framework.exceptions.EDataException;

public class MailTable extends TableModel
{      
    private final boolean received;
    
    public MailTable(boolean received) throws EDataException
    {
        super();
        
        this.received = received;
    }
    
    @Override
    protected String[] setKeys() {
        return new String[] {received ? "from" : "to", "object", "created"};
    }

    @Override
    protected String[] setColumns() {
        return new String[] {received ? "De" : "Pour", "Objet", "Date"};
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }   
}
