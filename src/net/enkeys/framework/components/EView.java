package net.enkeys.framework.components;

import java.awt.GridBagConstraints;

public abstract class EView extends ETable
{
    public EView()
    {
        super();
        getConstraints().fill = GridBagConstraints.BOTH;
    }
}
