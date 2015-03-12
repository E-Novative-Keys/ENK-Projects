package net.enkeys.framework.events;

import java.awt.event.InputMethodEvent;

public interface EInputMethodEvent
{
    public default void onInputMethodChanged(InputMethodEvent ime){}
    public default void onCaretPositionChanged(InputMethodEvent ime){}
}
