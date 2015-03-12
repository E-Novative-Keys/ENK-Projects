package net.enkeys.framework.events;

import java.awt.event.ActionEvent;

public interface EActionEvent
{
    public default void onActionPerformed(ActionEvent ae){}
}
