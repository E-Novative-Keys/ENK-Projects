package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
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

/**
 * Controller QuotationController
 * Gestion de la création d'un devis
 * @extends EController
 * @author E-Novative Keys
 */
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
        this.view.getGenerateButton().addActionListener(generateButtonListener());
    }
    
    //Génération du pdf à partir du devis et du projet selectionnés
    private ActionListener generateButtonListener()
    {
        return (ActionEvent e) -> {
            String dev_salary = new String(Float.parseFloat(view.getDevSpinner().getValue().toString()) + "");
            String leaddev_salary = new String(Float.parseFloat(view.getLeaddevSpinner().getValue().toString()) + "");
            String tva = new String(Float.parseFloat(view.getTVASpinner().getValue().toString()) + "");
            
            EHttpRequest request;
            Project model = (Project)getModel("Project");

            model.addData("data[Project][id]", ECrypto.base64(this.project.get("id")));
            model.addData("data[Project][dev_salary]", dev_salary);
            model.addData("data[Project][leaddev_salary]", leaddev_salary);
            model.addData("data[Project][tva]", tva);

            model.addData("data[Token][link]", ECrypto.base64(app.getUser().get("email")));
            model.addData("data[Token][fields]", app.getUser().get("token"));

            try
            {
                request = new EHttpRequest(new URL("http://enkwebservice.com/projects/quotation"), "data=" + model.getJsonData(), true);

                if(!request.download("POST", System.getProperty("user.home") + File.separator + "Téléchargements"))
                    setError("Une erreur est survenue lors de la génération du devis");
                else
                    setError("");
            }
            catch(MalformedURLException | EHttpRequestException | ESystemException ex)
            {
                setError(ex.getMessage());
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
