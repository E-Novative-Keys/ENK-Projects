package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

/**
 * Vue NewMailView.
 * Vue d'envoi de mail.
 * @extends EView
 * @author E-Novative Keys
 * @version 1.0
 */
public class NewMailView extends EView
{
    private final JLabel newMailLabel       = new JLabel("Nouveau Message :");
    private final JLabel objectLabel        = new JLabel("Objet");
    private final JTextField object         = new JTextField(50);
    private final JLabel mailLabel          = new JLabel("Message");
    private final JTextArea mail            = new JTextArea(20, 50);
    private JScrollPane scrollPane          = null;
    private final JButton sendButton        = new JButton("Envoyer");
    private final JButton backButton        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel         = new JLabel("");
    
    public NewMailView()
    {
        super();
        
        add(newMailPanel(), "Center");
        add(bottomPanel(),  "South");
    }
    
    private ETable newMailPanel()
    {
        ETable panel                    = new ETable();
        ETable newMail                  = new ETable();
        GridBagConstraints constraints  = newMail.getConstraints();
        
        constraints.fill    = GridBagConstraints.CENTER;
        constraints.insets  = new Insets(10, 10, 10, 10);
        
        newMail.add(newMailLabel,   constraints, 0, 0, 0);
        newMail.add(objectLabel,    constraints, 0, 1);
        newMail.add(object,         constraints, 1, 1);
        newMail.add(mailLabel,      constraints, 0, 2);
        
        mail.setLineWrap(true);
        scrollPane  = new JScrollPane(mail); 
        newMail.add(scrollPane,     constraints, 1, 2);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        
        panel.add(newMail,          constraints, 0, 0);
        
        return panel;
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel                    = new JPanel(new BorderLayout());
        ETable table                    = new ETable();
        ETable buttons                  = new ETable();
        GridBagConstraints constraints  = table.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        errorLabel.setForeground(Color.red);
        table.add(errorLabel,   constraints, 0, 0, 0);
        panel.add(table, "Center");
        
        constraints = buttons.getConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.CENTER;
        buttons.add(sendButton, constraints, 0, 0);
        panel.add(buttons, "East");
       
        return panel;
    }

    public JLabel getObjectLabel()
    {
        return objectLabel;
    }

    public JTextField getObject()
    {
        return object;
    }

    public JLabel getMailLabel()
    {
        return mailLabel;
    }

    public JTextArea getMail()
    {
        return mail;
    }

    public JButton getSendButton()
    {
        return sendButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JLabel getErrorLabel()
    {
        return errorLabel;
    }
}
