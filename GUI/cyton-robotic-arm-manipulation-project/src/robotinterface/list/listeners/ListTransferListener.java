
package robotinterface.list.listeners;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import robotinterface.list.*;
import robotinterface.list.entry.*;

/**
 * ListTransferListener Class.
 * <p>
 * @author Brian Bailey
 */
public class ListTransferListener implements DragSourceListener, DragGestureListener{

    private StringSelection transferable;
    private DragGestureRecognizer dgr;
    private RobotList list;
    private DragSource ds;    
    private Entry entry;

    
    /**
     * ListTransferListener Constructor.
     * <p>
     * @param list      RobotList for DND Implementation.
     */
    public ListTransferListener( RobotList list) {
        this.list = list;
        setGestureRecognizer();
    }

    
    /**
     * Set Gesture Recognizer.
     */
    private void setGestureRecognizer() {
        ds = new DragSource();
        dgr = ds.createDefaultDragGestureRecognizer(list,DnDConstants.ACTION_MOVE, this);
    }
    
     
    /**
     * Get Entry that was Dragged And Dropped.
     * <p>
     * @return      Entry that was Dragged And Dropped.
     */
    public Entry getEntry()
    {
        return entry;
    }
    
    
    @Override
    public void dragEnter( DragSourceDragEvent dsde ) { }

    @Override
    public void dragOver( DragSourceDragEvent dsde ) { }

    @Override
    public void dropActionChanged( DragSourceDragEvent dsde ) { }

    @Override
    public void dragExit( DragSourceEvent dse ) { }

    @Override
    public void dragDropEnd( DragSourceDropEvent dsde ) {
        if (dsde.getDropSuccess()) { /* System.out.println( "Succeeded" ); */ }
        else { /* System.out.println( "Failed" ); */ }
    }

    @Override
    public void dragGestureRecognized( DragGestureEvent dge ) {
        int index = list.getSelectedIndex();
        entry = (Entry)list.getModel().getElementAt(index);

        transferable = new StringSelection( entry.getTitle() );
        ds.startDrag( dge, DragSource.DefaultCopyDrop, transferable, this );
    }

}
