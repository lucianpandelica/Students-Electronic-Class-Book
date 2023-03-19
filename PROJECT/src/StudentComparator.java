import java.util.Comparator;

public class StudentComparator implements Comparator<Student> {

    public int compare(Student o1, Student o2) {
        if(o1.getLastName().compareTo(o2.getLastName()) < 0)
        {
            return -1;
        }

        if(o1.getLastName().compareTo(o2.getLastName()) > 0)
        {
            return 1;
        }

        if(o1.getFirstName().compareTo(o2.getFirstName()) < 0)
        {
            return -1;
        }

        if(o1.getFirstName().compareTo(o2.getFirstName()) > 0)
        {
            return 1;
        }

        return 0;
    }
}