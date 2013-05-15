
package robotinterface.factory;

import java.util.*;
import javax.swing.JTabbedPane;
import robotinterface.RobotInterface;
import robotinterface.factory.panel.*;
import robotinterface.list.actions.ListContextMenu;

/**
 * FactoryControl Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class FactoryControl {
    
    private static RobotInterface rIFace;
    private static HashMap<Integer, ListPanel> listPanels;
    private static HashMap<Integer, JTabbedPane> tabbedPanels;
    private static HashMap<Integer, JointWrapper> jointPanels;
    private static HashMap<Integer, OrientationWrapper> orientationPanels;

    
    /**
     * Default Constructor.
     */
    public FactoryControl(){ }
    
    
    /**
     * Build RobotInterface UI.
     * <p>
     * @param robotCnt      Robot Count.
     */
    public static void buildUI(int robotCnt) {
        
        jointPanels = new HashMap<>();
        orientationPanels = new HashMap<>();
        tabbedPanels = new HashMap<>();
        listPanels = new HashMap<>();
        
        for(int x=0;x<robotCnt;x++) {
            
            JTabbedPane jTabbedPane1 = new JTabbedPane();
                        
            JointWrapper joints = JointFactory.buildPanels(x);            
            joints.setToolTipText("Control pose of robotic arm #"+x+".");
            jointPanels.put(x, joints);
                                 
            OrientationWrapper orientation = OrientationFactory.buildPanels();            
            orientation.setToolTipText("Control robot arm #"+x+" position.");
            orientationPanels.put(x, orientation);
            
            jTabbedPane1.addTab("Slider", joints);
            jTabbedPane1.addTab("Orientation", orientation);
            
            tabbedPanels.put(x, jTabbedPane1);           
                       
            ListPanel buildPanels = ListFactory.buildPanels(x);
            listPanels.put(x, buildPanels);
                                    
            ListContextMenu contextMenu = buildPanels.getRobotList().getContextMenu();
            contextMenu.setPanel(jTabbedPane1);
        }
    }
       
    
    /**
     * Get ListPanel List.
     * <p>
     * @return      ArrayList of ListPanels.
     */
    public static ArrayList<ListPanel> getListPanelList() {
        return new ArrayList<>(listPanels.values());
    }
    
    
    /**
     * Get ListPanels.
     * <p>
     * @return      HashMap of ListPanels, Key is Identifier..
     */
    public static HashMap<Integer, ListPanel> getListPanels() {
        return listPanels;
    }
        
    
    /**
     * Get TabbedPanel List.
     * <p>
     * @return      ArrayList of JTabbedPane.
     */
    public static ArrayList<JTabbedPane> getTabbedList() {    
            return new ArrayList<>(tabbedPanels.values());
    }
    
    
    /** 
     * Get TabbedPanels.
     * <p>
     * @return      HashMap of JTabbedPane, Key is Identifier.
     */
    public static HashMap<Integer, JTabbedPane> getTabbedPanels() {
        return tabbedPanels;
    }        
        
    
    /**
     * Get JointPanels.
     * <p>
     * @return      HashMap of JointWrapper, Key is Identifier.
     */
    public HashMap<Integer, JointWrapper> getJointPanels() {
        return jointPanels;
    }      
     
    
    /**
     * Get JointPanel List.
     * <p>
     * @return      ArrayList of JointWrapper.
     */
    public static ArrayList<JointWrapper> getJointPanelList() {
        return new ArrayList<>(jointPanels.values());
    }
    
    
    /**
     * Get Joint Values.
     * <p>
     * @return      ArrayList of Doubles, for Joint Values.
     */
    public ArrayList<Double> getJointValues() {
        ArrayList<Double> aList = new ArrayList<>();
        Collection<JointWrapper> iSet = getJointPanels().values();
        for(JointWrapper c : iSet) {            
            for(JointPanel o : c.getJList()) { aList.add(o.getValue()); }
        }
        return aList;        
    }             
    
    
    /**
     * G etOrientationPanels.
     * <p>
     * @return      HashMap of OrientationWrapper, OrientationWrapperey is Identifier.
     */
    public HashMap<Integer, OrientationWrapper> getOrientationPanels() {
        return orientationPanels;
    }    

    
    /**
     * Get Orientation Values.
     * <p>
     * @return      ArrayList of Doubles, for Orientation Values.
     */
    public ArrayList<Double> getOrientationValues() {
        ArrayList<Double> aList = new ArrayList<>();        
        Collection<OrientationWrapper> iSet = getOrientationPanels().values();
        for(OrientationWrapper c : iSet) {
            for(OrientationPanel o : c.getOList()) { aList.addAll(o.getValues()); }
        }
        return aList;        
    }
        
}
