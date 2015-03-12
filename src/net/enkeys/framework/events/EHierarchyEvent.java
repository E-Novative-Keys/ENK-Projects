package net.enkeys.framework.events;

import java.awt.event.HierarchyEvent;

public interface EHierarchyEvent
{
    public default void onAncestorMoved(HierarchyEvent he){}
    public default void onAncestorResized(HierarchyEvent he){}
    public default void onHierarchyChange(HierarchyEvent he){}
}
