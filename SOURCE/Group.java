import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Group extends Vector<Student> {
    private Comparator<Student> comp;
    private Assistant assistant;
    private String ID;

    /* constructorii clasei */
    public Group(String ID, Assistant assistant, Comparator<Student> comp) {
        this.ID = ID;
        this.assistant = assistant;
        this.comp = comp;
    }

    public Group(String ID, Assistant assistant) {
        this.ID = ID;
        this.assistant = assistant;
        this.comp = null;
    }

    public void setComp(Comparator<Student> comp) {
        this.comp = comp;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public String getID() {
        return ID;
    }

    /* metoda de adaugare student la grupa */
    public void addStudent(Student student)
    {
        add(student);

        /* verificam daca a fost setat un comparator */
        if(comp != null)
            /* in caz afirmativ, sortam colectia folosind comparatorul */
            Collections.sort(this, comp);
        else
            /* altfel, transmitem un mesaj de eroare */
            System.out.println("Please set comparator!");
    }

    public synchronized boolean equals(Object o) {
        Group obj = (Group) o;

        /* consideram doua grupe egale daca acestea au acelasi ID */
        if(this.ID.compareTo(obj.ID) == 0)
            return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();

        res.append("\nassistant: " +
                    this.assistant +
                    "\nID: " +
                    this.ID +
                    "\nstudents:\n");

        int i;
        for(i = 0; i < this.size(); i++) {
            res.append(this.get(i) + "\n");
        }

        return res.toString();
    }
}