package net.enkeys.framework.events;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

public interface EDropTargetEvent
{
    public default void onDragEnter(DropTargetDragEvent dtde){}
    public default void onDragOver(DropTargetDragEvent dtde){}
    public default void onDropActionChanged(DropTargetDragEvent dtde){}
    public default void onDragExit(DropTargetEvent dte){}
    public default void onDrop(DropTargetDropEvent dtde){}
}
