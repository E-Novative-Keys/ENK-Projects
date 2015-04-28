package net.enkeys.projects.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import net.enkeys.framework.components.EApplication;
import net.enkeys.framework.components.EController;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.gson.Gson;
import net.enkeys.framework.gson.reflect.TypeToken;
import net.enkeys.framework.utils.ECrypto;
import net.enkeys.projects.ENKProjects;
import net.enkeys.projects.models.Mailbox;
import net.enkeys.projects.views.CurrentProjectManagerView;
import net.enkeys.projects.views.NewMailView;
import net.enkeys.projects.views.ReceivedMailsView;
import net.enkeys.projects.views.SentMailsView;

/**
 * Contrôlleur ReceivedMailsController.
 * Gestion de la liste des mails reçus.
 * @extends EController
 * @author E-Novative Keys
 * @version 1.0
 */
public class ReceivedMailsController extends EController
{
    private final ENKProjects app           = (ENKProjects)super.app;
    private final ReceivedMailsView view    = (ReceivedMailsView)super.view;
    private final HashMap<String, String> project;
    private JMenu mailMenu;
    
    public ReceivedMailsController(EApplication app, EView view, HashMap<String, String> project)
    {
        super(app, view);
        addModel(new Mailbox());
        
        this.project = project;
        
        this.view.getListMails().addMouseListener(mailListener());
        this.view.getDeleteButton().addActionListener(deleteListener());
        this.view.getBackButton().addActionListener(backListener());
        
        initView();
    }
    
    private void initView()
    {
        Mailbox mail = (Mailbox)getModel("Mailbox");
        Map<String, String> errors = new HashMap<>();
        
        if(app.getFrame(0).getJMenuBar().getMenuCount() == 1)
        {
            mailMenu            = new JMenu("Mail");
            JMenuItem received  = new JMenuItem("Boite de Réception");
            JMenuItem sent      = new JMenuItem("Eléments Envoyés");
            JMenuItem newMail   = new JMenuItem("Nouveau Message");
            
            received.addActionListener(receivedListener());
            sent.addActionListener(sentListener());
            newMail.addActionListener(newMailListener());
            
            mailMenu.add(received);
            mailMenu.add(sent);
            mailMenu.add(newMail);
            
            app.getFrame(0).getJMenuBar().add(mailMenu);
        }
        
        mail.addData("data[Mail][project]", ECrypto.base64(this.project.get("id")));
        mail.addData("data[Token][link]",   ECrypto.base64(app.getUser().get("email")));
        mail.addData("data[Token][fields]", app.getUser().get("token"));
        
        String json = mail.execute("SELECT", errors, true);
        Map<String, ArrayList<HashMap<String, String>>> values = new Gson().fromJson(json, new TypeToken<HashMap<String, ArrayList<HashMap<String, String>>>>(){}.getType());

        if(values != null && values.get("mails") != null)
        {
            ArrayList<HashMap<String, String>> mails = values.get("mails");

            for(HashMap<String, String> m : mails)
            {
                view.getDataTable().addValue(m);
                view.getDataTable().addOrigin(m);
            }
            view.getListMails().setAutoCreateRowSorter(true);
        }
        else
            app.message("Une erreur inattendue est survenue", JOptionPane.ERROR_MESSAGE);
    }
    
    private MouseListener mailListener()
    {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                view.getObject().setText("Sujet : ");
                view.getObjectLabel().setText(view.getDataTable().values.get(view.getListMails().getSelectedRow()).get("object"));
                view.getDate().setText("Reçu le : ");
                view.getDateLabel().setText(view.getDataTable().values.get(view.getListMails().getSelectedRow()).get("created"));
                view.getMailLabel().setVisible(true);
                view.getMailLabel().setText(view.getDataTable().values.get(view.getListMails().getSelectedRow()).get("content"));
            }
        };
    }
    
    private ActionListener deleteListener()
    {
        return (ActionEvent e) -> {
            int[] rows = view.getListMails().getSelectedRows();
            
            if(rows.length > 0
            && app.confirm("Êtes-vous certain de vouloir supprimer cet email ?") == ENKProjects.YES)
            {
                Mailbox mail                = (Mailbox)getModel("Mailbox");
                Map<String, String> errors  = new HashMap<>();
                
                mail.addData("data[Mail][project]", ECrypto.base64(this.project.get("id")));
                mail.addData("data[Token][link]",   ECrypto.base64(app.getUser().get("email")));
                mail.addData("data[Token][fields]", app.getUser().get("token"));
                
                for(int i = 0; i < rows.length; i++)
                {
                    int modelID = view.getListMails().convertRowIndexToModel(rows[i]-i);
                    
                    mail.addData("data[Mail][id]", ECrypto.base64(view.getDataTable().values.get(modelID).get("id")));
                    
                    String json                 = mail.execute("DELETE", errors, true);
                    Map<String, String> values  = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());

                    if(values != null && values.get("email") != null)
                    {
                        view.getObject().setText("");
                        view.getDataTable().removeValue(modelID);
                        view.getDate().setText("");
                        view.getObjectLabel().setText("");
                        view.getDateLabel().setText("");
                        view.getMailLabel().setText("");
                    }
                    else
                        app.message("Une erreur inattendue est survenue", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private ActionListener backListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).getJMenuBar().remove(1);
            app.getFrame(0).setJMenuBar(app.getFrame(0).getJMenuBar());
            app.getFrame(0).setContent(new CurrentProjectManagerController(app, new CurrentProjectManagerView(), this.project));
        };
    }
    
    private ActionListener receivedListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new ReceivedMailsController(app, new ReceivedMailsView(), this.project));
        };
    }
    
    private ActionListener sentListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new SentMailsController(app, new SentMailsView(), this.project));
        };
    }
    
    private ActionListener newMailListener()
    {
        return (ActionEvent e) -> {
            app.getFrame(0).setContent(new NewMailController(app, new NewMailView(), this.project));
        };
    }
}
