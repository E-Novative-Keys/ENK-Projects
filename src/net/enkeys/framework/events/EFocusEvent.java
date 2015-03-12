package net.enkeys.framework.events;

import java.awt.event.FocusEvent;

public interface EFocusEvent
{
    public default void onFocusGained(FocusEvent fe){}
    public default void onFocusLost(FocusEvent fe){}
}
