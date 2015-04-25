package net.enkeys.projects.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;
import net.enkeys.framework.utils.EResources;
import net.enkeys.framework.utils.ESystem;
import net.enkeys.projects.models.FileModel;
import net.enkeys.projects.models.FileRenderer;

/**
 * Vue CloudView
 * Vue principale du Cloud
 * @extends EView
 * @author E-Novative Keys
 */
public class CloudView extends EView
{
    private final FileRenderer clientRenderer   = new FileRenderer();
    private final FileRenderer devRenderer      = new FileRenderer();
    private final FileModel devData             = new FileModel();
    private final FileModel clientsData         = new FileModel();
    private final JList clientsList             = new JList(clientsData);
    private final JList devList                 = new JList(devData);
    private final JScrollPane clientsScroller   = new JScrollPane(clientsList);
    private final JScrollPane devScroller       = new JScrollPane(devList);
    private final JButton backButton            = new JButton(" Retour", EResources.loadImageIcon("back_dark.png"));
    private final JButton uploadDevButton       = new JButton("Upload");
    private final JButton uploadClientButton    = new JButton("Upload");
    private final JButton folderDevButton       = new JButton("Nouveau dossier");
    private final JButton folderClientButton    = new JButton("Nouveau dossier");
    private final JButton prevDevButton         = new JButton("Précédent");
    private final JButton prevClientButton      = new JButton("Précédent");
    private final JTextField commentField       = new JTextField(65);
    private final JButton commentButton         = new JButton("Enregistrer");
   
    
    public CloudView()
    {
        super();
        add(cloudPanel(), "Center");
        add(bottomPanel(), "South");
    }
    
    private JPanel cloudPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable table = new ETable();
        ETable buttons = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Cloud"));
        constraints.fill    = GridBagConstraints.BOTH;
        constraints.insets  = new Insets(0, 10, 0, 10);
        
        clientsList.setLayoutOrientation(JList.VERTICAL);
        clientsList.setVisibleRowCount(-1);
        clientsList.setCellRenderer(clientRenderer);
        
        devList.setLayoutOrientation(JList.VERTICAL);
        devList.setVisibleRowCount(-1);
        devList.setCellRenderer(devRenderer);
        
        devScroller.setPreferredSize(new Dimension(1, 1));
        clientsScroller.setPreferredSize(new Dimension(1, 1));
        table.add(devScroller,          constraints, 0, 0, 1, 1, 1);
        table.add(clientsScroller,      constraints, 1, 0, 1, 1, 1);
        panel.add(table, "Center");
        
        buttons.add(uploadDevButton,    constraints, 0, 1);
        buttons.add(folderDevButton,    constraints, 1, 1);
        buttons.add(prevDevButton,      constraints, 2, 1);
        buttons.add(uploadClientButton, constraints, 3, 1);
        buttons.add(folderClientButton, constraints, 4, 1);
        buttons.add(prevClientButton,   constraints, 5, 1);
        panel.add(buttons, "South");
        
        return panel;
    }
    
    private JPanel bottomPanel()
    {
        JPanel panel    = new JPanel(new BorderLayout());
        ETable table    = new ETable();
        GridBagConstraints constraints = table.getConstraints();
        
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(ESystem.getCursor(Cursor.HAND_CURSOR));
        panel.add(backButton, "West");
        
        constraints.fill = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        table.add(commentField, constraints, 0, 0);
        table.add(commentButton, constraints, 1, 0);
        
        panel.add(table, "Center");
        return panel;
    }

    public FileModel getClientsData()
    {
        return clientsData;
    }

    public FileModel getDevData()
    {
        return devData;
    }

    public JList getClientsList()
    {
        return clientsList;
    }

    public JList getDevList()
    {
        return devList;
    }

    public JScrollPane getClientsScroller()
    {
        return clientsScroller;
    }

    public JScrollPane getDevScroller()
    {   
        return devScroller;
    }

    public JButton getBackButton()
    {
        return backButton;
    }

    public JButton getUploadDevButton()
    {
        return uploadDevButton;
    }

    public JButton getUploadClientButton()
    {
        return uploadClientButton;
    }

    public JButton getFolderDevButton()
    {
        return folderDevButton;
    }

    public JButton getFolderClientButton()
    {
        return folderClientButton;
    }
    
    public JButton getPrevDevButton()
    {
        return prevDevButton;
    }

    public JButton getPrevClientButton()
    {
        return prevClientButton;
    }
    
    public FileRenderer getClientRenderer()
    {
        return clientRenderer;
    }

    public FileRenderer getDevRenderer()
    {
        return devRenderer;
    }    

    public JTextField getCommentField()
    {
        return commentField;
    }

    public JButton getCommentButton()
    {
        return commentButton;
    }
}
