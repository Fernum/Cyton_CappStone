
package robotinterface.factory.panel;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import robotinterface.RobotInterface;

/**
 * OrientationPanel Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class OrientationPanel extends JPanel {

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JSpinner jSpinner1;
    private JSpinner jSpinner2;
    private JSpinner jSpinner3;
    private boolean send = true;
    
    /**
     * Orientation Panel COnstructor.
     * <p>
     * @param oName     OrientationPanel Name
     */
    public OrientationPanel(String oName)
    {
        //setBackground(Color.DARK_GRAY);
        
        jLabel1 = new JLabel();
        //TODO: Slider Model.
        
        jSpinner1 = new JSpinner();

        
        jSpinner1.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        
        jLabel2 = new JLabel();
        jSpinner2 = new JSpinner();        
        jSpinner2.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });
        
        jLabel3 = new JLabel();
        jSpinner3 = new JSpinner();        
        jSpinner3.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), oName));
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinner2, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinner3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinner3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    /**
     * Create Orientation Panel.
     * <p>
     * @param ornt      Orientation
     * @return          OrientationPanel
     */
    public static OrientationPanel createJointPanel(Orientation ornt) {
        OrientationPanel sPanel = new OrientationPanel(ornt.getName());
        sPanel.setTooltip(ornt.getToolTips());
        sPanel.setLabels(ornt.getLabels());
        return sPanel;
    }
        
    /**
     * Set Orientation Labels.
     * <p>
     * @param oLabels       ArrayList of Orientation Labels
     */
    private void setLabels(ArrayList<String> oLabels) {
        jLabel1.setText( oLabels.get(0) );
        jLabel2.setText( oLabels.get(1) );
        jLabel3.setText( oLabels.get(2) );
    }
    
    /**
     * Set ToolTip for Orientation Labels.
     * <p>
     * @param oToolTips  ArrayList of Orientation ToolTips
     */
    private void setTooltip(ArrayList<String> oToolTips) {
        jLabel1.setToolTipText( oToolTips.get(0) );
        jLabel2.setToolTipText( oToolTips.get(0) );
        jLabel3.setToolTipText( oToolTips.get(0) );
    }
        
    /**
     * Spinner 1 State Changed.
     * <p>
     * @param evt       ChangeEvent
     */
    private void jSpinner1StateChanged(ChangeEvent evt) {        
        JSpinner aSpinner = (JSpinner) evt.getSource();
        int value = (int) aSpinner.getModel().getValue();        
        // TODO: Trigger Send Data to Server Event
        //FactoryService.fireEvent();
        RobotInterface.getRobotInterface().getData();
    }

    /**
     * Spinner 2 State Changed.
     * <p>
     * @param evt       ChangeEvent
     */
    private void jSpinner2StateChanged(ChangeEvent evt) {
        JSpinner aSpinner = (JSpinner) evt.getSource();
        int value = (int) aSpinner.getModel().getValue();
        // TODO: Trigger Send Data to Server Event
        //FactoryService.fireEvent();
        RobotInterface.getRobotInterface().getData();
    }

    
    /**
     * Spinner 3 State Changed.
     * <p>
     * @param evt       ChangeEvent
     */
    private void jSpinner3StateChanged(ChangeEvent evt) {
        JSpinner aSpinner = (JSpinner) evt.getSource();
        int value = (int) aSpinner.getModel().getValue();
        // TODO: Trigger Send Data to Server Event
        //FactoryService.fireEvent();
        RobotInterface.getRobotInterface().getData();
    }
    
    
    /**
     * Get Values of Spinners.
     * <p>
     * @return ArrayList of Doubles for Spinner values.
     */
    public ArrayList<Double> getValues() {
        ArrayList<Double> aList = new ArrayList<>();
        
        // TODO: Add XML Tag
        aList.add((int)jSpinner1.getModel().getValue()*1.0);
        aList.add((int)jSpinner2.getModel().getValue()*1.0);
        aList.add((int)jSpinner3.getModel().getValue()*1.0);
                
        return aList;
    } 
 
    
    /**
     * Reset Spinner Values.
     */
    public void resetSpinners() {
        this.send = false;
        jSpinner1.getModel().setValue(0);
        jSpinner2.getModel().setValue(0);
        jSpinner3.getModel().setValue(0);
        this.send = true;
    }

        
    @Override
    public String toString() {
        return "OrientationPanel{" + "jLabel1=" + jLabel1.getText() + ", jLabel2=" +
                jLabel2.getText() + ", jLabel3=" + jLabel3.getText() + '}';
    }
    
}