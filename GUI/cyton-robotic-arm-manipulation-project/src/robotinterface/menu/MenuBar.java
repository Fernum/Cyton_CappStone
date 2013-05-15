package robotinterface.menu;

/**
 *
 * @author Brian Bailey
 */
import robotinterface.dialogs.Help;
import robotinterface.dialogs.About;
import java.awt.event.*;
import javax.swing.*;
import robotinterface.RobotInterface;
import robotinterface.client.RobotClient;
import robotinterface.dao.EntryDAO;


/**
 * MenuBar Class.
 * <p>
 * @author Brian Bailey
 */
public class MenuBar {

    JTextArea output;
    JScrollPane scrollPane;

    /**
     * Create Menu Bar for Robot Interface.
     * <p>
     * @return      JMenuBar for RobotInterface.
     */
    public JMenuBar createMenuBar() {
        final JMenuBar menuBar;
        JMenu menu, submenu, clientMenu;
        JMenuItem menuItem, menuSave, menuLoad;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription( "Load/Export Iterface Lists");
        menuBar.add(menu);
        
        menuLoad = new JMenuItem("Load", KeyEvent.VK_L);
        menuLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuLoad.getAccessibleContext().setAccessibleDescription("Not Implemented Yet.");
        menuLoad.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent aa) {
                try {
                    // TODO: Load
                   JFrame frame = (JFrame)menuBar.getParent();                   
                   JOptionPane.showMessageDialog(frame,"File Selector Eventually."
                           ,"Load File",JOptionPane.PLAIN_MESSAGE);
                                   
                   //final JFileChooser fc = new JFileChooser();
                   //int returnVal = fc.showOpenDialog(RobotInterface.getRobotInterface());
                   //if (returnVal == JFileChooser.APPROVE_OPTION) {
                   //    File file = fc.getSelectedFile();
                   //     try { model.readObject(file.getAbsolutePath()); } 
                   //     catch (IOException | ClassNotFoundException ex) { }
                   //}                
                   //list.repaint();
                   
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(menuLoad);

        menuSave = new JMenuItem("Save", KeyEvent.VK_S);
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuSave.getAccessibleContext().setAccessibleDescription("Not Implemented Yet.");
        menuSave.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                try {
                    // TODO: Save                    
                    JFrame frame = (JFrame)menuBar.getParent();                   
                   JOptionPane.showMessageDialog(frame,"File Selector Eventually."
                           ,"Save File",JOptionPane.PLAIN_MESSAGE);
                                  
                   //final JFileChooser fc = new JFileChooser();
                   //int returnVal = fc.showSaveDialog(RobotInterface.getRobotInterface());
                   //if (returnVal == JFileChooser.APPROVE_OPTION) {
                   //    File file = fc.getSelectedFile();
                   //    try {  model.writeObject(file.getAbsolutePath()); }
                   //    catch (IOException ex) { } 
                   //}
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(menuSave);

        menu.addSeparator();
        submenu = new JMenu("Options");
        submenu.setMnemonic(KeyEvent.VK_O);

        menuItem = new JMenuItem("Robot");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Display DB");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
          
        menuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                try { EntryDAO.listEntrys(); } 
                catch (Exception exp) { System.err.println(exp); }
            }
        });
        
        submenu.add(menuItem);
        menu.add(submenu);

        menu = new JMenu("Run");
        menu.setMnemonic(KeyEvent.VK_1);
        menu.getAccessibleContext().setAccessibleDescription("Run RobotLists.");                         
        menuBar.add(menu);

        JMenuItem runMenu = new JMenuItem("Run");
        runMenu.getAccessibleContext().setAccessibleDescription("Start Running RobotList.");
        runMenu.setMnemonic(KeyEvent.VK_R);
        runMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                try {  RobotInterface.getRobotInterface().start(); }
                catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(runMenu);
        menu.addSeparator();

        runMenu = new JMenuItem("Step Forward");
        runMenu.getAccessibleContext().setAccessibleDescription("Not Implemented Yet.");
        runMenu.setMnemonic(KeyEvent.VK_F);
        menu.add(runMenu);

        runMenu = new JMenuItem("Step Back");
        runMenu.getAccessibleContext().setAccessibleDescription("Not Implemented Yet.");
        runMenu.setMnemonic(KeyEvent.VK_B);
        menu.add(runMenu);
        menu.addSeparator();

        runMenu = new JMenuItem("Stop");
        runMenu.getAccessibleContext().setAccessibleDescription("Stop Running RobotList.");
        runMenu.setMnemonic(KeyEvent.VK_T);       
        runMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                try { RobotInterface.getRobotInterface().stop(); }
                catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(runMenu);

        menu = new JMenu("Tools");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Not Implemented Yet.");

        clientMenu = new JMenu("Client");
        clientMenu.setMnemonic(KeyEvent.VK_S);
        ButtonGroup clientGroup = new ButtonGroup();

        JRadioButtonMenuItem rbClientStart = new JRadioButtonMenuItem("Start Client");
        rbClientStart.setSelected(false);
        rbClientStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        rbClientStart.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    RobotClient.startClient();
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        clientGroup.add(rbClientStart);
        clientMenu.add(rbClientStart);

        JRadioButtonMenuItem rbClientStop = new JRadioButtonMenuItem("Stop Client");
        rbClientStop.setSelected(true);
        rbClientStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
        rbClientStop.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    RobotClient.stopClient();
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        clientGroup.add(rbClientStop);
        clientMenu.add(rbClientStop);

        menu.add(clientMenu);

        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("Toogle XML Tagging");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);         
        cbMenuItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                try { RobotInterface.setXMLtagging(); } 
                catch (Exception exp) { System.err.println(exp); }
            }
        });        
        menu.add(cbMenuItem);
        
        menuBar.add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");

        JMenuItem helpMenu = new JMenuItem("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    Help dialog = new Help();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(helpMenu);

        JMenuItem aboutMenu = new JMenuItem("About");
        aboutMenu.setMnemonic(KeyEvent.VK_A);
        aboutMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    About dialog = new About();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception exp) { System.err.println(exp); }
            }
        });
        menu.add(aboutMenu);
        menuBar.add(menu);
        return menuBar;
    }

}
