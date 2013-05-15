
package robotinterface.factory.panel;

/**
 * Joint Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class Joint {
     
    private double min, max;  
    private String label, iconDir;  

    
    /**
     * Joint Constructor
     * <p>
     * @param label         Joint's Label.
     * @param iconDir       Joint's Icon Directory.
     * @param min           Joint's Min Value.
     * @param max           Joint's Max Value.
     */
    public Joint(String label, String iconDir, double min, double max) {
        this.min = min;
        this.max = max;
        this.label = label;
        this.iconDir = iconDir;
    }

    
    /**
     * Get Joint's Min Value.
     * <p>
     * @return      Joint's Min Value.
     */
    public double getMin() {
        return min;
    }

    
    /**
     * Get Joint's Max Value.
     * <p>
     * @return      Joint's Max Value.
     */
    public double getMax() {
        return max;
    }

    
    /**
     * Get Joint Label.
     * <p>
     * @return      Joint's Label.
     */
    public String getLabel() {
        return label;
    }

    
    /**
     * Get Joint's Icon Directory.
     * <p>
     * @return      String Representation of Icon Directory.
     */
    public String getIconDir() {
        return iconDir;
    }

}
