import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.Vector;

public class SendToClient extends Thread {

    private Socket s;
    private Vector<Message> v;
    public int status;

    public SendToClient(Socket s, Vector<Message> v) {
        this.s = s;
        this.v = v;
        status = 1;
    }

    public void run() {

        try {
            int read = -1;
            ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

            while (status != 0) {
                sleep(200);
                read = send(p, v, read + 1);

            }

            p.writeObject(new Message("status", "deconnecté"));// envoyer pour librer le ReceiverFromServer en cas de exit
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int send(ObjectOutputStream p, Vector<Message> v, int debut) throws Exception {

        for (int i = debut; i < v.size(); i++) {
            p.writeObject(v.elementAt(i));
        }

        return v.size() - 1;
    }

}