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

/**
 * Élément JComboBox permettant l'autocomplétion via saisie au clavier.
 * @author E-Novative Keys
 * @version 1.0
 */
public class EComboBox extends JComboBox
{
    private String searchFor;           //La chaîne recherchée dans la ComboBox
    private long lap;                   //Le temps écoulé entre deux saisies
    private final int graceTime = 1000; //Le temps après lequel la saisie se réinitialise
    
    /**
     * L'objet contenant les valeurs dans notre ComboBox.
     */
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
    
    /**
     * Crée une nouvelles instance de type EComboBox.
     * Permet l'initialisation du lap et l'ajout des listeners.
     */
    public EComboBox()
    {
        super();
        JTextField tf;
        
        lap = new Date().getTime();
        
        //On redéfinit le Document à utiliser et les listeners à appliquer
        if(getEditor() != null && (tf = (JTextField)getEditor().getEditorComponent()) != null)
        {
            tf.setDocument(new CBDocument());
            tf.addKeyListener(keyListener());
            
            addActionListener(actionListener());
        }
    }
    
    /**
     * L'événement clavier appliqué au TextField éditable de notre ComboBox.
     * @return 
     */
    private KeyAdapter keyListener()
    {
        return new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                ComboBoxModel model = getModel();
                long now = new Date().getTime();
                char key = e.getKeyChar();
                
                //Si la touche est bien un caractère ASCII "affichable"
                if(key >= 32 && key < 127)
                {
                    if(searchFor != null && key == KeyEvent.VK_BACK_SPACE && searchFor.length() > 0)
                        searchFor = searchFor.substring(0, searchFor.length()-1);
                    else
                    {
                        //Si le temps de grâce est écoulé, on réinitialise la chaîne recherchée
                        if(lap + graceTime < now)
                            searchFor = "" + key;
                        //Sinon, on concatène le caractère à la chaîne
                        else
                            searchFor += key;
                    }

                    lap = now;
                }
            }
        };
    }
    
    /**
     * Événément d'action appliquée à notre ComboBox.
     * Met à jour le contenu du composant en fonction de la chaîne recherchée.
     * @return 
     */
    private ActionListener actionListener()
    {
        return (ActionEvent e) ->
        {
            JTextField tf = (JTextField)getEditor().getEditorComponent();
            ComboBoxModel model = getModel();
            String current;

            if(searchFor != null && searchFor.length() > 0)
            {
                //On parcourt le contenu de la ComboBox à la recherche de l'élément recherché
                for(int i = 0  ; i < model.getSize() ; i++) 
                {
                    current = model.getElementAt(i).toString();

                    if(current.toLowerCase().contains(searchFor.toLowerCase())) 
                    {
                        tf.setText(current);
                        tf.setSelectionStart(current.toLowerCase().indexOf(searchFor.toLowerCase()) + searchFor.length() + 1);
                        tf.setSelectionEnd(current.length()); 
                        break;
                    }
                }
            }
        };
    }
}

