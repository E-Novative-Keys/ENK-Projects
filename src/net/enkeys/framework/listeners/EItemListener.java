package net.enkeys.framework.listeners;

import java.awt.event.ItemEvent;

public interface EItemListener
{
    public default void onItemStateChanged(ItemEvent ie){}
}
