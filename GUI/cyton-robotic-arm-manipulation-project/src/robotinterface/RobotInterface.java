package robotinterface;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import robotinterface.client.RobotClient;
import robotinterface.client.RobotHandler;
import robotinterface.factory.FactoryControl;
import robotinterface.factory.SplitFactory;
import robotinterface.factory.panel.JointWrapper;
import robotinterface.factory.panel.ListPanel;
import robotinterface.factory.panel.OrientationWrapper;
import robotinterface.list.RobotList;
import robotinterface.list.entry.Entry;
import robotinterface.menu.MenuBar;
import robotinterface.util.XMLTagger;

/**
 * RobotInterface Class. <p>
 *
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class RobotInterface extends JPanel {

    private static final int robotCnt = 1;
    private static final boolean VERBOSE = false;
    private static boolean xmlTagging = false;
    private static RobotInterface robotInterface;
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private ArrayList<RobotList> robots = new ArrayList<>();
   
    /**
     * Status of list entry execution.
     */
    public boolean isRunning = false;
    
    /**
     * Direction for iterating the list.
     */    
    public boolean isForward = true;
    
    /**
     * ArrayList of JSplitPane (RobotList Control Panels).
     */
    public ArrayList<JSplitPane> sPanels;

    
    /**
     * Get Instance of RobotInterface.
     * <p>
     * @return robotInterface
     */
    public static RobotInterface getRobotInterface() {
        return robotInterface;
    }

    
    /**
     * Set Instance of RobotInterface.
     * <p>
     * @param robotInterface        RobotInterface Instance to Set.
     */
    public static void setRobotInterface(RobotInterface robotInterface) {
        RobotInterface.robotInterface = robotInterface;
    }

    
    /**
     * Set Status of XML Tagging.
     */
    public static void setXMLtagging() {
        xmlTagging = !xmlTagging;
    }

    
    /**
     * RobotInterface Constructor.
     * <p>
     * @param robotCnt      Robots to Control.
     */
    private RobotInterface(int robotCnt) {

        sPanels = new ArrayList<>();
        FactoryControl.buildUI(robotCnt);
        ArrayList<JTabbedPane> tabbedList = FactoryControl.getTabbedList();
        ArrayList<ListPanel> listPanelList = FactoryControl.getListPanelList();

        for (int x = 0; x < robotCnt; x++) {
            JTabbedPane tPane = tabbedList.get(x);
            ListPanel lPane = listPanelList.get(x);
            JSplitPane splitPanel = SplitFactory.buildSplitPanels(tPane, lPane);
            sPanels.add(splitPanel);
        }

        JSplitPane splitPanel = new JSplitPane();
        if (robotCnt == 1) {
            splitPanel = sPanels.get(0);
        } else if (robotCnt == 2) {
            splitPanel = SplitFactory.buildSplitPanels(sPanels.get(1), sPanels.get(0));
        }

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPanel, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(splitPanel, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap()));
        setLayout(layout);
    }
        

    /**
     * Start Iteration of Lists.
     * <p>
     * @param robotList     List to Stop Iterating
     */
    public void start(RobotList robotList) {
        isRunning = true;
        robots.add(robotList);
        robotList.toogleRunning(isRunning);
        iterateLists();
    }

    
    /**
     * Start Iteration of Lists.
     */
    public void start() {
        isRunning = true;
        for (JSplitPane sPan : sPanels) {
            ListPanel lPanel = (ListPanel) sPan.getLeftComponent();
            lPanel.toogleRunning(isRunning);
            RobotList rList = lPanel.getRobotList();
            robots.add(rList);
        }
        iterateLists();
    }

    
    /**
     * Stop Iteration of Lists.
     * <p>
     * @param robotList     List to Stop Iterating.
     */
    public void stop(RobotList robotList) {
        isRunning = false;
        robotList.cancelled();
        robots.remove(robotList);
        robotList.toogleRunning(isRunning);
    }

    
    /**
     * Stop Iteration of Lists.
     */
    public void stop() {
        isRunning = false;
        for (JSplitPane sPan : sPanels) {
            ListPanel lPanel = (ListPanel) sPan.getLeftComponent();
            RobotList rList = lPanel.getRobotList();
            lPanel.toogleRunning(isRunning);
            robots.remove(rList);
            rList.cancelled();
        }
    }

    
    /**
     * Received Return Message from Server.
     * <p>
     * @param buffer        Message Sent by Server.
     */
    public void gotMessage(String buffer) {
        if (VERBOSE) {
            System.err.println("RobotList.gotMessage(): " + buffer);
        }
        if (isRunning) {
            iterateLists();
        }
    }

    
    /**
     * Iterate Through List.
     */
    public void iterateLists() {
        if (isRunning) {
            executor.execute(new Runnable() {
                @Override public void run() {
                    processLists();
                }
            });
        }
    }

    
    /**
     * Process Entries in list, iterating one Entry at a time.
     */
    private void processLists() {
        HashMap<Integer, Entry> entryMap = new HashMap<>();
        for (RobotList r : robots) {
            if (isForward) {
                entryMap.put(r.getId(), r.stepForward());
            } else {
                entryMap.put(r.getId(), r.stepBackward());
            }
        }
        try { Thread.sleep(1000); } 
        catch (InterruptedException nothing) { }
        executeTask(entryMap);
    }

    
    /**
     * Execute sending Data Set of Double values to server.
     * <p>
     * @param eMap      HashMap for RobotList Entrys to be Executed.
     */
    public void executeTask(final HashMap<Integer, Entry> eMap) {
        executor.execute(new Runnable() {
            @Override public void run() {
                StringBuilder buffer = new StringBuilder();
                if (xmlTagging) {
                    buffer.append("\n<RobotData>\n");
                }
                Set<Map.Entry<Integer, Entry>> entrySet = eMap.entrySet();
                for (Map.Entry<Integer, Entry> s : entrySet) {                    
                    if (xmlTagging) {
                        buffer.append("  <Robot ID=\"").append(s.getKey()).append("\">\n");
                    }
                    if (xmlTagging) {
                        buffer.append(s.getValue().xmlString());
                    } else {
                        buffer.append(s.getValue());
                    }
                }
                if (xmlTagging) {
                    buffer.append("  </Robot>\n");
                }
                if (xmlTagging) {
                    buffer.append("</RobotData>\n");
                }
                RobotClient.sendMessage(buffer.toString());
            }
        });
    }

    
    /**
     * Get Data Values of Joints or Orientation, from the currently selected
     * tabbed panel.
     */
    public void getData() {
        StringBuilder buffer = new StringBuilder();

        if (xmlTagging) {
            buffer.append("\n<RobotData>\n");
        }
        for (JSplitPane sp : sPanels) {

            int indexOf = sPanels.indexOf(sp);
            System.out.println("Index: " + indexOf);
            Component sComponent =
                    ((JTabbedPane) sp.getRightComponent()).getSelectedComponent();

            if (sComponent instanceof JointWrapper) {
                ArrayList<Double> jointValues = ((JointWrapper) sComponent).getJointValues();
                if (xmlTagging) {
                    buffer.append(XMLTagger.jointTag(indexOf, jointValues));
                } else {
                    buffer.append(Arrays.toString(jointValues.toArray()).replaceAll("[\\[\\]]", ""));
                }
            }
            if (sComponent instanceof OrientationWrapper) {
                ArrayList<Double> jointValues = ((OrientationWrapper) sComponent).getJointValues();
                if (xmlTagging) {
                    buffer.append(XMLTagger.jointTag(indexOf, jointValues));
                } else {
                    buffer.append(Arrays.toString(jointValues.toArray()).replaceAll("[\\[\\]]", ""));
                }
            }
            if (!xmlTagging && indexOf < sPanels.size()) {
                buffer.append(", ");
            }
        }
        if (xmlTagging) {
            buffer.append("</RobotData>\n");
        }
        RobotClient.sendMessage(buffer.toString());
    }

    
    /**
     * Create and show GUI.
     */
    private static void createAndShowGUI() {

        int w, h;
        if (robotCnt == 1) {
            w = 750; h = 700;
        } else {
            w = 1300; h = 700;
        }

        final JFrame frame = new JFrame("Robot Control Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(w, h));
        frame.setResizable(true);

        RobotInterface contentPane = new RobotInterface(robotCnt);
        RobotInterface.setRobotInterface(contentPane);

        /* Sets RobotHandler for RobotList and RobotClient*/
        RobotHandler rHandler = new RobotHandler();
        rHandler.registerIFace(contentPane);
        RobotClient.setHandler(rHandler);

        contentPane.setOpaque(true);
        MenuBar menu = new MenuBar();
        frame.setJMenuBar(menu.createMenuBar());

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    
    /**
     * Main Method.
     * <p>
     * @param args      Arguments when Main Method is Invoked.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
}