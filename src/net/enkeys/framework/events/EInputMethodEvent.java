package net.enkeys.framework.events;

import java.awt.event.InputMethodEvent;

/**
 * Inteface définissant les événements de saisie.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EInputMethodEvent
{
    public default void onInputMethodChanged(InputMethodEvent ime){}
    public default void onCaretPositionChanged(InputMethodEvent ime){}
}
