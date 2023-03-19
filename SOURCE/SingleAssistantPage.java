import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SingleAssistantPage extends JFrame {
    Assistant current_assistant;

    JList listCourses;
    JList listGrades;

    JLabel labelCourses;
    JLabel labelGrades;

    JButton buttonValidate;

    DefaultListModel<Course> listModelCourses;
    DefaultListModel<String> listModelGrades;

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

    public SingleAssistantPage(Assistant assistant){
        super("Assistant's profile: " +
                    assistant.getFirstName() +
                    " " +
                    assistant.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        current_assistant = assistant;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        labelCourses = new JLabel("Courses");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(labelCourses, c);

        labelGrades = new JLabel("Grades to validate");
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
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
            if(course.getAssistants().contains(current_assistant)){
                listModelCourses.add(ct, course);
                ct++;
            }
        }

        ct = 0;
        ScoreVisitor current_scores = Catalog.getInstance().getScores();
        ArrayList<String> current_grades = current_scores
                                            .getAssistantsGrades(current_assistant);

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
        buttonValidate.addActionListener(new SingleAssistantPage.Action());
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
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
                current_assistant.accept(current_scores);
                fillAssistantGrades(current_assistant);
            }
        }
    }
}