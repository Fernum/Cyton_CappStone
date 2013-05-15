package robotinterface.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.*;
import javax.swing.text.*;

/**
 * Help Dialog
 * <p>
 * @author Brian Bailey
 */
public class Help extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextPane textPane;
    private JButton cancelButton;

    /**
     *
     */
    public Help() {
        setTitle("Help");
        setBounds(100, 100, 450, 300);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            {
                textPane = new JTextPane();
                setFormat(textPane);
                scrollPane.setViewportView(textPane);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                cancelButton = new JButton("OK");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        Help.this.dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
            }
            GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
            gl_buttonPane.setHorizontalGroup(
                    gl_buttonPane.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
                    .addContainerGap(352, Short.MAX_VALUE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()));
            gl_buttonPane.setVerticalGroup(
                    gl_buttonPane.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton)
                    .addContainerGap()));
            buttonPane.setLayout(gl_buttonPane);
        }
    }

    /**
     * Set Format of Help Dialog
     * <p>
     * @param textPane 
     */
    private void setFormat(JTextPane textPane) {
        SimpleAttributeSet boldSet = new SimpleAttributeSet();
        StyleConstants.setBold(boldSet, true);
        StyleConstants.setFontSize(boldSet, 18);

        SimpleAttributeSet colorSet = new SimpleAttributeSet();
        StyleConstants.setForeground(colorSet, Color.blue);
        StyleConstants.setFontSize(colorSet, 14);

        SimpleAttributeSet normalSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(normalSet, 12);

        Document doc = textPane.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), "Help Content:", boldSet);
            StyleConstants.setFontSize(boldSet, 16);

            doc.insertString(doc.getLength(), "\n\nSomething:", boldSet);
            doc.insertString(doc.getLength(), "\nSome Text Goes Here\n\n", normalSet);

            doc.insertString(doc.getLength(), "Client:", boldSet);
            doc.insertString(doc.getLength(), "\nClient to send TCP Packets to Robots Server.\n", normalSet);

            doc.insertString(doc.getLength(), "   Start Client:", colorSet);
            doc.insertString(doc.getLength(), "\n      Starts Client to send data to robt's server.\n", normalSet);

            doc.insertString(doc.getLength(), "   Stop Client:", colorSet);
            doc.insertString(doc.getLength(), "\n      Stops Client to send data to robt's server.\n\n", normalSet);

        } catch (BadLocationException e) {
        }
    }
}
