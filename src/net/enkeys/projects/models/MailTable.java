package net.enkeys.projects.models;

/**
 * Modele MailTable
 * Table mail non éditable
 * @extends EModel
 * @author E-Novative Keys
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
