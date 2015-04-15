package net.enkeys.projects.models;

public class SentTable extends TableModel
{         
    @Override
    protected String[] setKeys() {
        return new String[] {"to", "object", "created"};
    }

    @Override
    protected String[] setColumns() {
        return new String[] {"Pour", "Objet", "Date"};
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }   
}
