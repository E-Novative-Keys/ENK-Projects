package net.enkeys.framework.components;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.rules.Rule;
import net.enkeys.framework.exceptions.EDataException;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.utils.EHttpRequest;

/**
 * Classe abstraite mère de tous les Modèles de l'application.
 * Permet l'ajout, la récupération et la validation de données, ainsi qu'une
 * intégration complète avec le WebService ENK.
 * @author E-Novative Keys
 * @version 1.0
 */
public abstract class EModel
{
    //URL du WebService utilisé par le Modèle
    protected String SERVICE_URL = "http://enkwebservice.com/";
    //Actions du WebService.
    public Map<String, String> actions;
            
    protected final Map<String, Object> data;   //Les données du modèle
    protected final Map<String, Rule[]> rules;  //Les règles de validation du modèle
    
    /**
     * Crée une nouvelle instance de type EModel.
     * Initialise les divers listes du modèles et fait appel à la méthode
     * abstraite de définition des règles, ainsi qu'à la méthode d'initialisation
     * des actions.
     */
    public EModel()
    {
        actions = new HashMap<>();
        data = new HashMap<>();
        rules = new HashMap<>();
        
        initRules(rules);
        initActions(actions);
    }
    
    /**
     * Fonction absrtaite d'initialisation des règles de validation des données
     * du modèle.
     * @param rules
     */
    protected abstract void initRules(Map<String, Rule[]> rules);
    
    /**
     * Initialisation des actions par défaut du modèle.
     * @param actions 
     */
    protected void initActions(Map<String, String> actions)
    {
        actions.put("SELECT", "");
        actions.put("INSERT", "new");
        actions.put("UPDATE", "edit");
        actions.put("DELETE", "delete");
    }
    
    public final boolean validate()
    {
        Object[] o = actions.keySet().toArray();
        
        //Si aucune action n'est précisée, la première de la liste est utilisée
        if(o.length > 0)
            return validate(o[0].toString(), this.data, null);
        else
            throw new EDataException("Model has no action");
    }
    
    public final boolean validate(String action)
    {
        return validate(action, this.data, null);
    }
    
    public final boolean validate(String action, Map<String, Object> data)
    {
        return validate(action, data, null);
    }
    
    /**
     * Validation des données du Modèle en fonction des Rule configurées.
     * @param action
     * @param data
     * @param errors
     * @return
     */
    public final boolean validate(String action, Map<String, Object> data, Map<String, String> errors)
    {
        Object o = data.get("data");
        
        //On échappe les actions ne devant pas être validées
        if(rules.containsKey("!" + action))
            return true;
        
        if(errors == null)
            errors = new HashMap<>();
        
        /* On place dans o la liste contenant les données propres au modèle.
           Dans un Modèle "Test" ayant comme données data[Test][ma_donnée]
           et data[Autre][autre_donnée], seule la liste data[Test] sera retenue */
        if(o != null && o instanceof Map<?,?>
        && (o = ((Map<String, Object>)o).get(getName())) != null
        && o instanceof Map<?,?>)
        {
            data = (Map<String, Object>)o;

            //On parcourt le tableau de règles du modèle
            for(Map.Entry ruleEntry : rules.entrySet())
            {
                String ruleKey = (String)ruleEntry.getKey();
                Rule[] rulesValue = (Rule[])ruleEntry.getValue();

                if(rulesValue != null)
                {
                    for(Rule rule : rulesValue)
                    {
                        //Si la règle est une règle d'échappement locale, on teste la valeur de l'action
                        if(rule.getClass().getSimpleName().equals("EscapeRule"))
                        {
                            //Si l'action doit être échappée, on break
                            if(rule.validate(action))
                                break;
                        }
                        //On valide la  données définit par la règle
                        else
                        {
                            //Si la donnée n'est pas présente, on ajoute une erreur
                            if(data.get(ruleKey) == null)
                                errors.put(ruleKey + "/" + rule.getName(), "Is not set");
                            //Sinon, on la valide
                            else if(!rule.validate(data.get(ruleKey)))
                                errors.put(ruleKey + "/" + rule.getName(), rule.getMessage());
                        }
                    }
                }
            }

            //Renvoie true s'il n'y a aucune erreur après validation
            if(errors.isEmpty())
                return true;
        }
        
        return false;
    }
    
    public final Map<String, Object> addData(String key)
    {
        return addData(null, key, null);
    }
    
    public final Map<String, Object> addData(String key, Object value)
    {
        return addData(null, key, value);
    }
    
