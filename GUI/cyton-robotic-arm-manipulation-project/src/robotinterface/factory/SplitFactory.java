
package robotinterface.factory;

import javax.swing.*;
import robotinterface.factory.panel.ListPanel;

/**
 * SplitFactory Class.
 * <p>
 * @author Brian Bailey
 */
public class SplitFactory {

    
    /**
     * Build SplitPanels
     * <p>
     * @param tPane     JTabbedPane.
     * @param lPane     ListPanel
     * @return          JSplitPane.
     */
    public static JSplitPane buildSplitPanels(JTabbedPane tPane, ListPanel lPane) {
                
        JSplitPane jSplitPane1 = new JSplitPane();
        jSplitPane1.setRightComponent(tPane);
        jSplitPane1.setLeftComponent(lPane);        
        return jSplitPane1;
    }
    
    
    /**
     * Build SplitPanels.
     * <p>
     * @param rPane     Right JSplitPane.
     * @param lPane     Left JSplitPane.
     * @return          JSplitPane containing Left and Right JSplitPanes.
     */
    public static JSplitPane buildSplitPanels(JSplitPane rPane, JSplitPane lPane) {
                
        JSplitPane jSplitPane1 = new JSplitPane();
        jSplitPane1.setRightComponent(rPane);
        jSplitPane1.setLeftComponent(lPane);        
        return jSplitPane1;
    }
    
}
