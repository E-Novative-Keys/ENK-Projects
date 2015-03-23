package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.Rule;

public class Cloud extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("project", new Rule[]{
            new NotEmptyRule("Vous n'avez pas d'id projet")
        });
    }

    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("LIST_DEV", "files/dev");
        actions.put("LIST_CLIENT", "files/client");
        actions.put("ADD_FILE", "files/add");
        actions.put("ADD_FOLDER", "folder/add");
        actions.put("RENAME_FILE", "files/rename");
        actions.put("DEL_FILE", "files/delete");
        actions.put("DOWNLOAD", "files/download");
        actions.put("COMMENT", "comment");
    }
    
    
}
