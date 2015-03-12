/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enkeys.framework.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class EComboBox extends JComboBox implements JComboBox.KeySelectionManager
{
    private String searchFor;
    private long lap;
    
    public class CBDocument extends PlainDocument
    {
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
        {
            if(str==null) 
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
        
        lap = new java.util.Date().getTime();
        setKeySelectionManager(this);
        
        if(getEditor() != null)
        {
            tf = (JTextField)getEditor().getEditorComponent();
            if(tf != null)
            {
                tf.setDocument(new CBDocument());
                addActionListener((ActionEvent evt) -> 
                {
                    JTextField tf1 = (JTextField)getEditor().getEditorComponent();
                    String text = tf1.getText();
                    ComboBoxModel aModel = getModel();
                    String current;
                    
                    for (int i = 0; i < aModel.getSize(); i++) 
                    {
                        current = aModel.getElementAt(i).toString();
                        
                        if (current.toLowerCase().startsWith(text.toLowerCase())) 
                        {
                            tf1.setText(current);
                            tf1.setSelectionStart(text.length());
                            tf1.setSelectionEnd(current.length());
                            break;
                        }
                    }
                });
            }
        }
    }
    
    @Override
    public int selectionForKey(char aKey, ComboBoxModel aModel)
    {
        String current;
        long now = new java.util.Date().getTime();
        
        if (searchFor!=null && aKey==KeyEvent.VK_BACK_SPACE &&	searchFor.length()>0)
        {
            searchFor = searchFor.substring(0, searchFor.length() -1);
        }
        else
        {
            if(lap + 1000 < now)
                searchFor = "" + aKey;
            else
                searchFor = searchFor + aKey;
        }
        
        lap = now;
        
        for(int i = 0; i < aModel.getSize(); i++)
        {
            current = aModel.getElementAt(i).toString().toLowerCase();
            
            if (current.toLowerCase().startsWith(searchFor.toLowerCase())) 
                return i;
        }
        return -1;
    }
    
    @Override
    public void fireActionEvent()
    {
        super.fireActionEvent();
    }
    
    /*public static void main(String arg[])
    {
        JFrame f = new JFrame("AutoCompleteComboBox");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(200,300);
        Container cp= f.getContentPane();
        cp.setLayout(null);
        //String[] names= {"Beate", "Claudia", "Fjodor", "Fred", "Friedrich",	"Fritz", "Frodo", "Hermann", "Willi"};
        //JComboBox cBox= new AutoComplete(names);
        Locale[] locales = Locale.getAvailableLocales();//
        JComboBox cBox= new EComboBox(locales);
        cBox.setBounds(50,50,100,21);
        cBox.setEditable(true);
        cp.add(cBox);
        f.setVisible(true);
    }*/
}

