package robotinterface.list.listeners;

import java.awt.datatransfer.*;
import java.io.IOException;
import javax.swing.TransferHandler;
import robotinterface.list.*;
import robotinterface.list.entry.*;

/**
 * ListTransferHandler Class.
 * <p>
 * @author Brian Bailey
 */
public class ListTransferHandler extends TransferHandler {

    String indexString;
    RobotList list;

    /**
     * ListTransferHandler Constructor.
     * <p>
     * @param list      RobotList for DND Implementation.
     */
    public ListTransferHandler(RobotList list) {
        this.list = list;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        RobotList.DropLocation dl = (RobotList.DropLocation) support.getDropLocation();
        if (dl.getIndex() == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        Transferable transferable = support.getTransferable();

        try {
            indexString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            RobotList.DropLocation dl = (RobotList.DropLocation) support.getDropLocation();

            RobotListModel model = (RobotListModel) list.getModel();
            Entry entry = (Entry) list.getSelectedValue();
            int destIndex = dl.getIndex();

            if (!dl.isInsert()) {
                model.removeElementAt(destIndex);
            }
            model.addElementAt(destIndex, entry);
        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }
        return true;
    }
    
}
