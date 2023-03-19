import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class SingleStudentPage extends JFrame {

    /* componente folosite */
    Student current_student;

    JList listCourses;
    JList listAssistants;

    JTextField textTeacher;
    JTextField textAssistant;
    JTextField textPartial;
    JTextField textExam;

    JLabel labelTeacher;
    JLabel labelAssistant;
    JLabel labelPartial;
    JLabel labelExam;
    JLabel labelAssistants;

    DefaultListModel<Course> listModelCourses;
    DefaultListModel<Assistant> listModelAssistants;

    public SingleStudentPage(Student student){
        super("Student's profile: " +
                student.getFirstName() +
                " " +
                student.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        current_student = student;

        /* construim si populam lista de cursuri */
        listModelCourses = new DefaultListModel<>();
        listCourses = new JList(listModelCourses);
        listCourses.addListSelectionListener(new SingleStudentPage.ListenerCourses());

        int ct = 0;
        ArrayList<Student> students;
        for (Course c : Catalog.getInstance().courses) {
            students = c.getAllStudents();

            if (students.contains(current_student)) {
                listModelCourses.add(ct, c);
                ct++;
            }
        }

        JScrollPane listScrollerCourses = new JScrollPane(listCourses);
        add(listScrollerCourses);

        /* construim panel-ul cu datele unui curs */
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

        /* lista de asistenti ai cursului */
        listModelAssistants = new DefaultListModel<>();
        listAssistants = new JList(listModelAssistants);

        JScrollPane listScrollerAssistants = new JScrollPane(listAssistants);
        panelCourse.add(listScrollerAssistants);

        add(panelCourse);

        pack();
        setVisible(true);
    }

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

            /* cautam asistentul asociat studentului selectat */
            for(Map.Entry<String, Group> entry :
                    listModelCourses.getElementAt(select).getGroups().entrySet()){

                if(entry.getValue().contains(current_student)){
                    textAssistant.setText(entry
                                          .getValue()
                                          .getAssistant()
                                          .toString());
                }
            }

            /* setam nota studentului la curs, daca exista */
            if(current_course.getGrade(current_student) != null) {
                textPartial.setText(current_course
                                    .getGrade(current_student)
                                    .getPartialScore()
                                    .toString());
                textExam.setText(current_course
                                 .getGrade(current_student)
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