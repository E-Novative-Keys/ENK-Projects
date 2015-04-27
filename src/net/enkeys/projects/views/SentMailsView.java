package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.models.SentTable;

/**
 * Vue SentMailsView.
 * Vue des messages envoyés.
 * @extends EView
 * @author E-Novative Keys
 * @version 1.0
 */
public class SentMailsView extends EView
{
    private final SentTable dataTable       = new SentTable();
    private final JTable listMails          = new JTable(dataTable);
    private final JScrollPane listScroller  = new JScrollPane(listMails);
    private final JLabel objectLabel        = new JLabel("");
    private final JLabel dateLabel          = new JLabel("");
    private final JLabel mailLabel          = new JLabel("");
    private final JButton deleteButton      = new JButton("Supprimer la sélection");
    private final JButton backButton        = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    
    public SentMailsView()
    {
        super();
        
        add(listScroller, "West");
        add(mailPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private JPanel mailPanel()
    {
        ETable mail     = new ETable();
        JPanel panel    = new JPanel(new BorderLayout());
        GridBagConstraints constraints = mail.getConstraints();
        
        mail.add(objectLabel, constraints, 0, 0);
        mail.add(dateLabel, constraints, 0, 1);
        mail.add(mailLabel, constraints, 0, 2);
        panel.add(mail, "North");
        
        return panel;
    }
   
    private JPanel bottomPanel()
    {
        JPanel panel    = new JPanel(new BorderLayout());
        ETable buttons  = new ETable();
        GridBagConstraints constraints = buttons.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        buttons.add(deleteButton, constraints, 2, 0);
        panel.add(buttons, "East");
       
        return panel; 
    }

    public SentTable getDataTable()
    {
        return dataTable;
    }

    public JTable getListMails()
    {
        return listMails;
    }

    public JScrollPane getListScroller()
    {
        return listScroller;
    }

    public JLabel getObjectLabel() {
        return objectLabel;
    }

    public JLabel getDateLabel() {
        return dateLabel;
    }

    public JLabel getMailLabel() {
        return mailLabel;
    }
    
    public JButton getDeleteButton()
    {
        return deleteButton;
    }

    public JButton getBackButton()
    {
        return backButton;
    }
}
