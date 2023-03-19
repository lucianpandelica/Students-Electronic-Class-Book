public class BestTotalScore implements Strategy{
    @Override
    public Student getBest(Course course) {
        Student res = null;
        Double max = 0.0;

        /* determinam studentul cu nota totala maxima */
        for(Grade g : course.getGrades()) {
            if(g.getTotal() > max) {
                max = g.getTotal();
                res = g.getStudent();
            }
        }

        return res;
    }
}
