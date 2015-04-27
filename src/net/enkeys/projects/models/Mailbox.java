package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.Rule;

/**
 * Modèle MailBox.
 * @extends EModel
 * @author E-Novative Keys
 * @version 1.0
 */
public class Mailbox extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("!SELECT", null);
        rules.put("!SENT", null);
        rules.put("!SEND", null);
        rules.put("!DELETE", null);
    }

    @Override
    protected void initActions(Map<String, String> actions) {
        actions.put("SELECT", "");
        actions.put("SENT", "sent");
        actions.put("SEND", "new");
        actions.put("DELETE", "delete");
    }
    
    
}
