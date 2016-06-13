package net.enkeys.framework.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Inteface définissant les événements de souris.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EMouseEvent
{
    public default void onMouseEntered(MouseEvent me){}
    public default void onMouseExited(MouseEvent me){}
    public default void onMouseClicked(MouseEvent me){}
    public default void onMouseDoubleClicked(MouseEvent me){}
    public default void onMousePressed(MouseEvent me){}
    public default void onMouseReleased(MouseEvent me){}
    public default void onMouseMoved(MouseEvent me){}
    public default void onMouseDragged(MouseEvent me){}
    public default void onMouseWheel(MouseWheelEvent mwe){}
}
