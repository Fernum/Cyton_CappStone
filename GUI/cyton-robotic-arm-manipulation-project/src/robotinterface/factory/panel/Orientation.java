package robotinterface.factory.panel;

import java.util.ArrayList;

/**
 * Orientation Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class Orientation {
    
    private String name;
    private ArrayList<String> labels;    
    private ArrayList<String> toolTips;

    
    /**
     * Orientation Constructor
     * <p>
     * @param name
     * @param labels
     * @param toolTips
     */
    public Orientation(String name, ArrayList labels, ArrayList toolTips) {
        this.name = name;
        this.labels = labels;
        this.toolTips = toolTips;
    }

    
    /**
     * Get Name of Orientation.
     * <p>
     * @return      Name of Orientation.
     */
    public String getName() {
        return name;
    }

    
    /**
     * Get Labels for Orientation.
     * <p>
     * @return      ArrayList of Orientation Labels.
     */
    public ArrayList<String> getLabels() {
        return labels;
    }

    
    /**
     * Get ToolTip for Orientation.
     * <p>
     * @return      ArrayList of Orientation ToolTips.
     */
    public ArrayList<String> getToolTips() {
        return toolTips;
    }
    
}
