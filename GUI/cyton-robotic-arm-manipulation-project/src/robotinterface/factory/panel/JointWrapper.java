
package robotinterface.factory.panel;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * JointWrapper Class.
 * <p>
 * @author Brian Bailey
 */
public class JointWrapper extends JPanel {
    
    private int id;
    private JointContextMenu contextMenu;
    private ArrayList<JointPanel> jointList;    
    
    
    /**
     * JointWrapper Constructor
     * <p>
     * @param id            JointWrapper Identifier.
     * @param jointList     ArrayList of JointPanels.
     */
    public JointWrapper(int id, ArrayList<JointPanel> jointList) {
        this.id = id;
        this.jointList = new ArrayList<>();
        this.jointList.addAll(jointList);
        setBackground(new Color(184, 207, 229));
                
        /* Context Menu */
        this.contextMenu = new JointContextMenu(this);
        this.addMouseListener(contextMenu);
    }

    
    /**
     * Build JointWrapper.
     * <p>
     * @param id        JointWrapper Identifier.
     * @param list      ArrayList of JointPanels.
     * @return          JointWrapper.
     */
    public static JointWrapper buildWrapper(int id, ArrayList<JointPanel> list) {
        
        JointWrapper panel = new JointWrapper(id, list);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                .addComponent(list.get(7), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(6), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(5), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(4), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(3), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(2), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(1), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(list.get(0), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                .addContainerGap()));
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                .addContainerGap()
                .addComponent(list.get(0), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(1), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(2), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(3), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(4), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(5), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(6), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(list.get(7), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE)));
        panel.setLayout(gl_panel);
        return panel;        
    }
    
    
    /**
     * Get ArrayList of JointPanels.
     * <p>
     * @return      ArrayList of JointPanels.
     */
    public ArrayList<JointPanel> getJList() {
        return jointList;
    } 
         
    
    /**
     * Get Joint Values.
     * <p>
     * @return      ArrayList of Joint Values.
     */
    public ArrayList<Double> getJointValues() {       
        ArrayList<Double> buff = new ArrayList<>();
        for(JointPanel j: jointList) { buff.add(j.getValue()); }
        return buff;
    } 
    
}
