package it.unibz.swipegan;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static it.unibz.swipegan.GAN.NUM_EPOCHS;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.os.Looper;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Random;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.OneClassClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private long startTime = 0;
    private boolean isTrainingMode = true;
    private boolean isTrainingClassifier = false;
    private int holdingPosition = 0;

    private ArrayList<Float> xLocations = null;
    private ArrayList<Float> yLocations = null;
    private ArrayList<Float> xVelocityTranslation = null;
    private ArrayList<Float> yVelocityTranslation = null;
    private ArrayList<Float> sizes = null;
    private int duration = 0;

    private double length = 0;

    private boolean isTrackingAccelerometer = false;
    private ArrayList<Float> xAccelerometers = null;
    private ArrayList<Float> yAccelerometers = null;
    private ArrayList<Float> zAccelerometers = null;

    private boolean isTrackingGyroscope = false;
    private ArrayList<Float> xGyroscopes = null;
    private ArrayList<Float> yGyroscopes = null;
    private ArrayList<Float> zGyroscopes = null;

    private boolean isTrackingMagnetometer;
    private ArrayList<Float> xOrientations = null;
    private ArrayList<Float> yOrientations = null;
    private ArrayList<Float> zOrientations = null;

    private float[] mGravity;
    private float[] mGeomagnetic;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;

    private DatabaseHelper dbHelper;

    private OneClassClassifier oneClassClassifiers[] = new OneClassClassifier[3];
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
    private RadioButton sittingRadioButton;
    private RadioButton standingRadioButton;
    private RadioButton walkingRadioButton;
    private RadioGroup holdingPositionRadioGroup;

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
        this.holdingPositionRadioGroup = findViewById(R.id.holdingPositionRadioGroup);
        this.sittingRadioButton = findViewById(R.id.sittingRadioButton);
        this.standingRadioButton = findViewById(R.id.standingRadioButton);
        this.walkingRadioButton = findViewById(R.id.walkingRadioButton);
        this.sittingRadioButton.setChecked(true);

        this.sittingRadioButton.setOnCheckedChangeListener((radioButtonView, isChecked) -> {
            if (isChecked) {
                this.holdingPosition = 1;
            }
        });

        this.standingRadioButton.setOnCheckedChangeListener((radioButtonView, isChecked) -> {
            if (isChecked) {
                this.holdingPosition = 2;
            }
        });

        this.walkingRadioButton.setOnCheckedChangeListener((radioButtonView, isChecked) -> {
            if (isChecked) {
                this.holdingPosition = 3;
            }
        });

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

        this.xLocations = new ArrayList<>();
        this.yLocations = new ArrayList<>();
        this.xVelocityTranslation = new ArrayList<>();
        this.yVelocityTranslation = new ArrayList<>();
        this.sizes = new ArrayList<>();

        this.xAccelerometers = new ArrayList<>();
        this.yAccelerometers = new ArrayList<>();
        this.zAccelerometers = new ArrayList<>();

        this.xGyroscopes = new ArrayList<>();
        this.yGyroscopes = new ArrayList<>();
        this.zGyroscopes = new ArrayList<>();

        this.xOrientations = new ArrayList<>();
        this.yOrientations = new ArrayList<>();
        this.zOrientations = new ArrayList<>();

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(MainActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && this.isTrackingAccelerometer) {
            this.mGravity = applyLowPassFilter(event.values.clone(), this.mGravity);

            this.xAccelerometers.add(event.values[0]);
            this.yAccelerometers.add(event.values[1]);
            this.zAccelerometers.add(event.values[2]);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE && this.isTrackingGyroscope) {
            this.xGyroscopes.add(event.values[0]);
            this.yGyroscopes.add(event.values[1]);
            this.zGyroscopes.add(event.values[2]);
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && this.isTrackingMagnetometer) {
            this.mGeomagnetic = applyLowPassFilter(event.values.clone(), this.mGeomagnetic);
        }

        if (this.mGravity != null && this.mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, this.mGravity, this.mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                this.xOrientations.add(orientation[1]);
                this.yOrientations.add(orientation[2]);
                this.zOrientations.add(orientation[0]);
            }
        }
    }

    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.5f * (input[i] - output[i]);
        }
        return output;
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
                this.length = 0;

                this.startTime = System.currentTimeMillis();
                this.isTrackingAccelerometer = true;
                this.isTrackingGyroscope = true;
                this.isTrackingMagnetometer = true;

                this.xLocations.add(event.getX(pointerId));
                this.yLocations.add(event.getY(pointerId));
                this.sizes.add(event.getSize(pointerId));

                break;
            case MotionEvent.ACTION_MOVE:

                final int historySize = event.getHistorySize();
                final int pointerCount = event.getPointerCount();
                for (int h = 0; h < historySize; h++) {

                    float hx = event.getHistoricalX(0, h);
                    float hy = event.getHistoricalY(0, h);
                    float dx = (hx - this.xLocations.get(this.xLocations.size() - 1));
                    float dy = (hy - this.yLocations.get(this.yLocations.size() - 1));
                    this.length += Math.sqrt(dx * dx + dy * dy);

                    for (int p = 0; p < pointerCount; p++) {
                        float newX = event.getHistoricalX(p, h);
                        float newY = event.getHistoricalY(p, h);
                        this.xVelocityTranslation.add(newX - this.xLocations.get(this.xLocations.size() - 1));
                        this.yVelocityTranslation.add(newY - this.yLocations.get(this.yLocations.size() - 1));
                        this.xLocations.add(newX);
                        this.yLocations.add(newY);
                    }
                }

                for (int p = 0; p < pointerCount; p++) {
                    float newX = event.getX(p);
                    float newY = event.getY(p);
                    this.xVelocityTranslation.add(newX - this.xLocations.get(this.xLocations.size() - 1));
                    this.yVelocityTranslation.add(newY - this.yLocations.get(this.yLocations.size() - 1));
                    this.xLocations.add(newX);
                    this.yLocations.add(newY);
                }

                this.sizes.add(event.getSize(pointerId));

                break;
            case MotionEvent.ACTION_UP:

                float newX = event.getX(pointerId);
                float newY = event.getY(pointerId);
                this.xVelocityTranslation.add(newX - this.xLocations.get(this.xLocations.size() - 1));
                this.yVelocityTranslation.add(newY - this.yLocations.get(this.yLocations.size() - 1));
                this.xLocations.add(newX);
                this.yLocations.add(newY);

                this.duration = (int) (System.currentTimeMillis() - this.startTime);

                double distance = Math.sqrt(Math.pow(this.yLocations.get(this.yLocations.size() - 1) - this.yLocations.get(0), 2) + Math.pow(this.xLocations.get(this.xLocations.size() - 1) - this.xLocations.get(0), 2));
                if(distance > 20) {
                    Swipe swipe = this.getSwipe();
                    if(this.isTrainingMode) {
                        this.saveButton.setVisibility(View.INVISIBLE);
                        this.dbHelper.addTrainRecord(swipe);
                        int recordsCount = this.dbHelper.getRecordsCount("REAL_SWIPES");
                        this.inputTextView.setText("Inputs " + recordsCount + " (min 5)");
                    } else {
                        String outputMessage = "";
                        outputMessage += String.format("%1$-15s %2$-16s %3$-18s", "Inputs", "Prediction", "Test time");
                        outputMessage += "\n";

                        for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                            long startTime = System.nanoTime();
                            double prediction = this.getPredictionFrom(swipe, modelType);
                            long endTime = System.nanoTime();
                            double testingTime = (double) (endTime - startTime) / 1_000_000_000;

                            double authenticationValue;
                            if (this.attackSwitch.isChecked()) {
                                authenticationValue = prediction != 0.0 ? 1.0 : 0.0;
                            } else {
                                authenticationValue = prediction == 0.0 ? 1.0 : 0.0;
                            }

                            int classifierSamples = this.dbHelper.getRecordsCount("REAL_SWIPES");

                            swipe.setAuthentication(authenticationValue, modelType);
                            swipe.setAuthenticationTime(testingTime, modelType);
                            swipe.setClassifierSamples(classifierSamples);

                            outputMessage += String.format("%1$-18s", String.format("%02d", classifierSamples));
                            outputMessage += String.format("%1$-18s", prediction == 0.0 ? "Accepted" : "Rejected");
                            outputMessage += String.format("%1$-18s", String.format("%.4f", testingTime));
                            outputMessage += "\n";

                            if(modelType == DatabaseHelper.ModelType.FULL) {
                                if (prediction == 0.0) {
                                    this.showSnackBar("Authentication: ACCEPTED", "#238823");
                                } else {
                                    this.showSnackBar("Authentication: REJECTED", "#D2222D");
                                }
                            }
                        }
                        this.dbHelper.addTestRecord(swipe);
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

    private double getPredictionFrom(Swipe swipe, DatabaseHelper.ModelType modelType) {
        Instance instance = swipe.getAsWekaInstance(this.getWekaDataset(modelType), false, dbHelper, modelType);
        instance.setDataset(this.getWekaDataset(modelType));

        double prediction = 0.0;

        try {
            // classifyInstances returns the index of the most likely class identified (NaN if neither class was identified)
            prediction = this.oneClassClassifiers[modelType.ordinal()].classifyInstance(instance);
            System.out.println("Prediction: " + prediction);
            System.out.println("Distribution: " + Arrays.toString(this.oneClassClassifiers[modelType.ordinal()].distributionForInstance(instance)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }

    public void resetValues() {
        this.xLocations.clear();
        this.yLocations.clear();
        this.xVelocityTranslation.clear();
        this.yVelocityTranslation.clear();
        this.sizes.clear();

        this.xAccelerometers.clear();
        this.yAccelerometers.clear();
        this.zAccelerometers.clear();

        this.xGyroscopes.clear();
        this.yGyroscopes.clear();
        this.zGyroscopes.clear();

        this.xOrientations.clear();
        this.yOrientations.clear();
        this.zOrientations.clear();

        this.mGravity = null;
        this.mGeomagnetic = null;

        this.isTrackingAccelerometer = false;
        this.isTrackingGyroscope = false;
        this.isTrackingMagnetometer = false;
    }

    public Swipe getSwipe() {
        double duration = this.duration;

        double length = this.length;

        DoubleSummaryStatistics sizesStats = this.sizes.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minSize = sizesStats.getMin();
        double maxSize = sizesStats.getMax();
        double avgSize = sizesStats.getAverage();
        double downSize = this.sizes.get(0);
        double upSize = this.sizes.get(this.sizes.size() - 1);

        double startX = this.xLocations.get(0);
        double startY = this.yLocations.get(0);
        double endX = this.xLocations.get(this.xLocations.size() - 1);
        double endY = this.yLocations.get(this.yLocations.size() - 1);

        DoubleSummaryStatistics xVelocityStats = this.xVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXVelocity = xVelocityStats.getMin();
        double maxXVelocity = xVelocityStats.getMax();
        double avgXVelocity = xVelocityStats.getAverage();
        double varXVelocity = this.xVelocityTranslation.stream().map(i -> i - avgXVelocity).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXVelocity = Math.sqrt(varXVelocity);

        DoubleSummaryStatistics yVelocityStats = this.xVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYVelocity = yVelocityStats.getMin();
        double maxYVelocity = yVelocityStats.getMax();
        double avgYVelocity = yVelocityStats.getAverage();
        double varYVelocity = this.yVelocityTranslation.stream().map(i -> i - avgYVelocity).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYVelocity = Math.sqrt(varYVelocity);

        DoubleSummaryStatistics xAccelerometerStats = this.xAccelerometers.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXAccelerometer = xAccelerometerStats.getMin();
        double maxXAccelerometer = xAccelerometerStats.getMax();
        double avgXAccelerometer = xAccelerometerStats.getAverage();
        double varXAccelerometer = this.xAccelerometers.stream().map(i -> i - avgXAccelerometer).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXAccelerometer = Math.sqrt(varXAccelerometer);

        DoubleSummaryStatistics yAccelerometerStats = this.yAccelerometers.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYAccelerometer = yAccelerometerStats.getMin();
        double maxYAccelerometer = yAccelerometerStats.getMax();
        double avgYAccelerometer = yAccelerometerStats.getAverage();
        double varYAccelerometer = this.yAccelerometers.stream().map(i -> i - avgYAccelerometer).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYAccelerometer = Math.sqrt(varYAccelerometer);

        DoubleSummaryStatistics zAccelerometerStats = this.zAccelerometers.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minZAccelerometer = zAccelerometerStats.getMin();
        double maxZAccelerometer = zAccelerometerStats.getMax();
        double avgZAccelerometer = zAccelerometerStats.getAverage();
        double varZAccelerometer = this.zAccelerometers.stream().map(i -> i - avgZAccelerometer).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdZAccelerometer = Math.sqrt(varZAccelerometer);

        DoubleSummaryStatistics xGyroscopeStats = this.xGyroscopes.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXGyroscope = xGyroscopeStats.getMin();
        double maxXGyroscope = xGyroscopeStats.getMax();
        double avgXGyroscope = xGyroscopeStats.getAverage();
        double varXGyroscope = this.xGyroscopes.stream().map(i -> i - avgXGyroscope).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXGyroscope = Math.sqrt(varXGyroscope);

        DoubleSummaryStatistics yGyroscopeStats = this.yGyroscopes.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYGyroscope = yGyroscopeStats.getMin();
        double maxYGyroscope = yGyroscopeStats.getMax();
        double avgYGyroscope = yGyroscopeStats.getAverage();
        double varYGyroscope = this.yGyroscopes.stream().map(i -> i - avgYGyroscope).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYGyroscope = Math.sqrt(varYGyroscope);

        DoubleSummaryStatistics zGyroscopeStats = this.zGyroscopes.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minZGyroscope = zGyroscopeStats.getMin();
        double maxZGyroscope = zGyroscopeStats.getMax();
        double avgZGyroscope = zGyroscopeStats.getAverage();
        double varZGyroscope = this.zGyroscopes.stream().map(i -> i - avgZGyroscope).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdZGyroscope = Math.sqrt(varZGyroscope);

        DoubleSummaryStatistics xOrientationStats = this.xOrientations.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minXOrientation = xOrientationStats.getMin();
        double maxXOrientation = xOrientationStats.getMax();
        double avgXOrientation = xOrientationStats.getAverage();
        double varXOrientation = this.xOrientations.stream().map(i -> i - avgXOrientation).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdXOrientation = Math.sqrt(varXOrientation);

        DoubleSummaryStatistics yOrientationStats = this.yOrientations.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYOrientation = yOrientationStats.getMin();
        double maxYOrientation = yOrientationStats.getMax();
        double avgYOrientation = yOrientationStats.getAverage();
        double varYOrientation = this.yOrientations.stream().map(i -> i - avgYOrientation).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYOrientation = Math.sqrt(varYOrientation);

        DoubleSummaryStatistics zOrientationStats = this.zOrientations.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minZOrientation = zOrientationStats.getMin();
        double maxZOrientation = zOrientationStats.getMax();
        double avgZOrientation = zOrientationStats.getAverage();
        double varZOrientation = this.zOrientations.stream().map(i -> i - avgZOrientation).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdZOrientation = Math.sqrt(varZOrientation);

        Swipe newSwipe = new Swipe();

        newSwipe.setDuration(duration);

        newSwipe.setLength(length);

        newSwipe.setMinSize(minSize);
        newSwipe.setMaxSize(maxSize);
        newSwipe.setAvgSize(avgSize);
        newSwipe.setDownSize(downSize);
        newSwipe.setUpSize(upSize);

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

        newSwipe.setMinXAccelerometer(minXAccelerometer);
        newSwipe.setMaxXAccelerometer(maxXAccelerometer);
        newSwipe.setAvgXAccelerometer(avgXAccelerometer);
        newSwipe.setStdXAccelerometer(stdXAccelerometer);
        newSwipe.setVarXAccelerometer(varXAccelerometer);

        newSwipe.setMinYAccelerometer(minYAccelerometer);
        newSwipe.setMaxYAccelerometer(maxYAccelerometer);
        newSwipe.setAvgYAccelerometer(avgYAccelerometer);
        newSwipe.setStdYAccelerometer(stdYAccelerometer);
        newSwipe.setVarYAccelerometer(varYAccelerometer);

        newSwipe.setMinZAccelerometer(minZAccelerometer);
        newSwipe.setMaxZAccelerometer(maxZAccelerometer);
        newSwipe.setAvgZAccelerometer(avgZAccelerometer);
        newSwipe.setStdZAccelerometer(stdZAccelerometer);
        newSwipe.setVarZAccelerometer(varZAccelerometer);

        newSwipe.setMinXGyroscope(minXGyroscope);
        newSwipe.setMaxXGyroscope(maxXGyroscope);
        newSwipe.setAvgXGyroscope(avgXGyroscope);
        newSwipe.setStdXGyroscope(stdXGyroscope);
        newSwipe.setVarXGyroscope(varXGyroscope);

        newSwipe.setMinYGyroscope(minYGyroscope);
        newSwipe.setMaxYGyroscope(maxYGyroscope);
        newSwipe.setAvgYGyroscope(avgYGyroscope);
        newSwipe.setStdYGyroscope(stdYGyroscope);
        newSwipe.setVarYGyroscope(varYGyroscope);

        newSwipe.setMinZGyroscope(minZGyroscope);
        newSwipe.setMaxZGyroscope(maxZGyroscope);
        newSwipe.setAvgZGyroscope(avgZGyroscope);
        newSwipe.setStdZGyroscope(stdZGyroscope);
        newSwipe.setVarZGyroscope(varZGyroscope);

        newSwipe.setMinXOrientation(minXOrientation);
        newSwipe.setMaxXOrientation(maxXOrientation);
        newSwipe.setAvgXOrientation(avgXOrientation);
        newSwipe.setStdXOrientation(stdXOrientation);
        newSwipe.setVarXOrientation(varXOrientation);

        newSwipe.setMinYOrientation(minYOrientation);
        newSwipe.setMaxYOrientation(maxYOrientation);
        newSwipe.setAvgYOrientation(avgYOrientation);
        newSwipe.setStdYOrientation(stdYOrientation);
        newSwipe.setVarYOrientation(varYOrientation);

        newSwipe.setMinZOrientation(minZOrientation);
        newSwipe.setMaxZOrientation(maxZOrientation);
        newSwipe.setAvgZOrientation(avgZOrientation);
        newSwipe.setStdZOrientation(stdZOrientation);
        newSwipe.setVarZOrientation(varZOrientation);

        if (this.isTrainingMode) {
            newSwipe.setHoldingPosition(this.holdingPosition);
        } else {
            newSwipe.setHoldingPosition(0);
        }

        newSwipe.setUserId(this.attackSwitch.isChecked() ? "Attacker" : "User");

        System.out.println("--------------------------------------------New Swipe---------------------------------------------------------------");
        System.out.println(newSwipe);
        return newSwipe;
    }

    public void disableUserInteraction() {
        this.trainButton.setEnabled(false);
        this.ganButton.setEnabled(false);
        this.profileButton.setEnabled(false);
        this.resetButton.setEnabled(false);
    }

    public void enableUserInteraction() {
        this.trainButton.setEnabled(true);
        this.ganButton.setEnabled(true);
        this.profileButton.setEnabled(true);
        this.resetButton.setEnabled(true);
    }

    public void editProfile(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose the profile you want to edit").setTitle("Choose profile");
        builder.setPositiveButton("User", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editUserProfile(view);
            }
        });
        builder.setNegativeButton("Model", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editModelProfile(view);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editUserProfile(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        hideMainActivity(popupWindow);

        ArrayList<String> userData = dbHelper.getUserData();

        EditText nicknameEditText = popupView.findViewById(R.id.nicknameEditText);
        nicknameEditText.setText(userData.get(0));

        RadioGroup genderRadioGroup = popupView.findViewById(R.id.genderRadioGroup);
        ArrayList<Integer> genderRadioGroupIndices = new ArrayList<Integer>();
        genderRadioGroupIndices.add(new Integer(R.id.genderNoneRadioButton));
        genderRadioGroupIndices.add(new Integer(R.id.genderMaleRadioButton));
        genderRadioGroupIndices.add(new Integer(R.id.genderFemaleRadioButton));
        int genderIdx = (int) Double.parseDouble(userData.get(1));
        genderRadioGroup.check(genderRadioGroupIndices.get(genderIdx));

        RadioGroup ageRadioGroup = popupView.findViewById(R.id.ageRadioGroup);
        ArrayList<Integer> ageRadioGroupIndices = new ArrayList<Integer>();
        ageRadioGroupIndices.add(new Integer(R.id.ageNoneRadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age20RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age40RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age60RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age80RadioButton));
        int ageIdx = (int) Double.parseDouble(userData.get(2));
        ageRadioGroup.check(ageRadioGroupIndices.get(ageIdx));

        EditText nationalityEditText = popupView.findViewById(R.id.nationalityEditText);
        nationalityEditText.setText(userData.get(3));

        RadioGroup holdingRadioGroup = popupView.findViewById(R.id.holdingRadioGroup);
        ArrayList<Integer> holdingRadioGroupIndices = new ArrayList<Integer>();
        holdingRadioGroupIndices.add(new Integer(R.id.holdingNoneRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingRightRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingLeftRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingBothRadioButton));
        int holdingIdx = (int) Double.parseDouble(userData.get(4));
        holdingRadioGroup.check(holdingRadioGroupIndices.get(holdingIdx));

        Button saveProfileButton = popupView.findViewById(R.id.saveProfileButton);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                int genderIndex = genderRadioGroupIndices.indexOf(new Integer(genderRadioGroup.getCheckedRadioButtonId()));
                int ageIndex = ageRadioGroupIndices.indexOf(new Integer(ageRadioGroup.getCheckedRadioButtonId()));
                String nationality = nationalityEditText.getText().toString();
                int holdingIndex = holdingRadioGroupIndices.lastIndexOf(new Integer(holdingRadioGroup.getCheckedRadioButtonId()));

                dbHelper.resetDB(false);
                dbHelper.saveUserData(nickname, genderIndex, ageIndex, nationality, holdingIndex);

                inputTextView.setText("Inputs 0 (min 5)"); // Forces UI refresh on main activity

                popupWindow.dismiss();
            }
        });

        Button cancelButton = popupView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void editModelProfile(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.model_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        hideMainActivity(popupWindow);

        ArrayList<Integer> featureData = dbHelper.getFeatureData();

        CheckBox accelerationCheckBox = popupView.findViewById(R.id.accelerationCheckBox);
        accelerationCheckBox.setChecked(featureData.get(0) == 1);

        CheckBox angularVelocityCheckBox = popupView.findViewById(R.id.angularVelocityCheckBox);
        angularVelocityCheckBox.setChecked(featureData.get(1) == 1);

        CheckBox orientationCheckBox = popupView.findViewById(R.id.orientationCheckBox);
        orientationCheckBox.setChecked(featureData.get(2) == 1);

        CheckBox swipeDurationCheckBox = popupView.findViewById(R.id.swipeDurationCheckBox);
        swipeDurationCheckBox.setChecked(featureData.get(3) == 1);

        CheckBox swipeShapeCheckBox = popupView.findViewById(R.id.swipeShapeCheckBox);
        swipeShapeCheckBox.setChecked(featureData.get(4) == 1);

        CheckBox swipeTouchSizeCheckBox = popupView.findViewById(R.id.swipeTouchSizeCheckBox);
        swipeTouchSizeCheckBox.setChecked(featureData.get(5) == 1);

        CheckBox swipeStartEndPosCheckBox = popupView.findViewById(R.id.swipeStartEndPosCheckBox);
        swipeStartEndPosCheckBox.setChecked(featureData.get(6) == 1);

        CheckBox swipeVelocityCheckBox = popupView.findViewById(R.id.swipeVelocityCheckBox);
        swipeVelocityCheckBox.setChecked(featureData.get(7) == 1);

        Button saveProfileButton = popupView.findViewById(R.id.saveProfileButton);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( accelerationCheckBox.isChecked() ||
                    angularVelocityCheckBox.isChecked() ||
                    orientationCheckBox.isChecked() ||
                    swipeDurationCheckBox.isChecked() ||
                    swipeShapeCheckBox.isChecked() ||
                    swipeTouchSizeCheckBox.isChecked() ||
                    swipeStartEndPosCheckBox.isChecked() ||
                    swipeVelocityCheckBox.isChecked()
                ) {
                    dbHelper.saveFeatureData(
                            accelerationCheckBox.isChecked() ? 1 : 0,
                            angularVelocityCheckBox.isChecked() ? 1 : 0,
                            orientationCheckBox.isChecked() ? 1 : 0,
                            swipeDurationCheckBox.isChecked() ? 1 : 0,
                            swipeShapeCheckBox.isChecked() ? 1 : 0,
                            swipeTouchSizeCheckBox.isChecked() ? 1 : 0,
                            swipeStartEndPosCheckBox.isChecked() ? 1 : 0,
                            swipeVelocityCheckBox.isChecked() ? 1 : 0
                    );

                    popupWindow.dismiss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please select at least one feature type").setTitle("No feature selected");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
                    builder.show();
                }
            }
        });

        Button cancelButton = popupView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void hideMainActivity(PopupWindow popupWindow) {
        this.profileButton.setVisibility(View.INVISIBLE);
        this.resetButton.setVisibility(View.INVISIBLE);
        this.inputTextView.setVisibility(View.INVISIBLE);
        this.trainButton.setVisibility(View.INVISIBLE);
        this.ganButton.setVisibility(View.INVISIBLE);
        this.swipeImageView.setVisibility(View.INVISIBLE);
        this.holdingPositionRadioGroup.setVisibility(View.INVISIBLE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                profileButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                inputTextView.setVisibility(View.VISIBLE);
                trainButton.setVisibility(View.VISIBLE);
                ganButton.setVisibility(View.VISIBLE);
                swipeImageView.setVisibility(View.VISIBLE);
                holdingPositionRadioGroup.setVisibility(View.VISIBLE);
            }
        });
    }

    public void resetData(View view) {
        if(this.isTrainingMode) {
            this.dbHelper.resetDB(false);
            this.inputTextView.setText("Inputs " + this.dbHelper.getRecordsCount("REAL_SWIPES") + " (min 5)");
        } else {
            String strSummary = "";
            strSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s %6$-9s %7$-9s %8$-9s", "Inputs", "TAR", "FRR", "TRR", "FAR", "Swipe", "Train", "Classifier");
            strSummary += "\n";

            ArrayList<Swipe> testSwipes = dbHelper.getAllSwipes("TEST_SWIPES");

            if (testSwipes.size() == 0) {
                this.showAlertDialog("ATTENTION", "You need to enter at least a swipe to test the authentication system");
                return;
            }

            ArrayList<double[]> userTestingData = this.dbHelper.getTestingData("User");
            ArrayList<double[]> attackerTestingData = this.dbHelper.getTestingData("Attacker");

            for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                int cur_idx = modelType.ordinal();

                System.out.println(Arrays.toString(attackerTestingData.stream().mapToDouble(x -> x[cur_idx]).toArray()));

                double instances = userTestingData.size() + attackerTestingData.size();
                double TAR = userTestingData.isEmpty() ? 0.0 : userTestingData.stream().mapToDouble(x -> x[cur_idx]).sum() / userTestingData.size() * 100;
                double FRR = userTestingData.isEmpty() ? 0.0 : (userTestingData.size() - (userTestingData.stream().mapToDouble(x -> x[cur_idx]).sum())) / userTestingData.size() * 100;
                double TRR = attackerTestingData.isEmpty() ? 0.0 : (attackerTestingData.stream().mapToDouble(x -> x[cur_idx]).sum()) / attackerTestingData.size() * 100;
                double FAR = attackerTestingData.isEmpty() ? 0.0 : (attackerTestingData.size() - (attackerTestingData.stream().mapToDouble(x -> x[cur_idx]).sum())) / attackerTestingData.size() * 100;
                double averageSwipeDuration = testSwipes.stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000.0;

                userTestingData.addAll(attackerTestingData);
                double avgTestTime = userTestingData.stream().mapToDouble(x -> x[cur_idx+3]).average().getAsDouble();

                int classifierSamples = this.dbHelper.getRecordsCount("REAL_SWIPES");
                this.dbHelper.saveTestResults(instances, TAR, FRR, TRR, FAR, averageSwipeDuration, avgTestTime, classifierSamples, modelType);

                if(modelType == DatabaseHelper.ModelType.FULL) {
                    strSummary += String.format("%1$-12s", String.format("%02.0f", instances));
                    strSummary += String.format("%1$-10s", String.format("%.1f", TAR));
                    strSummary += String.format("%1$-11s", String.format("%.1f", FRR));
                    strSummary += String.format("%1$-10s", String.format("%.1f", TRR));
                    strSummary += String.format("%1$-11s", String.format("%.1f", FAR));
                    strSummary += String.format("%1$-11s", String.format("%.3f", averageSwipeDuration));
                    strSummary += String.format("%1$-10s", String.format("%.3f", avgTestTime));
                    strSummary += String.format("%1$-10s", String.format("%02d", classifierSamples));
                    strSummary += "\n";
                }
            }

            this.showAlertDialog("TESTING RESULTS", strSummary);

            this.attackSwitch.setChecked(false);

            this.inputTextView.setText("Inputs " + this.dbHelper.getRecordsCount("REAL_SWIPES") + " (min 5)");
            this.ganButton.setVisibility(View.VISIBLE);
            this.trainButton.setVisibility(View.VISIBLE);
            this.saveButton.setVisibility(View.VISIBLE);
            this.attackSwitch.setVisibility(View.INVISIBLE);
            this.profileButton.setVisibility(View.VISIBLE);
            this.holdingPositionRadioGroup.setVisibility(View.VISIBLE);
            this.isTrainingMode = true;
            this.resetButton.setText("Reset DB");
        }
    }

    public void train(View view) {
        dbHelper.resetDB(true);

        this.isTrainingClassifier = true;
        this.disableUserInteraction();

        this.progressTextView.setText("Training classifier");
        this.train(false);

    }

    public void trainWithGAN(View view) {
        this.isTrainingClassifier = true;
        this.disableUserInteraction();

        this.progressTextView.setText("GAN epoch: 0 (out of " + NUM_EPOCHS + ")");
        this.train(true);
    }

    public void train(boolean isGanMode) {

        if (this.dbHelper.getRecordsCount("REAL_SWIPES") < 5) {
            this.showAlertDialog("ATTENTION", "You need to enter at least 5 swipes as input before training.");
            this.isTrainingClassifier = false;
            this.enableUserInteraction();
            return;
        }

        this.progressTextView.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.swipeImageView.setVisibility(View.INVISIBLE);
        this.saveButton.setVisibility(View.INVISIBLE);

        this.isTrainingMode = false;

        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable uiRunnable = () -> {
            this.inputTextView.setText("Swipe to authenticate");
            this.ganButton.setVisibility(View.INVISIBLE);
            this.trainButton.setVisibility(View.INVISIBLE);
            this.saveButton.setVisibility(View.INVISIBLE);
            this.resetButton.setText("Done");

            this.progressTextView.setVisibility(View.INVISIBLE);
            this.progressBar.setVisibility(View.INVISIBLE);
            this.swipeImageView.setVisibility(View.VISIBLE);
            this.attackSwitch.setVisibility(View.VISIBLE);
            this.attackSwitch.setChecked(false);
            this.profileButton.setVisibility(View.INVISIBLE);
            this.holdingPositionRadioGroup.setVisibility(View.INVISIBLE);
        };

        Runnable runnable = () -> {
            dbHelper.deleteRealResults();
            dbHelper.deleteGANData();
            dbHelper.deleteTestingData();
            dbHelper.deleteResourceData();

            ArrayList<Swipe> swipes = dbHelper.getAllSwipes("REAL_SWIPES");
            try {
                if (isGanMode) {
                    ResourceMonitor resourceMonitor = new ResourceMonitor();
                    resourceMonitor.start(
                            (ActivityManager) getSystemService(ACTIVITY_SERVICE),
                            (BatteryManager) getSystemService(BATTERY_SERVICE)
                    );

                    long ganStartTime = System.nanoTime();
                    ArrayList<Swipe> fakeSwipes = gan.getFakeSwipeSamples(swipes, swipes.size(), progressTextView);
                    dbHelper.addSwipesNormalized(swipes, "REAL_SWIPES_NORMALIZED");
                    dbHelper.addGANRecords(fakeSwipes);

                    swipes.addAll(fakeSwipes);
                    long ganEndTime = System.nanoTime();
                    double ganTime = (double) (ganEndTime - ganStartTime) / 1_000_000_000;

                    resourceMonitor.setTrainingTime(ganTime);
                    resourceMonitor.stop(dbHelper, "GAN");

                    for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                        uiHandler.post(() -> {progressTextView.setText("Training " + modelType.name() + " classifier");});
                        trainClassifierWith(swipes, true, ganTime, modelType); }
                } else {
                    for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                        uiHandler.post(() -> {progressTextView.setText("Training " + modelType.name() + " classifier");});
                        trainClassifierWith(swipes, false, 0.0, modelType); }
                }

                uiHandler.post(uiRunnable);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(runnable).start();

    }

    public void trainClassifierWith(ArrayList<Swipe> trainSwipes, boolean hasGan, double ganTime, DatabaseHelper.ModelType modelType) {
        Instances dataSet = this.getWekaDataset(modelType);

        for(int i=0 ; i < trainSwipes.size(); i++)
        {
            Instance newInstance = trainSwipes.get(i).getAsWekaInstance(dataSet,true, dbHelper, modelType);
            dataSet.add(newInstance);
        }

        String finalSummary = "";
        finalSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s", "Input", "TAR", "FRR", hasGan ? "GAN" : "Swipe", "Train");
        finalSummary += "\n";

        System.out.println("ARFF representation of Dataset");
        System.out.println(dataSet.toString());

        OneClassClassifier oneClassClassifier = new OneClassClassifier();
        try {
            String[] options = {"-num", "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0", "-nom", "weka.classifiers.meta.generators.NominalGenerator -S 1", "-trr", "0.001", "-tcl", "1", "-cvr", "10", "-cvf", "10.0", "-P", "0.5", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "100", "-num-slots", "1", "-K", "0", "-S", "1", "", "", "", "", "", "", "", ""};
            oneClassClassifier.setOptions(options);
            oneClassClassifier.setTargetClassLabel("User");

            ResourceMonitor resourceMonitor = new ResourceMonitor();
            resourceMonitor.start(
                    (ActivityManager) getSystemService(ACTIVITY_SERVICE),
                    (BatteryManager) getSystemService(BATTERY_SERVICE)
            );

            double rfStartTime = Debug.threadCpuTimeNanos();
            oneClassClassifier.buildClassifier(dataSet);
            double rfEndTime = Debug.threadCpuTimeNanos();
            double rfTrainingTime = (double) (rfEndTime - rfStartTime) / 1_000_000_000;

            resourceMonitor.setTrainingTime(rfTrainingTime);
            resourceMonitor.stop(dbHelper, modelType.name());

            Evaluation eTest = new Evaluation(dataSet);
            eTest.crossValidateModel(oneClassClassifier, dataSet, 5, new Random(1));

            this.oneClassClassifiers[modelType.ordinal()] = oneClassClassifier;

            double instances = eTest.numInstances();
            double TAR = eTest.pctCorrect();
            double FRR = eTest.pctUnclassified();

            double averageSwipeDuration = trainSwipes.subList(0, dataSet.size()).stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000;

            finalSummary += String.format("%1$-12s", String.format("%02.0f", instances));
            finalSummary += String.format("%1$-10s", String.format("%.1f", TAR));
            finalSummary += String.format("%1$-11s", String.format("%.1f", FRR));
            finalSummary += String.format("%1$-11s", String.format("%.3f", hasGan ? ganTime : averageSwipeDuration));
            finalSummary += String.format("%1$-10s", String.format("%.3f", rfTrainingTime));
            finalSummary += "\n";

           if(hasGan) {
               this.dbHelper.saveGANResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime, ganTime, trainSwipes.size(), modelType);
           } else {
               this.dbHelper.saveRealResults(instances, TAR, FRR, averageSwipeDuration, rfTrainingTime, trainSwipes.size(), modelType);
           }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(finalSummary);

        if(modelType == DatabaseHelper.ModelType.FULL) {
            this.isTrainingClassifier = false;

            final String strSummary = finalSummary;
            MainActivity context = this;

            runOnUiThread(() -> context.enableUserInteraction());
            runOnUiThread(() -> context.showAlertDialog("TRAINING RESULTS", strSummary));
        }
    }

    public synchronized void saveData(View view) {

        try {
            ArrayList<String> userData = dbHelper.getUserData();
            dbHelper.saveAllTablesAsCSV(getContentResolver(), getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + userData.get(0));
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

    public Instances getWekaDataset(DatabaseHelper.ModelType modelType) {
        ArrayList<Integer> featureData = dbHelper.getFeatureData();
        boolean useAcceleration = featureData.get(0) == 1;
        boolean useAngularVelocity = featureData.get(1) == 1;
        boolean useOrientation = featureData.get(2) == 1;
        boolean useSwipeDuration = featureData.get(3) == 1;
        boolean useSwipeShape = featureData.get(4) == 1;
        boolean useSwipeSize = featureData.get(5) == 1;
        boolean useSwipeStartEndPos = featureData.get(6) == 1;
        boolean useSwipeVelocity = featureData.get(7) == 1;

        ArrayList<Attribute> attributes = new ArrayList<>();

        if(modelType != DatabaseHelper.ModelType.HOLD) {
            if (useSwipeDuration) {
                attributes.add(new Attribute("duration"));
            }
            if (useSwipeShape) {
                attributes.add(new Attribute("length"));
            }
            if (useSwipeSize) {
                attributes.add(new Attribute("minSize"));
                attributes.add(new Attribute("maxSize"));
                attributes.add(new Attribute("avgSize"));
                attributes.add(new Attribute("downSize"));
                attributes.add(new Attribute("upSize"));
            }
            if (useSwipeStartEndPos) {
                attributes.add(new Attribute("startX"));
                attributes.add(new Attribute("startY"));
                attributes.add(new Attribute("endX"));
                attributes.add(new Attribute("endY"));
            }
            if (useSwipeVelocity) {
                attributes.add(new Attribute("minXVelocity"));
                attributes.add(new Attribute("maxXVelocity"));
                attributes.add(new Attribute("avgXVelocity"));
                attributes.add(new Attribute("stdXVelocity"));
                attributes.add(new Attribute("varXVelocity"));
                attributes.add(new Attribute("minYVelocity"));
                attributes.add(new Attribute("maxYVelocity"));
                attributes.add(new Attribute("avgYVelocity"));
                attributes.add(new Attribute("stdYVelocity"));
                attributes.add(new Attribute("varYVelocity"));
            }
        }
        if(modelType != DatabaseHelper.ModelType.SWIPE) {
            if (useAcceleration) {
                attributes.add(new Attribute("minXAccelerometer"));
                attributes.add(new Attribute("maxXAccelerometer"));
                attributes.add(new Attribute("avgXAccelerometer"));
                attributes.add(new Attribute("stdXAccelerometer"));
                attributes.add(new Attribute("varXAccelerometer"));
                attributes.add(new Attribute("minYAccelerometer"));
                attributes.add(new Attribute("maxYAccelerometer"));
                attributes.add(new Attribute("avgYAccelerometer"));
                attributes.add(new Attribute("stdYAccelerometer"));
                attributes.add(new Attribute("varYAccelerometer"));
                attributes.add(new Attribute("minZAccelerometer"));
                attributes.add(new Attribute("maxZAccelerometer"));
                attributes.add(new Attribute("avgZAccelerometer"));
                attributes.add(new Attribute("stdZAccelerometer"));
                attributes.add(new Attribute("varZAccelerometer"));
            }
            if (useAngularVelocity) {
                attributes.add(new Attribute("minXGyroscope"));
                attributes.add(new Attribute("maxXGyroscope"));
                attributes.add(new Attribute("avgXGyroscope"));
                attributes.add(new Attribute("stdXGyroscope"));
                attributes.add(new Attribute("varXGyroscope"));
                attributes.add(new Attribute("minYGyroscope"));
                attributes.add(new Attribute("maxYGyroscope"));
                attributes.add(new Attribute("avgYGyroscope"));
                attributes.add(new Attribute("stdYGyroscope"));
                attributes.add(new Attribute("varYGyroscope"));
                attributes.add(new Attribute("minZGyroscope"));
                attributes.add(new Attribute("maxZGyroscope"));
                attributes.add(new Attribute("avgZGyroscope"));
                attributes.add(new Attribute("stdZGyroscope"));
                attributes.add(new Attribute("varZGyroscope"));
            }
            if (useOrientation) {
                attributes.add(new Attribute("minXOrientation"));
                attributes.add(new Attribute("maxXOrientation"));
                attributes.add(new Attribute("avgXOrientation"));
                attributes.add(new Attribute("stdXOrientation"));
                attributes.add(new Attribute("varXOrientation"));
                attributes.add(new Attribute("minYOrientation"));
                attributes.add(new Attribute("maxYOrientation"));
                attributes.add(new Attribute("avgYOrientation"));
                attributes.add(new Attribute("stdYOrientation"));
                attributes.add(new Attribute("varYOrientation"));
                attributes.add(new Attribute("minZOrientation"));
                attributes.add(new Attribute("maxZOrientation"));
                attributes.add(new Attribute("avgZOrientation"));
                attributes.add(new Attribute("stdZOrientation"));
                attributes.add(new Attribute("varZOrientation"));
            }
        }

        ArrayList<String> myNominalValues = new ArrayList<>(2);
        myNominalValues.add("User");
        myNominalValues.add("Attacker");
        Attribute owner = new Attribute("owner", myNominalValues);
        attributes.add(owner);

        Instances dataSet = new Instances("swipes", attributes, 0);
        dataSet.setClass(owner);

        return dataSet;
    }

}