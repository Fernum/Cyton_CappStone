
package robotinterface.util;

import java.util.ArrayList;

/**
 * XMLTagger Class.
 * <p>
 * @author Brian Bailey
 */
public class XMLTagger {
   
    /**
     * Get String of Joints with XML Tags.
     * <p>
     * @param robotID       Identifier of Robot.
     * @param joints        ArrayList of Joints.
     * @return              String of XML Tagged Joints.
     */
    public static String jointTag(int robotID, ArrayList<Double> joints) {
        StringBuilder buff = new StringBuilder();
        
        buff.append("  <Robot ID=\"").append(robotID).append("\">\n");
        for(int x=0;x<joints.size();x++) {
            buff.append("    <Joint ID=\"").append(x).append("\" ")
                .append("Value=\"").append(joints.get(x)).append("\"/>\n");
        }
        buff.append("  </Robot>\n");        
        return buff.toString();                
    }
    
    /**
     * Get String of Joints with XML Tags.
     * <p>
     * @param joints        ArrayList of Joints.
     * @return              String of XML Tagged Joints.
     */
    public static String jointTag( ArrayList<Double> joints) {
        StringBuilder buff = new StringBuilder();
                
        for(int x=0;x<joints.size();x++) {
            buff.append("    <Joint ID=\"").append(x).append("\" ")
                .append("Value=\"").append(joints.get(x)).append("\"/>\n");
        }       
        return buff.toString();                
    }    
}
