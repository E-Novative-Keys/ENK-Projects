package net.enkeys;

import net.enkeys.framework.components.EApplication;

public class Main extends EApplication
{
    public Main(String name, String version, String dev, String contact, String[] args)
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
        new Main("Java HTTP(S) Requester", "0.1", "Valentin Fries", "contact@fries.io", args).run();
    }
}
