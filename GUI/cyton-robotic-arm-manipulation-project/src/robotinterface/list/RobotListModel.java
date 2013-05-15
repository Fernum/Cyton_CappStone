package robotinterface.list;

import java.io.*;
import java.util.*;
import javax.swing.*;
import robotinterface.list.entry.*;

/**
 * RobotListModel Class
 * <p>
 * @param <T> 
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class RobotListModel<T> extends AbstractListModel {

    private final EntryList<Entry> list;

    
    /**
     * RobotListModel Constructor
     * <p>
     * @param l     ArrayList of Entries.
     */
    public RobotListModel(ArrayList l) {
        list = new EntryList<>();
        list.addAll(l);
    }

    
    /**
     * Step Forward in List.
     * <p>
     * @return      Next Entry, Iterating Forwards.
     */
    public Entry stepForward() {
        Entry step = list.stepForward();
        list.incIndex();
        return step;
    }

    
    /**
     * Step Backward in List.
     * <p>
     * @return      Next Entry, Iterating Backwards.
     */
    public Entry stepBackward() {
        Entry step = list.stepBackward();
        list.decIndex();
        return step;
    }

    
    /**
     * Get Index of List.
     * <p>
     * @return      Current Index.
     */
    public int getIndex() {
        return list.getIndex();
    }

    
    /**
     * Set Index of List.
     * <p>
     * @param index     Current Index.
     */
    public void setIndex(int index) {
        list.setIndex(index);
    }

    
    /**
     * Set Index to the index of canceled task.
     */
    public void cancelled() {
        list.decIndex();        
    }
   

    /**
     * Add Entry to List.
     * <p>
     * @param entry     Entry to add.
     */
    public void add(Entry entry) {
        list.add(entry);
    }

    
    /**
     * Set Entry at Index.
     * <p>
     * @param index     Index to set Entry at.
     * @param entry     Entry to set.
     */
    public void set(int index, Entry entry) {
        list.set(index, entry);
    }

    
    /**
     * Add Entry at Index.
     * <p>
     * @param index     Index to add Entry at.
     * @param entry     Entry to add.
     */
    public void addElementAt(int index, Entry entry) {
        list.add(index, entry);
    }

    
    /**
     * Remove Entry at Index.
     * <p>
     * @param index     Index of Entry to Remove.
     */
    public void removeElementAt(int index) {
        list.remove(index);
    }

    
    /**
     * Read Serialized EntryList from File.
     * <p>
     * @param varName       Serialized EntryList to read.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readObject(String varName)
            throws IOException, ClassNotFoundException 
    {
        try (FileInputStream fileIn = new FileInputStream(varName);
                ObjectInputStream in = new ObjectInputStream(fileIn)) 
        {
            EntryList<Entry> e = (EntryList<Entry>) in.readObject();
            list.addAll(e);
        }
    }

    
    /**
     * Write Serialized EntryList to File.
     * <p>
     * @param varName       Serialized EntryList to write.
     * @throws IOException
     */
    public void writeObject(String varName)
            throws IOException
    {
        try (FileOutputStream fileOut = new FileOutputStream(varName);
                ObjectOutputStream out = new ObjectOutputStream(fileOut))
        {
            out.writeObject(list);
        }
    }
        
    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public String toString() {
        return "RobotListModel{" + "list=" + list + '}';
    }
        
}
