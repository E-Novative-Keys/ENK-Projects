package net.enkeys.framework.components;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public abstract class EView extends JPanel
{
    public EView()
    {
        super(new BorderLayout());
    }
    
    public EView(LayoutManager layout)
    {
        super(layout);
    }
}
