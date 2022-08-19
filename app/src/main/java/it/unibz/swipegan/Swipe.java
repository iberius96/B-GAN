package it.unibz.swipegan;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Swipe {
    private double duration;
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
    private double holdingPosition;
    private String userId;

    // Test swipe features
    private double authentication = 0.0;
    private double authenticationTime = 0.0;
    private int classifierSamples = 0;

    public Swipe() {

    }

    public Swipe(double[] swipeArray, double holdingPosition, String userId) {
        this.duration = swipeArray[0];
        this.minSize = swipeArray[1];
        this.maxSize = swipeArray[2];
        this.avgSize = swipeArray[3];
        this.downSize = swipeArray[4];
        this.upSize =  swipeArray[5];
        this.startX = swipeArray[6];
        this.startY = swipeArray[7];
        this.endX = swipeArray[8];
        this.endY = swipeArray[9];
        this.minXVelocity = swipeArray[10];
        this.maxXVelocity = swipeArray[11];
        this.avgXVelocity = swipeArray[12];
        this.stdXVelocity = swipeArray[13];
        this.varXVelocity = swipeArray[14];
        this.minYVelocity = swipeArray[15];
        this.maxYVelocity = swipeArray[16];
        this.avgYVelocity = swipeArray[17];
        this.stdYVelocity = swipeArray[18];
        this.varYVelocity = swipeArray[19];
        this.minXAccelerometer = swipeArray[20];
        this.maxXAccelerometer = swipeArray[21];
        this.avgXAccelerometer = swipeArray[22];
        this.stdXAccelerometer = swipeArray[23];
        this.varXAccelerometer = swipeArray[24];
        this.minYAccelerometer = swipeArray[25];
        this.maxYAccelerometer = swipeArray[26];
        this.avgYAccelerometer = swipeArray[27];
        this.stdYAccelerometer = swipeArray[28];
        this.varYAccelerometer = swipeArray[29];
        this.minZAccelerometer = swipeArray[30];
        this.maxZAccelerometer = swipeArray[31];
        this.avgZAccelerometer = swipeArray[32];
        this.stdZAccelerometer = swipeArray[33];
        this.varZAccelerometer = swipeArray[34];
        this.minXGyroscope = swipeArray[35];
        this.maxXGyroscope = swipeArray[36];
        this.avgXGyroscope = swipeArray[37];
        this.stdXGyroscope = swipeArray[38];
        this.varXGyroscope = swipeArray[39];
        this.minYGyroscope = swipeArray[40];
        this.maxYGyroscope = swipeArray[41];
        this.avgYGyroscope = swipeArray[42];
        this.stdYGyroscope = swipeArray[43];
        this.varYGyroscope = swipeArray[44];
        this.minZGyroscope = swipeArray[45];
        this.maxZGyroscope = swipeArray[46];
        this.avgZGyroscope = swipeArray[47];
        this.stdZGyroscope = swipeArray[48];
        this.varZGyroscope = swipeArray[49];
        this.minXOrientation = swipeArray[50];
        this.maxXOrientation = swipeArray[51];
        this.avgXOrientation = swipeArray[52];
        this.stdXOrientation = swipeArray[53];
        this.varXOrientation = swipeArray[54];
        this.minYOrientation = swipeArray[55];
        this.maxYOrientation = swipeArray[56];
        this.avgYOrientation = swipeArray[57];
        this.stdYOrientation = swipeArray[58];
        this.varYOrientation = swipeArray[59];
        this.minZOrientation = swipeArray[60];
        this.maxZOrientation = swipeArray[61];
        this.avgZOrientation = swipeArray[62];
        this.stdZOrientation = swipeArray[63];
        this.varZOrientation = swipeArray[64];
        this.holdingPosition = holdingPosition;
        this.userId = userId;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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

    public double getAuthentication() {
        return authentication;
    }

    public void setAuthentication(double authentication) {
        this.authentication = authentication;
    }

    public double getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(double authenticationTime) {
        this.authenticationTime = authenticationTime;
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
            map.put("MIN_DURATION", map.get("MIN_DURATION") == null || swipe.getDuration() < map.get("MIN_DURATION") ? swipe.getDuration() : map.get("MIN_DURATION"));
            map.put("MAX_DURATION", map.get("MAX_DURATION") == null || swipe.getDuration() > map.get("MAX_DURATION") ? swipe.getDuration() : map.get("MAX_DURATION"));

            map.put("MIN_MIN_SIZE", map.get("MIN_MIN_SIZE") == null || swipe.getMinSize() < map.get("MIN_MIN_SIZE") ? swipe.getMinSize() : map.get("MIN_MIN_SIZE"));
            map.put("MAX_MIN_SIZE", map.get("MAX_MIN_SIZE") == null || swipe.getMinSize() > map.get("MAX_MIN_SIZE") ? swipe.getMinSize() : map.get("MAX_MIN_SIZE"));

            map.put("MIN_MAX_SIZE", map.get("MIN_MAX_SIZE") == null || swipe.getMaxSize() < map.get("MIN_MAX_SIZE") ? swipe.getMaxSize() : map.get("MIN_MAX_SIZE"));
            map.put("MAX_MAX_SIZE", map.get("MAX_MAX_SIZE") == null || swipe.getMaxSize() > map.get("MAX_MAX_SIZE") ? swipe.getMaxSize() : map.get("MAX_MAX_SIZE"));

            map.put("MIN_AVG_SIZE", map.get("MIN_AVG_SIZE") == null || swipe.getAvgSize() < map.get("MIN_AVG_SIZE") ? swipe.getAvgSize() : map.get("MIN_AVG_SIZE"));
            map.put("MAX_AVG_SIZE", map.get("MAX_AVG_SIZE") == null || swipe.getAvgSize() > map.get("MAX_AVG_SIZE") ? swipe.getAvgSize() : map.get("MAX_AVG_SIZE"));

            map.put("MIN_DOWN_SIZE", map.get("MIN_DOWN_SIZE") == null || swipe.getDownSize() < map.get("MIN_DOWN_SIZE") ? swipe.getDownSize() : map.get("MIN_DOWN_SIZE"));
            map.put("MAX_DOWN_SIZE", map.get("MAX_DOWN_SIZE") == null || swipe.getDownSize() > map.get("MAX_DOWN_SIZE") ? swipe.getDownSize() : map.get("MAX_DOWN_SIZE"));

            map.put("MIN_UP_SIZE", map.get("MIN_UP_SIZE") == null || swipe.getUpSize() < map.get("MIN_UP_SIZE") ? swipe.getUpSize() : map.get("MIN_UP_SIZE"));
            map.put("MAX_UP_SIZE", map.get("MAX_UP_SIZE") == null || swipe.getUpSize() > map.get("MAX_UP_SIZE") ? swipe.getUpSize() : map.get("MAX_UP_SIZE"));

            map.put("MIN_START_X", map.get("MIN_START_X") == null || swipe.getStartX() < map.get("MIN_START_X") ? swipe.getStartX() : map.get("MIN_START_X"));
            map.put("MAX_START_X", map.get("MAX_START_X") == null || swipe.getStartX() > map.get("MAX_START_X") ? swipe.getStartX() : map.get("MAX_START_X"));
            map.put("MIN_START_Y", map.get("MIN_START_Y") == null || swipe.getStartY() < map.get("MIN_START_Y") ? swipe.getStartY() : map.get("MIN_START_Y"));
            map.put("MAX_START_Y", map.get("MAX_START_Y") == null || swipe.getStartY() > map.get("MAX_START_Y") ? swipe.getStartY() : map.get("MAX_START_Y"));

            map.put("MIN_END_X", map.get("MIN_END_X") == null || swipe.getEndX() < map.get("MIN_END_X") ? swipe.getEndX() : map.get("MIN_END_X"));
            map.put("MAX_END_X", map.get("MAX_END_X") == null || swipe.getEndX() > map.get("MAX_END_X") ? swipe.getEndX() : map.get("MAX_END_X"));
            map.put("MIN_END_Y", map.get("MIN_END_Y") == null || swipe.getEndY() < map.get("MIN_END_Y") ? swipe.getEndY() : map.get("MIN_END_Y"));
            map.put("MAX_END_Y", map.get("MAX_END_Y") == null || swipe.getEndY() > map.get("MAX_END_Y") ? swipe.getEndY() : map.get("MAX_END_Y"));

            for(int i = 0; i < DatabaseHelper.features.length; i++) {
                for(int j = 0; j < DatabaseHelper.metrics.length; j++) {
                    for(int x = 0; x < DatabaseHelper.dimensions.length; x++) {
                        if (x == 2 && i == 0) { continue; }

                        String min_key = "MIN_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();
                        String max_key = "MAX_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();

                        java.lang.reflect.Method cur_method = null;
                        Double cur_value = 0.0;
                        try {
                            cur_method = swipe.getClass().getMethod("get" + DatabaseHelper.metrics[j] + DatabaseHelper.dimensions[x] + DatabaseHelper.features[i]);
                            cur_value = (Double) cur_method.invoke(swipe);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        map.put(min_key, map.get(min_key) == null || cur_value < map.get(min_key) ? cur_value : map.get(min_key));
                        map.put(max_key, map.get(max_key) == null || cur_value > map.get(max_key) ? cur_value : map.get(max_key));
                    }
                }
            }
        }

        return map;
    }

    public double[] getNormalizedValues(ArrayList<Swipe> allSwipes) {
        Map<String, Double> map = getMinMaxValues(allSwipes);

        ArrayList<Double> ret = new ArrayList<Double>();

        ret.add((this.duration - map.get("MIN_DURATION")) / (map.get("MAX_DURATION") - map.get("MIN_DURATION")));
        ret.add((this.minSize - map.get("MIN_MIN_SIZE")) / (map.get("MAX_MIN_SIZE") - map.get("MIN_MIN_SIZE")));
        ret.add((this.maxSize - map.get("MIN_MAX_SIZE")) / (map.get("MAX_MAX_SIZE") - map.get("MIN_MAX_SIZE")));
        ret.add((this.avgSize - map.get("MIN_AVG_SIZE")) / (map.get("MAX_AVG_SIZE") - map.get("MIN_AVG_SIZE")));
        ret.add((this.downSize - map.get("MIN_DOWN_SIZE")) / (map.get("MAX_DOWN_SIZE") - map.get("MIN_DOWN_SIZE")));
        ret.add((this.upSize - map.get("MIN_UP_SIZE")) / (map.get("MAX_UP_SIZE") - map.get("MIN_UP_SIZE")));
        ret.add((this.startX - map.get("MIN_START_X")) / (map.get("MAX_START_X") - map.get("MIN_START_X")));
        ret.add((this.startY - map.get("MIN_START_Y")) / (map.get("MAX_START_Y") - map.get("MIN_START_Y")));
        ret.add((this.endX - map.get("MIN_END_X")) / (map.get("MAX_END_X") - map.get("MIN_END_X")));
        ret.add((this.endY - map.get("MIN_END_Y")) / (map.get("MAX_END_Y") - map.get("MIN_END_Y")));

        for(int i = 0; i < DatabaseHelper.features.length; i++) {
            for (int j = 0; j < DatabaseHelper.metrics.length; j++) {
                for (int x = 0; x < DatabaseHelper.dimensions.length; x++) {
                    if (x == 2 && i == 0) { continue; }

                    String min_key = "MIN_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();
                    String max_key = "MAX_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();

                    java.lang.reflect.Method cur_method = null;
                    Double cur_value = 0.0;
                    try {
                        cur_method = this.getClass().getMethod("get" + DatabaseHelper.metrics[j] + DatabaseHelper.dimensions[x] + DatabaseHelper.features[i]);
                        cur_value = (Double) cur_method.invoke(this);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    ret.add((cur_value - map.get(min_key)) / (map.get(max_key) - map.get(min_key)));
                }
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
        swipe.setDuration(values[0] * (map.get("MAX_DURATION") - map.get("MIN_DURATION")) + map.get("MIN_DURATION"));
        swipe.setMinSize(values[1] * (map.get("MAX_MIN_SIZE") - map.get("MIN_MIN_SIZE")) + map.get("MIN_MIN_SIZE"));
        swipe.setMaxSize(values[2] * (map.get("MAX_MAX_SIZE") - map.get("MIN_MAX_SIZE")) + map.get("MIN_MAX_SIZE"));
        swipe.setAvgSize(values[3] * (map.get("MAX_AVG_SIZE") - map.get("MIN_AVG_SIZE")) + map.get("MIN_AVG_SIZE"));
        swipe.setDownSize(values[4] * (map.get("MAX_DOWN_SIZE") - map.get("MIN_DOWN_SIZE")) + map.get("MIN_DOWN_SIZE"));
        swipe.setUpSize(values[5] * (map.get("MAX_UP_SIZE") - map.get("MIN_UP_SIZE")) + map.get("MIN_UP_SIZE"));
        swipe.setStartX(values[6] * (map.get("MAX_START_X") - map.get("MIN_START_X")) + map.get("MIN_START_X"));
        swipe.setStartY(values[7] * (map.get("MAX_START_Y") - map.get("MIN_START_Y")) + map.get("MIN_START_Y"));
        swipe.setEndX(values[8] * (map.get("MAX_END_X") - map.get("MIN_END_X")) + map.get("MIN_END_X"));
        swipe.setEndY(values[9] * (map.get("MAX_END_Y") - map.get("MIN_END_Y")) + map.get("MIN_END_Y"));

        Integer values_idx = 10;
        for(int i = 0; i < DatabaseHelper.features.length; i++) {
            for (int j = 0; j < DatabaseHelper.metrics.length; j++) {
                for (int x = 0; x < DatabaseHelper.dimensions.length; x++) {
                    if (x == 2 && i == 0) { continue; }

                    String min_key = "MIN_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();
                    String max_key = "MAX_" + DatabaseHelper.metrics[j].toUpperCase() + "_" + DatabaseHelper.dimensions[x] + "_" + DatabaseHelper.features[i].toUpperCase();

                    java.lang.reflect.Method cur_method = null;
                    try {
                        cur_method = swipe.getClass().getMethod("set" + DatabaseHelper.metrics[j] + DatabaseHelper.dimensions[x] + DatabaseHelper.features[i], double.class);
                        cur_method.invoke(swipe, values[values_idx] * (map.get(max_key) - map.get(min_key)) + map.get(min_key));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    values_idx = values_idx + 1;
                }
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
        boolean useSwipeSize = featureData.get(4) == 1;
        boolean useSwipeStartEndPos = featureData.get(5) == 1;
        boolean useSwipeVelocity = featureData.get(6) == 1;

        ArrayList<Double> featureSet = new ArrayList<>();
        if(useSwipeDuration) {
            featureSet.add(this.getDuration());
        }
        if(useSwipeSize) {
            featureSet.add(this.getMinSize());
            featureSet.add(this.getMaxSize());
            featureSet.add(this.getAvgSize());
            featureSet.add(this.getDownSize());
            featureSet.add(this.getUpSize());
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