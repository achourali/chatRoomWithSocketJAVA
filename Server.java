
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket s1 = new ServerSocket(Integer.parseInt(args[0]));
        Vector<Message> boiteDesMessages = new Vector<Message>();
        Vector<String> IDs = new Vector<String>();
        System.out.println("server started : ");
        Scanner sc;
        String ID;
        String liste_client;
        PrintStream p;
        Message msg=new Message();
        while (true) {

            Socket s = s1.accept();
            sc = new Scanner(s.getInputStream());
            p=new PrintStream(s.getOutputStream());
            ID = sc.nextLine();

            if (!IDs.contains(ID) && !ID.contentEquals("notification")) {
                p.println("OK");
                liste_client="";
                if (!IDs.isEmpty()) liste_client=IDs.elementAt(0);
                for(int i=1;i<IDs.size();i++){
                    liste_client=liste_client+" ,"+IDs.elementAt(i);
                }


                p.println(liste_client);
                msg=new Message("notification",ID+" CONNECTED.");
                
                boiteDesMessages.add(msg);
                IDs.add(ID);
                
                
                System.out.println(ID+" connected . ");

                Thread.sleep(50);
                ThreadClient client = new ThreadClient(s, boiteDesMessages,IDs,ID);

                client.start();
                
            }else p.println("ID existe deja !!!");

        }

    }

}