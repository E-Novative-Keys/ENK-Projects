
package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.MaxRule;
import net.enkeys.framework.components.rules.MinRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

/**
 * Modèle Macrotask.
 * @extends EModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class Macrotask extends EModel 
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) 
    {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
        
        rules.put("name", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom de tâche"),
            new BetweenRule(3, 100, "Nom de macrotâche mauvais format (3 à 100 caractères)"),
            new RegexRule("([a-zA-Z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de macrotâche non valide") 
        });
        
        rules.put("priority", new Rule[]{
            new MinRule(0, "Priorité inférieure à 1 impossible"),
            new MaxRule(101, "Priorité supérieure à 100 impossible")
        });
        
        rules.put("hour", new Rule[]{
            new MinRule(-1, "Heures inférieures à 0 impossible"),
            new MaxRule(24, "Heures supérieures à 23 impossible")
        });
        
        rules.put("minute", new Rule[]{
            new MinRule(-1, "Minutes inférieures à 0 impossible"),
            new MaxRule(60, "Minutes supérieures à 59 impossible")
        });
    }
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("SELECT", "");
        actions.put("INSERT", "new");
        actions.put("UPDATE", "edit");
        actions.put("DELETE", "delete");
    }
}
