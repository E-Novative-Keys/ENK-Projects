package net.enkeys.projects.models;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import net.enkeys.framework.exceptions.EDataException;

/**
 * Modèle de données pour composants JTable.
 * Modèle pour les table de données (user, client ...).
 * @extends AbstractTableModel
 * @author E-Novative Keys
 * @version 1.0
 */
public abstract class TableModel extends AbstractTableModel
{
    protected final String[] keys;
    protected final String[] columns;
    protected final ArrayList<HashMap<String, String>> origin   = new ArrayList<>();
    public final ArrayList<HashMap<String, String>> values      = new ArrayList<>();
    
    /**
     * Crée une nouvelle instance de type TableModel.
     * Initialisation des clés et des colonnes.
     * @throws EDataException 
     */
    public TableModel() throws EDataException
    {
        this.keys       = setKeys();
        this.columns    = setColumns();
        
        if(keys == null || columns == null || keys.length != columns.length)
            throw new EDataException("Incompatible set of Keys/Columns");
    }
    
    /**
     * Méthode abstraite de définition des clés de données.
     * @return 
     */
    protected abstract String[] setKeys();
    
    /**
     * Méthode abstraite de définition des nom de données.
     * @return 
     */
    protected abstract String[] setColumns();
    
    /**
     * Renvoie le nombre de ligne de la table.
     * @return 
     */
    @Override
    public int getRowCount()
    {
        return this.values.size();
    }

    /**
     * Renvoie le nombre de colonnes de la table.
     * @return 
     */
    @Override
    public int getColumnCount()
    {
        return this.columns.length;
    }

    /**
     * Renvoie le nom de la colonne à l'indice donné.
     * @param column
     * @return 
     */
    @Override
    public String getColumnName(int column)
    {
        return this.columns[column];
    }

    /**
     * Renvoie la valeur situé aux indices donnés.
     * @param rowIndex
     * @param columnIndex
     * @return
     * @throws IllegalArgumentException 
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws IllegalArgumentException
    {        
        if(columnIndex > -1 && columnIndex < this.keys.length)
            return this.values.get(rowIndex).get(this.keys[columnIndex]);
        else
            throw new IllegalArgumentException();
    }
    
    /**
     * Renvoie la classe de la donnée située dans la colonne donnée.
     * @param columnIndex
     * @return 
     */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    /**
     * Définit la valeur aux indices donnés.
     * @param value
     * @param rowIndex
     * @param columnIndex 
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        this.values.get(rowIndex).put(this.keys[columnIndex], (String)value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    /**
     * Ajout d'une donnée dans la liste d'origine.
     * @param value
     * @return 
     */
    public boolean addOrigin(HashMap<String, String> value)
    {
        return this.origin.add(value);
    }
    
    /**
     * Ajout d'une donnée dans la liste de valeurs affichées.
     * @param value
     * @return 
     */
    public boolean addValue(HashMap<String, String> value)
    {
        boolean flag = this.values.add(value);
        
        fireTableRowsInserted(this.values.size()-1, this.values.size()-1);
        return flag;
    }
    
    /**
     * Suppression d'une donnée dans la liste de valeurs.
     * @param index
     * @return 
     */
    public boolean removeValue(int index)
    {
        boolean flag = (this.values.remove(index) != null);
        
        fireTableRowsDeleted(index, index);
        return flag;
    }
    
    /**
     * Suppression d'une donnée dans la liste d'origine.
     * @param o
     * @return 
     */
    public boolean removeOrigin(HashMap<String, String> o)
    {
        return this.origin.remove(o);
    }
    
    /**
     * Suppression de toutes les données affichées.
     */
    public void clear()
    {
        fireTableRowsDeleted(0, this.values.size()-1);
        this.values.clear();
    }
    
    /**
     * Renvoie la valeur située à l'indice donné.
     * @param i
     * @return 
     */
    public HashMap<String, String> getValue(int i)
    {
        return this.values.get(i);
    }
    
    /**
     * Renvoie toutes les valeurs affichées.
     * @return 
     */
    public ArrayList<HashMap<String, String>> getValues()
    {
        return this.values;
    }
    
    /**
     * Renvoie toutes les valeurs d'origine.
     * @return 
     */
    public ArrayList<HashMap<String, String>> getOrigin()
    {
        return this.origin;
    }
}
