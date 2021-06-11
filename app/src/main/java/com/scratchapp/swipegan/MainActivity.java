package com.scratchapp.swipegan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.DoubleSummaryStatistics;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.classifiers.meta.OneClassClassifier;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int GAN_SAMPLES = 50;

    private VelocityTracker mVelocityTracker = null;
    private long startTime = 0;
    private boolean isTrainingMode = true;

    private ArrayList<Float> xCoordinates = null;
    private ArrayList<Float> yCoordinates = null;
    private ArrayList<Float> xVelocities = null;
    private ArrayList<Float> yVelocities = null;
    private ArrayList<Float> pressures = null;
    private ArrayList<Float> sizes = null;
    private int duration = 0;

    private boolean isTrackingAccelerometer = false;
    private ArrayList<Float> xAccelerations = null;
    private ArrayList<Float> yAccelerations = null;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private DatabaseHelper dbHelper;

    private OneClassClassifier oneClassClassifier;
    private GAN gan;

    private Button ganButton;
    private Button trainButton;
    private Button saveButton;
    private Button resetButton;
    private TextView inputTextView;
    private TextView progressTextView;
    private ProgressBar progressBar;
    private ImageView swipeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SwipeGAN");

        this.progressTextView = findViewById(R.id.progressTextView);
        this.progressTextView.setVisibility(View.INVISIBLE);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.inputTextView = findViewById(R.id.inputTextView);
        this.ganButton = findViewById(R.id.ganButton);
        this.saveButton = findViewById(R.id.saveButton);
        this.saveButton.setVisibility(View.INVISIBLE);
        this.trainButton = findViewById(R.id.trainButton);
        this.resetButton = findViewById(R.id.resetButton);
        this.swipeImageView = findViewById(R.id.swipeImageView);

        new Thread(() -> {
            this.dbHelper = new DatabaseHelper(this);
            int recordsCount = this.dbHelper.getRecordsCount("REAL_SWIPES");
            runOnUiThread(()->{
                inputTextView.setText("Inputs " + recordsCount + " (min 5)");
            });
        }).start();

        this.xCoordinates = new ArrayList<Float>();
        this.yCoordinates = new ArrayList<Float>();
        this.xVelocities = new ArrayList<Float>();
        this.yVelocities = new ArrayList<Float>();
        this.pressures = new ArrayList<Float>();
        this.sizes = new ArrayList<Float>();

        this.xAccelerations = new ArrayList<Float>();
        this.yAccelerations = new ArrayList<Float>();

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        new Thread(() -> {
            this.gan = new GAN();
        }).start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (this.isTrackingAccelerometer) {
            this.xAccelerations.add(event.values[0]);
            this.yAccelerations.add(event.values[1]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // initialization step
                this.startTime = System.currentTimeMillis();
                this.isTrackingAccelerometer = true;

                this.xCoordinates.add(event.getX(pointerId));
                this.yCoordinates.add(event.getY(pointerId));
                this.pressures.add(event.getPressure(pointerId));
                this.sizes.add(event.getSize(pointerId));

                this.mVelocityTracker = VelocityTracker.obtain();
                this.mVelocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_MOVE:
                this.mVelocityTracker.addMovement(event);
                this.mVelocityTracker.computeCurrentVelocity(1000);

                this.xVelocities.add(this.mVelocityTracker.getXVelocity(pointerId));
                this.yVelocities.add(this.mVelocityTracker.getYVelocity(pointerId));
                this.pressures.add(event.getPressure(pointerId));
                this.sizes.add(event.getSize(pointerId));

                break;
            case MotionEvent.ACTION_UP:
                this.xCoordinates.add(event.getX(pointerId));
                this.yCoordinates.add(event.getY(pointerId));
                this.duration = (int) (System.currentTimeMillis() - this.startTime);

                double distance = Math.sqrt(Math.pow(this.yCoordinates.get(this.yCoordinates.size() - 1) - this.yCoordinates.get(0), 2) + Math.pow(this.xCoordinates.get(this.xCoordinates.size() - 1) - this.xCoordinates.get(0), 2));
                if(distance > 20) {
                    Swipe swipe = this.getSwipe();
                    if(this.isTrainingMode) {
                        this.saveButton.setVisibility(View.INVISIBLE);
                        this.dbHelper.addTrainRecord(swipe, "REAL_SWIPES");
                        int recordsCount = this.dbHelper.getRecordsCount("REAL_SWIPES");
                        inputTextView.setText("Inputs " + recordsCount + " (min 5)");
                    } else {
                        String outputMessage = "";

                        long startTime = System.nanoTime();
                        double prediction = this.getPredictionFrom(swipe);
                        long endTime = System.nanoTime();
                        double testingTime = (double) (endTime - startTime) / 1_000_000_000;

                        double authenticationValue = prediction == 0.0 ? 1.0 : 0.0;
                        this.dbHelper.addTestRecord(swipe, authenticationValue, testingTime);

                        outputMessage += "Testing time for swipe: " + String.format("%.3f", testingTime) + "s\n\n";

                        double[] distribution = this.getDistributionFrom(swipe);
                        if(prediction == 0.0){
                            String predictedValue = this.getWekaDataset().classAttribute().value((int) prediction);
                            outputMessage += "Swipe authentication successful\nPrediction value: " + predictedValue + "\nDistribution: " +Arrays.toString(distribution);
                        } else {
                            outputMessage += "Swipe authentication failed\nPrediction value: " + prediction +"\nDistribution: " +Arrays.toString(distribution);
                        }
                        this.showAlertDialog("AUTHENTICATION", outputMessage);
                    }

                }
                this.resetValues();
                break;
            case MotionEvent.ACTION_CANCEL:
                this.resetValues();
                break;
        }

        return super.onTouchEvent(event);
    }

    private double getPredictionFrom(Swipe swipe) {
        double prediction = 0;

        Instance instance = swipe.getAsWekaInstance(this.getWekaDataset(), false);
        instance.setDataset(this.getWekaDataset());

        try {
            // classigyInstances returns the index of the most likely class identified (NaN if neither class was identified)
            prediction = this.oneClassClassifier.classifyInstance(instance);
            System.out.println("Prediction: " + prediction);
            System.out.println("Distribution: " + Arrays.toString(this.oneClassClassifier.distributionForInstance(instance)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }
    private double[] getDistributionFrom(Swipe swipe) {
        double[] distribution = new double[1];

        Instance instance = swipe.getAsWekaInstance(this.getWekaDataset(), false);
        instance.setDataset(this.getWekaDataset());
        try {
            distribution = this.oneClassClassifier.distributionForInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distribution;
    }

    public void resetValues() {
        this.xCoordinates.clear();
        this.yCoordinates.clear();
        this.xVelocities.clear();
        this.yVelocities.clear();
        this.pressures.clear();
        this.sizes.clear();

        this.xAccelerations.clear();
        this.yAccelerations.clear();

        this.mVelocityTracker = null;
        this.isTrackingAccelerometer = false;
    }

    public Swipe getSwipe() {
        double duration = this.duration;

        DoubleSummaryStatistics sizesStats = this.sizes.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double avgSize = sizesStats.getAverage();
        double downSize = this.sizes.get(0);

        double downPressure = this.pressures.get(0);
        double startX = this.xCoordinates.get(0);
        double startY = this.yCoordinates.get(0);
        double endX = this.xCoordinates.get(this.xCoordinates.size() - 1);
        double endY = this.yCoordinates.get(this.yCoordinates.size() - 1);

        DoubleSummaryStatistics xVelocityStats = this.xVelocities.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXVelocity = xVelocityStats.getMin();
        double maxXVelocity = xVelocityStats.getMax();
        double avgXVelocity = xVelocityStats.getAverage();
        double varXVelocity = this.xVelocities.stream().map(i -> i - avgXVelocity).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXVelocity = Math.sqrt(varXVelocity);

        DoubleSummaryStatistics yVelocityStats = this.xVelocities.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYVelocity = yVelocityStats.getMin();
        double maxYVelocity = yVelocityStats.getMax();
        double avgYVelocity = yVelocityStats.getAverage();
        double varYVelocity = this.yVelocities.stream().map(i -> i - avgYVelocity).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYVelocity = Math.sqrt(varYVelocity);

        DoubleSummaryStatistics pressureStats = this.pressures.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minPressure = pressureStats.getMin();
        double maxPressure = pressureStats.getMax();
        double avgPressure = pressureStats.getAverage();
        double varPressure = this.pressures.stream().map(i -> i - avgPressure).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdPressure = Math.sqrt(varPressure);

        DoubleSummaryStatistics xAccelerationStats = this.xAccelerations.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXAcceleration = xAccelerationStats.getMin();
        double maxXAcceleration = xAccelerationStats.getMax();
        double avgXAcceleration = xAccelerationStats.getAverage();
        double varXAcceleration = this.xAccelerations.stream().map(i -> i - avgXAcceleration).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXAcceleration = Math.sqrt(varXAcceleration);

        DoubleSummaryStatistics yAccelerationStats = this.yAccelerations.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYAcceleration = yAccelerationStats.getMin();
        double maxYAcceleration = yAccelerationStats.getMax();
        double avgYAcceleration = yAccelerationStats.getAverage();
        double varYAcceleration = this.yAccelerations.stream().map(i -> i - avgYAcceleration).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYAcceleration = Math.sqrt(varYAcceleration);

        Swipe newSwipe = new Swipe();
        newSwipe.setDuration(duration);
        newSwipe.setAvgSize(avgSize);
        newSwipe.setDownSize(downSize);
        newSwipe.setDownPressure(downPressure);
        newSwipe.setStartX(startX);
        newSwipe.setStartY(startY);
        newSwipe.setEndX(endX);
        newSwipe.setEndY(endY);

        newSwipe.setMinXVelocity(minXVelocity);
        newSwipe.setMaxXVelocity(maxXVelocity);
        newSwipe.setAvgXVelocity(avgXVelocity);
        newSwipe.setVarXVelocity(varXVelocity);
        newSwipe.setStdXVelocity(stdXVelocity);

        newSwipe.setMinYVelocity(minYVelocity);
        newSwipe.setMaxYVelocity(maxYVelocity);
        newSwipe.setAvgYVelocity(avgYVelocity);
        newSwipe.setVarYVelocity(varYVelocity);
        newSwipe.setStdYVelocity(stdYVelocity);

        newSwipe.setMinPressure(minPressure);
        newSwipe.setMaxPressure(maxPressure);
        newSwipe.setAvgPressure(avgPressure);
        newSwipe.setVarPressure(varPressure);
        newSwipe.setStdPressure(stdPressure);

        newSwipe.setMinXAcceleration(minXAcceleration);
        newSwipe.setMaxXAcceleration(maxXAcceleration);
        newSwipe.setAvgXAcceleration(avgXAcceleration);
        newSwipe.setVarXAcceleration(varXAcceleration);
        newSwipe.setStdXAcceleration(stdXAcceleration);

        newSwipe.setMinYAcceleration(minYAcceleration);
        newSwipe.setMaxYAcceleration(maxYAcceleration);
        newSwipe.setAvgYAcceleration(avgYAcceleration);
        newSwipe.setVarYAcceleration(varYAcceleration);
        newSwipe.setStdYAcceleration(stdYAcceleration);

        double[] normalizedSwipeValues = newSwipe.getNormalizedValues();

        String debugMessage = "";
        for (int i=0; i<normalizedSwipeValues.length; i++) {
            if ((normalizedSwipeValues[i] < 0) || (normalizedSwipeValues[i] > 1)) {
                debugMessage += "Issue for feature -> (" + i + "). Sample value: " + normalizedSwipeValues[i] + " (out of bounds)\n";
            }
        }

        if (!debugMessage.isEmpty()) {
            this.showAlertDialog("DEBUG ALERT", debugMessage);
        }

        System.out.println("--------------------------------------------New Swipe---------------------------------------------------------------");
        System.out.println(newSwipe);
        return newSwipe;
    }

    public void resetData(View view) {
        if(this.isTrainingMode) {
            this.dbHelper.resetDB();
            this.inputTextView.setText("Inputs " + this.dbHelper.getRecordsCount("REAL_SWIPES") + " (min 5)");
        } else {
            ArrayList<Swipe> testSwipes = dbHelper.getAllSwipes("TEST_SWIPES");

            if (testSwipes.size() == 0) {
                this.showAlertDialog("ATTENTION", "You need to enter at least a swipe to test the authentication system");
                return;
            }

            String strSummary = "Total number of Instances provided: " + testSwipes.size() + "\n";

            ArrayList<double[]> testingData = this.dbHelper.getTestingData();
            double instances = testingData.size();
            double TAR = testingData.stream().mapToDouble(x -> x[0]).sum() / testSwipes.size() * 100;
            double FRR = (testSwipes.size() - (testingData.stream().mapToDouble(x -> x[0]).sum())) / testSwipes.size() * 100;
            double averageSwipeDuration = testSwipes.stream().mapToDouble(x -> x.getDuration()).average().getAsDouble() / 1_000.0;
            double avgTestTime = testingData.stream().mapToDouble(x -> x[1]).average().getAsDouble();

            strSummary += "True acceptance rate: " + TAR + "%\n";
            strSummary += "False reject rate: " + FRR + "%\n";
            strSummary += "\n";
            strSummary += "Average sample acquisition time: " + String.format("%.3f", averageSwipeDuration) + "s\n";
            strSummary += "\n";
            strSummary += "Average classification time: " + String.format("%.3f", avgTestTime) + "s\n";

            this.dbHelper.saveTestResults(instances, TAR, FRR, averageSwipeDuration, avgTestTime);

            this.showAlertDialog("TESTING RESULTS", strSummary);

            this.inputTextView.setText("Inputs " + this.dbHelper.getRecordsCount("REAL_SWIPES") + " (min 5)");
            this.ganButton.setVisibility(View.VISIBLE);
            this.trainButton.setVisibility(View.VISIBLE);
            this.saveButton.setVisibility(View.VISIBLE);
            this.isTrainingMode = true;
            this.resetButton.setText("Reset DB");
        }
    }

    public void train(View view) throws Exception {

        if (this.dbHelper.getRecordsCount("REAL_SWIPES") < 5) {
            this.showAlertDialog("ATTENTION", "You need to enter at least 5 swipes as input before training.");
            return;
        }

        this.progressTextView.setVisibility(View.VISIBLE);
        this.progressTextView.setText("GAN epoch: 0 (out of 10_000)");
        this.progressBar.setVisibility(View.VISIBLE);
        this.swipeImageView.setVisibility(View.INVISIBLE);
        this.saveButton.setVisibility(View.INVISIBLE);

        this.isTrainingMode = false;

        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                inputTextView.setText("Swipe to authenticate");
                ganButton.setVisibility(View.INVISIBLE);
                trainButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                resetButton.setText("Done");

                progressTextView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                swipeImageView.setVisibility(View.VISIBLE);
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dbHelper.deleteRealResults();
                dbHelper.deleteGANData();
                dbHelper.deleteTestingData();
                ArrayList<Swipe> swipes = dbHelper.getAllSwipes("REAL_SWIPES");
                train(swipes, 0.0);
                uiHandler.post(uiRunnable);
            }
        };

        new Thread(runnable).start();

    }

    public void trainWithGAN(View view) throws Exception {

        if (this.dbHelper.getRecordsCount("REAL_SWIPES") < 5) {
            this.showAlertDialog("ATTENTION", "You need to enter at least 5 swipes as input before training.");
            return;
        }

        this.progressTextView.setVisibility(View.VISIBLE);
        this.progressTextView.setText("GAN epoch: 0 (out of 10_000)");
        this.progressBar.setVisibility(View.VISIBLE);
        this.swipeImageView.setVisibility(View.INVISIBLE);
        this.saveButton.setVisibility(View.INVISIBLE);

        this.isTrainingMode = false;

        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                inputTextView.setText("Swipe to authenticate");
                ganButton.setVisibility(View.INVISIBLE);
                trainButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                resetButton.setText("Done");

                progressTextView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                swipeImageView.setVisibility(View.VISIBLE);
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dbHelper.deleteRealResults();
                dbHelper.deleteGANData();
                dbHelper.deleteTestingData();

                ArrayList<Swipe> swipes = dbHelper.getAllSwipes("REAL_SWIPES");
                try {
                    long ganStartTime = System.nanoTime();
                    swipes.addAll(gan.getFakeSwipeSamples(swipes, GAN_SAMPLES, progressTextView));
                    long ganEndTime = System.nanoTime();
                    double ganTime = (double) (ganEndTime - ganStartTime) / 1_000_000_000;

                    train(swipes, ganTime);
                    uiHandler.post(uiRunnable);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

    }

    public void train(ArrayList<Swipe> trainSwipes, double ganTime) {

        Instances dataSet = this.getWekaDataset();

        for(int i = 0 ; i < trainSwipes.size(); i++)
        {
            Instance newInstance = trainSwipes.get(i).getAsWekaInstance(dataSet,true);
            dataSet.add(newInstance);
        }

        System.out.println("ARFF representation of Dataset");
        System.out.println(dataSet.toString());

        this.oneClassClassifier = new OneClassClassifier();
        try {
//            Options from Weka GUI when testing One-class RandomForest classifier:
//            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.1", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.meta.Bagging", "--", "-P", "100", "-S", "1", "-num-slots", "1", "-I", "10", "-W", "weka.classifiers.trees.RandomForest", "--", "-P", "100", "-I", "100", "-num-slots", "1", "-K", "0", "-M", "1.0", "-V", "0.001", "-S", "1", "", ""};
            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.001", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "100", "-num-slots", "1", "-K", "0", "-S", "1", "", "", "", "", "", "", "", ""};
            this.oneClassClassifier.setOptions(options);
            this.oneClassClassifier.setTargetClassLabel("User");

            long rfStartTime = System.nanoTime();
            this.oneClassClassifier.buildClassifier(dataSet);
            long rfEndTime = System.nanoTime();
            double rfTrainingTime = (double) (rfEndTime - rfStartTime) / 1_000_000_000;

            Evaluation eTest = new Evaluation(dataSet);
            eTest.evaluateModel(oneClassClassifier, dataSet);

            double instances = eTest.numInstances();
            double TAR = eTest.correct() / instances * 100;
            double FRR = eTest.unclassified() / instances * 100;
            double averageSwipeDuration = trainSwipes.stream().mapToDouble(x -> x.getDuration()).average().getAsDouble() / 1_000;

            String strSummary = "Total number of Instances provided: " + instances + "\n";
            strSummary += "True acceptance rate: " + String.format("%.1f", TAR) + "%\n";
            strSummary += "False reject rate: " + String.format("%.1f", FRR) + "%\n";
            strSummary += "\n";
            strSummary += "Average sample acquisition time: " + String.format("%.3f", averageSwipeDuration) + "s\n";
            strSummary += "\n";
            if (ganTime != 0.0) {
                strSummary += "Generating " + GAN_SAMPLES + " GAN samples in: " + String.format("%.3f", ganTime) + "s\n";
                strSummary += "\n";
            }
            strSummary += "Training time of classifier: " + String.format("%.3f", rfTrainingTime) + "s\n";

            System.out.println("Evaluation of classifier:");
            System.out.println(strSummary);

            if (ganTime != 0.0) {
                this.dbHelper.saveGANResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime, ganTime);
            } else {
                this.dbHelper.saveRealResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime);
            }

            final String summary = strSummary;
            MainActivity context = this;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.showAlertDialog("TRAINING RESULTS", summary);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveData(View view) throws InterruptedException {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
            String currentDateTime = dateFormat.format(new Date());

            dbHelper.saveAsCSV("REAL_SWIPES", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "realSwipes.csv");
            dbHelper.saveAsCSV("GAN_SWIPES", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "ganSwipes.csv");
            dbHelper.saveAsCSV("TEST_SWIPES", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "testSwipes.csv");
            dbHelper.saveAsCSV("REAL_RESULTS", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "realResults.csv");
            dbHelper.saveAsCSV("GAN_RESULTS", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "ganResults.csv");
            dbHelper.saveAsCSV("TEST_RESULTS", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + currentDateTime + "_" + "testResultsPath.csv");

            this.showAlertDialog("SUCCESS", "CSV files have been saved into the file manager");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public Instances getWekaDataset() {
        Attribute duration = new Attribute("duration");
        Attribute avgSize = new Attribute("avgSize");
        Attribute downSize = new Attribute("downSize");
        Attribute downPressure = new Attribute("downPressure");
        Attribute startX = new Attribute("startX");
        Attribute startY = new Attribute("startY");
        Attribute endX = new Attribute("endX");
        Attribute endY = new Attribute("endY");
        Attribute minXVelocity = new Attribute("minXVelocity");
        Attribute maxXVelocity = new Attribute("maxXVelocity");
        Attribute avgXVelocity = new Attribute("avgXVelocity");
        Attribute varXVelocity = new Attribute("varXVelocity");
        Attribute stdXVelocity = new Attribute("stdXVelocity");
        Attribute minYVelocity = new Attribute("minYVelocity");
        Attribute maxYVelocity = new Attribute("maxYVelocity");
        Attribute avgYVelocity = new Attribute("avgYVelocity");
        Attribute varYVelocity = new Attribute("varYVelocity");
        Attribute stdYVelocity = new Attribute("stdYVelocity");
        Attribute minPressure = new Attribute("minPressure");
        Attribute maxPressure = new Attribute("maxPressure");
        Attribute avgPressure = new Attribute("avgPressure");
        Attribute varPressure = new Attribute("varPressure");
        Attribute stdPressure = new Attribute("stdPressure");
        Attribute minXAcceleration = new Attribute("minXAcceleration");
        Attribute maxXAcceleration = new Attribute("maxXAcceleration");
        Attribute avgXAcceleration = new Attribute("avgXAcceleration");
        Attribute varXAcceleration = new Attribute("varXAcceleration");
        Attribute stdXAcceleration = new Attribute("stdXAcceleration");
        Attribute minYAcceleration = new Attribute("minYAcceleration");
        Attribute maxYAcceleration = new Attribute("maxYAcceleration");
        Attribute avgYAcceleration = new Attribute("avgYAcceleration");
        Attribute varYAcceleration = new Attribute("varYAcceleration");
        Attribute stdYAcceleration = new Attribute("stdYAcceleration");
        ArrayList<String> myNominalValues = new ArrayList<String>(1);
        myNominalValues.add("User");
        Attribute owner = new Attribute("owner", myNominalValues);

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(duration);
        attributes.add(avgSize);
        attributes.add(downSize);
        attributes.add(downPressure);
        attributes.add(startX);
        attributes.add(startY);
        attributes.add(endX);
        attributes.add(endY);
        attributes.add(minXVelocity);
        attributes.add(maxXVelocity);
        attributes.add(avgXVelocity);
        attributes.add(varXVelocity);
        attributes.add(stdXVelocity);
        attributes.add(minYVelocity);
        attributes.add(maxYVelocity);
        attributes.add(avgYVelocity);
        attributes.add(varYVelocity);
        attributes.add(stdYVelocity);
        attributes.add(minPressure);
        attributes.add(maxPressure);
        attributes.add(avgPressure);
        attributes.add(varPressure);
        attributes.add(stdPressure);
        attributes.add(minXAcceleration);
        attributes.add(maxXAcceleration);
        attributes.add(avgXAcceleration);
        attributes.add(varXAcceleration);
        attributes.add(stdXAcceleration);
        attributes.add(minYAcceleration);
        attributes.add(maxYAcceleration);
        attributes.add(avgYAcceleration);
        attributes.add(varYAcceleration);
        attributes.add(stdYAcceleration);
        attributes.add(owner);

        Instances dataSet = new Instances("swipes", attributes, 0);
        dataSet.setClass(owner);

        return dataSet;
    }

}