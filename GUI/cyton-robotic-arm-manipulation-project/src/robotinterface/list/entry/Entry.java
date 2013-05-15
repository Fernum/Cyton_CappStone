
package robotinterface.list.entry;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import robotinterface.util.XMLTagger;

/**
 * Entry Class.
 * <p>
 * @author Brian Bailey
 */
public class Entry implements Serializable {

    private String title;
    private String toolTip;
    private ImageIcon image;
    private String imagePath;
    private ArrayList<Double> joints;    
    
    
    /**
     * Entry Constructor.
     * <p>
     * @param t     Entry's Title.
     */
    public Entry(String t) {       
        title = t;
        setToolTip(t);
        joints = new ArrayList<>();
        stringToJoints(title);
    }
    
    
    /**
     * Entry Constructor.
     * <p>
     * @param joints        ArrayList of Doubles, Entry's Joint Values.
     */
    public Entry(ArrayList<Double> joints) {        
        title = joints.toString();
        setToolTip(joints.toString());
        this.joints = joints;
    }
    
    
    /**
     * Entry Constructor.
     * <p>
     * @param title         Entry's Title.
     * @param imagePath     Path to Entry's Image.
     */
    public Entry(String title, String imagePath) {        
        this.title = title;
        this.imagePath = imagePath;
        joints = new ArrayList<>();
        stringToJoints(title);
    }

    
    /**
     * Get Entry's Title.
     * <p>
     * @return      Entry's Title.
     */
    public String getTitle() {
        return title;
    }

    
    /**
     * Get Entry's Image.
     * <p>
     * @return      Entry's Image.
     */
    public ImageIcon getImage() {
        if (image == null) {
            image = new ImageIcon(imagePath);
        }
        return image;
    }

    
    /**
     * Get Entry's ToolTip.
     * <p>
     * @return      Entry's ToolTip.
     */
    public String getToolTip() {
        return toolTip;
    }

    
    /**
     * Set ToolTip of Entry
     * <p>
     * @param tTip      ToolTip for Entry to set.
     */
    private void setToolTip(String tTip) {
        this.toolTip = "<html>"+ "Item" +"<br>"+tTip+"</html>";
    }
    
    
    /**
     * Get XML String representation of Entry.
     * <p>
     * @return      XML String of Entry.
     */
    public String xmlString() {
        return XMLTagger.jointTag(joints);
    }
    
    
    /**
     * Set Joint Values from String Representation.
     * <p>
     * @param s     String Representation of Joint Values.
     */
    private void stringToJoints(String s) {
        String delimiter = ",";
        s = s.replaceAll("[\\[\\]]", "");
        String[] tok = s.split(delimiter);
        for (String t : tok) { 
            System.out.println(t);
            joints.add(Double.valueOf(t)); 
        }
    }
    
    
    @Override
    public String toString() {        
        return title;
    }

}
