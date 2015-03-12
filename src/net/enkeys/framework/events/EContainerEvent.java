package net.enkeys.framework.events;

import java.awt.event.ContainerEvent;

public interface EContainerEvent
{
    public default void onComponentAdded(ContainerEvent ce){}
    public default void onComponentRemoved(ContainerEvent ce){}
}
