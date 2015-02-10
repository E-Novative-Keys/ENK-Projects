package net.enkeys.framework.listeners;

import java.awt.event.InputMethodEvent;

public interface EInputMethodListener
{
    public default void onInputMethodChanged(InputMethodEvent ime){}
    public default void onCaretPositionChanged(InputMethodEvent ime){}
}
