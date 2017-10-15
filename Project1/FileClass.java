import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

class FileClass{
    public static File fileExists(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            if(file.isFile()){
                return file;
            }
        }
        return null;
    }
}