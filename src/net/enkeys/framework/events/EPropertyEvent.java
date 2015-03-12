package net.enkeys.framework.events;

import java.beans.PropertyChangeEvent;

public interface EPropertyEvent
{
    public default void onPropertyChange(PropertyChangeEvent pce){}
}
