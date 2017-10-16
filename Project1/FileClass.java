import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

class FileClass{
    public static File fileExists(String filePath){
        final String textExt = "txt";
        File file = new File(filePath);
        if(file.exists()){
            if(file.isFile()){
                if(filePath.endsWith(".txt")){
                    return file;
                }
            }
        }
        return null;
    }
}