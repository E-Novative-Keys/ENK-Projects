package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.NewUserView;
import net.enkeys.projects.views.UsersManagerView;

/**
 * Controller NewUserController
 * Gestion de la création d'un utilisateur
 * @extends EController
 * @author E-Novative Keys
 */
public class NewUserController extends EController
{
    private final ENKProjects app   = (ENKProjects)super.app;
    private final NewUserView view  = (NewUserView)super.view;
    
    public NewUserController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new User());
        
        this.view.getBack().addActionListener(backButtonListener());
        this.view.getSave().addActionListener(saveButtonListener());
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
        };
    }
    
    //Sauvegarde de l'utilisateur entré en vue, via le webservice
    private ActionListener saveButtonListener()
    {
        return (ActionEvent e) -> {
            User user       = (User) getModel("User");
            DateFormat df   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> errors = new HashMap<>();
                      
            if(!new String(view.getPassword().getPassword()).equals(new String(view.getConfirm().getPassword())))
                setError("Mot de passe et confirmation différents"); 
            
            user.addData("data[User][password]", user.password(new String(view.getPassword().getPassword())));
            user.addData("data[User][firstname]", view.getFirstname().getText().substring(0, 1).toUpperCase()
                                                + view.getFirstname().getText().substring(1));
            user.addData("data[User][lastname]", view.getLastname().getText().toUpperCase());
            user.addData("data[User][email]", view.getEmail().getText());
            user.addData("data[User][role]", view.getRole().getSelectedItem());
            user.addData("data[User][validated]", (view.getValidated().isSelected()) ? 1 : 0);
            user.addData("data[User][password]", user.password(new String(view.getPassword().getPassword())));
            user.addData("data[User][created]", df.format(new Date()));
            user.addData("data[User][updated]", df.format(new Date()));
            user.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            user.addData("data[Token][fields]", app.getUser().get("token"));
            
            try
            {
                if(user.validate("INSERT", user.getData(), errors))
                {
                    String json = user.execute("INSERT");
                    
                    if(json.contains("users"))
                    {
                        app.getFrame(0).setContent(new UsersManagerController(app, new UsersManagerView()));
                        app.message("L'utilisateur a été correctement créé");
                    }
                    else if(json.contains("error"))
                    {
                        Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                        
                        if((errors = values.get("error")) != null)
                            setError(errors.get(errors.keySet().toArray()[0].toString()));
                        else
                            setError("Une erreur inattendue est survenue");
                    }
                    else
                        setError(json);
                }
                else
                    setError(errors.get(errors.keySet().toArray()[0].toString()));
            }
            catch(ERuleException | EHttpRequestException ex)
            {
                setError(ex.getMessage());
            }
        };
    }
    
    private void setError(String err)
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
