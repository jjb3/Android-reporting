package edu.gatech.reporter.utils;

/**
 * Created by Wendi on 2016/11/4.
 */

public class VectorMath {
    public static double dotProdct(double[] first, double[] second) throws NumberFormatException{
        double result = (double)0;
        if(first.length != second.length){
            throw new NumberFormatException("Dimension does not match!");
        }
        for(int i = 0; i < first.length; i++){
            result += first[i] * second[i];
        }
        return result;
    }

    public static double[] crossProduct3D(double[] first, double[] second) throws NumberFormatException{
        double[] result = new double[3];
        result[0] = 0.0;
        result[1] = 0.0;
        result[2] = 0.0;
        if(first.length != 3 || second.length != 3){
            throw new NumberFormatException("Dimension does not match!");
        }
        result[0] = first[1] * second[2] - first[2] * second[1];
        result[1] = first[2] * second[0] - first[0] * second[2];
        result[2] = first[0] * second[1] - first[1] * second[0];
        return result;
    }

    public static double[] vectorSubstraction(double[] first, double[] second) throws NumberFormatException{
        if(first.length != second.length){
            throw new NumberFormatException("Dimension does not match!");
        }
        double[] result = new double[first.length];
        for(int i = 0; i < first.length; i++){
            result[i] = first[i] - second[i];
        }
        return result;
    }

    public static double[] vectorSummation(double[] first, double[] second) throws NumberFormatException{
        if(first.length != second.length){
            throw new NumberFormatException("Dimension does not match!");
        }
        double[] result = new double[first.length];
        for(int i = 0; i < first.length; i++){
            result[i] = first[i] + second[i];
        }
        return result;
    }

    public static double getTetrahedronVolume(double[] a, double[] b, double[] c, double[] d) throws NumberFormatException{
        if(a.length != 3 || a.length != b.length || a.length != c.length || a.length != d.length){
            throw new NumberFormatException("Dimension does not match!");
        }
        double result = 0.0;
        double intermediate = 0.0;
        //(a-d)·((b-d)X(c-d))
        intermediate = dotProdct(vectorSubstraction(a,b),crossProduct3D(vectorSubstraction(b,d), vectorSubstraction(c,d)));
        result = Math.abs(intermediate)/6;
//        Debug.print(arrayToString(a) + " " + arrayToString(b) + " " + arrayToString(c) + " " +
//                arrayToString(d) + " " + String.valueOf(result));
        return result;
    }

    public static double getVectorMagnitude(double[] a){
        double result = 0;
        for(int i =0; i < a.length; i++){
            result += a[i]*a[i];
        }
//        Debug.print("a:" + arrayToString(a));
        return Math.sqrt(result);
    }

    private static String arrayToString(double[] a){
        String temp ="[";
        for(int i = 0; i < a.length; i++){
            temp+=String.valueOf(a[i]);
            temp+=String.valueOf(",");
        }
        temp+="]";
        return temp;
    }
}
