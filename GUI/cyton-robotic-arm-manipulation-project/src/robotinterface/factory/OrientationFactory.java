
package robotinterface.factory;

import java.util.*;
import robotinterface.factory.panel.*;

/**
 * OrientationFactory Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class OrientationFactory {
    
    /**
     * ArrayList of Orientations
     */
    static private ArrayList<Orientation> oList = new ArrayList<Orientation>() {{
        add(new Orientation(
                "Orientation",
                new ArrayList<String>() {{
                    add("     X:");
                    add("     Y:");
                    add("     Z:");
                }},
                new ArrayList<String>() {{
                    add("The east/west position of the gripper reletive to the base");
                    add("The north/south position of the gripper reletive to the base");
                    add("The vertical position of the gripper reletive to the base");
                }}
        ));
        add(new Orientation(
                "Position",
                new ArrayList<String>() {{
                    add("Alpha:");
                    add("Beta:");
                    add("Gama:");
                }},
                new ArrayList<String>() {{
                    add("The rotation about the x-axis relitive to the base");
                    add("The rotation about the y-axis relitive to the base");
                    add("The rotation about the z-axis relitive to the base");        
                }}
        ));
    }};
    
    
    /**
     * Build OrientationPanels.
     * <p>
     * @return      ArrayList of OrientationPanel.
     */
    private static ArrayList<OrientationPanel> buildOrientationPanels() {
        ArrayList<OrientationPanel> aList = new ArrayList<>();
        for (Orientation o : oList) {
            OrientationPanel oPanel = OrientationFactory.createPanel(o);
            aList.add(oPanel);            
        }
        return aList;
    }
        
    
    /**
     * Create OrientationPanel.
     * <p>
     * @param orientation       Orientation.
     * @return                  OrientationPanel.
     */
    private static OrientationPanel createPanel(Orientation orientation) {
        OrientationPanel oPanel = OrientationPanel.createJointPanel(orientation);
        return oPanel;
    }

    
    /**
     * Build OrientationWrapper.
     * <p>
     * @return      OrientationWrapper.
     */
    public static OrientationWrapper buildPanels() {
        return OrientationWrapper.buildWrapper(buildOrientationPanels());
    }
    
}
