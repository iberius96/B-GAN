package it.unibz.swipegan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.DoubleSummaryStatistics;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.classifiers.meta.OneClassClassifier;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import static it.unibz.swipegan.GAN.NUM_EPOCHS;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int NUMBER_GAN_SAMPLES = 100;

    private VelocityTracker mVelocityTracker = null;
    private long startTime = 0;
    private boolean isTrainingMode = true;
    private boolean isTrainingClassifier = false;

    private ArrayList<Float> xCoordinates = null;
    private ArrayList<Float> yCoordinates = null;
    private ArrayList<Float> xVelocities = null;
    private ArrayList<Float> yVelocities = null;
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
    private Button profileButton;
    private TextView inputTextView;
    private TextView progressTextView;
    private ProgressBar progressBar;
    private ImageView swipeImageView;
    private Switch attackSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
        this.profileButton = findViewById(R.id.profileButton);
        this.swipeImageView = findViewById(R.id.swipeImageView);
        this.attackSwitch = findViewById(R.id.attackSwitch);
        this.attackSwitch.setVisibility(View.INVISIBLE);
        this.attackSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                swipeImageView.setVisibility(View.INVISIBLE);
            } else {
                swipeImageView.setVisibility(View.VISIBLE);
            }
        });

        new Thread(() -> {
            this.dbHelper = new DatabaseHelper(this);
            int recordsCount = this.dbHelper.getRecordsCount("REAL_SWIPES");
            runOnUiThread(()-> inputTextView.setText("Inputs " + recordsCount + " (min 5)"));
        }).start();

        this.xCoordinates = new ArrayList<>();
        this.yCoordinates = new ArrayList<>();
        this.xVelocities = new ArrayList<>();
        this.yVelocities = new ArrayList<>();
        this.sizes = new ArrayList<>();

        this.xAccelerations = new ArrayList<>();
        this.yAccelerations = new ArrayList<>();

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        new Thread(() -> this.gan = new GAN()).start();

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
        if (this.isTrainingClassifier) {
            return super.onTouchEvent(event);
        }

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
                this.sizes.add(event.getSize(pointerId));

                this.mVelocityTracker = VelocityTracker.obtain();
                this.mVelocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_MOVE:
                this.mVelocityTracker.addMovement(event);
                this.mVelocityTracker.computeCurrentVelocity(1000);

                this.xVelocities.add(this.mVelocityTracker.getXVelocity(pointerId));
                this.yVelocities.add(this.mVelocityTracker.getYVelocity(pointerId));
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
                        this.dbHelper.addTrainRecord(swipe);
                        int recordsCount = this.dbHelper.getRecordsCount("REAL_SWIPES");
                        inputTextView.setText("Inputs " + recordsCount + " (min 5)");
                    } else {
                        String outputMessage = "";
                        outputMessage += String.format("%1$-15s %2$-16s %3$-18s", "Inputs", "Prediction", "Test time");
                        outputMessage += "\n";

                        long startTime = System.nanoTime();
                        double prediction = this.getPredictionFrom(swipe);
                        long endTime = System.nanoTime();
                        double testingTime = (double) (endTime - startTime) / 1_000_000_000;

                        double authenticationValue;
                        if (this.attackSwitch.isChecked()) {
                            authenticationValue = prediction != 0.0 ? 1.0 : 0.0;
                        } else {
                            authenticationValue = prediction == 0.0 ? 1.0 : 0.0;
                        }

                        int classifierSamples = this.dbHelper.getRecordsCount("REAL_SWIPES");
                        this.dbHelper.addTestRecord(swipe, authenticationValue, testingTime, classifierSamples);

                        outputMessage += String.format("%1$-18s", String.format("%02d", classifierSamples));
                        outputMessage += String.format("%1$-18s", prediction == 0.0 ? "Accepted" : "Rejected");
                        outputMessage += String.format("%1$-18s", String.format("%.4f", testingTime));
                        outputMessage += "\n";

                        //this.showAlertDialog("AUTHENTICATION", outputMessage);
                        if (prediction == 0.0) {
                            this.showSnackBar("Authentication: ACCEPTED", "#238823");
                        } else {
                            this.showSnackBar("Authentication: REJECTED", "#D2222D");
                        }

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
        Instance instance = swipe.getAsWekaInstance(this.getWekaDataset(), false);
        instance.setDataset(this.getWekaDataset());

        double prediction = 0.0;

        try {
            // classifyInstances returns the index of the most likely class identified (NaN if neither class was identified)
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
        newSwipe.setStartX(startX);
        newSwipe.setStartY(startY);
        newSwipe.setEndX(endX);
        newSwipe.setEndY(endY);

        newSwipe.setMinXVelocity(minXVelocity);
        newSwipe.setMaxXVelocity(maxXVelocity);
        newSwipe.setAvgXVelocity(avgXVelocity);
        newSwipe.setStdXVelocity(stdXVelocity);
        newSwipe.setVarXVelocity(varXVelocity);

        newSwipe.setMinYVelocity(minYVelocity);
        newSwipe.setMaxYVelocity(maxYVelocity);
        newSwipe.setAvgYVelocity(avgYVelocity);
        newSwipe.setStdYVelocity(stdYVelocity);
        newSwipe.setVarYVelocity(varYVelocity);

        newSwipe.setMinXAcceleration(minXAcceleration);
        newSwipe.setMaxXAcceleration(maxXAcceleration);
        newSwipe.setAvgXAcceleration(avgXAcceleration);
        newSwipe.setStdXAcceleration(stdXAcceleration);
        newSwipe.setVarXAcceleration(varXAcceleration);

        newSwipe.setMinYAcceleration(minYAcceleration);
        newSwipe.setMaxYAcceleration(maxYAcceleration);
        newSwipe.setAvgYAcceleration(avgYAcceleration);
        newSwipe.setStdYAcceleration(stdYAcceleration);
        newSwipe.setVarYAcceleration(varYAcceleration);

        newSwipe.setUserId(this.attackSwitch.isChecked() ? "Attacker" : "User");

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

            String strSummary = "";
            strSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s %6$-9s %7$-9s %8$-9s", "Inputs", "TAR", "FRR", "TRR", "FAR", "Swipe", "Train", "Classifier");
            strSummary += "\n";

            //for (int i=0; i < this.oneClassClassifiers.size(); i++) {
            ArrayList<Swipe> testSwipes = dbHelper.getTestSwipes();

            if (testSwipes.size() == 0) {
                this.showAlertDialog("ATTENTION", "You need to enter at least a swipe to test the authentication system");
                return;
            }

            ArrayList<double[]> userTestingData = this.dbHelper.getTestingData("User");
            ArrayList<double[]> attackerTestingData = this.dbHelper.getTestingData("Attacker");
            System.out.println(Arrays.toString(attackerTestingData.stream().mapToDouble(x -> x[0]).toArray()));

            double instances = userTestingData.size() + attackerTestingData.size();
            double TAR = userTestingData.isEmpty() ? 0.0 : userTestingData.stream().mapToDouble(x -> x[0]).sum() / userTestingData.size() * 100;
            double FRR = userTestingData.isEmpty() ? 0.0 : (userTestingData.size() - (userTestingData.stream().mapToDouble(x -> x[0]).sum())) / userTestingData.size() * 100;
            double TRR = attackerTestingData.isEmpty() ? 0.0 : (attackerTestingData.stream().mapToDouble(x -> x[0]).sum()) / attackerTestingData.size() * 100;
            double FAR = attackerTestingData.isEmpty() ? 0.0 : (attackerTestingData.size() - (attackerTestingData.stream().mapToDouble(x -> x[0]).sum())) / attackerTestingData.size() * 100;
            double averageSwipeDuration = testSwipes.stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000.0;

            userTestingData.addAll(attackerTestingData);
            double avgTestTime = userTestingData.stream().mapToDouble(x -> x[1]).average().getAsDouble();

            int classifierSamples = this.dbHelper.getRecordsCount("REAL_SWIPES");
            this.dbHelper.saveTestResults(instances, TAR, FRR, TRR, FAR, averageSwipeDuration, avgTestTime, classifierSamples);

            strSummary += String.format("%1$-12s", String.format("%02.0f", instances));
            strSummary += String.format("%1$-10s", String.format("%.1f", TAR));
            strSummary += String.format("%1$-11s", String.format("%.1f", FRR));
            strSummary += String.format("%1$-10s", String.format("%.1f", TRR));
            strSummary += String.format("%1$-11s", String.format("%.1f", FAR));
            strSummary += String.format("%1$-11s", String.format("%.3f", averageSwipeDuration));
            strSummary += String.format("%1$-10s", String.format("%.3f", avgTestTime));
            strSummary += String.format("%1$-10s", String.format("%02d", classifierSamples));
            strSummary += "\n";
            //}

            this.showAlertDialog("TESTING RESULTS", strSummary);

            this.attackSwitch.setChecked(false);

            this.inputTextView.setText("Inputs " + this.dbHelper.getRecordsCount("REAL_SWIPES") + " (min 5)");
            this.ganButton.setVisibility(View.VISIBLE);
            this.trainButton.setVisibility(View.VISIBLE);
            this.saveButton.setVisibility(View.VISIBLE);
            this.attackSwitch.setVisibility(View.INVISIBLE);
            this.isTrainingMode = true;
            this.resetButton.setText("Reset DB");
        }
    }

    public void train(View view) {
        this.isTrainingClassifier = true;

        this.progressTextView.setText("Training classifier");
        this.train(false);

    }

    public void trainWithGAN(View view) {
        this.isTrainingClassifier = true;

        this.progressTextView.setText("GAN epoch: 0 (out of " + NUM_EPOCHS + ")");
        this.train(true);
    }

    public void train(boolean isGanMode) {

        if (this.dbHelper.getRecordsCount("REAL_SWIPES") < 5) {
            this.showAlertDialog("ATTENTION", "You need to enter at least 5 swipes as input before training.");
            this.isTrainingClassifier = false;
            return;
        }

        this.progressTextView.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.swipeImageView.setVisibility(View.INVISIBLE);
        this.saveButton.setVisibility(View.INVISIBLE);

        this.isTrainingMode = false;

        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable uiRunnable = () -> {
            inputTextView.setText("Swipe to authenticate");
            ganButton.setVisibility(View.INVISIBLE);
            trainButton.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
            resetButton.setText("Done");

            progressTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            swipeImageView.setVisibility(View.VISIBLE);
            attackSwitch.setVisibility(View.VISIBLE);
            attackSwitch.setChecked(false);
        };

        Runnable uiRFTrainingRunnable = () -> {
            progressTextView.setText("Training classifier");
        };

        Runnable runnable = () -> {
            dbHelper.deleteRealResults();
            dbHelper.deleteGANData();
            dbHelper.deleteTestingData();

            ArrayList<Swipe> swipes = dbHelper.getAllSwipes("REAL_SWIPES");
            try {
                if (isGanMode) {
                    long ganStartTime = System.nanoTime();
                    ArrayList<Swipe> fakeSwipes = gan.getFakeSwipeSamples(swipes, swipes.size(), progressTextView);
                    dbHelper.addGANRecords(fakeSwipes);

                    swipes.addAll(fakeSwipes);
                    long ganEndTime = System.nanoTime();
                    double ganTime = (double) (ganEndTime - ganStartTime) / 1_000_000_000;

                    uiHandler.post(uiRFTrainingRunnable);

                    trainClassifierWith(swipes, ganTime);
                } else {
                    trainClassifierWith(swipes);
                }

                uiHandler.post(uiRunnable);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(runnable).start();

    }
    public void trainClassifierWith(ArrayList<Swipe> trainSwipes, double ganTime) {
        Instances dataSet = this.getWekaDataset();

        for(int i=0 ; i < trainSwipes.size(); i++)
        {
            Instance newInstance = trainSwipes.get(i).getAsWekaInstance(dataSet,true);
            dataSet.add(newInstance);
        }

        String finalSummary = "";
        finalSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s", "Input", "TAR", "FRR", "GAN", "Train");
        finalSummary += "\n";

        System.out.println("ARFF representation of Dataset");
        System.out.println(dataSet.toString());

        OneClassClassifier oneClassClassifier = new OneClassClassifier();
        try {
//            Options from Weka GUI when testing One-class RandomForest classifier:
//            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.1", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.meta.Bagging", "--", "-P", "100", "-S", "1", "-num-slots", "1", "-I", "10", "-W", "weka.classifiers.trees.RandomForest", "--", "-P", "100", "-I", "100", "-num-slots", "1", "-K", "0", "-M", "1.0", "-V", "0.001", "-S", "1", "", ""};
            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.001", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "100", "-num-slots", "1", "-K", "0", "-S", "1", "", "", "", "", "", "", "", ""};
            oneClassClassifier.setOptions(options);
            oneClassClassifier.setTargetClassLabel("User");

            long rfStartTime = System.nanoTime();
            oneClassClassifier.buildClassifier(dataSet);
            long rfEndTime = System.nanoTime();
            double rfTrainingTime = (double) (rfEndTime - rfStartTime) / 1_000_000_000;

            Evaluation eTest = new Evaluation(dataSet);
            eTest.crossValidateModel(oneClassClassifier, dataSet, 5, new Random(1));

            this.oneClassClassifier = oneClassClassifier;

            double instances = eTest.numInstances();
            double TAR = eTest.pctCorrect();
            double FRR = eTest.pctUnclassified();

            double averageSwipeDuration = trainSwipes.subList(0, dataSet.size()).stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000;

            finalSummary += String.format("%1$-12s", String.format("%02.0f", instances));
            finalSummary += String.format("%1$-10s", String.format("%.1f", TAR));
            finalSummary += String.format("%1$-11s", String.format("%.1f", FRR));
            finalSummary += String.format("%1$-11s", String.format("%.3f", ganTime));
            finalSummary += String.format("%1$-10s", String.format("%.3f", rfTrainingTime));
            finalSummary += "\n";

            this.dbHelper.saveGANResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime, ganTime, trainSwipes.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(finalSummary);
        this.isTrainingClassifier = false;

        final String strSummary = finalSummary;
        MainActivity context = this;

        runOnUiThread(() -> context.showAlertDialog("TRAINING RESULTS", strSummary));

    }

    public void trainClassifierWith(ArrayList<Swipe> trainSwipes) {

        Instances dataSet = this.getWekaDataset();

        for(int i=0 ; i < trainSwipes.size(); i++)
        {
            Instance newInstance = trainSwipes.get(i).getAsWekaInstance(dataSet,true);
            dataSet.add(newInstance);
        }

        String finalSummary = "";
        finalSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s", "Input", "TAR", "FRR", "Swipe", "Train");
        finalSummary += "\n";

        System.out.println("ARFF representation of Dataset");
        System.out.println(dataSet.toString());

        OneClassClassifier oneClassClassifier = new OneClassClassifier();
        try {
//            Options from Weka GUI when testing One-class RandomForest classifier:
//            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.1", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.meta.Bagging", "--", "-P", "100", "-S", "1", "-num-slots", "1", "-I", "10", "-W", "weka.classifiers.trees.RandomForest", "--", "-P", "100", "-I", "100", "-num-slots", "1", "-K", "0", "-M", "1.0", "-V", "0.001", "-S", "1", "", ""};
            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.001", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "100", "-num-slots", "1", "-K", "0", "-S", "1", "", "", "", "", "", "", "", ""};
            oneClassClassifier.setOptions(options);
            oneClassClassifier.setTargetClassLabel("User");

            long rfStartTime = System.nanoTime();
            oneClassClassifier.buildClassifier(dataSet);
            long rfEndTime = System.nanoTime();
            double rfTrainingTime = (double) (rfEndTime - rfStartTime) / 1_000_000_000;

            Evaluation eTest = new Evaluation(dataSet);
            eTest.crossValidateModel(oneClassClassifier, dataSet, 5, new Random(1));

            this.oneClassClassifier = oneClassClassifier;

            double instances = eTest.numInstances();
            double TAR = eTest.pctCorrect();
            double FRR = eTest.pctUnclassified();

            double averageSwipeDuration = trainSwipes.subList(0, dataSet.size()).stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000;

            finalSummary += String.format("%1$-12s", String.format("%02.0f", instances));
            finalSummary += String.format("%1$-10s", String.format("%.1f", TAR));
            finalSummary += String.format("%1$-11s", String.format("%.1f", FRR));
            finalSummary += String.format("%1$-11s", String.format("%.3f", averageSwipeDuration));
            finalSummary += String.format("%1$-10s", String.format("%.3f", rfTrainingTime));
            finalSummary += "\n";

            this.dbHelper.saveRealResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime, trainSwipes.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(finalSummary);
        this.isTrainingClassifier = false;

        final String strSummary = finalSummary;
        MainActivity context = this;

        runOnUiThread(() -> context.showAlertDialog("TRAINING RESULTS", strSummary));
    }

    public synchronized void saveData(View view) {

        try {
            dbHelper.saveAllTablesAsCSV(getContentResolver(), getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath());
            //this.showAlertDialog("SUCCESS", "CSV files have been saved into the file manager");
            this.showSnackBar("CSV files saved to /Downloads", "#238823");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void showSnackBar(String message, String hexColor) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor(hexColor));

        snackbar.show();
    }

    public Instances getWekaDataset() {
        Attribute duration = new Attribute("duration");
        Attribute avgSize = new Attribute("avgSize");
        Attribute downSize = new Attribute("downSize");
        Attribute startX = new Attribute("startX");
        Attribute startY = new Attribute("startY");
        Attribute endX = new Attribute("endX");
        Attribute endY = new Attribute("endY");
        Attribute minXVelocity = new Attribute("minXVelocity");
        Attribute maxXVelocity = new Attribute("maxXVelocity");
        Attribute avgXVelocity = new Attribute("avgXVelocity");
        Attribute stdXVelocity = new Attribute("stdXVelocity");
        Attribute varXVelocity = new Attribute("varXVelocity");
        Attribute minYVelocity = new Attribute("minYVelocity");
        Attribute maxYVelocity = new Attribute("maxYVelocity");
        Attribute avgYVelocity = new Attribute("avgYVelocity");
        Attribute stdYVelocity = new Attribute("stdYVelocity");
        Attribute varYVelocity = new Attribute("varYVelocity");
        Attribute minXAcceleration = new Attribute("minXAcceleration");
        Attribute maxXAcceleration = new Attribute("maxXAcceleration");
        Attribute avgXAcceleration = new Attribute("avgXAcceleration");
        Attribute stdXAcceleration = new Attribute("stdXAcceleration");
        Attribute varXAcceleration = new Attribute("varXAcceleration");
        Attribute minYAcceleration = new Attribute("minYAcceleration");
        Attribute maxYAcceleration = new Attribute("maxYAcceleration");
        Attribute avgYAcceleration = new Attribute("avgYAcceleration");
        Attribute stdYAcceleration = new Attribute("stdYAcceleration");
        Attribute varYAcceleration = new Attribute("varYAcceleration");
        ArrayList<String> myNominalValues = new ArrayList<>(2);
        myNominalValues.add("User");
        myNominalValues.add("Attacker");
        Attribute owner = new Attribute("owner", myNominalValues);

        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(duration);
        attributes.add(avgSize);
        attributes.add(downSize);
        attributes.add(startX);
        attributes.add(startY);
        attributes.add(endX);
        attributes.add(endY);
        attributes.add(minXVelocity);
        attributes.add(maxXVelocity);
        attributes.add(avgXVelocity);
        attributes.add(stdXVelocity);
        attributes.add(varXVelocity);
        attributes.add(minYVelocity);
        attributes.add(maxYVelocity);
        attributes.add(avgYVelocity);
        attributes.add(stdYVelocity);
        attributes.add(varYVelocity);
        attributes.add(minXAcceleration);
        attributes.add(maxXAcceleration);
        attributes.add(avgXAcceleration);
        attributes.add(stdXAcceleration);
        attributes.add(varXAcceleration);
        attributes.add(minYAcceleration);
        attributes.add(maxYAcceleration);
        attributes.add(avgYAcceleration);
        attributes.add(stdYAcceleration);
        attributes.add(varYAcceleration);
        attributes.add(owner);

        Instances dataSet = new Instances("swipes", attributes, 0);
        dataSet.setClass(owner);

        return dataSet;
    }

}