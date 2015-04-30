package net.enkeys.framework.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Conteneur graphique permettant de placer des composants dans un tableau.
 * @author E-Novative Keys
 * @version 1.0
 */
public class ETable extends JPanel
{
    //Les contraintes appliquées au positionnement des éléments dans le tableau
    private final GridBagConstraints constraints;
    
    /**
     * Crée une nouvelle instance de type ETable.
     * Instancie un layout en grille, des marges par défaut de 2px et un remplissage
     * horizontal.
     */
    public ETable()
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        
        constraints = new GridBagConstraints();
	constraints.insets = new Insets(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y)
    {
        add(component, constraints, x, y, 1, 1, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, Insets insets)
    {
        add(component, constraints, x, y, 1, 1, 1, GridBagConstraints.CENTER, insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width)
    {
        add(component, constraints, x, y, width, 1, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height)
    {
        add(component, constraints, x, y, width, height, 1, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight)
    {
        add(component, constraints, x, y, width, height, weight, GridBagConstraints.CENTER, constraints.insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, Insets insets)
    {
        add(component, constraints, x, y, width, height, weight, GridBagConstraints.CENTER, insets);
    }
    
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, int anchor)
    {
        add(component, constraints, x, y, width, height, weight, anchor, constraints.insets);
    }

    /**
     * Ajout d'un nouveau composant graphique au tableau.
     * {x} et {y} définissent la colonne et la ligne.
     * {width} et {height} et nombre de colonnes et de lignes utilisées.
     * {weight} le poids appliqué au composant par rapport aux autres.
     * {anchor} le type d'ancrage du composant dans sa cellule de tableau.
     * {insets} les marges spécifiques à ce composant.
     * @param component
     * @param constraints
     * @param x
     * @param y
     * @param width
     * @param height
     * @param weight
     * @param anchor
     * @param insets 
     */
    public void add(Component component, GridBagConstraints constraints, int x, int y, int width, int height, int weight, int anchor, Insets insets)
    {
        constraints.gridx       = x;
        constraints.gridy       = y;
        constraints.weightx     = weight;
        constraints.weighty     = 1;
        constraints.gridwidth   = width;
        constraints.gridheight  = height;
        constraints.anchor      = anchor;
        constraints.insets      = insets;

        add(component, constraints);
    }
    
    public void addEmpty(GridBagConstraints constraints, int x, int y)
    {
        addEmpty(constraints, x, y, 1);
    }
    
    /**
     * Ajout d'une nouvelle "cellule vide" au tableau.
     * Instanciation d'un JLabel vide.
     * @param constraints
     * @param x
     * @param y
     * @param weight 
     */
    public void addEmpty(GridBagConstraints constraints, int x, int y, int weight)
    {
        add(new JLabel(), constraints, x, y, 1, 1, weight);
    }
    
    /**
     * Renvoie les contraintes de positionnement appliquées sur ce tableau.
     * @return 
     */
    public GridBagConstraints getConstraints()
    {
        return constraints;
    }
}
