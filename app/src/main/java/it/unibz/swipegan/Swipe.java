package it.unibz.swipegan;

import android.content.res.Resources;

import java.util.ArrayList;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Swipe {
    private static final double MIN_DURATION = 0;
    private static final double MAX_DURATION = 6000;

    private static final double MIN_START_X = 0;
    private static final double MAX_START_X = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static final double MIN_START_Y = 0;
    private static final double MAX_START_Y = Resources.getSystem().getDisplayMetrics().heightPixels;

    private static final double MIN_END_X = 0;
    private static final double MAX_END_X = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static final double MIN_END_Y = 0;
    private static final double MAX_END_Y = Resources.getSystem().getDisplayMetrics().heightPixels;

    private static final double MIN_X_VELOCITY = -2_000;
    private static final double MAX_X_VELOCITY = 2_000;
    private static final double MIN_Y_VELOCITY = -2_000;
    private static final double MAX_Y_VELOCITY = 2_000;

    private static final double MIN_X_ACCELEROMETER = -10;
    private static final double MAX_X_ACCELEROMETER = 10;
    private static final double MIN_Y_ACCELEROMETER = -10;
    private static final double MAX_Y_ACCELEROMETER = 10;
    private static final double MIN_Z_ACCELEROMETER = -10;
    private static final double MAX_Z_ACCELEROMETER = 10;

    private static final double MIN_X_GYROSCOPE = -10;
    private static final double MAX_X_GYROSCOPE = 10;
    private static final double MIN_Y_GYROSCOPE = -10;
    private static final double MAX_Y_GYROSCOPE = 10;
    private static final double MIN_Z_GYROSCOPE = -10;
    private static final double MAX_Z_GYROSCOPE = 10;

    private static final double MIN_X_ORIENTATION = -Math.PI/2;
    private static final double MAX_X_ORIENTATION = Math.PI/2;
    private static final double MIN_Y_ORIENTATION = -Math.PI;
    private static final double MAX_Y_ORIENTATION = Math.PI;
    private static final double MIN_Z_ORIENTATION = -Math.PI;
    private static final double MAX_Z_ORIENTATION = Math.PI;

    private double duration;
    private double avgSize;
    private double downSize;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double minXVelocity;
    private double maxXVelocity;
    private double avgXVelocity;
    private double varXVelocity;
    private double stdXVelocity;
    private double minYVelocity;
    private double maxYVelocity;
    private double avgYVelocity;
    private double varYVelocity;
    private double stdYVelocity;
    private double minXAccelerometer;
    private double maxXAccelerometer;
    private double avgXAccelerometer;
    private double varXAccelerometer;
    private double stdXAccelerometer;
    private double minYAccelerometer;
    private double maxYAccelerometer;
    private double avgYAccelerometer;
    private double varYAccelerometer;
    private double stdYAccelerometer;
    private double minZAccelerometer;
    private double maxZAccelerometer;
    private double avgZAccelerometer;
    private double varZAccelerometer;
    private double stdZAccelerometer;
    private double minXGyroscope;
    private double maxXGyroscope;
    private double avgXGyroscope;
    private double varXGyroscope;
    private double stdXGyroscope;
    private double minYGyroscope;
    private double maxYGyroscope;
    private double avgYGyroscope;
    private double varYGyroscope;
    private double stdYGyroscope;
    private double minZGyroscope;
    private double maxZGyroscope;
    private double avgZGyroscope;
    private double varZGyroscope;
    private double stdZGyroscope;
    private double minXOrientation;
    private double maxXOrientation;
    private double avgXOrientation;
    private double varXOrientation;
    private double stdXOrientation;
    private double minYOrientation;
    private double maxYOrientation;
    private double avgYOrientation;
    private double varYOrientation;
    private double stdYOrientation;
    private double minZOrientation;
    private double maxZOrientation;
    private double avgZOrientation;
    private double varZOrientation;
    private double stdZOrientation;
    private double holdingPosition;
    private String userId;

    public static Swipe fromNormalizedValues(double[] values, double holdingPosition, String userId) {

        Swipe swipe = new Swipe();
        swipe.setDuration(values[0] * (MAX_DURATION - MIN_DURATION) + MIN_DURATION);
        swipe.setAvgSize(values[1]);
        swipe.setDownSize(values[2]);
        swipe.setStartX(values[3] * (MAX_START_X - MIN_START_X) + MIN_START_X);
        swipe.setStartY(values[4] * (MAX_START_Y - MIN_START_Y) + MIN_START_Y);
        swipe.setEndX(values[5] * (MAX_END_X - MIN_END_X) + MIN_END_X);
        swipe.setEndY(values[6] * (MAX_END_Y - MIN_END_Y) + MIN_END_Y);

        swipe.setMinXVelocity(values[7] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setMaxXVelocity(values[8] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setAvgXVelocity(values[9] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setStdXVelocity(values[10] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setVarXVelocity(values[11] * Math.pow(MAX_X_VELOCITY - MIN_X_VELOCITY, 2) + MIN_X_VELOCITY);

        swipe.setMinYVelocity(values[12] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setMaxYVelocity(values[13] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setAvgYVelocity(values[14] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setStdYVelocity(values[15] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setVarYVelocity(values[16] * Math.pow(MAX_Y_VELOCITY - MIN_Y_VELOCITY, 2) + MIN_Y_VELOCITY);

        swipe.setMinXAccelerometer(values[17] * (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER) + MIN_X_ACCELEROMETER);
        swipe.setMaxXAccelerometer(values[18] * (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER) + MIN_X_ACCELEROMETER);
        swipe.setAvgXAccelerometer(values[19] * (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER) + MIN_X_ACCELEROMETER);
        swipe.setStdXAccelerometer(values[20] * (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER) + MIN_X_ACCELEROMETER);
        swipe.setVarXAccelerometer(values[21] * Math.pow(MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER, 2) + MIN_X_ACCELEROMETER);

        swipe.setMinYAccelerometer(values[22] * (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER) + MIN_Y_ACCELEROMETER);
        swipe.setMaxYAccelerometer(values[23] * (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER) + MIN_Y_ACCELEROMETER);
        swipe.setAvgYAccelerometer(values[24] * (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER) + MIN_Y_ACCELEROMETER);
        swipe.setStdYAccelerometer(values[25] * (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER) + MIN_Y_ACCELEROMETER);
        swipe.setVarYAccelerometer(values[26] * Math.pow(MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER, 2) + MIN_Y_ACCELEROMETER);

        swipe.setMinZAccelerometer(values[27] * (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER) + MIN_Z_ACCELEROMETER);
        swipe.setMaxZAccelerometer(values[28] * (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER) + MIN_Z_ACCELEROMETER);
        swipe.setAvgZAccelerometer(values[29] * (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER) + MIN_Z_ACCELEROMETER);
        swipe.setStdZAccelerometer(values[30] * (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER) + MIN_Z_ACCELEROMETER);
        swipe.setVarZAccelerometer(values[31] * Math.pow(MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER, 2) + MIN_Z_ACCELEROMETER);

        swipe.setMinXGyroscope(values[17] * (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE) + MIN_X_GYROSCOPE);
        swipe.setMaxXGyroscope(values[18] * (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE) + MIN_X_GYROSCOPE);
        swipe.setAvgXGyroscope(values[19] * (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE) + MIN_X_GYROSCOPE);
        swipe.setStdXGyroscope(values[20] * (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE) + MIN_X_GYROSCOPE);
        swipe.setVarXGyroscope(values[21] * Math.pow(MAX_X_GYROSCOPE - MIN_X_GYROSCOPE, 2) + MIN_X_GYROSCOPE);

        swipe.setMinYGyroscope(values[22] * (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE) + MIN_Y_GYROSCOPE);
        swipe.setMaxYGyroscope(values[23] * (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE) + MIN_Y_GYROSCOPE);
        swipe.setAvgYGyroscope(values[24] * (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE) + MIN_Y_GYROSCOPE);
        swipe.setStdYGyroscope(values[25] * (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE) + MIN_Y_GYROSCOPE);
        swipe.setVarYGyroscope(values[26] * Math.pow(MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE, 2) + MIN_Y_GYROSCOPE);

        swipe.setMinZGyroscope(values[27] * (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE) + MIN_Z_GYROSCOPE);
        swipe.setMaxZGyroscope(values[28] * (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE) + MIN_Z_GYROSCOPE);
        swipe.setAvgZGyroscope(values[29] * (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE) + MIN_Z_GYROSCOPE);
        swipe.setStdZGyroscope(values[30] * (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE) + MIN_Z_GYROSCOPE);
        swipe.setVarZGyroscope(values[31] * Math.pow(MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE, 2) + MIN_Z_GYROSCOPE);

        swipe.setMinXOrientation(values[32] * (MAX_X_ORIENTATION - MIN_X_ORIENTATION) + MIN_X_ORIENTATION);
        swipe.setMaxXOrientation(values[33] * (MAX_X_ORIENTATION - MIN_X_ORIENTATION) + MIN_X_ORIENTATION);
        swipe.setAvgXOrientation(values[34] * (MAX_X_ORIENTATION - MIN_X_ORIENTATION) + MIN_X_ORIENTATION);
        swipe.setStdXOrientation(values[35] * (MAX_X_ORIENTATION - MIN_X_ORIENTATION) + MIN_X_ORIENTATION);
        swipe.setVarXOrientation(values[36] * Math.pow(MAX_X_ORIENTATION - MIN_X_ORIENTATION, 2) + MIN_X_ORIENTATION);

        swipe.setMinYOrientation(values[37] * (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION) + MIN_Y_ORIENTATION);
        swipe.setMaxYOrientation(values[38] * (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION) + MIN_Y_ORIENTATION);
        swipe.setAvgYOrientation(values[39] * (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION) + MIN_Y_ORIENTATION);
        swipe.setStdYOrientation(values[40] * (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION) + MIN_Y_ORIENTATION);
        swipe.setVarYOrientation(values[41] * Math.pow(MAX_Y_ORIENTATION - MIN_Y_ORIENTATION, 2) + MIN_Y_ORIENTATION);

        swipe.setMinZOrientation(values[42] * (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION) + MIN_Z_ORIENTATION);
        swipe.setMaxZOrientation(values[43] * (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION) + MIN_Z_ORIENTATION);
        swipe.setAvgZOrientation(values[44] * (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION) + MIN_Z_ORIENTATION);
        swipe.setStdZOrientation(values[45] * (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION) + MIN_Z_ORIENTATION);
        swipe.setVarZOrientation(values[46] * Math.pow(MAX_Z_ORIENTATION - MIN_Z_ORIENTATION, 2) + MIN_Z_ORIENTATION);

        swipe.setHoldingPosition(holdingPosition);

        swipe.setUserId(userId);

        return swipe;
    }

    public Swipe() {

    }

    public Swipe(double[] swipeArray, double holdingPosition, String userId) {
        this.duration = swipeArray[0];
        this.avgSize = swipeArray[1];
        this.downSize = swipeArray[2];
        this.startX = swipeArray[3];
        this.startY = swipeArray[4];
        this.endX = swipeArray[5];
        this.endY = swipeArray[6];
        this.minXVelocity = swipeArray[7];
        this.maxXVelocity = swipeArray[8];
        this.avgXVelocity = swipeArray[9];
        this.stdXVelocity = swipeArray[10];
        this.varXVelocity = swipeArray[11];
        this.minYVelocity = swipeArray[12];
        this.maxYVelocity = swipeArray[13];
        this.avgYVelocity = swipeArray[14];
        this.stdYVelocity = swipeArray[15];
        this.varYVelocity = swipeArray[16];
        this.minXAccelerometer = swipeArray[17];
        this.maxXAccelerometer = swipeArray[18];
        this.avgXAccelerometer = swipeArray[19];
        this.stdXAccelerometer = swipeArray[20];
        this.varXAccelerometer = swipeArray[21];
        this.minYAccelerometer = swipeArray[22];
        this.maxYAccelerometer = swipeArray[23];
        this.avgYAccelerometer = swipeArray[24];
        this.stdYAccelerometer = swipeArray[25];
        this.varYAccelerometer = swipeArray[26];
        this.minZAccelerometer = swipeArray[27];
        this.maxZAccelerometer = swipeArray[28];
        this.avgZAccelerometer = swipeArray[29];
        this.stdZAccelerometer = swipeArray[30];
        this.varZAccelerometer = swipeArray[31];
        this.minXGyroscope = swipeArray[32];
        this.maxXGyroscope = swipeArray[33];
        this.avgXGyroscope = swipeArray[34];
        this.stdXGyroscope = swipeArray[35];
        this.varXGyroscope = swipeArray[36];
        this.minYGyroscope = swipeArray[37];
        this.maxYGyroscope = swipeArray[38];
        this.avgYGyroscope = swipeArray[39];
        this.stdYGyroscope = swipeArray[40];
        this.varYGyroscope = swipeArray[41];
        this.minZGyroscope = swipeArray[42];
        this.maxZGyroscope = swipeArray[43];
        this.avgZGyroscope = swipeArray[44];
        this.stdZGyroscope = swipeArray[45];
        this.varZGyroscope = swipeArray[46];
        this.minXOrientation = swipeArray[47];
        this.maxXOrientation = swipeArray[48];
        this.avgXOrientation = swipeArray[49];
        this.stdXOrientation = swipeArray[50];
        this.varXOrientation = swipeArray[51];
        this.minYOrientation = swipeArray[52];
        this.maxYOrientation = swipeArray[53];
        this.avgYOrientation = swipeArray[54];
        this.stdYOrientation = swipeArray[55];
        this.varYOrientation = swipeArray[56];
        this.minZOrientation = swipeArray[57];
        this.maxZOrientation = swipeArray[58];
        this.avgZOrientation = swipeArray[59];
        this.stdZOrientation = swipeArray[60];
        this.varZOrientation = swipeArray[61];
        this.holdingPosition = holdingPosition;
        this.userId = userId;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getAvgSize() {
        return avgSize;
    }

    public void setAvgSize(double avgSize) {
        this.avgSize = avgSize;
    }

    public double getDownSize() {
        return downSize;
    }

    public void setDownSize(double downSize) {
        this.downSize = downSize;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getMinXVelocity() {
        return minXVelocity;
    }

    public void setMinXVelocity(double minXVelocity) {
        this.minXVelocity = minXVelocity;
    }

    public double getMaxXVelocity() {
        return maxXVelocity;
    }

    public void setMaxXVelocity(double maxXVelocity) {
        this.maxXVelocity = maxXVelocity;
    }

    public double getAvgXVelocity() {
        return avgXVelocity;
    }

    public void setAvgXVelocity(double avgXVelocity) {
        this.avgXVelocity = avgXVelocity;
    }

    public double getStdXVelocity() {
        return stdXVelocity;
    }

    public void setStdXVelocity(double stdXVelocity) {
        this.stdXVelocity = stdXVelocity;
    }

    public double getVarXVelocity() {
        return varXVelocity;
    }

    public void setVarXVelocity(double varXVelocity) {
        this.varXVelocity = varXVelocity;
    }

    public double getMinYVelocity() {
        return minYVelocity;
    }

    public void setMinYVelocity(double minYVelocity) {
        this.minYVelocity = minYVelocity;
    }

    public double getMaxYVelocity() {
        return maxYVelocity;
    }

    public void setMaxYVelocity(double maxYVelocity) {
        this.maxYVelocity = maxYVelocity;
    }

    public double getAvgYVelocity() {
        return avgYVelocity;
    }

    public void setAvgYVelocity(double avgYVelocity) {
        this.avgYVelocity = avgYVelocity;
    }

    public double getStdYVelocity() {
        return stdYVelocity;
    }

    public void setStdYVelocity(double stdYVelocity) {
        this.stdYVelocity = stdYVelocity;
    }

    public double getVarYVelocity() {
        return varYVelocity;
    }

    public void setVarYVelocity(double varYVelocity) {
        this.varYVelocity = varYVelocity;
    }

    public double getMinXAccelerometer() {
        return minXAccelerometer;
    }

    public void setMinXAccelerometer(double minXAccelerometer) {
        this.minXAccelerometer = minXAccelerometer;
    }

    public double getMaxXAccelerometer() {
        return maxXAccelerometer;
    }

    public void setMaxXAccelerometer(double maxXAccelerometer) {
        this.maxXAccelerometer = maxXAccelerometer;
    }

    public double getAvgXAccelerometer() {
        return avgXAccelerometer;
    }

    public void setAvgXAccelerometer(double avgXAccelerometer) {
        this.avgXAccelerometer = avgXAccelerometer;
    }

    public double getStdXAccelerometer() {
        return stdXAccelerometer;
    }

    public void setStdXAccelerometer(double stdXAccelerometer) {
        this.stdXAccelerometer = stdXAccelerometer;
    }

    public double getVarXAccelerometer() {
        return varXAccelerometer;
    }

    public void setVarXAccelerometer(double varXAccelerometer) {
        this.varXAccelerometer = varXAccelerometer;
    }

    public double getMinYAccelerometer() {
        return minYAccelerometer;
    }

    public void setMinYAccelerometer(double minYAccelerometer) {
        this.minYAccelerometer = minYAccelerometer;
    }

    public double getMaxYAccelerometer() {
        return maxYAccelerometer;
    }

    public void setMaxYAccelerometer(double maxYAccelerometer) {
        this.maxYAccelerometer = maxYAccelerometer;
    }

    public double getAvgYAccelerometer() {
        return avgYAccelerometer;
    }

    public void setAvgYAccelerometer(double avgYAccelerometer) {
        this.avgYAccelerometer = avgYAccelerometer;
    }

    public double getStdYAccelerometer() {
        return stdYAccelerometer;
    }

    public void setStdYAccelerometer(double stdYAccelerometer) {
        this.stdYAccelerometer = stdYAccelerometer;
    }

    public double getVarYAccelerometer() {
        return varYAccelerometer;
    }

    public void setVarYAccelerometer(double varYAccelerometer) {
        this.varYAccelerometer = varYAccelerometer;
    }

    public double getMinZAccelerometer() {
        return minZAccelerometer;
    }

    public void setMinZAccelerometer(double minZAccelerometer) {
        this.minZAccelerometer = minZAccelerometer;
    }

    public double getMaxZAccelerometer() {
        return maxZAccelerometer;
    }

    public void setMaxZAccelerometer(double maxZAccelerometer) {
        this.maxZAccelerometer = maxZAccelerometer;
    }

    public double getAvgZAccelerometer() {
        return avgZAccelerometer;
    }

    public void setAvgZAccelerometer(double avgZAccelerometer) {
        this.avgZAccelerometer = avgZAccelerometer;
    }

    public double getStdZAccelerometer() {
        return stdZAccelerometer;
    }

    public void setStdZAccelerometer(double stdZAccelerometer) {
        this.stdZAccelerometer = stdZAccelerometer;
    }

    public double getVarZAccelerometer() {
        return varZAccelerometer;
    }

    public void setVarZAccelerometer(double varZAccelerometer) {
        this.varZAccelerometer = varZAccelerometer;
    }

    public double getMinXGyroscope() {
        return minXGyroscope;
    }

    public void setMinXGyroscope(double minXGyroscope) {
        this.minXGyroscope = minXGyroscope;
    }

    public double getMaxXGyroscope() {
        return maxXGyroscope;
    }

    public void setMaxXGyroscope(double maxXGyroscope) {
        this.maxXGyroscope = maxXGyroscope;
    }

    public double getAvgXGyroscope() {
        return avgXGyroscope;
    }

    public void setAvgXGyroscope(double avgXGyroscope) {
        this.avgXGyroscope = avgXGyroscope;
    }

    public double getStdXGyroscope() {
        return stdXGyroscope;
    }

    public void setStdXGyroscope(double stdXGyroscope) {
        this.stdXGyroscope = stdXGyroscope;
    }

    public double getVarXGyroscope() {
        return varXGyroscope;
    }

    public void setVarXGyroscope(double varXGyroscope) {
        this.varXGyroscope = varXGyroscope;
    }

    public double getMinYGyroscope() {
        return minYGyroscope;
    }

    public void setMinYGyroscope(double minYGyroscope) {
        this.minYGyroscope = minYGyroscope;
    }

    public double getMaxYGyroscope() {
        return maxYGyroscope;
    }

    public void setMaxYGyroscope(double maxYGyroscope) {
        this.maxYGyroscope = maxYGyroscope;
    }

    public double getAvgYGyroscope() {
        return avgYGyroscope;
    }

    public void setAvgYGyroscope(double avgYGyroscope) {
        this.avgYGyroscope = avgYGyroscope;
    }

    public double getStdYGyroscope() {
        return stdYGyroscope;
    }

    public void setStdYGyroscope(double stdYGyroscope) {
        this.stdYGyroscope = stdYGyroscope;
    }

    public double getVarYGyroscope() {
        return varYGyroscope;
    }

    public void setVarYGyroscope(double varYGyroscope) {
        this.varYGyroscope = varYGyroscope;
    }

    public double getMinZGyroscope() {
        return minZGyroscope;
    }

    public void setMinZGyroscope(double minZGyroscope) {
        this.minZGyroscope = minZGyroscope;
    }

    public double getMaxZGyroscope() {
        return maxZGyroscope;
    }

    public void setMaxZGyroscope(double maxZGyroscope) {
        this.maxZGyroscope = maxZGyroscope;
    }

    public double getAvgZGyroscope() {
        return avgZGyroscope;
    }

    public void setAvgZGyroscope(double avgZGyroscope) {
        this.avgZGyroscope = avgZGyroscope;
    }

    public double getStdZGyroscope() {
        return stdZGyroscope;
    }

    public void setStdZGyroscope(double stdZGyroscope) {
        this.stdZGyroscope = stdZGyroscope;
    }

    public double getVarZGyroscope() {
        return varZGyroscope;
    }

    public void setVarZGyroscope(double varZGyroscope) {
        this.varZGyroscope = varZGyroscope;
    }

    public double getMinXOrientation() {
        return minXOrientation;
    }

    public void setMinXOrientation(double minXOrientation) {
        this.minXOrientation = minXOrientation;
    }

    public double getMaxXOrientation() {
        return maxXOrientation;
    }

    public void setMaxXOrientation(double maxXOrientation) {
        this.maxXOrientation = maxXOrientation;
    }

    public double getAvgXOrientation() {
        return avgXOrientation;
    }

    public void setAvgXOrientation(double avgXOrientation) {
        this.avgXOrientation = avgXOrientation;
    }

    public double getStdXOrientation() {
        return stdXOrientation;
    }

    public void setStdXOrientation(double stdXOrientation) {
        this.stdXOrientation = stdXOrientation;
    }

    public double getVarXOrientation() {
        return varXOrientation;
    }

    public void setVarXOrientation(double varXOrientation) {
        this.varXOrientation = varXOrientation;
    }

    public double getMinYOrientation() {
        return minYOrientation;
    }

    public void setMinYOrientation(double minYOrientation) {
        this.minYOrientation = minYOrientation;
    }

    public double getMaxYOrientation() {
        return maxYOrientation;
    }

    public void setMaxYOrientation(double maxYOrientation) {
        this.maxYOrientation = maxYOrientation;
    }

    public double getAvgYOrientation() {
        return avgYOrientation;
    }

    public void setAvgYOrientation(double avgYOrientation) {
        this.avgYOrientation = avgYOrientation;
    }

    public double getStdYOrientation() {
        return stdYOrientation;
    }

    public void setStdYOrientation(double stdYOrientation) {
        this.stdYOrientation = stdYOrientation;
    }

    public double getVarYOrientation() {
        return varYOrientation;
    }

    public void setVarYOrientation(double varYOrientation) {
        this.varYOrientation = varYOrientation;
    }

    public double getMinZOrientation() {
        return minZOrientation;
    }

    public void setMinZOrientation(double minZOrientation) {
        this.minZOrientation = minZOrientation;
    }

    public double getMaxZOrientation() {
        return maxZOrientation;
    }

    public void setMaxZOrientation(double maxZOrientation) {
        this.maxZOrientation = maxZOrientation;
    }

    public double getAvgZOrientation() {
        return avgZOrientation;
    }

    public void setAvgZOrientation(double avgZOrientation) {
        this.avgZOrientation = avgZOrientation;
    }

    public double getStdZOrientation() {
        return stdZOrientation;
    }

    public void setStdZOrientation(double stdZOrientation) {
        this.stdZOrientation = stdZOrientation;
    }

    public double getVarZOrientation() {
        return varZOrientation;
    }

    public void setVarZOrientation(double varZOrientation) {
        this.varZOrientation = varZOrientation;
    }

    public double getHoldingPosition() {
        return holdingPosition;
    }

    public void setHoldingPosition(double holdingPosition) {
        this.holdingPosition = holdingPosition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public double[] getNormalizedValues() {

        return new double[] {(this.duration - MIN_DURATION) / (MAX_DURATION - MIN_DURATION),
                this.avgSize,
                this.downSize,
                (this.startX - MIN_START_X) / (MAX_START_X - MIN_START_X),
                (this.startY - MIN_START_Y) / (MAX_START_Y - MIN_START_Y),
                (this.endX - MIN_END_X) / (MAX_END_X - MIN_END_X),
                (this.endY - MIN_END_Y) / (MAX_END_Y - MIN_END_Y),
                (this.minXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.maxXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.avgXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.stdXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.varXVelocity - MIN_X_VELOCITY) / Math.pow(MAX_X_VELOCITY - MIN_X_VELOCITY, 2),
                (this.minYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.maxYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.avgYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.stdYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.varYVelocity - MIN_Y_VELOCITY) / Math.pow(MAX_Y_VELOCITY - MIN_Y_VELOCITY, 2),
                (this.minXAccelerometer - MIN_X_ACCELEROMETER) / (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER),
                (this.maxXAccelerometer - MIN_X_ACCELEROMETER) / (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER),
                (this.avgXAccelerometer - MIN_X_ACCELEROMETER) / (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER),
                (this.stdXAccelerometer - MIN_X_ACCELEROMETER) / (MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER),
                (this.varXAccelerometer - MIN_X_ACCELEROMETER) / Math.pow(MAX_X_ACCELEROMETER - MIN_X_ACCELEROMETER, 2),
                (this.minYAccelerometer - MIN_Y_ACCELEROMETER) / (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER),
                (this.maxYAccelerometer - MIN_Y_ACCELEROMETER) / (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER),
                (this.avgYAccelerometer - MIN_Y_ACCELEROMETER) / (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER),
                (this.stdYAccelerometer - MIN_Y_ACCELEROMETER) / (MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER),
                (this.varYAccelerometer - MIN_Y_ACCELEROMETER) / Math.pow(MAX_Y_ACCELEROMETER - MIN_Y_ACCELEROMETER, 2),
                (this.minZAccelerometer - MIN_Z_ACCELEROMETER) / (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER),
                (this.maxZAccelerometer - MIN_Z_ACCELEROMETER) / (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER),
                (this.avgZAccelerometer - MIN_Z_ACCELEROMETER) / (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER),
                (this.stdZAccelerometer - MIN_Z_ACCELEROMETER) / (MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER),
                (this.varZAccelerometer - MIN_Z_ACCELEROMETER) / Math.pow(MAX_Z_ACCELEROMETER - MIN_Z_ACCELEROMETER, 2),
                (this.minXGyroscope - MIN_X_GYROSCOPE) / (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE),
                (this.maxXGyroscope - MIN_X_GYROSCOPE) / (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE),
                (this.avgXGyroscope - MIN_X_GYROSCOPE) / (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE),
                (this.stdXGyroscope - MIN_X_GYROSCOPE) / (MAX_X_GYROSCOPE - MIN_X_GYROSCOPE),
                (this.varXGyroscope - MIN_X_GYROSCOPE) / Math.pow(MAX_X_GYROSCOPE - MIN_X_GYROSCOPE, 2),
                (this.minYGyroscope - MIN_Y_GYROSCOPE) / (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE),
                (this.maxYGyroscope - MIN_Y_GYROSCOPE) / (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE),
                (this.avgYGyroscope - MIN_Y_GYROSCOPE) / (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE),
                (this.stdYGyroscope - MIN_Y_GYROSCOPE) / (MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE),
                (this.varYGyroscope - MIN_Y_GYROSCOPE) / Math.pow(MAX_Y_GYROSCOPE - MIN_Y_GYROSCOPE, 2),
                (this.minZGyroscope - MIN_Z_GYROSCOPE) / (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE),
                (this.maxZGyroscope - MIN_Z_GYROSCOPE) / (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE),
                (this.avgZGyroscope - MIN_Z_GYROSCOPE) / (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE),
                (this.stdZGyroscope - MIN_Z_GYROSCOPE) / (MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE),
                (this.varZGyroscope - MIN_Z_GYROSCOPE) / Math.pow(MAX_Z_GYROSCOPE - MIN_Z_GYROSCOPE, 2),
                (this.minXOrientation - MIN_X_ORIENTATION) / (MAX_X_ORIENTATION - MIN_X_ORIENTATION),
                (this.maxXOrientation - MIN_X_ORIENTATION) / (MAX_X_ORIENTATION - MIN_X_ORIENTATION),
                (this.avgXOrientation - MIN_X_ORIENTATION) / (MAX_X_ORIENTATION - MIN_X_ORIENTATION),
                (this.stdXOrientation - MIN_X_ORIENTATION) / (MAX_X_ORIENTATION - MIN_X_ORIENTATION),
                (this.varXOrientation - MIN_X_ORIENTATION) / Math.pow(MAX_X_ORIENTATION - MIN_X_ORIENTATION, 2),
                (this.minYOrientation - MIN_Y_ORIENTATION) / (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION),
                (this.maxYOrientation - MIN_Y_ORIENTATION) / (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION),
                (this.avgYOrientation - MIN_Y_ORIENTATION) / (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION),
                (this.stdYOrientation - MIN_Y_ORIENTATION) / (MAX_Y_ORIENTATION - MIN_Y_ORIENTATION),
                (this.varYOrientation - MIN_Y_ORIENTATION) / Math.pow(MAX_Y_ORIENTATION - MIN_Y_ORIENTATION, 2),
                (this.minZOrientation - MIN_Z_ORIENTATION) / (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION),
                (this.maxZOrientation - MIN_Z_ORIENTATION) / (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION),
                (this.avgZOrientation - MIN_Z_ORIENTATION) / (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION),
                (this.stdZOrientation - MIN_Z_ORIENTATION) / (MAX_Z_ORIENTATION - MIN_Z_ORIENTATION),
                (this.varZOrientation - MIN_Z_ORIENTATION) / Math.pow(MAX_Z_ORIENTATION - MIN_Z_ORIENTATION, 2)};
    }

    public double[] toArray() {
        return new double[] {this.duration,
                this.avgSize,
                this.downSize,
                this.startX,
                this.startY,
                this.endX,
                this.endY,
                this.minXVelocity,
                this.maxXVelocity,
                this.avgXVelocity,
                this.stdXVelocity,
                this.varXVelocity,
                this.minYVelocity,
                this.maxYVelocity,
                this.avgYVelocity,
                this.stdYVelocity,
                this.varYVelocity,
                this.minXAccelerometer,
                this.maxXAccelerometer,
                this.avgXAccelerometer,
                this.stdXAccelerometer,
                this.varXAccelerometer,
                this.minYAccelerometer,
                this.maxYAccelerometer,
                this.avgYAccelerometer,
                this.stdYAccelerometer,
                this.varYAccelerometer,
                this.minZAccelerometer,
                this.maxZAccelerometer,
                this.avgZAccelerometer,
                this.stdZAccelerometer,
                this.varZAccelerometer,
                this.minXGyroscope,
                this.maxXGyroscope,
                this.avgXGyroscope,
                this.stdXGyroscope,
                this.varXGyroscope,
                this.minYGyroscope,
                this.maxYGyroscope,
                this.avgYGyroscope,
                this.stdYGyroscope,
                this.varYGyroscope,
                this.minZGyroscope,
                this.maxZGyroscope,
                this.avgZGyroscope,
                this.stdZGyroscope,
                this.varZGyroscope,
                this.minXOrientation,
                this.maxXOrientation,
                this.avgXOrientation,
                this.stdXOrientation,
                this.varXOrientation,
                this.minYOrientation,
                this.maxYOrientation,
                this.avgYOrientation,
                this.stdYOrientation,
                this.varYOrientation,
                this.minZOrientation,
                this.maxZOrientation,
                this.avgZOrientation,
                this.stdZOrientation,
                this.varZOrientation};
    }

    @Override
    public String toString() {
        return "Swipe{" +
                "duration=" + duration +
                "\n avgSize=" + avgSize +
                "\n downSize=" + downSize +
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
                "\n holdingPosition=" + holdingPosition +
                "\n userId=" + userId +
                '}';
    }

    public Instance getAsWekaInstance(Instances dataSet, boolean isTrainInstance, DatabaseHelper dbHelper){
        ArrayList<Integer> featureData = dbHelper.getFeatureData();
        boolean useAcceleration = featureData.get(0) == 1;
        boolean useAngularVelocity = featureData.get(1) == 1;
        boolean useOrientation = featureData.get(2) == 1;
        boolean useSwipeDuration = featureData.get(3) == 1;
        boolean useSwipeStartEndPos = featureData.get(4) == 1;
        boolean useSwipeVelocity = featureData.get(5) == 1;

        ArrayList<Double> featureSet = new ArrayList<>();
        if(useSwipeDuration) {
            featureSet.add(this.getDuration());
            featureSet.add(this.getAvgSize());
            featureSet.add(this.getDownSize());
        }
        if(useSwipeStartEndPos) {
            featureSet.add(this.getStartX());
            featureSet.add(this.getStartY());
            featureSet.add(this.getEndX());
            featureSet.add(this.getEndY());
        }
        if(useSwipeVelocity) {
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
        if(useAcceleration) {
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
        if(useAngularVelocity) {
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
        if(useOrientation){
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