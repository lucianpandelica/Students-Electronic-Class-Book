import java.util.ArrayList;

public class PartialCourse extends Course{

    /*
     * metoda pentru determinarea studentilor promovati, dupa
     * conditia impusa de clasa PartialCourse
     */
    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> res = new ArrayList<>();

        for(Grade g : getGrades()) {
            /* testam conditia */
            if(g.getTotal() >= 5) {
                res.add(g.getStudent());
            }
        }

        return res;
    }

    private PartialCourse(PartialCourseBuilder builder) {
        super(builder);
    }

    /* extendiem clasa builder CourseBuilder */
    public static class PartialCourseBuilder extends Course.CourseBuilder{

        public PartialCourseBuilder(String name, Teacher teacher, int points) {
            super(name, teacher, points);
        }

        /* implementam metoda build() din CourseBuilder */
        @Override
        public Course build() {
            return new PartialCourse(this);
        }
    }
}