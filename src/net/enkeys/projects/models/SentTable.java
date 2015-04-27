package net.enkeys.projects.models;

/**
 * Modèle de données SentTable.
 * @extends TableModel
 * @author E-Novative Keys
 * @version 1.0
 */
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
    
    /**
     * Edition de la table.
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
