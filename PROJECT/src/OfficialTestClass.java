import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class OfficialTestClass {

    public static Course getCourse(String courseName){
        for(Course course : Catalog.getInstance().courses){
            if(course.getName().compareTo(courseName) == 0)
                return course;
        }

        return null;
    }

    public static Student getStudent(String firstName, String lastName){
        for(Student stud : Catalog.getInstance().getAllStudents()){
            if(stud.getFirstName().compareTo(firstName) == 0 &&
                stud.getLastName().compareTo(lastName) == 0){
                return stud;
            }
        }

        return null;
    }

    public static Teacher getTeacher(String firstName, String lastName){

        for(Teacher teach : Catalog.getInstance().getAllTeachers()){
            if(teach.getFirstName().compareTo(firstName) == 0 &&
                teach.getLastName().compareTo(lastName) == 0){
                return teach;
            }
        }

        return null;
    }

    public static Assistant getAssistant(Course course, String firstName, String lastName){

        for(Assistant assist : course.getAssistants()){
            if(assist.getFirstName().compareTo(firstName) == 0 &&
                assist.getLastName().compareTo(lastName) == 0) {
                return assist;
            }
        }

        return null;
    }

    public static Parent getParent(String firstName, String lastName){

        for(Observer o : Catalog.getInstance().getObservers()){
            Parent p = (Parent) o;
            if(p.getFirstName().compareTo(firstName) == 0 &&
                p.getLastName().compareTo(lastName) == 0)
                return p;
        }

        return null;
    }

    public static void addCourseComponents(Course course, JSONObject courseObj){

        /* lista asistenti */
        JSONArray arrayAssistants = (JSONArray) courseObj.get("assistants");

        Iterator iteratorAssistants = arrayAssistants.iterator();
        while(iteratorAssistants.hasNext()){
            JSONObject newAssistant = (JSONObject) iteratorAssistants.next();

            /* preluam datele asistentului */
            String assistFirstName = (String) newAssistant.get("firstName");
            String assistLastName = (String) newAssistant.get("lastName");

            Assistant newAssist = (Assistant) UserFactory
                                    .getUser(UserFactory.UserType.Assistant,
                                    assistFirstName,
                                    assistLastName);

            /* adaugam asistentul in multimea de asistenti ai cursului */
            course.getAssistants().add(newAssist);
        }

        /* lista grupe */
        JSONArray arrayGroups = (JSONArray) courseObj.get("groups");

        Iterator iteratorGroups = arrayGroups.iterator();
        while(iteratorGroups.hasNext()){
            JSONObject newGroup = (JSONObject) iteratorGroups.next();

            /* preluam datele grupei */
            String id = (String) newGroup.get("ID");
            JSONObject assistantObj = (JSONObject) newGroup.get("assistant");

            String groupAssistantFirstName = (String) assistantObj.get("firstName");
            String groupAssistantLastName = (String) assistantObj.get("lastName");

            System.out.println(groupAssistantFirstName + " " + groupAssistantLastName + "\n");

            Assistant groupAssistant = getAssistant(course,
                                                    groupAssistantFirstName,
                                                    groupAssistantLastName);

            /* instantiem grupa si o adaugam la curs */
            course.addGroup(id, groupAssistant);

            /* lista studenti grupa */
            JSONArray arrayStudentsGroup = (JSONArray) newGroup.get("students");

            Iterator iteratorStudents = arrayStudentsGroup.iterator();
            while(iteratorStudents.hasNext()){
                JSONObject newStud = (JSONObject) iteratorStudents.next();

                String studFirstName = (String) newStud.get("firstName");
                String studLastName = (String) newStud.get("lastName");

                System.out.println(studFirstName + " " + studLastName + "\n");
                Student student = (Student) UserFactory
                        .getUser(UserFactory.UserType.Student,
                                studFirstName,
                                studLastName);

                if(newStud.containsKey("mother")){
                    JSONObject studMother = (JSONObject) newStud.get("mother");

                    String motherFirstName = (String) studMother.get("firstName");
                    String motherLastName = (String) studMother.get("lastName");

                    Parent mother = (Parent) UserFactory
                            .getUser(UserFactory.UserType.Parent,
                                    motherFirstName,
                                    motherLastName);

                    student.setMother(mother);
                }

                if(newStud.containsKey("father")){
                    JSONObject studFather = (JSONObject) newStud.get("father");

                    String fatherFirstName = (String) studFather.get("firstName");
                    String fatherLastName = (String) studFather.get("lastName");

                    Parent father = (Parent) UserFactory
                            .getUser(UserFactory.UserType.Parent,
                                    fatherFirstName,
                                    fatherLastName);

                    student.setFather(father);
                }

                /* adaugam studentul la grupa ID */
                course.addStudent(id, student);
            }
        }
    }

    public static void main(String[] args) throws IOException, ParseException {

        Object parser = new JSONParser()
                        .parse(new FileReader("catalog.json"));
        JSONObject parserObj = (JSONObject) parser;

        JSONArray arrayCourses = (JSONArray) parserObj.get("courses");
        Vector<Course> courses = new Vector<>();

        Iterator iteratorCourses = arrayCourses.iterator();
        while(iteratorCourses.hasNext()){
            JSONObject newCourse = (JSONObject) iteratorCourses.next();

            int points = 5;

            /* preluam datele principale ale cursului */
            String type = (String) newCourse.get("type");
            String strategy = (String) newCourse.get("strategy");
            String name = (String) newCourse.get("name");

            /* preluam datele profesorului */
            JSONObject teacherObj = (JSONObject) newCourse.get("teacher");
            String teacherFirstName = (String) teacherObj.get("firstName");
            String teacherLastName = (String) teacherObj.get("lastName");

            Teacher teacher = new Teacher(teacherFirstName, teacherLastName);

            /* identificam strategia cursului */
            Strategy str = null;
            if(strategy.compareTo("BestTotalScore") == 0){
                str = new BestTotalScore();
            }
            else if(strategy.compareTo("BestPartialScore") == 0){
                str = new BestPartialScore();
            }
            else if(strategy.compareTo("BestExamScore") == 0){
                str = new BestExamScore();
            }

            /* instantiem cursul */
            if(type.compareTo("PartialCourse") == 0){
                PartialCourse newPart = (PartialCourse) new PartialCourse
                        .PartialCourseBuilder(name, teacher, points)
                        .assistants(new TreeSet<>())
                        .grades(new TreeSet<>())
                        .groups(new TreeMap<>())
                        .strategy(str)
                        .build();

                Catalog.getInstance().addCourse(newPart);

                addCourseComponents(newPart, newCourse);

                courses.add(newPart);
            }
            else if(type.compareTo("FullCourse") == 0){
                FullCourse newFull = (FullCourse) new FullCourse
                        .FullCourseBuilder(name, teacher, points)
                        .assistants(new TreeSet<>())
                        .grades(new TreeSet<>())
                        .groups(new TreeMap<>())
                        .strategy(str)
                        .build();

                Catalog.getInstance().addCourse(newFull);

                addCourseComponents(newFull, newCourse);

                courses.add(newFull);
            }
        }

        JSONArray arrayExamScores = (JSONArray) parserObj.get("examScores");

        Iterator iteratorExamScores = arrayExamScores.iterator();
        while(iteratorExamScores.hasNext()) {
            JSONObject newExamScore = (JSONObject) iteratorExamScores.next();

            JSONObject teacherObj = (JSONObject) newExamScore.get("teacher");
            String teacherFirstName = (String) teacherObj.get("firstName");
            String teacherLastName = (String) teacherObj.get("lastName");

            Teacher teacherExam = getTeacher(teacherFirstName, teacherLastName);

            JSONObject studentObj = (JSONObject) newExamScore.get("student");
            String studentFirstName = (String) studentObj.get("firstName");
            String studentLastName = (String) studentObj.get("lastName");

            Student studentExam = getStudent(studentFirstName, studentLastName);

            String courseNameExam = (String) newExamScore.get("course");
            Double gradeExam = ((Number)newExamScore.get("grade")).doubleValue();

            if(teacherExam != null && studentExam != null){
                Catalog.getInstance().getScores().addExamGrade(teacherExam, studentExam, courseNameExam, gradeExam);
            }
            else {
                System.out.println("Error: Profesorul / studentul nu exista!");
            }
        }

        JSONArray arrayPartScores = (JSONArray) parserObj.get("partialScores");

        Iterator iteratorPartScores = arrayPartScores.iterator();
        while(iteratorPartScores.hasNext()) {
            JSONObject newPartScore = (JSONObject) iteratorPartScores.next();

            JSONObject assistObj = (JSONObject) newPartScore.get("assistant");
            String assistFirstName = (String) assistObj.get("firstName");
            String assistLastName = (String) assistObj.get("lastName");

            JSONObject studentObj = (JSONObject) newPartScore.get("student");
            String studentFirstName = (String) studentObj.get("firstName");
            String studentLastName = (String) studentObj.get("lastName");

            Student studentPart = getStudent(studentFirstName, studentLastName);

            String courseNamePart = (String) newPartScore.get("course");
            Double gradePart = ((Number)newPartScore.get("grade")).doubleValue();

            Course course = getCourse(courseNamePart);
            Assistant assistPart = getAssistant(course, assistFirstName, assistLastName);

            if(assistPart != null && studentPart != null){
                Catalog.getInstance().getScores().addPartialGrade(assistPart, studentPart, courseNamePart, gradePart);
            }
            else {
                System.out.println("Error: Profesorul / studentul nu exista!");
            }
        }

        //PageStudent studPage = new PageStudent();
        //PageTeacherAssistant taPage = new PageTeacherAssistant();
        //PageParent parentPage = new PageParent();

        //SingleStudentPage singleStud = new SingleStudentPage(getStudent("Iulian", "Iancu"));
        //SingleAssistantPage singleAssist = new SingleAssistantPage(getAssistant(getCourse("Electronica digitala"), "Adrian", "Sava"));
        //SingleTeacherPage singleTeach = new SingleTeacherPage(getTeacher("George", "Dinu"));
        //SingleParentPage singleParentPage = new SingleParentPage(getParent("Petra", "Tunaru"));

        AuthPage authPage = new AuthPage();
        //CourseOperationsPage courseOpPage = new CourseOperationsPage(getCourse("Electronica digitala"));
        //AddCoursePage addCPage = new AddCoursePage();
    }
}