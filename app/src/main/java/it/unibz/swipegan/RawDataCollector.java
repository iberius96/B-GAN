package it.unibz.swipegan;

import java.util.Arrays;

public class RawDataCollector implements Runnable {

    private Integer frequency;
    private Thread resourceMonitorThread;
    private MainActivity mainActivity;
    private DatabaseHelper dbHelper;

    public void start(MainActivity mainActivity, DatabaseHelper dbHelper) {
        this.mainActivity = mainActivity;
        this.dbHelper = dbHelper;
        this.frequency = this.dbHelper.getFeatureData().get(DatabaseHelper.COL_RAW_DATA_FREQUENCY);

        if (resourceMonitorThread == null) {
            resourceMonitorThread = new Thread(this);
            resourceMonitorThread.start();
        }
    }

    public void stop() {
        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }
    }

    public boolean isRunning() {
        return resourceMonitorThread != null;
    }

    public void run() {
        long dueDate = System.currentTimeMillis();
        while (!resourceMonitorThread.interrupted()) {
            if(this.mainActivity.isTrackingSensors()) {
                dbHelper.addRawDataEntry(this.mainActivity.getRawData(), "TRAIN_RAW_DATA");
            }

            dueDate = dueDate + (1000 / this.frequency);
            long sleepInterval = dueDate - System.currentTimeMillis();
            if (sleepInterval > 0) {
                try {
                    Thread.currentThread().sleep(sleepInterval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        resourceMonitorThread = null;
    }
}
