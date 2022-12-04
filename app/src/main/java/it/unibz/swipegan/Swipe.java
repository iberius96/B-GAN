package it.unibz.swipegan;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Swipe {
    
    /**
     * Swipe duration (in seconds).
     */
    private double duration;
    
    /**
     * Swipe length.
     */
    private double length;
    
    /**
     * Swipe X segments.
     */
    private double[] segmentsX;
    
    /**
     * Swipe Y segments.
     */
    private double[] segmentsY;
    
    /**
     * Minimum Swipe touch size.
     */
    private double minSize;
    
    /**
     * Maximum Swipe touch size.
     */
    private double maxSize;
    
    /**
     * Average Swipe touch size.
     */
    private double avgSize;
    
    /**
     * Initial Swipe touch size.
     */
    private double downSize;
    
    /**
     * Final Swipe touch size.
     */
    private double upSize;
    
    /**
     * Swipe start X coordinate.
     */
    private double startX;
    
    /**
     * Swipe start Y coordinate.
     */
    private double startY;
    
    /**
     * Swipe end X coordinate.
     */
    private double endX;
    
    /**
     * Swipe end Y coordinate.
     */
    private double endY;
    
    /**
     * Minimum Swipe X velocity.
     */
    private double minXVelocity;
    
    /**
     * Maximum Swipe X velocity.
     */
    private double maxXVelocity;
    
    /**
     * Average Swipe X velocity.
     */
    private double avgXVelocity;
    
    /**
     * Variance Swipe X velocity.
     */
    private double varXVelocity;
    
    /**
     * Standard deviation Swipe X velocity.
     */
    private double stdXVelocity;
    
    /**
     * Minimum Swipe Y velocity.
     */
    private double minYVelocity;
    
    /**
     * Maximum Swipe Y velocity.
     */
    private double maxYVelocity;
    
    /**
     * Average Swipe Y velocity.
     */
    private double avgYVelocity;
    
    /**
     * Variance Swipe Y velocity.
     */
    private double varYVelocity;
    
    /**
     * Standard deviation Swipe Y velocity.
     */
    private double stdYVelocity;
    
    /**
     * Minimum X acceleration.
     */
    private double minXAccelerometer;
    
    /**
     * Maximum X acceleration.
     */
    private double maxXAccelerometer;
    
    /**
     * Average X acceleration.
     */
    private double avgXAccelerometer;
    
    /**
     * Variance X acceleration.
     */
    private double varXAccelerometer;
    
    /**
     * Standard deviation X acceleration.
     */
    private double stdXAccelerometer;
    
    /**
     * Minimum Y acceleration.
     */
    private double minYAccelerometer;
    
    /**
     * Maximum Y acceleration.
     */
    private double maxYAccelerometer;
    
    /**
     * Average Y acceleration.
     */
    private double avgYAccelerometer;
    
    /**
     * Variance Y acceleration.
     */
    private double varYAccelerometer;
    
    /**
     * Standard deviation Y acceleration.
     */
    private double stdYAccelerometer;
    
    /**
     * Minimum Z acceleration.
     */
    private double minZAccelerometer;
    
    /**
     * Maximum Z acceleration.
     */
    private double maxZAccelerometer;
    
    /**
     * Average Z acceleration.
     */
    private double avgZAccelerometer;
    
    /**
     * Variance Z acceleration.
     */
    private double varZAccelerometer;
    
    /**
     * Standard deviation Z acceleration.
     */
    private double stdZAccelerometer;
    
    /**
     * Minimum X angular velocity.
     */
    private double minXGyroscope;
    
    /**
     * Maximum X angular velocity.
     */
    private double maxXGyroscope;
    
    /**
     * Average X angular velocity.
     */
    private double avgXGyroscope;
    
    /**
     * Variance X angular velocity.
     */
    private double varXGyroscope;
    
    /**
     * Standard deviation X angular velocity.
     */
    private double stdXGyroscope;
    
    /**
     * Minimum Y angular velocity.
     */
    private double minYGyroscope;
    
    /**
     * Maximum Y angular velocity.
     */
    private double maxYGyroscope;
    
    /**
     * Average Y angular velocity.
     */
    private double avgYGyroscope;
    
    /**
     * Variance Y angular velocity.
     */
    private double varYGyroscope;
    
    /**
     * Standard deviation Y angular velocity.
     */
    private double stdYGyroscope;
    
    /**
     * Minimum Z angular velocity.
     */
    private double minZGyroscope;
    
    /**
     * Maximum Z angular velocity.
     */
    private double maxZGyroscope;
    
    /**
     * Average Z angular velocity.
     */
    private double avgZGyroscope;
    
    /**
     * Variance Z angular velocity.
     */
    private double varZGyroscope;
    
    /**
     * Standard deviation Z angular velocity.
     */
    private double stdZGyroscope;
    
    /**
     * Minimum X orientation value.
     */
    private double minXOrientation;
    
    /**
     * Maximum X orientation value.
     */
    private double maxXOrientation;
    
    /**
     * Average X orientation value.
     */
    private double avgXOrientation;
    
    /**
     * Variance X orientation value.
     */
    private double varXOrientation;
    
    /**
     * Standard deviation X orientation value.
     */
    private double stdXOrientation;
    
    /**
     * Minimum Y orientation value.
     */
    private double minYOrientation;
    
    /**
     * Maximum Y orientation value.
     */
    private double maxYOrientation;
    
    /**
     * Average Y orientation value.
     */
    private double avgYOrientation;
    
    /**
     * Variance Y orientation value.
     */
    private double varYOrientation;
    
    /**
     * Standard deviation Y orientation value.
     */
    private double stdYOrientation;
    
    /**
     * Minimum Z orientation value.
     */
    private double minZOrientation;
    
    /**
     * Maximum Z orientation value.
     */
    private double maxZOrientation;
    
    /**
     * Average Z orientation value.
     */
    private double avgZOrientation;
    
    /**
     * Variance Z orientation value.
     */
    private double varZOrientation;
    
    /**
     * Standard deviation Z orientation value.
     */
    private double stdZOrientation;

    /**
     * Keystroke individual press durations.
     */
    private double[] keystrokeDurations = null;
    
    /**
     * Keystroke individual press intervals.
     */
    private double[] keystrokeIntervals = null;
    
    /**
     * Keystroke start intervals.
     */
    private double[] keystrokeStartIntervals = null;
    
    /**
     * Keystroke end intervals.
     */
    private double[] keystrokeEndIntervals = null;
    
    /**
     * Full Keystroke duration.
     */
    private double keystrokeFullDuration = -1;

    /**
     * Signature initial X coordinate value.
     */
    private double signatureStartX = -1;
    
    /**
     * Signature initial Y coordinate value.
     */
    private double signatureStartY = -1;
    
    /**
     * Signature final X coordinate value.
     */
    private double signatureEndX = -1;
    
    /**
     * Signature final Y coordinate value.
     */
    private double signatureEndY = -1;
    
    /**
     * Signature standard deviation X coordinate value.
     */
    private double signatureStdX = -1;
    
    /**
     * Signature standard deviation Y coordinate value.
     */
    private double signatureStdY = -1;
    
    /**
     * Signature X coordinate min / max difference.
     */
    private double signatureDiffX = -1;
    
    /**
     * Signature Y coordinate min / max difference.
     */
    private double signatureDiffY = -1;
    
    /**
     * Signature start end position euclidean distance.
     */
    private double signatureEuclideanDistance = -1;
    
    /**
     * Signature average horizontal velocity.
     */
    private double signatureAvgXVelocity = -1;
    
    /**
     * Signature average vertical velocity.
     */
    private double signatureAvgYVelocity = -1;
    
    /**
     * Signature maximum horizontal velocity.
     */
    private double signatureMaxXVelocity = -1;
    
    /**
     * Signature maximum vertical velocity.
     */
    private double signatureMaxYVelocity = -1;
    
    /**
     * Signature X segment values.
     */
    private double[] signatureSegmentsX = null;
    
    /**
     * Signature Y segment values.
     */
    private double[] signatureSegmentsY = null;

    
    /**
     * Holding position identifier (1 = Sitting, 2 = Standing, 3 = Walking).
     */
    private double holdingPosition;
    private String userId;
    
    /**
     * Test interaction authentication results.
     */
    private double[] authentication;
    
    /**
     * Test interaction authentication times.
     */
    private double[] authenticationTime;

    /**
     * Nr. of training samples.
     */
    private int classifierSamples = 0;

    /**
     * Class constructor.
     */
    public Swipe() {

    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * 
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double[] getSegmentsX() {
        return segmentsX;
    }

    /**
     * 
     * @param segmentsX
     */
    public void setSegmentsX(double[] segmentsX) {
        this.segmentsX = segmentsX;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double[] getSegmentsY() {
        return segmentsY;
    }

    /**
     * 
     * @param segmentsY
     */
    public void setSegmentsY(double[] segmentsY) {
        this.segmentsY = segmentsY;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double getMinSize() {
        return minSize;
    }

    /**
     * 
     * 
     * @param minSize
     */
    public void setMinSize(double minSize) {
        this.minSize = minSize;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public double getMaxSize() {
        return maxSize;
    }

    /**
     * 
     * @param maxSize
     */
    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 
     * @return
     */
    public double getAvgSize() {
        return avgSize;
    }

    /**
     * 
     * @param avgSize
     */
    public void setAvgSize(double avgSize) {
        this.avgSize = avgSize;
    }

    /**
     * 
     * @return
     */
    public double getDownSize() {
        return downSize;
    }

    /**
     * 
     * @param downSize
     */
    public void setDownSize(double downSize) {
        this.downSize = downSize;
    }

    /**
     * 
     * @return
     */
    public double getUpSize() {
        return upSize;
    }

    /**
     * 
     * @param upSize
     */
    public void setUpSize(double upSize) {
        this.upSize = upSize;
    }

    /**
     * 
     * @return
     */
    public double getStartX() {
        return startX;
    }

    /**
     * 
     * @param startX
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /**
     * 
     * @return
     */
    public double getStartY() {
        return startY;
    }

    /**
     * 
     * @param startY
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /**
     * 
     * @return
     */
    public double getEndX() {
        return endX;
    }

    /**
     * 
     * @param endX
     */
    public void setEndX(double endX) {
        this.endX = endX;
    }

    /**
     * 
     * @return
     */
    public double getEndY() {
        return endY;
    }

    /**
     * 
     * @param endY
     */
    public void setEndY(double endY) {
        this.endY = endY;
    }

    /**
     * 
     * @return
     */
    public double getMinXVelocity() {
        return minXVelocity;
    }

    /**
     * 
     * @param minXVelocity
     */
    public void setMinXVelocity(double minXVelocity) {
        this.minXVelocity = minXVelocity;
    }

    /**
     * 
     * @return
     */
    public double getMaxXVelocity() {
        return maxXVelocity;
    }

    /**
     * 
     * @param maxXVelocity
     */
    public void setMaxXVelocity(double maxXVelocity) {
        this.maxXVelocity = maxXVelocity;
    }

    /**
     * 
     * @return
     */
    public double getAvgXVelocity() {
        return avgXVelocity;
    }

    /**
     * 
     * @param avgXVelocity
     */
    public void setAvgXVelocity(double avgXVelocity) {
        this.avgXVelocity = avgXVelocity;
    }

    /**
     * 
     * @return
     */
    public double getStdXVelocity() {
        return stdXVelocity;
    }

    /**
     * 
     * @param stdXVelocity
     */
    public void setStdXVelocity(double stdXVelocity) {
        this.stdXVelocity = stdXVelocity;
    }

    /**
     * 
     * @return
     */
    public double getVarXVelocity() {
        return varXVelocity;
    }

    /**
     * 
     * @param varXVelocity
     */
    public void setVarXVelocity(double varXVelocity) {
        this.varXVelocity = varXVelocity;
    }

    /**
     * 
     * @return
     */
    public double getMinYVelocity() {
        return minYVelocity;
    }

    /**
     * 
     * @param minYVelocity
     */
    public void setMinYVelocity(double minYVelocity) {
        this.minYVelocity = minYVelocity;
    }

    /**
     * 
     * @return
     */
    public double getMaxYVelocity() {
        return maxYVelocity;
    }

    /**
     * 
     * @param maxYVelocity
     */
    public void setMaxYVelocity(double maxYVelocity) {
        this.maxYVelocity = maxYVelocity;
    }

    /**
     * 
     * @return
     */
    public double getAvgYVelocity() {
        return avgYVelocity;
    }

    /**
     * 
     * @param avgYVelocity
     */
    public void setAvgYVelocity(double avgYVelocity) {
        this.avgYVelocity = avgYVelocity;
    }

    /**
     * 
     * @return
     */
    public double getStdYVelocity() {
        return stdYVelocity;
    }

    /**
     * 
     * @param stdYVelocity
     */
    public void setStdYVelocity(double stdYVelocity) {
        this.stdYVelocity = stdYVelocity;
    }

    /**
     * 
     * @return
     */
    public double getVarYVelocity() {
        return varYVelocity;
    }

    /**
     * 
     * @param varYVelocity
     */
    public void setVarYVelocity(double varYVelocity) {
        this.varYVelocity = varYVelocity;
    }

    /**
     * 
     * @return
     */
    public double getMinXAccelerometer() {
        return minXAccelerometer;
    }

    /**
     * 
     * @param minXAccelerometer
     */
    public void setMinXAccelerometer(double minXAccelerometer) {
        this.minXAccelerometer = minXAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMaxXAccelerometer() {
        return maxXAccelerometer;
    }

    /**
     * 
     * @param maxXAccelerometer
     */
    public void setMaxXAccelerometer(double maxXAccelerometer) {
        this.maxXAccelerometer = maxXAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getAvgXAccelerometer() {
        return avgXAccelerometer;
    }

    /**
     * 
     * @param avgXAccelerometer
     */
    public void setAvgXAccelerometer(double avgXAccelerometer) {
        this.avgXAccelerometer = avgXAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getStdXAccelerometer() {
        return stdXAccelerometer;
    }

    /**
     * 
     * @param stdXAccelerometer
     */
    public void setStdXAccelerometer(double stdXAccelerometer) {
        this.stdXAccelerometer = stdXAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getVarXAccelerometer() {
        return varXAccelerometer;
    }

    /**
     * 
     * @param varXAccelerometer
     */
    public void setVarXAccelerometer(double varXAccelerometer) {
        this.varXAccelerometer = varXAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMinYAccelerometer() {
        return minYAccelerometer;
    }

    /**
     * 
     * @param minYAccelerometer
     */
    public void setMinYAccelerometer(double minYAccelerometer) {
        this.minYAccelerometer = minYAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMaxYAccelerometer() {
        return maxYAccelerometer;
    }

    /**
     * 
     * @param maxYAccelerometer
     */
    public void setMaxYAccelerometer(double maxYAccelerometer) {
        this.maxYAccelerometer = maxYAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getAvgYAccelerometer() {
        return avgYAccelerometer;
    }

    /**
     * 
     * @param avgYAccelerometer
     */
    public void setAvgYAccelerometer(double avgYAccelerometer) {
        this.avgYAccelerometer = avgYAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getStdYAccelerometer() {
        return stdYAccelerometer;
    }

    /**
     * 
     * @param stdYAccelerometer
     */
    public void setStdYAccelerometer(double stdYAccelerometer) {
        this.stdYAccelerometer = stdYAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getVarYAccelerometer() {
        return varYAccelerometer;
    }

    /**
     * 
     * @param varYAccelerometer
     */
    public void setVarYAccelerometer(double varYAccelerometer) {
        this.varYAccelerometer = varYAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMinZAccelerometer() {
        return minZAccelerometer;
    }

    /**
     * 
     * @param minZAccelerometer
     */
    public void setMinZAccelerometer(double minZAccelerometer) {
        this.minZAccelerometer = minZAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMaxZAccelerometer() {
        return maxZAccelerometer;
    }

    /**
     * 
     * @param maxZAccelerometer
     */
    public void setMaxZAccelerometer(double maxZAccelerometer) {
        this.maxZAccelerometer = maxZAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getAvgZAccelerometer() {
        return avgZAccelerometer;
    }

    /**
     * 
     * @param avgZAccelerometer
     */
    public void setAvgZAccelerometer(double avgZAccelerometer) {
        this.avgZAccelerometer = avgZAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getStdZAccelerometer() {
        return stdZAccelerometer;
    }

    /**
     * 
     * @param stdZAccelerometer
     */
    public void setStdZAccelerometer(double stdZAccelerometer) {
        this.stdZAccelerometer = stdZAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getVarZAccelerometer() {
        return varZAccelerometer;
    }

    /**
     * 
     * @param varZAccelerometer
     */
    public void setVarZAccelerometer(double varZAccelerometer) {
        this.varZAccelerometer = varZAccelerometer;
    }

    /**
     * 
     * @return
     */
    public double getMinXGyroscope() {
        return minXGyroscope;
    }

    /**
     * 
     * @param minXGyroscope
     */
    public void setMinXGyroscope(double minXGyroscope) {
        this.minXGyroscope = minXGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getMaxXGyroscope() {
        return maxXGyroscope;
    }

    /**
     * 
     * @param maxXGyroscope
     */
    public void setMaxXGyroscope(double maxXGyroscope) {
        this.maxXGyroscope = maxXGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getAvgXGyroscope() {
        return avgXGyroscope;
    }

    /**
     * 
     * @param avgXGyroscope
     */
    public void setAvgXGyroscope(double avgXGyroscope) {
        this.avgXGyroscope = avgXGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getStdXGyroscope() {
        return stdXGyroscope;
    }

    /**
     * 
     * @param stdXGyroscope
     */
    public void setStdXGyroscope(double stdXGyroscope) {
        this.stdXGyroscope = stdXGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getVarXGyroscope() {
        return varXGyroscope;
    }

    /**
     * 
     * @param varXGyroscope
     */
    public void setVarXGyroscope(double varXGyroscope) {
        this.varXGyroscope = varXGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getMinYGyroscope() {
        return minYGyroscope;
    }

    /**
     * 
     * @param minYGyroscope
     */
    public void setMinYGyroscope(double minYGyroscope) {
        this.minYGyroscope = minYGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getMaxYGyroscope() {
        return maxYGyroscope;
    }

    /**
     * 
     * @param maxYGyroscope
     */
    public void setMaxYGyroscope(double maxYGyroscope) {
        this.maxYGyroscope = maxYGyroscope;
    }

    /**
     * 
     * @return
     */
    public double getAvgYGyroscope() {
        return avgYGyroscope;
    }

    /**
     * 
     * @param avgYGyroscope
     */
    public void setAvgYGyroscope(double avgYGyroscope) {
        this.avgYGyroscope = avgYGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getStdYGyroscope() {
        return stdYGyroscope;
    }

    /**
     * 
     * @param stdYGyroscope
     */
    public  void setStdYGyroscope(double stdYGyroscope) {
        this.stdYGyroscope = stdYGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getVarYGyroscope() {
        return varYGyroscope;
    }

    /**
     * 
     * @param varYGyroscope
     */
    public  void setVarYGyroscope(double varYGyroscope) {
        this.varYGyroscope = varYGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getMinZGyroscope() {
        return minZGyroscope;
    }

    /**
     * 
     * @param minZGyroscope
     */
    public  void setMinZGyroscope(double minZGyroscope) {
        this.minZGyroscope = minZGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getMaxZGyroscope() {
        return maxZGyroscope;
    }

    /**
     * 
     * @param maxZGyroscope
     */
    public  void setMaxZGyroscope(double maxZGyroscope) {
        this.maxZGyroscope = maxZGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getAvgZGyroscope() {
        return avgZGyroscope;
    }

    /**
     * 
     * @param avgZGyroscope
     */
    public  void setAvgZGyroscope(double avgZGyroscope) {
        this.avgZGyroscope = avgZGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getStdZGyroscope() {
        return stdZGyroscope;
    }

    /**
     * 
     * @param stdZGyroscope
     */
    public  void setStdZGyroscope(double stdZGyroscope) {
        this.stdZGyroscope = stdZGyroscope;
    }

    /**
     * 
     * @return
     */
    public  double getVarZGyroscope() {
        return varZGyroscope;
    }

    /**
     * 
     * @param varZGyroscope
     */
     public  void setVarZGyroscope(double varZGyroscope) {
     this.varZGyroscope = varZGyroscope;
     }

     /**
     *
     * @return
     */
     public  double getMinXOrientation() {
     return minXOrientation;
     }

     /**
     *
     * @param minXOrientation
     */
     public  void setMinXOrientation(double minXOrientation) {
     this.minXOrientation = minXOrientation;
     }

     /**
     *
     * @return
     */
     public  double getMaxXOrientation() {
     return maxXOrientation;
     }

     /**
     *
     * @param maxXOrientation
     */
     public  void setMaxXOrientation(double maxXOrientation) {
     this.maxXOrientation = maxXOrientation;
     }

     /**
     *
     * @return
     */
     public  double getAvgXOrientation() {
     return avgXOrientation;
     }

     /**
     *
     * @param avgXOrientation
     */
    public  void setAvgXOrientation(double avgXOrientation) {
        this.avgXOrientation = avgXOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getStdXOrientation() {
        return stdXOrientation;
    }

    /**
     * 
     * @param stdXOrientation
     */
    public  void setStdXOrientation(double stdXOrientation) {
        this.stdXOrientation = stdXOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getVarXOrientation() {
        return varXOrientation;
    }

    /**
     * 
     * @param varXOrientation
     */
    public  void setVarXOrientation(double varXOrientation) {
        this.varXOrientation = varXOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getMinYOrientation() {
        return minYOrientation;
    }

    /**
     * 
     * @param minYOrientation
     */
    public  void setMinYOrientation(double minYOrientation) {
        this.minYOrientation = minYOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getMaxYOrientation() {
        return maxYOrientation;
    }

    /**
     * 
     * @param maxYOrientation
     */
    public  void setMaxYOrientation(double maxYOrientation) {
        this.maxYOrientation = maxYOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getAvgYOrientation() {
        return avgYOrientation;
    }

    /**
     * 
     * @param avgYOrientation
     */
    public  void setAvgYOrientation(double avgYOrientation) {
        this.avgYOrientation = avgYOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getStdYOrientation() {
        return stdYOrientation;
    }

    /**
     * 
     * @param stdYOrientation
     */
    public  void setStdYOrientation(double stdYOrientation) {
        this.stdYOrientation = stdYOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getVarYOrientation() {
        return varYOrientation;
    }

    /**
     * 
     * @param varYOrientation
     */
    public  void setVarYOrientation(double varYOrientation) {
        this.varYOrientation = varYOrientation;
    }

    /**
     * 
     * @return
     */
    public  double getMinZOrientation() {
        return minZOrientation;
    }

    /**
     * 
     * @param minZOrientation
     */
    public  void setMinZOrientation(double minZOrientation) {
        this.minZOrientation = minZOrientation;
    }

    /**
     * 
     * @return
     */
     public  double getMaxZOrientation() {
     return maxZOrientation;
     }

     /**
     *
     * @param maxZOrientation
     */
     public  void setMaxZOrientation(double maxZOrientation) {
     this.maxZOrientation = maxZOrientation;
     }

     /**
     *
     * @return
     */
     public  double getAvgZOrientation() {
     return avgZOrientation;
     }

     /**
     *
     * @param avgZOrientation
     */
     public  void setAvgZOrientation(double avgZOrientation) {
     this.avgZOrientation = avgZOrientation;
     }

     /**
     *
     * @return
     */
     public  double getStdZOrientation() {
     return stdZOrientation;
     }

     /**
     *
     * @param stdZOrientation
     */
     public  void setStdZOrientation(double stdZOrientation) {
     this.stdZOrientation = stdZOrientation;
     }

     /**
     *
     * @return
     */
    public  double getVarZOrientation() {
        return varZOrientation;
    }

    /**
     * 
     * @param varZOrientation
     */
    public  void setVarZOrientation(double varZOrientation) {
        this.varZOrientation = varZOrientation;
    }

    /**
     * 
     * @return
     */
    public  double[] getKeystrokeDurations() {
        return this.keystrokeDurations;
    }

    /**
     * 
     * @param duration
     * @param curKeystroke
     * @param pinLength
     */
    public  void addKeystrokeDuration(double duration, int curKeystroke, int pinLength) {
        if(this.keystrokeDurations == null) {
            this.keystrokeDurations = new double[pinLength];
        }

        this.keystrokeDurations[curKeystroke] = duration;
    }

    /**
     * 
     * @param keystrokeDurations
     */
    public  void setKeystrokeDurations(double[] keystrokeDurations) {
        this.keystrokeDurations = keystrokeDurations;
    }

    /**
     * 
     * @return
     */
    public  double[] getKeystrokeIntervals() {
        return this.keystrokeIntervals;
    }

    /**
     * 
     * @param interval
     * @param curKeystroke
     * @param pinLength
     */
    public  void addKeystrokeInterval(double interval, int curKeystroke, int pinLength) {
        if(this.keystrokeIntervals == null) {
            this.keystrokeIntervals = new double[pinLength - 1];
        }

        this.keystrokeIntervals[curKeystroke] = interval;
    }

    /**
     * 
     * @param keystrokeIntervals
     */
    public  void setKeystrokeIntervals(double[] keystrokeIntervals) {
        this.keystrokeIntervals = keystrokeIntervals;
    }

    /**
     * 
     * @return
     */
    public  double[] getKeystrokeStartIntervals() {
        return this.keystrokeStartIntervals;
    }

    /**
     * 
     * @param startInterval
     * @param curKeystroke
     * @param pinLength
     */
    public  void addKeystrokeStartInterval(double startInterval, int curKeystroke, int pinLength) {
        if(this.keystrokeStartIntervals == null) {
            this.keystrokeStartIntervals = new double[pinLength - 1];
        }

        this.keystrokeStartIntervals[curKeystroke] = startInterval;
    }

    /**
     * 
     * @param keystrokeStartIntervals
     */
    public  void setKeystrokeStartIntervals(double[] keystrokeStartIntervals) {
        this.keystrokeStartIntervals = keystrokeStartIntervals;
    }

    /**
     * 
     * @return
     */
    public  double[] getKeystrokeEndIntervals() {
        return this.keystrokeEndIntervals;
    }

    /**
     * 
     * @param endInterval
     * @param curKeystroke
     * @param pinLength
     */
    public  void addKeystrokeEndInterval(double endInterval, int curKeystroke, int pinLength) {
        if(this.keystrokeEndIntervals == null) {
            this.keystrokeEndIntervals = new double[pinLength - 1];
        }

        this.keystrokeEndIntervals[curKeystroke] = endInterval;
    }

    /**
     * 
     * @param keystrokeEndIntervals
     */
    public  void setKeystrokeEndIntervals(double[] keystrokeEndIntervals) {
        this.keystrokeEndIntervals = keystrokeEndIntervals;
    }

    /**
     * 
     * @return
     */
    public  double getKeystrokeFullDuration() {
        return this.keystrokeFullDuration;
    }

    /**
     * 
     */
    public  void setKeystrokeFullDuration() {
        this.keystrokeFullDuration = Arrays.stream(this.getKeystrokeDurations()).sum() + Arrays.stream(this.getKeystrokeIntervals()).sum();
    }

    /**
     * 
     * @param keystrokeFullDuration
     */
    public  void setKeystrokeFullDurationNormalized(double keystrokeFullDuration) {
        this.keystrokeFullDuration = keystrokeFullDuration;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureStartX() {
        return signatureStartX;
    }

    /**
     * 
     * @param signatureStartX
     */
    public  void setSignatureStartX(double signatureStartX) {
        this.signatureStartX = signatureStartX;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureStartY() {
        return signatureStartY;
    }

    /**
     * 
     * @param signatureStartY
     */
    public  void setSignatureStartY(double signatureStartY) {
        this.signatureStartY = signatureStartY;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureEndX() {
        return signatureEndX;
    }

    /**
     * 
     * @param signatureEndX
     */
    public  void setSignatureEndX(double signatureEndX) {
        this.signatureEndX = signatureEndX;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureEndY() {
        return signatureEndY;
    }

    /**
     * 
     * @param signatureEndY
     */
    public  void setSignatureEndY(double signatureEndY) {
        this.signatureEndY = signatureEndY;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureStdX() {
        return signatureStdX;
    }

    /**
     * 
     * @param signatureStdX
     */
    public  void setSignatureStdX(double signatureStdX) {
        this.signatureStdX = signatureStdX;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureStdY() {
        return signatureStdY;
    }

    /**
     * 
     * @param signatureStdY
     */
    public  void setSignatureStdY(double signatureStdY) {
        this.signatureStdY = signatureStdY;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureDiffX() {
        return signatureDiffX;
    }

    /**
     * 
     * @param signatureDiffX
     */
    public  void setSignatureDiffX(double signatureDiffX) {
        this.signatureDiffX = signatureDiffX;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureDiffY() {
        return signatureDiffY;
    }

    /**
     * 
     * @param signatureDiffY
     */
    public  void setSignatureDiffY(double signatureDiffY) {
        this.signatureDiffY = signatureDiffY;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureEuclideanDistance() {
        return signatureEuclideanDistance;
    }

    /**
     * 
     * @param signatureEuclideanDistance
     */
    public  void setSignatureEuclideanDistance(double signatureEuclideanDistance) {
        this.signatureEuclideanDistance = signatureEuclideanDistance;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureAvgXVelocity() {
        return signatureAvgXVelocity;
    }

    /**
     * 
     * @param signatureAvgXVelocity
     */
    public  void setSignatureAvgXVelocity(double signatureAvgXVelocity) {
        this.signatureAvgXVelocity = signatureAvgXVelocity;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureAvgYVelocity() {
        return signatureAvgYVelocity;
    }

    /**
     * 
     * @param signatureAvgYVelocity
     */
    public  void setSignatureAvgYVelocity(double signatureAvgYVelocity) {
        this.signatureAvgYVelocity = signatureAvgYVelocity;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureMaxXVelocity() {
        return signatureMaxXVelocity;
    }

    /**
     * 
     * @param signatureMaxXVelocity
     */
    public  void setSignatureMaxXVelocity(double signatureMaxXVelocity) {
        this.signatureMaxXVelocity = signatureMaxXVelocity;
    }

    /**
     * 
     * @return
     */
    public  double getSignatureMaxYVelocity() {
        return signatureMaxYVelocity;
    }

    /**
     * 
     * @param signatureMaxYVelocity
     */
    public  void setSignatureMaxYVelocity(double signatureMaxYVelocity) {
        this.signatureMaxYVelocity = signatureMaxYVelocity;
    }

    /**
     * 
     * @return
     */
    public  double[] getSignatureSegmentsX() {
        return signatureSegmentsX;
    }

    /**
     * 
     * @param signatureSegmentsX
     */
    public  void setSignatureSegmentsX(double[] signatureSegmentsX) {
        this.signatureSegmentsX = signatureSegmentsX;
    }

    /**
     * 
     * @return
     */
    public  double[] getSignatureSegmentsY() {
        return signatureSegmentsY;
    }

    /**
     * 
     * @param signatureSegmentsY
     */
    public  void setSignatureSegmentsY(double[] signatureSegmentsY) {
        this.signatureSegmentsY = signatureSegmentsY;
    }

    /**
     * 
     * @return
     */
    public  double getHoldingPosition() {
        return holdingPosition;
    }

    /**
     * 
     * @param holdingPosition
     */
    public  void setHoldingPosition(double holdingPosition) {
        this.holdingPosition = holdingPosition;
    }

    /**
     * 
     * @return
     */
    public  String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     */
    public  void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     */
    public  double[] getAuthentication() {
        return this.authentication;
    }

    /**
     * 
     * @param authentication
     */
    public  void setAuthentication(double[] authentication) {
        this.authentication = authentication;
    }

    /**
     * 
     * @return
     */
    public  double[] getAuthenticationTime() {
        return this.authenticationTime;
    }

    /**
     * 
     * @param authenticationTime
     */
    public  void setAuthenticationTime(double[] authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    /**
     * 
     * @return
     */
    public  int getClassifierSamples() {
        return classifierSamples;
    }

    /**
     * 
     * @param classifierSamples
     */
    public  void setClassifierSamples(int classifierSamples) {
        this.classifierSamples = classifierSamples;
    }

    /**
     * Gets from the gathered training examples (and for each active feature) the minimum and maximum values that are used for min/max scaling.
     *
     * @param allSwipes The set of training interactions.
     * @return
     */
    public  static Map<String, Double> getMinMaxValues(ArrayList<Swipe> allSwipes) {
        Map<String, Double> map = new HashMap<String, Double>();

        for(Swipe swipe : allSwipes) {
            for(String head_feature : DatabaseHelper.head_features) {
                String min_key = "MIN_" + head_feature.toUpperCase();
                String max_key = "MAX_" + head_feature.toUpperCase();

                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)));

                    if(head_feature == DatabaseHelper.COL_SEGMENTS_X || head_feature == DatabaseHelper.COL_SEGMENTS_Y) {
                        double[] segment_vals = (double[]) cur_method.invoke(swipe);

                        for(int i = 0; i < segment_vals.length; i++) {
                            map.put(min_key + "_" + i, map.get(min_key + "_" + i) == null || segment_vals[i] < map.get(min_key + "_" + i) ? segment_vals[i] : map.get(min_key + "_" + i));
                            map.put(max_key + "_" + i, map.get(max_key + "_" + i) == null || segment_vals[i] > map.get(max_key + "_" + i) ? segment_vals[i] : map.get(max_key + "_" + i));
                        }
                    } else {
                        Double cur_value = (Double) cur_method.invoke(swipe);
                        map.put(min_key, map.get(min_key) == null || cur_value < map.get(min_key) ? cur_value : map.get(min_key));
                        map.put(max_key, map.get(max_key) == null || cur_value > map.get(max_key) ? cur_value : map.get(max_key));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            for(String feature : DatabaseHelper.features) {
                for(String metric : DatabaseHelper.metrics) {
                    for(String dimension : DatabaseHelper.dimensions) {
                        if (dimension == "Z" && feature == "Velocity") { continue; }

                        String min_key = "MIN_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();
                        String max_key = "MAX_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();

                        java.lang.reflect.Method cur_method = null;
                        Double cur_value = 0.0;
                        try {
                            cur_method = swipe.getClass().getMethod("get" + metric + dimension + feature);
                            cur_value = (Double) cur_method.invoke(swipe);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        map.put(min_key, map.get(min_key) == null || cur_value < map.get(min_key) ? cur_value : map.get(min_key));
                        map.put(max_key, map.get(max_key) == null || cur_value > map.get(max_key) ? cur_value : map.get(max_key));
                    }
                }
            }

            for(String keystroke_feature : DatabaseHelper.keystroke_features) {
                String min_key = "MIN_" + keystroke_feature.toUpperCase();
                String max_key = "MAX_" + keystroke_feature.toUpperCase();

                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)));

                    if(keystroke_feature == DatabaseHelper.COL_KEYSTROKE_FULL_DURATION) {
                        Double cur_value = (Double) cur_method.invoke(swipe);

                        if(cur_value != -1) {
                            map.put(min_key, map.get(min_key) == null || cur_value < map.get(min_key) ? cur_value : map.get(min_key));
                            map.put(max_key, map.get(max_key) == null || cur_value > map.get(max_key) ? cur_value : map.get(max_key));
                        }
                    } else {
                        double[] keystroke_vals = (double[]) cur_method.invoke(swipe);

                        if (keystroke_vals != null) {
                            for (int i = 0; i < keystroke_vals.length; i++) {
                                map.put(min_key + "_" + i, map.get(min_key + "_" + i) == null || keystroke_vals[i] < map.get(min_key + "_" + i) ? keystroke_vals[i] : map.get(min_key + "_" + i));
                                map.put(max_key + "_" + i, map.get(max_key + "_" + i) == null || keystroke_vals[i] > map.get(max_key + "_" + i) ? keystroke_vals[i] : map.get(max_key + "_" + i));
                            }
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            for(String signature_feature : DatabaseHelper.signature_features) {
                String min_key = "MIN_" + signature_feature.toUpperCase();
                String max_key = "MAX_" + signature_feature.toUpperCase();

                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)));

                    if(signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_X || signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_Y) {
                        double[] segment_vals = (double[]) cur_method.invoke(swipe);

                        if (segment_vals != null) {
                            for (int i = 0; i < segment_vals.length; i++) {
                                map.put(min_key + "_" + i, map.get(min_key + "_" + i) == null || segment_vals[i] < map.get(min_key + "_" + i) ? segment_vals[i] : map.get(min_key + "_" + i));
                                map.put(max_key + "_" + i, map.get(max_key + "_" + i) == null || segment_vals[i] > map.get(max_key + "_" + i) ? segment_vals[i] : map.get(max_key + "_" + i));
                            }
                        }
                    } else {
                        Double cur_value = (Double) cur_method.invoke(swipe);

                        if(cur_value != -1) {
                            map.put(min_key, map.get(min_key) == null || cur_value < map.get(min_key) ? cur_value : map.get(min_key));
                            map.put(max_key, map.get(max_key) == null || cur_value > map.get(max_key) ? cur_value : map.get(max_key));
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    /**
     * 
     * @param allSwipes
     * @return
     */
    public  double[] getNormalizedValues(ArrayList<Swipe> allSwipes) {
        Map<String, Double> map = getMinMaxValues(allSwipes);

        ArrayList<Double> ret = new ArrayList<Double>();

        for(String head_feature : DatabaseHelper.head_features) {
            String min_key = "MIN_" + head_feature.toUpperCase();
            String max_key = "MAX_" + head_feature.toUpperCase();

            java.lang.reflect.Method cur_method = null;
            try {
                cur_method = this.getClass().getMethod("get" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)));

                if(head_feature == DatabaseHelper.COL_SEGMENTS_X || head_feature == DatabaseHelper.COL_SEGMENTS_Y) {
                    double[] segment_vals = (double[]) cur_method.invoke(this);

                    for(int i = 0; i < segment_vals.length; i++) {
                        ret.add((segment_vals[i] - map.get(min_key + "_" + i)) / (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)));
                    }
                } else {
                    Double cur_value = (Double) cur_method.invoke(this);
                    ret.add((cur_value - map.get(min_key)) / (map.get(max_key) - map.get(min_key)));
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for(String feature : DatabaseHelper.features) {
            for(String metric : DatabaseHelper.metrics) {
                for(String dimension : DatabaseHelper.dimensions) {
                    if (dimension == "Z" && feature == "Velocity") { continue; }

                    String min_key = "MIN_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();
                    String max_key = "MAX_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();

                    java.lang.reflect.Method cur_method = null;
                    Double cur_value = 0.0;
                    try {
                        cur_method = this.getClass().getMethod("get" + metric + dimension + feature);
                        cur_value = (Double) cur_method.invoke(this);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    ret.add((cur_value - map.get(min_key)) / (map.get(max_key) - map.get(min_key)));
                }
            }
        }

        for(String keystroke_feature : DatabaseHelper.keystroke_features) {
            String min_key = "MIN_" + keystroke_feature.toUpperCase();
            String max_key = "MAX_" + keystroke_feature.toUpperCase();

            java.lang.reflect.Method cur_method = null;
            try {
                cur_method = this.getClass().getMethod("get" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)));

                if(keystroke_feature == DatabaseHelper.COL_KEYSTROKE_FULL_DURATION) {
                    Double cur_value = (Double) cur_method.invoke(this);
                    if(cur_value != -1) { ret.add((cur_value - map.get(min_key)) / (map.get(max_key) - map.get(min_key))); }
                } else {
                    double[] keystroke_vals = (double[]) cur_method.invoke(this);

                    if (keystroke_vals != null) {
                        for (int i = 0; i < keystroke_vals.length; i++) {
                            ret.add((keystroke_vals[i] - map.get(min_key + "_" + i)) / (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)));
                        }
                    }
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for(String signature_feature : DatabaseHelper.signature_features) {
            String min_key = "MIN_" + signature_feature.toUpperCase();
            String max_key = "MAX_" + signature_feature.toUpperCase();

            java.lang.reflect.Method cur_method = null;
            try {
                cur_method = this.getClass().getMethod("get" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)));

                if(signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_X || signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_Y) {
                    double[] segment_vals = (double[]) cur_method.invoke(this);

                    if (segment_vals != null) {
                        for (int i = 0; i < segment_vals.length; i++) {
                            ret.add((segment_vals[i] - map.get(min_key + "_" + i)) / (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)));
                        }
                    }
                } else {
                    Double cur_value = (Double) cur_method.invoke(this);
                    if(cur_value != -1) { ret.add((cur_value - map.get(min_key)) / (map.get(max_key) - map.get(min_key))); }
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        double[] retArray = new double[ret.size()];
        for(int i =0 ; i < ret.size(); i++) {
            if(Double.isNaN(ret.get(i))) {
                ret.set(i, 0d);
            }
            retArray[i] = ret.get(i);
        }
        return retArray;
    }

    /**
     * 
     * @param values
     * @param holdingPosition
     * @param userId
     * @param allSwipes
     * @return
     */
    public  static Swipe fromNormalizedValues(double[] values, double holdingPosition, String userId, ArrayList<Swipe> allSwipes) {
        Map<String, Double> map = getMinMaxValues(allSwipes);

        Swipe swipe = new Swipe();
        Integer values_idx = 0;

        for(String head_feature : DatabaseHelper.head_features) {
            String min_key = "MIN_" + head_feature.toUpperCase();
            String max_key = "MAX_" + head_feature.toUpperCase();

            Method cur_method = null;
            try {
                if(head_feature == DatabaseHelper.COL_SEGMENTS_X || head_feature == DatabaseHelper.COL_SEGMENTS_Y) {
                    Integer segments_size = map.entrySet()
                            .stream()
                            .filter(x -> x.getKey().startsWith(head_feature.toUpperCase(), 4)) // Offset for prefix MIN_ / MAX_
                            .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).size() / 2;

                    double[] normalized_segments = new double[segments_size];
                    for(int i = 0; i < segments_size; i++) {
                        normalized_segments[i] = values[values_idx] * (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)) + map.get(min_key + "_" + i);
                        values_idx = values_idx + 1;
                    }

                    cur_method = swipe.getClass().getMethod("set" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)), double[].class);
                    cur_method.invoke(swipe, normalized_segments);
                } else {
                    cur_method = swipe.getClass().getMethod("set" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)), double.class);
                    cur_method.invoke(swipe, values[values_idx] * (map.get(max_key) - map.get(min_key)) + map.get(min_key));
                    values_idx = values_idx + 1;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for(String feature : DatabaseHelper.features) {
            for(String metric : DatabaseHelper.metrics) {
                for(String dimension : DatabaseHelper.dimensions) {
                    if (dimension == "Z" && feature == "Velocity") { continue; }

                    String min_key = "MIN_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();
                    String max_key = "MAX_" + metric.toUpperCase() + "_" + dimension + "_" + feature.toUpperCase();

                    Method cur_method = null;
                    try {
                        cur_method = swipe.getClass().getMethod("set" + metric + dimension + feature, double.class);
                        cur_method.invoke(swipe, values[values_idx] * (map.get(max_key) - map.get(min_key)) + map.get(min_key));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    values_idx = values_idx + 1;
                }
            }
        }

        for(String keystroke_feature : DatabaseHelper.keystroke_features) {
            String min_key = "MIN_" + keystroke_feature.toUpperCase();
            String max_key = "MAX_" + keystroke_feature.toUpperCase();

            Method cur_method = null;
            try {
                Integer keystroke_size = map.entrySet()
                        .stream()
                        .filter(x -> x.getKey().contains(keystroke_feature.toUpperCase()))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).size() / 2;

                if (keystroke_size != 0) {
                    if (keystroke_feature == DatabaseHelper.COL_KEYSTROKE_FULL_DURATION) {
                        cur_method = swipe.getClass().getMethod("set" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)) + "Normalized", double.class);
                        cur_method.invoke(swipe, values[values_idx] * (map.get(max_key) - map.get(min_key)) + map.get(min_key));
                        values_idx = values_idx + 1;
                    } else {
                        double[] normalized_keystroke = new double[keystroke_size];
                        for (int i = 0; i < keystroke_size; i++) {
                            normalized_keystroke[i] = values[values_idx] * (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)) + map.get(min_key + "_" + i);
                            values_idx = values_idx + 1;
                        }

                        cur_method = swipe.getClass().getMethod("set" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)), double[].class);
                        cur_method.invoke(swipe, normalized_keystroke);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for(String signature_feature : DatabaseHelper.signature_features) {
            String min_key = "MIN_" + signature_feature.toUpperCase();
            String max_key = "MAX_" + signature_feature.toUpperCase();

            Method cur_method = null;
            try {
                if(signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_X || signature_feature == DatabaseHelper.COL_SIGNATURE_SEGMENTS_Y) {
                    Integer segments_size = map.entrySet()
                            .stream()
                            .filter(x -> x.getKey().contains(signature_feature.toUpperCase()))
                            .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).size() / 2;

                    double[] normalized_segments = new double[segments_size];
                    for(int i = 0; i < segments_size; i++) {
                        normalized_segments[i] = values[values_idx] * (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)) + map.get(min_key + "_" + i);
                        values_idx = values_idx + 1;
                    }

                    cur_method = swipe.getClass().getMethod("set" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)), double[].class);
                    cur_method.invoke(swipe, normalized_segments);
                } else {
                    cur_method = swipe.getClass().getMethod("set" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)), double.class);
                    cur_method.invoke(swipe, values[values_idx] * (map.get(max_key) - map.get(min_key)) + map.get(min_key));
                    values_idx = values_idx + 1;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        swipe.setHoldingPosition(holdingPosition);
        swipe.setUserId(userId);

        return swipe;
    }

    /**
     * 
     * @return
     */
    @Override
    public  String toString() {
        return "Swipe{" +
                "duration=" + duration +
                "\n length=" + length +
                "\n segmentsX=" + segmentsX +
                "\n segmentsY" + segmentsY +
                "\n minSize=" + minSize +
                "\n maxSize=" + maxSize +
                "\n avgSize=" + avgSize +
                "\n downSize=" + downSize +
                "\n upSize=" + upSize +
                "\n startX=" + startX +
                "\n startY=" + startY +
                "\n endX=" + endX +
                "\n endY=" + endY +
                "\n minXVelocity=" + minXVelocity +
                "\n maxXVelocity=" + maxXVelocity +
                "\n avgXVelocity=" + avgXVelocity +
                "\n stdXVelocity=" + stdXVelocity +
                "\n varXVelocity=" + varXVelocity +
                "\n minYVelocity=" + minYVelocity +
                "\n maxYVelocity=" + maxYVelocity +
                "\n avgYVelocity=" + avgYVelocity +
                "\n stdYVelocity=" + stdYVelocity +
                "\n varYVelocity=" + varYVelocity +
                "\n minXAccelerometer=" + minXAccelerometer +
                "\n maxXAccelerometer=" + maxXAccelerometer +
                "\n avgXAccelerometer=" + avgXAccelerometer +
                "\n stdXAccelerometer=" + stdXAccelerometer +
                "\n varXAccelerometer=" + varXAccelerometer +
                "\n minYAccelerometer=" + minYAccelerometer +
                "\n maxYAccelerometer=" + maxYAccelerometer +
                "\n avgYAccelerometer=" + avgYAccelerometer +
                "\n stdYAccelerometer=" + stdYAccelerometer +
                "\n varYAccelerometer=" + varYAccelerometer +
                "\n minZAccelerometer=" + minZAccelerometer +
                "\n maxZAccelerometer=" + maxZAccelerometer +
                "\n avgZAccelerometer=" + avgZAccelerometer +
                "\n stdZAccelerometer=" + stdZAccelerometer +
                "\n varZAccelerometer=" + varZAccelerometer +
                "\n minXGyroscope=" + minXGyroscope +
                "\n maxXGyroscope=" + maxXGyroscope +
                "\n avgXGyroscope=" + avgXGyroscope +
                "\n stdXGyroscope=" + stdXGyroscope +
                "\n varXGyroscope=" + varXGyroscope +
                "\n minYGyroscope=" + minYGyroscope +
                "\n maxYGyroscope=" + maxYGyroscope +
                "\n avgYGyroscope=" + avgYGyroscope +
                "\n stdYGyroscope=" + stdYGyroscope +
                "\n varYGyroscope=" + varYGyroscope +
                "\n minZGyroscope=" + minZGyroscope +
                "\n maxZGyroscope=" + maxZGyroscope +
                "\n avgZGyroscope=" + avgZGyroscope +
                "\n stdZGyroscope=" + stdZGyroscope +
                "\n varZGyroscope=" + varZGyroscope +
                "\n minXOrientation=" + minXOrientation +
                "\n maxXOrientation=" + maxXOrientation +
                "\n avgXOrientation=" + avgXOrientation +
                "\n stdXOrientation=" + stdXOrientation +
                "\n varXOrientation=" + varXOrientation +
                "\n minYOrientation=" + minYOrientation +
                "\n maxYOrientation=" + maxYOrientation +
                "\n avgYOrientation=" + avgYOrientation +
                "\n stdYOrientation=" + stdYOrientation +
                "\n varYOrientation=" + varYOrientation +
                "\n minZOrientation=" + minZOrientation +
                "\n maxZOrientation=" + maxZOrientation +
                "\n avgZOrientation=" + avgZOrientation +
                "\n stdZOrientation=" + stdZOrientation +
                "\n varZOrientation=" + varZOrientation +
                "\n keystrokeDurations=" + Arrays.toString(keystrokeDurations) +
                "\n keystrokeIntervals=" + Arrays.toString(keystrokeIntervals) +
                "\n keystrokeStartIntervals=" + Arrays.toString(keystrokeStartIntervals) +
                "\n keystrokeEndIntervals=" + Arrays.toString(keystrokeEndIntervals) +
                "\n keystrokeFullDuration=" + keystrokeFullDuration +
                "\n signatureStartX" + signatureStartX +
                "\n signatureStartY" + signatureStartY +
                "\n signatureEndX" + signatureEndX +
                "\n signatureEndY" + signatureEndY +
                "\n signatureStdX" + signatureStdX +
                "\n signatureStdY" + signatureStdY +
                "\n signatureDiffX" + signatureDiffX +
                "\n signatureDiffY" + signatureDiffY +
                "\n signatureEuclideanDistance" + signatureEuclideanDistance +
                "\n signatureAvgXVelocity" + signatureAvgXVelocity +
                "\n signatureAvgYVelocity" + signatureAvgYVelocity +
                "\n signatureMaxXVelocity" + signatureMaxXVelocity +
                "\n signatureMaxYVelocity" + signatureMaxYVelocity +
                "\n signatureSegmentsX" + Arrays.toString(signatureSegmentsX) +
                "\n signatureSegmentsY" + Arrays.toString(signatureSegmentsY) +
                "\n holdingPosition=" + holdingPosition +
                "\n userId=" + userId +
                '}';
    }

    /**
     * 
     * @param dataSet
     * @param isTrainInstance
     * @param dbHelper
     * @param trainingModel
     * @return
     */
    public  Instance getAsWekaInstance(Instances dataSet, boolean isTrainInstance, DatabaseHelper dbHelper, List<DatabaseHelper.ModelType> trainingModel) {
        Map<String, Integer> featureData = dbHelper.getFeatureData();
        boolean useAcceleration = featureData.get(DatabaseHelper.COL_ACCELERATION) == 1;
        boolean useAngularVelocity = featureData.get(DatabaseHelper.COL_ANGULAR_VELOCITY) == 1;
        boolean useOrientation = featureData.get(DatabaseHelper.COL_ORIENTATION) == 1;

        boolean useSwipeDuration = featureData.get(DatabaseHelper.COL_SWIPE_DURATION) == 1;
        boolean useSwipeShape = featureData.get(DatabaseHelper.COL_SWIPE_SHAPE) == 1;
        boolean useSwipeSize = featureData.get(DatabaseHelper.COL_SWIPE_TOUCH_SIZE) == 1;
        boolean useSwipeStartEndPos = featureData.get(DatabaseHelper.COL_SWIPE_START_END_POS) == 1;
        boolean useSwipeVelocity = featureData.get(DatabaseHelper.COL_SWIPE_VELOCITY) == 1;

        boolean useKeystroke = featureData.get(DatabaseHelper.COL_KEYSTROKE) == 1;
        boolean useKeystrokeDurations = featureData.get(DatabaseHelper.COL_KEYSTROKE_DURATIONS) == 1;
        boolean useKeystrokeIntervals = featureData.get(DatabaseHelper.COL_KEYSTROKE_INTERVALS) == 1;

        boolean useSignature = featureData.get(DatabaseHelper.COL_SIGNATURE) == 1;
        boolean useSignatureStartEndPos = featureData.get(DatabaseHelper.COL_SIGNATURE_START_END_POS) == 1;
        boolean useSignatureVelocity =  featureData.get(DatabaseHelper.COL_SIGNATURE) == 1;
        boolean useSignatureShape = featureData.get(DatabaseHelper.COL_SIGNATURE_SHAPE) == 1;

        ArrayList<Double> featureSet = new ArrayList<>();

        if(trainingModel.contains(DatabaseHelper.ModelType.SWIPE) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            if (useSwipeDuration) {
                featureSet.add(this.getDuration());
            }
            if (useSwipeShape) {
                featureSet.add(this.getLength());
                for(Double xSegment : this.getSegmentsX()) { featureSet.add(xSegment); }
                for(Double ySegment : this.getSegmentsY()) { featureSet.add(ySegment); }
            }
            if (useSwipeSize) {
                featureSet.add(this.getMinSize());
                featureSet.add(this.getMaxSize());
                featureSet.add(this.getAvgSize());
                featureSet.add(this.getDownSize());
                featureSet.add(this.getUpSize());
            }
            if (useSwipeStartEndPos) {
                featureSet.add(this.getStartX());
                featureSet.add(this.getStartY());
                featureSet.add(this.getEndX());
                featureSet.add(this.getEndY());
            }
            if (useSwipeVelocity) {
                featureSet.add(this.getMinXVelocity());
                featureSet.add(this.getMaxXVelocity());
                featureSet.add(this.getAvgXVelocity());
                featureSet.add(this.getStdXVelocity());
                featureSet.add(this.getVarXVelocity());
                featureSet.add(this.getMinYVelocity());
                featureSet.add(this.getMaxYVelocity());
                featureSet.add(this.getAvgYVelocity());
                featureSet.add(this.getStdYVelocity());
                featureSet.add(this.getVarYVelocity());
            }
        }
        if(trainingModel.contains(DatabaseHelper.ModelType.HOLD) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            if (useAcceleration) {
                featureSet.add(this.getMinXAccelerometer());
                featureSet.add(this.getMaxXAccelerometer());
                featureSet.add(this.getAvgXAccelerometer());
                featureSet.add(this.getStdXAccelerometer());
                featureSet.add(this.getVarXAccelerometer());
                featureSet.add(this.getMinYAccelerometer());
                featureSet.add(this.getMaxYAccelerometer());
                featureSet.add(this.getAvgYAccelerometer());
                featureSet.add(this.getStdYAccelerometer());
                featureSet.add(this.getVarYAccelerometer());
                featureSet.add(this.getMinZAccelerometer());
                featureSet.add(this.getMaxZAccelerometer());
                featureSet.add(this.getAvgZAccelerometer());
                featureSet.add(this.getStdZAccelerometer());
                featureSet.add(this.getVarZAccelerometer());
            }
            if (useAngularVelocity) {
                featureSet.add(this.getMinXGyroscope());
                featureSet.add(this.getMaxXGyroscope());
                featureSet.add(this.getAvgXGyroscope());
                featureSet.add(this.getStdXGyroscope());
                featureSet.add(this.getVarXGyroscope());
                featureSet.add(this.getMinYGyroscope());
                featureSet.add(this.getMaxYGyroscope());
                featureSet.add(this.getAvgYGyroscope());
                featureSet.add(this.getStdYGyroscope());
                featureSet.add(this.getVarYGyroscope());
                featureSet.add(this.getMinZGyroscope());
                featureSet.add(this.getMaxZGyroscope());
                featureSet.add(this.getAvgZGyroscope());
                featureSet.add(this.getStdZGyroscope());
                featureSet.add(this.getVarZGyroscope());
            }
            if (useOrientation) {
                featureSet.add(this.getMinXOrientation());
                featureSet.add(this.getMaxXOrientation());
                featureSet.add(this.getAvgXOrientation());
                featureSet.add(this.getStdXOrientation());
                featureSet.add(this.getVarXOrientation());
                featureSet.add(this.getMinYOrientation());
                featureSet.add(this.getMaxYOrientation());
                featureSet.add(this.getAvgYOrientation());
                featureSet.add(this.getStdYOrientation());
                featureSet.add(this.getVarYOrientation());
                featureSet.add(this.getMinZOrientation());
                featureSet.add(this.getMaxZOrientation());
                featureSet.add(this.getAvgZOrientation());
                featureSet.add(this.getStdZOrientation());
                featureSet.add(this.getVarZOrientation());
            }
        }
        if(trainingModel.contains(DatabaseHelper.ModelType.KEYSTROKE) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            if(useKeystroke) {
                if(useKeystrokeDurations) {
                    for(Double keystrokeDuration : this.getKeystrokeDurations()) { featureSet.add(keystrokeDuration); }
                    featureSet.add(this.getKeystrokeFullDuration());
                }
                if(useKeystrokeIntervals) {
                    for(Double keystrokeInterval : this.getKeystrokeIntervals()) { featureSet.add(keystrokeInterval); }
                    for(Double keystrokeStartInterval : this.getKeystrokeStartIntervals()) { featureSet.add(keystrokeStartInterval); }
                    for(Double keystrokeEndInterval : this.getKeystrokeEndIntervals()) { featureSet.add(keystrokeEndInterval); }
                }
            }
        }
        if(trainingModel.contains(DatabaseHelper.ModelType.SIGNATURE) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            if (useSignature) {
                if(useSignatureStartEndPos) {
                    featureSet.add(this.getSignatureStartX());
                    featureSet.add(this.getSignatureStartY());
                    featureSet.add(this.getSignatureEndX());
                    featureSet.add(this.getSignatureEndY());
                    featureSet.add(this.getSignatureStdX());
                    featureSet.add(this.getSignatureStdY());
                    featureSet.add(this.getSignatureDiffX());
                    featureSet.add(this.getSignatureDiffY());
                    featureSet.add(this.getSignatureEuclideanDistance());
                }
                if(useSignatureVelocity) {
                    featureSet.add(this.getSignatureAvgXVelocity());
                    featureSet.add(this.getSignatureAvgYVelocity());
                    featureSet.add(this.getSignatureMaxXVelocity());
                    featureSet.add(this.getSignatureMaxYVelocity());
                }
                if(useSignatureShape) {
                    for(Double signatureSegmentX : this.getSignatureSegmentsX()) { featureSet.add(signatureSegmentX); }
                    for(Double signatureSegmentY : this.getSignatureSegmentsY()) { featureSet.add(signatureSegmentY); }
                }
            }
        }

        Instance instance = new DenseInstance(featureSet.size() + 1);
        instance.setDataset(dataSet);
        for(int i = 0; i < featureSet.size(); i++) {
            instance.setValue(i, featureSet.get(i));
        }
        if(isTrainInstance){
            instance.setClassValue(this.userId);
        }
        return instance;
    }
}