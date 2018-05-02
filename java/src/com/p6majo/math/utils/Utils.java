package com.p6majo.math.utils;

import org.apfloat.Apint;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static void errorMsg(String text){
        System.err.println(text);
        System.exit(0);
    }


    public static Apint ggT(Apint a, Apint b){

        if (b.compareTo(a)>0) {
            Apint tmp = a;
            a = b;
            b= tmp;
        }

        Apint ggt = b;

        while (a.mod(b).compareTo(Apint.ZERO)!=0){
            ggt = a.mod(b);
            a=b;
            b=ggt;
        }

        return ggt;
    }

    /**
     * Display an array as string
     * Optionally the string length for each object can be given
     * @param array
     * @param <T>
     * @return
     */
    public static <T extends Object> String array2String(T[] array, Integer l){
        String out="[";
        for (int i=0;i<array.length;i++)
            if (array instanceof Double[])
                if (l!=null){
                    double a = Math.round((Double) array[i]*Math.pow(10,l))/Math.pow(10,l);
                    String s = String.format("%.19f",a);
                    s=s.substring(0,l+1);
                    out+=s+";";
                }
                else out+=String.format("%.10f",array[i])+";";
            else
                out+=array[i].toString()+",";
        if (l!=null) out=out.substring(0,out.length()-2);
        else out = out.substring(0,out.length()-2);
        out+="]";
        return out;
    }

    public static<T extends Object> String array2String(T[] array){
        return array2String(array,null);
    }

    /**
     * generate URL for a file on the local storage such that it can be accessed via browsers
     * @param filename
     * @return
     */
    public static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

    /**
     * get Time string that can be used to have a unique file name
     * @return
     */
    public static String getGMTTimeString(){
        try{
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

            //Local time zone
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

            //Time in GMT
            return dateFormatLocal.parse( dateFormatGmt.format(new Date()) ).toString();
        }
        catch(ParseException ex){
            Utils.errorMsg(ex.getMessage());
        }
        return null;
    }

    public static String intArray2String(int[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket.substring(0,1));
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket.substring(1,2));
        }
        return output.toString();
    }

    public static String floatArray2String(float[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket.substring(0,1));
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(String.format("%.2f",array[i]));
            }
            if (bracket.length()>1) output.append(bracket.substring(1,2));
        }
        return output.toString();
    }

    public static String doubleArray2String(double[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket.substring(0,1));
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket.substring(1,2));
        }
        return output.toString();
    }

}
