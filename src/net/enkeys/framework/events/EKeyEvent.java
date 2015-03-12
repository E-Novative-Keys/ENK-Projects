package net.enkeys.framework.events;

import java.awt.event.KeyEvent;

public interface EKeyEvent
{
    public default void onKeyPressed(KeyEvent ke){}
    public default void onKeyReleased(KeyEvent ke){}
    public default void onKeyTyped(KeyEvent ke){}
}
