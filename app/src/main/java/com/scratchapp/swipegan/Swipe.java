package com.scratchapp.swipegan;

import android.content.res.Resources;

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

    private static final double MIN_X_VELOCITY = -50_000;
    private static final double MAX_X_VELOCITY = 50_000;
    private static final double MIN_Y_VELOCITY = -50_000;
    private static final double MAX_Y_VELOCITY = 50_000;

    private static final double MIN_X_ACCELERATION = -10;
    private static final double MAX_X_ACCELERATION = 10;
    private static final double MIN_Y_ACCELERATION = -10;
    private static final double MAX_Y_ACCELERATION = 10;

    private double duration;
    private double avgSize;
    private double downSize;
    private double downPressure;
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
    private double minPressure;
    private double maxPressure;
    private double avgPressure;
    private double varPressure;
    private double stdPressure;
    private double minXAcceleration;
    private double maxXAcceleration;
    private double avgXAcceleration;
    private double varXAcceleration;
    private double stdXAcceleration;
    private double minYAcceleration;
    private double maxYAcceleration;
    private double avgYAcceleration;
    private double varYAcceleration;
    private double stdYAcceleration;

    public static Swipe fromNormalizedValues(double[] values) {

        Swipe swipe = new Swipe();
        swipe.setDuration(values[0] * (MAX_DURATION - MIN_DURATION) + MIN_DURATION);
        swipe.setAvgSize(values[1]);
        swipe.setDownSize(values[2]);
        swipe.setDownPressure(values[3]);
        swipe.setStartX(values[4] * (MAX_START_X - MIN_START_X) + MIN_START_X);
        swipe.setStartY(values[5] * (MAX_START_Y - MIN_START_Y) + MIN_START_Y);
        swipe.setEndX(values[6] * (MAX_END_X - MIN_END_X) + MIN_END_X);
        swipe.setEndY(values[7] * (MAX_END_Y - MIN_END_Y) + MIN_END_Y);

        swipe.setMinXVelocity(values[8] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setMaxXVelocity(values[9] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setAvgXVelocity(values[10] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);
        swipe.setVarXVelocity(values[11] * Math.pow(MAX_X_VELOCITY - MIN_X_VELOCITY, 2) + MIN_X_VELOCITY);
        swipe.setStdXVelocity(values[12] * (MAX_X_VELOCITY - MIN_X_VELOCITY) + MIN_X_VELOCITY);

        swipe.setMinYVelocity(values[13] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setMaxYVelocity(values[14] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setAvgYVelocity(values[15] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);
        swipe.setVarYVelocity(values[16] * Math.pow(MAX_Y_VELOCITY - MIN_Y_VELOCITY, 2) + MIN_Y_VELOCITY);
        swipe.setStdYVelocity(values[17] * (MAX_Y_VELOCITY - MIN_Y_VELOCITY) + MIN_Y_VELOCITY);

        swipe.setMinPressure(values[18]);
        swipe.setMaxPressure(values[19]);
        swipe.setAvgPressure(values[20]);
        swipe.setVarPressure(values[21]);
        swipe.setStdPressure(values[22]);

        swipe.setMinXAcceleration(values[23] * (MAX_X_ACCELERATION - MIN_X_ACCELERATION) + MIN_X_ACCELERATION);
        swipe.setMaxXAcceleration(values[24] * (MAX_X_ACCELERATION - MIN_X_ACCELERATION) + MIN_X_ACCELERATION);
        swipe.setAvgXAcceleration(values[25] * (MAX_X_ACCELERATION - MIN_X_ACCELERATION) + MIN_X_ACCELERATION);
        swipe.setVarXAcceleration(values[26] * Math.pow(MAX_X_ACCELERATION - MIN_X_ACCELERATION, 2) + MIN_X_ACCELERATION);
        swipe.setStdXAcceleration(values[27] * (MAX_X_ACCELERATION - MIN_X_ACCELERATION) + MIN_X_ACCELERATION);

        swipe.setMinYAcceleration(values[28] * (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION) + MIN_Y_ACCELERATION);
        swipe.setMaxYAcceleration(values[29] * (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION) + MIN_Y_ACCELERATION);
        swipe.setAvgYAcceleration(values[30] * (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION) + MIN_Y_ACCELERATION);
        swipe.setVarYAcceleration(values[31] * Math.pow(MAX_Y_ACCELERATION - MIN_Y_ACCELERATION, 2) + MIN_Y_ACCELERATION);
        swipe.setStdYAcceleration(values[32] * (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION) + MIN_Y_ACCELERATION);

        return swipe;
    }

    public Swipe() {

    }

    public Swipe(double[] swipeArray) {
        this.duration = swipeArray[0];
        this.avgSize = swipeArray[1];
        this.downSize = swipeArray[2];
        this.downPressure = swipeArray[3];
        this.startX = swipeArray[4];
        this.startY = swipeArray[5];
        this.endX = swipeArray[6];
        this.endY = swipeArray[7];
        this.minXVelocity = swipeArray[8];
        this.maxXVelocity = swipeArray[9];
        this.avgXVelocity = swipeArray[10];
        this.varXVelocity = swipeArray[11];
        this.stdXVelocity = swipeArray[12];
        this.minYVelocity = swipeArray[13];
        this.maxYVelocity = swipeArray[14];
        this.avgYVelocity = swipeArray[15];
        this.varYVelocity = swipeArray[16];
        this.stdYVelocity = swipeArray[17];
        this.minPressure = swipeArray[18];
        this.maxPressure = swipeArray[19];
        this.avgPressure = swipeArray[20];
        this.varPressure = swipeArray[21];
        this.stdPressure = swipeArray[22];
        this.minXAcceleration = swipeArray[23];
        this.maxXAcceleration = swipeArray[24];
        this.avgXAcceleration = swipeArray[25];
        this.varXAcceleration = swipeArray[26];
        this.stdXAcceleration = swipeArray[27];
        this.minYAcceleration = swipeArray[28];
        this.maxYAcceleration = swipeArray[29];
        this.avgYAcceleration = swipeArray[30];
        this.varYAcceleration = swipeArray[31];
        this.stdYAcceleration = swipeArray[32];
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

    public double getDownPressure() {
        return downPressure;
    }

    public void setDownPressure(double downPressure) {
        this.downPressure = downPressure;
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

    public double getVarXVelocity() {
        return varXVelocity;
    }

    public void setVarXVelocity(double varXVelocity) {
        this.varXVelocity = varXVelocity;
    }

    public double getStdXVelocity() {
        return stdXVelocity;
    }

    public void setStdXVelocity(double stdXVelocity) {
        this.stdXVelocity = stdXVelocity;
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

    public double getVarYVelocity() {
        return varYVelocity;
    }

    public void setVarYVelocity(double varYVelocity) {
        this.varYVelocity = varYVelocity;
    }

    public double getStdYVelocity() {
        return stdYVelocity;
    }

    public void setStdYVelocity(double stdYVelocity) {
        this.stdYVelocity = stdYVelocity;
    }

    public double getMinPressure() {
        return minPressure;
    }

    public void setMinPressure(double minPressure) {
        this.minPressure = minPressure;
    }

    public double getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(double maxPressure) {
        this.maxPressure = maxPressure;
    }

    public double getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(double avgPressure) {
        this.avgPressure = avgPressure;
    }

    public double getVarPressure() {
        return varPressure;
    }

    public void setVarPressure(double varPressure) {
        this.varPressure = varPressure;
    }

    public double getStdPressure() {
        return stdPressure;
    }

    public void setStdPressure(double stdPressure) {
        this.stdPressure = stdPressure;
    }

    public double getMinXAcceleration() {
        return minXAcceleration;
    }

    public void setMinXAcceleration(double minXAcceleration) {
        this.minXAcceleration = minXAcceleration;
    }

    public double getMaxXAcceleration() {
        return maxXAcceleration;
    }

    public void setMaxXAcceleration(double maxXAcceleration) {
        this.maxXAcceleration = maxXAcceleration;
    }

    public double getAvgXAcceleration() {
        return avgXAcceleration;
    }

    public void setAvgXAcceleration(double avgXAcceleration) {
        this.avgXAcceleration = avgXAcceleration;
    }

    public double getVarXAcceleration() {
        return varXAcceleration;
    }

    public void setVarXAcceleration(double varXAcceleration) {
        this.varXAcceleration = varXAcceleration;
    }

    public double getStdXAcceleration() {
        return stdXAcceleration;
    }

    public void setStdXAcceleration(double stdXAcceleration) {
        this.stdXAcceleration = stdXAcceleration;
    }

    public double getMinYAcceleration() {
        return minYAcceleration;
    }

    public void setMinYAcceleration(double minYAcceleration) {
        this.minYAcceleration = minYAcceleration;
    }

    public double getMaxYAcceleration() {
        return maxYAcceleration;
    }

    public void setMaxYAcceleration(double maxYAcceleration) {
        this.maxYAcceleration = maxYAcceleration;
    }

    public double getAvgYAcceleration() {
        return avgYAcceleration;
    }

    public void setAvgYAcceleration(double avgYAcceleration) {
        this.avgYAcceleration = avgYAcceleration;
    }

    public double getVarYAcceleration() {
        return varYAcceleration;
    }

    public void setVarYAcceleration(double varYAcceleration) {
        this.varYAcceleration = varYAcceleration;
    }

    public double getStdYAcceleration() {
        return stdYAcceleration;
    }

    public void setStdYAcceleration(double stdYAcceleration) {
        this.stdYAcceleration = stdYAcceleration;
    }

    public double[] getNormalizedValues() {

        return new double[] {(this.duration - MIN_DURATION) / (MAX_DURATION - MIN_DURATION),
                this.avgSize,
                this.downSize,
                this.downPressure,
                (this.startX - MIN_START_X) / (MAX_START_X - MIN_START_X),
                (this.startY - MIN_START_Y) / (MAX_START_Y - MIN_START_Y),
                (this.endX - MIN_END_X) / (MAX_END_X - MIN_END_X),
                (this.endY - MIN_END_Y) / (MAX_END_Y - MIN_END_Y),
                (this.minXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.maxXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.avgXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.varXVelocity - MIN_X_VELOCITY) / Math.pow(MAX_X_VELOCITY - MIN_X_VELOCITY, 2),
                (this.stdXVelocity - MIN_X_VELOCITY) / (MAX_X_VELOCITY - MIN_X_VELOCITY),
                (this.minYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.maxYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.avgYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                (this.varYVelocity - MIN_Y_VELOCITY) / Math.pow(MAX_Y_VELOCITY - MIN_Y_VELOCITY, 2),
                (this.stdYVelocity - MIN_Y_VELOCITY) / (MAX_Y_VELOCITY - MIN_Y_VELOCITY),
                this.minPressure,
                this.maxPressure,
                this.avgPressure,
                this.varPressure,
                this.stdPressure,
                (this.minXAcceleration - MIN_X_ACCELERATION) / (MAX_X_ACCELERATION - MIN_X_ACCELERATION),
                (this.maxXAcceleration - MIN_X_ACCELERATION) / (MAX_X_ACCELERATION - MIN_X_ACCELERATION),
                (this.avgXAcceleration - MIN_X_ACCELERATION) / (MAX_X_ACCELERATION - MIN_X_ACCELERATION),
                (this.varXAcceleration - MIN_X_ACCELERATION) / Math.pow(MAX_X_ACCELERATION - MIN_X_ACCELERATION, 2),
                (this.stdXAcceleration - MIN_X_ACCELERATION) / (MAX_X_ACCELERATION - MIN_X_ACCELERATION),
                (this.minYAcceleration - MIN_Y_ACCELERATION) / (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION),
                (this.maxYAcceleration - MIN_Y_ACCELERATION) / (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION),
                (this.avgYAcceleration - MIN_Y_ACCELERATION) / (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION),
                (this.varYAcceleration - MIN_Y_ACCELERATION) / Math.pow(MAX_Y_ACCELERATION - MIN_Y_ACCELERATION, 2),
                (this.stdYAcceleration - MIN_Y_ACCELERATION) / (MAX_Y_ACCELERATION - MIN_Y_ACCELERATION)};
    }

    public double[] toArray() {
        return new double[] {this.duration,
                this.avgSize,
                this.downSize,
                this.downPressure,
                this.startX,
                this.startY,
                this.endX,
                this.endY,
                this.minXVelocity,
                this.maxXVelocity,
                this.avgXVelocity,
                this.varXVelocity,
                this.stdXVelocity,
                this.minYVelocity,
                this.maxYVelocity,
                this.avgYVelocity,
                this.varYVelocity,
                this.stdYVelocity,
                this.minPressure,
                this.maxPressure,
                this.avgPressure,
                this.varPressure,
                this.stdPressure,
                this.minXAcceleration,
                this.maxXAcceleration,
                this.avgXAcceleration,
                this.varXAcceleration,
                this.stdXAcceleration,
                this.minYAcceleration,
                this.maxYAcceleration,
                this.avgYAcceleration,
                this.varYAcceleration,
                this.stdYAcceleration};
    }

    @Override
    public String toString() {
        return "Swipe{" +
                "duration=" + duration +
                "\n avgSize=" + avgSize +
                "\n downSize=" + downSize +
                "\n downPressure=" + downPressure +
                "\n startX=" + startX +
                "\n startY=" + startY +
                "\n endX=" + endX +
                "\n endY=" + endY +
                "\n minXVelocity=" + minXVelocity +
                "\n maxXVelocity=" + maxXVelocity +
                "\n avgXVelocity=" + avgXVelocity +
                "\n varXVelocity=" + varXVelocity +
                "\n stdXVelocity=" + stdXVelocity +
                "\n minYVelocity=" + minYVelocity +
                "\n maxYVelocity=" + maxYVelocity +
                "\n avgYVelocity=" + avgYVelocity +
                "\n varYVelocity=" + varYVelocity +
                "\n stdYVelocity=" + stdYVelocity +
                "\n minPressure=" + minPressure +
                "\n maxPressure=" + maxPressure +
                "\n avgPressure=" + avgPressure +
                "\n varPressure=" + varPressure +
                "\n stdPressure=" + stdPressure +
                "\n minXAcceleration=" + minXAcceleration +
                "\n maxXAcceleration=" + maxXAcceleration +
                "\n avgXAcceleration=" + avgXAcceleration +
                "\n varXAcceleration=" + varXAcceleration +
                "\n stdXAcceleration=" + stdXAcceleration +
                "\n minYAcceleration=" + minYAcceleration +
                "\n maxYAcceleration=" + maxYAcceleration +
                "\n avgYAcceleration=" + avgYAcceleration +
                "\n varYAcceleration=" + varYAcceleration +
                "\n stdYAcceleration=" + stdYAcceleration +
                '}';
    }

    public Instance getAsWekaInstance(Instances dataSet, boolean isTrainInstance){
        Instance instance = new DenseInstance(34);
        instance.setDataset(dataSet);
        instance.setValue(0, this.getDuration());
        instance.setValue(1, this.getAvgSize());
        instance.setValue(2, this.getDownSize());
        instance.setValue(3, this.getDownPressure());
        instance.setValue(4, this.getStartX());
        instance.setValue(5, this.getStartY());
        instance.setValue(6, this.getEndX());
        instance.setValue(7, this.getEndY());
        instance.setValue(8, this.getMinXVelocity());
        instance.setValue(9, this.getMaxXVelocity());
        instance.setValue(10, this.getAvgXVelocity());
        instance.setValue(11, this.getVarXVelocity());
        instance.setValue(12, this.getStdXVelocity());
        instance.setValue(13, this.getMinYVelocity());
        instance.setValue(14, this.getMaxYVelocity());
        instance.setValue(15, this.getAvgYVelocity());
        instance.setValue(16, this.getVarYVelocity());
        instance.setValue(17, this.getStdYVelocity());
        instance.setValue(18, this.getMinPressure());
        instance.setValue(19, this.getMaxPressure());
        instance.setValue(20, this.getAvgPressure());
        instance.setValue(21, this.getVarPressure());
        instance.setValue(22, this.getStdPressure());
        instance.setValue(23, this.getMinXAcceleration());
        instance.setValue(24, this.getMaxXAcceleration());
        instance.setValue(25, this.getAvgXAcceleration());
        instance.setValue(26, this.getVarXAcceleration());
        instance.setValue(27, this.getStdXAcceleration());
        instance.setValue(28, this.getMinYAcceleration());
        instance.setValue(29, this.getMaxYAcceleration());
        instance.setValue(30, this.getAvgYAcceleration());
        instance.setValue(31, this.getVarYAcceleration());
        instance.setValue(32, this.getStdYAcceleration());
        if(isTrainInstance){
            instance.setClassValue("User");
        }
        return instance;
    }
}