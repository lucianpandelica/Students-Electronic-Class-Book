import java.util.ArrayList;

public class FullCourse extends Course{

    /*
     * metoda pentru determinarea studentilor promovati, dupa
     * conditia impusa de clasa FullCourse
     */
    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> res = new ArrayList<>();

        for(Grade g : getGrades()) {
            /* testam conditia */
            if(g.getPartialScore() >= 3 && g.getExamScore() >= 2) {
                res.add(g.getStudent());
            }
        }

        return res;
    }

    private FullCourse(FullCourse.FullCourseBuilder builder) {
        super(builder);
    }

    /* extendiem clasa builder CourseBuilder */
    public static class FullCourseBuilder extends Course.CourseBuilder{

        public FullCourseBuilder(String name, Teacher teacher, int points) {
            super(name, teacher, points);
        }

        /* implementam metoda build() din CourseBuilder */
        @Override
        public Course build() {
            return new FullCourse(this);
        }
    }
}