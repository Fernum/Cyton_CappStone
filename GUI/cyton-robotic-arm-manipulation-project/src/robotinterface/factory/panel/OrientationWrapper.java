
package robotinterface.factory.panel;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * OrientationWrapper Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class OrientationWrapper extends JPanel {
    
    private ArrayList<OrientationPanel> aList;
    private OrientationContextMenu contextMenu;

    
    /**
     * OrientationWrapper Constructor.
     * <p>
     * @param aList     ArrayList of OrientationPanels.
     */
    private OrientationWrapper(ArrayList<OrientationPanel> aList) {
       this.aList = new ArrayList<>();
       this.aList.addAll(aList);
       setBackground(new Color(184, 207, 229));
       
       /* Context Menu */
       this.contextMenu = new OrientationContextMenu(this);
       this.addMouseListener(contextMenu);
    }
    
    
    /**
     * Build OrientationWrapper.
     * <p>
     * @param aList     ArrayList of OrientationPanels.
     * @return          OrientationWrapper.
     */
    public static OrientationWrapper buildWrapper(ArrayList<OrientationPanel> aList) {
                        
        OrientationWrapper panel = new OrientationWrapper(aList);
        
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                .addComponent(aList.get(1), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addComponent(aList.get(0), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                .addContainerGap()));
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                .addContainerGap()
                .addComponent(aList.get(0), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(aList.get(1), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addContainerGap(110, Short.MAX_VALUE)));
        panel.setLayout(gl_panel);
        
        return panel;        
    }    

    
    /**
     * Get OrientationPanel List.
     * <p>
     * @return      ArrayList of OrientationPanels
     */
    public ArrayList<OrientationPanel> getOList() {
        return aList;
    }
  
    
    /**
     * Get Joint Values, for Orientation.
     * <p>
     * @return      ArrayList of Double Values
     */
    public ArrayList<Double> getJointValues() {
        ArrayList<Double> buff = new ArrayList<>();
        for(OrientationPanel o: aList) { buff.addAll(o.getValues()); }
        return buff;
    }  

    
    @Override
    public String toString() {
        return "OrientationWrapper{" + "aList=" + aList + '}';
    }    
    
}
