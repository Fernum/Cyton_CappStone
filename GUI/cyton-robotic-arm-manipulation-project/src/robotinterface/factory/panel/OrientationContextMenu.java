
package robotinterface.factory.panel;

import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

/**
 * OrientationContextMenu Class.
 * <p>
 * @author Brian Bailey
 */
public class OrientationContextMenu extends MouseAdapter {

    private OrientationWrapper oPanel;    
    private JointWrapper joints;

    /**
     * OrientationContextMenu Constructor
     * <p>
     * @param oPanel
     */
    public OrientationContextMenu(OrientationWrapper oPanel) {
        this.oPanel = oPanel;
    }

    /**
     * Show Context Menu.
     * <p>
     * @param c     OrientationWrapper Component
     * @param x     OrientationWrapper X-axis Value.
     * @param y     OrientationWrapper Y-axis Value.
     */
    private void showContextMenu(Component c, int x, int y) {
        Border border = UIManager.getBorder("List.focusCellHighlightBorder");
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<OrientationPanel> oList = oPanel.getOList();
               for (OrientationPanel o : oList) {
                   o.resetSpinners();
               }                
                oPanel.repaint();
            }
        });
        contextMenu.add(resetItem);
        contextMenu.show(c, x, y);
    }

    
    /**
     * Set Joint JointWrapper
     * <p>
     * @param joints        JointWrapper
     */
    public void setJointPanel(JointWrapper joints) {
        this.joints=joints;
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
