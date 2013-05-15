
package robotinterface.list.listeners;

import javax.swing.event.*;


/**
 * EntryDataListener Class.
 * <p>
 * @author Brian Bailey
 */
public class EntryDataListener implements ListDataListener {

    @Override
    public void intervalAdded( ListDataEvent e ) {
//        RobotListModel source = (RobotListModel) e.getSource();
//        System.out.println("Added: "+e);
    }

    @Override
    public void intervalRemoved( ListDataEvent e ) {
//         RobotListModel model = (RobotListModel) e.getSource();
//         System.out.println("Remove: "+e);
    }

    @Override
    public void contentsChanged( ListDataEvent e ) {
//         RobotListModel model = (RobotListModel) e.getSource();
//         System.out.println("Changed: "+e);
    }
}
