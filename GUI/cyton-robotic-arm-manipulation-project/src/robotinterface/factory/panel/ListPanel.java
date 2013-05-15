package robotinterface.factory.panel;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import robotinterface.list.*;
import robotinterface.list.entry.Entry;
import robotinterface.list.listeners.*;

/**
 * ListPanel Class. <p>
 *
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class ListPanel extends JPanel {

    private RobotList robotList;
    private ListWrapper lWrapper;

    /**
     * ListPanel Consructor. <p>
     *
     * @param id Identifier for ListPanel.
     * @param lWrapper ListWrapper for ListPanel.
     */
    public ListPanel(int id, ListWrapper lWrapper) {

        this.lWrapper = lWrapper;
        RobotListModel robotListModel;
        ListTransferListener listTransferListener;

        JLabel listLabel = new JLabel("Positions List");
        add(listLabel);

        /* Scroll Panel for JList */
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        ArrayList<Entry> listData = new ArrayList<>();
        ArrayList<Double> dlist = new ArrayList<>(8);
        for (int data = 0; data < 8; data++) {
            dlist.add(0.0);
        }
        Entry home = new Entry(dlist);
        /* RobotList and ListModel */
        robotListModel = new RobotListModel(listData);
        robotListModel.add(home);
        robotList = new RobotList(id, robotListModel);

        robotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        robotList.setSelectionForeground(Color.LIGHT_GRAY);
        robotList.setSelectionBackground(Color.DARK_GRAY);
        robotList.setFixedCellHeight(20);
        robotList.setSelectedIndex(0);

        /* Renderer for RobotList Cells */
        RobotCellRenderer robotCellRenderer = new RobotCellRenderer();
        robotList.setCellRenderer(robotCellRenderer);

        /* EntryList Drag-N-Drop */
        robotList.setDragEnabled(true);
        robotList.setDropMode(DropMode.ON_OR_INSERT);
        robotList.setTransferHandler(new ListTransferHandler(robotList));
        robotList.setListDragListener(new ListTransferListener(robotList));

        scrollPane.setViewportView(robotList);
        add(scrollPane);
    }

    /**
     * Get RobotList <p>
     *
     * @return RobotList.
     */
    public RobotList getRobotList() {
        return robotList;
    }

    /**
     * Toggle Iterating List. <p>
     *
     * @param runState Status of List Iteration.
     */
    public void toogleRunning(boolean runState) {
        boolean toogleRunning = getRobotList().toogleRunning(runState);
        lWrapper.toogleRunning(toogleRunning);
    }

    @Override
    public String toString() {
        return "ListPanel{" + "robotList=" + robotList + '}';
    }
}
