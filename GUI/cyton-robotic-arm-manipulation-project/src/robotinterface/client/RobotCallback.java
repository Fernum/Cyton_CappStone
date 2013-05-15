package robotinterface.client;

/**
 * RobotCallback interface.
 * <p>
 * @author Brian Bailey
 */
public interface RobotCallback  {    
    /**
     * Received Return Message from Server.
     * <p>
     * @param buffer        Message from Server.
     */
    public void gotMessage(String buffer);
}
