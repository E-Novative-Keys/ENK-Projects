package net.enkeys.projects.models;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;

/**
 * Modele FileModel
 * Validation des fichiers Cloud
 * @extends EModel
 * @author E-Novative Keys
 */
public class FileModel extends DefaultListModel<Object>
{
    private ArrayList<HashMap<String, String>> values;

    public ArrayList<HashMap<String, String>> getValues()
    {
        return values;
    }
    
    public boolean addValue(HashMap<String, String> value)
    {
        return values.add(value);
    }
    
    public HashMap<String, String> getValue(int index)
    {
        return values.get(index);
    }

    public void setValues(ArrayList<HashMap<String, String>> values)
    {
        this.values = values;
    }
}
