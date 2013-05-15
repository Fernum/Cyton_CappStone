package robotinterface.list;

import java.awt.event.MouseEvent;
import java.io.*;
import javax.swing.*;
import robotinterface.list.actions.*;
import robotinterface.list.entry.*;
import robotinterface.list.listeners.*;


/**
 * RobotList Class
 * <p>
 * @param <T> 
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class RobotList<T> extends JList {

    private static final boolean VERBOSE = false;    
    //private ExecutorService executor = Executors.newFixedThreadPool(2);    
    
    /**
     * Status of list entry execution.
     */
    public boolean isRunning = false;
    
    /**
     * Direction for iterating the list.
     */
    public boolean isForward = true;
    
    private ListTransferListener listTransferListener;
    private ListContextMenu contextMenu;
    private EditListAction editAction;
    private RobotListModel listModel;
    private ListAction listAction;

    private int id;

    /**
     * RobotList Constructor.
     */
    //public RobotList() {
    //    super();
    //}

    /**
     * RobotList Constructor.
     * <p>
     * @param id            Identifier of List.
     * @param listModel     RobotListModel.
     */
    public RobotList(int id, RobotListModel listModel) {
        super();
        this.id = id;
        
        /* List Cell Edit Action */
        this.editAction = new EditListAction();
        this.listAction = new ListAction(this, editAction);

        /* Context Menu */
        this.contextMenu = new ListContextMenu(this);
        this.addMouseListener(contextMenu);

        /* Custom List Model  */
        this.listModel = listModel;
        this.setModel(listModel);
    }

    
    /**
     * Get RobotList Identifier.
     * <p>
     * @return      Identifier of RobotList.
     */
    public int getId() {
        return id;
    }

    
    /**
     * Get Context Menu for RobotList.
     * <p>
     * @return      ListContextMenu.
     */
    public ListContextMenu getContextMenu() {
        return contextMenu;
    }   
    
    
    /**
     * Set Index of RobotList.
     * <p>
     * @param index     Index to be Set.
     */
    public void setIndex(int index) {
        listModel.setIndex(index);
    }

    
    /**
     * Get Index of RobotList.
     * <p>
     * @return      Current Index of RobotList.
     */
    public int getIndex() {
        return listModel.getIndex();
    }

    
    /**
     * Get Element at given Index, in RobotList.
     * <p>
     * @param index     Index of Entry to retrieve.
     * @return          Entry at Given Index.
     */
    public Entry getElementAt(int index) {
        return (Entry) listModel.getElementAt(index);
    }


    /**
     * Set listTransferListener.
     * <p>
     * @param listTransferListener      Transfer Listener for Lists DND.
     */
    public void setListDragListener(ListTransferListener listTransferListener) {
        this.listTransferListener = listTransferListener;
    }

    /**
     * Read Serialized EntryList from File.
     * <p>
     * @param varName       Serialized EntryList to read.
     */
    public void readObject(String varName) {
        try { listModel.readObject(varName); } 
        catch (ClassNotFoundException | IOException ex) { }
    }

    
    /**
     * Write Serialized EntryList to File.
     * <p>
     * @param varName       Serialized EntryList to write.
     */
    public void writeObject(String varName) {
        try { listModel.writeObject(varName); } 
        catch (IOException ex) { }
    }

    
    /**
     * Step Forward in List.
     * <p>
     * @return      Next Entry, Iterating Forwards.
     */
    public Entry stepForward() {        
        Entry aEntry = listModel.stepForward();
        setSelectedValue(aEntry, true);        
        setSelectedIndex(listModel.getIndex());
        
        if (VERBOSE) { System.err.println("RobotList.stepForward() Entry: " + aEntry); }
        return aEntry;
    }

    
    /**
     * Step Backward in List.
     * <p>
     * @return      Next Entry, Iterating Backwards.
     */
    public Entry stepBackward() {
        Entry aEntry = listModel.stepBackward();
        setSelectedValue(aEntry, true);
        setSelectedIndex(listModel.getIndex());
        
        if (VERBOSE) { System.err.println("RobotList.stepBackward() Entry: " + aEntry); }
        return aEntry;
    }        
        
    
    /**
     * Cancel Iterating List.
     */
    public void cancelled() {
        listModel.cancelled();  
        setSelectedIndex( listModel.getIndex() ); 
        //setSelectedIndex( 0 );        
    }

    
    /**
     * Toggle Iterating LIST On/Off.
     * <p>
     * @param runState      Status of Desired List Iterating.
     * @return              Current Status of List Iteration.
     */
    public boolean toogleRunning(boolean runState) {
        if( isRunning != true && runState==true) { isRunning=runState; }
        else if(isRunning != false && runState==false){ isRunning=runState; }
        return isRunning;
    }    

    
    /**
     * Determine if List is being Iterated.
     * <p>
     * @return      Status of list iteration
     */
    public boolean isRunning() {
        return isRunning;
    }

    
    /**
     * Set Status of list iteration, for executing commands.
     * <p>
     * @param isRunning     Status to be set.
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    @Override
    public String getToolTipText(MouseEvent event) {
        int index = locationToIndex(event.getPoint());
        return getElementAt(index).getToolTip();
    }
    
    @Override
    public String toString() {
        return "RobotList{" + "listModel=" + listModel.toString() + '}';
    }

}
