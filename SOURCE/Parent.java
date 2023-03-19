public class Parent extends User implements Observer{

    /* toate notificarile primite de acest parinte */
    public StringBuffer notif;

    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
        notif = new StringBuffer();
    }

    @Override
    public void update(Notification notification) {
        System.out.println("Notification for parent " + this + ":\n" + notification);
        this.notif.append("Notification for parent " + this + ":\n" + notification + "\n");
    }
}
