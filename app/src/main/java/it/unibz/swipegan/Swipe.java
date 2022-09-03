package it.unibz.swipegan;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Swipe {
    private double duration;
    private double length;
    private double[] segmentsX;
    private double[] segmentsY;
    private double minSize;
    private double maxSize;
    private double avgSize;
    private double downSize;
    private double upSize;
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

    //Keystroke features
    private double[] keystrokeDurations;
    private double[] keystrokeIntervals;

    private double holdingPosition;
    private String userId;

    // Test swipe features
    private double authenticationHold = 0.0;
    private double authenticationSwipe = 0.0;
    private double authenticationFull = 0.0;

    private double authenticationTimeHold = 0.0;
    private double authenticationTimeSwipe = 0.0;
    private double authenticationTimeFull = 0.0;

    private int classifierSamples = 0;

    public Swipe() {

    }

    public Swipe(double[] swipeArray, double holdingPosition, String userId) {
        int array_idx = 0;

        for(String head_feature : DatabaseHelper.head_features) {
            java.lang.reflect.Method cur_method = null;
            try {
                cur_method = this.getClass().getMethod("set" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)), double.class);
                cur_method.invoke(this, swipeArray[array_idx]);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            array_idx = array_idx + 1;
        }

        for(String feature : DatabaseHelper.features) {
            for(String metric : DatabaseHelper.metrics) {
                for(String dimension : DatabaseHelper.dimensions) {
                    if (dimension == "Z" && feature == "Velocity") { continue; }

                    java.lang.reflect.Method cur_method = null;
                    try {
                        cur_method = this.getClass().getMethod("set" + metric + dimension + feature, double.class);
                        cur_method.invoke(this, swipeArray[array_idx]);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    array_idx = array_idx + 1;
                }
            }
        }

        this.holdingPosition = holdingPosition;
        this.userId = userId;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double[] getSegmentsX() {
        return segmentsX;
    }

    public void setSegmentsX(double[] segmentsX) {
        this.segmentsX = segmentsX;
    }

    public double[] getSegmentsY() {
        return segmentsY;
    }

    public void setSegmentsY(double[] segmentsY) {
        this.segmentsY = segmentsY;
    }

    public double getMinSize() {
        return minSize;
    }

    public void setMinSize(double minSize) {
        this.minSize = minSize;
    }

    public double getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
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

    public double getUpSize() {
        return upSize;
    }

    public void setUpSize(double upSize) {
        this.upSize = upSize;
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

    public double[] getKeystrokeDurations() {
        return this.keystrokeDurations;
    }

    public void addKeystrokeDuration(double duration, int curKeystroke, int pinLength) {
        if(this.keystrokeDurations == null) {
            this.keystrokeDurations = new double[pinLength];
        }

        this.keystrokeDurations[curKeystroke] = duration;
    }

    public void setKeystrokeDurations(double[] keystrokeDurations) {
        this.keystrokeDurations = keystrokeDurations;
    }

    public double[] getKeystrokeIntervals() {
        return this.keystrokeIntervals;
    }

    public void addKeystrokeInterval(double interval, int curKeystroke, int pinLength) {
        if(this.keystrokeIntervals == null) {
            this.keystrokeIntervals = new double[pinLength - 1];
        }

        this.keystrokeIntervals[curKeystroke] = interval;
    }

    public void setKeystrokeIntervals(double[] keystrokeIntervals) {
        this.keystrokeIntervals = keystrokeIntervals;
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

    public double getAuthentication(DatabaseHelper.ModelType modelType) {
        if (modelType == DatabaseHelper.ModelType.HOLD) {
            return authenticationHold;
        } else if(modelType == DatabaseHelper.ModelType.SWIPE) {
            return authenticationSwipe;
        } else {
            return authenticationFull;
        }
    }

    public void setAuthentication(double authentication, DatabaseHelper.ModelType modelType) {
        if (modelType == DatabaseHelper.ModelType.HOLD) {
            this.authenticationHold = authentication;
        } else if(modelType == DatabaseHelper.ModelType.SWIPE) {
            this.authenticationSwipe = authentication;
        } else {
            this.authenticationFull = authentication;
        }
    }

    public double getAuthenticationTime(DatabaseHelper.ModelType modelType) {
        if (modelType == DatabaseHelper.ModelType.HOLD) {
            return authenticationTimeHold;
        } else if(modelType == DatabaseHelper.ModelType.SWIPE) {
            return authenticationTimeSwipe;
        } else {
            return authenticationTimeFull;
        }
    }

    public void setAuthenticationTime(double authenticationTime, DatabaseHelper.ModelType modelType) {
        if (modelType == DatabaseHelper.ModelType.HOLD) {
            this.authenticationTimeHold = authenticationTime;
        } else if(modelType == DatabaseHelper.ModelType.SWIPE) {
            this.authenticationTimeSwipe = authenticationTime;
        } else {
            this.authenticationTimeFull = authenticationTime;
        }
    }

    public int getClassifierSamples() {
        return classifierSamples;
    }

    public void setClassifierSamples(int classifierSamples) {
        this.classifierSamples = classifierSamples;
    }

    // Gets the MIN/MAX values to use for normalization from the gathered training examples
    public static Map<String, Double> getMinMaxValues(ArrayList<Swipe> allSwipes) {
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
                    double[] keystroke_vals = (double[]) cur_method.invoke(swipe);

                    if(keystroke_vals != null) {
                        for (int i = 0; i < keystroke_vals.length; i++) {
                            map.put(min_key + "_" + i, map.get(min_key + "_" + i) == null || keystroke_vals[i] < map.get(min_key + "_" + i) ? keystroke_vals[i] : map.get(min_key + "_" + i));
                            map.put(max_key + "_" + i, map.get(max_key + "_" + i) == null || keystroke_vals[i] > map.get(max_key + "_" + i) ? keystroke_vals[i] : map.get(max_key + "_" + i));
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    public double[] getNormalizedValues(ArrayList<Swipe> allSwipes) {
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
                double[] keystroke_vals = (double[]) cur_method.invoke(this);

                if(keystroke_vals != null) {
                    for (int i = 0; i < keystroke_vals.length; i++) {
                        ret.add((keystroke_vals[i] - map.get(min_key + "_" + i)) / (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)));
                    }
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

    public static Swipe fromNormalizedValues(double[] values, double holdingPosition, String userId, ArrayList<Swipe> allSwipes) {
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
                            .filter(x -> x.getKey().contains(head_feature.toUpperCase()))
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

                if(keystroke_size != 0) {
                    double[] normalized_keystroke = new double[keystroke_size];
                    for (int i = 0; i < keystroke_size; i++) {
                        normalized_keystroke[i] = values[values_idx] * (map.get(max_key + "_" + i) - map.get(min_key + "_" + i)) + map.get(min_key + "_" + i);
                        values_idx = values_idx + 1;
                    }

                    cur_method = swipe.getClass().getMethod("set" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)), double[].class);
                    cur_method.invoke(swipe, normalized_keystroke);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        swipe.setHoldingPosition(holdingPosition);
        swipe.setUserId(userId);

        return swipe;
    }

    @Override
    public String toString() {
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
                "\n keystrokeDurations=" + keystrokeDurations +
                "\n keystrokeIntervals=" + keystrokeIntervals +
                "\n holdingPosition=" + holdingPosition +
                "\n userId=" + userId +
                '}';
    }

    public Instance getAsWekaInstance(Instances dataSet, boolean isTrainInstance, DatabaseHelper dbHelper, DatabaseHelper.ModelType modelType) {
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

        ArrayList<Double> featureSet = new ArrayList<>();

        if(modelType == DatabaseHelper.ModelType.SWIPE || modelType == DatabaseHelper.ModelType.FULL) {
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
        if(modelType == DatabaseHelper.ModelType.HOLD || modelType == DatabaseHelper.ModelType.FULL) {
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
        if(modelType == DatabaseHelper.ModelType.KEYSTROKE || modelType == DatabaseHelper.ModelType.FULL) {
            if(useKeystroke) {
                for(Double keystrokeDuration : this.getKeystrokeDurations()) { featureSet.add(keystrokeDuration); }
                for(Double keystrokeInterval : this.getKeystrokeIntervals()) { featureSet.add(keystrokeInterval); }
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