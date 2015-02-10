package net.enkeys.framework.listeners;

import java.awt.event.KeyEvent;

public interface EKeyListener
{
    public default void onKeyPressed(KeyEvent ke){}
    public default void onKeyReleased(KeyEvent ke){}
    public default void onKeyTyped(KeyEvent ke){}
}
