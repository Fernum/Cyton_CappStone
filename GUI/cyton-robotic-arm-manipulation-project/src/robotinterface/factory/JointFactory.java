
package robotinterface.factory;

import java.util.ArrayList;
import robotinterface.factory.panel.Joint;
import robotinterface.factory.panel.JointPanel;
import robotinterface.factory.panel.JointWrapper;

/**
 * JointFactory Class.
 * <p>
 * @author Brian Bailey
 */
public class JointFactory {
    
    static private ArrayList<Joint> jList = new ArrayList<Joint>() {{
        add(new Joint("Joint 1","images/joint1.jpg",-105.0, 105.0));
        add(new Joint("Joint 2","images/joint2.jpg",-95.0, 95.0));
        add(new Joint("Joint 3","images/joint3.jpg",-105.0, 105.0));
        add(new Joint("Joint 4","images/joint4.jpg",-95.0, 95.0));
        add(new Joint("Joint 5","images/joint5.jpg",-105.0, 105.0));
        add(new Joint("Joint 6","images/joint6.jpg",-95.0, 95.0));
        add(new Joint("Joint 7","images/joint7.jpg",-95.0, 95.0));
        add(new Joint("Gripper","images/gripper.jpg",-8.0, 8.0));
    }};

   
    /**
     * Build JointPanels.
     * <p>
     * @return      ArrayList of JointPanels.
     */
    static private ArrayList<JointPanel> buildJointPanels() {
        ArrayList<JointPanel> aList = new ArrayList<>();
        for (Joint j : jList) {
            aList.add(JointFactory.createPanel(j));
        }
        return aList;
    }
    
    /**
     * Create JointPanel.
     * <p>
     * @param joint     Joint.
     * @return          JointPanel.
     */
    static private JointPanel createPanel(Joint joint) { 
        JointPanel createJointPanel = JointPanel.createJointPanel(joint);        
        return createJointPanel;        
    }

    
    /**
     * Build JointWrapper.
     * <p>
     * @param id        Panel Identifier.
     * @return          JointWrapper.
     */
    static public JointWrapper buildPanels(int id) {
        return JointWrapper.buildWrapper(id, buildJointPanels());        
    }
    
}
