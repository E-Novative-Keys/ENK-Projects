package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

/**
 * Modele Project
 * Validation des données Project envoyées
 * @extends EModel
 * @author E-Novative Keys
 */
public class Project extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
        rules.put("!CURRENT", null);
        rules.put("!AFFECT", null);
        rules.put("!DISAFFECT", null);
        rules.put("!QUOTATION", null);
        
        rules.put("name", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom de projet"),
            new BetweenRule(3, 30, "Nom incorrect (3 à 30 caractères)"),
            new RegexRule("([\\.a-zA-Z0-_9&àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de projet non valide") 
        });
        
        rules.put("estimation", new Rule[]{
            new RegexRule("([0-9]*[\\.]?[0-9]{0,3})", "Estimation non valide")
        });
        
        rules.put("budget", new Rule[]{
            new NotEmptyRule("Veuillez préciser un budget alloué"),
            new RegexRule("([0-9]*[\\.]?[0-9]{0,3})", "Budget non valide")
        });
        
        rules.put("discount", new Rule[]{
            new NotEmptyRule("Veuillez saisir une remise (0 si aucune)"),
            new RegexRule("([0-9]?[0-9][\\.]?[0-9]{0,2})", "Remise non valide (valeur entre 0 et 99 nécessaire)")
        });
    }
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("SELECT", "");
        actions.put("INSERT", "new");
        actions.put("UPDATE", "edit");
        actions.put("DELETE", "delete");
        actions.put("CURRENT", "current");
        actions.put("AFFECT", "assign");
        actions.put("DISAFFECT", "unassign");
        actions.put("QUOTATION", "quotation");
    }
}
