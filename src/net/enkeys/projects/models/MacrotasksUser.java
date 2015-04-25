
package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

/**
 * Modele MacrotaskUser
 * Validation des données pour l'ajout
 * d'association User/Macrotask
 * @extends EModel
 * @author E-Novative Keys
 */
public class MacrotasksUser extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) 
    {
        rules.put("!SELECT", null);
        
        rules.put("user_name", new Rule[]{
            new RegexRule("([a-zA-Zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom d'utilisateur nécessaire") 
        });
    } 
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("SELECT", "");
        actions.put("INSERT", "new");
        actions.put("UPDATE", "edit");
    }
}
