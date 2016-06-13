package net.enkeys.framework.events;

import java.beans.PropertyChangeEvent;

/**
 * Inteface définissant les événements de propriété.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EPropertyEvent
{
    public default void onPropertyChange(PropertyChangeEvent pce){}
}
