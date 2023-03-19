import java.util.ArrayList;
import java.util.HashMap;

public class ScoreVisitor implements Visitor{

    /* dictionarele asociate notelor pentru partial, respectiv pentru examen */
    public HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores;
    public HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores;

    public ScoreVisitor(){
        examScores = new HashMap<>();
        partialScores = new HashMap<>();
    }

    /*
     * metoda pentru adaugarea unei note pentru examen
     * profesorul care adauga nota este dat ca parametru functiei
     */
    public void addExamGrade(Teacher teacher, Student stud, String courseName, Double gradeValue){

        /* instantiem un obiect Tuple coresp. parametrilor primiti de functie */
        Tuple<Student, String, Double> tup = new Tuple<>(stud, courseName, gradeValue);

        /* verificam daca exista profesorul in dictionar */
        if(this.examScores.containsKey(teacher)) {
            /* in caz afirmativ, adaugam obiectul Tuple la lista asociata */
            this.examScores.get(teacher).add(tup);
        }
        else {
            /* altfel, adaugam mai intai profesorul */
            this.examScores.put(teacher, new ArrayList<>());
            this.examScores.get(teacher).add(tup);
        }
    }

    /*
     * metoda pentru adaugarea unei note pentru partial
     * asistentul care adauga nota este dat ca parametru functiei
     */
    public void addPartialGrade(Assistant assistant,
                                Student stud,
                                String courseName,
                                Double gradeValue){

        /* instantiem un obiect Tuple coresp. parametrilor primiti de functie */
        Tuple<Student, String, Double> tup = new Tuple<>(stud,
                                                         courseName,
                                                         gradeValue);

        /* verificam daca exista asistentul in dictionar */
        if(this.partialScores.containsKey(assistant)) {
            /* in caz afirmativ, adaugam obiectul Tuple la lista asociata */
            this.partialScores.get(assistant).add(tup);
        }
        else {
            /* altfel, adaugam mai intai asistentul */
            this.partialScores.put(assistant, new ArrayList<>());
            this.partialScores.get(assistant).add(tup);
        }
    }

    /* clasa Tuple, implementata generic */
    private class Tuple<S,C,G>{
        S stud;
        C courseName;
        G gradeValue;

        Tuple(S stud, C courseName, G gradeValue){
            this.stud = stud;
            this.courseName = courseName;
            this.gradeValue = gradeValue;
        }

        /* metode getter */
        Student getStud(){
            return (Student)this.stud;
        }

        String getCourseName(){
            return (String)this.courseName;
        }

        Double getGradeValue(){
            return (Double)this.gradeValue;
        }
    }

    /* implementarea metodelor din interfata Visit */
    @Override
    public void visit(Assistant assistant) {

        /*
         * verificam daca exista o lista de note asociata
         * asistentului primit ca parametru
         */
        if(!this.partialScores.containsKey(assistant)) {
            /* in caz negativ, afisam un mesaj de eroare */
            System.out.println("Error: Nu exista note de validat!");
            return;
        }

        /*
         * altfel, parcurgem notele asistentului,
         * pe care le trecem in catalog
         */
        int found = 0;

        for (Tuple<Student, String, Double> t : this.partialScores.get(assistant)) {

            /*
             * instantiem un obiect de tip Grade corespondent
             * obiectului de tip Tuple curent
             */
            Grade g = new Grade();
            g.setStudent(t.getStud());
            g.setCourse(t.getCourseName());

            /* cautam cursul caruia ii este destinata nota */
            for (Course c : Catalog.getInstance().courses) {
                if (c.getName().compareTo(g.getCourse()) == 0) {

                    /* verificam daca exista o nota asociata studentului curent */
                    found = 0;
                    for (Grade course_grade : c.getGrades()) {
                        if (course_grade.equals(g)) {
                            /* in caz afirmativ, modificam nota */
                            course_grade.setPartialScore(t.getGradeValue());
                            /* notificam observatorii */
                            Catalog.getInstance().notifyObservers(course_grade);
                            found = 1;
                        }
                    }

                    /* in caz negativ, adaugam nota la curs */
                    if (found == 0) {
                        g.setPartialScore(t.getGradeValue());
                        c.addGrade(g);
                        /* notificam observatorii */
                        Catalog.getInstance().notifyObservers(g);
                    }
                }
            }
        }

        /* asistentul nu mai are note de validat */
        this.partialScores.remove(assistant);
    }

    @Override
    public void visit(Teacher teacher) {

        /*
         * verificam daca exista o lista de note asociata
         * profesorului primit ca parametru
         */
        if(!this.examScores.containsKey(teacher)) {
            System.out.println("Error: Nu exista note de validat!");
            return;
        }

        /* parcurgem notele profesorului */
        int found = 0;
        for (Tuple<Student, String, Double> t : this.examScores.get(teacher)) {

            /*
             * instantiem un obiect de tip Grade corespondent
             * obiectului de tip Tuple curent
             */
            Grade g = new Grade();
            g.setStudent(t.getStud());
            g.setCourse(t.getCourseName());

            for (Course c : Catalog.getInstance().courses) {
                if (c.getName().compareTo(g.getCourse()) == 0) {

                    /* verificam daca exista o nota asociata studentului curent */
                    found = 0;
                    for (Grade course_grade : c.getGrades()) {
                        if (course_grade.equals(g)) {
                            /* in caz afirmativ, modificam nota */
                            course_grade.setExamScore(t.getGradeValue());
                            Catalog.getInstance().notifyObservers(course_grade);
                            found = 1;
                        }
                    }

                    /* in caz negativ, adaugam nota la curs */
                    if (found == 0) {
                        g.setExamScore(t.getGradeValue());
                        c.addGrade(g);
                        Catalog.getInstance().notifyObservers(g);
                    }
                }
            }
        }

        /* profesorul nu mai are note de validat */
        this.examScores.remove(teacher);
    }

    /*
     * metoda ce returneaza o lista cu notele pe care profesorul dat ca
     * parametru le are de validat, sub forma de text
     */
    public ArrayList<String> getTeachersGrades(Teacher teacher){
        ArrayList<String> res = new ArrayList<>();

        /* verificam daca exista note de validat */
        if(!this.examScores.containsKey(teacher)) {
            /* in caz negativ, afisam un mesaj si marcam acest lucru in lista */
            System.out.println("Error: Nu exista note de validat!");
            res.add("-");
            return res;
        }

        /* in caz afirmativ, le parcurgem si formam lista */
        for(Tuple<Student, String, Double> t : this.examScores.get(teacher)){
            String new_elem = t.getStud().getFirstName() +
                              " " +
                              t.getStud().getLastName() +
                              "; " +
                              t.getCourseName() +
                              "; " +
                              t.getGradeValue();
            res.add(new_elem);
        }

        return res;
    }

    /*
     * metoda ce returneaza o lista cu notele pe care asistentul dat ca
     * parametru le are de validat, sub forma de text
     */
    public ArrayList<String> getAssistantsGrades(Assistant assistant){
        ArrayList<String> res = new ArrayList<>();

        /* verificam daca exista note de validat */
        if(!this.partialScores.containsKey(assistant)) {
            System.out.println("Error: Nu exista note de validat!");
            res.add("-");
            return res;
        }

        /* in caz afirmativ, le parcurgem si formam lista */
        for(Tuple<Student, String, Double> t : this.partialScores.get(assistant)){
            String new_elem = t.getStud().getFirstName() +
                              " " +
                              t.getStud().getLastName() +
                              "; " +
                              t.getCourseName() +
                              "; " +
                              t.getGradeValue();
            res.add(new_elem);
        }

        return res;
    }
}