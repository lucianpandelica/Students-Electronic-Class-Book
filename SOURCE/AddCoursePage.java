import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddCoursePage extends JFrame {

    JLabel labelPath;
    JTextField textPath;
    JButton buttonCreate;

    public AddCoursePage(){
        super("Add course page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;

        labelPath = new JLabel("Path to JSON file:");
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(labelPath, c);

        c.insets = new Insets(0, 10, 0, 10);
        textPath = new JTextField(20);
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(textPath, c);

        c.insets = new Insets(0, 10, 10, 10);
        buttonCreate = new JButton("Create course");
        buttonCreate.addActionListener(new AddCoursePage.ActionCreate());
        c.gridy = c.gridy + c.gridheight;
        c.gridheight = 1;
        add(buttonCreate, c);

        pack();
        setVisible(true);
    }

    class ActionCreate implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {

                if(textPath.getText().length() > 0) {
                    try {
                        Object parser = new JSONParser()
                                        .parse(new FileReader(textPath.getText()));

                        JSONObject jObj = (JSONObject) parser;

                        String type = (String) jObj.get("type");
                        String courseName = (String) jObj.get("courseName");
                        String teacherFirstName = (String) jObj.get("teacherFirstName");
                        String teacherLastName = (String) jObj.get("teacherLastName");
                        int points = Math.toIntExact((Long)jObj.get("points"));
                        String strategy = (String) jObj.get("strategy");

                        Teacher teacher = new Teacher(teacherFirstName, teacherLastName);

                        Strategy str = null;
                        if(strategy.compareTo("best_total") == 0){
                             str = new BestTotalScore();
                        }
                        else if(strategy.compareTo("best_partial") == 0){
                            str = new BestPartialScore();
                        }
                        else if(strategy.compareTo("best_exam") == 0){
                            str = new BestExamScore();
                        }

                        if(type.compareTo("partial") == 0){
                            PartialCourse new_part = (PartialCourse) new PartialCourse
                                    .PartialCourseBuilder(courseName, teacher, points)
                                    .assistants(new TreeSet<>())
                                    .grades(new TreeSet<>())
                                    .groups(new TreeMap<>())
                                    .strategy(str)
                                    .build();
                            Catalog.getInstance().addCourse(new_part);

                            CourseOperationsPage objc = new CourseOperationsPage(new_part);
                        }
                        else if(type.compareTo("full") == 0){
                            FullCourse new_full = (FullCourse) new FullCourse
                                    .FullCourseBuilder(courseName, teacher, points)
                                    .assistants(new TreeSet<>())
                                    .grades(new TreeSet<>())
                                    .groups(new TreeMap<>())
                                    .strategy(str)
                                    .build();
                            Catalog.getInstance().addCourse(new_full);

                            CourseOperationsPage objc = new CourseOperationsPage(new_full);
                        }

                        System.out.println("Got points: " + points);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}