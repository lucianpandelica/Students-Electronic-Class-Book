public class Notification {

    /*
     * construim notificarea notei folosind cursul pentru care aceasta este
     * atribuita, impreuna cu notele pentru partial si examen
     */
    public String course;
    public Double partial, exam;

    public Notification(String course, Double partial, Double exam) {
        this.course = course;
        this.partial = partial;
        this.exam = exam;
    }

    public String toString() {
        return "New grade from " +
                course +
                " course:\npartialScore: " +
                partial +
                "\nexamScore: " +
                exam +
                "\n";
    }
}