package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;

public class NewMailView extends EView
{
    private final JLabel objectLabel    = new JLabel("Objet");
    private final JTextField object     = new JTextField();
    private final JLabel mailLabel      = new JLabel("Message");
    private final JTextArea mail        = new JTextArea(10, 5);
    private final JButton sendButton    = new JButton("Envoyer");
    private final JButton backButton    = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JLabel errorLabel     = new JLabel("");
    
    public NewMailView()
    {
        super();
        
        add(newMailPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private ETable newMailPanel()
    {
        ETable panel                    = new ETable();
        ETable newMail                  = new ETable();
        GridBagConstraints constraints  = newMail.getConstraints();
        
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(10, 10, 10, 10);
        
        newMail.add(objectLabel, constraints, 0, 0);
        newMail.add(object, constraints, 1, 0);
        newMail.add(mailLabel, constraints, 0, 1);
        newMail.add(mail, constraints, 1, 1);
        newMail.add(sendButton, constraints, 1, 2, 1, 2, 2);
        
        constraints         = panel.getConstraints();
        constraints.fill    = GridBagConstraints.CENTER;
        panel.add(newMail, constraints, 0, 0);
        
        return newMail;
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        errorLabel.setForeground(Color.red);
        panel.add(errorLabel, "Center");
       
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
