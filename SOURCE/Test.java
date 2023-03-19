import java.util.*;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {

        int i = 0;

        /* students */
        Student student = (Student) UserFactory.getUser(UserFactory.UserType.Student, "Mihai", "Popescu");
        Parent father = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Cornel", "Popescu");
        Parent mother = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Mihaela", "Popescu");

        student.setMother(mother);
        student.setFather(father);

        Student student2 = (Student) UserFactory.getUser(UserFactory.UserType.Student, "Horia", "Danciulescu");
        Parent father2 = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Ion", "Danciulescu");
        Parent mother2 = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Corina", "Danciulescu");

        student2.setMother(mother2);
        student2.setFather(father2);

        Student student3 = (Student) UserFactory.getUser(UserFactory.UserType.Student, "Matei", "Ivanescu");
        Parent father3 = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Andrei", "Ivanescu");
        Parent mother3 = (Parent) UserFactory.getUser(UserFactory.UserType.Parent, "Felicia", "Ivanescu");

        student3.setMother(mother3);
        student3.setFather(father3);

        /* grades */
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse("ALGAED");
        grade.setPartialScore(5.0);
        grade.setExamScore(4.0);

        Grade grade2 = new Grade();
        grade2.setStudent(student2);
        grade2.setCourse("ALGAED");
        grade2.setPartialScore(2.0);
        grade2.setExamScore(2.5);

        Grade grade3 = new Grade();
        grade3.setStudent(student3);
        grade3.setCourse("ALGAED");
        grade3.setPartialScore(6.0);
        grade3.setExamScore(3.5);

        /* assistants */
        Assistant assistant = (Assistant) UserFactory.getUser(UserFactory.UserType.Assistant, "Razvan", "Ionescu");
        Assistant assistant2 = (Assistant) UserFactory.getUser(UserFactory.UserType.Assistant, "Mihai", "Turcescu");
        Assistant assistant3 = (Assistant) UserFactory.getUser(UserFactory.UserType.Assistant, "Ionut", "Bratu");
        Assistant assistant4 = (Assistant) UserFactory.getUser(UserFactory.UserType.Assistant, "Ionut", "Zatu");

        /* group */
        StudentComparator comp = new StudentComparator();
        Group group = new Group("321CC", assistant, comp);


        /* partial course */
        Teacher teacher_part = new Teacher("Adriana", "Balan");
        PartialCourse part = (PartialCourse) new PartialCourse.PartialCourseBuilder("ALGAED", teacher_part, 5)
                .assistants(new TreeSet<>())
                .grades(new TreeSet<>())
                .groups(new TreeMap<>())
                .strategy(new BestPartialScore())
                .build();

        Catalog.getInstance().addCourse(part);

        /* add groups to partial course */

        part.addGroup(group);
        part.addGroup("322CC", assistant);
        part.addAssistant("321CC", assistant2);
        part.addGroup("323CC", assistant3, new StudentComparator());
        part.addAssistant("323CC", assistant4);

        /* add students to group */
        part.addStudent("321CC", student);
        part.addStudent("321CC", student2);
        part.addStudent("321CC", student3);

        /* add grades to partial course */
        //part.addGrade(grade);
        //part.addGrade(grade2);
        //part.addGrade(grade3);

        ScoreVisitor sc = new ScoreVisitor();
        sc.addPartialGrade(assistant2, student, "ALGAED", 5.0);
        sc.addPartialGrade(assistant2, student2, "ALGAED", 2.0);
        sc.addPartialGrade(assistant, student3, "ALGAED", 6.0);

        sc.addExamGrade(teacher_part, student, "ALGAED", 4.0);
        sc.addExamGrade(teacher_part, student2, "ALGAED", 2.5);
        sc.addExamGrade(teacher_part, student3, "ALGAED", 3.5);

        assistant2.accept(sc);
        assistant.accept(sc);

        part.makeBackup();

        teacher_part.accept(sc);
        student.setMother(new Parent("Cecilia", "Marinescu"));

        System.out.println("Grades inainte de undo:\n");
        for(Grade g : part.getGrades()) {
            System.out.println(g.getStudent() + "\npartialScore: " + g.getPartialScore() + "\nexamScore: " + g.getExamScore() + "\n");
        }

        part.undo();

        System.out.println("Grades dupa undo:\n");
        for(Grade g : part.getGrades()) {
            System.out.println(g.getStudent() + "\npartialScore: " + g.getPartialScore() + "\nexamScore: " + g.getExamScore() + "\n");
        }

        /* partial course about */
        System.out.println(part + "\n");

        /* partial course assistants */
        TreeSet<Assistant> assistants = part.getAssistants();

        System.out.println("Assistants: \n");

        for(Assistant a : assistants)
            System.out.println(a);

        System.out.println("\n");

        /* partial course groups */
        System.out.println("Groups: \n");

        for(Map.Entry<String, Group> entry : part.getGroups().entrySet()) {
            System.out.println(entry.getValue());
        }

        /* partial course students */
        System.out.println("All students:\n");

        ArrayList<Student> students = part.getAllStudents();
        for(i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
        }

        /* partial course student grades */
        HashMap<Student, Grade> res = part.getAllStudentGrades();

        System.out.println("\nGrades from HashMap:\n");

        for (Map.Entry<Student, Grade> set : res.entrySet()) {

            System.out.println(set.getKey() + " = " + set.getValue().getTotal());
        }

        System.out.println("\nGrades from TreeSet (ordered by total score):\n");

        for(Grade g : part.getGrades()) {
            System.out.println(g.getStudent() + " = " + g.getTotal());
        }

        /* partial course graduated students */
        ArrayList<Student> gradStudents = part.getGraduatedStudents();

        System.out.println("\nGraduated students:\n");

        for(i = 0; i < gradStudents.size(); i++) {
            System.out.println(gradStudents.get(i));
        }

        /* full course */
        FullCourse full = (FullCourse) new FullCourse.FullCourseBuilder("USO", new Teacher("Razvan", "Deaconescu"), 4)
                .assistants(new TreeSet<>())
                .grades(new TreeSet<>())
                .groups(new TreeMap<>())
                .strategy(new BestTotalScore())
                .build();

        Grade grade4 = new Grade();
        grade4.setStudent(student);
        grade4.setCourse("USO");
        grade4.setPartialScore(3.0);
        grade4.setExamScore(5.0);

        Grade grade5 = new Grade();
        grade5.setStudent(student2);
        grade5.setCourse("USO");
        grade5.setPartialScore(5.0);
        grade5.setExamScore(4.0);

        full.addGrade(grade4);
        full.addGrade(grade5);

        /* full course about */
        System.out.println("-----------------------------------------\n");
        System.out.println(full + "\n");

        /* full course graduated students */
        ArrayList<Student> gradStudentsFull = full.getGraduatedStudents();

        System.out.println("\nGraduated students (full course):\n");

        for(i = 0; i < gradStudentsFull.size(); i++) {
            System.out.println(gradStudentsFull.get(i));
        }

        System.out.println("-----------------------------------------\n");

        Catalog.getInstance().addCourse(full);

        System.out.println("Cursuri in catalog:\n");
        for(i = 0; i < Catalog.getInstance().courses.size(); i++) {
            System.out.println(Catalog.getInstance().courses.get(i).getName());
            System.out.println("Best student:\n" + Catalog.getInstance().courses.get(i).getBestStudent());
        }
    }
}