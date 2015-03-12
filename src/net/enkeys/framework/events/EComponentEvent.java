package net.enkeys.framework.events;

import java.awt.event.ComponentEvent;

public interface EComponentEvent
{
    public default void onComponentResized(ComponentEvent ce){}
    public default void onComponentMoved(ComponentEvent ce){}
    public default void onComponentShown(ComponentEvent ce){}
    public default void onComponentHidden(ComponentEvent ce){}
}
