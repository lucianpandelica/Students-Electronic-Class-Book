import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SingleTeacherPage extends JFrame {

    /* componente folosite */
    Teacher current_teacher;

    JList listCourses;
    JList listGrades;

    JLabel labelCourses;
    JLabel labelGrades;

    JButton buttonValidate;

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

    public SingleTeacherPage(Teacher teacher){
        super("Teacher's profile: " +
                    teacher.getFirstName() +
                    " " +
                    teacher.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        current_teacher = teacher;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        labelCourses = new JLabel("Courses");
        add(labelCourses, c);

        labelGrades = new JLabel("Grades to validate");
        c.gridx = 1;
        add(labelGrades, c);

        JPanel data = new JPanel();
        data.setLayout(new GridLayout(0, 2));

        listModelCourses = new DefaultListModel<>();
        listCourses = new JList(listModelCourses);
        JScrollPane listScrollerCourses = new JScrollPane(listCourses);
        data.add(listScrollerCourses);

        listModelGrades = new DefaultListModel<>();
        listGrades = new JList(listModelGrades);
        JScrollPane listScrollerGrades = new JScrollPane(listGrades);
        data.add(listScrollerGrades);

        int ct = 0;
        for(Course course : Catalog.getInstance().courses){
            if(course.getTeacher().equals(current_teacher)){
                listModelCourses.add(ct, course);
                ct++;
            }
        }

        ct = 0;
        ScoreVisitor current_scores = Catalog.getInstance().getScores();
        ArrayList<String> current_grades = current_scores
                                            .getTeachersGrades(current_teacher);

        for(String grade : current_grades){
            listModelGrades.add(ct, grade);
            ct++;
        }

        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;

        add(data, c);

        buttonValidate = new JButton("Validate grades");
        buttonValidate.addActionListener(new SingleTeacherPage.Action());
        c.gridy = 4;
        add(buttonValidate, c);

        pack();
        setVisible(true);
    }

    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {

                ScoreVisitor current_scores = Catalog.getInstance().getScores();
                current_teacher.accept(current_scores);
                fillTeacherGrades(current_teacher);
            }
        }
    }
}