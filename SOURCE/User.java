public abstract class User implements Comparable{
    private String firstName, lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /* metode getter */
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /*
     * metode folosite pentru ordonarea colectiilor
     * de useri / verificarea apartenentei la acestea
     */
    public boolean equals(Object o) {
        User obj = (User) o;

        /* consideram doi useri identici daca au acelasi nume */
        if(obj.firstName.compareTo(this.firstName) == 0 &&
            obj.lastName.compareTo(this.lastName) == 0)
            return true;

        return false;
    }

    public int compareTo(Object o) {
        User obj = (User) o;

        /* construim comparatorul pentru o sortare alfabetica */
        if(obj.lastName.compareTo(this.lastName) > 0)
            return 1;

        if(obj.lastName.compareTo(this.lastName) < 0)
            return -1;

        if(obj.lastName.compareTo(this.lastName) == 0) {
            if(obj.firstName.compareTo(this.firstName) > 0)
                return 1;

            if(obj.firstName.compareTo(this.firstName) < 0)
                return -1;

            if(obj.firstName.compareTo(this.firstName) == 0)
                return 0;
        }

        return 0;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
