
package robotinterface.factory;

import java.util.*;

/**
 * FactoryEvent Class.
 * <p>
 * @author Brian Bailey
 */
public class FactoryEvent extends EventObject {
    
    private String eventData;
    
    /**
     * FactoryEvent Constructor.
     * <p>
     * @param source        Event Source.
     * @param eventData     Event Data.
     */
    public FactoryEvent(Object source,String eventData) {
        super(source);
        this.eventData =eventData;
    }

    /**
     * Get Event Data.
     * <p>
     * @return      Event Data.
     */
    public String getEventData() {
        return eventData;
    }
    
    
}
