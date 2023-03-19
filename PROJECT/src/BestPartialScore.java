public class BestPartialScore implements Strategy{
    @Override
    public Student getBest(Course course) {
        Student res = null;
        Double max = 0.0;

        /* determinam studentul cu nota maxima pentru partial */
        for(Grade g : course.getGrades()) {
            if(g.getPartialScore() > max) {
                max = g.getPartialScore();
                res = g.getStudent();
            }
        }

        return res;
    }
}