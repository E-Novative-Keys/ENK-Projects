package net.enkeys.framework.components;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.rules.Rule;
import net.enkeys.framework.exceptions.EDataException;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.utils.EHttpRequest;

public abstract class EModel
{
    /**
     * URL du WebService utilisé par le Modèle.
     */
    protected String SERVICE_URL = "http://enkwebservice.com/";
    /**
     * Actions pouvant être effectuées par le WebService.
     */
    public Map<String, String> actions;
            
    protected final Map<String, Object> data;
    protected final Map<String, Rule[]> rules;
    
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
     * du Modèle.
     * @param rules
     */
    protected abstract void initRules(Map<String, Rule[]> rules);
    
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
        
        if(rules.containsKey("!" + action))
            return true;
        
        if(errors == null)
            errors = new HashMap<>();
        
        if(o != null && o instanceof Map<?,?>
        && (o = ((Map<String, Object>)o).get(getName())) != null
        && o instanceof Map<?,?>)
        {
            data = (Map<String, Object>)o;

            for(Map.Entry ruleEntry : rules.entrySet())
            {
                String ruleKey = (String)ruleEntry.getKey();
                Rule[] rulesValue = (Rule[])ruleEntry.getValue();

                if(rulesValue != null)
                {
                    for(Rule rule : rulesValue)
                    {
                        if(rule.getClass().getSimpleName().equals("EscapeRule"))
                        {
                            if(rule.validate(action))
                                break;
                        }
                        else
                        {
                            if(data.get(ruleKey) == null)
                                errors.put(ruleKey + "/" + rule.getName(), "Is not set");
                            else if(!rule.validate(data.get(ruleKey)))
                                errors.put(ruleKey + "/" + rule.getName(), rule.getMessage());
                        }
                    }
                }
            }

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
     * Ajout d'une donnée formattée au tableau associatif donné.
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
        
        if(key.contains("["))
        {
            Map<String, Object> map = new HashMap<>();
            StringBuilder nextKeys = new StringBuilder();
            String firstKey = key.substring(0, key.indexOf("["));
            
            nextKeys.append(key.substring(key.indexOf("[")+1, key.indexOf("]")));
            nextKeys.append(key.substring(key.indexOf("]")+1));
            
            if(data.containsKey(firstKey))
            {
                Object o = data.get(firstKey);
                
                if(!(o instanceof Map<?,?>))
                {
                    Map<String, Object> convert = new HashMap<>();
                    convert.put(firstKey, o);
                    addData(convert, nextKeys.toString(), (value == null) ? "" : value); 
                }
                else
                    addData((Map<String, Object>)o, nextKeys.toString(), (value == null) ? "" : value); 
            }
            else
                data.put(firstKey, addData(map, nextKeys.toString(), (value == null) ? "" : value));
        }
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
                    getData((Map<String, Object>)o, nextKeys.toString()); 
            }
            else
                return null;
        }
        else
            return data.get(key);
        
        return null;
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
        
        if(o.length > 0)
            return execute(o[0].toString(), "POST", null);
        else
            throw new EDataException("Model has no action");
    }
    
    public final String execute(String action) throws EDataException, EHttpRequestException
    {
        return execute(action, "POST", null);
    }
    
    public final String execute(String action, String method) throws EDataException, EHttpRequestException
    {
        return execute(action, method, null);
    }
    
    public final String execute(String action, Map<String, String> errors) throws EDataException, EHttpRequestException
    {
        return execute(action, "POST", errors);
    }
    
    /**
     * Envoie les données au WebService et récupère la valeur de retour.
     * @param action
     * @param method
     * @param errors
     * @return
     * @throws EDataException, EHttpRequestException
     */
    public final String execute(String action, String method, Map<String, String> errors) throws EDataException, EHttpRequestException
    {
        EHttpRequest request;
        String url, actionURL;
        
        if((actionURL = this.actions.get(action)) == null)
            throw new EDataException("Invalid action specified");
        
        url = SERVICE_URL + this.getName().toLowerCase() + "s" + ((!actionURL.isEmpty()) ? "/" + actionURL : "");
        System.out.println(url);
        try
        {
            request = new EHttpRequest(
                new URL(url),
                "data=" + this.getJsonData()
            );
        }
        catch(MalformedURLException e)
        {
            throw new EDataException("An error occured while initializing request", e);
        }
        
        if(validate(action, data, errors))
        {
            String value;
            
            if(method.equalsIgnoreCase("GET"))
                value = request.get();
            else if(method.equalsIgnoreCase("POST"))
                value =  request.post();
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
