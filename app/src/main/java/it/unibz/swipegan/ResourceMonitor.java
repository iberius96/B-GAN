package it.unibz.swipegan;

import android.app.ActivityManager;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ResourceMonitor implements Runnable {

    private Thread resourceMonitorThread;

    private ActivityManager activityManager;
    private BatteryManager batteryManager;

    private ArrayList<Float> tempValues;
    private ArrayList<Float> memoryValues;
    private ArrayList<Long> batteryValues;
    private double trainingTime;

    public void start(ActivityManager activityManager, BatteryManager batteryManager) {
        this.activityManager = activityManager;
        this.batteryManager = batteryManager;

        this.tempValues = new ArrayList<>();
        this.memoryValues = new ArrayList<>();
        this.batteryValues = new ArrayList<>();

        if (resourceMonitorThread == null) {
            resourceMonitorThread = new Thread(this);
            resourceMonitorThread.start();
        }
    }

    public void stop(DatabaseHelper dbHelpber, String modelType) {
        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }

        ArrayList<Float> curTempValues = (ArrayList<Float>) tempValues.clone();
        ArrayList<Float> curMemoryValues = (ArrayList<Float>) memoryValues.clone();
        ArrayList<Long> curBatteryValues = (ArrayList<Long>) batteryValues.clone();

        Double[] arrayTemps = {curTempValues.stream().mapToDouble(d -> d).min().orElse(0.0),  curTempValues.stream().mapToDouble(d -> d).max().orElse(0.0), curTempValues.stream().mapToDouble(d -> d).average().orElse(0.0)};
        Double[] arrayMemory = {curMemoryValues.stream().mapToDouble(d -> d).min().orElse(0.0),  curMemoryValues.stream().mapToDouble(d -> d).max().orElse(0.0), curMemoryValues.stream().mapToDouble(d -> d).average().orElse(0.0)};

        dbHelpber.saveResourceData(
                arrayTemps,
                arrayMemory,
                curBatteryValues.stream().mapToDouble(d -> d).sum(),
                trainingTime,
                modelType);
    }

    public void run() {
        while (!resourceMonitorThread.interrupted()) {
            this.tempValues.add(getCpuTemperature());
            this.memoryValues.add(getUsedMemory());
            this.batteryValues.add(getPowerDraw());
        }

        resourceMonitorThread = null;
    }

    private static float getCpuTemperature()
    {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            }else{
                return 51.0f;
            }
        } catch (InterruptedException | IOException e) {
            return 0.0f;
        }
    }

    public float getUsedMemory() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        this.activityManager.getMemoryInfo(mi);

        float usage = (float) mi.availMem / mi.totalMem;
        return usage * 100;
    }

    public long getPowerDraw() {
        // Instantaneous battery current in microamperes, as an integer.
        // Positive values indicate net current entering the battery from a charge source, negative values indicate net current discharging from the battery.
        Long curDraw = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        if(curDraw < 0) {
            return -curDraw;
        } else {
            return 0;
        }
    }

    public void setTrainingTime(double trainingTime) {
        this.trainingTime = trainingTime;
    }
}