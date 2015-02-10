package net.enkeys.framework.listeners;

import java.awt.event.FocusEvent;

public interface EFocusListener
{
    public default void onFocusGained(FocusEvent fe){}
    public default void onFocusLost(FocusEvent fe){}
}
