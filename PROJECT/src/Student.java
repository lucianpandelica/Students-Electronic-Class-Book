public class Student extends User{

    private Parent father = null;
    private Parent mother = null;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);

        /* adaugam studentul in catalog */
        Catalog.getInstance().addStudent(this);
    }

    /* metode setter pentru campurile corespunzatoare parintilor */
    public void setMother(Parent mother){
        this.mother = mother;

        /* adaugam parintele ca observator la catalog */
        Catalog.getInstance().addObserver(mother);
    }

    public void setFather(Parent father){
        this.father = father;

        /* adaugam parintele ca observator la catalog */
        Catalog.getInstance().addObserver(father);
    }

    /* metode getter pentru campurile corespunzatoare parintilor */
    public Parent getMother() {
        return mother;
    }

    public Parent getFather() {
        return father;
    }

    /* suprascriem metoda equals() mostenita din clasa User */
    public boolean equals(Object o) {

        /*
         * consideram doi studenti identici daca acestia au aceleasi nume,
         * respectiv parintii lor au aceleasi nume
         */
        if(super.equals(o)) {
            Student obj = (Student) o;
            int equal = 1;

            if(obj.father != null && this.father != null)
                if(!obj.father.equals(this.father))
                    equal = 0;

            if(obj.mother != null && this.mother != null)
                if(!obj.mother.equals(this.mother))
                    equal = 0;

            if(equal == 1)
                return true;
        }

        return false;
    }

    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append(super.toString());

        if(this.father != null)
            res.append("; \nFather: " + father.toString());

        if(this.mother != null)
            res.append("; \nMother: " + mother.toString());

        res.append("\n");

        return res.toString();
    }
}