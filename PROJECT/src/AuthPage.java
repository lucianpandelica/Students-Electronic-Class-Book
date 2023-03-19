import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

public class AuthPage extends JFrame {

    TreeSet<Assistant> assistantsList;

    JLabel labelType;
    JLabel labelFirstName;
    JLabel labelLastName;

    JTextField textFirstName;
    JTextField textLastName;

    JRadioButton studentType;
    JRadioButton parentType;
    JRadioButton teacherType;
    JRadioButton assistantType;

    ButtonGroup userType;

    JButton buttonAuth;

    public AuthPage(){
        super("Authentication page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        labelType = new JLabel("Select user type:");
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        add(labelType, c);

        userType = new ButtonGroup();

        studentType = new JRadioButton();
        studentType.setText("Student");

        parentType = new JRadioButton();
        parentType.setText("Parent");

        teacherType = new JRadioButton();
        teacherType.setText("Teacher");

        assistantType = new JRadioButton();
        assistantType.setText("Assistant");

        userType.add(studentType);
        userType.add(parentType);
        userType.add(teacherType);
        userType.add(assistantType);

        c.gridy = 1;
        add(studentType, c);

        c.gridy = 2;
        add(parentType, c);

        c.gridy = 3;
        add(teacherType, c);

        c.gridy = 4;
        add(assistantType, c);

        labelFirstName = new JLabel("Prenume:");
        c.gridy = 5;
        add(labelFirstName, c);

        textFirstName = new JTextField(10);
        c.gridy = 6;
        add(textFirstName, c);

        labelLastName = new JLabel("Nume:");
        c.gridy = 7;
        add(labelLastName, c);

        textLastName = new JTextField(10);
        c.gridy = 8;
        add(textLastName, c);

        buttonAuth = new JButton("Authenticate");
        buttonAuth.addActionListener(new AuthPage.Action());

        c.gridy = 9;
        add(buttonAuth, c);

        assistantsList = new TreeSet<>();
        for(Course course : Catalog.getInstance().courses) {
            for(Assistant a : course.getAssistants()){
                assistantsList.add(a);
            }
        }

        pack();
        setVisible(true);
    }

    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {

                if(textFirstName.getText().length() == 0 ||
                    textLastName.getText().length() == 0){
                    System.out.println("Please enter first name and last name!");
                    return;
                }

                String lastName = textLastName.getText();
                String firstName = textFirstName.getText();

                if(studentType.isSelected()){
                    for(Student stud : Catalog.getInstance().getAllStudents()){
                        if(stud.getLastName().compareTo(lastName) == 0 &&
                            stud.getFirstName().compareTo(firstName) == 0){
                            SingleStudentPage newPage = new SingleStudentPage(stud);
                            return;
                        }
                    }

                    System.out.println("Didn't find the specified student!");
                }
                else if(parentType.isSelected()){
                    for(Observer obs : Catalog.getInstance().getObservers()){
                        Parent crt_parent = (Parent) obs;
                        if(crt_parent.getLastName().compareTo(lastName) == 0 &&
                            crt_parent.getFirstName().compareTo(firstName) == 0){
                            SingleParentPage newPage = new SingleParentPage(crt_parent);
                            return;
                        }
                    }

                    System.out.println("Didn't find the specified parent!");
                }
                else if(teacherType.isSelected()){
                    for(Teacher teach : Catalog.getInstance().getAllTeachers()){
                        if(teach.getLastName().compareTo(lastName) == 0 &&
                            teach.getFirstName().compareTo(firstName) == 0){
                            SingleTeacherPage newPage = new SingleTeacherPage(teach);
                            return;
                        }
                    }

                    System.out.println("Didn't find the specified teacher!");
                }
                else if(assistantType.isSelected()){
                    for(Assistant a : assistantsList){
                        if(a.getLastName().compareTo(lastName) == 0 &&
                            a.getFirstName().compareTo(firstName) == 0){
                            SingleAssistantPage newPage = new SingleAssistantPage(a);
                            return;
                        }
                    }

                    System.out.println("Didn't find the specified assistant!");
                }
                else{
                    System.out.println("Please select a user type!");
                }
            }
        }
    }
}