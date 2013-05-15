
package robotinterface.factory.panel;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.*;
import robotinterface.RobotInterface;

/**
 * JointPanel Class.
 * <p>
 * @author Brian Bailey
 */
@SuppressWarnings("unchecked")
public class JointPanel extends JPanel {

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JSlider jSlider1;
    private JSpinner jSpinner1;
    private boolean send = true;
    
    
    /**
     * Joint Panel Constructor.
     * <p>
     * @param title     JointPanel Name.
     * @param min       JointPanel Min. Value.
     * @param max       JointPanel Max. Value.
     */
    public JointPanel(String title, Double min, Double max) {
                        
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        
        
        jSlider1 = new JSlider();
        BoundedRangeModel jModel = jSlider1.getModel();
        jModel.setMinimum(min.intValue());
        jModel.setMaximum(max.intValue());
        jModel.setValue(0);
        
        SpinnerNumberModel model = new SpinnerNumberModel(jModel.getValue(), 
                jModel.getMinimum(), jModel.getMaximum(), 1);
        jSpinner1 = new JSpinner(model);        

        jLabel1.setText(title);
        jLabel2.setText(String.valueOf(min) + "°");
        jLabel3.setText(String.valueOf(max) + "°");                
        
       
        jSlider1.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        
        jSpinner1.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jSlider1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)//
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)//
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        setLayout(layout);
    }
       
    
    /**
     * Create Joint Panel.
     * <p>
     * @param joint     Joint
     * @return          JointPanel
     */
    public static JointPanel createJointPanel(Joint joint) {
        JointPanel sPanel = new JointPanel(joint.getLabel(),joint.getMin(),joint.getMax());
        sPanel.setTooltip(sPanel, joint.getLabel(), joint.getIconDir());        
        return sPanel;
    }

    
    /**
     * Slider State Changed.
     * <p>
     * @param evt       ChangeEvent
     */
    private void jSlider1StateChanged(ChangeEvent evt) {
        JSlider aSlider = (JSlider) evt.getSource();
        jSpinner1.getModel().setValue((int) aSlider.getValue());
        if (!aSlider.getModel().getValueIsAdjusting()) {
            // TODO: Trigger Send Data to Server Event
            RobotInterface.getRobotInterface().getData();
        }
    }

    
    /**
     * Spinner State Changed.
     * <p>
     * @param evt       ChangeEvent
     */
    private void jSpinner1StateChanged(ChangeEvent evt) {
        JSpinner aSpinner = (JSpinner) evt.getSource();
        Integer value = (Integer)aSpinner.getModel().getValue();
        if (value < jSlider1.getMaximum() || value > jSlider1.getMinimum()) {
            
            if(!jSlider1.getModel().getValueIsAdjusting()) {
                jSlider1.getModel().setValue(value); 
                aSpinner.getModel().setValue(value);
                RobotInterface.getRobotInterface().getData();
            }
        }
    }

    
    /**
     * Set ToolTip for Joint Panel.
     * <p>
     * @param label
     * @param iconDir 
     */
    private void setTooltip(JointPanel sPanel, String label, String iconDir) {
        
        int initialDelay = 1000;
        ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
        
        String aImage = "file:" + iconDir;        
        sPanel.setToolTipText("<html>" + "<h4><center>" + label
                + "</center></h4></br>" + " <img src=" + aImage + "></html>");
        jLabel2.setToolTipText("Minimum range for joint: " + label);
        jLabel3.setToolTipText("Maximum range for joint: " + label);
    }

    
    /**
     * Get Slider Value.
     * <p>
     * @return      Double Value of Slider
     */
    public double getValue() {        
        return jSlider1.getModel().getValue();
    }
    
    
    /**
     * Reset Spinner Values.
     */
    void resetSpinners() {
        send = false;
        jSpinner1.getModel().setValue(0);
        send = true;
    }    
      
    
    @Override
    public Point getToolTipLocation(MouseEvent e) {
        // TODO: Set to a better coordinate.
        return new Point(-300,0);
    }
        
    @Override
    public String toString() {
        return "JointPanel{" + "jLabel1=" + jLabel1.getText() + ", jLabel2=" +
                jLabel2.getText() + ", jLabel3=" + jLabel3.getText() + '}';
    }
    
}