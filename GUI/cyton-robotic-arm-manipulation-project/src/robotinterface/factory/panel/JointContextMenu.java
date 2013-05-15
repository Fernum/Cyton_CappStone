
package robotinterface.factory.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * JointContextMenu Class.
 * <p>
 * @author Brian Bailey
 */
public class JointContextMenu extends MouseAdapter {
    
    private JointWrapper jntPanel;

    
    /**
     * JointContextMenu Constructor.
     * <p>
     * @param jntPanel      JointWrapper
     */
    public JointContextMenu(JointWrapper jntPanel) {
        this.jntPanel = jntPanel;
    }

    /**
     * Show JointWrapper ContextMenu.
     * <p>
     * @param c     JointWrapper Component.
     * @param x     JointWrapper X-Axis Value.
     * @param y     JointWrapper Y-Axis Value.
     */
    private void showContextMenu(Component c, int x, int y) {
        Border border = UIManager.getBorder("List.focusCellHighlightBorder");
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                ArrayList<JointPanel> jList = jntPanel.getJList();
                for (JointPanel j : jList) {
                    j.resetSpinners();
                }                
                jntPanel.repaint();
            }
        });
        contextMenu.add(resetItem);
        contextMenu.show(c, x, y);
    }

    
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showContextMenu(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showContextMenu(e.getComponent(), e.getX(), e.getY());
        }
    }
    
}
