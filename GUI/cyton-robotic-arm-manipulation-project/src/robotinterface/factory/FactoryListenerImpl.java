
package robotinterface.factory;

/**
 * FactoryListenerImpl interface.
 * <p>
 * @author Brian Bailey
 */
public interface FactoryListenerImpl {
    
    /**
     * New Panel Event
     * <p>
     * @param aEvent        FactoryEvent.
     */
    public void panelEvent(FactoryEvent aEvent);
    
    
    /**
     * Send Event.
     * <p>
     * @param aEvent        FactoryEvent.
     */
    public void sendEvent(FactoryEvent aEvent);
}
