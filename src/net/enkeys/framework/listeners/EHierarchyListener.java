package net.enkeys.framework.listeners;

import java.awt.event.HierarchyEvent;

public interface EHierarchyListener
{
    public default void onAncestorMoved(HierarchyEvent he){}
    public default void onAncestorResized(HierarchyEvent he){}
    public default void onHierarchyChange(HierarchyEvent he){}
}
