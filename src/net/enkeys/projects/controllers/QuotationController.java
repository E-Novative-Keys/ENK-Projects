package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.exceptions.EHttpRequestException;
import net.enkeys.framework.exceptions.ESystemException;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.framework.utils.EHttpRequest;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Project;
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
        addModel(new Project());
        
        this.project = project;
        
        this.view.getBackButton().addActionListener(backButtonListener());
        this.view.getDevPercentSpinner().addChangeListener(spinnerListener());
        this.view.getGenerateButton().addActionListener(generateButtonListener());
    }
    
    private ActionListener generateButtonListener()
    {
        return (ActionEvent e) -> {
            float dev_salary = view.getDevSpinner().getValue() != null ? Float.parseFloat(view.getDevSpinner().getValue().toString()) : -1.f;
            float dev_percent = view.getDevPercentSpinner().getValue() != null ? Float.parseFloat(view.getDevPercentSpinner().getValue().toString()) : -1.f;
            float leaddev_salary = view.getLeaddevSpinner().getValue() != null ? Float.parseFloat(view.getLeaddevSpinner().getValue().toString()) : -1.f;
            float leaddev_percent = view.getLeaddevPercentSpinner().getValue() != null ? Float.parseFloat(view.getLeaddevPercentSpinner().getValue().toString()) : -1.f;
            
            if(dev_salary != -1.f && dev_percent != -1.f && leaddev_salary != -1.f && leaddev_percent != -1.f)
            {
                EHttpRequest request;
                Project model = (Project)getModel("Project");

                model.addData("data[Project][id]", ECrypto.base64(this.project.get("id")));
                model.addData("data[Project][dev_salary]", dev_salary);
                model.addData("data[Project][dev_percent]", view.getDevPercentSpinner().getValue());
                model.addData("data[Project][leaddev_salary]", view.getLeaddevSpinner().getValue());
                model.addData("data[Project][leaddev_percent]", view.getLeaddevPercentSpinner().getValue());

                model.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
                model.addData("data[Token][fields]", app.getUser().get("token"));
                
                try
                {
                    request = new EHttpRequest(new URL("http://enkwebservice.com/projects/quotation"), "data=" + model.getJsonData(), true);
                    
                    if(!request.download("POST", System.getProperty("user.home") + File.separator + "Téléchargements"))
                        setError("Une erreur est survenue lors de la génération du devis");
                }
                catch(MalformedURLException ex)
                {
                    setError(ex.getMessage());
                }
                catch(EHttpRequestException | ESystemException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            else
                setError("Veuillez saisir des valeurs correctes");
        };
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
    
    private void setError(String err)
    {
        if(!err.isEmpty())
            app.getLogger().warning(err);
        
        view.getErrorLabel().setText(err);
    }
}
