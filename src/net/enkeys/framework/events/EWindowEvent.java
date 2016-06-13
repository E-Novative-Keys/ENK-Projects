package net.enkeys.framework.events;

import java.awt.event.WindowEvent;

/**
 * Inteface définissant les événements de fenêtre.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EWindowEvent
{
    public default void onWindowOpened(WindowEvent we){}
    public default void onWindowClosing(WindowEvent we){}
    public default void onWindowClosed(WindowEvent we){}
    public default void onWindowActivated(WindowEvent we){}
    public default void onWindowDeactivated(WindowEvent we){}
    public default void onWindowIconified(WindowEvent we){}
    public default void onWindowDeiconified(WindowEvent we){}
    public default void onWindowLostFocus(WindowEvent we){}
    public default void onWindowGainedFocus(WindowEvent we){}
    public default void onWindowStateChanged(WindowEvent we){}
}
