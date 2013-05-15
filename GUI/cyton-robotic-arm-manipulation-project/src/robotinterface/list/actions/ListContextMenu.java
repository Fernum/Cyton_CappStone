package robotinterface.list.actions;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import robotinterface.RobotInterface;
import robotinterface.factory.panel.JointWrapper;
import robotinterface.factory.panel.OrientationWrapper;
import robotinterface.list.*;
import robotinterface.list.entry.*;

/**
 * ListContextMenu Class.
 * <p>
 * @author Brian Bailey
 */
public class ListContextMenu extends MouseAdapter {

    private RobotList list;
    private JTabbedPane tabbedPanel;
    
    /**
     * ListContextMenu Constructor
     * <p>
     * @param list      RobotList
     */
    public ListContextMenu(RobotList list) {
        this.list = list;
    }
    
    
    /**
     * Show Context Menu for RobotList.
     * <p>
     * @param c     RobotList's Component
     * @param x     RobotList's X-axis Value.
     * @param y     RobotList's Y-axis Value.
     */
    private void showContextMenu(Component c, int x, int y) {
        Border border = UIManager.getBorder("List.focusCellHighlightBorder");
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem addItem = new JMenuItem("Add");
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotListModel model = (RobotListModel) list.getModel();
                Point mousePosition = list.getMousePosition();
                int mouseIndex;
                if (mousePosition == null) { mouseIndex = 0; }
                else {mouseIndex = list.locationToIndex(mousePosition); }
                int lastIndex = model.getSize();

                if ((lastIndex <= mouseIndex + 1)) {          
                    model.add(new Entry(getValues()));
                } else {
                    model.addElementAt(mouseIndex, new Entry(getValues())); 
                }
                list.repaint();
            }
        });

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotListModel model = (RobotListModel) list.getModel();
                if (!list.isSelectionEmpty()) {
                    int index = list.getSelectedIndex();
                    model.removeElementAt(index);
                }
                list.repaint();
            }
        });
       
        JMenuItem importItem = new JMenuItem("Import");
        importItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotListModel model = (RobotListModel) list.getModel();
                
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(RobotInterface.getRobotInterface());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try { model.readObject(file.getAbsolutePath()); } 
                    catch (IOException | ClassNotFoundException ex) { }
                }                
                list.repaint();
            }
        });
        
        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotListModel model = (RobotListModel) list.getModel();
                
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(RobotInterface.getRobotInterface());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {  model.writeObject(file.getAbsolutePath()); }
                    catch (IOException ex) { }                    
                }
            }
        });
        
        contextMenu.add(addItem);
        contextMenu.add(deleteItem);
        contextMenu.addSeparator();
        contextMenu.add(importItem);
        contextMenu.add(exportItem);
        contextMenu.show(c, x, y);
    }

   
    /**
     * Set JTabbedPane.
     * <p>
     * @param tabbedPanel       JTabbedPane.
     */
    public void setPanel(JTabbedPane tabbedPanel) {
        this.tabbedPanel = tabbedPanel;
    }
       
    
    /**
     * Get Values from JTabbedPane.
     * <p>
     * @return      ArrayList<Double> of currently selected JTabbedPane.
     */
    private ArrayList<Double> getValues() {
        ArrayList<Double> jointValues = null;
        Component sComponent = tabbedPanel.getSelectedComponent();
        
        if (sComponent instanceof JointWrapper) {
            jointValues = ((JointWrapper) sComponent).getJointValues();
        }
        if (sComponent instanceof OrientationWrapper) {
            jointValues = ((OrientationWrapper) sComponent).getJointValues();
        }
        // TODO: Throw Exception
        return jointValues;
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
