package net.enkeys.framework.components;

import java.util.ArrayList;

public abstract class EController
{
    protected final EApplication app;
    protected final EView view;
    
    protected ArrayList<EModel> models;
    
    public EController(EApplication app, EView view)
    {
        this.app = app;
        this.view = view;
        this.models = new ArrayList<>();
    }
    
    /**
     * Ajoute le modèle donné à la liste des modèles du contrôleur.
     * @param model
     * @return 
     */
    protected final boolean addModel(EModel model)
    {
        return models.add(model);
    }
    
    /**
     * Supprime le modèle donné de la liste des modèles du contrôleur.
     * @param model
     * @return 
     */
    protected final boolean removeModel(EModel model)
    {
        return models.remove(model);
    }
    
    /**
     * Supprime le modèle situé à l'indice donné de la liste des modèles.
     * de la fenêtre.
     * @param i
     * @return 
     */
    protected final boolean removeModel(int i)
    {
        EModel model = models.get(i);
        
        if(models != null)
            return models.remove(model);
        else
            return false;
    }
    
    /**
     * Renvoie le modèle possédant le nom donné.
     * @param name
     * @return 
     */
    protected final EModel getModel(String name)
    {
        for(EModel model : models)
        {
            if(model.getName().equals(name))
                return model;
        }
        
        return null;
    }
    
    /**
     * Renvoie le modèle situé à l'indice donné.
     * @param i
     * @return 
     */
    protected final EModel getModel(int i)
    {
        return models.get(i);
    }
    
    /**
     * Affiche ou masque la vue contrôlée.
     * @param flag
     */
    public void render(boolean flag)
    {
        view.setVisible(flag);
    }
    
    /**
     * Renvoie la vue contrôlée.
     * @return 
     */
    public final EView getView()
    {
        return view;
    }
}
