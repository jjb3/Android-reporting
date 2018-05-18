package edu.gatech.reporter;

import org.junit.Test;

import edu.gatech.reporter.utils.VectorMath;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals((double)VectorMath.getTetrahedronVolume(new double[]{0,0,0},new double[]{2,0,0},new double[]{0,3,0},new double[]{1,1,2}),(double)2.0, 0.001);
        assertEquals((double)VectorMath.getTetrahedronVolume(new double[]{0,1.9,0},new double[]{2,0,0},new double[]{0,3,0},new double[]{1,1,2}),(double)0.733333, 0.001);
    }
}