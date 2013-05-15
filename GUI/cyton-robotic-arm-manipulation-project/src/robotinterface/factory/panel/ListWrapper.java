
package robotinterface.factory.panel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import robotinterface.RobotInterface;
import robotinterface.list.RobotList;

/**
 * ListWrapper Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class ListWrapper extends JPanel {
    
    private ListPanel lPanel;
    private JToggleButton tglbtnRun;
       
    /**
     * ListWrapper Constructor.
     * <p>
     * @param id        ListWrapper Identifier.
     */
    public ListWrapper(int id) {
                
        this.lPanel = new ListPanel(id, this);
        
        JButton btnNewButton = new JButton("Export");
        btnNewButton.setPreferredSize(new Dimension(80, 25));
        btnNewButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent evt) {
                btnNewButtonChanged(evt);
            }
        });
        
        tglbtnRun = new JToggleButton("Run");
        tglbtnRun.setPreferredSize(new Dimension(80, 25));
        tglbtnRun.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent evt) {
                tglbtnRunChanged(evt);
            }
        });

        GroupLayout groupLayout = new GroupLayout(lPanel);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(lPanel.getRobotList(), GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGroup(groupLayout.createSequentialGroup()
                .addComponent(btnNewButton)
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tglbtnRun, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lPanel.getRobotList(), GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(btnNewButton)
                .addComponent(tglbtnRun))
                .addContainerGap()));
        lPanel.setLayout(groupLayout);        
    }        
   
    /**
     * Build ListWrapper and ListPanel.
     * <p>
     * @param id        ListWrapper Identifier.
     * @return          ListPanel
     */
    public static ListPanel buildWrapper(int id) {        
        ListWrapper listWrap = new ListWrapper(id);
        return listWrap.getlPanel();
    }

    /**
     * Get RobotList.
     * <p>
     * @return      RobotList.
     */
    public RobotList getRobotList() {
        return lPanel.getRobotList();
    }    

    /**
     * Get List Panel.
     * <p>
     * @return      ListPanel.
     */
    public ListPanel getlPanel() {
        return lPanel;
    }   
    
    /**
     * Import List Button Clicked.
     * <p>
     * @param evt       Import Button ActionEvent.
     */
    private void btnNewButtonChanged(ActionEvent evt) {             
        JButton button = (JButton)evt.getSource();
        
        // TODO: Handle Button Event
        //FactoryService.fireEvent();        

        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(ListWrapper.this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            lPanel.getRobotList()
                  .writeObject(file.getAbsolutePath());  
        }              
    }    
   
    /**
     * Toggle Run Button
     * <p>
     * @param evt       Run Button ActionEvent
     */
    private void tglbtnRunChanged(ActionEvent evt) {       
        JToggleButton button = (JToggleButton)evt.getSource();
        //FactoryService.fireEvent(); 
        if(button.getText().equalsIgnoreCase("Run")) { 
            toogleRunning(true); 
            RobotInterface.getRobotInterface().start(lPanel.getRobotList());
        } 
        else { 
            toogleRunning(false); 
            RobotInterface.getRobotInterface().stop(lPanel.getRobotList());
        }        
    }        
     
    
    /**
     * Toogle Running Status
     * <p>
     * @param runState      Current Running Status.
     */
    public void toogleRunning(boolean runState) {
        if(runState==true) { 
            tglbtnRun.setText("Stop"); 
            tglbtnRun.setSelected(true);            
        } else {
            tglbtnRun.setText("Run"); 
            tglbtnRun.setSelected(false);            
        }
    }  

        
    @Override
    public String toString() {
        return "ListWrapper{" + "lPanel=" + lPanel.getName() + '}';
    }
    
}
