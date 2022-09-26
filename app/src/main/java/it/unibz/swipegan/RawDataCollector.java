package it.unibz.swipegan;

import java.util.Arrays;

public class RawDataCollector implements Runnable {

    private final Integer frequency = 50;
    private Thread resourceMonitorThread;
    private MainActivity mainActivity;

    public void start(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        if (resourceMonitorThread == null) {
            resourceMonitorThread = new Thread(this);
            resourceMonitorThread.start();
        }
    }

    public void stop() {
        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }

        //dbHelpber.saveResourceData
    }

    public boolean isRunning() {
        return resourceMonitorThread != null;
    }

    public void run() {
        long dueDate = System.currentTimeMillis();
        while (!resourceMonitorThread.interrupted()) {
            if(this.mainActivity.isTrackingSensors()) {
                System.out.println(Arrays.toString(this.mainActivity.getRawData()));
            }

            dueDate = dueDate + (1000 / this.frequency);
            long sleepInterval = dueDate - System.currentTimeMillis();
            if (sleepInterval > 0) {
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        resourceMonitorThread = null;
    }
}
