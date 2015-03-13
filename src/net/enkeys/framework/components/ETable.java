package net.enkeys.framework.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ETable extends JPanel
{
    private final GridBagConstraints constraints;
    
    public ETable()
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        
        constraints = new GridBagConstraints();
	constraints.insets = new Insets(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y)
    {
        add(component, constraints, x, y, 1, 1, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, Insets insets)
    {
        add(component, constraints, x, y, 1, 1, 1, GridBagConstraints.CENTER, insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width)
    {
        add(component, constraints, x, y, width, 1, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height)
    {
        add(component, constraints, x, y, width, height, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight)
    {
        add(component, constraints, x, y, width, height, weight, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, Insets insets)
    {
        add(component, constraints, x, y, width, height, weight, GridBagConstraints.CENTER, insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, int anchor)
    {
        add(component, constraints, x, y, width, height, weight, anchor, constraints.insets);
    }

    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, int anchor, Insets insets)
    {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = weight;
        constraints.weighty = 1;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.anchor = anchor;
        constraints.insets = insets;

        add(component, constraints);
    }
    
    public void addEmpty(GridBagConstraints constraints, int x, int y)
    {
        addEmpty(constraints, x, y, 1);
    }
    
    public void addEmpty(GridBagConstraints constraints, int x, int y, int weight)
    {
        add(new JLabel(), constraints, x, y, 1, 1, weight);
    }
    
    public GridBagConstraints getConstraints()
    {
        return constraints;
    }
}
