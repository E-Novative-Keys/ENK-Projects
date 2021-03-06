package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.HomeView;
import net.enkeys.projects.views.ProfileView;

/**
 * Contrôlleur ProfileController.
 * Gestion du profil.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class ProfileController extends EController
{
    private final ENKProjects app   = (ENKProjects) super.app;
    private final ProfileView view  = (ProfileView) super.view;
    public ProfileController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new User());
        
        this.view.getBackButton().addActionListener(backListener());
        this.view.getMajButton().addActionListener(majListener());
        
        initView();
    }
    
    private void initView()
    {
        try
        {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(app.getUser().get("lastlogin"));
            
            this.view.getUserValue().setText(app.getUser().get("firstname") + " " + app.getUser().get("lastname"));
            this.view.getMailValue().setText(app.getUser().get("email"));
            this.view.getDateValue().setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date));
            this.view.getIpValue().setText(app.getUser().get("lastip"));
        }
        catch(ParseException ex)
        {
            app.getLogger().warning(ex.getMessage());
        }
        
    }
    
    private ActionListener backListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new HomeController(app, new HomeView()));
        };
    }
    
    //Mise à jour du profil via le webservice
    private ActionListener majListener()
    {
        return (ActionEvent e) -> {
            User user                   = (User)getModel("User");
            Map<String, String> errors  = new HashMap<>();
            
            String oldpass              = new String(view.getOldPass().getPassword());
            String password             = new String(view.getNewPass().getPassword());
            String confirm              = new String(view.getConfirm().getPassword());
            
            if(!oldpass.isEmpty() && !password.isEmpty() && !confirm.isEmpty())
            {
                if(password.equals(confirm))
                {
                    user.addData("data[User][id]",          app.getUser().get("id"));
                    user.addData("data[User][oldpass]",     user.password(oldpass));
                    user.addData("data[User][password]",    user.password(password));
                    
                    user.addData("data[Token][link]",       ECrypto.base64(app.getUser().get("email")));
                    user.addData("data[Token][fields]",     app.getUser().get("token"));
                    
                    String json = user.execute("CHANGEPASS", errors);
                    
                    if(json.contains("error"))
                    {
                        Map<String, String> values = new Gson().fromJson(
                            json, new TypeToken<Map<String, String>>(){}.getType()
                        );
                        
                        if(values != null)
                        {
                            if(values.get("error") != null)
                                setError(values.get("error"));
                            else
                                setError("Une erreur est survenue lors de la mise à jour du password");
                        }
                    }
                    else if(json.contains("users"))
                    {
                        app.message("Votre mot de passe a bien été édité.");
                        app.getFrame(0).setContent(new HomeController(app, new HomeView()));
                    }
                    else
                        setError(json);  
                }
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