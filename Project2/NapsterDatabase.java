import java.util.*;
import java.io.*; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//The Napster "Database" which stores records in an arraylist
public class NapsterDatabase{

    //Database is made of tables for users and files
    static String jsonFile = "fileInfo.json";
    static ArrayList<String> users;
    static ArrayList<String> files;
    public NapsterDatabase(){
        users = new ArrayList<String>();
        files = new ArrayList<String>(); 
    }

    //Add user to the "users" table
    public static void addUser(String userInfo){
        if(users.contains(userInfo)){
            System.out.println("User info already exists in the database");
        }
        else{
            users.add(userInfo);
        }
        printUsers();
    }

    //Add file information to the "files" table
    public static void addFileInfo(){
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(jsonFile));
            System.out.println(jsonObject);
            



            /*
            if(files.contains(fileInfo)){
                System.out.println("File info already exists in the database");
            }
            else{
                files.add(fileInfo);
            }
            printFiles();  */  
        }
        catch(ParseException ex){
            System.out.println("Problem parsing JSON");
        }
        catch(IOException ex){
            System.out.println("Problem writing file info");
        }

    }

    //Testing only - display all users and files in database
    public static void printUsers(){
        for(String user : users){
            System.out.println(user);
        }
    }
    public static void printFiles(){
        for(String file : files){
            System.out.println(file);
        }
    }
}