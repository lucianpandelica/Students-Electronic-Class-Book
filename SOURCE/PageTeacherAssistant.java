import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

public class PageTeacherAssistant extends JFrame {

    /* componente folosite */
    JList listTeachers;
    JList listAssistants;
    JList listCourses;
    JList listGrades;

    JLabel labelTeachers;
    JLabel labelAssistants;
    JLabel labelCourses;
    JLabel labelGrades;

    JButton buttonValidate;

    DefaultListModel<Teacher> listModelTeachers;
    DefaultListModel<Assistant> listModelAssistants;
    DefaultListModel<Course> listModelCourses;
    DefaultListModel<String> listModelGrades;

    /* functie pentru popularea listei de note nevalidate pentru un profesor */
    public void fillTeacherGrades(Teacher teacher){

        /* golim lista de note nevalidate */
        listModelGrades.removeAllElements();

        /*
         * punem in lista notele nevalidate ale
         * profesorului curent, in forma text
         */
        int ct = 0;
        ScoreVisitor current_scores = Catalog.getInstance().getScores();
        ArrayList<String> current_grades = current_scores
                .getTeachersGrades(teacher);

        for(String grade : current_grades){
            listModelGrades.add(ct, grade);
            ct++;
        }
    }

    /* functie pentru popularea listei de note nevalidate pentru un asistent */
    public void fillAssistantGrades(Assistant assistant){

        listModelGrades.removeAllElements();

        /* punem in lista notele nevalidate ale asistentului curent */
        int ct = 0;
        ScoreVisitor current_scores = Catalog.getInstance().getScores();
        ArrayList<String> current_grades = current_scores
                                           .getAssistantsGrades(assistant);

        for(String grade : current_grades){
            listModelGrades.add(ct, grade);
            ct++;
        }
    }

    public PageTeacherAssistant() {

        super("T/A page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.weightx = 0.5;

        labelTeachers = new JLabel("Teachers list");
        c.gridx = 0;
        add(labelTeachers, c);

        labelAssistants = new JLabel("Assistants list");
        c.gridx = 1;
        add(labelAssistants, c);

        JPanel lists = new JPanel();
        lists.setLayout(new GridLayout(0, 2));

        /* construim si populam lista de profesori */
        listModelTeachers = new DefaultListModel<>();
        int ct = 0;
        ct = 0;
        for(Teacher teach : Catalog.getInstance().getAllTeachers()){
            listModelTeachers.add(ct, teach);
            ct++;
        }

        listTeachers = new JList(listModelTeachers);
        listTeachers.addListSelectionListener(new PageTeacherAssistant
                                                  .ListenerTeachers());

        JScrollPane listScrollerTeachers = new JScrollPane(listTeachers);
        lists.add(listScrollerTeachers);

        /* construim si populam lista de asistenti */
        listModelAssistants = new DefaultListModel<>();
        ct = 0;
        TreeSet<Assistant> assistantsList = new TreeSet<>();
        for(Course course : Catalog.getInstance().courses) {
            for(Assistant a : course.getAssistants()){
                assistantsList.add(a);
            }
        }
        for(Assistant a : assistantsList){
            listModelAssistants.add(ct, a);
            ct++;
        }

        listAssistants = new JList(listModelAssistants);
        listAssistants.addListSelectionListener(new PageTeacherAssistant
                                                    .ListenerAssistants());

        JScrollPane listScrollerAssistants = new JScrollPane(listAssistants);
        lists.add(listScrollerAssistants);

        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        add(lists, c);

        c.weightx = 0.5;
        c.gridy = 2;

        labelCourses = new JLabel("Courses");
        c.gridx = 0;
        add(labelCourses, c);

        labelGrades = new JLabel("Grades to validate");
        c.gridx = 1;;
        add(labelGrades, c);

        JPanel data = new JPanel();
        data.setLayout(new GridLayout(0, 2));

        /* construim lista de cursuri */
        listModelCourses = new DefaultListModel<>();
        listCourses = new JList(listModelCourses);
        JScrollPane listScrollerCourses = new JScrollPane(listCourses);
        data.add(listScrollerCourses);

        /* construim lista de note nevalidate */
        listModelGrades = new DefaultListModel<>();
        listGrades = new JList(listModelGrades);
        JScrollPane listScrollerGrades = new JScrollPane(listGrades);
        data.add(listScrollerGrades);

        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;

        c.gridy = 3;
        add(data, c);

        /* adaugam butonul de validare a notelor */
        buttonValidate = new JButton("Validate grades");
        buttonValidate.addActionListener(new Action());
        c.gridy = 4;
        add(buttonValidate, c);

        pack();
        setVisible(true);
    }

    /* ListSelectionListener pentru lista de profesori */
    class ListenerTeachers implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            /* verificam daca exista un element selectat */
            if(listTeachers.isSelectionEmpty())
                return;

            /*
             * stergem selectia (daca exista) din lista de asistenti,
             * pentru a evidentia faptul ca se vor afisa mai multe detalii
             * despre profesor
             */
            listAssistants.clearSelection();

            int ct = 0;
            Teacher current_teacher = listModelTeachers.getElementAt(listTeachers
                                                                     .getSelectedIndex());

            /* golim lista de cursuri */
            listModelCourses.removeAllElements();

            /* punem in lista cursurile profesorului selectat */
            ct = 0;
            for(Course c : Catalog.getInstance().courses){
                if(c.getTeacher().equals(current_teacher)){
                    listModelCourses.add(ct, c);
                    ct++;
                }
            }

            /* actualizam lista de note nevalidate */
            fillTeacherGrades(current_teacher);
        }
    }

    /* ListSelectionListener pentru lista de asistenti */
    class ListenerAssistants implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            /* verificam daca exista un element selectat */
            if(listAssistants.isSelectionEmpty())
                return;

            /* stergem selectia din lista de profesori */
            listTeachers.clearSelection();

            int ct = 0;
            Assistant current_assistant = listModelAssistants
                                          .getElementAt(listAssistants
                                                        .getSelectedIndex());

            listModelCourses.removeAllElements();

            /* punem in lista cursurile asistentului selectat */
            ct = 0;
            for(Course c : Catalog.getInstance().courses){
                if(c.getAssistants().contains(current_assistant)){
                    listModelCourses.add(ct, c);
                    ct++;
                }
            }

            fillAssistantGrades(current_assistant);
        }
    }

    /* ActionListener pentru butonul de validare note */
    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {

                /* verificam ce tip de utilizator este selectat */
                if (!listTeachers.isSelectionEmpty()) {
                    Teacher current_teacher = listModelTeachers
                                              .getElementAt(listTeachers
                                                            .getSelectedIndex());
                    ScoreVisitor current_scores = Catalog
                                                  .getInstance()
                                                  .getScores();

                    /* validam notele profesorului */
                    current_teacher.accept(current_scores);

                    /* actualizam lista de note nevalidate */
                    fillTeacherGrades(current_teacher);

                } else if (!listAssistants.isSelectionEmpty()) {
                    Assistant current_assistant = listModelAssistants
                                                  .getElementAt(listAssistants
                                                                .getSelectedIndex());
                    ScoreVisitor current_scores = Catalog
                                                  .getInstance()
                                                  .getScores();

                    /* validam notele asistentului */
                    current_assistant.accept(current_scores);

                    fillAssistantGrades(current_assistant);
                }
            }
        }
    }
}