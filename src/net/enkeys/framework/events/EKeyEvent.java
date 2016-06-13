package net.enkeys.framework.events;

import java.awt.event.KeyEvent;

/**
 * Inteface définissant les événements clavier.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EKeyEvent
{
    public default void onKeyPressed(KeyEvent ke){}
    public default void onKeyReleased(KeyEvent ke){}
    public default void onKeyTyped(KeyEvent ke){}
}