    /**
     * Méthode récursive d'ajout d'une donnée formattée au tableau associatif donné.
     * Exemple : addData(this.data, "data[User][lastname]", "Dupont");
     * Si data est null, c'est this.data qui est utilisé.
     * @param data
     * @param key
     * @param value
     * @return 
     */
    public final Map<String, Object> addData(Map<String, Object> data, String key, Object value)
    {
        if(data == null)
            data = this.data;
        
        /* Si la clé contient un crochet, on extrait les valeurs le précédant
           et contenu à l'intérieur du crochet de manière à former une hiérarchie
           dans la liste de données */
        if(key.contains("["))
        {
            Map<String, Object> map = new HashMap<>();
            StringBuilder nextKeys = new StringBuilder();         //Valeur contenu entre les premiers crochets
            String firstKey = key.substring(0, key.indexOf("[")); //Valeur précédant le crochet
            
            nextKeys.append(key.substring(key.indexOf("[")+1, key.indexOf("]")));
            nextKeys.append(key.substring(key.indexOf("]")+1));
            
            //Si la clé existe déjà, on ajoute les données à sa liste
            if(data.containsKey(firstKey))
            {
                Object o = data.get(firstKey); //La liste correspondant à cette clé
                
                //Si la donnée n'est pas une liste
                if(!(o instanceof Map<?,?>))
                {
                    //On convertit la donnée en liste, on injecte la valeur déjà présente...
                    Map<String, Object> convert = new HashMap<>();
                    convert.put(firstKey, o);
                    //... puis on y ajoute les nouvelles données
                    addData(convert, nextKeys.toString(), (value == null) ? "" : value); 
                }
                else
                    addData((Map<String, Object>)o, nextKeys.toString(), (value == null) ? "" : value); 
            }
            //Sinon, on crée la sous-liste et on y ajoute les données
            else
                data.put(firstKey, addData(map, nextKeys.toString(), (value == null) ? "" : value));
        }
        //Si la clé ne contient aucun crochet, on ajoute la donnée à la liste
        else
        {
            if(data == this.data)
                throw new EDataException("Trying to add a new data with an invalid key: " + key);
            else
                data.put(key, (value == null) ? "" : value);
        }
        
        return data;
    }
    
    public final Object getData(String key)
    {
        return getData(null, key);
    }
    
    /**
     * Méthode récursive renvoyant la donnée contenue derrière la clé donnée.
     * @param data
     * @param key
     * @return 
     */
    public final Object getData(Map<String, Object> data, String key)
    {
        if(data == null)
            data = this.data;
        
        if(key.contains("["))
        {
            StringBuilder nextKeys = new StringBuilder();
            String firstKey = key.substring(0, key.indexOf("["));
            
            nextKeys.append(key.substring(key.indexOf("[")+1, key.indexOf("]")));
            nextKeys.append(key.substring(key.indexOf("]")+1));
            
            if(data.containsKey(firstKey))
            {
                Object o = data.get(firstKey);
                
                if(!(o instanceof Map<?,?>))
                    return null;
                else
                    return getData((Map<String, Object>)o, nextKeys.toString()); 
            }
            else
                return null;
        }
        else
            return data.get(key);
    }
    
    /**
     * Vide le contenu de la Map de données du Modèle.
     */
    public final void clearData()
    {
        data.clear();
    }
    
    public final String execute() throws EDataException, EHttpRequestException
    {
        Object[] o = actions.keySet().toArray();
        
        //Si aucune action n'est spécifiée, on appelle la première action du modèle
        if(o.length > 0)
            return execute(o[0].toString(), "POST", null, false, null);
        else
            throw new EDataException("Model has no action");
    }
    
    public final String execute(String action) throws EDataException, EHttpRequestException
    {
        return execute(action, "POST", null, false, null);
    }
    
    public final String execute(String action, String method) throws EDataException, EHttpRequestException
    {
        return execute(action, method, null, false, null);
    }
    
    public final String execute(String action, Map<String, String> errors) throws EDataException, EHttpRequestException
    {
        return execute(action, "POST", errors, false, null);
    }
    
    public final String execute(String action, Map<String, String> errors, boolean escape) throws EDataException, EHttpRequestException
    {
        return execute(action, "POST", errors, escape, null);
    }
    
    /**
     * Envoie les données au WebService et récupère la valeur de retour.
     * @param action
     * @param method
     * @param errors
     * @param escape
     * @param file
     * @return
     * @throws EDataException, EHttpRequestException
     */
    public final String execute(String action, String method, Map<String, String> errors, boolean escape, File file) throws EDataException, EHttpRequestException
    {
        EHttpRequest request;
        String url, actionURL;
        
        //Lance une exception si l'action spécifiée n'existe pas
        if((actionURL = this.actions.get(action)) == null)
            throw new EDataException("Invalid action specified");
        
        //Construction de l'url à partir de l'adresse du WebService, du nom du modèle et de l'action
        url = SERVICE_URL + ((escape) ? this.getName().toLowerCase() : this.getName().toLowerCase() + "s") + ((!actionURL.isEmpty()) ? "/" + actionURL : "");

        try
        {
            //On instancie une nouvelle requête HTTP à partir de l'url et des données du modèle
            request = new EHttpRequest(
                new URL(url),
                "data=" + this.getJsonData()
            );
        }
        catch(MalformedURLException e)
        {
            throw new EDataException("An error occured while initializing request", e);
        }
        
        //Si les données sont correctement validées, on exécute la requête et on récupère sa valeur de retour
        if(validate(action, data, errors))
        {
            String value;
            
            if(method.equalsIgnoreCase("GET"))
                value = request.get();
            else if(method.equalsIgnoreCase("POST"))
                value = request.post();
            else
                throw new EHttpRequestException("Invalid method specified");
            
            if(value != null && !value.isEmpty())
                return value;
            else
                return null;
        }
        else
            throw new EDataException(this.getName() + " data could not be validated");
    }
    
    /**
     * Renvoie les données du Modèle.
     * @return 
     */
    public final Map<String, Object> getData()
    {
        return data;
    }
    
    /**
     * Renvoie les données du Modèle formatées en Json.
     * @return 
     */
    public final String getJsonData()
    {
        return new Gson().toJson(data);
    }
    
    /**
     * Renvoie le nom du Modèle courant.
     * @return 
     */
    public final String getName()
    {
        return this.getClass().getSimpleName();
    }
}
