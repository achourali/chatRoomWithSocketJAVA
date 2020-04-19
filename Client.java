import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        String id = "ia";
        Socket s = new Socket("localhost", 1234);
        Scanner sc = new Scanner(System.in);
        




        PrintStream p = new PrintStream(s.getOutputStream());
        Scanner sc2 = new Scanner(s.getInputStream());
        String peutPasser = "";
        
        p.println(id);
        peutPasser = sc2.nextLine();
        

        if (peutPasser.contentEquals("OK")) {
            

            
            
            String listeClient=sc2.nextLine();
            if (!listeClient.contentEquals("")) listeClient="CONNECTED CLIENTS :"+listeClient;
            

            Window window=new Window(s,id,"welcome to chat ,"+listeClient +" .write exit and press enter to quit : ");
            window.run();
            
            
            
        }else System.out.println("ID existe deja !!!");

        sc.close();

    }

}