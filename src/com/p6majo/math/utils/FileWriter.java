package com.p6majo.math.utils;

import java.io.File;
import java.io.PrintWriter;

public class FileWriter {

    public static void writeStringToFile(String filename,String data) {
       try {
           File file = new File(filename);
           PrintWriter writer = new PrintWriter(file, "UTF-8");
           writer.write(data);
           writer.close();
       }
       catch(Exception ex){
           Utils.errorMsg(ex.getMessage());
       }
    }
}
