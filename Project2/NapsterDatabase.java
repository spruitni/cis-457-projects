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
    static ArrayList<ArrayList<String>> users;
    static ArrayList<ArrayList<String>> files;
    public NapsterDatabase(){
        users = new ArrayList<ArrayList<String>>();
        files = new ArrayList<ArrayList<String>>(); 
    }

    //Add user to the "users" table
    public static void addUser(ArrayList<String> userInfo){
        users.add(userInfo);
        printUsers();
    }

    //Parse JSON file and Add file information to the "files" table
    public static void addFileInfo(){
        try{
            ArrayList<String> fileInfo = new ArrayList<String>();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
            JSONArray filesArray = (JSONArray) jsonObject.get("Files");
            Iterator<JSONObject> iterator = filesArray.iterator();
            while (iterator.hasNext()){
                JSONObject jsonObject2 = iterator.next(); 
                fileInfo.add((String) jsonObject2.get("Username"));
                fileInfo.add((String) jsonObject2.get("Filename"));
                fileInfo.add((String) jsonObject2.get("Description"));
                files.add(fileInfo);
            }
            printFiles();
        }
        catch(ParseException ex){
            System.out.println("Problem parsing JSON: " + ex);
        }
        catch(IOException ex){
            System.out.println("Problem writing file info: " + ex);
        }
    }

    //Testing only - display all users and files in database
    public static void printUsers(){
        for(ArrayList<String> user : users){
            for(String userInfo : user){
                System.out.println(userInfo);
            }
        }
    }
    public static void printFiles(){
        for(ArrayList<String> file : files){
            for(String fileInfo : file){
                System.out.println(fileInfo);
            }
        }
    }
}