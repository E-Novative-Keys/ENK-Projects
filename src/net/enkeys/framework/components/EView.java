package net.enkeys.framework.components;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * Classe abstraite mère de toutes les Vues de l'application.
 * Redéfinition personnalisée du JPanel.
 * @author E-Novative Keys
 * @version 1.0
 */
public abstract class EView extends JPanel
{
    public EView()
    {
        //Layout par défaut : BorderLayout.
        super(new BorderLayout());
    }
    
    /**
     * Crée une nouvelle instance de type EView.
     * @param layout 
     */
    public EView(LayoutManager layout)
    {
        super(layout);
    }
}
