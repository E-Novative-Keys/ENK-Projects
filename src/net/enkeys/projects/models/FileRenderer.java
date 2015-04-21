package net.enkeys.projects.models;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.enkeys.framework.utils.EResources;

public class FileRenderer extends DefaultListCellRenderer
{
    public ArrayList<Boolean> directories = new ArrayList<>();
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(index < directories.size())
        {
            if(directories.get(index))
                label.setIcon(EResources.loadImageIcon("icons/directory.png", 25, 30));
            else
            {
                String name = label.getText();
                int dot = name.lastIndexOf(".");
                if(dot > 0 && (name = name.substring(dot + 1)) != null
                && EResources.resourceExists("icons/" + name + ".png"))
                    label.setIcon(EResources.loadImageIcon("icons/" + name + ".png", 25, 30));
                else
                    label.setIcon(EResources.loadImageIcon("icons/file.png", 25, 30));
            }
                
        }
        label.setHorizontalTextPosition(JLabel.RIGHT);
        
        return label;
    }    
}
