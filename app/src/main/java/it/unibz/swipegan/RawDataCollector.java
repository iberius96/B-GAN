package it.unibz.swipegan;

import java.util.Arrays;

/**
 * The raw data collector object.
 * Controls the execution of the raw data collector.
 * Throughout a given interaction, the collector repeatedly gathers from the device information related to:
 *  Size of the screen area currently touched.
 *  X / Y coordinates values of the touched location.
 *  Current X / Y velocity values.
 *  Current X / Y / Z values of Accelerometer / Gyroscope / Orientation.
 *  Identifier of the current gesture (0 = Swipe, 1 = Keystroke, 2 = Signature).
 *
 *  The frequency at which data is gathered is determined by the corresponding setting in the user profile.
 *  Raw data is collected only when actively completing an interaction and, more precisely:
 *      When the user is actively touching the screen during the swipe gesture.
 *      In the period between the insertion of the first to last digit of the keystroke gesture.
 *      When the user is actively touching the screen during the signature gesture.
 */
public class RawDataCollector implements Runnable {
    /**
     * The frequency (expressed in Hz) at which the raw data is collected.
     */
    private Integer frequency;

    /**
     * The thread object controlling the execution of the raw data collector.
     */
    private Thread rawDataCollectorThread;

    /**
     * The main activity object.
     */
    private MainActivity mainActivity;

    /**
     * The database helper object.
     */
    private DatabaseHelper dbHelper;

    /**
     * Starts the raw data collection process by:
     *  1. Retrieving the currently selected raw data collection frequency from the DB.
     *  2. Initializing (and starting) the thread in which the current instance of the raw data collector class will be executed.
     *
     * @param mainActivity The main activity object.
     * @param dbHelper The database helper object.
     */
    public void start(MainActivity mainActivity, DatabaseHelper dbHelper) {
        this.mainActivity = mainActivity;
        this.dbHelper = dbHelper;
        this.frequency = this.dbHelper.getFeatureData().get(DatabaseHelper.COL_RAW_DATA_FREQUENCY);

        if (rawDataCollectorThread == null) {
            rawDataCollectorThread = new Thread(this);
            rawDataCollectorThread.start();
        }
    }

    /**
     * Stops the raw data collection process by interrupting the currently active thread.
     */
    public void stop() {
        if (rawDataCollectorThread != null) {
            rawDataCollectorThread.interrupt();
        }
    }

    /**
     * Checks whether the thread associated with raw data collection process is currently active.
     *
     * @return True if the raw data collection process is running; False otherwise.
     */
    public boolean isRunning() {
        return rawDataCollectorThread != null;
    }

    /**
     * Method called upon execution of the raw data collection thread.
     * As long as the thread is running, this method repeatedly calls the .getRawData() method in the main activity and (after each call) adds the raw data entry to the DB.
     * Sleep times between requests are determined by the initially set frequency.
     */
    public void run() {
        long dueDate = System.currentTimeMillis();
        while (!rawDataCollectorThread.interrupted()) {
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

        rawDataCollectorThread = null;
    }
}
