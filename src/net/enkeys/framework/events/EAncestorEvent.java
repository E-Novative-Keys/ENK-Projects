package net.enkeys.framework.events;

import javax.swing.event.AncestorEvent;

public interface EAncestorEvent
{
    public default void onAncestorAdded(AncestorEvent ae){}
    public default void onAncestorRemoved(AncestorEvent ae){}
    public default void onAncestorMoved(AncestorEvent ae){}
}
