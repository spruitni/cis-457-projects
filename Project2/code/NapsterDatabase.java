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
    static ArrayList<String[]> users;
    static ArrayList<String[]> files;
    public NapsterDatabase(){
        users = new ArrayList<String[]>();
        files = new ArrayList<String[]>(); 
    }

    //Add user to the "users" table
    public static void addUser(String[] userInfo){
        users.add(userInfo);
        printUsers();
    }

    //Parse JSON file and Add file information to the "files" table
    public static void addFileInfo(){
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
            JSONArray filesArray = (JSONArray) jsonObject.get("Files");
            Iterator<JSONObject> iterator = filesArray.iterator();
            while(iterator.hasNext()){
                JSONObject jsonObject2 = iterator.next(); 
                String[] fileInfo = new String[4];
                fileInfo[0] = (String) jsonObject2.get("Username");
                fileInfo[1] = (String) jsonObject2.get("Filename");
                fileInfo[2] = (String) jsonObject2.get("Description");
                for(String[] user : users){
                    if(user[0].equals(fileInfo[0])){
                        fileInfo[3] = user[3];
                        break;
                    }
                }
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

    //Search for a keyword and return any that match
    public static ArrayList<String[]> search(String searchDesc){
        ArrayList<String[]> results = new ArrayList<String[]>();
        for(String[] file : files){
            if(file[2].toLowerCase().contains(searchDesc.toLowerCase())){   
                results.add(file);
            }
        }
        return results;
    }

    //Testing only - display all users and files in database
    public static void printUsers(){
        for(String[] user : users){
            for(String userInfo : user){
                System.out.println(userInfo);
            }
        }
    }
    public static void printFiles(){
        for(String[] file : files){
            for(String fileInfo : file){
                System.out.println(fileInfo);
            }
        }
    }
}