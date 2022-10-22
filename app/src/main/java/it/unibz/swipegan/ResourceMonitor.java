package it.unibz.swipegan;

import android.app.ActivityManager;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceMonitor implements Runnable {

    private Thread resourceMonitorThread;

    private ActivityManager activityManager;
    private BatteryManager batteryManager;

    private ArrayList<Integer[]> freqValues;
    private ArrayList<Float> memoryValues;
    private ArrayList<Long> batteryValues;
    private double trainingTime;

    private final static int DEFAULT_CORES = 8;

    public void start(ActivityManager activityManager, BatteryManager batteryManager) {
        this.activityManager = activityManager;
        this.batteryManager = batteryManager;

        this.freqValues = new ArrayList<>();
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

        ArrayList<Integer[]> curFreqValues = (ArrayList<Integer[]>) freqValues.clone();
        ArrayList<Float> curMemoryValues = (ArrayList<Float>) memoryValues.clone();
        ArrayList<Long> curBatteryValues = (ArrayList<Long>) batteryValues.clone();

        Integer[] minCpuFreq = new Integer[DEFAULT_CORES];
        Integer[] maxCpuFreq = new Integer[DEFAULT_CORES];
        Integer[] avgCpuFreq = new Integer[DEFAULT_CORES];

        for(int i = 0; i < DEFAULT_CORES; i++) {
            final int core = i;
            minCpuFreq[i] = curFreqValues.stream().mapToInt(r->r[core]).min().getAsInt();
            maxCpuFreq[i] = curFreqValues.stream().mapToInt(r->r[core]).max().getAsInt();
            avgCpuFreq[i] = (int) curFreqValues.stream().mapToInt(r->r[core]).average().getAsDouble();
        }

        String[] arrayCpu = {
                Arrays.toString(minCpuFreq),
                Arrays.toString(maxCpuFreq),
                Arrays.toString(avgCpuFreq)
        };

        Double[] arrayMemory = {curMemoryValues.stream().mapToDouble(d -> d).min().orElse(0.0),  curMemoryValues.stream().mapToDouble(d -> d).max().orElse(0.0), curMemoryValues.stream().mapToDouble(d -> d).average().orElse(0.0)};

        dbHelpber.saveResourceData(
            arrayCpu,
            arrayMemory,
            curBatteryValues.stream().mapToDouble(d -> d).sum(),
            trainingTime,
            modelType);
    }

    public void run() {
        while (!resourceMonitorThread.interrupted()) {
            this.freqValues.add(getCpuFrequency());
            this.memoryValues.add(getUsedMemory());
            this.batteryValues.add(getPowerDraw());
        }

        resourceMonitorThread = null;
    }

    private static Integer[] getCpuFrequency()
    {
        Process process;
        Integer[] cpu_freq = new Integer[DEFAULT_CORES];
        for(int i = 0; i < DEFAULT_CORES; i++) {
            try {
                process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                if(line!=null) {
                    cpu_freq[i] = Integer.parseInt(line);
                } else{
                    cpu_freq[i] = 1170;
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                return new Integer[DEFAULT_CORES];
            }
        }

        return cpu_freq;
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