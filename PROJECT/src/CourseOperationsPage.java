import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CourseOperationsPage extends JFrame {

    Course current_course;

    JList listStudents;
    JList listGrades;
    JList listAssistants;

    JLabel labelTitle;
    JLabel labelTeacher;
    JLabel labelPoints;
    JLabel labelAssistants;
    JLabel labelGrades;
    JLabel labelStudents;
    JLabel A_labelLastName;
    JLabel A_labelFirstName;
    JLabel A_labelGroup;
    JLabel S_labelLastName;
    JLabel S_labelFirstName;
    JLabel S_labelGroup;
    JLabel G_labelLastName;
    JLabel G_labelFirstName;
    JLabel G_labelPart;
    JLabel G_labelExam;
    JLabel labelAddAssistant;
    JLabel labelAddStudent;
    JLabel labelAddGrade;
    JLabel labelStatistics;
    JLabel labelMeanPart;
    JLabel labelMeanExam;
    JLabel labelMeanFull;
    JLabel labelBestStudent;

    JTextField A_textLastName;
    JTextField A_textFirstName;
    JTextField A_textGroup;
    JTextField S_textLastName;
    JTextField S_textFirstName;
    JTextField S_textGroup;
    JTextField G_textLastName;
    JTextField G_textFirstName;
    JTextField G_textPart;
    JTextField G_textExam;
    JTextField textMeanPart;
    JTextField textMeanExam;
    JTextField textMeanFull;
    JTextField textBestStudent;

    JButton buttonAddAssistant;
    JButton buttonAddStudent;
    JButton buttonAddGrade;

    DefaultListModel<String> listModelStudents;
    DefaultListModel<String> listModelGrades;
    DefaultListModel<Assistant> listModelAssistants;

    public void fillStudentsList(){
        listModelStudents.removeAllElements();

        int ct = 0;
        if(current_course.getGroups() == null) {
            listModelStudents.add(0, "-");
            return;
        }

        for(Map.Entry<String, Group> entry :
                current_course.getGroups().entrySet()){
            for(Student stud : entry.getValue()){
                listModelStudents.add(ct, stud.getFirstName() +
                                            " " +
                                            stud.getLastName() +
                                            "; group: " +
                                            entry.getKey());
                ct++;
            }
        }
    }

    public void fillGradesList(){
        listModelGrades.removeAllElements();

        if(current_course.getGrades() == null){
            listModelGrades.add(0, "-");
            return;
        }

        int ct = 0;
        for(Grade g : current_course.getGrades()){
            Student stud = g.getStudent();
            String new_elem = stud.getFirstName() +
                                " " +
                                stud.getLastName() +
                                " part: " +
                                g.getPartialScore() +
                                " exam: " +
                                g.getExamScore();

            listModelGrades.add(ct, new_elem);
            ct++;
        }
    }

    public void fillAssistantsList(){
        listModelAssistants.removeAllElements();

        if(current_course.getAssistants() == null){
            return;
        }

        int ct = 0;
        for(Assistant a : current_course.getAssistants()){
            listModelAssistants.add(ct, a);
            ct++;
        }
    }

    public void calculateMeans(){
        Double sumPart = 0.0;
        Double sumExam = 0.0;
        Double sumTotal = 0.0;

        if(current_course.getGrades() == null){
            textMeanPart.setText(sumPart.toString());
            textMeanExam.setText(sumExam.toString());
            textMeanFull.setText(sumTotal.toString());
            textBestStudent.setText("");
            return;
        }

        for(Grade g : current_course.getGrades()){
            sumPart = sumPart + g.getPartialScore();
            sumExam = sumExam + g.getExamScore();
            sumTotal = sumTotal + g.getTotal();
        }

        sumPart = sumPart / current_course.getGrades().size();
        sumExam = sumExam / current_course.getGrades().size();
        sumTotal = sumTotal / current_course.getGrades().size();

        textMeanPart.setText(sumPart.toString());
        textMeanExam.setText(sumExam.toString());
        textMeanFull.setText(sumTotal.toString());

        Student best_stud = null;

        if(current_course.getBestStudent() != null){
            best_stud = current_course.getBestStudent();
            textBestStudent.setText(best_stud.getFirstName() +
                                    " " +
                                    best_stud.getLastName());
        }
    }

    public CourseOperationsPage(Course course){
        super("Course page: " + course.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        current_course = course;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(0, 10, 0, 10);
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;

        labelTitle = new JLabel(course.getName());
        c.gridwidth = 3;
        c.gridheight = 1;
        add(labelTitle, c);

        labelTeacher = new JLabel("Teacher: " + course.getTeacher());
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        c.gridwidth = 1;
        add(labelTeacher, c);

        labelPoints = new JLabel("Points: " + course.getPoints());
        c.gridy++;
        add(labelPoints, c);

        labelStudents = new JLabel("Students:");
        c.ipady = 40;
        c.gridy++;
        c.gridheight = 2;
        add(labelStudents, c);

        /* lista studenti */
        listModelStudents = new DefaultListModel<>();
        listStudents = new JList(listModelStudents);
        JScrollPane listScrollerStudents = new JScrollPane(listStudents);

        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 7;
        add(listScrollerStudents, c);

        fillStudentsList();

        labelGrades = new JLabel("Grades:");
        c.ipady = 40;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 2;
        add(labelGrades, c);

        /* lista note */
        listModelGrades = new DefaultListModel<>();
        listGrades = new JList(listModelGrades);
        JScrollPane listScrollerGrades = new JScrollPane(listGrades);

        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 9;
        add(listScrollerGrades, c);

        fillGradesList();

        labelAssistants = new JLabel("Assistants:");
        c.ipady = 40;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 2;
        add(labelAssistants, c);

        /* lista asistenti */
        listModelAssistants = new DefaultListModel<>();
        listAssistants = new JList(listModelAssistants);
        JScrollPane listScrollerAssistants = new JScrollPane(listAssistants);

        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 7;
        c.insets = new Insets(0, 10, 10, 10);
        add(listScrollerAssistants, c);

        fillAssistantsList();

        c.gridwidth = 1;
        c.insets = new Insets(0, 10, 0, 10);

        /* interfata adaugare student */
        labelAddStudent = new JLabel("Add student:");
        c.gridx++;
        c.gridy = 3;
        c.gridheight = 2;
        add(labelAddStudent, c);

        S_labelFirstName = new JLabel("First name:");
        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_labelFirstName, c);

        S_textFirstName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_textFirstName, c);

        S_labelLastName = new JLabel("Last name:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_labelLastName, c);

        S_textLastName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_textLastName, c);

        S_labelGroup = new JLabel("Group:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_labelGroup, c);

        S_textGroup = new JTextField();
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(S_textGroup, c);

        buttonAddStudent = new JButton("Add student");
        buttonAddStudent.addActionListener(new CourseOperationsPage.ActionAddStud());
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(buttonAddStudent, c);

        /* interfata adaugare nota */
        labelAddGrade = new JLabel("Add grade:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 2;
        add(labelAddGrade, c);

        G_labelFirstName = new JLabel("First name:");
        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_labelFirstName, c);

        G_textFirstName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_textFirstName, c);

        G_labelLastName = new JLabel("Last name:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_labelLastName, c);

        G_textLastName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_textLastName, c);

        G_labelPart = new JLabel("Partial:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_labelPart, c);

        G_textPart = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_textPart, c);

        G_labelExam = new JLabel("Exam:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_labelExam, c);

        G_textExam = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(G_textExam, c);

        buttonAddGrade = new JButton("Add grade");
        buttonAddGrade.addActionListener(new CourseOperationsPage.ActionAddGrade());
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(buttonAddGrade, c);

        /* interfata adaugare asistent / grupa */
        labelAddAssistant = new JLabel("Add assistant / add group:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 2;
        add(labelAddAssistant, c);

        A_labelFirstName = new JLabel("First name:");
        c.ipady = 0;
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_labelFirstName, c);

        A_textFirstName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_textFirstName, c);

        A_labelLastName = new JLabel("Last name:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_labelLastName, c);

        A_textLastName = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_textLastName, c);

        A_labelGroup = new JLabel("Group:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_labelGroup, c);

        A_textGroup = new JTextField();
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(A_textGroup, c);

        c.insets = new Insets(0, 10, 10, 10);

        buttonAddAssistant = new JButton("Add");
        buttonAddAssistant.addActionListener(new CourseOperationsPage.ActionAddAssist());
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(buttonAddAssistant, c);

        c.insets = new Insets(0, 10, 0, 20);

        /* statistici curs */
        labelStatistics = new JLabel("Course statistics");
        c.gridy = 3;
        c.gridx++;
        c.gridheight = 2;
        add(labelStatistics, c);

        labelMeanPart = new JLabel("Mean partial scores");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(labelMeanPart, c);

        textMeanPart = new JTextField(5);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(textMeanPart, c);

        labelMeanExam = new JLabel("Mean exam scores");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(labelMeanExam, c);

        textMeanExam = new JTextField(5);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(textMeanExam, c);

        labelMeanFull = new JLabel("Mean total scores");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(labelMeanFull, c);

        textMeanFull = new JTextField(5);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(textMeanFull, c);

        labelBestStudent = new JLabel("Best student");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(labelBestStudent, c);

        textBestStudent = new JTextField();
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(textBestStudent, c);

        calculateMeans();

        pack();
        setVisible(true);
    }

    class ActionAddStud implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                if(S_textFirstName.getText().length() > 0 &&
                        S_textLastName.getText().length() > 0 &&
                        S_textGroup.getText().length() > 0){
                    String firstName = S_textFirstName.getText();
                    String lastName = S_textLastName.getText();
                    String groupName = S_textGroup.getText();

                    for(Student stud : Catalog.getInstance().getAllStudents()){
                        if(stud.getLastName().compareTo(lastName) == 0 &&
                            stud.getFirstName().compareTo(firstName) == 0) {
                            current_course.addStudent(groupName, stud);
                            fillStudentsList();
                            return;
                        }
                    }

                    System.out.println("Didn't find this student!");
                }
                else{
                    System.out.println("Please enter first name, last name and group!");
                }
            }
        }
    }

    class ActionAddGrade implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                if(G_textFirstName.getText().length() > 0 &&
                        G_textLastName.getText().length() > 0 &&
                        G_textPart.getText().length() > 0 &&
                        G_textExam.getText().length() > 0){

                    String firstName = G_textFirstName.getText();
                    String lastName = G_textLastName.getText();
                    Double part = Double.parseDouble(G_textPart.getText());
                    Double exam = Double.parseDouble(G_textExam.getText());

                    for(Student stud : Catalog.getInstance().getAllStudents()){
                        if(stud.getLastName().compareTo(lastName) == 0 &&
                                stud.getFirstName().compareTo(firstName) == 0) {

                            Grade new_grade = new Grade();
                            new_grade.setCourse(current_course.getName());
                            new_grade.setStudent(stud);
                            new_grade.setPartialScore(part);
                            new_grade.setExamScore(exam);

                            current_course.addGrade(new_grade);
                            fillGradesList();
                            calculateMeans();

                            return;
                        }
                    }

                    System.out.println("Didn't find this student!");
                }
                else {
                    System.out.println("Please enter first name, last name and grades!");
                }
            }
        }
    }

    class ActionAddAssist implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                if(A_textFirstName.getText().length() > 0 &&
                        A_textLastName.getText().length() > 0 &&
                        A_textGroup.getText().length() > 0){
                    String firstName = A_textFirstName.getText();
                    String lastName = A_textLastName.getText();
                    String groupName = A_textGroup.getText();

                    Assistant new_assist = new Assistant(firstName, lastName);

                    if(!current_course.getGroups().containsKey(groupName)){
                        current_course.addGroup(groupName, new_assist);
                    }
                    else{
                        current_course.addAssistant(groupName, new_assist);
                    }

                    fillAssistantsList();
                }
                else {
                    System.out.println("Please enter first name, last name and group!");
                }
            }
        }
    }
}