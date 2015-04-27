package net.enkeys.framework.events;

import java.awt.event.ContainerEvent;

/**
 * Inteface définissant les événements de conteneur.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EContainerEvent
{
    public default void onComponentAdded(ContainerEvent ce){}
    public default void onComponentRemoved(ContainerEvent ce){}
}
