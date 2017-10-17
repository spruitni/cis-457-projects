import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
/************************************************************
 * File Class
 * Author: Nick Spruit
 * Date: October 17, 2017
 * Desc: This code is reusable - it makes sure that a file
 * exists, is a file, and is a test file 
 ***********************************************************/
class FileClass{
    public static File fileExists(String filePath){
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