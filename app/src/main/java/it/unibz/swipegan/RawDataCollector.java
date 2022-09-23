package it.unibz.swipegan;

import android.app.ActivityManager;
import android.os.BatteryManager;

import java.util.ArrayList;

public class RawDataCollector implements Runnable {

    private Thread resourceMonitorThread;

    public void start(ActivityManager activityManager, BatteryManager batteryManager) {
        if (resourceMonitorThread == null) {
            resourceMonitorThread = new Thread(this);
            resourceMonitorThread.start();
        }
    }

    public void stop(DatabaseHelper dbHelpber, String modelType) {
        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }

        //dbHelpber.saveResourceData
    }

    public void run() {
        while (!resourceMonitorThread.interrupted()) {

        }

        resourceMonitorThread = null;
    }
}
