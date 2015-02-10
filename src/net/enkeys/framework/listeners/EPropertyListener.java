package net.enkeys.framework.listeners;

import java.beans.PropertyChangeEvent;

public interface EPropertyListener
{
    public default void onPropertyChange(PropertyChangeEvent pce){}
}
