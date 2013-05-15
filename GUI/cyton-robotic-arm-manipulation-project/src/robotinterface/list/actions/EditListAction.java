package robotinterface.list.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import robotinterface.list.*;
import robotinterface.list.entry.*;

/**
 * EditListAction Class.
 * <p>
 * @author Brian Bailey
 */
public class EditListAction extends AbstractAction {

    private RobotList list;
    private Class<?> modelClass;
    private JPopupMenu editPopup;
    private JTextField editTextField;
    

    /**
     * EditListAction Constructor.
     */
    public EditListAction() {
        setModelClass(RobotListModel.class);
    }

    
    /**
     * Set List's ModelClass.
     * <p>
     * @param modelClass        List's Model Class.
     */
    private void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }

    
    /**
     * Apply Value to Model.
     * <p>
     * @param model     List's ListModel.
     * @param entry     Entry to Add to Model.
     * @param index     Index to Add Entry At.
     */
    protected void applyValueToModel(ListModel model, Entry entry, int index) {
        RobotListModel dlm = (RobotListModel) model;
        dlm.set(index, entry);
    }

      
    /**
     * Create Edit Pop-Up inside List.
     */
    private void createEditPopup() {
        Border border = UIManager.getBorder("List.focusCellHighlightBorder");
        editTextField = new JTextField();
        editTextField.setBorder(border);

        editTextField.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                Entry value = new Entry(editTextField.getText());
                applyValueToModel(list.getModel(), value, list.getSelectedIndex());
                editPopup.setVisible(false);
            }
        });
        editPopup = new JPopupMenu();
        editPopup.setBorder(new EmptyBorder(0, 0, 0, 0));
        editPopup.add(editTextField);
    }
        
        
    @Override
    public void actionPerformed(ActionEvent e) {
        list = (RobotList) e.getSource();
        ListModel model = list.getModel();

        if (!modelClass.isAssignableFrom(model.getClass())) { return; }
        if (editPopup == null) { createEditPopup(); }

        int index = list.getSelectedIndex();
        Rectangle r = list.getCellBounds(index, index);
        editPopup.setPreferredSize(new Dimension(r.width, r.height));
        editPopup.show(list, r.x, r.y);

        editTextField.setText(list.getSelectedValue().toString());
        editTextField.selectAll();
        editTextField.requestFocusInWindow();
    }

}
