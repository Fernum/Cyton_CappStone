
package robotinterface.list.entry;

import java.util.concurrent.Callable;
import robotinterface.client.*;

/**
 * ListTask Class.
 * <p>
 * @author Brian Bailey
 */
public class ListTask implements Callable<String> {

    private Entry entry;
    
    
    /**
     * ListTask Constructor.
     * <p>
     * @param e     Entry to Process.
     */
    public ListTask(Entry e) {
        entry = e;
    }

    
    /**
     * Process Entry and Send Data to Server
     * <p>
     * @param entry     Entry to Process.
     * @return          Title of Entry that was Processed.
     */
    private String processEntry(Entry entry) {        
        RobotClient.sendMessage( entry.getTitle() );
        return entry.getTitle();
    }
    
    
    @Override
    public String call() throws Exception {
        return processEntry(entry);
    }

}
