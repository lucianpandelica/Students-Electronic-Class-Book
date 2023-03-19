public class BestExamScore implements Strategy{
    @Override
    public Student getBest(Course course) {
        Student res = null;
        Double max = 0.0;

        /* determinam studentul cu nota maxima pentru examen */
        for(Grade g : course.getGrades()) {
            if(g.getExamScore() > max) {
                max = g.getExamScore();
                res = g.getStudent();
            }
        }

        return res;
    }
}