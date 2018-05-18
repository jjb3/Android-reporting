package edu.gatech.reporter.utils;

import java.util.LinkedList;

/**
 * Created by Wendi on 2016/11/4.
 */

public class PastAccelerometerDataList {
    private LinkedList<double[]> prevAccDataPoints;
    private int capacity;
    public PastAccelerometerDataList(){
        capacity = 4;
        prevAccDataPoints = new LinkedList();
    }
    public PastAccelerometerDataList(int capacityFactor){
        capacity = 4 * capacityFactor;
        prevAccDataPoints = new LinkedList();
    }

    public void add(double[] accData){
        while(prevAccDataPoints.size() >= capacity){
            prevAccDataPoints.removeFirst();
        }
        prevAccDataPoints.addLast(accData);
    }

    public Double getJostleIndex(){
        if(prevAccDataPoints.size() < capacity)
            return 0.0;
        return VectorMath.getTetrahedronVolume(prevAccDataPoints.get(capacity/4 - 1),prevAccDataPoints.get(capacity/2 - 1),prevAccDataPoints.get(capacity/4*3 - 1),prevAccDataPoints.get(capacity - 1));

    }

    public Double getDistanceSumFromOrigin(){
        if(prevAccDataPoints.size() < capacity)
            return 0.0;
        double result = 0.0;
        result = VectorMath.getVectorMagnitude(prevAccDataPoints.get(capacity/4 - 1))
                + VectorMath.getVectorMagnitude(prevAccDataPoints.get(capacity/2 - 1))
                + VectorMath.getVectorMagnitude(prevAccDataPoints.get((capacity/4)*3 - 1))
                + VectorMath.getVectorMagnitude(prevAccDataPoints.get(capacity - 1));
//        Debug.print("Result: " + String.valueOf(result));
        return result;
    }

}
