import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        String id = args[2];
        Socket s = new Socket(args[0], Integer.parseInt(args[1]));
        Scanner sc = new Scanner(System.in);




        PrintStream p = new PrintStream(s.getOutputStream());
        Scanner sc2 = new Scanner(s.getInputStream());
        String peutPasser = "";
        
        p.println(id);
        peutPasser = sc2.nextLine();
        

        if (peutPasser.contentEquals("OK")) {
            ReceiveFromServer receiver = new ReceiveFromServer(s, id);
            ObjectOutputStream printer = new ObjectOutputStream(s.getOutputStream());

            System.out.println("bienvenue au chat , tapez exit pour quitter : ");

            receiver.start();

            String data = "";

            Message msg = new Message();

            while (!data.contentEquals("exit")) {

                data = sc.nextLine();

                msg = new Message(id, data);

                printer.writeObject(msg);

            }

            receiver.status = 0;
        }else System.out.println("ID existe deja !!!");

        sc.close();

    }

}