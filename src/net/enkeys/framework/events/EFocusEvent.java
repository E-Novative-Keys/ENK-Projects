package net.enkeys.framework.events;

import java.awt.event.FocusEvent;

/**
 * Inteface définissant les événements de focus.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EFocusEvent
{
    public default void onFocusGained(FocusEvent fe){}
    public default void onFocusLost(FocusEvent fe){}
}
