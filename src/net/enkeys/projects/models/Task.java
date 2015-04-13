
package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.MaxRule;
import net.enkeys.framework.components.rules.MinRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

public class Task extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) 
    {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
        
        rules.put("name", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom de tâche"),
            new BetweenRule(2, 100, "Nom de tâche mauvais format (2 à 100 caractères)"),
            new RegexRule("([a-zA-Z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de tâche non valide") 
        });
        
        rules.put("progress", new Rule[]{
            new NotEmptyRule("Veuillez saisir une progression pour la tâche"),
            new RegexRule("[0-1]", "Progression invalide (0 ou 1 attendu)") 
        });
    
        rules.put("priority", new Rule[]{
            new MinRule(1, "Priorité inférieure à 1 impossible (tâche)"),
            new MaxRule(100, "Priorité supérieure à 100 impossible (tâche)")
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
