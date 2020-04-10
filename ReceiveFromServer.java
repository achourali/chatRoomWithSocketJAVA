import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiveFromServer extends Thread {
    private Socket s;
    public int status;
    private String id;

    public ReceiveFromServer(Socket s, String id) {
        this.s = s;
        this.id = id;
        status = 1;
    }

    public void run() {
        /*
         * ce thread s'occupe de la reception des messages envoyés du server et les
         * affichés sur le terminal du client
         */
        try {

            ObjectInputStream sc = new ObjectInputStream(s.getInputStream());
            Message msg;
            while (status != 0) {
                msg = (Message) sc.readObject();
                if (!msg.id.contentEquals(id))
                    System.out.println(msg.id + " : " + msg.data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
