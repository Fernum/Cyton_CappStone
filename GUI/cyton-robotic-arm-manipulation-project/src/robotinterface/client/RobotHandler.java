
package robotinterface.client;

import robotinterface.RobotInterface;

/**
 * RobotHandler Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class RobotHandler implements RobotCallback {
    
    private RobotInterface rIface;
    //List<RobotInterface> rLists = new ArrayList<>();

    
    /**
     * RobotHandler Default Constructor.
     */
    public RobotHandler() { }
    
    
    /**
     * Register RobotInterface.
     * <p>
     * @param rIface        RobotInterface to register.
     */
    public void registerIFace(RobotInterface rIface) {
        this.rIface = rIface;
    }

    
    /**
     * UnRegister RobotInterface.
     */
    public void unregisterIFace() {
        this.rIface = null;
    }
       
    
    /**
     * Received Return Message from Server.
     * <p>
     * @param buffer        Message from Server.
     */
    @Override
    public void gotMessage(String buffer)
    {
        // TODO: Allow Error Checking for Resending List Entry
        //for (RobotInterface l : rLists) { l.gotMessage(buffer); }
        rIface.gotMessage(buffer);
    }    
        
    //public void registerList(RobotList rList) {
    //    rLists.add(rList);
    //}
    
    //public void unregisterList(RobotList rList) {
    //    rLists.remove(rList);
    //}
    
}
