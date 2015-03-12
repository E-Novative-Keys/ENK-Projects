package net.enkeys.framework.events;

import java.awt.event.ItemEvent;

public interface EItemEvent
{
    public default void onItemStateChanged(ItemEvent ie){}
}
