package net.enkeys.framework.events;

import javax.swing.event.AncestorEvent;

/**
 * Inteface définissant les événements de composants parents.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EAncestorEvent
{
    public default void onAncestorAdded(AncestorEvent ae){}
    public default void onAncestorRemoved(AncestorEvent ae){}
    public default void onAncestorMoved(AncestorEvent ae){}
}
