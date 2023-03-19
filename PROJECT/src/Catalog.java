import java.util.TreeSet;
import java.util.Vector;

public class Catalog implements Subject{

    /* Singleton Pattern - lazy instantiation */
    private static Catalog catalog = null;

    /* lista cu obiecte de tip Course */
    public Vector<Course> courses;

    /* multime de observatori ai catalogului (parintii studentilor) */
    public static TreeSet<Observer> observers = null;

    /* multime de studenti */
    public static TreeSet<Student> students = null;

    /* multime de profesori */
    public static TreeSet<Teacher> teachers = null;

    /* obiect de tip ScoreVisitor asociat profesorilor si asistentilor */
    public static ScoreVisitor scores = null;

    private Catalog(){
        /* pentru a nu permite instantierea */
    }

    /* metoda ce returneaza instanta unica a catalogului */
    public static Catalog getInstance(){
        if(catalog == null){
            catalog = new Catalog();
            observers = new TreeSet<>();
            students = new TreeSet<>();
            teachers = new TreeSet<>();
            scores = new ScoreVisitor();
        }
        return catalog;
    }

    /* adauga un curs in catalog */
    public void addCourse(Course course){
        if(courses == null) {
            courses = new Vector<>();
        }

        courses.add(course);
        teachers.add(course.getTeacher());
    }

    /* sterge un curs din catalog */
    public void removeCourse(Course course){
        courses.remove(course);
    }

    /* adauga un student in catalog */
    public void addStudent(Student student){
        students.add(student);
    }

    public TreeSet<Student> getAllStudents(){
        return students;
    }

    public TreeSet<Teacher> getAllTeachers(){
        return teachers;
    }

    public TreeSet<Observer> getObservers() {
        return observers;
    }

    public ScoreVisitor getScores() {
        return scores;
    }

    /* metode din interfata Subject */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Grade grade) {
        /* construim notificarea */
        Notification n = new Notification(grade.getCourse(), grade.getPartialScore(), grade.getExamScore());

        /* trimitem notificarea catre observatori */
        for(Observer o : this.getObservers()){
            Parent p = (Parent) o;

            if(grade.getStudent().getMother() != null)
                if(p.equals(grade.getStudent().getMother())){
                    o.update(n);
                }

            if(grade.getStudent().getFather() != null)
                if(p.equals(grade.getStudent().getFather())){
                    o.update(n);
                }
        }
    }
}