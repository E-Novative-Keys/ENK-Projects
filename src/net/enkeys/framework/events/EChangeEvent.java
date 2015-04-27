package net.enkeys.framework.events;

import javax.swing.event.ChangeEvent;

/**
 * Inteface définissant les événements de changement d'état.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EChangeEvent
{
    public default void onStateChanged(ChangeEvent ce){}
}
