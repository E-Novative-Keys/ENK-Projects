package net.enkeys.framework.listeners;

import javax.swing.event.AncestorEvent;

public interface EAncestorListener
{
    public default void onAncestorAdded(AncestorEvent ae){}
    public default void onAncestorRemoved(AncestorEvent ae){}
    public default void onAncestorMoved(AncestorEvent ae){}
}
