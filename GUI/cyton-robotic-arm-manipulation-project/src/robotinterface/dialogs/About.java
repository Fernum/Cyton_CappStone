package robotinterface.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

/**
 * About Dialog
 * <p>
 * @author Brian Bailey
 */
public class About extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JButton cancelButton;
    private JLabel imageLabel;
    private final JLabel lblrelease = new JLabel("RobotControl" + version, JLabel.CENTER);
    private final JLabel lblAbout = new JLabel("A Java utility for coontrolling a Cyton robtic arm.", JLabel.CENTER);
    private final JLabel lbllicense = new JLabel("Copyright © 2012 SUNY Oswego Computer Science Department", JLabel.CENTER);
    private static final String version = "v1.0rc1";

    /**
     *
     */
    public About() {
        lblAbout.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblrelease.setFont(new Font("Dialog", Font.BOLD, 25));
        setTitle("About Dialog");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(About.class.getResource("/dialogs/java-icon.png")));
        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_contentPanel.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(lblrelease, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
                .addGap(236))
                .addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
                .addGap(24)
                .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPanel.createSequentialGroup()
                .addGap(12)
                .addComponent(lbllicense))
                .addComponent(lblAbout))
                .addContainerGap(192, Short.MAX_VALUE)));
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPanel.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                .addComponent(lblrelease, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addComponent(lblAbout)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(lbllicense)
                .addContainerGap(80, Short.MAX_VALUE)));
        lbllicense.setFont(new Font("Dialog", Font.ITALIC, 12));
        contentPanel.setLayout(gl_contentPanel);
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                cancelButton = new JButton("OK");
                cancelButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        About.this.dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
            }
            GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
            gl_buttonPane.setHorizontalGroup(
                    gl_buttonPane.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
                    .addContainerGap(353, Short.MAX_VALUE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()));
            gl_buttonPane.setVerticalGroup(
                    gl_buttonPane.createParallelGroup(Alignment.LEADING)
                    .addGroup(gl_buttonPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(cancelButton)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            buttonPane.setLayout(gl_buttonPane);
        }
    }
}
