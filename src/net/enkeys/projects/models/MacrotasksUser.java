
package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

public class MacrotasksUser extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) 
    {
        rules.put("user_name", new Rule[]{
            new RegexRule("([a-zA-Zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de tâche non valide") 
        });
    } 
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("INSERT", "new");
        actions.put("UPDATE", "edit");
    }
}
