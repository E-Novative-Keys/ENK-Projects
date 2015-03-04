package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import net.enkeys.projects.views.LoginView;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.JsonSyntaxException;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.MainFrame;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.HomeView;

public class LoginController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final LoginView view = (LoginView)super.view;
    
    public LoginController(EApplication app, EView view)
    {
        super(app, view);
        addModel(new User());
        
        this.view.getEmailField().addKeyListener(fieldReturnListener());
        this.view.getPasswordField().addKeyListener(fieldReturnListener());
        this.view.getLoginButton().addActionListener(loginButtonListener());
    }
    
    private KeyListener fieldReturnListener()
    {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                switch(e.getExtendedKeyCode())
                {
                    case KeyEvent.VK_ENTER:
                        loginButtonListener().actionPerformed(null);
                        break;
                }
            }
            
        };
    }
    
    private ActionListener loginButtonListener()
    {
        return (ActionEvent e) -> {
            User user = (User)getModel("User");
            Map<String, String> errors = new HashMap<>();
            
            user.addData("data[User][email]", view.getEmailField().getText());
            user.addData("data[User][password]", user.password(new String(view.getPasswordField().getPassword())));
            
            try
            {
                if(user.validate("login", user.getData(), errors))
                {
                    String json = user.execute();
                    
                    if(json.contains("user"))
                    {
                        try
                        {
                            Map<String, Map<String, String>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, Map<String, String>>>(){}.getType());

                            if(values != null)
                            {
                                Map<String, String> data = null;

                                if(values.get("user") == null || (data = values.get("user")) == null)
                                    setError("Identifiants invalides");
                                else if(data.get("role") == null || data.get("role").equalsIgnoreCase("client")
                                     || data.get("validated") == null || data.get("validated").equalsIgnoreCase("0"))
                                    setError("Vous n'êtes pas autorisé à accéder à " + app.getName());
                                else
                                {
                                    MainFrame frame = (MainFrame)app.getFrame(0);

                                    app.setUser(data);

                                    frame.setSize(940, 580);
                                    frame.setLocationRelativeTo(null);
                                    frame.setContent(new HomeController(app, new HomeView()));
                                }
                            }
                            else
                                setError("Identifiants invalides");
                        
                        }
                        catch(JsonSyntaxException ex)
                        {
                            setError("Identifiants invalides");
                        }
                    }
                    else
                        setError("Identifiants invalides");
                }
                else
                {
                    setError(errors.get(errors.keySet().toArray()[0].toString()));
                }
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
