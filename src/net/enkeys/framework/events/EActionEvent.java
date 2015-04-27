package net.enkeys.framework.events;

import java.awt.event.ActionEvent;

/**
 * Inteface définissant les événements d'action.
 * @author E-Novative Keys
 * @version 1.0
 */
public interface EActionEvent
{
    public default void onActionPerformed(ActionEvent ae){}
}
