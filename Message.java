import java.io.Serializable;
 
public class Message implements Serializable{

    public String id;
    public String data;

    public Message(String s1,String s2){
        id=s1;
        data=s2;
    }
    public Message(){

    }

}