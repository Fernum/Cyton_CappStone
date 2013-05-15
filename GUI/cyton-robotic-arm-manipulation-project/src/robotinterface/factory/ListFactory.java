
package robotinterface.factory;

import robotinterface.factory.panel.ListPanel;
import robotinterface.factory.panel.ListWrapper;

/**
 * ListFactory Class.
 * <p>
 * @author Brian Bailey
 */
public class ListFactory {
   
    
    /**
     * Build ListPanel.
     * <p>
     * @param id        ListPanel Identifier.
     * @return          ListPanel.
     */
    public static ListPanel buildPanels(int id) {
        ListPanel buildWrapper = ListWrapper.buildWrapper(id);        
        return buildWrapper;
    }
    
}
