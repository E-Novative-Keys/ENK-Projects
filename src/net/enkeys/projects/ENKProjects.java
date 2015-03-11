package net.enkeys.projects;

import java.util.HashMap;
import java.util.Map;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ERuleException;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.EResources;
import net.enkeys.projects.controllers.LoginController;
import net.enkeys.projects.controllers.NewUserController;
import net.enkeys.projects.models.User;
import net.enkeys.projects.views.LoginView;
import net.enkeys.projects.views.NewUserView;

public class ENKProjects extends EApplication
{
    public static final String SALT = "$$-;-GQ^ TdD/-)7;_Kls+Q/Z<w+RI^],f6/bL 8=>ou!Hx~N/T-I| ~n@lOp6+t";
    public static final String PEPPER = "kwl:mxn+>Du2g}mXH$Yq|V{G-uo5yAY-:!%3G.38vR-Z<Rq@K/H-73SV>T RWFQK";
    
    private Map<String, String> user = null;
    
    public ENKProjects(String name, String version, String dev, String contact, String[] args)
    {
        super(name, version, dev, contact, args);
        
        EResources.setPackage("net.enkeys.projects.resources");
        addFrame(new MainFrame(this, getName(), 440, 380));
    }
    
    public void setUser(Map<String, String> user)
    {
        resetUser();
        this.user = user;
        ((MainFrame)getFrame(0)).getDisconnectItem().setVisible(true);
    }
    
    public void resetUser()
    {
        user = null;
        ((MainFrame)getFrame(0)).getDisconnectItem().setVisible(false);
    }
    
    public Map<String, String> getUser()
    {
        return user;
    }

    @Override
    public void run()
    {
        logger.log(getName() + " v" + getVersion());
        User user = new User();
        MainFrame frame = (MainFrame)getFrame(0);
        
        frame.setContent(new LoginController(this, new LoginView()));
        frame.setVisible(true);
        
        try
        {
            user.addData("data[User][link]", "test");
            String json = user.execute("VERIFY");
            System.out.println(json);
            
            if(json != null && json.contains("error"))
            {
                Map<String, String> value = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
                message(value.get("error"));
            }               
        }
        catch(ERuleException | EHttpRequestException ex)
        {
            getLogger().warning(ex.getMessage());
        }
        
    }
    
    public static void main(String[] args)
    {
        new ENKProjects("ENK-Projects", "0.1", "E-Novative Keys", "contact@enkeys.net", args).run();
    }
}
