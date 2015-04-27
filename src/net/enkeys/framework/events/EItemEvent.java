package net.enkeys.framework.events;

import java.awt.event.ItemEvent;

/**
 * Inteface définissant les événements d'état d'objet fils.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EItemEvent
{
    public default void onItemStateChanged(ItemEvent ie){}
}
