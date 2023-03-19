import java.util.*;

public abstract class Course {
    private final String name;
    private final Teacher teacher;
    private final TreeSet<Assistant> assistants;
    private final TreeSet<Grade> grades;
    private final TreeMap<String, Group> groups;
    private final int points;
    private final Strategy strategy;

    private Snapshot savedGrades = new Snapshot();

    public Course(CourseBuilder builder) {
        this.name = builder.name;
        this.teacher = builder.teacher;
        this.points = builder.points;
        this.assistants = builder.assistants;
        this.grades = builder.grades;
        this.groups = builder.groups;
        this.strategy = builder.strategy;
    }

    /* metode getter, cele de tip setter nefiind necesare */
    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public TreeSet<Assistant> getAssistants() {
        return assistants;
    }

    public TreeSet<Grade> getGrades() {
        return grades;
    }

    public TreeMap<String, Group> getGroups() {
        return groups;
    }

    public int getPoints() {
        return points;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    /*
     * seteaza asistentul in grupa cu ID-ul indicat
     * daca nu exista deja, adauga asistentul si în multimea asistentilor
     */
    public void addAssistant(String ID, Assistant assistant) {
        Assistant oldAssist;
        boolean found = false;

        /* verificam daca exista grupa specificata */
        if(groups.containsKey(ID)) {

            /* in caz afirmativ, retinem asistentul curent al grupei */
            oldAssist = groups.get(ID).getAssistant();
            /* atribuim apoi grupei noul asistent */
            groups.get(ID).setAssistant(assistant);

            /*
             * daca vechiul asistent avea acest rol doar pentru grupa ID,
             * il eliminam din multimea de asistenti
             */
            for(Map.Entry<String, Group> entry : getGroups().entrySet()) {
                if(entry.getValue().getAssistant().equals(oldAssist)) {
                    found = true;
                }
            }

            if(found == false) {
                assistants.remove(oldAssist);
            }

            /*
             * adaugam noul asistent la multimea de asistenti a cursului,
             * daca nu il contine deja
             */
            if(assistants.contains(assistant) == false) {
                assistants.add(assistant);
            }
        }
        else {

            /* in caz negativ, afisam un mesaj de eroare */
            System.out.println("Error: Nu se poate seta asistentul " +
                                    "pentru ca grupa nu exista!");
        }
    }

    /* adauga studentul în grupa cu ID-ul indicat */
    public void addStudent(String ID, Student student) {

        for(Map.Entry<String, Group> entry : groups.entrySet()){
            if(entry.getValue().contains(student)){
                System.out.println("Studentul apartine deja unei grupe!");
                return;
            }
        }

        /*
         * verificam daca exista grupa 'ID' si daca
         * aceasta nu contine studentul 'student'
         */
        if(groups.containsKey(ID) && (!groups.get(ID).contains(student))) {

            /* in caz afirmativ, adaugam studentul la grupa 'ID' */
            groups.get(ID).addStudent(student);
        }
        else {
            /* altfel, afisam un mesaj de eroare */
            System.out.println("Error: Nu se poate adauga studentul pentru ca " +
                                "grupa nu exista / exista deja studentul in grupa!");
        }
    }

    /* adauga grupa */
    public void addGroup(Group group) {

        /* verificam daca grupa este deja asociata cursului */
        if(!groups.containsKey(group.getID())) {

            /* in caz negativ, o adaugam */
            groups.put(group.getID(), group);

            /* adaugam asistentul grupei la multimea de asistenti */
            if(assistants.contains(group.getAssistant()) == false) {
                assistants.add(group.getAssistant());
            }
        }
        else {

            /* altfel, afisam un mesaj de eroare */
            System.out.println("Error: Grupa exista deja!");
        }
    }

    /* instantiaza o grupa si o adauga */
    public void addGroup(String ID, Assistant assistant) {

        /* instatiem noul obiect */
        Group new_group = new Group(ID, assistant);
        /* setam comparatorul implicit */
        new_group.setComp(new StudentComparator());
        /* adaugam grupa */
        addGroup(new_group);

        /* adaugam asistentul la multimea de asistenti */
        if(assistants.contains(assistant) == false) {
            assistants.add(assistant);
        }
    }

    /* instantiaza o grupa si o adauga */
    public void addGroup(String ID, Assistant assist, Comparator<Student> comp) {
        /* instatiem noul obiect */
        Group new_group = new Group(ID, assist, comp);
        /* adaugam grupa */
        addGroup(new_group);

        /* adaugam asistentul la multimea de asistenti */
        if(assistants.contains(assist) == false) {
            assistants.add(assist);
        }
    }

    /* returneaza nota unui student sau null */
    public Grade getGrade(Student student) {

        /* parcurgem colectia de obiecte tip Grade a cursului */
        for(Grade g : grades) {
            /* verificam daca nota apartine studentului cautat */
            if(g.getStudent().equals(student)) {
                return g;
            }
        }

        return null;
    }

    /* adauga o nota */
    public void addGrade(Grade grade) {

        /*
         * parcurgem grupele asociate cursului si verificam daca studentul
         * caruia dorim sa ii asociem o nota face parte dintr-o grupa
         * (a fost adaugat la curs)
         */
        int found = 0;
        for(Map.Entry<String, Group> entry : groups.entrySet()){
            if(entry.getValue().contains(grade.getStudent())){
                found = 1;
            }
        }

        /* daca nu am gasit studentul, afisam un mesaj de eroare */
        if(found == 0){
            System.out.println("Studentul nu a fost adaugat la acest curs!");
            return;
        }

        if(grades.contains(grade))
            System.out.println("Dublura!");

        /* altfel, adaugam nota */

        /* verificam daca exista o nota pentru acelasi student la acest curs */
        for(Grade g : this.grades){
            if(g.getStudent().equals(grade.getStudent())){
                /* in caz afirmativ, actualizam nota */
                g.setPartialScore(grade.getPartialScore());
                g.setExamScore(grade.getExamScore());

                return;
            }
        }

        /* in caz negativ, adaugam nota */
        grades.add(grade);
    }

    /* returneaza o lista cu toti studentii */
    public ArrayList<Student> getAllStudents() {

        /* instantiem lista */
        ArrayList<Student> res = new ArrayList<>();

        /*
         * parcurgem grupele asociate cursului si adaugam
         * toti studentii acestora la lista
         */
        Set<String> keySet = groups.keySet();

        int i;
        for(String key : keySet) {
            for(i = 0; i < groups.get(key).size(); i++) {
                res.add(groups.get(key).get(i));
            }
        }

        return res;
    }

    /* returneaza un dictionar cu situatia studentilor */
    public HashMap<Student, Grade> getAllStudentGrades() {

        /* instantiem dictionarul */
        HashMap<Student, Grade> res = new HashMap<>();

        /* adaugam perechi (student, nota) */
        for(Grade g : grades) {
            res.put(g.getStudent(), g);
        }

        return res;
    }

    /* metoda ce o sa fie implementata pentru a determina studentii promovati */
    public abstract ArrayList<Student> getGraduatedStudents();

    /*
     * va returna cel mai bun student tinand cont de strategia
     * aleasa de profesor pentru curs
     */
    public Student getBestStudent() {
        return strategy.getBest(this);
    }

    public String toString() {
        return "Course: " +
                this.name +
                "; \nTeacher: " +
                this.teacher.toString() +
                "; \nPoints: " +
                this.points;
    }

    public static abstract class CourseBuilder {

        /*
         * campurile marcate drept "final" sunt cele
         * strict necesare la instantiere
         */
        private final String name;
        private final Teacher teacher;
        private TreeSet<Assistant> assistants;
        private TreeSet<Grade> grades;
        private TreeMap<String, Group> groups;
        private final int points;
        private Strategy strategy;

        /* constructor cu atributele strict necesare drept parametrii */
        public CourseBuilder(String name, Teacher teacher, int points) {
            this.name = name;
            this.teacher = teacher;
            this.points = points;
        }

        /* metode pentru setarea atributelor optionale */
        public CourseBuilder assistants(TreeSet<Assistant> assistants) {
            this.assistants = assistants;
            return this;
        }

        public CourseBuilder grades(TreeSet<Grade> grades) {
            this.grades = grades;
            return this;
        }

        public CourseBuilder groups(TreeMap<String, Group> groups) {
            this.groups = groups;
            return this;
        }

        public CourseBuilder strategy(Strategy strategy) {
            this.strategy = strategy;
            return this;
        }

        /*
         * metoda build() pentru returnarea obiectului construit,
         * ce va fi definita in cadrul claselor PartialCourse si FullCourse
         */
        public abstract Course build();
    }

    /* clasa folosita pentru stocarea notelor */
    private class Snapshot{

        /* copia colectiei de note */
        private TreeSet<Grade> grades_clone = null;

        /* metoda pentru salvarea unei copii (metoda setter) */
        public void saveGrades(TreeSet<Grade> grades_clone){
            this.grades_clone = grades_clone;
        }

        /* metoda getter */
        public TreeSet<Grade> getGrades(){
            return grades_clone;
        }
    }

    /*
     * metoda pentru construirea unei copii a
     * colectiei de note si salvarea ei
     */
    public void makeBackup() throws CloneNotSupportedException {
        TreeSet<Grade> backUp = new TreeSet<>();

        for(Grade g : this.grades) {
            backUp.add((Grade) g.clone());
        }

        savedGrades.saveGrades(backUp);
    }

    /*
     * metoda pentru restaurarea colectiei de note la ultima
     * copie salvata
     */
    public void undo() {
        this.grades.clear();

        for(Grade g : savedGrades.getGrades()) {
            this.grades.add(g);
        }
    }
}