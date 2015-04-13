package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.Rule;

public class Mailbox extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
    }

    @Override
    protected void initActions(Map<String, String> actions) {
        actions.put("SELECT", "");
        actions.put("SENT", "sent");
        actions.put("DELETE", "delete");
    }
    
    
}
