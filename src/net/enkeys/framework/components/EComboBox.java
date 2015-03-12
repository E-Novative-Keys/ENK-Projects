package net.enkeys.framework.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EComboBox extends JComboBox //implements JComboBox.KeySelectionManager
{
    private String searchFor;
    private long lap;
    
    public class CBDocument extends PlainDocument
    {
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
        {
            if(str == null)
                return;
            
            super.insertString(offset, str, a);
                
            if(!isPopupVisible() && str.length() != 0) 
                fireActionEvent();
        }
    }
    
    public EComboBox()
    {
        super();
        JTextField tf;
        
        lap = new Date().getTime();
        
        if(getEditor() != null && (tf = (JTextField)getEditor().getEditorComponent()) != null)
        {
            tf.setDocument(new CBDocument());
            tf.addKeyListener(keyListener());
            
            addActionListener(actionListener());
        }
    }
    
    private KeyAdapter keyListener()
    {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                ComboBoxModel model = getModel();
                long now = new Date().getTime();
                char key = e.getKeyChar();
                
                if(key >= 32 && key < 127)
                {
                    if(searchFor != null && key == KeyEvent.VK_BACK_SPACE && searchFor.length() > 0)
                        searchFor = searchFor.substring(0, searchFor.length()-1);
                    else
                    {
                        if(lap + 1000 < now)
                            searchFor = "" + key;
                        else
                            searchFor += key;
                    }

                    lap = now;
                }
            }
        };
    }
    
    private ActionListener actionListener()
    {
        return (ActionEvent e) -> {
            JTextField tf = (JTextField)getEditor().getEditorComponent();
            ComboBoxModel model = getModel();
            String current;

            if(searchFor != null && searchFor.length() > 0)
            {
                for(int i = 0  ; i < model.getSize() ; i++) 
                {
                    current = model.getElementAt(i).toString();

                    if(current.toLowerCase().contains(searchFor.toLowerCase())) 
                    {
                        tf.setText(current);
                        tf.setSelectionStart(current.toLowerCase().indexOf(searchFor.toLowerCase()) + searchFor.length());
                        tf.setSelectionEnd(current.length()); 
                        break;
                    }
                }
            }
        };
    }
}

