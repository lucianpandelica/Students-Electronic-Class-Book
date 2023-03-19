public class Grade implements Comparable, Cloneable{
    private Double partialScore = 0.0, examScore = 0.0;
    private Student student;
    private String course; /* numele cursului */

    /* metode getter si setter */
    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getPartialScore() {
        return partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public Student getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    /* metoda pentru calculul notei */
    public Double getTotal(){
        return (this.partialScore + this.examScore);
    }

    public boolean equals(Object o) {
        Grade obj = (Grade) o;

        if(this.course.compareTo(obj.course) == 0 &&
                this.student.equals(obj.student)) {
            return true;
        }

        return false;
    }

    public int compareTo(Object o) {
        Grade obj = (Grade) o;

        if((this.getTotal() - obj.getTotal()) < 0)
            return 1;
        if((this.getTotal() - obj.getTotal()) > 0)
            return -1;

        Student stud = obj.getStudent();
        Student this_stud = this.getStudent();

        if(obj.equals(this))
            return 0;
        else if(stud.getLastName().compareTo(this_stud.getLastName()) > 0){
            return 1;
        }
        else if(stud.getLastName().compareTo(this_stud.getLastName()) < 0){
            return -1;
        }
        else {

            if(stud.getFirstName().compareTo(this_stud.getFirstName()) > 0){
                return 1;
            }
            else if(stud.getFirstName().compareTo(this_stud.getFirstName()) < 0){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    /* metoda pentru clonarea unui obiect de tip Grade */
    @Override
    protected Object clone() throws CloneNotSupportedException {

        Grade grade_clone = new Grade();
        grade_clone.setPartialScore(new Double(this.partialScore.doubleValue()));
        grade_clone.setExamScore(new Double(this.examScore.doubleValue()));

        Student student_clone = new Student(this.student.getFirstName(),
                                            this.student.getLastName());

        Parent mother_clone = new Parent(this.student.getMother().getFirstName(),
                                         this.student.getMother().getLastName());
        Parent father_clone = new Parent(this.student.getFather().getFirstName(),
                                         this.student.getFather().getLastName());

        student_clone.setMother(mother_clone);
        student_clone.setFather(father_clone);

        grade_clone.setStudent(student_clone);

        String course_clone = this.course;
        grade_clone.setCourse(course_clone);

        return grade_clone;
    }
}