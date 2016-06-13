package net.enkeys.projects.models;

/**
 * Modèle de données MailTable.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class MailTable extends TableModel
{         
    @Override
    protected String[] setKeys() {
        return new String[] {"from", "object", "created"};
    }

    @Override
    protected String[] setColumns() {
        return new String[] {"De", "Objet", "Date"};
    }
    
    /**
     * Edition de la table
     * @param rowIndex
     * @param columnIndex
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }   
}
