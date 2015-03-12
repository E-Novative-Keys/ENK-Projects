/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.projects.models;

import java.util.Map;
import net.enkeys.framework.components.EModel;
import net.enkeys.framework.components.rules.BetweenRule;
import net.enkeys.framework.components.rules.NotEmptyRule;
import net.enkeys.framework.components.rules.RegexRule;
import net.enkeys.framework.components.rules.Rule;

/**
 *
 * @author Worker
 */
public class Project extends EModel
{
    @Override
    protected void initRules(Map<String, Rule[]> rules) {
        rules.put("!SELECT", null);
        rules.put("!DELETE", null);
        
        rules.put("name", new Rule[]{
            new NotEmptyRule("Veuillez saisir un nom de projet"),
            new BetweenRule(3, 30, "Nom incorrect (3 à 30 caractères)"),
            new RegexRule("([a-zA-Z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ -]+)", "Nom de projet non valide") 
        });
        
        rules.put("estimation", new Rule[]{
            new RegexRule("([0-9]*[,\\.]?[0-9]{0,3})", "Estimation non valide")
        });
        
        rules.put("budget", new Rule[]{
            new NotEmptyRule("Veuillez préciser un budget alloué"),
            new RegexRule("([0-9]*[,\\.]?[0-9]{0,3})", "Budget non valide")
        });
        
        rules.put("discount", new Rule[]{
            new NotEmptyRule("Veuillez saisir une remise (0 si aucune)"),
            new RegexRule("([0-9]?[0-9])", "Remise non valide (valeur entre 0 et 99 nécessaire)")
        });
    }  
}
