package net.enkeys.framework.listeners;

import java.awt.event.ContainerEvent;

public interface EContainerListener
{
    public default void onComponentAdded(ContainerEvent ce){}
    public default void onComponentRemoved(ContainerEvent ce){}
}
