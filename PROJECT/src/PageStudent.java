import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class PageStudent extends JFrame {

    /* componente folosite */
    JList listStudents;
    JList listCourses;
    JList listAssistants;

    JTextField textLastName;
    JTextField textFirstName;
    JTextField textTeacher;
    JTextField textAssistant;
    JTextField textPartial;
    JTextField textExam;

    JLabel labelLastName;
    JLabel labelFirstName;
    JLabel labelTeacher;
    JLabel labelAssistant;
    JLabel labelPartial;
    JLabel labelExam;
    JLabel labelAssistants;

    JButton buttonCourses;

    DefaultListModel<Student> listModelStudents;
    DefaultListModel<Course> listModelCourses;
    DefaultListModel<Assistant> listModelAssistants;

    public PageStudent(){

        super("Students page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(2, 2));
        setSize(new Dimension(100, 100));
        setMaximumSize(new Dimension(100, 100));

        /* construim lista de studenti */
        int ct = 0;
        listModelStudents = new DefaultListModel<>();
        for(Student stud : Catalog.getInstance().getAllStudents()){
            listModelStudents.add(ct, stud);
            ct++;
        }

        listStudents = new JList(listModelStudents);
        listStudents.addListSelectionListener(new ListenerStudents());

        JScrollPane listScroller = new JScrollPane(listStudents);
        add(listScroller);

        /* construim panel-ul cu datele studentului */
        labelLastName = new JLabel("Nume");
        textLastName = new JTextField(10);
        labelFirstName = new JLabel("Prenume");
        textFirstName = new JTextField(10);
        buttonCourses = new JButton("See courses");
        buttonCourses.addActionListener(new Action());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 0));
        panel.add(labelLastName);
        panel.add(textLastName);
        panel.add(labelFirstName);
        panel.add(textFirstName);
        panel.add(buttonCourses);

        add(panel);

        /*
         * construim lista de cursuri la care este inscris
         * studentul selectat din lista
         */
        listModelCourses = new DefaultListModel<>();
        listCourses = new JList(listModelCourses);
        listCourses.addListSelectionListener(new ListenerCourses());

        JScrollPane listScrollerCourses = new JScrollPane(listCourses);
        add(listScrollerCourses);

        /* construim panel-ul cursului */
        labelTeacher = new JLabel("Teacher");
        labelAssistant = new JLabel("Student's assistant");
        labelPartial = new JLabel("Partial score");
        labelExam = new JLabel("Exam score");
        labelAssistants = new JLabel("Assistants");
        textTeacher = new JTextField(20);
        textAssistant = new JTextField(20);
        textPartial = new JTextField(5);
        textExam = new JTextField(5);

        JPanel panelCourse = new JPanel();
        panelCourse.setLayout(new GridLayout(5, 0));
        panelCourse.add(labelTeacher);
        panelCourse.add(textTeacher);
        panelCourse.add(labelAssistant);
        panelCourse.add(textAssistant);
        panelCourse.add(labelPartial);
        panelCourse.add(textPartial);
        panelCourse.add(labelExam);
        panelCourse.add(textExam);
        panelCourse.add(labelAssistants);

        listModelAssistants = new DefaultListModel<>();
        listAssistants = new JList(listModelAssistants);

        JScrollPane listScrollerAssistants = new JScrollPane(listAssistants);
        panelCourse.add(listScrollerAssistants);

        add(panelCourse);

        pack();
        setVisible(true);
    }

    /* ActionListener pentru butonul "See courses" */
    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {

                /* verificam daca este selectat un student */
                if (listStudents.isSelectionEmpty()) {
                    return;
                }

                /* golim lista curenta de cursuri */
                listModelCourses.removeAllElements();

                /* parcurgem colectia de cursuri din catalog */
                int ct = 0;
                int select = listStudents.getSelectedIndex();
                ArrayList<Student> students;
                for (Course c : Catalog.getInstance().courses) {
                    students = c.getAllStudents();

                    /* verificam daca studentul este inscris la acest curs */
                    if (students.contains(listModelStudents.getElementAt(select))) {
                        /* in caz afirmativ, adaugam cursul la lista de cursuri */
                        listModelCourses.add(ct, c);
                        ct++;
                    }
                }
            }
        }
    }

    /* ListSelectionListener pentru lista de studenti */
    class ListenerStudents implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if(listStudents.isSelectionEmpty())
                return;

            /* actualizam datele studentului */
            textLastName.setText(listModelStudents.getElementAt(listStudents
                                                                .getSelectedIndex())
                                                                .getLastName());
            textFirstName.setText(listModelStudents.getElementAt(listStudents
                                                                 .getSelectedIndex())
                                                                 .getFirstName());
        }
    }

    /* ListSelectionListener pentru lista de cursuri */
    class ListenerCourses implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if(listCourses.isSelectionEmpty())
                return;

            /* actualizam datele cursului */
            int select = listCourses.getSelectedIndex();
            textTeacher.setText(listModelCourses
                                .getElementAt(select)
                                .getTeacher()
                                .toString());

            Course current_course = listModelCourses
                                    .getElementAt(select);
            Student current_stud = listModelStudents
                                    .getElementAt(listStudents.getSelectedIndex());

            /* cautam asistentul asociat studentului selectat */
            for(Map.Entry<String, Group> entry :
                    listModelCourses.getElementAt(select).getGroups().entrySet()){

                if(entry.getValue().contains(current_stud)){
                    textAssistant.setText(entry
                                          .getValue()
                                          .getAssistant()
                                          .toString());
                }
            }

            /* setam nota studentului la curs, daca exista */
            if(current_course.getGrade(current_stud) != null) {
                textPartial.setText(current_course
                                    .getGrade(current_stud)
                                    .getPartialScore()
                                    .toString());
                textExam.setText(current_course
                                 .getGrade(current_stud)
                                 .getExamScore()
                                 .toString());
            }
            else {
                textPartial.setText("-");
                textExam.setText("-");
            }

            /* actualizam lista de asistenti ai cursului */
            int ct = 0;
            listModelAssistants.removeAllElements();
            for(Assistant a : current_course.getAssistants()){
                listModelAssistants.add(ct, a);
                ct++;
            }
        }
    }
}