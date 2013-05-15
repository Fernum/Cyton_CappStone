package robotinterface.list;

import java.awt.*;
import javax.swing.*;
import robotinterface.list.entry.*;

/**
 * RobotCellRenderer Class.
 * <p>
 * @author Brian Bailey
 */
public class RobotCellRenderer extends JLabel implements ListCellRenderer {

    private static final Color HIGHLIGHT_COLOR = new Color(184, 207, 229);
    private static final Color REPLACE_COLOR = new Color(255, 0, 0);
    private static final Color EVEN_COLOR = new Color(255, 255, 255);
    private static final Color ODD_COLOR = new Color(238, 238, 238);

    /**
     * RobotCellRenderer Constructor.
     */
    public RobotCellRenderer() {
        setOpaque(true);
        setIconTextGap(20);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
    {
        Color background, foreground;

        Entry entry = (Entry) value;
        setText(entry.getTitle());
        //setIcon( entry.getImage() );

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {
            background = REPLACE_COLOR;
            foreground = Color.WHITE;
        } else if (isSelected) {
            //background = (isRunning)? STEP_COLOR : HIGHLIGHT_COLOR;
            background = HIGHLIGHT_COLOR;
            foreground = Color.white;
        } else {
            background = (index % 2 == 0) ? ODD_COLOR : EVEN_COLOR;
            foreground = Color.BLACK;
        }
        setBackground(background);
        setForeground(foreground);
        return this;
    }
}
