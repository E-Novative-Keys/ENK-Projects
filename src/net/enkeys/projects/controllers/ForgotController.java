package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.ForgotView;
import net.enkeys.projects.views.LoginView;

/**
 * Contrôlleur ForgotController.
 * Gestion de l'oublie de credentials.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class ForgotController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final ForgotView view = (ForgotView)super.view;
    
    public ForgotController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new User());
        
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getTokenButton().addActionListener(tokenButtonListener());
        this.view.getSendButton().addActionListener(sendButtonListener());
        
        initView();
    }
    
    private void initView()
    {
        view.getPasswordField().setEnabled(false);
        view.getConfirmField().setEnabled(false);
        view.getTokenField().setEnabled(false);
        view.getSendButton().setEnabled(false);
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new LoginController(app, new LoginView()));
        };
    }
    
    private ActionListener tokenButtonListener()
    {
        return (ActionEvent e) -> {
            if(!view.getEmailField().getText().isEmpty()
             && app.confirm("Un code de sécurité va vous être envoyé à l'adresse indiquée. Confirmez-vous cette adresse e-mail ?") == ENKProjects.YES)
            {
                User user = (User)getModel("User");
                
                user.addData("data[User][email]", view.getEmailField().getText());
                
                if(user.validate("TOKEN"))
                {
                    String json = user.execute("TOKEN");
                    
                    if(json != null && json.contains("token"))
                    {
                        view.getPasswordField().setEnabled(true);
                        view.getConfirmField().setEnabled(true);
                        view.getTokenField().setEnabled(true);
                        view.getSendButton().setEnabled(true);
                        view.getEmailField().setEnabled(false);
                        view.getTokenButton().setEnabled(false);
       
                        setError("");
                    }
                    else
                        setError("Une erreur inattendue est survenue");
                }
                else
                    setError("Adresse email invalide");
            }
        };
    }
    
    private ActionListener sendButtonListener()
    {
        return (ActionEvent e) -> {
            String password = new String(view.getPasswordField().getPassword());
            String confirm = new String(view.getConfirmField().getPassword());
            String token = view.getTokenField().getText();
            
            if(!password.isEmpty() && !confirm.isEmpty() && !token.isEmpty())
            {
                if(password.equals(confirm))
                {
                    User user = (User)getModel("User");
                    Map<String, String> errors = new HashMap<>();
                    
                    //user.addData("data[User][email]", view.getEmailField().getText());
                    user.addData("data[User][password]", user.password(password));
                    user.addData("data[User][token]", token);
                    
                    if(user.validate("VALIDATE", user.getData(), errors))
                    {
                        String json = user.execute("VALIDATE", errors);
                        
                        if(json != null && json.contains("user"))
                        {
                            Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());
                            
                            if(values != null && values.get("user") != null)
                            {
                                Map<String, String> dataUser = values.get("user");
                                user.clearData();
                                user.addData("data[User][id]", dataUser.get("id"));
                                user.addData("data[User][firstname]", dataUser.get("firstname"));
                                user.addData("data[User][lastname]", dataUser.get("lastname"));
                                user.addData("data[User][email]", dataUser.get("email"));
                                user.addData("data[User][role]", dataUser.get("role"));
                                user.addData("data[User][password]", user.password(password));
                                user.addData("data[User][token_email]", "");
                                
                                user.addData("data[Token][link]", ECrypto.base64(dataUser.get("email")));
                                user.addData("data[Token][fields]", dataUser.get("token"));
                                
                                if(user.validate("UPDATE", user.getData(), errors))
                                {
                                    json = user.execute("UPDATE", errors);
                                    
                                    if(json != null)
                                    {
                                        app.message("Votre mot de passe a bien été modifié, vous pouvez à présent vous connecter");
                                        app.getFrame(0).setContent(new LoginController(app, new LoginView()));
                                    }
                                    else
                                        app.message("Une erreur est survenue lors de la modification de votre mot de passe", JOptionPane.ERROR_MESSAGE);
                                }
                                else
                                    setError(errors.toString());
                            }
                            else
                                setError("Une erreur inattendue est survenue");
                        }
                        else
                            setError("Le code de sécurité est invalide");
                    }
                    else
                        setError("Données non valides");
                }
                else
                    setError("Les mots de passe ne correspondent pas");
            }
            else
                setError("Veuillez remplir tous les champs");
                
        };
    }
    
    private void setError(String err)
    {
        view.getErrorLabel().setText(err);
        
        if(!err.isEmpty())
            app.getLogger().warning(err);
    }
}
