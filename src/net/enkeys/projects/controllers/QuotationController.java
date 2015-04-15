package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.QuotationView;

public class QuotationController extends EController
{
    private final ENKProjects app = (ENKProjects)super.app;
    private final QuotationView view = (QuotationView)super.view;
    private final HashMap<String, String> project;
    
    public QuotationController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        
        this.project = project;
        
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDevPercentSpinner().addChangeListener(spinnerListener());
    }
    
    private ChangeListener spinnerListener()
    {
        return (ChangeEvent e) -> {
            JSpinner spinner = (JSpinner)e.getSource();
            
            if(spinner != null && spinner.getName() != null)
            {
                if(spinner.getName().equals("dev_percent_spinner"))
                {
                    view.getLeaddevPercentSpinner().setValue(100.0 - (float)spinner.getValue());
                    System.out.println(100.0 - (float)spinner.getValue());
                }
                else if(spinner.getName().equals("leaddev_percent_spinner"))
                    view.getDevPercentSpinner().setValue(100.0 - (float)spinner.getValue());
            }
        };
    }
    
    private ActionListener backButtonListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
}
