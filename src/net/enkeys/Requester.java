package net.enkeys;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.EmailRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

public class Requester extends EModel
{    
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("firstname", new Rule[] {
            new NotEmptyRule("Vous devez préciser un prénom"),
            new RegexRule("([a-z0-9\\-]+)", "Le prénom n'est pas valide"),
            new BetweenRule(5, 10, "Entre 5 et 10 caractères")
        });
        
        rules.put("email", new Rule[] {
            new EmailRule("Vous devez saisir un email valide")
        });
    }
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("SEND", "");
    }
}
