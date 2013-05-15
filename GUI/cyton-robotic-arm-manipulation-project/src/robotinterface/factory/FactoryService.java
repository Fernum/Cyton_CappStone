
package robotinterface.factory;

import java.util.*;

/**
 * FactoryService Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class FactoryService {
    
    private List<FactoryListenerImpl> listeners = new ArrayList();

    
    /**
     * Add EventListener.
     * <p>
     * @param listener      FactoryListenerImpl to Add.
     */
    public synchronized void addEventListener(FactoryListenerImpl listener) {
        listeners.add(listener);
    }

    
    /**
     * Remove EventListener.
     * <p>
     * @param listener      FactoryListenerImpl to Remove.
     */
    public synchronized void removeEventListener(FactoryListenerImpl listener) {
        listeners.remove(listener);
    }

    
    /**
     * Fire Event.
     * <p>
     * @param eventData     Event Data.
     */
    private synchronized void fireEvent(String eventData) {
        FactoryEvent event = new FactoryEvent(this, eventData);
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            ((FactoryListenerImpl) i.next()).panelEvent(event);
        }
    }
    
}
