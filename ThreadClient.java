import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

public class ThreadClient extends Thread {

    private Socket s;
    private Vector<Message> v;
    private Vector<String> IDs;
    String mon_ID;
    public ThreadClient(Socket s, Vector<Message> v,Vector<String> IDs,String ID) {
        this.s = s;
        this.v = v;
        this.IDs=IDs;
        this.mon_ID=ID;
    }

    public void run() {
        /*
         * SendToClient est le thread qui envoie tout les messages recents au client
         * avec un temps d'actualisation de 200ms
         */
        /*
         * ce thread lance le SendToClient et puis s'occupe de la reception des messages
         * de la part du client
         */

        SendToClient sender = new SendToClient(s, v);
        sender.start();

        try {

            ObjectInputStream sc = new ObjectInputStream(s.getInputStream());

            while (true) {

                Message msg = (Message) sc.readObject();

                if (msg.data.contentEquals("exit")) {
                    sender.status = 0;
                    break;
                } else {
                    v.add(msg);
                    
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("client "+mon_ID+" out.");
        IDs.remove(mon_ID);
        
    }

}
