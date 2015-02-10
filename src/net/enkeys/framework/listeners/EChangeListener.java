package net.enkeys.framework.listeners;

import javax.swing.event.ChangeEvent;

public interface EChangeListener
{
    public default void onStateChanged(ChangeEvent ce){}
}
