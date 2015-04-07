
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
        rules.put("name", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom de tâche"),
            new BetweenRule(2, 100, "Nom de tâche mauvais format (2 à 100 caractères)"),
            new RegexRule("([a-zA-Z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de tâche non valide") 
        });
    /*    
        rules.put("priority", new Rule[]{
            new MinRule(1, "Priorité inférieure à 1 impossible"),
            new MaxRule(101, "Priorité supérieure à 100 impossible")
        });
    */}
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("INSERT", "new");
    }
}
