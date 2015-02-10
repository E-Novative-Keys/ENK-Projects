package net.enkeys.projects;

import net.enkeys.framework.components.EApplication;

public class ENKProjects extends EApplication
{
    public ENKProjects(String name, String version, String dev, String contact, String[] args)
    {
        super(name, version, dev, contact, args);
        
        addFrame(new MainFrame(this, getName(), 1040, 480));
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
