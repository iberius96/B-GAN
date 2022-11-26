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

/**
 * Controls the execution of the device resource monitor.
 * When training a model, the resource monitor gathers from the device information related to:
 *  Min / Max / Avg CPU frequency for each CPU core.
 *  Min / Max / Avg memory usage (in percentage).
 *  Total battery power draw (in microamperes).
 *  The total training time.
 * A resource entry is generated and added to the DB (at the end of the training phase for each model) when the execution of resource monitor is interrupted.
 */
public class ResourceMonitor implements Runnable {
    /**
     * Stores the thread object controlling the execution of the resource monitor.
     */
    private Thread resourceMonitorThread;

    /**
     * Stores the activity manager object used to access the main memory information.
     */
    private ActivityManager activityManager;

    /**
     * Stores the battery manager object used to access the battery information.
     */
    private BatteryManager batteryManager;

    /**
     * Stores the set of frequency values gathered from the CPU.
     * Each entry is a list of integer containing one frequency value per CPU core.
     */
    private ArrayList<Integer[]> freqValues;

    /**
     * Stores the set of memory values (in the form of percentage of currently used memory).
     */
    private ArrayList<Float> memoryValues;

    /**
     * Stores the set of battery consumption values (in the form of instantaneous battery current in microamperes).
     */
    private ArrayList<Long> batteryValues;

    /**
     * Stores the training time (in seconds) of the model for which the resource data is currently being gathered.
     */
    private double trainingTime;

    /**
     * Defines the default value for the number of CPU cores.
     */
    private final static int DEFAULT_CORES = 8;

    /**
     * Starts the resource data collection process by initializing (and starting) the thread in which the current instance of the resource monitor class will be executed.
     *
     * @param activityManager The activity manager object.
     * @param batteryManager The battery manager object.
     */
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

    /**
     * Stops the resource monitoring process by interrupting the currently active thread.
     * Upon stopping the active thread, the method retrieves the collected resource values and:
     *  Computes the min / max / avg CPU frequencies for each CPU core.
     *  Computes the min / max / avg memory usage.
     *  Computes the sum of all the gathered battery consumption values.
     * Finally, it adds the current resource entry to the DB.
     *
     * @param dbHelpber
     * @param modelType
     */
    public void stop(DatabaseHelper dbHelpber, String modelType) {
        ArrayList<Integer[]> curFreqValues = (ArrayList<Integer[]>) freqValues.clone();
        ArrayList<Float> curMemoryValues = (ArrayList<Float>) memoryValues.clone();
        ArrayList<Long> curBatteryValues = (ArrayList<Long>) batteryValues.clone();

        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }

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

    /**
     * Method called upon execution of the resource monitor thread.
     * As long as the thread is running, this method repeatedly extends the sets of CPU, memory and battery values.
     */
    public void run() {
        while (!resourceMonitorThread.interrupted()) {
            this.freqValues.add(getCpuFrequency());
            this.memoryValues.add(getUsedMemory());
            this.batteryValues.add(getPowerDraw());
        }

        resourceMonitorThread = null;
    }

    /**
     * Retrieves the current CPU frequency values for all cores.
     * The values are fetched from `sys/devices/system/cpu/cpui/cpufreq/scaling_cur_freq` where `i` in `cpui` corresponds to the currently analyzed core.
     *
     * @return The current set of CPU frequencies for all cores.
     */
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

    /**
     * Gets the currently utilised device memory by calling the .getMemoryInfo() method of the activity manager object.
     *
     * @return The percentage of memory currently in use.
     */
    public float getUsedMemory() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        this.activityManager.getMemoryInfo(mi);

        float usage = (float) mi.availMem / mi.totalMem;
        return usage * 100;
    }

    /**
     * Retrieves the instantaneous battery current (in microamperes) by calling the .getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) of the battery manager object.
     *
     * @return The current power draw or 0 if the device is being charged.
     */
    public long getPowerDraw() {
        Long curDraw = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        if(curDraw < 0) {
            return -curDraw;
        } else {
            return 0;
        }
    }

    /**
     * Setter for the training time value associated with the current resource monitor entry.
     *
     * @param trainingTime The training time in seconds.
     */
    public void setTrainingTime(double trainingTime) {
        this.trainingTime = trainingTime;
    }
}