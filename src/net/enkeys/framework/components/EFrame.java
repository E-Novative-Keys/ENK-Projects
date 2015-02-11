package net.enkeys.framework.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import net.enkeys.framework.listeners.EComponentListener;
import net.enkeys.framework.listeners.EContainerListener;
import net.enkeys.framework.listeners.EFocusListener;
import net.enkeys.framework.listeners.EKeyListener;
import net.enkeys.framework.listeners.EMouseListener;
import net.enkeys.framework.listeners.EWindowListener;
import net.enkeys.framework.utils.EResources;

public abstract class EFrame extends JFrame implements EWindowListener, EMouseListener, EKeyListener, EComponentListener, EContainerListener, EFocusListener
{
    protected final EApplication app;
    protected final String name;
    protected final boolean pack;
    
    protected JMenuBar menuBar = null;
    protected EController content = null;
    
    public EFrame(EApplication app, String title)
    {
        this(app, title, 0, 0);
    }
    
    public EFrame(EApplication app, String title, int width, int height)
    {
        super(title);
        this.app = app;
        this.name = title;
        
        if(width == 0 || height == 0)
            this.pack = true;
        else
        {
            setSize(width, height);
            this.pack = false;
        }
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        menuBar = new JMenuBar();
        initMenu(menuBar);
        setMenu(menuBar);
        
        eventHandler();
    }
    
    protected abstract void initMenu(JMenuBar menuBar);
    
    /**
     * Définit la taille minimale de la fenêtre.
     * @param width
     * @param height 
     */
    public final void setMinimumSize(int width, int height)
    {
        setMinimumSize(new Dimension(width, height));
    }
    
    /**
     * Définit la taille maximale de la fenêtre.
     * @param width
     * @param height 
     */
    public final void setMaximumSize(int width, int height)
    {
        setMaximumSize(new Dimension(width, height));
    }
    
    /**
     * Définit la barre de menu utilisée par la fenêtre.
     * @param menuBar
     */
    public final void setMenu(JMenuBar menuBar)
    {
        setJMenuBar(menuBar);
    }
    
    /**
     * Définit le curseur actif à l'intérieur de la fenêtre.
     * @param cursor 
     */
    @Override
    public final void setCursor(Cursor cursor)
    {
        getGlassPane().setVisible(true);
        getGlassPane().setCursor(cursor);
    }
    
    /**
     * Définit l'icône de la fenêtre.
     * @param path 
     */
    public final void setFavicon(String path)
    {
        setIconImage(EResources.loadImage(path));
    }
    
    /**
     * Fermeture de la fenêtre.
     */
    public void close()
    {
        dispose();
    }
    
    /**
     * Met à jour la vue active de la fenêtre.
     * @param controller
     * @return 
     */
    public final boolean setContent(EController controller)
    {
        onSetContent(controller);
        
        if(controller != null)
        {
            if(content != null)
            {
                content.render(false);
                remove(content.getView());
            }
            
            content = controller;
            add(content.getView());
            content.render(true);
            
            if(pack)
                pack();
            return true;
        }
        
        return false;
    }
    
    /**
     * Renvoie le nom (title) de la fenêtre.
     * @return 
     */
    @Override
    public final String getName()
    {
        return this.name;
    }
    
    /**
     * Renvoie l'application mère de la fenêtre.
     * @return 
     */
    public final EApplication getApp()
    {
        return app;
    }
    
    /**
     * Gestion des événements de la Frame.
     */
    private void eventHandler()
    {
        handleWindow();
        handleKeyboard();
        handleMouse();
        handleComponent();
        handleContainer();
        handleFocus();
    }
    
    /**
     * Gestion des événements de Focus.
     */
    private void handleFocus()
    {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                onFocusGained(fe);
            }

            @Override
            public void focusLost(FocusEvent fe) {
                onFocusLost(fe);
            }
        });
    }
    
    /**
     * Gestion des événements de Container.
     */
    private void handleContainer()
    {
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent ce) {
                onComponentAdded(ce);
            }

            @Override
            public void componentRemoved(ContainerEvent ce) {
                onComponentRemoved(ce);
            }
        });
    }
    
    /**
     * Gestions des événements de Component.
     */
    private void handleComponent()
    {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                onComponentShown(ce);
            }

            @Override
            public void componentResized(ComponentEvent ce) {
                onComponentResized(ce);
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                onComponentMoved(ce);
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
                onComponentHidden(ce);
            }
        });
    }
    
    /**
     * Gestion des événements de Window.
     */
    private void handleWindow()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowOpened(WindowEvent we) {
                onWindowOpened(we);
            }
            
            @Override
            public void windowClosing(WindowEvent we) {
                onWindowClosing(we);
            }

            @Override
            public void windowClosed(WindowEvent we) {
                onWindowClosed(we);
            }

            @Override
            public void windowActivated(WindowEvent we) {
                onWindowActivated(we);
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
                onWindowDeactivated(we);
            }

            @Override
            public void windowIconified(WindowEvent we) {
                onWindowIconified(we);
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
                onWindowDeiconified(we);
            }

            @Override
            public void windowGainedFocus(WindowEvent we) {
                onWindowGainedFocus(we);
            }

            @Override
            public void windowLostFocus(WindowEvent we) {
                onWindowLostFocus(we);
            }

            @Override
            public void windowStateChanged(WindowEvent we) {
                onWindowStateChanged(we);
            }
        });
    }
    
    /**
     * Gestion des événements de Keyboard.
     */
    private void handleKeyboard()
    {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                onKeyPressed(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                onKeyReleased(ke);
            }

            @Override
            public void keyTyped(KeyEvent ke) {
                onKeyTyped(ke);
            }
            
        });
    }
    
    /**
     * Gestion des événements de Mouse.
     */
    private void handleMouse()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                onMouseEntered(me);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                onMouseExited(me);
            }
            
            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2)
                    onMouseDoubleClicked(me);
                else
                    onMouseClicked(me);
            }

            @Override
            public void mousePressed(MouseEvent me) {
                onMousePressed(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                onMouseReleased(me);
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                onMouseMoved(me);
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                onMouseDragged(me);
            }
        });
        
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                onMouseWheel(mwe);
            }
        });
    }
    
    /**
     * Gestion des événement de Content.
     * @param controller 
     */
    public void onSetContent(EController controller){}
}
