package net.enkeys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.enkeys.framework.components.ETable;
import net.enkeys.framework.components.EView;

public class RequesterView extends EView
{
    private final JLabel keyLabel = new JLabel("Data key: ");
    private final JTextField keyField = new JTextField(20);
    private final JLabel valueLabel = new JLabel("Data value: ");
    private final JTextField valueField = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JTextArea valuesArea = new JTextArea(10, 1);
    private final JScrollPane valuesScroller = new JScrollPane(valuesArea);
    
    private final JLabel errorLabel = new JLabel();
    private final JButton resetButton = new JButton("Reset");
    private final JButton getButton = new JButton("GET");
    private final JButton postButton = new JButton("POST");
    
    private final JTextArea terminal = new JTextArea();
    private final JScrollPane terminalScroller = new JScrollPane(terminal);
    
    public RequesterView()
    {
        super();
        
        add(configPanel(), getConstraints(), 0, 0, 1, 1, 0);
        add(terminalPanel(), getConstraints(), 1, 0, 1, 1, 1);
    }
    
    private JPanel configPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        ETable config = new ETable();
        ETable buttons = new ETable();
        GridBagConstraints constraints;
        
        //Config
        constraints = config.getConstraints();
        constraints.insets = new Insets(5, 10, 5, 5);
        
        keyLabel.setLabelFor(keyField);
        config.add(keyLabel, constraints, 0, 0);
        config.add(keyField, constraints, 1, 0);
        
        valueLabel.setLabelFor(valueField);
        config.add(valueLabel, constraints, 0, 1);
        config.add(valueField, constraints, 1, 1);
        
        constraints.fill = GridBagConstraints.NONE;
        config.add(addButton, constraints, 1, 2, 1, 1, 1, GridBagConstraints.EAST);
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        valuesArea.setEditable(false);
        config.add(valuesScroller, constraints, 0, 3, 0);
        
        //Buttons
        constraints = buttons.getConstraints();
        
        errorLabel.setForeground(Color.red);
        buttons.add(errorLabel, constraints, 0, 1, 0);
        
        buttons.add(resetButton, constraints, 0, 2);
        buttons.addEmpty(constraints, 1, 2);
        buttons.add(getButton, constraints, 2, 2);
        buttons.add(postButton, constraints, 3, 2);
        
        panel.add(config, "North");
        panel.add(buttons, "South");
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Config"));
        
        return panel;
    }
    
    private JPanel terminalPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Terminal"));
        
        terminal.setEditable(false);
        panel.add(terminalScroller);
        
        return panel;
    }
    
    public JLabel getError()
    {
        return errorLabel;
    }

    public JTextField getKeyField()
    {
        return keyField;
    }

    public JTextField getValueField() 
    {
        return valueField;
    }

    public JButton getAddButton() 
    {
        return addButton;
    }

    public JTextArea getValuesArea() 
    {
        return valuesArea;
    }

    public JButton getResetButton() 
    {
        return resetButton;
    }

    public JButton getGetButton()
    {
        return getButton;
    }

    public JButton getPostButton() 
    {
        return postButton;
    }

    public JTextArea getTerminal()
    {
        return terminal;
    }
}
