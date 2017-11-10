import java.util.*;

//The Napster "Database" which stores records in an arraylist
public class NapsterDatabase{

    static ArrayList<String[]> users;
    static ArrayList<String[]> files;

    public NapsterDatabase(){
        users = new ArrayList<String[]>();
        files = new ArrayList<String[]>(); 
    }

    //Add user to the "users" table
    public static void addUser(String[] userInfo){
        users.add(userInfo);
    }

    //Testing only
    public static void printUsers(){
        for(String[] user : users){
            for(String userPart: user){
                System.out.println(userPart);
            }
        }
    }
}