
package robotinterface.list.entry;

import java.io.Serializable;
import java.util.*;

/**
 * EntryList
 * <p>
 * @param <Entry> 
 * @author Brian Bailey
 */
public class EntryList<Entry> extends LinkedList<Entry> implements Serializable {

    private int index = 0;

    /**
     * Step Forward in List.
     * <p>
     * @return      Current Index.
     */
    public Entry stepForward()
    {
        return get(index);
    }

    /**
     * Step Forward in List.
     * <p>
     * @return      Current Index.
     */
    public Entry stepBackward()
    {
        return get(index);
    }

    /**
     * Set Index of List.
     * <p>
     * @param i     Value of index to be set.
     * @return      Current Index.
     */
    public int setIndex(int i)
    {
        return index = i;
    }

    /**
     * Get Index.
     * <p>
     * @return      Current Index.
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Increment Index.
     */
    public void incIndex()
    {
        if ( index == size()-1) { index = 0; }
        else { index++; }
    }

    /**
     * Decrement Index.
     */
    public void decIndex()
    {
        if ( index == 0) { index = size()-1; }
        else { index--; }
    }
}
