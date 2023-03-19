import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingleParentPage extends JFrame {

    Parent current_parent;

    JLabel labelLastName;
    JLabel labelFirstName;

    JTextField textLastName;
    JTextField textFirstName;

    JTextArea notifications;

    JButton buttonRefresh;

    JScrollPane scr;

    public SingleParentPage(Parent parent){
        super("Parent's profile: " +
                    parent.getFirstName() +
                    " " +
                    parent.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        current_parent = parent;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelProfile = new JPanel();
        panelProfile.setLayout(new GridLayout(4, 0));

        labelLastName = new JLabel("Nume:");
        textLastName = new JTextField(10);
        textLastName.setText(current_parent.getLastName());
        labelFirstName = new JLabel("Prenume:");
        textFirstName = new JTextField(10);
        textFirstName.setText(current_parent.getFirstName());

        panelProfile.add(labelLastName);
        panelProfile.add(textLastName);
        panelProfile.add(labelFirstName);
        panelProfile.add(textFirstName);

        c.weightx = 0.5;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        add(panelProfile, c);

        scr = new JScrollPane();
        scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        notifications = new JTextArea(30, 30);
        notifications.setText(current_parent.notif.toString());
        notifications.setEditable(false);

        scr.setBounds(20, 20, 100, 100);
        scr.setViewportView(notifications);

        JPanel panel = new JPanel();
        panel.setBorder ( new TitledBorder( new EtchedBorder(), "Notifications" ) );
        panel.add(scr);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        add(panel, c);

        buttonRefresh = new JButton("Refresh");
        buttonRefresh.addActionListener(new SingleParentPage.ActionRefresh());
        c.gridx = 0;
        c.gridy = 3;
        add(buttonRefresh, c);

        pack();
        setVisible(true);
    }

    class ActionRefresh implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                notifications.setText(current_parent.notif.toString());
            }
        }
    }
}
