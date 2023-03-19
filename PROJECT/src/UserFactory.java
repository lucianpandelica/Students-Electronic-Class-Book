public class UserFactory {

    /* Factory Pattern */

    /* tipuri de useri */
    public enum UserType{
        Student,
        Parent,
        Assistant,
        Teacher
    }

    public static User getUser(UserType userType, String firstName, String lastName){

        /* identificam tipul userului si folosim constructorul corespunzator */
        switch(userType){
            case Student:
                return new Student(firstName, lastName);
            case Parent:
                return new Parent(firstName, lastName);
            case Assistant:
                return new Assistant(firstName, lastName);
            case Teacher:
                return new Teacher(firstName, lastName);
        }
        throw new IllegalArgumentException("User type " + userType + " not recognized.");
    }
}
