package robotinterface.list.actions;

import java.awt.event.*;
import javax.swing.*;
import robotinterface.list.*;

/**
 * ListAction Class.
 * <p>
 * @author Brian Bailey
 */
public class ListAction extends MouseAdapter {

    private static final KeyStroke ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    private KeyStroke keyStroke;
    private RobotList list;

    
    /**
     * ListAction Constructor.
     * <p>
     * @param list
     * @param action
     */
    public ListAction(RobotList list, Action action) {
        this(list, action, ENTER);
    }

    
    /**
     * ListAction Constructor.
     * <p>
     * @param list          RobotList.
     * @param action        RobotList's Edit Action.
     * @param keyStroke     "Enter" KeyStroke for List's Action.
     */
    public ListAction(RobotList list, Action action, KeyStroke keyStroke) {
        this.list = list;
        this.keyStroke = keyStroke;
        InputMap im = list.getInputMap();
        im.put(keyStroke, keyStroke);

        setAction(action);
        list.addMouseListener(this);
    }

    
    /**
     * Set List's Action.
     * <p>
     * @param action        Action to be Set.
     */
    private void setAction(Action action) {
        list.getActionMap().put(keyStroke, action);
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Action action = list.getActionMap().get(keyStroke);
            if (action != null) {
                ActionEvent event = new ActionEvent(list, ActionEvent.ACTION_PERFORMED, "");
                action.actionPerformed(event);
            }
        }
    }
    
}
