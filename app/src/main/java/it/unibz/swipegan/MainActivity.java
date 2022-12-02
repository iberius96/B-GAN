package it.unibz.swipegan;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static it.unibz.swipegan.GAN.NUM_EPOCHS;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * Swipe gesture star time.
     */
    private long startTime = 0;

    /**
     * Indicates whether the application is currently in training mode (i.e. Training interaction are being collected).
     */
    private boolean isTrainingMode = true;

    /**
     * Indicates whether the classifier training procedure is currently ongoing.
     */
    private boolean isTrainingClassifier = false;

    /**
     * Indicates whether the application is currently gathering a swipe gesture.
     */
    private boolean isTrackingSwipe = true;

    /**
     * Indicates whether the application is currently displaying SUS questions.
     */
    private boolean isTakingSUSQuestions = false;

    /**
     * Indicates whether existing test interactions from a previous testing phase need to be preserved.
     */
    private boolean keepTestSwipes;

    /**
     * Counter for the SUS question nr.
     */
    private int SUSQuestionNr = 1;

    /**
     * Identifier for the current holding position (1 = Sitting, 2 = Standing, 3 = Walking).
     */
    private int holdingPosition = 0;

    /**
     * Indicates the currently active gesture (one of SWIPE, KEYSTROKE, SIGNATURE)
     */
    private DatabaseHelper.ModelType currentGesture = DatabaseHelper.ModelType.SWIPE;

    /**
     * Set of X location values of the swipe gesture.
     */
    private ArrayList<Float> xLocations = null;

    /**
     * Set of Y location values of the swipe gesture.
     */
    private ArrayList<Float> yLocations = null;

    /**
     * Set of X velocity values of the swipe gesture.
     */
    private ArrayList<Float> xVelocityTranslation = null;

    /**
     * Set of Y velocity values of the swipe gesture.
     */
    private ArrayList<Float> yVelocityTranslation = null;

    /**
     * Set of touch sizes values of the swipe gesture.
     */
    private ArrayList<Float> sizes = null;

    /**
     * Full duration value of the swipe gesture.
     */
    private int duration = 0;

    /**
     * Full length value of the swipe gesture.
     */
    private double length = 0;

    /**
     * Indicates whether the accelerometer data is currently being collected.
     */
    private boolean isTrackingAccelerometer = false;

    /**
     * Set of X values recoded from the accelerometers sensor.
     */
    private ArrayList<Float> xAccelerometers = null;

    /**
     * Set of Y values recoded from the accelerometers sensor.
     */
    private ArrayList<Float> yAccelerometers = null;

    /**
     * Set of Z values recoded from the accelerometers sensor.
     */
    private ArrayList<Float> zAccelerometers = null;

    /**
     * Indicates whether the gyroscope data is currently being collected.
     */
    private boolean isTrackingGyroscope = false;

    /**
     * Set of X values recoded from the gyroscope sensor.
     */
    private ArrayList<Float> xGyroscopes = null;

    /**
     * Set of Y values recoded from the gyroscope sensor.
     */
    private ArrayList<Float> yGyroscopes = null;

    /**
     * Set of Z values recoded from the gyroscope sensor.
     */
    private ArrayList<Float> zGyroscopes = null;

    /**
     * Indicates whether the magnetometer data is currently being collected.
     */
    private boolean isTrackingMagnetometer = false;

    /**
     * Set of X orientation values.
     */
    private ArrayList<Float> xOrientations = null;

    /**
     * Set of Y orientation values.
     */
    private ArrayList<Float> yOrientations = null;

    /**
     * Set of Z orientation values.
     */
    private ArrayList<Float> zOrientations = null;

    private float[] mGravity;
    private float[] mGeomagnetic;

    /**
     * SensorManager object allowing access the device's sensors.
     */
    private SensorManager sensorManager;

    /**
     * Sensor object for the accelerometer.
     */
    private Sensor accelerometer;

    /**
     * Sensor object for the gyroscope.
     */
    private Sensor gyroscope;

    /**
     * Sensor object for the magnetometer.
     */
    private Sensor magnetometer;

    /**
     * DatabaseHelper object.
     */
    private DatabaseHelper dbHelper;

    /**
     * Set of generated classifiers.
     */
    private CustomOneClassClassifier oneClassClassifiers[];

    /**
     * Set of identifiers for the active models.
     */
    private List<List<DatabaseHelper.ModelType>> trainingModels;

    /**
     * GAN object.
     */
    private GAN gan;

    /**
     * GAN button.
     */
    private Button ganButton;

    /**
     * Train button.
     */
    private Button trainButton;

    /**
     * Save button.
     */
    private Button saveButton;

    /**
     * Reset button.
     */
    private Button resetButton;

    /**
     * Profile button.
     */
    private Button profileButton;

    /**
     * Text view for the interactions input.
     */
    private TextView inputTextView;

    /**
     * Text view for the training progress.
     */
    private TextView progressTextView;

    /**
     * Circular training progress bar.
     */
    private ProgressBar progressBar;

    /**
     * Swipe gesture image view.
     */
    private ImageView swipeImageView;

    /**
     * Testing attack switch.
     */
    private Switch attackSwitch;

    /**
     * Radio button for the sitting position.
     */
    private RadioButton sittingRadioButton;

    /**
     * Radio button for the standing position.
     */
    private RadioButton standingRadioButton;

    /**
     * Radio button for the walking position.
     */
    private RadioButton walkingRadioButton;

    /**
     * Radio button group for the body position.
     */
    private RadioGroup holdingPositionRadioGroup;

    private Button keystrokeButton0;
    private Button keystrokeButton1;
    private Button keystrokeButton2;
    private Button keystrokeButton3;
    private Button keystrokeButton4;
    private Button keystrokeButton5;
    private Button keystrokeButton6;
    private Button keystrokeButton7;
    private Button keystrokeButton8;
    private Button keystrokeButton9;

    /**
     * Next button to confirm the signature gesture.
     */
    private Button nextButton;

    /**
     * Clear button to delete the signature gesture.
     */
    private Button clearButton;

    /**
     * SignatureView object.
     */
    private SignatureView signatureView;

    /**
     * Counter for the keystroke digits.
     */
    private int keystrokeCount = 0;

    /**
     * Keystroke gesture start time.
     */
    private long keystrokeStartTime = 0;

    /**
     * Keystroke gesture end time.
     */
    private long keystrokeEndTime = 0;

    private double lastKeystrokeX = 0.0;
    private double lastKeystrokeY = 0.0;
    private double lastKeystrokeSize = 0.0;

    private Swipe pendingSwipe = null;

    private static final Integer NUMPAD_SIZE = 10;

    private RadioGroup SUSQuestionRadioGroup;
    private RadioButton SUSQuestionRadioButton1;
    private RadioButton SUSQuestionRadioButton2;
    private RadioButton SUSQuestionRadioButton3;
    private RadioButton SUSQuestionRadioButton4;
    private RadioButton SUSQuestionRadioButton5;
    private TextView SUSQuestionTextView;
    private TextView SUSQuestionDisagreeTextView;
    private TextView SUSQuestionAgreeTextView;

    private Integer[] SUSAnswers = new Integer[DatabaseHelper.DEFAULT_SUS_QUESTIONS];

    // Classifier options
    String m_DefaultNumericGenerator = "weka.classifiers.meta.generators.GaussianGenerator -S 1 -M 0.0 -SD 1.0"; //-num
    String m_DefaultNominalGenerator = "weka.classifiers.meta.generators.NominalGenerator -S 1"; //-nom
    String m_TargetRejectionRate = "0.001"; //-trr
    String m_TargetClassLabel = "1"; //-tcl
    String m_NumRepeats = "10"; //-cvr
    String m_PercentHeldout = "10.0"; //-cvf
    String m_ProportionGenerated = "0.5"; //-P
    String m_Seed = "1"; //-S

    RawDataCollector rawDataCollector;
    Map<DatabaseHelper.ModelType, Double> weightData = null;

    /**
     * Called upon the creation of the Activity, it sets up the basic UI elements (together, if necessary, with their event listeners).
     * Additionally, it initialises the DB Helper, raw data collector and GAN objects.
     * Finally, it initializes the lists that will hold the collected data (X / Y location and velocity and X / Y / Z acceleration, angular velocity and orientations) together with the sensors
     * objects (and corresponding listeners) that will allow for their collection
     *
     * @param savedInstanceState Reference to the bundle object.
     */
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
        this.SUSQuestionRadioGroup = findViewById(R.id.SUSQuestionRadioGroup);
        this.SUSQuestionRadioButton1 = findViewById(R.id.SUSQuestionradioButton1);
        this.SUSQuestionRadioButton2 = findViewById(R.id.SUSQuestionradioButton2);
        this.SUSQuestionRadioButton3 = findViewById(R.id.SUSQuestionradioButton3);
        this.SUSQuestionRadioButton4 = findViewById(R.id.SUSQuestionradioButton4);
        this.SUSQuestionRadioButton5 = findViewById(R.id.SUSQuestionradioButton5);
        this.SUSQuestionTextView = findViewById(R.id.SUSQuestionTextView);
        this.SUSQuestionDisagreeTextView = findViewById(R.id.SUSQuestionDisagreeTextView);
        this.SUSQuestionAgreeTextView = findViewById(R.id.SUSQuestionAgreeTextView);

        this.setNumpadVisibility(View.INVISIBLE);
        this.setKeystrokeButtonsEventListener();

        this.signatureView = findViewById(R.id.signature_view);
        this.signatureView.setMainActivity(this);

        this.nextButton = findViewById(R.id.nextButton);
        this.clearButton = findViewById(R.id.clearButton);
        this.setSignatureVisibility(View.INVISIBLE);

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
            int recordsCount = this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES);
            runOnUiThread(()-> inputTextView.setText(getString(R.string.inputs) + " " + recordsCount));
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

        this.rawDataCollector = new RawDataCollector();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        Integer segments = this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS);
        Integer pinLength = this.dbHelper.getFeatureData().get(DatabaseHelper.COL_KEYSTROKE) == 1 ? this.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH) : 0;
        Integer signatureSegments = this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 1 ? this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS) : 0;
        new Thread(() -> this.gan = new GAN(segments, pinLength, signatureSegments)).start();
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * Mandatory implementation of empty method required due to main activity class implementing the SensorEventListener.
     *
     * @param sensor The sensor.
     * @param accuracy The new accuracy of this sensor.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Called when there is a new sensor event.
     * Adds the current values for acceleration (Sensor.TYPE_ACCELEROMETER), angular velocity (Sensor.TYPE_GYROSCOPE) and orientation (Sensor.TYPE_ACCELEROMETER + Sensor.TYPE_MAGNETIC_FIELD).
     * If enabled in the model profile, starts the raw data collector if one is not already running and the training phase is currently undergoing.
     *
     * @param event The sensor event.
     */
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

        if(
                this.isTrainingMode &&
                this.dbHelper.getFeatureData().get(DatabaseHelper.COL_RAW_DATA) == 1 &&
                this.xOrientations.size() != 0 &&
                !rawDataCollector.isRunning()
        ) {
            rawDataCollector.start(this, dbHelper);
        }
    }

    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.5f * (input[i] - output[i]);
        }
        return output;
    }

    /**
     * Sets the boolean variables controlling device sensors tracking (Accelerometer / Gyroscope / Magnetometer).
     *
     * @param tracking The boolean value to set the tracking variables to.
     */
    public void setSensorsTracking(boolean tracking) {
        this.isTrackingAccelerometer = tracking;
        this.isTrackingGyroscope = tracking;
        this.isTrackingMagnetometer = tracking;
    }

    /**
     * Checks whether the device is currently tracking data from its sensors.
     *
     * @return True if the device is tracking data from all sensors (Accelerometer / Gyroscope / Magnetometer); False otherwise.
     */
    public boolean isTrackingSensors() {
        return  this.isTrackingAccelerometer == true &&
                this.isTrackingGyroscope == true &&
                this.isTrackingMagnetometer == true;
    }

    /**
     * Handles touch screen motion events.
     * When necessary, triggers the logic handling the processing of a given swipe gesture.
     * Immediately returns (with a call to the superclass) if the training procedure is currently undergoing.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isTrainingClassifier) {
            return super.onTouchEvent(event);
        }

        if(this.isTrackingSwipe) {
            this.handleSwipeEvent(event);
        }

        return super.onTouchEvent(event);
    }

    /**
     * Handles the data collection process (swipe length, duration, touch sizes, locations, velocities) of all swipe gestures.
     * With the start of the swipe gesture (MotionEvent.ACTION_DOWN), it resets the length and start time variables and enables sensors tracking.
     * On each subsequent call of onSensorChanged() during the swipe (MotionEvent.ACTION_MOVE), it computes and updates the X / Y velocity and length values.
     * Once the gesture is completed (MotionEvent.ACTION_UP), it finalizes the swipe duration and start / end position distance and executes the logic required to either:
     *  Adding the (train or test) swipe to the DB (if no other gestures are enabled).
     *  Triggering the logic required to switch to the subsequent gesture (Keystroke or Signature).
     *
     * @param event The motion event passed by the onTouchEvent() method.
     */
    private void handleSwipeEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // initialization step
                this.length = 0;

                this.startTime = System.currentTimeMillis();
                this.setSensorsTracking(true);

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
                        this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                        this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                        this.xLocations.add(newX);
                        this.yLocations.add(newY);
                    }
                }

                for (int p = 0; p < pointerCount; p++) {
                    float newX = event.getX(p);
                    float newY = event.getY(p);
                    this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                    this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                    this.xLocations.add(newX);
                    this.yLocations.add(newY);
                }

                this.sizes.add(event.getSize(pointerId));

                break;
            case MotionEvent.ACTION_UP:

                float newX = event.getX(pointerId);
                float newY = event.getY(pointerId);
                this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                this.xLocations.add(newX);
                this.yLocations.add(newY);

                this.duration = (int) (System.currentTimeMillis() - this.startTime);

                double distance = Math.sqrt(Math.pow(this.yLocations.get(this.yLocations.size() - 1) - this.yLocations.get(0), 2) + Math.pow(this.xLocations.get(this.xLocations.size() - 1) - this.xLocations.get(0), 2));
                if(distance > 250) {
                    Swipe swipe = this.getSwipe();
                    if(this.isTrainingMode) {
                        this.saveButton.setVisibility(View.INVISIBLE);

                        if(this.dbHelper.getFeatureData().get(DatabaseHelper.COL_KEYSTROKE) == 0 && this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 0) {
                            this.dbHelper.addTrainRecord(swipe);
                        } else {
                            this.attackSwitch.setEnabled(false);
                            this.pendingSwipe = swipe;
                        }

                        int recordsCount = this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES);
                        this.inputTextView.setText(getString(R.string.inputs) + " " + recordsCount);
                    } else {
                        if(this.dbHelper.getFeatureData().get(DatabaseHelper.COL_KEYSTROKE) == 0 && this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 0) {
                            this.processTestRecord(swipe);
                        } else {
                            this.attackSwitch.setEnabled(false);
                            this.pendingSwipe = swipe;
                        }
                    }

                    if(this.dbHelper.getFeatureData().get(DatabaseHelper.COL_KEYSTROKE) == 1) {
                        this.isTrackingSwipe = false;
                        this.resetKeystrokeValues();
                        this.currentGesture = DatabaseHelper.ModelType.KEYSTROKE;
                        this.setNumpadVisibility(View.VISIBLE);
                    } else if (this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 1) {
                        this.currentGesture = DatabaseHelper.ModelType.SIGNATURE;
                        this.setSignatureVisibility(View.VISIBLE);
                    }
                }

                if(this.dbHelper.getFeatureData().get(DatabaseHelper.COL_KEYSTROKE) == 0 && this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 0) {
                    this.resetSwipeValues();
                } else {
                    // Stop hold tracking until start of keystroke or signature gesture
                    this.setSensorsTracking(false);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                this.resetSwipeValues();
                break;
        }
    }

    /**
     * Retrieves the most recent available data related to a single raw data sample.
     * In this context, the method reads the latest data for:
     *  X / Y / Z values of Accelerometer / Gyroscope / Orientation.
     *  Screen touch size.
     *  X / Y touch coordinates values.
     *  X / Y velocity values.
     *  An ID value for the currently active gesture (0 = Swipe, 1 = Keystroke, 2 = Signature).
     *
     * @return The values of a single raw data entry in the form of an Hashmap with columns string names as keys.
     */
    public Map<String, Double> getRawData() {
        Map<String, Double> rawData = new HashMap<String, Double>();

        if(this.currentGesture.ordinal() == DatabaseHelper.ModelType.SIGNATURE.ordinal()) {
            rawData.put(DatabaseHelper.COL_SIZE, this.signatureView.getSizes().size() != 0 ? this.signatureView.getSizes().get(this.signatureView.getSizes().size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_X, this.signatureView.getXLocations().size() != 0 ? this.signatureView.getXLocations().get(this.signatureView.getXLocations().size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_Y, this.signatureView.getYLocations().size() != 0 ? this.signatureView.getYLocations().get(this.signatureView.getYLocations().size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_VELOCITY_X, this.signatureView.getXVelocityTranslations().size() != 0 ? this.signatureView.getXVelocityTranslations().get(this.signatureView.getXVelocityTranslations().size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_VELOCITY_Y, this.signatureView.getYVelocityTranslations().size() != 0 ? this.signatureView.getYVelocityTranslations().get(this.signatureView.getYVelocityTranslations().size() - 1) : 0.0);
        } else if(this.currentGesture.ordinal() == DatabaseHelper.ModelType.KEYSTROKE.ordinal()) {
            rawData.put(DatabaseHelper.COL_SIZE, this.lastKeystrokeSize);
            rawData.put(DatabaseHelper.COL_X, this.lastKeystrokeX);
            rawData.put(DatabaseHelper.COL_Y, this.lastKeystrokeY);
            rawData.put(DatabaseHelper.COL_VELOCITY_X, 0.0);
            rawData.put(DatabaseHelper.COL_VELOCITY_Y, 0.0);
        } else {
            rawData.put(DatabaseHelper.COL_SIZE, this.sizes.size() != 0 ? this.sizes.get(this.sizes.size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_X, this.xLocations.size() != 0 ? this.xLocations.get(this.xLocations.size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_Y, this.yLocations.size() != 0 ? this.yLocations.get(this.yLocations.size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_VELOCITY_X, this.xVelocityTranslation.size() != 0 ? this.xVelocityTranslation.get(this.xVelocityTranslation.size() - 1) : 0.0);
            rawData.put(DatabaseHelper.COL_VELOCITY_Y, this.yVelocityTranslation.size() != 0 ? this.yVelocityTranslation.get(this.yVelocityTranslation.size() - 1) : 0.0);
        }

        rawData.put(DatabaseHelper.COL_ACCELEROMETER_X, this.xAccelerometers.size() != 0 ? this.xAccelerometers.get(this.xAccelerometers.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_ACCELEROMETER_Y, this.yAccelerometers.size() != 0 ? this.yAccelerometers.get(this.yAccelerometers.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_ACCELEROMETER_Z, this.zAccelerometers.size() != 0 ? this.zAccelerometers.get(this.zAccelerometers.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_GYROSCOPE_X, this.xGyroscopes.size() != 0 ? this.xGyroscopes.get(this.xGyroscopes.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_GYROSCOPE_Y, this.yGyroscopes.size() != 0 ? this.yGyroscopes.get(this.yGyroscopes.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_GYROSCOPE_Z, this.zGyroscopes.size() != 0 ? this.zGyroscopes.get(this.zGyroscopes.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_ORIENTATION_X, this.xOrientations.size() != 0 ? this.xOrientations.get(this.xOrientations.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_ORIENTATION_Y, this.yOrientations.size() != 0 ? this.yOrientations.get(this.yOrientations.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_ORIENTATION_Z, this.zOrientations.size() != 0 ? this.zOrientations.get(this.zOrientations.size() - 1) : 0.0);
        rawData.put(DatabaseHelper.COL_GESTURE_ID, (double) this.currentGesture.ordinal());

        return rawData;
    }

    /**
     * Generates the authentication results (and processing times) associated with executing the given test interaction against all active models.
     * Model predictions for the given interaction are generated by calling the .getPredictionFrom() method.
     * A correct classification will receive an authentication value of 1 (as opposed to a value of 0 for a misclassification).
     * The method also handles the logic related to obtaining a classification result from the weighted ensemble model.
     * In this context, all individual models (Hold, Swipe, Keystroke, Signature) are queried against the given interaction.
     * The final classification is obtained by weighting the results obtained from the individual models.
     * (Details of the weight calculation are provided in DatabaseHelper.getModelWeights())
     * Additionally, the method visually outputs a result message (ACCEPTED / REJECTED) which represents the classification results of the weighted ensemble (or the full model if the WE is not an active model).
     * Finally, the method adds the test record and associated authentication results to the DB.
     *
     * @param swipe The interaction to be tested against all active models.
     */
    private void processTestRecord(Swipe swipe) {
        String outputMessage = "";
        outputMessage += String.format("%1$-15s %2$-16s %3$-18s", getString(R.string.inputs), getString(R.string.prediction), getString(R.string.test_time));
        outputMessage += "\n";

        double[] authenticationValues;
        double[] authenticationTimes;
        if(dbHelper.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS) == DatabaseHelper.ModelsCombinations.FULL.ordinal()) {
            authenticationValues = new double[this.trainingModels.size()];
            authenticationTimes = new double[this.trainingModels.size()];
        } else {
            authenticationValues = new double[this.trainingModels.size() + 1];
            authenticationTimes = new double[this.trainingModels.size() + 1];
        }

        for(List<DatabaseHelper.ModelType> model : this.trainingModels) {
            if(!dbHelper.isModelEnabled(model)) {
                continue;
            }

            long startTime = System.nanoTime();
            double prediction = this.getPredictionFrom(swipe, model);
            long endTime = System.nanoTime();
            double testingTime = (double) (endTime - startTime) / 1_000_000_000;

            double authenticationValue;
            if (swipe.getUserId().equals("Attacker")) {
                authenticationValue = prediction != 0.0 ? 1.0 : 0.0;
            } else {
                authenticationValue = prediction == 0.0 ? 1.0 : 0.0;
            }

            authenticationValues[this.trainingModels.indexOf(model)] = authenticationValue;
            authenticationTimes[this.trainingModels.indexOf(model)] = testingTime;

            outputMessage += String.format("%1$-18s", String.format("%02d", this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES)));
            outputMessage += String.format("%1$-18s", prediction == 0.0 ? getString(R.string.accepted) : getString(R.string.rejected));
            outputMessage += String.format("%1$-18s", String.format("%.4f", testingTime));
            outputMessage += "\n";
        }

        double prediction;
        if(dbHelper.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS) == DatabaseHelper.ModelsCombinations.FULL.ordinal()) { // Use full model
            prediction = this.getPredictionFrom(swipe, Arrays.asList(DatabaseHelper.ModelType.FULL));
        } else { // Use weighted ensemble
            Map<DatabaseHelper.ModelType, Double> weighted_predictions = new HashMap<>();

            long startTime = System.nanoTime();
            for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                if(modelType == DatabaseHelper.ModelType.FULL || !dbHelper.isModelEnabled(Arrays.asList(modelType))) { continue; }

                double cur_prediction = this.getPredictionFrom(swipe, Arrays.asList(modelType)) == 0.0 ? 1.0 : 0.0;
                weighted_predictions.put(modelType, cur_prediction * this.weightData.get(modelType));
            }

            prediction = weighted_predictions.values().stream().mapToDouble(d-> d).sum() >= 0.5 ? 0.0 : 1.0;
            long endTime = System.nanoTime();
            double testingTime = (double) (endTime - startTime) / 1_000_000_000;

            double authenticationValue;
            if (swipe.getUserId().equals("Attacker")) {
                authenticationValue = prediction != 0.0 ? 1.0 : 0.0;
            } else {
                authenticationValue = prediction == 0.0 ? 1.0 : 0.0;
            }

            authenticationValues[authenticationValues.length - 1] = authenticationValue;
            authenticationTimes[authenticationTimes.length - 1] = testingTime;
        }

        if (prediction == 0.0) {
            this.showSnackBar(getString(R.string.authentication) + ": " + getString(R.string.accepted), "#238823");
        } else {
            this.showSnackBar(getString(R.string.authentication) + ": " + getString(R.string.rejected), "#D2222D");
        }

        swipe.setAuthentication(authenticationValues);
        swipe.setAuthenticationTime(authenticationTimes);
        swipe.setClassifierSamples(this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES));
        this.dbHelper.addTestRecord(swipe);

        int recordsCount = this.dbHelper.getRecordsCount(DatabaseHelper.TEST_SWIPES);
        this.inputTextView.setText(getString(R.string.test_inputs) + " " + recordsCount);

        this.attackSwitch.setEnabled(true); // Re-enable attack toggle
    }

    /**
     * Empties the DB tables containing test data and re-processes all the currently stored test interactions.
     * Called when test interactions are kept from a previous testing session and need to be re-evaluate against a different set of models.
     *
     * @param testSwipes The set of test interactions.
     */
    private void reProcessTestRecords(ArrayList<Swipe> testSwipes) {
        this.dbHelper.deleteTestingData();

        for(Swipe testSwipe : testSwipes) {
            this.processTestRecord(testSwipe);
        }
    }

    /**
     * Gets the classification result of a given interaction against the specified model.
     * Predictions are obtained by calling the OneClassClassifier.classifyInstance() method and passing the weka.core.Instance object generated from the given interaction.
     * Note that .classifyInstances() returns the index of the most likely class identified (NaN if neither of the available classes were identified).
     *
     * @param swipe The interaction that will be tested against the model.
     * @param modelType The identifier of the model that needs to perform the classification.
     * @return Classification results for the given interaction. Interactions classified as genuine return a prediction value of 0.
     */
    private double getPredictionFrom(Swipe swipe, List<DatabaseHelper.ModelType> modelType) {
        Instance instance = swipe.getAsWekaInstance(this.getWekaDataset(modelType), false, dbHelper, modelType);
        instance.setDataset(this.getWekaDataset(modelType));

        double prediction = 0.0;

        try {
            prediction = this.oneClassClassifiers[this.trainingModels.indexOf(modelType)].classifyInstance(instance);
            System.out.println(getString(R.string.prediction) + ": " + prediction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }

    /**
     * Stops the raw data collector, the device sensors tracking and empties the locally stored interactions values.
     * This includes:
     *  X / Y / Z values of Accelerometer / Gyroscope / Orientation.
     *  Screen touch size.
     *  X / Y touch coordinates values.
     *  X / Y velocity values.
     */
    private void resetSwipeValues() {
        if(rawDataCollector.isRunning()) {
            rawDataCollector.stop();
        }

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

        this.setSensorsTracking(false);
    }

    /**
     * Gathers all data related to the provided interactions and generates the corresponding Swipe object.
     *
     * @return The newly generated Swipe object representing the current interaction.
     */
    public Swipe getSwipe() {
        double duration = this.duration;

        double length = this.length;

        double[] segmentsX = this.getSegmentsOffset(this.xLocations, this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS));
        double[] segmentsY = this.getSegmentsOffset(this.yLocations, this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS));

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

        DoubleSummaryStatistics yVelocityStats = this.yVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
        double minYVelocity = yVelocityStats.getMin();
        double maxYVelocity = yVelocityStats.getMax();
        double avgYVelocity = yVelocityStats.getAverage();
        double varYVelocity = this.yVelocityTranslation.stream().map(i -> i - avgYVelocity).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
        double stdYVelocity = Math.sqrt(varYVelocity);

        Swipe newSwipe = new Swipe();

        newSwipe.setDuration(duration);

        newSwipe.setLength(length);

        newSwipe.setSegmentsX(segmentsX);
        newSwipe.setSegmentsY(segmentsY);

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

        this.setHoldFeatures(newSwipe);

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

    /**
     * Retrieves and set the Hold related features for a new Swipe object that is currently being generated.
     * These features include Min / Max / Avg / Var / Std X / Y / Z values for Accelerometer / Gyroscope / Orientation
     *
     * @param swipe The swipe object that is currently being created.
     */
    public void setHoldFeatures(Swipe swipe) {
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

        swipe.setMinXAccelerometer(minXAccelerometer);
        swipe.setMaxXAccelerometer(maxXAccelerometer);
        swipe.setAvgXAccelerometer(avgXAccelerometer);
        swipe.setStdXAccelerometer(stdXAccelerometer);
        swipe.setVarXAccelerometer(varXAccelerometer);

        swipe.setMinYAccelerometer(minYAccelerometer);
        swipe.setMaxYAccelerometer(maxYAccelerometer);
        swipe.setAvgYAccelerometer(avgYAccelerometer);
        swipe.setStdYAccelerometer(stdYAccelerometer);
        swipe.setVarYAccelerometer(varYAccelerometer);

        swipe.setMinZAccelerometer(minZAccelerometer);
        swipe.setMaxZAccelerometer(maxZAccelerometer);
        swipe.setAvgZAccelerometer(avgZAccelerometer);
        swipe.setStdZAccelerometer(stdZAccelerometer);
        swipe.setVarZAccelerometer(varZAccelerometer);

        swipe.setMinXGyroscope(minXGyroscope);
        swipe.setMaxXGyroscope(maxXGyroscope);
        swipe.setAvgXGyroscope(avgXGyroscope);
        swipe.setStdXGyroscope(stdXGyroscope);
        swipe.setVarXGyroscope(varXGyroscope);

        swipe.setMinYGyroscope(minYGyroscope);
        swipe.setMaxYGyroscope(maxYGyroscope);
        swipe.setAvgYGyroscope(avgYGyroscope);
        swipe.setStdYGyroscope(stdYGyroscope);
        swipe.setVarYGyroscope(varYGyroscope);

        swipe.setMinZGyroscope(minZGyroscope);
        swipe.setMaxZGyroscope(maxZGyroscope);
        swipe.setAvgZGyroscope(avgZGyroscope);
        swipe.setStdZGyroscope(stdZGyroscope);
        swipe.setVarZGyroscope(varZGyroscope);

        swipe.setMinXOrientation(minXOrientation);
        swipe.setMaxXOrientation(maxXOrientation);
        swipe.setAvgXOrientation(avgXOrientation);
        swipe.setStdXOrientation(stdXOrientation);
        swipe.setVarXOrientation(varXOrientation);

        swipe.setMinYOrientation(minYOrientation);
        swipe.setMaxYOrientation(maxYOrientation);
        swipe.setAvgYOrientation(avgYOrientation);
        swipe.setStdYOrientation(stdYOrientation);
        swipe.setVarYOrientation(varYOrientation);

        swipe.setMinZOrientation(minZOrientation);
        swipe.setMaxZOrientation(maxZOrientation);
        swipe.setAvgZOrientation(avgZOrientation);
        swipe.setStdZOrientation(stdZOrientation);
        swipe.setVarZOrientation(varZOrientation);
    }

    /**
     * Computes the set of segment sizes (provided in percentage values between 0-1 over the full gesture length) starting from the provided locations.
     *
     * Firstly, it checks whether the specified number of segments can be generated given the available set of gesture locations.
     * (Note: If that is not the case, the maximum number of segments will be generated instead. This means that for a gesture with X locations, X-1 segments will be generated).
     * Then, the interval size of an individual segment is computed ((nr. of locations - 1) / nr of segments).
     * This interval size is computed in relation to the total nr of provided locations and will instruct the system on how to select the anchor points that will define the start / end of a given segment.
     * These anchor locations are then used to compute the size of each segment with regards to the total size of the gesture (for the provided dimension).
     *
     * @param locations The full set of (X or Y) locations of points belonging to a Swipe or Signature gesture.
     * @param segments The number of segments to divide the gesture into.
     * @return The set of segment sizes provided in percentage values (between 0-1) over the full size of the gesture for the given dimension.
     */
    public double[] getSegmentsOffset(ArrayList<Float> locations, Integer segments) {
        Integer collectable_segments = segments + 1 > locations.size() ? locations.size() - 1 : segments;

        float interval_size = (float) (locations.size() - 1) / collectable_segments;
        Integer[] locs_idx = new Integer[collectable_segments + 1];
        float curr_loc = 0;
        for(int i = 0; i < locs_idx.length; i++) {
            locs_idx[i] = Math.round(curr_loc);
            curr_loc += interval_size;
        }

        ArrayList<Float> segments_locs = new ArrayList<>();
        for(Integer idx : locs_idx) { segments_locs.add(locations.get(idx)); }

        double[] ret = new double[segments];
        for(int i = 0; i < collectable_segments; i++) {
            ret[i] = (segments_locs.get(i + 1) - segments_locs.get(i)) / Math.abs((segments_locs.get(segments_locs.size() - 1) - segments_locs.get(0)));
        }

        return ret;
    }

    /**
     * Disables Train / GAN / Profile / Reset DB buttons
     */
    public void disableUserInteraction() {
        this.trainButton.setEnabled(false);
        this.ganButton.setEnabled(false);
        this.profileButton.setEnabled(false);
        this.resetButton.setEnabled(false);
    }

    /**
     * Enables Train / GAN / Profile / Reset DB buttons
     */
    public void enableUserInteraction() {
        this.trainButton.setEnabled(true);
        this.ganButton.setEnabled(true);
        this.profileButton.setEnabled(true);
        this.resetButton.setEnabled(true);
    }

    /**
     * Builds the edit profile alert dialog.
     *
     * @param view The view that was clicked.
     */
    public void editProfile(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.choose_profile_edit)).setTitle(getString(R.string.choose_profile));
        builder.setPositiveButton(getString(R.string.user), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switchActivities(ProfileActivity.class);
            }
        });
        builder.setNegativeButton(getString(R.string.model), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switchActivities(ModelActivity.class);
            }
        });
        builder.setNeutralButton(getString(R.string.cancel_lower), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Triggers the process allowing the application to switch to either the User (class ProfileActivity) or Model (class ModelActivity) profile screen.
     *
     * @param targetActivity The activity (either ProfileActivity or ModelActivity) to switch to.
     */
    private void switchActivities(Class targetActivity) {
        Intent switchActivityIntent = new Intent(this, targetActivity);
        startActivityForResult(switchActivityIntent, targetActivity == ModelActivity.class ? 0 : 1); // TODO: Refactor request code
    }

    /**
     * Collects data upon closure of the User (class ProfileActivity) or Model (class ModelActivity) profile screen.
     * The method is called when a launched activity exits, giving the requestCode with which it was started, the resultCode it returned, and any additional data from it.
     * When the Model activity is closed with result code Activity.RESULT_OK, the system extracts from the intent data the current and initial selections for all contained settings.
     * Additionally, the DB is reset if:
     *  The keystroke gesture or signature gesture are enabled / disabled.
     *  The value for the collection frequency of the raw data, the number of swipe segments, the pin length or the number of signature segments is changed.
     * This is done because changes to this settings might create a mismatch with data already present in the DB.
     * Here is important to note that disabling only certain features from a given model (apart from the ones mentioned above) won't cause a DB reset.
     * This is due to the fact that the data related to a disabled feature is still collected from the interaction but not used during training.
     *
     * When the User profile activity is closed with result code Activity.RESULT_OK, a DB reset is triggered.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult() (0 for ModelActivity and 1 for ProfileActivity).
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0): { // Model activity
                if (resultCode == Activity.RESULT_OK) {
                    Map<String, Object> modelSelection = (HashMap<String, Object>) data.getSerializableExtra("modelSelection");

                    List<List<DatabaseHelper.ModelType>> curActiveModels = dbHelper.getActiveModels().stream().filter(s -> dbHelper.isModelEnabled(s)).collect(Collectors.toList());
                    List<List<DatabaseHelper.ModelType>> initialActiveModels = (List<List<DatabaseHelper.ModelType>>) modelSelection.get("initialActiveModels");

                    Integer curModelsSelection = (Integer) modelSelection.get("curModelsSelection");
                    Integer initialModelsSelection = (Integer) modelSelection.get("initialModelsSelection");

                    boolean curRawDataEnabled = (boolean) modelSelection.get("curRawDataEnabled");
                    boolean initialRawDataEnabled = (boolean) modelSelection.get("initialRawDataEnabled");
                    Integer curRawDataFrequency = (Integer) modelSelection.get("curRawDataFrequency");
                    Integer initialRawDataFrequency = (Integer) modelSelection.get("initialRawDataFrequency");

                    Integer curSegmentSelection = (Integer) modelSelection.get("curSegmentSelection");
                    Integer initialSegmentSelection = (Integer) modelSelection.get("initialSegmentSelection");

                    boolean curKeystrokeEnabled = (boolean) modelSelection.get("curKeystrokeEnabled");
                    boolean initialKeystrokeEnabled = (boolean) modelSelection.get("initialKeystrokeEnabled");
                    Integer curPinLength = (Integer) modelSelection.get("curPinLength");
                    Integer initialPinLength = (Integer) modelSelection.get("initialPinLength");

                    boolean curSignatureEnabled = (boolean) modelSelection.get("curSignatureEnabled");
                    boolean initialSignatureEnabled = (boolean) modelSelection.get("initialSignatureEnabled");
                    Integer curSignatureSegmentSelection = (Integer) modelSelection.get("curSignatureSegmentSelection");
                    Integer initialSignatureSegmentSelection = (Integer) modelSelection.get("initialSignatureSegmentSelection");

                    if(
                            (curModelsSelection != initialModelsSelection) ||
                            (!curActiveModels.equals(initialActiveModels))
                    ) {
                        dbHelper.generateTestAuthenticationTable(null, true, curActiveModels);
                    }

                    if(
                            (curRawDataEnabled != initialRawDataEnabled) ||
                            (curRawDataFrequency != initialRawDataFrequency) ||
                            (curSegmentSelection != initialSegmentSelection) ||
                            (curKeystrokeEnabled != initialKeystrokeEnabled) ||
                            (curPinLength != initialPinLength) ||
                            (curSignatureEnabled != initialSignatureEnabled) ||
                            (curSignatureSegmentSelection != initialSignatureSegmentSelection)
                    ) {
                        dbHelper.resetDB(false);
                        inputTextView.setText("Inputs 0");
                        new Thread(() -> this.gan = new GAN(
                                curSegmentSelection,
                                curKeystrokeEnabled ? curPinLength : 0,
                                curSignatureEnabled ? curSignatureSegmentSelection : 0)).start();

                        if(curKeystrokeEnabled != initialKeystrokeEnabled || curSignatureEnabled != initialSignatureEnabled) {
                            dbHelper.generateSwipesTables(null, true, curKeystrokeEnabled, curSignatureEnabled);
                        }
                    }
                }
                break;
            }
            case(1): {
                if (resultCode == Activity.RESULT_OK) {
                    dbHelper.resetDB(false);
                    inputTextView.setText(getString(R.string.inputs) + " 0");
                }
                break;
            }
        }
    }

    /**
     * Sets the visibility of the Numpad UI elements.
     *
     * @param visibility The visibility of the Numpad UI elements, one of VISIBLE, INVISIBLE or GONE.
     */
    private void setNumpadVisibility(Integer visibility) {
        this.setAccessoryUI(visibility);

        for(int i = 0; i < NUMPAD_SIZE; i++) {
            try {
                Field class_var = this.getClass().getDeclaredField("keystrokeButton" + String.valueOf(i));
                int res_id = getResources().getIdentifier("keystrokeButton" + String.valueOf(i), "id", this.getPackageName());
                class_var.set(this, findViewById(res_id));

                Method cur_method = class_var.getType().getMethod("setVisibility", int.class);
                cur_method.invoke(class_var.get(this), visibility);
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        //Reset keystroke values
        if(visibility == View.INVISIBLE) {
            this.lastKeystrokeX = 0.0;
            this.lastKeystrokeY = 0.0;
            this.lastKeystrokeSize = 0.0;
        }
    }

    /**
     * Sets the OnTouchListener for the Numpad buttons.
     */
    private void setKeystrokeButtonsEventListener() {
        for(int i = 0; i < NUMPAD_SIZE; i++) {
            try {
                Field class_var = this.getClass().getDeclaredField("keystrokeButton" + String.valueOf(i));

                Method cur_method = class_var.getType().getMethod("setOnTouchListener", View.OnTouchListener.class);
                cur_method.invoke(class_var.get(this), new KeystrokeOnTouchListener(this));
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The OnTouchListener used for the Numpad buttons.
     */
    class KeystrokeOnTouchListener implements View.OnTouchListener {
        /**
         * Stores the main activity object.
         */
        private MainActivity mainActivity;

        /**
         * Constructor of the OnTouchListener.
         * Initializes the local main activity object.
         *
         * @param mainActivity The instance of the main activity object.
         */
        public KeystrokeOnTouchListener(MainActivity mainActivity) {
            super();
            this.mainActivity = mainActivity;
        }

        /**
         * Called when a touch event is dispatched to a view. This allows listeners to get a chance to respond before the target view.
         * When a Numpad button is pressed (MotionEvent.ACTION_DOWN):
         *  Enables the device sensor tracking if the current digit is the first one of the keystroke gesture.
         *  Records the keystroke digit start time.
         *  Saves the intervals from the current start time to the previous start and end times if the current digit is not the first one of the keystroke gesture.
         *  Saves the touch X / Y position and touch size.
         * When a Numpad button is released (MotionEvent.ACTION_UP):
         *  Records the keystroke digit end time.
         *  Saves the intervals from the current end time to the previous end time if the current digit is not the first one of the keystroke gesture.
         *  Computes and saves the total duration of the current keystroke digit.
         *  Increases the keystroke digit count.
         * Additionally, when a Numpad button is released and the last keystroke digit has been reached, the full duration of the keystroke gesture is computed and saved.
         * At this point, the logic for switching to the Signature gesture or to save / process the train / test record is triggered (if the Signature gesture is currently disabled).
         *
         * @param v The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int index = event.getActionIndex();
            int pointerId = event.getPointerId(index);

            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(this.mainActivity.keystrokeCount == 0) { this.mainActivity.setSensorsTracking(true); }

                    long prevStartTime = this.mainActivity.keystrokeStartTime;
                    this.mainActivity.keystrokeStartTime = System.nanoTime();

                    if(prevStartTime != 0) {
                        double keystrokeStartInterval = (double) (this.mainActivity.keystrokeStartTime - prevStartTime) / 1_000_000_000;
                        this.mainActivity.pendingSwipe.addKeystrokeStartInterval(keystrokeStartInterval, this.mainActivity.keystrokeCount - 1, this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH));
                    }

                    if(this.mainActivity.keystrokeCount != 0) {
                        double keystrokeInterval = (double) (this.mainActivity.keystrokeStartTime - this.mainActivity.keystrokeEndTime) / 1_000_000_000;
                        this.mainActivity.pendingSwipe.addKeystrokeInterval(keystrokeInterval, this.mainActivity.keystrokeCount - 1, this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH));
                    }

                    this.mainActivity.lastKeystrokeX = event.getX(pointerId);
                    this.mainActivity.lastKeystrokeY = event.getY(pointerId);
                    this.mainActivity.lastKeystrokeSize = event.getSize(pointerId);

                    break;

                case MotionEvent.ACTION_UP:
                    long prevEndTime = this.mainActivity.keystrokeEndTime;
                    this.mainActivity.keystrokeEndTime = System.nanoTime();

                    if(prevEndTime != 0) {
                        double keystrokeEndInterval = (double) (this.mainActivity.keystrokeEndTime - prevEndTime) / 1_000_000_000;
                        this.mainActivity.pendingSwipe.addKeystrokeEndInterval(keystrokeEndInterval, this.mainActivity.keystrokeCount - 1, this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH));
                    }

                    double keystrokeDuration = (double) (this.mainActivity.keystrokeEndTime - this.mainActivity.keystrokeStartTime) / 1_000_000_000;
                    this.mainActivity.pendingSwipe.addKeystrokeDuration(keystrokeDuration, this.mainActivity.keystrokeCount, this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH));

                    this.mainActivity.keystrokeCount += 1;

                    if(this.mainActivity.keystrokeCount == this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH)) {
                        this.mainActivity.pendingSwipe.setKeystrokeFullDuration();
                        this.mainActivity.setNumpadVisibility(View.INVISIBLE);

                        if(this.mainActivity.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE) == 1) {
                            this.mainActivity.setSensorsTracking(false);
                            this.mainActivity.currentGesture = DatabaseHelper.ModelType.SIGNATURE;
                            this.mainActivity.setSignatureVisibility(View.VISIBLE);
                        } else {
                            this.mainActivity.setHoldFeatures(this.mainActivity.pendingSwipe); // Finalize hold features

                            this.mainActivity.resetSwipeValues();
                            this.mainActivity.isTrackingSwipe = true;

                            if(this.mainActivity.isTrainingMode) {
                                this.mainActivity.dbHelper.addTrainRecord(this.mainActivity.pendingSwipe);
                                this.mainActivity.inputTextView.setText("Inputs " + this.mainActivity.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES));
                            } else {
                                this.mainActivity.processTestRecord(this.mainActivity.pendingSwipe);
                            }
                        }
                    }
                    break;
            }

            return true;
        }
    }

    /**
     * Resets the keystroke digit count, start and end times.
     */
    private void resetKeystrokeValues() {
        this.keystrokeCount = 0;
        this.keystrokeStartTime = 0;
        this.keystrokeEndTime = 0;
    }

    /**
     * Sets the visibility of the signature UI elements.
     *
     * @param visibility The visibility of the Signature UI elements, one of VISIBLE, INVISIBLE or GONE.
     */
    private void setSignatureVisibility(Integer visibility) {
        this.setAccessoryUI(visibility);

        this.signatureView.setVisibility(visibility);
        this.nextButton.setVisibility(visibility);
        this.clearButton.setVisibility(visibility);
    }

    /**
     * It ends the signature gesture or triggers the next SUS question.
     * If the signature gesture is currently being recorder, the method finalizes the recorded signature data, clears the drawn signature and disables the signature view.
     * On the other end, if the SUS question are currently being answered, the method saves the current answer and moves to the next question.
     * @param view The view that was clicked.
     */
    public synchronized void nextInput(View view) {
        if(this.isTakingSUSQuestions) {
            if(SUSQuestionRadioGroup.getCheckedRadioButtonId() != -1) {
                this.getSUSAnswer();
                this.SUSQuestionNr += 1;
                this.SUSQuestionTextView.setText(getResources().getString(getResources().getIdentifier("SUS_" + this.SUSQuestionNr, "string", getPackageName())));

                if(this.SUSQuestionNr == DatabaseHelper.DEFAULT_SUS_QUESTIONS) {
                    this.nextButton.setVisibility(View.INVISIBLE);
                    this.resetButton.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if(this.signatureView.getXLocations().size() != 0) {
                this.setHoldFeatures(this.pendingSwipe); // Finalize hold features

                ArrayList<Float> signatureXLocations = this.signatureView.getXLocations();
                ArrayList<Float> signatureYLocations = this.signatureView.getYLocations();

                this.pendingSwipe.setSignatureStartX(signatureXLocations.get(0));
                this.pendingSwipe.setSignatureStartY(signatureYLocations.get(0));
                this.pendingSwipe.setSignatureEndX(signatureXLocations.get(signatureXLocations.size() - 1));
                this.pendingSwipe.setSignatureEndY(signatureYLocations.get(signatureYLocations.size() - 1));

                this.pendingSwipe.setSignatureEuclideanDistance(Math.sqrt(
                        Math.pow((this.pendingSwipe.getSignatureEndX() - this.pendingSwipe.getSignatureStartX()), 2) +
                                Math.pow((this.pendingSwipe.getSignatureEndY() - this.pendingSwipe.getSignatureStartY()), 2))
                );

                double signatureAvgX = signatureXLocations.stream().mapToDouble(x -> (double) x).summaryStatistics().getAverage();
                double signatureVarX = signatureXLocations.stream().map(i -> i - signatureAvgX).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
                this.pendingSwipe.setSignatureStdX(Math.sqrt(signatureVarX));

                double signatureAvgY = signatureYLocations.stream().mapToDouble(x -> (double) x).summaryStatistics().getAverage();
                double signatureVarY = signatureYLocations.stream().map(i -> i - signatureAvgY).map(i -> i*i).mapToDouble(i -> i).average().getAsDouble();
                this.pendingSwipe.setSignatureStdY(Math.sqrt(signatureVarY));

                DoubleSummaryStatistics signatureXLocationsSummary = signatureXLocations.stream().mapToDouble(x -> (double) x).summaryStatistics();
                this.pendingSwipe.setSignatureDiffX(signatureXLocationsSummary.getMax() - signatureXLocationsSummary.getMin());

                DoubleSummaryStatistics signatureYLocationsSummary = signatureYLocations.stream().mapToDouble(x -> (double) x).summaryStatistics();
                this.pendingSwipe.setSignatureDiffY(signatureYLocationsSummary.getMax() - signatureYLocationsSummary.getMin());

                DoubleSummaryStatistics xVelocityStats = this.signatureView.getXVelocitySummaryStatistics();
                this.pendingSwipe.setSignatureMaxXVelocity(xVelocityStats.getMax());
                this.pendingSwipe.setSignatureAvgXVelocity(xVelocityStats.getAverage());

                DoubleSummaryStatistics yVelocityStats = this.signatureView.getYVelocitySummaryStatistics();
                this.pendingSwipe.setSignatureMaxYVelocity(yVelocityStats.getMax());
                this.pendingSwipe.setSignatureAvgYVelocity(yVelocityStats.getAverage());

                this.pendingSwipe.setSignatureSegmentsX(this.getSegmentsOffset(signatureXLocations, this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS)));
                this.pendingSwipe.setSignatureSegmentsY(this.getSegmentsOffset(signatureYLocations, this.dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS)));

                this.signatureView.clearPath();
                this.resetSwipeValues();
                this.isTrackingSwipe = true;
                this.currentGesture = DatabaseHelper.ModelType.SWIPE;
                this.setSignatureVisibility(View.INVISIBLE);

                if(this.isTrainingMode) {
                    this.dbHelper.addTrainRecord(this.pendingSwipe);
                    this.inputTextView.setText(getString(R.string.inputs) + " " + this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES));
                } else {
                    this.processTestRecord(this.pendingSwipe);
                }
            }
        }
    }

    /**
     * Gets the SUS answer by checking which radio button is currently selected.
     */
    private void getSUSAnswer() {
        int selected = 0;
        switch (SUSQuestionRadioGroup.getCheckedRadioButtonId()) {
            case R.id.SUSQuestionradioButton1:
                selected = 1;
                break;
            case R.id.SUSQuestionradioButton2:
                selected = 2;
                break;
            case R.id.SUSQuestionradioButton3:
                selected = 3;
                break;
            case R.id.SUSQuestionradioButton4:
                selected = 4;
                break;
            case R.id.SUSQuestionradioButton5:
                selected = 5;
                break;
        }

        this.SUSAnswers[this.SUSQuestionNr - 1] = selected;
    }

    /**
     * Triggers the deletion of the currently drawn signature.
     * @param view The view that was clicked.
     */
    public synchronized void clearSignature(View view) {
        this.signatureView.clearPath();
    }

    /**
     * Toggles visibility of some 'accessory' UI elements, namely:
     *  The profile, reset, train and GAN buttons.
     *  The interactions input text.
     *  The swipe image.
     *  The sitting / standing / walking radio buttons.
     *
     * @param visibility The visibility of the accessory UI elements, one of VISIBLE, INVISIBLE or GONE.
     */
    public void setAccessoryUI(Integer visibility) {
        this.profileButton.setEnabled(visibility == View.INVISIBLE);
        this.resetButton.setEnabled(visibility == View.INVISIBLE);
        this.inputTextView.setEnabled(visibility == View.INVISIBLE);
        this.trainButton.setEnabled(visibility == View.INVISIBLE);
        this.ganButton.setEnabled(visibility == View.INVISIBLE);
        this.swipeImageView.setVisibility((visibility == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);

        this.sittingRadioButton.setEnabled(visibility == View.INVISIBLE);
        this.standingRadioButton.setEnabled(visibility == View.INVISIBLE);
        this.walkingRadioButton.setEnabled(visibility == View.INVISIBLE);
    }

    /**
     * If called during training, it triggers a reset of all DB data.
     * If called during testing:
     *  It saves the SUS data and switches back to training mode if the SUS have been taken.
     *  It shows the alert dialog asking to take the SUS questions if they are yet to be taken.
     *
     * @param view The view that was clicked.
     */
    public void resetData(View view) {
        if(this.isTrainingMode) {
            this.dbHelper.resetDB(false);
            this.inputTextView.setText(getString(R.string.inputs) + " " + this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES));
        } else {
            ArrayList<Swipe> testSwipes = dbHelper.getAllSwipes(DatabaseHelper.TEST_SWIPES);

            if(this.isTakingSUSQuestions) { // End questions
                this.getSUSAnswer();
                dbHelper.saveSUSData(this.SUSAnswers);
                toggleSUSQuestionsView(View.INVISIBLE);
                switchToTrainingView(testSwipes);
            } else { // Prompt question message
                if (testSwipes.size() == 0) {
                    this.showAlertDialog(getString(R.string.attention), getString(R.string.min_swipes_test));
                    return;
                }

                if(!dbHelper.hasSUSData()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.sus_questions_desc)).setTitle(getString(R.string.sus_questions));
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            toggleSUSQuestionsView(View.VISIBLE);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            switchToTrainingView(testSwipes);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    switchToTrainingView(testSwipes);
                }
            }
        }
    }

    /**
     * Toggles visibility of SUS questions UI elements.
     *
     * @param visibility The visibility of the SUS questions UI elements, one of VISIBLE, INVISIBLE or GONE.
     */
    private void toggleSUSQuestionsView(int visibility) {
        this.isTrackingSwipe = (visibility != View.VISIBLE);
        this.isTakingSUSQuestions = (visibility == View.VISIBLE);
        this.SUSQuestionNr = 1;

        this.inputTextView.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        this.swipeImageView.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        this.attackSwitch.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        this.resetButton.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);

        this.SUSQuestionTextView.setVisibility(visibility);
        this.SUSQuestionRadioGroup.setVisibility(visibility);
        this.SUSQuestionAgreeTextView.setVisibility(visibility);
        this.SUSQuestionDisagreeTextView.setVisibility(visibility);
        this.nextButton.setVisibility(visibility);
    }

    /**
     *
     * @param testSwipes
     */
    private void switchToTrainingView(ArrayList<Swipe> testSwipes) {
        // If test results were kept from previous training iterations, recompute all test interactions against the model
        if(this.keepTestSwipes) {
            this.reProcessTestRecords(testSwipes);
        }

        String fullSummary = "";
        String ensembleSummary = "";

        String strSummary = "";
        strSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s %6$-9s %7$-9s %8$-9s", "Inputs", "TAR", "FRR", "TRR", "FAR", "Swipe", "Train", "Classifier");
        strSummary += "\n";

        ArrayList<double[]> userAuthentication = this.dbHelper.getTestingData("User", DatabaseHelper.COL_AUTHENTICATION);
        ArrayList<double[]> userAuthenticationTime = this.dbHelper.getTestingData("User", DatabaseHelper.COL_AUTHENTICATION_TIME);

        ArrayList<double[]> attackerAuthentication = this.dbHelper.getTestingData("Attacker", DatabaseHelper.COL_AUTHENTICATION);
        ArrayList<double[]> attackerAuthenticationTime = this.dbHelper.getTestingData("Attacker", DatabaseHelper.COL_AUTHENTICATION_TIME);

        for(List<DatabaseHelper.ModelType> model : this.trainingModels) {
            if(!dbHelper.isModelEnabled(model)) {
                continue;
            }

            double[] curUserAuthentication = userAuthentication.stream().mapToDouble(x -> x[this.trainingModels.indexOf(model)]).toArray();
            double[] curAttackerAuthentication = attackerAuthentication.stream().mapToDouble(x -> x[this.trainingModels.indexOf(model)]).toArray();

            DoubleStream curUserAuthenticationTime = userAuthenticationTime.stream().mapToDouble(x -> x[this.trainingModels.indexOf(model)]);
            DoubleStream curAttackerAuthenticationTime = attackerAuthenticationTime.stream().mapToDouble(x -> x[this.trainingModels.indexOf(model)]);

            fullSummary += computeTestMetrics(
                    testSwipes,
                    curUserAuthentication, curUserAuthenticationTime,
                    curAttackerAuthentication, curAttackerAuthenticationTime,
                    model.toString());
        }

        if(dbHelper.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS) != DatabaseHelper.ModelsCombinations.FULL.ordinal()) {
            double[] curUserAuthentication = userAuthentication.stream().mapToDouble(x -> x[x.length - 1]).toArray();
            double[] curAttackerAuthentication = attackerAuthentication.stream().mapToDouble(x -> x[x.length - 1]).toArray();

            DoubleStream curUserAuthenticationTime = userAuthenticationTime.stream().mapToDouble(x -> x[x.length - 1]);
            DoubleStream curAttackerAuthenticationTime = attackerAuthenticationTime.stream().mapToDouble(x -> x[x.length - 1]);

            ensembleSummary += computeTestMetrics(
                    testSwipes,
                    curUserAuthentication, curUserAuthenticationTime,
                    curAttackerAuthentication, curAttackerAuthenticationTime,
                    DatabaseHelper.COL_WEIGHTED_ENSEMBLE
            );
        }

        if(ensembleSummary != "") {
            this.showAlertDialog(getString(R.string.testing_results), strSummary + ensembleSummary);
        } else {
            this.showAlertDialog(getString(R.string.testing_results), strSummary + fullSummary);
        }

        this.attackSwitch.setChecked(false);

        this.inputTextView.setText(getString(R.string.inputs) + " " + this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES));
        this.ganButton.setVisibility(View.VISIBLE);
        this.trainButton.setVisibility(View.VISIBLE);
        this.saveButton.setVisibility(View.VISIBLE);
        this.attackSwitch.setVisibility(View.INVISIBLE);
        this.profileButton.setVisibility(View.VISIBLE);
        this.holdingPositionRadioGroup.setVisibility(View.VISIBLE);
        this.isTrainingMode = true;
        this.resetButton.setText(getString(R.string.reset_db));
    }

    private String computeTestMetrics(
            ArrayList<Swipe> testSwipes,
            double[] curUserAuthentication, DoubleStream curUserAuthenticationTime,
            double[] curAttackerAuthentication, DoubleStream curAttackerAuthenticationTime,
            String model
    ) {
        String strSummary = "";

        double instances = curUserAuthentication.length + curAttackerAuthentication.length;
        double TAR = curUserAuthentication.length == 0 ? 0.0 : Arrays.stream(curUserAuthentication).sum() / curUserAuthentication.length * 100;
        double FRR = curUserAuthentication.length == 0 ? 0.0 : (curUserAuthentication.length - (Arrays.stream(curUserAuthentication).sum())) / curUserAuthentication.length * 100;
        double TRR = curAttackerAuthentication.length == 0 ? 0.0 : (Arrays.stream(curAttackerAuthentication).sum()) / curAttackerAuthentication.length * 100;
        double FAR = curAttackerAuthentication.length == 0 ? 0.0 : (curAttackerAuthentication.length - (Arrays.stream(curAttackerAuthentication).sum())) / curAttackerAuthentication.length * 100;
        double averageSwipeDuration = testSwipes.stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000.0;

        double avgTestTime = DoubleStream.concat(curUserAuthenticationTime, curAttackerAuthenticationTime).average().getAsDouble();

        int classifierSamples = this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES);
        this.dbHelper.saveTestResults(instances, TAR, FRR, TRR, FAR, averageSwipeDuration, avgTestTime, classifierSamples, model);

        if(model.contains(DatabaseHelper.ModelType.FULL.name()) || model.contains(DatabaseHelper.COL_WEIGHTED_ENSEMBLE)) {
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

        return strSummary;
    }

    public void train(View view) {
        dbHelper.resetDB(true);

        this.isTrainingClassifier = true;
        this.disableUserInteraction();

        this.progressTextView.setText(getString(R.string.training_classifier));
        this.train(false);

    }

    public void trainWithGAN(View view) {
        this.isTrainingClassifier = true;
        this.disableUserInteraction();

        this.progressTextView.setText(getString(R.string.gan_epoch) + ": 0 (" + getString(R.string.out_of) + " " + NUM_EPOCHS + ")");
        this.train(true);
    }

    public void train(boolean isGanMode) {

        if (this.dbHelper.getRecordsCount(DatabaseHelper.REAL_SWIPES) < 5) {
            this.showAlertDialog(getString(R.string.attention), getString(R.string.min_swipes_train));
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
            this.inputTextView.setText(getString(R.string.test_inputs) + " " + this.dbHelper.getRecordsCount(DatabaseHelper.TEST_SWIPES));
            this.ganButton.setVisibility(View.INVISIBLE);
            this.trainButton.setVisibility(View.INVISIBLE);
            this.saveButton.setVisibility(View.INVISIBLE);
            this.resetButton.setText(getString(R.string.done));

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
            if(!keepTestSwipes) {
                dbHelper.deleteTestingData();
                dbHelper.deleteSUSData();
            }
            dbHelper.deleteResourceData();

            ArrayList<Swipe> swipes = dbHelper.getAllSwipes(DatabaseHelper.REAL_SWIPES);
            try {
                this.trainingModels = dbHelper.getActiveModels();
                this.oneClassClassifiers = new CustomOneClassClassifier[this.trainingModels.size()];

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

                    for(List<DatabaseHelper.ModelType> trainingModel : trainingModels) {
                        if(!dbHelper.isModelEnabled(trainingModel)) {
                            continue;
                        }

                        uiHandler.post(() -> {progressTextView.setText(getString(R.string.training) + " " + trainingModel.toString() + " " + getString(R.string.classifier));});
                        trainClassifierWith(swipes, true, ganTime, trainingModel);
                    }
                } else {
                    for(List<DatabaseHelper.ModelType> trainingModel : trainingModels) {
                        if(!dbHelper.isModelEnabled(trainingModel)) {
                            continue;
                        }

                        uiHandler.post(() -> {progressTextView.setText(getString(R.string.training) + " " + trainingModel.toString() + " " + getString(R.string.classifier));});
                        trainClassifierWith(swipes, false, 0.0, trainingModel);
                    }
                }

                if(dbHelper.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS) != DatabaseHelper.ModelsCombinations.FULL.ordinal()) {
                    weightData = dbHelper.getModelWeights(isGanMode);
                }

                uiHandler.post(uiRunnable);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if(dbHelper.getAllSwipes(DatabaseHelper.TEST_SWIPES).size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.keep_testing_data_desc)).setTitle(getString(R.string.keep_testing_data));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    keepTestSwipes = true;
                    new Thread(runnable).start();
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    keepTestSwipes = false;
                    new Thread(runnable).start();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            keepTestSwipes = false;
            new Thread(runnable).start();
        }
    }

    public void trainClassifierWith(ArrayList<Swipe> trainSwipes, boolean hasGan, double ganTime, List<DatabaseHelper.ModelType> trainingModel) {
        Instances dataSet = this.getWekaDataset(trainingModel);

        for(int i=0 ; i < trainSwipes.size(); i++)
        {
            Instance newInstance = trainSwipes.get(i).getAsWekaInstance(dataSet,true, dbHelper, trainingModel);
            dataSet.add(newInstance);
        }

        String finalSummary = "";
        finalSummary += String.format("%1$-9s %2$-9s %3$-9s %4$-9s %5$-9s", "Input", "TAR", "FRR", hasGan ? "GAN" : "Swipe", "Train");
        finalSummary += "\n";

        System.out.println("ARFF representation of Dataset");
        System.out.println(dataSet.toString());

        CustomOneClassClassifier oneClassClassifier = new CustomOneClassClassifier();
        try {
            String[] options = {
                    "-num", m_DefaultNumericGenerator,
                    "-nom", m_DefaultNominalGenerator,
                    "-trr", m_TargetRejectionRate,
                    "-tcl", m_TargetClassLabel,
                    "-cvr", m_NumRepeats,
                    "-cvf", m_PercentHeldout,
                    "-P", m_ProportionGenerated,
                    "-S", m_Seed,
                    "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "100", "-num-slots", "1", "-K", "0", "-S", "1", "", "", "", "", "", "", "", ""};

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
            resourceMonitor.stop(dbHelper, trainingModel.toString());

            Evaluation eTest = new Evaluation(dataSet);
            eTest.crossValidateModel(oneClassClassifier, dataSet, 5, new Random(1));

            this.oneClassClassifiers[this.trainingModels.indexOf(trainingModel)] = oneClassClassifier;

            double instances = eTest.numInstances();
            double TAR = eTest.pctCorrect();
            double FRR = eTest.pctUnclassified();
            double ER = eTest.unclassified() / eTest.numInstances();

            double averageSwipeDuration = trainSwipes.subList(0, dataSet.size()).stream().mapToDouble(Swipe::getDuration).average().getAsDouble() / 1_000;

            finalSummary += String.format("%1$-12s", String.format("%02.0f", instances));
            finalSummary += String.format("%1$-10s", String.format("%.1f", TAR));
            finalSummary += String.format("%1$-11s", String.format("%.1f", FRR));
            finalSummary += String.format("%1$-11s", String.format("%.3f", hasGan ? ganTime : averageSwipeDuration));
            finalSummary += String.format("%1$-10s", String.format("%.3f", rfTrainingTime));
            finalSummary += "\n";

           if(hasGan) {
               this.dbHelper.saveGANResults(instances, TAR, FRR, ER, averageSwipeDuration, rfTrainingTime, ganTime, trainSwipes.size(), trainingModel);
           } else {
               this.dbHelper.saveRealResults(instances, TAR, FRR, ER, averageSwipeDuration, rfTrainingTime, trainSwipes.size(), trainingModel);
           }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(finalSummary);

        if(trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            this.isTrainingClassifier = false;

            final String strSummary = finalSummary;
            MainActivity context = this;

            runOnUiThread(() -> context.enableUserInteraction());
            runOnUiThread(() -> context.showAlertDialog(getString(R.string.training_results), strSummary));
        }
    }

    /**
     * Triggers the creation of the set of .csv files from the DB data.
     * @param view The view that was clicked.
     */
    public synchronized void saveData(View view) {
        try {
            dbHelper.saveAllTablesAsCSV(getContentResolver());
            //this.showAlertDialog("SUCCESS", "CSV files have been saved into the file manager");
            this.showSnackBar(getString(R.string.csv_saved), "#238823");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Builds and shows an alert dialog with the provided title and message.
     *
     * @param title The alert dialog title.
     * @param message The alert dialog message.
     */
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /**
     * Builds and shows a snack bar with the provided message and color.
     *
     * @param message The snack bar message.
     * @param hexColor The hex code of the snack bar color.
     */
    public void showSnackBar(String message, String hexColor) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor(hexColor));

        snackbar.show();
    }

    public Instances getWekaDataset(List<DatabaseHelper.ModelType> trainingModel) {
        Map<String, Integer> featureData = dbHelper.getFeatureData();
        boolean useAcceleration = featureData.get(DatabaseHelper.COL_ACCELERATION) == 1;
        boolean useAngularVelocity = featureData.get(DatabaseHelper.COL_ANGULAR_VELOCITY) == 1;
        boolean useOrientation = featureData.get(DatabaseHelper.COL_ORIENTATION) == 1;

        boolean useSwipeDuration = featureData.get(DatabaseHelper.COL_SWIPE_DURATION) == 1;
        boolean useSwipeShape = featureData.get(DatabaseHelper.COL_SWIPE_SHAPE) == 1;
        boolean useSwipeSize = featureData.get(DatabaseHelper.COL_SWIPE_TOUCH_SIZE) == 1;
        boolean useSwipeStartEndPos = featureData.get(DatabaseHelper.COL_SWIPE_START_END_POS) == 1;
        boolean useSwipeVelocity = featureData.get(DatabaseHelper.COL_SWIPE_VELOCITY) == 1;

        boolean useKeystroke = featureData.get(DatabaseHelper.COL_KEYSTROKE) == 1;
        boolean useKeystrokeDurations = featureData.get(DatabaseHelper.COL_KEYSTROKE_DURATIONS) == 1;
        boolean useKeystrokeIntervals = featureData.get(DatabaseHelper.COL_KEYSTROKE_INTERVALS) == 1;

        boolean useSignature = featureData.get(DatabaseHelper.COL_SIGNATURE) == 1;
        boolean useSignatureStartEndPos = featureData.get(DatabaseHelper.COL_SIGNATURE_START_END_POS) == 1;
        boolean useSignatureVelocity =  featureData.get(DatabaseHelper.COL_SIGNATURE) == 1;
        boolean useSignatureShape = featureData.get(DatabaseHelper.COL_SIGNATURE_SHAPE) == 1;

        ArrayList<Attribute> attributes = new ArrayList<>();

        if(trainingModel.contains(DatabaseHelper.ModelType.SWIPE) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
            if (useSwipeDuration) {
                attributes.add(new Attribute("duration"));
            }
            if (useSwipeShape) {
                attributes.add(new Attribute("length"));

                for(String dimension : new String[]{"x", "y"}) {
                    for(int i = 0; i < dbHelper.getFeatureData().get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS); i++) {
                        attributes.add(new Attribute("segments_" + dimension + "_" + i));
                    }
                }
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
        if(trainingModel.contains(DatabaseHelper.ModelType.HOLD) || trainingModel.contains(DatabaseHelper.ModelType.FULL)) {
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
        if(useKeystroke && (trainingModel.contains(DatabaseHelper.ModelType.KEYSTROKE) || trainingModel.contains(DatabaseHelper.ModelType.FULL))) {
            if(useKeystrokeDurations) {
                for (int i = 0; i < dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH); i++) { attributes.add(new Attribute(LOWER_UNDERSCORE.to(LOWER_CAMEL, DatabaseHelper.COL_KEYSTROKE_DURATIONS) + "_" + i)); }
                attributes.add(new Attribute(DatabaseHelper.COL_KEYSTROKE_FULL_DURATION));
            }
            if(useKeystrokeIntervals) {
                for (int i = 0; i < dbHelper.getFeatureData().get(DatabaseHelper.COL_PIN_LENGTH) - 1; i++) {
                    attributes.add(new Attribute(LOWER_UNDERSCORE.to(LOWER_CAMEL, DatabaseHelper.COL_KEYSTROKE_INTERVALS) + "_" + i));
                    attributes.add(new Attribute(LOWER_UNDERSCORE.to(LOWER_CAMEL, DatabaseHelper.COL_KEYSTROKE_START_INTERVALS) + "_" + i));
                    attributes.add(new Attribute(LOWER_UNDERSCORE.to(LOWER_CAMEL, DatabaseHelper.COL_KEYSTROKE_END_INTERVALS) + "_" + i));
                }
            }
        }
        if(useSignature && (trainingModel.contains(DatabaseHelper.ModelType.SIGNATURE) || trainingModel.contains(DatabaseHelper.ModelType.FULL))) {
            if(useSignatureStartEndPos) {
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_START_X));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_START_Y));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_END_X));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_END_Y));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_STD_X));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_STD_Y));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_DIFF_X));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_DIFF_Y));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_EUCLIDEAN_DISTANCE));
            }
            if(useSignatureVelocity) {
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_AVG_X_VELOCITY));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_AVG_Y_VELOCITY));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_MAX_X_VELOCITY));
                attributes.add(new Attribute(DatabaseHelper.COL_SIGNATURE_MAX_Y_VELOCITY));
            }
            if(useSignatureShape) {
                for(String dimension : new String[]{"x", "y"}) {
                    for(int i = 0; i < dbHelper.getFeatureData().get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS); i++) {
                        attributes.add(new Attribute("signature_segments_" + dimension + "_" + i));
                    }
                }
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