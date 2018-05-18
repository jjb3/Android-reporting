package edu.gatech.reporter.utils;

/**
 * Created by Wendi on 2016/10/23.
 */

public class Debug {
    public static boolean enableDebugInfo = true;
    public static void print(String message){
        if(enableDebugInfo)
            System.out.println(message);
    }
}
