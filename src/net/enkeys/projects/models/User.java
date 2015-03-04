package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.EmailRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.Rule;
import net.enkeys.framework.exceptions.ECryptoException;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;

public class User extends EModel
{    
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("email", new Rule[] {
            new NotEmptyRule("Veuillez saisr une adresse e-mail"),
            new EmailRule("Veuillez saisir une adresse e-mail valide")
        });
        
        rules.put("password", new Rule[] {
            new NotEmptyRule("Veuillez saisir votre mot de passe")
        });
    }
    
    @Override
    protected void initActions(Map<String, String> actions)
    {
        actions.put("LOGIN", "login");
        actions.put("SELECT", "");
    }
    
    public String password(String pass) throws ECryptoException
    {
        return ECrypto.base64(ECrypto.md5(ENKProjects.SALT) 
                            + ECrypto.md5(pass)
                            + ECrypto.md5(ENKProjects.PEPPER));
    }
}
