package net.enkeys.projects;

import java.net.PasswordAuthentication;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.utils.EResources;

public class ENKProjects extends EApplication
{
    public static final String SALT = "$$-;-GQ^ TdD/-)7;_Kls+Q/Z<w+RI^],f6/bL 8=>ou!Hx~N/T-I| ~n@lOp6+t";
    public static final String PEPPER = "kwl:mxn+>Du2g}mXH$Yq|V{G-uo5yAY-:!%3G.38vR-Z<Rq@K/H-73SV>T RWFQK";
    
    private PasswordAuthentication auth;
    
    public ENKProjects(String name, String version, String dev, String contact, String[] args)
    {
        super(name, version, dev, contact, args);
        
        EResources.setPackage("net.enkeys.projects.resources");
        addFrame(new MainFrame(this, getName(), 440, 380));
    }
    
    public void setAuth(String user, String password)
    {
        resetAuth();
        auth = new PasswordAuthentication(user, password.toCharArray());
    }
    
    public String getUser()
    {
        return (auth != null) ? auth.getUserName() : null;
    }
    
    public String getToken()
    {
        return (auth != null) ? new String(auth.getPassword()) : null;
    }
    
    public void resetAuth()
    {
        auth = null;
    }

    @Override
    public void run()
    {
        logger.log(getName() + " v" + getVersion());
        getFrame(getName()).setVisible(true);
    }
    
    public static void main(String[] args)
    {
        new ENKProjects("ENK-Projects", "0.1", "E-Novative Keys", "contact@enkeys.net", args).run();
    }
}
