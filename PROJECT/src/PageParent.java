import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class PageParent extends JFrame {

    /* componente folosite */
    JList listParents;
    JLabel labelLastName;
    JLabel labelFirstName;

    JTextField textLastName;
    JTextField textFirstName;

    JTextArea notifications;

    JScrollPane scr;

    DefaultListModel<Parent> listModelParent;

    public PageParent() {
        super("Parents page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        /* construim si populam lista de parinti */
        int ct = 0;
        listModelParent = new DefaultListModel<>();
        for(Observer o : Catalog.getInstance().getObservers()){
            Parent p = (Parent) o;
            listModelParent.add(ct, p);
            ct++;
        }

        listParents = new JList(listModelParent);
        listParents.addListSelectionListener(new PageParent.Listener());

        JScrollPane listScrollerParent = new JScrollPane(listParents);

        c.gridheight = 3;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(listScrollerParent, c);

        /* construim panel-ul ce prezinta datele profilului */
        JPanel panelProfile = new JPanel();
        panelProfile.setLayout(new GridLayout(4, 0));

        labelLastName = new JLabel("Nume:");
        textLastName = new JTextField(10);
        labelFirstName = new JLabel("Prenume:");
        textFirstName = new JTextField(10);

        panelProfile.add(labelLastName);
        panelProfile.add(textLastName);
        panelProfile.add(labelFirstName);
        panelProfile.add(textFirstName);

        c.weightx = 0.5;
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 0;
        add(panelProfile, c);

        /*
         * adaugam zona text unde vor fi afisate notificarile
         * primite de un parinte
         */
        scr = new JScrollPane();
        scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        notifications = new JTextArea(30, 30);
        notifications.setEditable(false);

        scr.setBounds(20, 20, 100, 100);
        scr.setViewportView(notifications);

        JPanel panel = new JPanel();
        panel.setBorder ( new TitledBorder( new EtchedBorder(), "Notifications" ) );
        panel.add(scr);

        c.weightx = 0.5;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        add(panel, c);

        pack();
        setVisible(true);
    }

    /* ListSelectionListener pentru lista de parinti */
    class Listener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if(listParents.isSelectionEmpty())
                return;

            Parent current_parent = listModelParent
                                    .getElementAt(listParents
                                                  .getSelectedIndex());
            notifications.setText(current_parent.notif.toString());

            scr.revalidate();
            scr.repaint();

            textLastName.setText(current_parent.getLastName());
            textFirstName.setText(current_parent.getFirstName());
        }
    }
}