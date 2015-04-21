package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.EmailRule;
import net.enkeys.framework.components.rules.MaxRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.PhoneNumberRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

public class Client extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules)
    {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
        
        rules.put("firstname", new Rule[]{
            new NotEmptyRule("Veuillez saisir un prénom"),
            new BetweenRule(2, 31, "Prénom mauvais format (3 à 30 caractères)"),
            new RegexRule("([a-zA-Zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Prénom non valide") 
        });
        
        rules.put("lastname", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom"),
            new BetweenRule(1, 31, "Nom mauvais format (2 à 30 caractères)"),
            new RegexRule("([a-zA-Zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom non valide") 
        });
        
        rules.put("phonenumber", new Rule[]{
            new NotEmptyRule("Veuillez saisir un numéro de téléphone"),
            new PhoneNumberRule("Veuillez saisir un numéro de téléphone valide")
        });
        
        rules.put("email", new Rule[]{
            new NotEmptyRule("Veuillez saisir une adresse e-mail"),
            new EmailRule("Veuillez saisir une adresse e-mail valide")
        });
        
        rules.put("enterprise", new Rule[]{
            new NotEmptyRule("Veuillez saisir le nom d'une entreprise"),
            new MaxRule(100, "Nom d'entreprise trop long (100 caractères max)"),
            new RegexRule("([\\.a-zA-Z0-9&àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom d'entreprise non valide") 
        });
        
        rules.put("address", new Rule[]{
            new NotEmptyRule("Veuillez saisir l'adresse de l'entreprise"),
            new MaxRule(100, "Adresse de l'entreprise trop long (100 caractères max)")
        });
        
        rules.put("siret", new Rule[]{
            new NotEmptyRule("Veuillez saisir un siret"),
            new RegexRule("[0-9]{3}?[0-9]{3}?[0-9]{3}?[0-9]{5}", "Siret non valide")
        });
    }    
}
