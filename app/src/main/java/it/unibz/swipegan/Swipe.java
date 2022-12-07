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

    /**
     * User type identifier (User/Attacker).
     */
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
     * Getter for the Swipe duration (in seconds).
     * 
     * @return The Swipe duration (in seconds).
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Setter for the Swipe duration (in seconds).
     *
     * @param duration The Swipe duration (in seconds).
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Getter for the Swipe length.
     * 
     * @return The Swipe length.
     */
    public double getLength() {
        return length;
    }

    /**
     * Setter for the Swipe length.
     *
     * @param length The Swipe length.
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Getter for the Swipe X segments.
     * 
     * @return The Swipe X segments.
     */
    public double[] getSegmentsX() {
        return segmentsX;
    }

    /**
     * Setter for the Swipe X segments.
     *
     * @param segmentsX The Swipe X segments.
     */
    public void setSegmentsX(double[] segmentsX) {
        this.segmentsX = segmentsX;
    }

    /**
     * Getter for the Swipe Y segments.
     * 
     * @return The Swipe Y segments.
     */
    public double[] getSegmentsY() {
        return segmentsY;
    }

    /**
     * Setter for the Swipe Y segments.
     *
     * @param segmentsY The Swipe Y segments.
     */
    public void setSegmentsY(double[] segmentsY) {
        this.segmentsY = segmentsY;
    }

    /**
     * Getter for the Minimum Swipe touch size.
     * 
     * @return The Minimum Swipe touch size.
     */
    public double getMinSize() {
        return minSize;
    }

    /**
     * Setter for the Minimum Swipe touch size.
     * 
     * @param minSize The Minimum Swipe touch size.
     */
    public void setMinSize(double minSize) {
        this.minSize = minSize;
    }

    /**
     * Getter for the Maximum Swipe touch size.
     * 
     * @return The Maximum Swipe touch size.
     */
    public double getMaxSize() {
        return maxSize;
    }

    /**
     * Setter for the Maximum Swipe touch size.
     *
     * @param maxSize The Maximum Swipe touch size.
     */
    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Getter for the Average Swipe touch size.
     * 
     * @return The Average Swipe touch size.
     */
    public double getAvgSize() {
        return avgSize;
    }

    /**
     * Setter for the Average Swipe touch size.
     *
     * @param avgSize The Average Swipe touch size.
     */
    public void setAvgSize(double avgSize) {
        this.avgSize = avgSize;
    }

    /**
     * Getter for the Initial Swipe touch size.
     * 
     * @return The Initial Swipe touch size.
     */
    public double getDownSize() {
        return downSize;
    }

    /**
     * Setter for the Initial Swipe touch size.
     *
     * @param downSize The Initial Swipe touch size.
     */
    public void setDownSize(double downSize) {
        this.downSize = downSize;
    }

    /**
     * Getter for the Final Swipe touch size.
     * 
     * @return The Final Swipe touch size.
     */
    public double getUpSize() {
        return upSize;
    }

    /**
     * Setter for the Final Swipe touch size.
     *
     * @param upSize The Final Swipe touch size.
     */
    public void setUpSize(double upSize) {
        this.upSize = upSize;
    }

    /**
     * Getter for the Swipe start X coordinate.
     * 
     * @return The Swipe start X coordinate.
     */
    public double getStartX() {
        return startX;
    }

    /**
     * Setter for the Swipe start X coordinate.
     *
     * @param startX The Swipe start X coordinate.
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /**
     * Getter for the Swipe start Y coordinate.
     * 
     * @return The Swipe start Y coordinate.
     */
    public double getStartY() {
        return startY;
    }

    /**
     * Setter for the Swipe start Y coordinate.
     *
     * @param startY The Swipe start Y coordinate.
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /**
     * Getter for the Swipe end X coordinate.
     * 
     * @return The Swipe end X coordinate.
     */
    public double getEndX() {
        return endX;
    }

    /**
     * Setter for the Swipe end X coordinate.
     *
     * @param endX The Swipe end X coordinate.
     */
    public void setEndX(double endX) {
        this.endX = endX;
    }

    /**
     * Getter for the Swipe end Y coordinate.
     * 
     * @return The Swipe end Y coordinate.
     */
    public double getEndY() {
        return endY;
    }

    /**
     * Setter for the Swipe end Y coordinate.
     *
     * @param endY The Swipe end Y coordinate.
     */
    public void setEndY(double endY) {
        this.endY = endY;
    }

    /**
     * Getter for the Minimum Swipe X velocity.
     * 
     * @return The Minimum Swipe X velocity.
     */
    public double getMinXVelocity() {
        return minXVelocity;
    }

    /**
     * Setter for the Minimum Swipe X velocity.
     *
     * @param minXVelocity The Minimum Swipe X velocity.
     */
    public void setMinXVelocity(double minXVelocity) {
        this.minXVelocity = minXVelocity;
    }

    /**
     * Getter for the Maximum Swipe X velocity.
     * 
     * @return The Maximum Swipe X velocity.
     */
    public double getMaxXVelocity() {
        return maxXVelocity;
    }

    /**
     * Setter for the Maximum Swipe X velocity.
     *
     * @param maxXVelocity The Maximum Swipe X velocity.
     */
    public void setMaxXVelocity(double maxXVelocity) {
        this.maxXVelocity = maxXVelocity;
    }

    /**
     * Getter for the Average Swipe X velocity.
     * 
     * @return The Average Swipe X velocity.
     */
    public double getAvgXVelocity() {
        return avgXVelocity;
    }

    /**
     * Setter for the Average Swipe X velocity.
     *
     * @param avgXVelocity The Average Swipe X velocity.
     */
    public void setAvgXVelocity(double avgXVelocity) {
        this.avgXVelocity = avgXVelocity;
    }

    /**
     * Getter for the Standard deviation Swipe X velocity.
     * 
     * @return The Standard deviation Swipe X velocity.
     */
    public double getStdXVelocity() {
        return stdXVelocity;
    }

    /**
     * Setter for the Standard deviation Swipe X velocity.
     *
     * @param stdXVelocity The Standard deviation Swipe X velocity.
     */
    public void setStdXVelocity(double stdXVelocity) {
        this.stdXVelocity = stdXVelocity;
    }

    /**
     * Getter for the Variance Swipe X velocity.
     * 
     * @return The Variance Swipe X velocity.
     */
    public double getVarXVelocity() {
        return varXVelocity;
    }

    /**
     * Setter for the Variance Swipe X velocity.
     *
     * @param varXVelocity The Variance Swipe X velocity.
     */
    public void setVarXVelocity(double varXVelocity) {
        this.varXVelocity = varXVelocity;
    }

    /**
     * Getter for the Minimum Swipe Y velocity.
     * 
     * @return The Minimum Swipe Y velocity.
     */
    public double getMinYVelocity() {
        return minYVelocity;
    }

    /**
     * Setter for the Minimum Swipe Y velocity.
     *
     * @param minYVelocity The Minimum Swipe Y velocity.
     */
    public void setMinYVelocity(double minYVelocity) {
        this.minYVelocity = minYVelocity;
    }

    /**
     * Getter for the Maximum Swipe Y velocity.
     * 
     * @return The Maximum Swipe Y velocity.
     */
    public double getMaxYVelocity() {
        return maxYVelocity;
    }

    /**
     * Setter for the Maximum Swipe Y velocity.
     *
     * @param maxYVelocity The Maximum Swipe Y velocity.
     */
    public void setMaxYVelocity(double maxYVelocity) {
        this.maxYVelocity = maxYVelocity;
    }

    /**
     * Getter for the Average Swipe Y velocity.
     * 
     * @return The Average Swipe Y velocity.
     */
    public double getAvgYVelocity() {
        return avgYVelocity;
    }

    /**
     * Setter for the Average Swipe Y velocity.
     *
     * @param avgYVelocity The Average Swipe Y velocity.
     */
    public void setAvgYVelocity(double avgYVelocity) {
        this.avgYVelocity = avgYVelocity;
    }

    /**
     * Getter for the Standard deviation Swipe Y velocity.
     * 
     * @return The Standard deviation Swipe Y velocity.
     */
    public double getStdYVelocity() {
        return stdYVelocity;
    }

    /**
     * Setter for the Standard deviation Swipe Y velocity.
     *
     * @param stdYVelocity The Standard deviation Swipe Y velocity.
     */
    public void setStdYVelocity(double stdYVelocity) {
        this.stdYVelocity = stdYVelocity;
    }

    /**
     * Getter for the Variance Swipe Y velocity.
     * 
     * @return The Variance Swipe Y velocity.
     */
    public double getVarYVelocity() {
        return varYVelocity;
    }

    /**
     * Setter for the Variance Swipe Y velocity.
     *
     * @param varYVelocity The Variance Swipe Y velocity.
     */
    public void setVarYVelocity(double varYVelocity) {
        this.varYVelocity = varYVelocity;
    }

    /**
     * Getter for the Minimum X acceleration.
     * 
     * @return The Minimum X acceleration.
     */
    public double getMinXAccelerometer() {
        return minXAccelerometer;
    }

    /**
     * Setter for the Minimum X acceleration.
     *
     * @param minXAccelerometer The Minimum X acceleration.
     */
    public void setMinXAccelerometer(double minXAccelerometer) {
        this.minXAccelerometer = minXAccelerometer;
    }

    /**
     * Getter for the Maximum X acceleration.
     * 
     * @return The Maximum X acceleration.
     */
    public double getMaxXAccelerometer() {
        return maxXAccelerometer;
    }

    /**
     * Setter for the Maximum X acceleration.
     *
     * @param maxXAccelerometer The Maximum X acceleration.
     */
    public void setMaxXAccelerometer(double maxXAccelerometer) {
        this.maxXAccelerometer = maxXAccelerometer;
    }

    /**
     * Getter for the Average X acceleration.
     * 
     * @return The Average X acceleration.
     */
    public double getAvgXAccelerometer() {
        return avgXAccelerometer;
    }

    /**
     * Setter for the Average X acceleration.
     *
     * @param avgXAccelerometer The Average X acceleration.
     */
    public void setAvgXAccelerometer(double avgXAccelerometer) {
        this.avgXAccelerometer = avgXAccelerometer;
    }

    /**
     * Getter for the Standard deviation X acceleration.
     * 
     * @return The Standard deviation X acceleration.
     */
    public double getStdXAccelerometer() {
        return stdXAccelerometer;
    }

    /**
     * Setter for the Standard deviation X acceleration.
     *
     * @param stdXAccelerometer The Standard deviation X acceleration.
     */
    public void setStdXAccelerometer(double stdXAccelerometer) {
        this.stdXAccelerometer = stdXAccelerometer;
    }

    /**
     * Getter for the Variance X acceleration.
     * 
     * @return The Variance X acceleration.
     */
    public double getVarXAccelerometer() {
        return varXAccelerometer;
    }

    /**
     * Setter for the Variance X acceleration.
     *
     * @param varXAccelerometer The Variance X acceleration.
     */
    public void setVarXAccelerometer(double varXAccelerometer) {
        this.varXAccelerometer = varXAccelerometer;
    }

    /**
     * Getter for the Minimum Y acceleration.
     * 
     * @return The Minimum Y acceleration.
     */
    public double getMinYAccelerometer() {
        return minYAccelerometer;
    }

    /**
     * Setter for the Minimum Y acceleration.
     *
     * @param minYAccelerometer The Minimum Y acceleration.
     */
    public void setMinYAccelerometer(double minYAccelerometer) {
        this.minYAccelerometer = minYAccelerometer;
    }

    /**
     * Getter for the Maximum Y acceleration.
     * 
     * @return The Maximum Y acceleration.
     */
    public double getMaxYAccelerometer() {
        return maxYAccelerometer;
    }

    /**
     * Setter for the Maximum Y acceleration.
     *
     * @param maxYAccelerometer The Maximum Y acceleration.
     */
    public void setMaxYAccelerometer(double maxYAccelerometer) {
        this.maxYAccelerometer = maxYAccelerometer;
    }

    /**
     * Getter for the Average Y acceleration.
     * 
     * @return The Average Y acceleration.
     */
    public double getAvgYAccelerometer() {
        return avgYAccelerometer;
    }

    /**
     * Setter for the Average Y acceleration.
     *
     * @param avgYAccelerometer The Average Y acceleration.
     */
    public void setAvgYAccelerometer(double avgYAccelerometer) {
        this.avgYAccelerometer = avgYAccelerometer;
    }

    /**
     * Getter for the Standard deviation Y acceleration.
     * 
     * @return The Standard deviation Y acceleration.
     */
    public double getStdYAccelerometer() {
        return stdYAccelerometer;
    }

    /**
     * Setter for the Standard deviation Y acceleration.
     *
     * @param stdYAccelerometer The Standard deviation Y acceleration.
     */
    public void setStdYAccelerometer(double stdYAccelerometer) {
        this.stdYAccelerometer = stdYAccelerometer;
    }

    /**
     * Getter for the Variance Y acceleration.
     * 
     * @return The Variance Y acceleration.
     */
    public double getVarYAccelerometer() {
        return varYAccelerometer;
    }

    /**
     * Setter for the Variance Y acceleration.
     *
     * @param varYAccelerometer The Variance Y acceleration.
     */
    public void setVarYAccelerometer(double varYAccelerometer) {
        this.varYAccelerometer = varYAccelerometer;
    }

    /**
     * Getter for the Minimum Z acceleration.
     * 
     * @return The Minimum Z acceleration.
     */
    public double getMinZAccelerometer() {
        return minZAccelerometer;
    }

    /**
     * Setter for the Minimum Z acceleration.
     *
     * @param minZAccelerometer The Minimum Z acceleration.
     */
    public void setMinZAccelerometer(double minZAccelerometer) {
        this.minZAccelerometer = minZAccelerometer;
    }

    /**
     * Getter for the Maximum Z acceleration.
     * 
     * @return The Maximum Z acceleration.
     */
    public double getMaxZAccelerometer() {
        return maxZAccelerometer;
    }

    /**
     * Setter for the Maximum Z acceleration.
     *
     * @param maxZAccelerometer The Maximum Z acceleration.
     */
    public void setMaxZAccelerometer(double maxZAccelerometer) {
        this.maxZAccelerometer = maxZAccelerometer;
    }

    /**
     * Getter for the Average Z acceleration.
     * 
     * @return The Average Z acceleration.
     */
    public double getAvgZAccelerometer() {
        return avgZAccelerometer;
    }

    /**
     * Setter for the Average Z acceleration.
     *
     * @param avgZAccelerometer The Average Z acceleration.
     */
    public void setAvgZAccelerometer(double avgZAccelerometer) {
        this.avgZAccelerometer = avgZAccelerometer;
    }

    /**
     * Getter for the Standard deviation Z acceleration.
     * 
     * @return The Standard deviation Z acceleration.
     */
    public double getStdZAccelerometer() {
        return stdZAccelerometer;
    }

    /**
     * Setter for the Standard deviation Z acceleration.
     *
     * @param stdZAccelerometer The Standard deviation Z acceleration.
     */
    public void setStdZAccelerometer(double stdZAccelerometer) {
        this.stdZAccelerometer = stdZAccelerometer;
    }

    /**
     * Getter for the Variance Z acceleration.
     * 
     * @return The Variance Z acceleration.
     */
    public double getVarZAccelerometer() {
        return varZAccelerometer;
    }

    /**
     * Setter for the Variance Z acceleration.
     *
     * @param varZAccelerometer The Variance Z acceleration.
     */
    public void setVarZAccelerometer(double varZAccelerometer) {
        this.varZAccelerometer = varZAccelerometer;
    }

    /**
     * Getter for Minimum X angular velocity.
     * 
     * @return The Minimum X angular velocity.
     */
    public double getMinXGyroscope() {
        return minXGyroscope;
    }

    /**
     * Setter for the Minimum X angular velocity.
     *
     * @param minXGyroscope The Minimum X angular velocity.
     */
    public void setMinXGyroscope(double minXGyroscope) {
        this.minXGyroscope = minXGyroscope;
    }

    /**
     * Getter for the Maximum X angular velocity.
     * 
     * @return The Maximum X angular velocity.
     */
    public double getMaxXGyroscope() {
        return maxXGyroscope;
    }

    /**
     * Setter for the Maximum X angular velocity.
     *
     * @param maxXGyroscope The Maximum X angular velocity.
     */
    public void setMaxXGyroscope(double maxXGyroscope) {
        this.maxXGyroscope = maxXGyroscope;
    }

    /**
     * Getter for the Average X angular velocity.
     * 
     * @return The Average X angular velocity.
     */
    public double getAvgXGyroscope() {
        return avgXGyroscope;
    }

    /**
     * Setter for the Average X angular velocity.
     *
     * @param avgXGyroscope The Average X angular velocity.
     */
    public void setAvgXGyroscope(double avgXGyroscope) {
        this.avgXGyroscope = avgXGyroscope;
    }

    /**
     * Getter for the Standard deviation X angular velocity.
     * 
     * @return The Standard deviation X angular velocity.
     */
    public double getStdXGyroscope() {
        return stdXGyroscope;
    }

    /**
     * Setter for the Standard deviation X angular velocity.
     *
     * @param stdXGyroscope The Standard deviation X angular velocity.
     */
    public void setStdXGyroscope(double stdXGyroscope) {
        this.stdXGyroscope = stdXGyroscope;
    }

    /**
     * Getter for the Variance X angular velocity.
     * 
     * @return The Variance X angular velocity.
     */
    public double getVarXGyroscope() {
        return varXGyroscope;
    }

    /**
     * Setter for the Variance X angular velocity.
     *
     * @param varXGyroscope The Variance X angular velocity.
     */
    public void setVarXGyroscope(double varXGyroscope) {
        this.varXGyroscope = varXGyroscope;
    }

    /**
     * Getter for the Minimum Y angular velocity.
     * 
     * @return The Minimum Y angular velocity.
     */
    public double getMinYGyroscope() {
        return minYGyroscope;
    }

    /**
     * Setter for the Minimum Y angular velocity.
     *
     * @param minYGyroscope The Minimum Y angular velocity.
     */
    public void setMinYGyroscope(double minYGyroscope) {
        this.minYGyroscope = minYGyroscope;
    }

    /**
     * Getter for the Maximum Y angular velocity.
     * 
     * @return The Maximum Y angular velocity.
     */
    public double getMaxYGyroscope() {
        return maxYGyroscope;
    }

    /**
     * Setter for the Maximum Y angular velocity.
     *
     * @param maxYGyroscope The Maximum Y angular velocity.
     */
    public void setMaxYGyroscope(double maxYGyroscope) {
        this.maxYGyroscope = maxYGyroscope;
    }

    /**
     * Getter for the Average Y angular velocity.
     * 
     * @return The Average Y angular velocity.
     */
    public double getAvgYGyroscope() {
        return avgYGyroscope;
    }

    /**
     * Setter for the Average Y angular velocity.
     *
     * @param avgYGyroscope The Average Y angular velocity.
     */
    public void setAvgYGyroscope(double avgYGyroscope) {
        this.avgYGyroscope = avgYGyroscope;
    }

    /**
     * Getter for the Standard deviation Y angular velocity.
     * 
     * @return The Standard deviation Y angular velocity.
     */
    public  double getStdYGyroscope() {
        return stdYGyroscope;
    }

    /**
     * Setter for the Standard deviation Y angular velocity.
     *
     * @param stdYGyroscope The Standard deviation Y angular velocity.
     */
    public  void setStdYGyroscope(double stdYGyroscope) {
        this.stdYGyroscope = stdYGyroscope;
    }

    /**
     * Getter for the Variance Y angular velocity.
     * 
     * @return The Variance Y angular velocity.
     */
    public  double getVarYGyroscope() {
        return varYGyroscope;
    }

    /**
     * Setter for the Variance Y angular velocity.
     *
     * @param varYGyroscope The Variance Y angular velocity.
     */
    public  void setVarYGyroscope(double varYGyroscope) {
        this.varYGyroscope = varYGyroscope;
    }

    /**
     * Getter for the Minimum Z angular velocity.
     * 
     * @return The Minimum Z angular velocity.
     */
    public  double getMinZGyroscope() {
        return minZGyroscope;
    }

    /**
     * Setter for the Minimum Z angular velocity.
     *
     * @param minZGyroscope The Minimum Z angular velocity.
     */
    public  void setMinZGyroscope(double minZGyroscope) {
        this.minZGyroscope = minZGyroscope;
    }

    /**
     * Getter for the Maximum Z angular velocity.
     * 
     * @return The Maximum Z angular velocity.
     */
    public  double getMaxZGyroscope() {
        return maxZGyroscope;
    }

    /**
     * Setter for the Maximum Z angular velocity.
     *
     * @param maxZGyroscope The Maximum Z angular velocity.
     */
    public  void setMaxZGyroscope(double maxZGyroscope) {
        this.maxZGyroscope = maxZGyroscope;
    }

    /**
     * Getter for the Average Z angular velocity.
     * 
     * @return The Average Z angular velocity.
     */
    public  double getAvgZGyroscope() {
        return avgZGyroscope;
    }

    /**
     * Setter for the Average Z angular velocity.
     *
     * @param avgZGyroscope The Average Z angular velocity.
     */
    public  void setAvgZGyroscope(double avgZGyroscope) {
        this.avgZGyroscope = avgZGyroscope;
    }

    /**
     * Getter for the Standard deviation Z angular velocity.
     * 
     * @return The Standard deviation  Z angular velocity.
     */
    public  double getStdZGyroscope() {
        return stdZGyroscope;
    }

    /**
     * Setter for the Standard deviation  Z angular velocity.
     *
     * @param stdZGyroscope Standard deviation  Z angular velocity.
     */
    public  void setStdZGyroscope(double stdZGyroscope) {
        this.stdZGyroscope = stdZGyroscope;
    }

    /**
     * Getter for the Variance Z angular velocity.
     * 
     * @return The Variance Z angular velocity.
     */
    public  double getVarZGyroscope() {
        return varZGyroscope;
    }

    /**
     * Setter for the Variance Z angular velocity.
     *
     * @param varZGyroscope The Variance Z angular velocity.
     */
     public  void setVarZGyroscope(double varZGyroscope) {
     this.varZGyroscope = varZGyroscope;
     }

    /**
     * Getter for the Minimum X orientation value.
     *
     * @return The Minimum X orientation value.
     */
     public  double getMinXOrientation() {
     return minXOrientation;
     }

    /**
     * Setter for the Minimum X orientation value.
     *
     * @param minXOrientation The Minimum X orientation value.
     */
     public  void setMinXOrientation(double minXOrientation) {
     this.minXOrientation = minXOrientation;
     }

    /**
     * Getter for the Maximum X orientation value.
     *
     * @return The Maximum X orientation value.
     */
     public  double getMaxXOrientation() {
     return maxXOrientation;
     }

    /**
     * Setter Maximum X orientation value.
     *
     * @param maxXOrientation The Maximum X orientation value.
     */
     public  void setMaxXOrientation(double maxXOrientation) {
     this.maxXOrientation = maxXOrientation;
     }

    /**
     * Getter for the Average X orientation value.
     *
     * @return The Average X orientation value.
     */
     public  double getAvgXOrientation() {
     return avgXOrientation;
     }

    /**
     * Setter for the Average X orientation value.
     *
     * @param avgXOrientation The Average X orientation value.
     */
    public  void setAvgXOrientation(double avgXOrientation) {
        this.avgXOrientation = avgXOrientation;
    }

    /**
     * Getter for the Standard deviation X orientation value.
     * 
     * @return The Standard deviation X orientation value.
     */
    public  double getStdXOrientation() {
        return stdXOrientation;
    }

    /**
     * Setter for the Standard deviation X orientation value.
     *
     * @param stdXOrientation The Standard deviation X orientation value.
     */
    public  void setStdXOrientation(double stdXOrientation) {
        this.stdXOrientation = stdXOrientation;
    }

    /**
     * Getter for the Variance X orientation value.
     * 
     * @return The Variance X orientation value.
     */
    public  double getVarXOrientation() {
        return varXOrientation;
    }

    /**
     * Setter for the Variance X orientation value.
     *
     * @param varXOrientation The Variance X orientation value.
     */
    public  void setVarXOrientation(double varXOrientation) {
        this.varXOrientation = varXOrientation;
    }

    /**
     * Getter for the Minimum Y orientation value.
     * 
     * @return The Minimum Y orientation value.
     */
    public  double getMinYOrientation() {
        return minYOrientation;
    }

    /**
     * Setter for the Minimum Y orientation value.
     *
     * @param minYOrientation The Minimum Y orientation value.
     */
    public  void setMinYOrientation(double minYOrientation) {
        this.minYOrientation = minYOrientation;
    }

    /**
     * Getter for the Maximum Y orientation value.
     * 
     * @return The Maximum Y orientation value.
     */
    public  double getMaxYOrientation() {
        return maxYOrientation;
    }

    /**
     * Setter for the Maximum Y orientation value.
     *
     * @param maxYOrientation The Maximum Y orientation value.
     */
    public  void setMaxYOrientation(double maxYOrientation) {
        this.maxYOrientation = maxYOrientation;
    }

    /**
     * Getter for the Average Y orientation value.
     * 
     * @return The Average Y orientation value.
     */
    public  double getAvgYOrientation() {
        return avgYOrientation;
    }

    /**
     * Setter for the Average Y orientation value.
     *
     * @param avgYOrientation The Average Y orientation value.
     */
    public  void setAvgYOrientation(double avgYOrientation) {
        this.avgYOrientation = avgYOrientation;
    }

    /**
     * Getter for the Standard deviation Y orientation value.
     * 
     * @return The Standard deviation Y orientation value.
     */
    public  double getStdYOrientation() {
        return stdYOrientation;
    }

    /**
     * Setter for the Standard deviation Y orientation value.
     *
     * @param stdYOrientation The Standard deviation Y orientation value.
     */
    public  void setStdYOrientation(double stdYOrientation) {
        this.stdYOrientation = stdYOrientation;
    }

    /**
     * Getter for the Variance Y orientation value.
     * 
     * @return The Variance Y orientation value.
     */
    public  double getVarYOrientation() {
        return varYOrientation;
    }

    /**
     * Setter for the Variance Y orientation value.
     *
     * @param varYOrientation The Variance Y orientation value.
     */
    public  void setVarYOrientation(double varYOrientation) {
        this.varYOrientation = varYOrientation;
    }

    /**
     * Getter for the Minimum Z orientation value.
     * 
     * @return The Minimum Z orientation value.
     */
    public  double getMinZOrientation() {
        return minZOrientation;
    }

    /**
     * Setter for the Minimum Z orientation value.
     *
     * @param minZOrientation The Minimum Z orientation value.
     */
    public  void setMinZOrientation(double minZOrientation) {
        this.minZOrientation = minZOrientation;
    }

    /**
     * Getter for the Maximum Z orientation value.
     * 
     * @return The Maximum Z orientation value.
     */
     public  double getMaxZOrientation() {
     return maxZOrientation;
     }

    /**
     * Setter for the Maximum Z orientation value.
     *
     * @param maxZOrientation The Maximum Z orientation value.
     */
     public  void setMaxZOrientation(double maxZOrientation) {
     this.maxZOrientation = maxZOrientation;
     }

    /**
     * Getter for the Average Z orientation value.
     *
     * @return The Average Z orientation value.
     */
    public  double getAvgZOrientation() {
     return avgZOrientation;
     }

    /**
     * Setter for the Average Z orientation value.
     *
     * @param avgZOrientation The Average Z orientation value.
     */
     public  void setAvgZOrientation(double avgZOrientation) {
     this.avgZOrientation = avgZOrientation;
     }

    /**
     * Getter for the Standard deviation Z orientation value.
     *
     * @return The Standard deviation Z orientation value.
     */
     public  double getStdZOrientation() {
     return stdZOrientation;
     }

    /**
     * Setter for the Standard deviation Z orientation value.
     *
     * @param stdZOrientation The Standard deviation Z orientation value.
     */
     public  void setStdZOrientation(double stdZOrientation) {
     this.stdZOrientation = stdZOrientation;
     }

    /**
     * Getter for the Variance Z orientation value.
     *
     * @return The Variance Z orientation value.
     */
    public  double getVarZOrientation() {
        return varZOrientation;
    }

    /**
     * Setter for the Variance Z orientation value.
     *
     * @param varZOrientation The Variance Z orientation value.
     */
    public  void setVarZOrientation(double varZOrientation) {
        this.varZOrientation = varZOrientation;
    }

    /**
     * Getter for the Keystroke individual press durations.
     * 
     * @return The Keystroke individual press durations.
     */
    public  double[] getKeystrokeDurations() {
        return this.keystrokeDurations;
    }

    /**
     * Setter for the Keystroke individual press durations.
     *
     * @param keystrokeDurations The Keystroke individual press durations.
     */
    public  void setKeystrokeDurations(double[] keystrokeDurations) {
        this.keystrokeDurations = keystrokeDurations;
    }

    /**
     * Setter for an individual Keystroke press durations.
     *
     * @param duration The duration of the keystroke.
     * @param curKeystroke The current keystroke identifier.
     * @param pinLength The total length of the sequence.
     */
    public  void addKeystrokeDuration(double duration, int curKeystroke, int pinLength) {
        if(this.keystrokeDurations == null) {
            this.keystrokeDurations = new double[pinLength];
        }

        this.keystrokeDurations[curKeystroke] = duration;
    }

    /**
     * Getter for the Keystroke individual press intervals.
     * 
     * @return The Keystroke individual press intervals.
     */
    public  double[] getKeystrokeIntervals() {
        return this.keystrokeIntervals;
    }

    /**
     * Setter for the Keystroke individual press intervals.
     *
     * @param keystrokeIntervals The Keystroke individual press intervals.
     */
    public  void setKeystrokeIntervals(double[] keystrokeIntervals) {
        this.keystrokeIntervals = keystrokeIntervals;
    }

    /**
     * Setter for an individual Keystroke press durations.
     *
     * @param interval The duration of the interval.
     * @param curKeystroke The current keystroke identifier.
     * @param pinLength The total length of the sequence.
     */
    public  void addKeystrokeInterval(double interval, int curKeystroke, int pinLength) {
        if(this.keystrokeIntervals == null) {
            this.keystrokeIntervals = new double[pinLength - 1];
        }

        this.keystrokeIntervals[curKeystroke] = interval;
    }

    /**
     * Getter for the Keystroke start intervals.
     * 
     * @return The Keystroke start intervals.
     */
    public  double[] getKeystrokeStartIntervals() {
        return this.keystrokeStartIntervals;
    }

    /**
     * Setter for the Keystroke start intervals.
     *
     * @param keystrokeStartIntervals The Keystroke start intervals.
     */
    public  void setKeystrokeStartIntervals(double[] keystrokeStartIntervals) {
        this.keystrokeStartIntervals = keystrokeStartIntervals;
    }


    /**
     * Setter for an individual Keystroke start interval.
     *
     * @param startInterval The duration of the start interval.
     * @param curKeystroke The current keystroke identifier.
     * @param pinLength The total length of the sequence.
     */
    public  void addKeystrokeStartInterval(double startInterval, int curKeystroke, int pinLength) {
        if(this.keystrokeStartIntervals == null) {
            this.keystrokeStartIntervals = new double[pinLength - 1];
        }

        this.keystrokeStartIntervals[curKeystroke] = startInterval;
    }

    /**
     * Getter for the Keystroke end intervals.
     * 
     * @return The Keystroke end intervals.
     */
    public  double[] getKeystrokeEndIntervals() {
        return this.keystrokeEndIntervals;
    }

    /**
     * Setter for the Keystroke end intervals.
     *
     * @param keystrokeEndIntervals The Keystroke end intervals.
     */
    public  void setKeystrokeEndIntervals(double[] keystrokeEndIntervals) {
        this.keystrokeEndIntervals = keystrokeEndIntervals;
    }

    /**
     * Setter for an individual Keystroke end interval.
     *
     * @param endInterval The duration of the end interval.
     * @param curKeystroke The current keystroke identifier.
     * @param pinLength The total length of the sequence.
     */
    public  void addKeystrokeEndInterval(double endInterval, int curKeystroke, int pinLength) {
        if(this.keystrokeEndIntervals == null) {
            this.keystrokeEndIntervals = new double[pinLength - 1];
        }

        this.keystrokeEndIntervals[curKeystroke] = endInterval;
    }

    /**
     * Getter for the Full Keystroke duration.
     * 
     * @return The Full Keystroke duration.
     */
    public  double getKeystrokeFullDuration() {
        return this.keystrokeFullDuration;
    }

    /**
     * Setter for the Full Keystroke duration.
     */
    public void setKeystrokeFullDuration() {
        this.keystrokeFullDuration = Arrays.stream(this.getKeystrokeDurations()).sum() + Arrays.stream(this.getKeystrokeIntervals()).sum();
    }

    /**
     * Getter for the Signature initial X coordinate value.
     * 
     * @return The Signature initial X coordinate value.
     */
    public  double getSignatureStartX() {
        return signatureStartX;
    }

    /**
     * Setter for the Signature initial X coordinate value.
     *
     * @param signatureStartX The Signature initial X coordinate value.
     */
    public  void setSignatureStartX(double signatureStartX) {
        this.signatureStartX = signatureStartX;
    }

    /**
     * Getter for the Signature initial Y coordinate value.
     * 
     * @return The Signature initial Y coordinate value.
     */
    public  double getSignatureStartY() {
        return signatureStartY;
    }

    /**
     * Setter for the Signature initial Y coordinate value.
     *
     * @param signatureStartY Teh Signature initial Y coordinate value.
     */
    public  void setSignatureStartY(double signatureStartY) {
        this.signatureStartY = signatureStartY;
    }

    /**
     * Getter for the Signature final X coordinate value.
     * 
     * @return The Signature final X coordinate value.
     */
    public  double getSignatureEndX() {
        return signatureEndX;
    }

    /**
     * Setter for the Signature final X coordinate value.
     *
     * @param signatureEndX The Signature final X coordinate value.
     */
    public  void setSignatureEndX(double signatureEndX) {
        this.signatureEndX = signatureEndX;
    }

    /**
     * Getter for the Signature final Y coordinate value.
     * 
     * @return The Signature final Y coordinate value.
     */
    public  double getSignatureEndY() {
        return signatureEndY;
    }

    /**
     * Setter for the Signature final Y coordinate value.
     *
     * @param signatureEndY The Signature final Y coordinate value.
     */
    public  void setSignatureEndY(double signatureEndY) {
        this.signatureEndY = signatureEndY;
    }

    /**
     * Getter for the Signature Standard deviation X coordinate value.
     * 
     * @return The Signature Standard deviation X coordinate value.
     */
    public  double getSignatureStdX() {
        return signatureStdX;
    }

    /**
     * Setter for the Signature Standard deviation X coordinate value.
     *
     * @param signatureStdX The Signature Standard deviation X coordinate value.
     */
    public  void setSignatureStdX(double signatureStdX) {
        this.signatureStdX = signatureStdX;
    }

    /**
     * Getter for the Signature Standard deviation Y coordinate value.
     * 
     * @return The Signature Standard deviation Y coordinate value.
     */
    public  double getSignatureStdY() {
        return signatureStdY;
    }

    /**
     * Setter for the Signature Standard deviation Y coordinate value.
     *
     * @param signatureStdY The Signature Standard deviation Y coordinate value.
     */
    public  void setSignatureStdY(double signatureStdY) {
        this.signatureStdY = signatureStdY;
    }

    /**
     * Getter for the Signature X coordinate min / max difference.
     * 
     * @return The Signature X coordinate min / max difference.
     */
    public  double getSignatureDiffX() {
        return signatureDiffX;
    }

    /**
     * Setter for the Signature X coordinate min / max difference.
     *
     * @param signatureDiffX The Signature X coordinate min / max difference.
     */
    public  void setSignatureDiffX(double signatureDiffX) {
        this.signatureDiffX = signatureDiffX;
    }

    /**
     * Getter for the Signature Y coordinate min / max difference.
     * 
     * @return The Signature Y coordinate min / max difference.
     */
    public  double getSignatureDiffY() {
        return signatureDiffY;
    }

    /**
     * Setter for the Signature Y coordinate min / max difference.
     *
     * @param signatureDiffY The Signature Y coordinate min / max difference.
     */
    public  void setSignatureDiffY(double signatureDiffY) {
        this.signatureDiffY = signatureDiffY;
    }

    /**
     * Getter for the Signature start end position euclidean distance.
     * 
     * @return The Signature start end position euclidean distance.
     */
    public  double getSignatureEuclideanDistance() {
        return signatureEuclideanDistance;
    }

    /**
     * Setter for the Signature start end position euclidean distance.
     *
     * @param signatureEuclideanDistance The Signature start end position euclidean distance.
     */
    public  void setSignatureEuclideanDistance(double signatureEuclideanDistance) {
        this.signatureEuclideanDistance = signatureEuclideanDistance;
    }

    /**
     * Getter for the Signature average horizontal velocity.
     * 
     * @return The Signature average horizontal velocity.
     */
    public  double getSignatureAvgXVelocity() {
        return signatureAvgXVelocity;
    }

    /**
     * Setter for the Signature average horizontal velocity.
     *
     * @param signatureAvgXVelocity The Signature average horizontal velocity.
     */
    public  void setSignatureAvgXVelocity(double signatureAvgXVelocity) {
        this.signatureAvgXVelocity = signatureAvgXVelocity;
    }

    /**
     * Getter for the Signature average vertical velocity.
     * 
     * @return The Signature average vertical velocity.
     */
    public  double getSignatureAvgYVelocity() {
        return signatureAvgYVelocity;
    }

    /**
     * Setter for the Signature average vertical velocity.
     *
     * @param signatureAvgYVelocity The Signature average vertical velocity.
     */
    public  void setSignatureAvgYVelocity(double signatureAvgYVelocity) {
        this.signatureAvgYVelocity = signatureAvgYVelocity;
    }

    /**
     * Getter for the Signature maximum horizontal velocity.
     * 
     * @return The Signature maximum horizontal velocity.
     */
    public  double getSignatureMaxXVelocity() {
        return signatureMaxXVelocity;
    }

    /**
     * Setter for the Signature maximum horizontal velocity.
     *
     * @param signatureMaxXVelocity The Signature maximum horizontal velocity.
     */
    public  void setSignatureMaxXVelocity(double signatureMaxXVelocity) {
        this.signatureMaxXVelocity = signatureMaxXVelocity;
    }

    /**
     * Getter for the Signature maximum vertical velocity.
     * 
     * @return The Signature maximum vertical velocity.
     */
    public  double getSignatureMaxYVelocity() {
        return signatureMaxYVelocity;
    }

    /**
     * Setter for the Signature maximum vertical velocity.
     *
     * @param signatureMaxYVelocity The Signature maximum vertical velocity.
     */
    public  void setSignatureMaxYVelocity(double signatureMaxYVelocity) {
        this.signatureMaxYVelocity = signatureMaxYVelocity;
    }

    /**
     * Getter for the Signature X segment values.
     * 
     * @return The Signature X segment values.
     */
    public  double[] getSignatureSegmentsX() {
        return signatureSegmentsX;
    }

    /**
     * Setter for the Signature X segment values.
     *
     * @param signatureSegmentsX The Signature X segment values.
     */
    public  void setSignatureSegmentsX(double[] signatureSegmentsX) {
        this.signatureSegmentsX = signatureSegmentsX;
    }

    /**
     * Getter for the Signature Y segment values.
     * 
     * @return The Signature Y segment values.
     */
    public  double[] getSignatureSegmentsY() {
        return signatureSegmentsY;
    }

    /**
     * Setter for the Signature Y segment values.
     *
     * @param signatureSegmentsY The Signature Y segment values.
     */
    public  void setSignatureSegmentsY(double[] signatureSegmentsY) {
        this.signatureSegmentsY = signatureSegmentsY;
    }

    /**
     * Getter for the Holding position identifier.
     * 
     * @return The Holding position identifier (1 = Sitting, 2 = Standing, 3 = Walking).
     */
    public  double getHoldingPosition() {
        return holdingPosition;
    }

    /**
     * Setter for the Holding position identifier.
     *
     * @param holdingPosition The Holding position identifier (1 = Sitting, 2 = Standing, 3 = Walking).
     */
    public  void setHoldingPosition(double holdingPosition) {
        this.holdingPosition = holdingPosition;
    }

    /**
     * Getter for the User type identifier.
     * 
     * @return The User type identifier (User/Attacker).
     */
    public  String getUserId() {
        return userId;
    }

    /**
     * Setter for the User type identifier.
     *
     * @param userId The User type identifier (User/Attacker).
     */
    public  void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter for the Test interaction authentication results.
     * 
     * @return The Test interaction authentication results.
     */
    public  double[] getAuthentication() {
        return this.authentication;
    }

    /**
     * Setter for the Test interaction authentication results.
     *
     * @param authentication The Test interaction authentication results.
     */
    public  void setAuthentication(double[] authentication) {
        this.authentication = authentication;
    }

    /**
     * Getter for the Test interaction authentication times.
     * 
     * @return The Test interaction authentication times.
     */
    public  double[] getAuthenticationTime() {
        return this.authenticationTime;
    }

    /**
     * Setter for the Test interaction authentication times.
     *
     * @param authenticationTime The Test interaction authentication times.
     */
    public  void setAuthenticationTime(double[] authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    /**
     * Getter for the Nr. of training samples.
     * 
     * @return The Nr. of training samples.
     */
    public  int getClassifierSamples() {
        return classifierSamples;
    }

    /**
     * Setter for the Nr. of training samples.
     *
     * @param classifierSamples The Nr. of training samples.
     */
    public  void setClassifierSamples(int classifierSamples) {
        this.classifierSamples = classifierSamples;
    }

    /**
     * Gets from the passed examples (and for each active feature) the minimum and maximum values.
     *
     * @param allSwipes The set of (training) interactions.
     * @return The set of minimum and maximum values for each feature in the form of an Hashmap with keys having the format: MIN_FEATURE_NAME, MAX_FEATURE_NAME.
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
     * Generates the set of normalized values for the current interaction (Swipe object).
     * Normalized values are computed starting from the non-normalized ones using min/max scaling via the following formula: (x - min(x)) / (max(x) - min(x)).
     * Where x is the value of a given feature for the current object and min(x) and max(x) are the minimum and maximum values for that feature across all passed interactions.
     *
     * @param allSwipes The set of (training) interactions.
     * @return An array containing the normalized feature values for the current Swipe object.
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
     * Generates a non-normalized interaction starting from the passed (normalized) feature values.
     * Non-normalized values are computed starting from the normalized ones via the following formula: (x * (max(x) - min(x))) + min(x)
     * Where x is the value of a given feature and min(x) and max(x) are the minimum and maximum values for that feature across all passed interactions.
     *
     * @param values The array containing the feature values of the normalized interaction.
     * @param holdingPosition The interaction holding position (1 = Sitting, 2 = Standing, 3 = Walking).
     * @param userId Interaction user type identifier (User/Attacker).
     * @param allSwipes The set of (training) interactions.
     * @return The non-normalized interaction in the form of a newly created Swipe object.
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
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
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
     * Generates the weka.core.Instance of the Swipe object.
     *
     * @param dataSet The weka.core.Instances object acting as the Instance dataset.
     * @param isTrainInstance Indicates whether the current objects represents a training interaction.
     * @param dbHelper The instance of the DatabaseHelper.
     * @param modelType The identifier of the model that will interact with the generated Instance object.
     * @return The weka.core.Instance object generated from the current object.
     */
    public  Instance getAsWekaInstance(Instances dataSet, boolean isTrainInstance, DatabaseHelper dbHelper, List<DatabaseHelper.ModelType> modelType) {
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

        if(modelType.contains(DatabaseHelper.ModelType.SWIPE) || modelType.contains(DatabaseHelper.ModelType.FULL)) {
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
        if(modelType.contains(DatabaseHelper.ModelType.HOLD) || modelType.contains(DatabaseHelper.ModelType.FULL)) {
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
        if(modelType.contains(DatabaseHelper.ModelType.KEYSTROKE) || modelType.contains(DatabaseHelper.ModelType.FULL)) {
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
        if(modelType.contains(DatabaseHelper.ModelType.SIGNATURE) || modelType.contains(DatabaseHelper.ModelType.FULL)) {
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