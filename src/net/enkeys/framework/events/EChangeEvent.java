package net.enkeys.framework.events;

import javax.swing.event.ChangeEvent;

public interface EChangeEvent
{
    public default void onStateChanged(ChangeEvent ce){}
}
