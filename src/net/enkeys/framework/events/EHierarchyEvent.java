package net.enkeys.framework.events;

import java.awt.event.HierarchyEvent;

/**
 * Inteface définissant les événements de hiérarchie des composants.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EHierarchyEvent
{
    public default void onAncestorMoved(HierarchyEvent he){}
    public default void onAncestorResized(HierarchyEvent he){}
    public default void onHierarchyChange(HierarchyEvent he){}
}
