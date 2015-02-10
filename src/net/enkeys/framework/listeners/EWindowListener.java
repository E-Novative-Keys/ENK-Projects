package net.enkeys.framework.listeners;

import java.awt.event.WindowEvent;

public interface EWindowListener
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
