import java.util.*;

//The Napster "Database" which stores records in an arraylist
public class NapsterDatabase{

    //Database is made of tables for users and files
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
    public static void addFileInfo(String fileInfo){
        if(files.contains(fileInfo)){
            System.out.println("File info alread exists in the database");
        }
        else{
            files.add(fileInfo);
        }
        printFiles();    
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