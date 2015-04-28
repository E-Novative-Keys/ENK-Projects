package net.enkeys.projects.models;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.enkeys.framework.utils.EResources;

/**
 * Classe permettant le rendu d'icônes dans les listes de fichiers du Cloud.
 * @extends DefaultListCellRenderer
 * @author E-Novative Keys
 * @version 1.0
 */
public class FileRenderer extends DefaultListCellRenderer
{
    public ArrayList<Boolean> directories   = new ArrayList<>();
    private final int width                 = 25;
    private final int height                = 30;
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(index < directories.size())
        {
            if(directories.get(index))
                label.setIcon(EResources.loadImageIcon("icons/directory.png", width, height));
            else
            {
                String name = label.getText();
                int dot     = name.lastIndexOf(".");
                
                if(dot > 0 && (name = name.substring(dot + 1)) != null
                && EResources.resourceExists("icons/" + name + ".png"))
                    label.setIcon(EResources.loadImageIcon("icons/" + name + ".png", width, height));
                else
                    label.setIcon(EResources.loadImageIcon("icons/file.png", width, height));
            }
                
        }
        label.setHorizontalTextPosition(JLabel.RIGHT);
        
        return label;
    }    
}
