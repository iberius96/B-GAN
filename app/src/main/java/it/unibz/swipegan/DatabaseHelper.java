package it.unibz.swipegan;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Getter instance of the database helper.
     */
    private static DatabaseHelper sInstance;

    /**
     * DB name.
     */
    private static final String DATABASE_NAME = "GAN.db";

    /**
     * Training interactions table name.
     */
    public static final String REAL_SWIPES = "REAL_SWIPES";

    /**
     * Normalized training interactions table name.
     */
    private static final String REAL_SWIPES_NORMALIZED = "REAL_SWIPES_NORMALIZED";

    /**
     * GAN interactions table name.
     */
    private static final String GAN_SWIPES = "GAN_SWIPES";

    /**
     * Normalized GAN interactions table name.
     */
    private static final String GAN_SWIPES_NORMALIZED = "GAN_SWIPES_NORMALIZED";

    /**
     * Test interactions table name.
     */
    public static final String TEST_SWIPES = "TEST_SWIPES";

    /**
     * Normalized test interactions table name.
     */
    private static final String TEST_SWIPES_NORMALIZED = "TEST_SWIPES_NORMALIZED";

    /**
     * Training results table name.
     */
    private static final String REAL_RESULTS = "REAL_RESULTS";

    /**
     * Training results table name (models trained with genuine + synthetic samples).
     */
    private static final String GAN_RESULTS = "GAN_RESULTS";

    /**
     * Test results table name.
     */
    private static final String TEST_RESULTS = "TEST_RESULTS";

    /**
     * Test authentication results table name.
     */
    private static final String TEST_AUTHENTICATION = "TEST_AUTHENTICATION";

    /**
     * User data table name.
     */
    private static final String USER_DATA = "USER_DATA";

    /**
     * Feature data table name.
     */
    private static final String FEATURE_DATA = "FEATURE_DATA";

    /**
     * Resource data table name.
     */
    private static final String RESOURCE_DATA = "RESOURCE_DATA";

    /**
     * Raw training interaction data table name.
     */
    private static final String TRAIN_RAW_DATA = "TRAIN_RAW_DATA";

    /**
     * SUS questions data table name.
     */
    private static final String SUS_DATA = "SUS_DATA";

    /**
     * Authentication result column name.
     */
    public static final String COL_AUTHENTICATION = "AUTHENTICATION";

    /**
     * Authentication time column name.
     */
    public static final String COL_AUTHENTICATION_TIME = "AUTHENTICATION_TIME";

    /**
     * Weighted ensemble model column name.
     */
    public static final String COL_WEIGHTED_ENSEMBLE = "WEIGHTED_ENSEMBLE";

    /**
     * Interaction step duration column name.
     */
    private static final String COL_DURATION = "duration";

    /**
     * Interaction step length column name.
     */
    private static final String COL_LENGTH = "length";

    /**
     * Swipe/Signature X segment column name.
     */
    public static final String COL_SEGMENTS_X = "segments_x";

    /**
     * Swipe/Signature Y segment column name.
     */
    public static final String COL_SEGMENTS_Y = "segments_y";

    /**
     * Minimum touch size column name.
     */
    private static final String COL_MIN_SIZE = "min_size";

    /**
     * Maximum touch size column name.
     */
    private static final String COL_MAX_SIZE = "max_size";

    /**
     * Average touch size column name.
     */
    private static final String COL_AVG_SIZE = "avg_size";

    /**
     * Initial touch size column name.
     */
    private static final String COL_DOWN_SIZE = "down_size";

    /**
     * Final touch size column name.
     */
    private static final String COL_UP_SIZE = "up_size";

    /**
     * Touch start position X value column name.
     */
    private static final String COL_START_X = "start_x";

    /**
     * Touch start position Y value column name.
     */
    private static final String COL_START_Y = "start_y";

    /**
     * Touch end position X value column name.
     */
    private static final String COL_END_X = "end_x";

    /**
     * Touch end position Y value column name.
     */
    private static final String COL_END_Y = "end_y";

    /**
     * Device holding position column name.
     */
    private static final String COL_HOLDING_POSITION = "holding_position";

    /**
     * Interactions count column name.
     */
    private static final String COL_INSTANCES = "INSTANCES";

    /**
     * TAR value column name.
     */
    private static final String COL_TAR = "TAR";

    /**
     * FRR value column name.
     */
    private static final String COL_FRR = "FRR";

    /**
     * TRR value column name.
     */
    private static final String COL_TRR = "TRR";

    /**
     * FAR value column name.
     */
    private static final String COL_FAR = "FAR";

    /**
     * ER value column name.
     */
    private static final String COL_ER = "ER";

    /**
     * Average interaction time column name.
     */
    private static final String COL_AVG_SAMPLE_TIME = "AVG_SAMPLE_TIME";

    /**
     * Training time column name.
     */
    private static final String COL_TRAINING_TIME = "TRAINING_TIME";

    /**
     * GAN training time column name.
     */
    private static final String COL_GAN_TIME = "GAN_TIME";

    /**
     * Average per-interaction test time column name.
     */
    private static final String COL_AVG_TEST_TIME = "AVG_TEST_TIME";

    /**
     * User ID column name.
     */
    private static final String COL_USER_ID = "USER_ID";

    /**
     * Train/Test interactions count column name.
     */
    private static final String COL_CLASSIFIER_SAMPLES = "CLASSIFIER_SAMPLES";

    /**
     * Model type column name.
     */
    private static final String COL_MODEL_TYPE = "MODEL_TYPE";

    /**
     * Nickname column name.
     */
    public static final String COL_NICKNAME = "nickname";

    /**
     * Gender column name.
     */
    public static final String COL_GENDER = "gender";

    /**
     * Age column name.
     */
    public static final String COL_AGE = "age";

    /**
     * Nationality column name.
     */
    public static final String COL_NATIONALITY = "nationality";

    /**
     * Hand used to hold the device column name.
     */
    public static final String COL_HOLDING_HAND = "holding_hand";

    // Feature data columns

    /**
     * Type of model configuration (FULL, INDIVIDUAL_FULL, ALL) column name.
     */
    public static final String COL_MODELS_COMBINATIONS = "models_combinations";

    /**
     * Acceleration column name.
     */
    public static final String COL_ACCELERATION = "acceleration";

    /**
     * Angular velocity column name.
     */
    public static final String COL_ANGULAR_VELOCITY = "angular_velocity";

    /**
     * Orientation column name.
     */
    public static final String COL_ORIENTATION = "orientation";

    /**
     * Swipe duration column name.
     */
    public static final String COL_SWIPE_DURATION = "swipe_duration";

    /**
     * Swipe shape column name.
     */
    public static final String COL_SWIPE_SHAPE = "swipe_shape";

    /**
     * Swipe segments nr column name.
     */
    public static final String COL_SWIPE_SHAPE_SEGMENTS = "swipe_shape_segments";

    /**
     * Touch size column name.
     */
    public static final String COL_SWIPE_TOUCH_SIZE = "swipe_touch_size";

    /**
     * Swipe start/end position column name.
     */
    public static final String COL_SWIPE_START_END_POS = "swipe_start_end_pos";

    /**
     * Swipe velocity column name.
     */
    public static final String COL_SWIPE_VELOCITY = "swipe_velocity";

    /**
     * Keystroke column name.
     */
    public static final String COL_KEYSTROKE = "keystroke";

    /**
     * PIN length column name.
     */
    public static final String COL_PIN_LENGTH = "pin_length";

    /**
     * Signature column name.
     */
    public static final String COL_SIGNATURE = "signature";

    /**
     * Signature start/end position column name.
     */
    public static final String COL_SIGNATURE_START_END_POS = "signature_start_end_pos";

    /**
     * Signature velocity column name.
     */
    public static final String COL_SIGNATURE_VELOCITY = "signature_velocity";

    /**
     * Signature shape column name.
     */
    public static final String COL_SIGNATURE_SHAPE = "signature_shape";

    /**
     * Signature segments nr column name.
     */
    public static final String COL_SIGNATURE_SHAPE_SEGMENTS = "signature_shape_segments";

    /**
     * Raw data column name.
     */
    public static final String COL_RAW_DATA = "raw_data";

    /**
     * Frequency of raw data sampling column name.
     */
    public static final String COL_RAW_DATA_FREQUENCY = "raw_data_frequency";

    // Resource columns

    /**
     *  Minimum CPU frequency column name.
     */
    private static final String COL_MIN_CPU_FREQ = "min_cpu_freq";

    /**
     *  Maximum CPU frequency column name.
     */
    private static final String COL_MAX_CPU_FREQ = "max_cpu_freq";

    /**
     *  Average CPU frequency column name.
     */
    private static final String COL_AVG_CPU_FREQ = "avg_cpu_freq";

    /**
     *  Minimum memory usage column name.
     */
    private static final String COL_MIN_MEMORY_USAGE = "min_memory_usage";

    /**
     *  Maximum memory usage column name.
     */
    private static final String COL_MAX_MEMORY_USAGE = "max_memory_usage";

    /**
     *  Average memory usage column name.
     */
    private static final String COL_AVG_MEMORY_USAGE = "avg_memory_usage";

    /**
     *  Power draw column name.
     */
    private static final String COL_POWER_DRAW = "power_draw";

    // Keystrokes columns

    /**
     *  Keystroke individual durations column name.
     */
    public static final String COL_KEYSTROKE_DURATIONS = "keystroke_durations";

    /**
     *  Keystroke individual intervals column name.
     */
    public static final String COL_KEYSTROKE_INTERVALS = "keystroke_intervals";

    /**
     *  Start time of individual keystroke intervals column name.
     */
    public static final String COL_KEYSTROKE_START_INTERVALS = "keystroke_start_intervals";

    /**
     *  End time of individual keystroke intervals column name.
     */
    public static final String COL_KEYSTROKE_END_INTERVALS = "keystroke_end_intervals";

    /**
     *  Keystroke full duration column name.
     */
    public static final String COL_KEYSTROKE_FULL_DURATION = "keystroke_full_duration";

    // Signature columns

    /**
     *  Signature start X position column name.
     */
    public static final String COL_SIGNATURE_START_X = "signature_start_x";

    /**
     *  Signature start Y position column name.
     */
    public static final String COL_SIGNATURE_START_Y = "signature_start_y";

    /**
     *  Signature end X position column name.
     */
    public static final String COL_SIGNATURE_END_X = "signature_end_x";

    /**
     *  Signature end Y position column name.
     */
    public static final String COL_SIGNATURE_END_Y = "signature_end_y";

    /**
     *  Signature std X positions column name.
     */
    public static final String COL_SIGNATURE_STD_X = "signature_std_x";

    /**
     *  Signature std Y positions column name.
     */
    public static final String COL_SIGNATURE_STD_Y = "signature_std_y";

    /**
     *  Signature X displacement column name.
     */
    public static final String COL_SIGNATURE_DIFF_X = "signature_diff_x";

    /**
     *  Signature Y displacement column name.
     */
    public static final String COL_SIGNATURE_DIFF_Y = "signature_diff_y";

    /**
     *  Signature start/end position euclidean distance column name.
     */
    public static final String COL_SIGNATURE_EUCLIDEAN_DISTANCE = "signature_euclidean_distance";

    /**
     *  Signature average X velocity column name.
     */
    public static final String COL_SIGNATURE_AVG_X_VELOCITY = "signature_avg_x_velocity";

    /**
     *  Signature average Y velocity column name.
     */
    public static final String COL_SIGNATURE_AVG_Y_VELOCITY = "signature_avg_y_velocity";

    /**
     *  Signature maximum X velocity column name.
     */
    public static final String COL_SIGNATURE_MAX_X_VELOCITY = "signature_max_x_velocity";

    /**
     *  Signature maximum Y velocity column name.
     */
    public static final String COL_SIGNATURE_MAX_Y_VELOCITY = "signature_max_y_velocity";

    /**
     *  Signature X segments size column name.
     */
    public static final String COL_SIGNATURE_SEGMENTS_X = "signature_segments_x";

    /**
     *  Signature Y segments size column name.
     */
    public static final String COL_SIGNATURE_SEGMENTS_Y = "signature_segments_y";

    // Raw data columns

    /**
     *  Touch size column name.
     */
    public static final String COL_SIZE = "size";

    /**
     *  X coordinate value column name.
     */
    public static final String COL_X = "x";

    /**
     *  Y coordinate value column name.
     */
    public static final String COL_Y = "y";

    /**
     *  X velocity column name.
     */
    public static final String COL_VELOCITY_X = "velocity_x";

    /**
     *  Y velocity column name.
     */
    public static final String COL_VELOCITY_Y = "velocity_Y";

    /**
     *  X accelerometer value column name.
     */
    public static final String COL_ACCELEROMETER_X = "accelerometer_x";

    /**
     *  Y accelerometer value column name.
     */
    public static final String COL_ACCELEROMETER_Y = "accelerometer_y";

    /**
     *  Z accelerometer value column name.
     */
    public static final String COL_ACCELEROMETER_Z = "accelerometer_z";

    /**
     *  X gyroscope value column name.
     */
    public static final String COL_GYROSCOPE_X = "gyroscope_x";

    /**
     *  Y gyroscope value column name.
     */
    public static final String COL_GYROSCOPE_Y = "gyroscope_y";

    /**
     *  Z gyroscope value column name.
     */
    public static final String COL_GYROSCOPE_Z = "gyroscope_z";

    /**
     *  X orientation value column name.
     */
    public static final String COL_ORIENTATION_X = "orientation_x";

    /**
     *  Y orientation value column name.
     */
    public static final String COL_ORIENTATION_Y = "orientation_y";

    /**
     *  Z orientation value column name.
     */
    public static final String COL_ORIENTATION_Z = "orientation_z";

    /**
     *  Current gesture (Swipe/Keystroke/Signature) identifier column name.
     */
    public static final String COL_GESTURE_ID = "gesture_id";

    // SUS columns

    /**
     *  SUS question prefix column name.
     */
    private static final String COL_QUESTION = "question_";

    /**
     *  Set of interaction prefix for column names.
     */
    public static final String[] head_features = {
            COL_DURATION,
            COL_LENGTH,
            COL_SEGMENTS_X, COL_SEGMENTS_Y,
            COL_MIN_SIZE, COL_MAX_SIZE, COL_AVG_SIZE, COL_DOWN_SIZE, COL_UP_SIZE,
            COL_START_X, COL_START_Y,
            COL_END_X, COL_END_Y,
    };

    // Used to reference velocity- and all swipe- related features
    /**
     *  Set of interaction feature suffix for column names.
     */
    public static final String[] features = {"Velocity", "Accelerometer", "Gyroscope", "Orientation"};

    /**
     *  Set of interaction metric (Min/Max/Avg/Var/Std) suffix for column names.
     */
    public static final String[] metrics = {"Min", "Max", "Avg", "Var", "Std"};

    /**
     *  Set of interaction dimension (X/Y/Z) suffix for column names.
     */
    public static final String[] dimensions = {"X", "Y", "Z"};

    /**
     * Set of keystroke features column names.
     */
    public static final String[] keystroke_features = {
            COL_KEYSTROKE_DURATIONS,
            COL_KEYSTROKE_INTERVALS,
            COL_KEYSTROKE_START_INTERVALS,
            COL_KEYSTROKE_END_INTERVALS,
            COL_KEYSTROKE_FULL_DURATION
    };

    /**
     * Set of signature features column names.
     */
    public static final String[] signature_features = {
        COL_SIGNATURE_START_X, COL_SIGNATURE_START_Y,
        COL_SIGNATURE_END_X, COL_SIGNATURE_END_Y,
        COL_SIGNATURE_AVG_X_VELOCITY, COL_SIGNATURE_AVG_Y_VELOCITY,
        COL_SIGNATURE_MAX_X_VELOCITY, COL_SIGNATURE_MAX_Y_VELOCITY,
        COL_SIGNATURE_STD_X, COL_SIGNATURE_STD_Y,
        COL_SIGNATURE_DIFF_X, COL_SIGNATURE_DIFF_Y,
        COL_SIGNATURE_EUCLIDEAN_DISTANCE,
        COL_SIGNATURE_SEGMENTS_X, COL_SIGNATURE_SEGMENTS_Y
    };

    /**
     * Set of raw data features column names.
     */
    public static final String[] raw_features = {
        COL_SIZE,
        COL_X, COL_Y,
        COL_VELOCITY_X, COL_VELOCITY_Y,
        COL_ACCELEROMETER_X,
        COL_ACCELEROMETER_Y,
        COL_ACCELEROMETER_Z,
        COL_GYROSCOPE_X,
        COL_GYROSCOPE_Y,
        COL_GYROSCOPE_Z,
        COL_ORIENTATION_X,
        COL_ORIENTATION_Y,
        COL_ORIENTATION_Z,
        COL_GESTURE_ID
    };

    /**
     * Enum used to identify the type of the individual models (Hold/Swipe/Keystroke/Signature/Full).
     * Full model refers to the model obtained by performing feature level fusion on all the individual models.
     */
    public enum ModelType {
        HOLD,
        SWIPE,
        KEYSTROKE,
        SIGNATURE,
        FULL
    };

    /**
     * Enum used to identify the set of models to build during the training procedure.
     * Full allows only the model obtained by performing feature level fusion on all the active features to be built.
     * Individual_Full allows for the training of the four individual models and the one obtained by performing feature level fusion.
     * All allows to build all the models mentioned above together with all the different combination obtainable by performing feature level fusion on the individual models.
     */
    public enum ModelsCombinations {
        FULL,
        INDIVIDUAL_FULL,
        ALL
    }

    /**
     * Defines the number of base features collected in a single interaction.
     */
    public static final Integer BASE_FEATURES = 66;

    /**
     * Defines the default number of segments used to decompose swipe and signature gestures.
     */
    public static final Integer DEFAULT_SEGMENTS = 10;

    /**
     * Defines the default value of the PIN length.
     */
    public static final Integer DEFAULT_PIN_LENGTH = 8;

    /**
     * Defines the default value (0 = Disabled, 1 = Enabled) for the setting controlling raw data collection.
     */
    public static final Integer DEFAULT_RAW_DATA = 1;

    /**
     * Defines the default frequency (in Hz) at which the raw data is collected.
     */
    public static final Integer DEFAULT_FREQUENCY = 50;

    /**
     * Defines the default number of SUS questions.
     */
    public static final int DEFAULT_SUS_QUESTIONS = 10;

    /**
     * Class Constructor.
     * @param context Interface to global information about the application environment.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Returns the instance of the DatabaseHelper class.
     * @param context Interface to global information about the application environment.
     * @return An instance of the DatabaseHelper class.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Generates all the db tables.
     *
     * The list of generated tables looks as follows:
     * Table containing the training results for all the models generated using genuine user interactions (REAL_RESULTS).
     * Table containing the training results for all the models generated using both genuine ans synthetic interactions (GAN_RESULTS).
     * Table containing the testing results (TEST_RESULTS).
     * Table containing the provided user information (USER_DATA).
     * Table containing info on the features used to train the models (FEATURE_DATA).
     * Table containing device resource information over the training phase (RESOURCE_DATA).
     * Table containing the training data collected as a continuous stream (TRAIN_RAW_DATA).
     * Table containing the responses for each SUS question (SUS_DATA).
     * Tables containing data related to the individual interactions (REAL_SWIPES, GAN_SWIPES, TEST_SWIPES, REAL_SWIPES_NORMALIZED, GAN_SWIPES_NORMALIZED, TEST_SWIPES_NORMALIZED).
     * Table containing authentication results for the individual interactions against all generated models (TEST_AUTHENTICATION).
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRealResultsTable = "CREATE TABLE " + REAL_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_ER + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53),"
                + COL_MODEL_TYPE + " varchar(20))";

        String createGanResultsTable = "CREATE TABLE " + GAN_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_ER + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_GAN_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53),"
                + COL_MODEL_TYPE + " varchar(20))";

        String createTestResultsTable = "CREATE TABLE " + TEST_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_TRR + " float(53), "
                + COL_FAR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_AVG_TEST_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53),"
                + COL_MODEL_TYPE + " varchar(20))";

        String createUserDataTable = "CREATE TABLE " + USER_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NICKNAME + " varchar(20), "
                + COL_GENDER + " integer(1), "
                + COL_AGE + " integer(1), "
                + COL_NATIONALITY + " varchar(20), "
                + COL_HOLDING_HAND + " integer(1))";

        String createFeatureDataTable = "CREATE TABLE " + FEATURE_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MODELS_COMBINATIONS + " integer(1), "
                + COL_ACCELERATION + " integer(1), "
                + COL_ANGULAR_VELOCITY + " integer(1), "
                + COL_ORIENTATION + " integer(1), "
                + COL_SWIPE_DURATION + " integer(1), "
                + COL_SWIPE_SHAPE + " integer(1), "
                + COL_SWIPE_SHAPE_SEGMENTS + " integer(2), "
                + COL_SWIPE_TOUCH_SIZE + " integer(1), "
                + COL_SWIPE_START_END_POS + " integer(1), "
                + COL_SWIPE_VELOCITY + " integer(1), "
                + COL_KEYSTROKE + " integer(1), "
                + COL_PIN_LENGTH + " integer(1), "
                + COL_KEYSTROKE_DURATIONS + " integer(1), "
                + COL_KEYSTROKE_INTERVALS + " integet(1), "
                + COL_SIGNATURE + " integer(1), "
                + COL_SIGNATURE_START_END_POS + " integer(1), "
                + COL_SIGNATURE_VELOCITY + " integer(1), "
                + COL_SIGNATURE_SHAPE + " integer(1), "
                + COL_SIGNATURE_SHAPE_SEGMENTS + " integer(2), "
                + COL_RAW_DATA + " integer(1), "
                + COL_RAW_DATA_FREQUENCY + " integer(3))";

        String createResourceDataTable = "CREATE TABLE " + RESOURCE_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MIN_CPU_FREQ + " varchar(255), "
                + COL_MAX_CPU_FREQ + " varchar(255), "
                + COL_AVG_CPU_FREQ + " varchar(255), "
                + COL_MIN_MEMORY_USAGE + " float(53), "
                + COL_MAX_MEMORY_USAGE + " float(53), "
                + COL_AVG_MEMORY_USAGE + " float(53), "
                + COL_POWER_DRAW + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_MODEL_TYPE + " varchar(20))";

        String createRawDataTable = "CREATE TABLE " + TRAIN_RAW_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SIZE + " float(53), "
                + COL_X + " float(53), "
                + COL_Y + " float(53), "
                + COL_VELOCITY_X + " float(53), "
                + COL_VELOCITY_Y + " float(53), "
                + COL_ACCELEROMETER_X + " float(53), "
                + COL_ACCELEROMETER_Y + " float(53), "
                + COL_ACCELEROMETER_Z + " float(53), "
                + COL_GYROSCOPE_X + " float(53), "
                + COL_GYROSCOPE_Y + " float(53), "
                + COL_GYROSCOPE_Z + " float(53), "
                + COL_ORIENTATION_X + " float(53), "
                + COL_ORIENTATION_Y + " float(53), "
                + COL_ORIENTATION_Z + " float(53), "
                + COL_GESTURE_ID + " float(53))";

        String createSUSTable = "CREATE TABLE " + SUS_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, ";

        for(int i = 0; i < DEFAULT_SUS_QUESTIONS; i++) {
            createSUSTable += COL_QUESTION + i + " integer(1), ";
        }
        createSUSTable += COL_USER_ID + " varchar(20))";


        this.generateSwipesTables(db,false, true, true);
        this.generateTestAuthenticationTable(db, false, null);

        db.execSQL(createRealResultsTable);
        db.execSQL(createGanResultsTable);
        db.execSQL(createTestResultsTable);

        db.execSQL(createUserDataTable);
        db.execSQL(createFeatureDataTable);
        db.execSQL(createResourceDataTable);

        db.execSQL(createRawDataTable);

        db.execSQL(createSUSTable);

        // Insert default user data
        Cursor user_cursor = db.rawQuery("SELECT * FROM " + USER_DATA, null);
        int user_count = user_cursor.getCount();
        user_cursor.close();

        if (user_count == 0) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COL_NICKNAME, "None");
            contentValues.put(COL_GENDER, 0);
            contentValues.put(COL_AGE, 0);
            contentValues.put(COL_NATIONALITY, "None");
            contentValues.put(COL_HOLDING_HAND, 0);

            db.insert(USER_DATA, null, contentValues);
        }

        // Insert default feature data
        Cursor feature_cursor = db.rawQuery("SELECT * FROM " + FEATURE_DATA, null);
        int feature_count = feature_cursor.getCount();
        feature_cursor.close();

        if (feature_count == 0) {
            ContentValues contentValues = new ContentValues();

            String[] feature_cols = {
                    COL_MODELS_COMBINATIONS,
                    COL_ACCELERATION, COL_ANGULAR_VELOCITY, COL_ORIENTATION,
                    COL_SWIPE_DURATION, COL_SWIPE_SHAPE, COL_SWIPE_SHAPE_SEGMENTS, COL_SWIPE_TOUCH_SIZE, COL_SWIPE_START_END_POS, COL_SWIPE_VELOCITY,
                    COL_KEYSTROKE, COL_PIN_LENGTH, COL_KEYSTROKE_DURATIONS, COL_KEYSTROKE_INTERVALS,
                    COL_SIGNATURE, COL_SIGNATURE_START_END_POS, COL_SIGNATURE_VELOCITY, COL_SIGNATURE_SHAPE, COL_SIGNATURE_SHAPE_SEGMENTS,
                    COL_RAW_DATA, COL_RAW_DATA_FREQUENCY};

            for(String feature_col : feature_cols) {
                if(feature_col == COL_MODELS_COMBINATIONS) {
                    contentValues.put(feature_col, ModelsCombinations.ALL.ordinal());
                } else if (feature_col == COL_SWIPE_SHAPE_SEGMENTS || feature_col == COL_SIGNATURE_SHAPE_SEGMENTS) {
                    contentValues.put(feature_col, DEFAULT_SEGMENTS);
                } else if(feature_col == COL_PIN_LENGTH) {
                    contentValues.put(feature_col, DEFAULT_PIN_LENGTH);
                } else if(feature_col == COL_RAW_DATA) {
                    contentValues.put(feature_col, DEFAULT_RAW_DATA);
                } else if (feature_col == COL_RAW_DATA_FREQUENCY){
                    contentValues.put(feature_col, DEFAULT_FREQUENCY);
                } else {
                    contentValues.put(feature_col, 1);
                }
            }
            db.insert(FEATURE_DATA, null, contentValues);
        }
    }

    /**
     * Generates the set of interaction tables.
     *
     * @param db The database.
     * @param regenerate Indicates whether a set of interaction tables already exists in the current db.
     * @param hasKeystrokes Indicates whether the keystroke model is currently active.
     * @param hasSignature Indicates whether the signature model is currently active.
     */
    public void generateSwipesTables(SQLiteDatabase db, boolean regenerate, boolean hasKeystrokes, boolean hasSignature) {
        if(db == null) { db = this.getWritableDatabase(); }
        String[] swipes_tables = {REAL_SWIPES, GAN_SWIPES, TEST_SWIPES, REAL_SWIPES_NORMALIZED, GAN_SWIPES_NORMALIZED, TEST_SWIPES_NORMALIZED};

        if(regenerate) {
            for (String swipe_table : swipes_tables) { db.execSQL("DROP TABLE " + swipe_table); }
        }

        String swipes_base = "CREATE TABLE " + "BASE_TABLE"
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, ";

        for(String head_feature : head_features) {
            if(head_feature == COL_SEGMENTS_X || head_feature == COL_SEGMENTS_Y) {
                swipes_base += head_feature + " varchar(255), ";
            } else {
                swipes_base += head_feature + " float(53), ";
            }
        }

        for(String feature : DatabaseHelper.features) {
            for (String metric : DatabaseHelper.metrics) {
                for (String dimension : DatabaseHelper.dimensions) {
                    if (dimension == "Z" && feature == "Velocity") { continue; }

                    swipes_base += metric.toLowerCase() + "_" + dimension.toLowerCase() + "_" + feature.toLowerCase() + " float(53), ";
                }
            }
        }

        if(hasKeystrokes) {
            for(String keystroke_feature : keystroke_features) {
                swipes_base += keystroke_feature + (keystroke_feature == COL_KEYSTROKE_FULL_DURATION ? " float(53), " : " varchar(255), ");
            }
        }

        if(hasSignature) {
            for(String signature_feature : signature_features) {
                swipes_base += signature_feature + ((signature_feature == COL_SIGNATURE_SEGMENTS_X || signature_feature == COL_SIGNATURE_SEGMENTS_Y) ? " varchar(255), " : " float(53), ");
            }
        }

        swipes_base += COL_HOLDING_POSITION + " float(53), " + COL_USER_ID + " varchar(20))";

        for(int i = 0; i < swipes_tables.length; i++) {
            String create_statement = swipes_base.replace("BASE_TABLE", swipes_tables[i]);
            if(swipes_tables[i] == TEST_SWIPES) {
                create_statement = create_statement.replace(
                        COL_USER_ID,
                        COL_AUTHENTICATION + " varchar(255), " +
                                COL_AUTHENTICATION_TIME + " varchar(255), " +
                                COL_CLASSIFIER_SAMPLES + " float(53), " +
                                COL_USER_ID
                );
            }
            db.execSQL(create_statement);
        }
    }

    /**
     * Generates the table containing the authentication results of the individual interaction for all the active models.
     *
     * @param db The database.
     * @param regenerate Indicates whether a test authentication table already exists in the current db.
     * @param activeModels The list of active models that will evaluate the test interactions.
     */
    public void generateTestAuthenticationTable(SQLiteDatabase db, boolean regenerate, List<List<ModelType>> activeModels) {
        if(db == null) { db = this.getWritableDatabase(); }
        if(regenerate) { db.execSQL("DROP TABLE " + TEST_AUTHENTICATION); }

        String create_statement = "CREATE TABLE " + TEST_AUTHENTICATION
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, ";

        if(activeModels == null) {
            activeModels = new ArrayList<>();
            //for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) { activeModels.add(Arrays.asList(modelType)); }
            activeModels = this.getAllModelsCombinations();
        }

        for(List<ModelType> trainingModel : activeModels) {
            String modelStr = trainingModel.toString().replace(", ", "_").replace("[", "").replace("]", "");
            create_statement += modelStr + "_" + COL_AUTHENTICATION + " float(53), ";
            create_statement += modelStr + "_" + COL_AUTHENTICATION_TIME + " float(53), ";
        }

        if(activeModels.size() != 1) {
            create_statement += COL_WEIGHTED_ENSEMBLE + "_" + COL_AUTHENTICATION + " float(53), ";
            create_statement += COL_WEIGHTED_ENSEMBLE + "_" + COL_AUTHENTICATION_TIME + " float(53), ";
        }

        create_statement += COL_USER_ID + " varchar(20))";
        System.out.println(create_statement);
        db.execSQL(create_statement);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] upgrade_tables = {
            REAL_SWIPES,
            GAN_SWIPES,
            TEST_SWIPES,
            REAL_SWIPES_NORMALIZED,
            GAN_SWIPES_NORMALIZED,
            TEST_SWIPES_NORMALIZED,
            REAL_RESULTS,
            GAN_RESULTS,
            TEST_RESULTS,
            TEST_AUTHENTICATION,
            USER_DATA,
            FEATURE_DATA,
            RESOURCE_DATA,
            TRAIN_RAW_DATA,
            SUS_DATA
        };

        for(int i = 0; i < upgrade_tables.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + upgrade_tables[i]);
        }

        onCreate(db);
    }

    /**
     * Goes through the individual feature values of the interaction and adds them to the specified table.
     *
     * @param swipe The swipe object representing the interaction that needs to be added the the DB.
     * @param tableName The name of the table to which the interaction needs to be added to.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    private boolean addSwipe(Swipe swipe, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(String head_feature : head_features) {
            java.lang.reflect.Method cur_method = null;
            try {
                cur_method = swipe.getClass().getMethod("get" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)));
                if(head_feature == COL_SEGMENTS_X || head_feature == COL_SEGMENTS_Y) {
                    contentValues.put(head_feature, Arrays.toString((double[]) cur_method.invoke(swipe)));
                } else {
                    contentValues.put(head_feature, (Double) cur_method.invoke(swipe));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for(String feature : DatabaseHelper.features) {
            for(String metric : DatabaseHelper.metrics) {
                for(String dimension : DatabaseHelper.dimensions) {
                    if (dimension == "Z" && feature == "Velocity") { continue; }

                    String col_name = metric.toLowerCase() + "_" + dimension.toLowerCase() + "_" + feature.toLowerCase();

                    java.lang.reflect.Method cur_method = null;
                    Double col_value = 0.0;
                    try {
                        cur_method = swipe.getClass().getMethod("get" + metric + dimension + feature);
                        col_value = (Double) cur_method.invoke(swipe);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    contentValues.put(col_name, col_value);
                }
            }
        }

        if(this.getFeatureData().get(COL_KEYSTROKE) == 1) {
            for (String keystroke_feature : keystroke_features) {
                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)));

                    if(keystroke_feature == COL_KEYSTROKE_FULL_DURATION) {
                        contentValues.put(keystroke_feature, (Double) cur_method.invoke(swipe));
                    } else {
                        contentValues.put(keystroke_feature, Arrays.toString((double[]) cur_method.invoke(swipe)));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if(this.getFeatureData().get(COL_SIGNATURE) == 1) {
            for (String signature_feature : signature_features) {
                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)));

                    if(signature_feature == COL_SIGNATURE_SEGMENTS_X || signature_feature == COL_SIGNATURE_SEGMENTS_Y) {
                        contentValues.put(signature_feature, Arrays.toString((double[]) cur_method.invoke(swipe)));
                    } else {
                        contentValues.put(signature_feature, (Double) cur_method.invoke(swipe));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
        contentValues.put(COL_USER_ID, swipe.getUserId());

        if(tableName == TEST_SWIPES) {
            this.addSwipeAuthentication(db, swipe);

            contentValues.put(COL_AUTHENTICATION, Arrays.toString(swipe.getAuthentication()));
            contentValues.put(COL_AUTHENTICATION_TIME, Arrays.toString(swipe.getAuthenticationTime()));
            contentValues.put(COL_CLASSIFIER_SAMPLES, swipe.getClassifierSamples());
        }

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Goes through the individual feature values of the raw entry and adds them to the specified table.
     *
     * @param rawDataEntry The current raw data entry expressed by an Hashmap of column names strings as keys.
     * @param tableName The name of the table to which the raw data entry needs to be added to.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean addRawDataEntry(Map<String, Double> rawDataEntry, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(String raw_feature : raw_features) {
            contentValues.put(raw_feature, rawDataEntry.get(raw_feature));
        }

        long result = db.insert(tableName, null, contentValues);
        return result != -1;
    }

    /**
     * Retrieves authentication results (0 = Misclassified, 1 = Correctly classified) and times of a given interactions with regards to all active models and adds them to the relevant table.
     *
     * @param db The database.
     * @param swipe The interaction for which authentication results and times are being stored.
     */
    private void addSwipeAuthentication(SQLiteDatabase db, Swipe swipe) {
        ContentValues contentValues = new ContentValues();

        double[] swipeAuthentication = swipe.getAuthentication();
        double[] swipeAuthenticationTime = swipe.getAuthenticationTime();
        List<List<DatabaseHelper.ModelType>> activeModels = getActiveModels().stream().filter(s -> isModelEnabled(s)).collect(Collectors.toList());

        for(List<ModelType> activeModel : activeModels) {
            String modelStr = activeModel.toString().replace(", ", "_").replace("[", "").replace("]", "");
            contentValues.put(modelStr + "_" + COL_AUTHENTICATION, swipeAuthentication[activeModels.indexOf(activeModel)]);
            contentValues.put(modelStr + "_" + COL_AUTHENTICATION_TIME, swipeAuthenticationTime[activeModels.indexOf(activeModel)]);
        }

        if(this.getFeatureData().get(COL_MODELS_COMBINATIONS) != ModelsCombinations.FULL.ordinal()) {
            contentValues.put(COL_WEIGHTED_ENSEMBLE + "_" + COL_AUTHENTICATION, swipeAuthentication[swipeAuthentication.length - 1]);
            contentValues.put(COL_WEIGHTED_ENSEMBLE + "_" + COL_AUTHENTICATION_TIME, swipeAuthenticationTime[swipeAuthenticationTime.length - 1]);
        }

        contentValues.put(COL_USER_ID, swipe.getUserId());
        db.insert(TEST_AUTHENTICATION, null, contentValues);
    }

    /**
     * Builds the list of active models with regards to the selected value in the model profile (Full/Individual_Full/All).
     *
     * @return The list of active models.
     */
    public List<List<DatabaseHelper.ModelType>> getActiveModels() {
        List<List<DatabaseHelper.ModelType>> activeModels = new ArrayList<>();
        Integer modelsCombination = this.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS);

        if(modelsCombination == DatabaseHelper.ModelsCombinations.FULL.ordinal()) {
            activeModels.add(Arrays.asList(DatabaseHelper.ModelType.FULL));
        } else if(modelsCombination == DatabaseHelper.ModelsCombinations.INDIVIDUAL_FULL.ordinal()) {
            for(DatabaseHelper.ModelType modelType : DatabaseHelper.ModelType.values()) {
                activeModels.add(Arrays.asList(modelType));
            }
        } else {
            activeModels = getAllModelsCombinations();
        }

        return activeModels;
    }

    /**
     * Generates all possible combinations (of any size) of the individual models (Hold,Swipe,Keystroke,Signature).
     *
     * @return List containing the names of all the model combinations.
     */
    private List<List<DatabaseHelper.ModelType>> getAllModelsCombinations() {
        List<List<DatabaseHelper.ModelType>> allModels = new ArrayList<>();
        Generator.subset(DatabaseHelper.ModelType.HOLD, DatabaseHelper.ModelType.SWIPE, DatabaseHelper.ModelType.KEYSTROKE, DatabaseHelper.ModelType.SIGNATURE)
                .simple()
                .stream()
                .filter(s -> !s.isEmpty())
                .filter(s -> s.size() != DatabaseHelper.ModelType.values().length - 1)
                .forEach(x -> allModels.add(x));
        allModels.add(Arrays.asList(DatabaseHelper.ModelType.FULL));

        return allModels;
    }

    /**
     * Triggers the insertion of a given train interaction in the relevant table.
     *
     * @param swipe The train interaction that needs to be inserted in the relevant table.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean addTrainRecord(Swipe swipe) {
        return this.addSwipe(swipe, REAL_SWIPES);
    }

    /**
     * Triggers the insertion of a given synthetic interaction in the relevant table.
     *
     * @param swipe The synthetic interaction that needs to be inserted in the relevant table.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean addGANRecord(Swipe swipe) {
        return this.addSwipe(swipe, GAN_SWIPES);
    }

    /**
     * Triggers the insertion of a given set of synthetic interactions in the relevant table.
     *
     * @param swipes The list of synthetic interactions that need to be inserted in the relevant table.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean[] addGANRecords(ArrayList<Swipe> swipes) {
        boolean [] result = new boolean[swipes.size()];
        for (int i=0; i < swipes.size(); i++) {
            result[i] = this.addGANRecord(swipes.get(i));
        }
        return result;
    }

    /**
     * Triggers the insertion of a given test interaction in the relevant table.
     *
     * @param swipe The test interaction that needs to be inserted in the relevant table.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean addTestRecord(Swipe swipe) {
        return this.addSwipe(swipe, TEST_SWIPES);
    }

    /**
     * Retrieves the normalized values (min-max scaling) of the provided set of interactions and stores them to the specified table.
     *
     * @param allSwipes The original set of (non-normalized) interactions.
     * @param tableName The name of the table to which the normalized interaction needs to be added to.
     */
    public void addSwipesNormalized(ArrayList<Swipe> allSwipes, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Swipe swipe : allSwipes) {
            double[] normalizedValues = swipe.getNormalizedValues(allSwipes);
            ContentValues contentValues = new ContentValues();
            int values_idx = 0;

            for(String head_feature : head_features) {
                if(head_feature == COL_SEGMENTS_X || head_feature == COL_SEGMENTS_Y) {
                    String segment_str = "[";
                    for(int i = 0; i < this.getFeatureData().get(COL_SWIPE_SHAPE_SEGMENTS); i++) {
                        segment_str += normalizedValues[values_idx] + ",";
                        values_idx = values_idx + 1;
                    }
                    segment_str = segment_str.substring(0, segment_str.length() - 1);
                    segment_str += "]";

                    contentValues.put(head_feature, segment_str);
                } else {
                    contentValues.put(head_feature, normalizedValues[values_idx]);
                    values_idx = values_idx + 1;
                }
            }

            for(int i = 0; i < features.length; i++) {
                for (int j = 0; j < metrics.length; j++) {
                    for (int x = 0; x < dimensions.length; x++) {
                        if (x == 2 && i == 0) { continue; }

                        String col_name = metrics[j].toLowerCase() + "_" + dimensions[x].toLowerCase() + "_" + features[i].toLowerCase();
                        contentValues.put(col_name, normalizedValues[values_idx]);
                        values_idx = values_idx + 1;
                    }
                }
            }

            for(String keystroke_feature : keystroke_features) {
                java.lang.reflect.Method cur_method = null;
                try {
                    cur_method = swipe.getClass().getMethod("get" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)));

                    if((keystroke_feature == COL_KEYSTROKE_FULL_DURATION && (Double) cur_method.invoke(swipe) == -1.0) || cur_method.invoke(swipe) == null) {
                        continue;
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                if(keystroke_feature == COL_KEYSTROKE_FULL_DURATION) {
                    contentValues.put(keystroke_feature, normalizedValues[values_idx]);
                    values_idx = values_idx + 1;
                } else {
                    Integer feature_length = (this.getFeatureData().get(COL_PIN_LENGTH) - 1);
                    if (keystroke_feature == COL_KEYSTROKE_DURATIONS) {
                        feature_length = this.getFeatureData().get(COL_PIN_LENGTH);
                    }

                    String keystroke_str = "[";
                    for (int i = 0; i < feature_length; i++) {
                        keystroke_str += normalizedValues[values_idx] + ",";
                        values_idx = values_idx + 1;
                    }
                    keystroke_str = keystroke_str.substring(0, keystroke_str.length() - 1);
                    keystroke_str += "]";

                    contentValues.put(keystroke_feature, keystroke_str);
                }
            }

            for(String signature_feature : signature_features) {
                if(signature_feature == COL_SIGNATURE_SEGMENTS_X || signature_feature == COL_SIGNATURE_SEGMENTS_Y) {
                    String segment_str = "[";
                    for(int i = 0; i < this.getFeatureData().get(COL_SIGNATURE_SHAPE_SEGMENTS); i++) {
                        segment_str += normalizedValues[values_idx] + ",";
                        values_idx = values_idx + 1;
                    }
                    segment_str = segment_str.substring(0, segment_str.length() - 1);
                    segment_str += "]";

                    contentValues.put(signature_feature, segment_str);
                } else {
                    contentValues.put(signature_feature, normalizedValues[values_idx]);
                    values_idx = values_idx + 1;
                }
            }

            contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
            contentValues.put(COL_USER_ID, swipe.getUserId());

            db.insert(tableName, null, contentValues);
        }
    }

    /**
     * Retrieves all the stored interactions from the specified table.
     *
     * @param tableName The name of the table from which to get the interactions from.
     * @return The list of retrieved interactions as Swipe objects.
     */
    public ArrayList<Swipe> getAllSwipes(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        ArrayList<Swipe> swipes = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Swipe swipe = new Swipe();

            for(String head_feature : head_features) {
                java.lang.reflect.Method cur_method = null;
                try {
                    if(head_feature == COL_SEGMENTS_X || head_feature == COL_SEGMENTS_Y) {
                        cur_method = swipe.getClass().getMethod("set" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)), double[].class);
                        String cursor_str = cursor.getString(cursor.getColumnIndex(head_feature));
                        String[] cursor_array = cursor_str.replace("[", "").replace("]","").split(",");
                        cur_method.invoke(swipe, Arrays.stream(cursor_array).mapToDouble(Double::parseDouble).toArray());
                    } else {
                        cur_method = swipe.getClass().getMethod("set" + head_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, head_feature.substring(1)), double.class);
                        cur_method.invoke(swipe, cursor.getDouble(cursor.getColumnIndex(head_feature)));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            for(String feature : DatabaseHelper.features) {
                for(String metric : DatabaseHelper.metrics) {
                    for(String dimension : DatabaseHelper.dimensions) {
                        if (dimension == "Z" && feature == "Velocity") { continue; }

                        String col_name = metric.toLowerCase() + "_" + dimension.toLowerCase() + "_" + feature.toLowerCase();

                        java.lang.reflect.Method cur_method = null;
                        try {
                            cur_method = swipe.getClass().getMethod("set" + metric + dimension + feature, double.class);
                            cur_method.invoke(swipe, cursor.getDouble(cursor.getColumnIndex(col_name)));
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            for(String keystroke_feature : keystroke_features) {
                if(cursor.getColumnIndex(keystroke_feature) != -1) {
                    java.lang.reflect.Method cur_method = null;
                    try {
                        if(keystroke_feature == COL_KEYSTROKE_FULL_DURATION) {
                            cur_method = swipe.getClass().getMethod("set" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)));
                            cur_method.invoke(swipe);
                        } else {
                            cur_method = swipe.getClass().getMethod("set" + keystroke_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, keystroke_feature.substring(1)), double[].class);
                            String cursor_str = cursor.getString(cursor.getColumnIndex(keystroke_feature));
                            String[] cursor_array = cursor_str.replace("[", "").replace("]", "").split(",");
                            cur_method.invoke(swipe, Arrays.stream(cursor_array).mapToDouble(Double::parseDouble).toArray());
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            for(String signature_feature : signature_features) {
                if(cursor.getColumnIndex(signature_feature) != -1) {
                    java.lang.reflect.Method cur_method = null;
                    try {
                        if (signature_feature == COL_SIGNATURE_SEGMENTS_X || signature_feature == COL_SIGNATURE_SEGMENTS_Y) {
                            cur_method = swipe.getClass().getMethod("set" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)), double[].class);
                            String cursor_str = cursor.getString(cursor.getColumnIndex(signature_feature));
                            String[] cursor_array = cursor_str.replace("[", "").replace("]", "").split(",");
                            cur_method.invoke(swipe, Arrays.stream(cursor_array).mapToDouble(Double::parseDouble).toArray());
                        } else {
                            cur_method = swipe.getClass().getMethod("set" + signature_feature.substring(0, 1).toUpperCase() + LOWER_UNDERSCORE.to(LOWER_CAMEL, signature_feature.substring(1)), double.class);
                            cur_method.invoke(swipe, cursor.getDouble(cursor.getColumnIndex(signature_feature)));
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            swipe.setHoldingPosition(cursor.getDouble(cursor.getColumnIndex(COL_HOLDING_POSITION)));
            swipe.setUserId(cursor.getString(cursor.getColumnIndex(COL_USER_ID)));
            swipes.add(swipe);
            cursor.move(1);
        }
        cursor.close();

        return swipes;
    }

    /**
     * Gets the number of records contained in the specified table.
     *
     * @param tableName The name of the table from which the records need to be counted.
     * @return The number of records stored in the specified table.
     */
    public int getRecordsCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * Gets the set of individual test results from the specified column and user type (User/Attacker).
     *
     * @param userId The type of user (User/Attacker) from which to get the individual test results from.
     * @param column The column from which to get the individual results.
     * @return The set of individual results.
     */
    public ArrayList<double[]> getTestingData(String userId, String column) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES + " WHERE " + COL_USER_ID + " == '" + userId + "'";
        ArrayList<double[]> testingData = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            String cursor_str = cursor.getString(cursor.getColumnIndex(column));
            String[] cursor_array = cursor_str.replace("[", "").replace("]", "").split(",");

            testingData.add(Arrays.stream(cursor_array).mapToDouble(Double::parseDouble).toArray());
            cursor.move(1);
        }
        cursor.close();
        return testingData;

    }

    /**
     * Deletes all tables containing test-related data (TEST_SWIPES, TEST_SWIPES_NORMALIZED, TEST_RESULTS, TEST_AUTHENTICATION).
     */
    public void deleteTestingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TEST_SWIPES);
        db.execSQL("DELETE FROM " + TEST_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + TEST_RESULTS);
        db.execSQL("DELETE FROM " + TEST_AUTHENTICATION);
    }

    /**
     * Deletes all tables containing GAN-related data (GAN_SWIPES, GAN_SWIPES_NORMALIZED, GAN_RESULTS).
     */
    public void deleteGANData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GAN_SWIPES);
        db.execSQL("DELETE FROM " + GAN_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + GAN_RESULTS);
    }

    /**
     * Deletes the table containing resource-related data (RESOURCE_DATA).
     */
    public void deleteResourceData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RESOURCE_DATA);
    }

    /**
     * Deletes the table containing data related to the training results (REAL_RESULTS).
     */
    public void deleteRealResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REAL_RESULTS);
    }

    /**
     * Deletes the table containing the answers to the SUS questions (SUS_DATA).
     */
    public void deleteSUSData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SUS_DATA);
    }

    /**
     * Saves the train result metrics of the models generated using only the genuine samples to the relevant table.
     *
     * @param instances Number of test interactions used to generate the performance metrics.
     * @param TAR True Acceptance Rate.
     * @param FRR False Rejection Rate.
     * @param ER Error rate.
     * @param avgSampleTime Average duration of the test interactions.
     * @param trainingTime Time required to build the model.
     * @param classifierSamples Total interactions used to train the models (synthetic + non-synthetic interactions).
     * @param trainingModel Model name.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveRealResults(double instances, double TAR, double FRR, double ER, double avgSampleTime, double trainingTime, int classifierSamples, List<ModelType> trainingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_ER, ER);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);
        contentValues.put(COL_MODEL_TYPE, trainingModel.toString());

        long result = db.insert(REAL_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Saves the train result metrics of the models generated using the synthetic samples to the relevant table.
     *
     * @param instances Number of test interactions used to generate the performance metrics.
     * @param TAR True Acceptance Rate.
     * @param FRR False Rejection Rate.
     * @param avgSampleTime Average duration of the test interactions.
     * @param trainingTime Time required to build the model.
     * @param ganTime Time required to build the GAN and generate the synthetic samples.
     * @param classifierSamples Total interactions used to train the models (synthetic + non-synthetic interactions).
     * @param trainingModel Model name.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveGANResults(double instances, double TAR, double FRR, double ER, double avgSampleTime, double trainingTime, double ganTime, int classifierSamples, List<ModelType> trainingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_ER, ER);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_GAN_TIME, ganTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);
        contentValues.put(COL_MODEL_TYPE, trainingModel.toString());

        long result = db.insert(GAN_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Computes the weights assigned to the individual models for use in the ensemble.
     * Starting from the error rate of the individual models, a success index is computed for all the individual models (index(c) = 1 - (er(c) / (sum_{i=1}^{n} er(i)))
     * Then, the success index is used to computed the individual weights (weight(c) = index(c) / (sum_{i=1}^{n} index(i)))
     *
     * Weight calculation taken from "TSME: Multi-modal and Unobtrusive Behavioural User Authentication for Smartphones" (Buriro et al., 2016)
     *
     * @param isGanMode Defines whether the current models have been trained using also synthetic samples.
     * @return The set of weights in the form of an Hashmap with model types as keys.
     */
    public Map<ModelType, Double> getModelWeights(boolean isGanMode) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        if(isGanMode) {
            query = "SELECT * FROM " + GAN_RESULTS;
        } else {
            query = "SELECT * FROM " + REAL_RESULTS;
        }

        Map<ModelType, Double> ERData = new HashMap<>(); // Error rate
        Map<ModelType, Double> SIData = new HashMap<>();  // Success index
        Map<ModelType, Double> weightData = new HashMap<>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            String model_str = cursor.getString(cursor.getColumnIndex(COL_MODEL_TYPE));
            String[] model_array = model_str.replace("[", "").replace("]", "").split(",");

            if(model_array.length == 1 && !model_array[0].equals(ModelType.FULL.toString())) {
                Double ER = cursor.getDouble(cursor.getColumnIndex(COL_ER));
                ERData.put(ModelType.valueOf(model_array[0]), ER);
            }

            cursor.move(1);
        }
        cursor.close();

        for(ModelType modelType : ModelType.values()) {
            if(isModelEnabled(Arrays.asList(modelType)) && modelType != ModelType.FULL) {
                SIData.put(modelType, 1 - (ERData.get(modelType) / ERData.values().stream().mapToDouble(d-> d).sum()));
            }
        }
        for(ModelType modelType : ModelType.values()) {
            if(isModelEnabled(Arrays.asList(modelType)) && modelType != ModelType.FULL){
                weightData.put(modelType, (SIData.get(modelType) / SIData.values().stream().mapToDouble(d-> d).sum()));
            }
        }

        return weightData;
    }

    /**
     * Saves the test result metrics to the relevant table.
     *
     * @param instances Number of test interactions used to generate the performance metrics.
     * @param TAR True Acceptance Rate.
     * @param FRR False Rejection Rate.
     * @param TRR True Rejection Rate.
     * @param FAR False Acceptance Rate.
     * @param avgSampleTime Average duration of the test interactions.
     * @param avgTestTime Average time required by the model to process a single interaction.
     * @param classifierSamples Number of interactions used to train the models.
     * @param trainingModel Model name.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveTestResults(double instances, double TAR, double FRR, double TRR, double FAR, double avgSampleTime, double avgTestTime, int classifierSamples, String trainingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_TRR, TRR);
        contentValues.put(COL_FAR, FAR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_AVG_TEST_TIME, avgTestTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);
        contentValues.put(COL_MODEL_TYPE, trainingModel);

        long result = db.insert(TEST_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Saves user data to the relevant table.
     *
     * @param nickname Nickname used for the current user.
     * @param gender Gender of the user.
     * @param age Age range of the user.
     * @param nationality Nationality of the user.
     * @param holdingHand Hand used by the user to hold the device.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveUserData(String nickname, int gender, int age, String nationality, int holdingHand) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USER_DATA);

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NICKNAME, nickname);
        contentValues.put(COL_GENDER, gender);
        contentValues.put(COL_AGE, age);
        contentValues.put(COL_NATIONALITY, nationality);
        contentValues.put(COL_HOLDING_HAND, holdingHand);

        long result = db.insert(USER_DATA, null, contentValues);
        return result != -1;
    }

    /**
     * Gets the user data from the relevant table.
     *
     * @return The user data in the form of an Hashmap with column names as keys.
     */
    public Map<String, String> getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Map<String, String> userData = new HashMap<>();

        userData.put(COL_NICKNAME, cursor.getString(cursor.getColumnIndex(COL_NICKNAME)));
        userData.put(COL_GENDER, Integer.toString(cursor.getInt(cursor.getColumnIndex(COL_GENDER))));
        userData.put(COL_AGE, Integer.toString(cursor.getInt(cursor.getColumnIndex(COL_AGE))));
        userData.put(COL_NATIONALITY, cursor.getString(cursor.getColumnIndex(COL_NATIONALITY)));
        userData.put(COL_HOLDING_HAND, Integer.toString(cursor.getInt(cursor.getColumnIndex(COL_HOLDING_HAND))));

        cursor.close();

        return userData;
    }

    /**
     * Saves feature information to the relevant table.
     *
     * @param models_combinations Chosen model combination (Full, Individual_Full, All) used during training.
     * @param acceleration Indicates whether the features related to the device acceleration are enabled.
     * @param angular_velocity Indicates whether the features related to the angular velocity are enabled.
     * @param orientation Indicates whether the features related to the orientation are enabled.
     * @param swipe_duration Indicates whether the features related to the swipe duration are enabled.
     * @param swipe_shape Indicates whether the features related to the swipe shape are enabled.
     * @param swipe_shape_segments Indicates the number of segments used to decompose the swipe gesture.
     * @param swipe_touch_size Indicates whether the features related to the touch size are enabled.
     * @param swipe_start_end_pos Indicates whether the features related to the swipe start/end position are enabled.
     * @param swipe_velocity Indicates whether the features related to the swipe velocity are enabled.
     * @param keystroke Indicates whether the keystroke gesture is enabled.
     * @param pin_length Indicates length of the PIN code.
     * @param keystroke_durations Indicates whether the features related to the keystroke durations are enabled.
     * @param keystroke_intervals Indicates whether the features related to the keystroke intervals are enabled.
     * @param signature Indicates whether the signature gesture is enabled.
     * @param signature_start_end_pos Indicates whether the features related to the signature start/end positions are enabled.
     * @param signature_velocity Indicates whether the features related to the signature velocity are enabled.
     * @param signature_shape Indicates whether the features related to the signature shape are enabled.
     * @param signature_shape_segments Indicates the number of segments used to decompose the signature gesture.
     * @param raw_data Indicates whether the raw data collection is enabled.
     * @param raw_data_frequency Indicates the frequency (in Hz) used to collect the raw data entries.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveFeatureData(
            int models_combinations,
            int acceleration, int angular_velocity, int orientation,
            int swipe_duration, int swipe_shape, int swipe_shape_segments, int swipe_touch_size, int swipe_start_end_pos, int swipe_velocity,
            int keystroke, int pin_length, int keystroke_durations, int keystroke_intervals,
            int signature, int signature_start_end_pos, int signature_velocity, int signature_shape, int signature_shape_segments,
            int raw_data, int raw_data_frequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FEATURE_DATA);

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_MODELS_COMBINATIONS, models_combinations);
        contentValues.put(COL_ACCELERATION, acceleration);
        contentValues.put(COL_ANGULAR_VELOCITY, angular_velocity);
        contentValues.put(COL_ORIENTATION, orientation);
        contentValues.put(COL_SWIPE_DURATION, swipe_duration);
        contentValues.put(COL_SWIPE_SHAPE, swipe_shape);
        contentValues.put(COL_SWIPE_SHAPE_SEGMENTS, swipe_shape_segments);
        contentValues.put(COL_SWIPE_TOUCH_SIZE, swipe_touch_size);
        contentValues.put(COL_SWIPE_START_END_POS, swipe_start_end_pos);
        contentValues.put(COL_SWIPE_VELOCITY, swipe_velocity);
        contentValues.put(COL_KEYSTROKE, keystroke);
        contentValues.put(COL_PIN_LENGTH, pin_length);
        contentValues.put(COL_KEYSTROKE_DURATIONS, keystroke_durations);
        contentValues.put(COL_KEYSTROKE_INTERVALS, keystroke_intervals);
        contentValues.put(COL_SIGNATURE, signature);
        contentValues.put(COL_SIGNATURE_START_END_POS, signature_start_end_pos);
        contentValues.put(COL_SIGNATURE_VELOCITY, signature_velocity);
        contentValues.put(COL_SIGNATURE_SHAPE, signature_shape);
        contentValues.put(COL_SIGNATURE_SHAPE_SEGMENTS, signature_shape_segments);
        contentValues.put(COL_RAW_DATA, raw_data);
        contentValues.put(COL_RAW_DATA_FREQUENCY, raw_data_frequency);

        long result = db.insert(FEATURE_DATA, null, contentValues);
        return result != -1;
    }

    /**
     * Gets feature information from the relevant table.
     *
     * @return The feature data in the form of an Hashmap with column names as keys.
     */
    public Map<String, Integer> getFeatureData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FEATURE_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Map<String, Integer> featureData = new HashMap<>();

        featureData.put(COL_MODELS_COMBINATIONS, cursor.getInt(cursor.getColumnIndex(COL_MODELS_COMBINATIONS)));
        featureData.put(COL_ACCELERATION, cursor.getInt(cursor.getColumnIndex(COL_ACCELERATION)));
        featureData.put(COL_ANGULAR_VELOCITY, cursor.getInt(cursor.getColumnIndex(COL_ANGULAR_VELOCITY)));
        featureData.put(COL_ORIENTATION, cursor.getInt(cursor.getColumnIndex(COL_ORIENTATION)));
        featureData.put(COL_SWIPE_DURATION, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_DURATION)));
        featureData.put(COL_SWIPE_SHAPE, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_SHAPE)));
        featureData.put(COL_SWIPE_SHAPE_SEGMENTS, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_SHAPE_SEGMENTS)));
        featureData.put(COL_SWIPE_TOUCH_SIZE, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_TOUCH_SIZE)));
        featureData.put(COL_SWIPE_START_END_POS, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_START_END_POS)));
        featureData.put(COL_SWIPE_VELOCITY, cursor.getInt(cursor.getColumnIndex(COL_SWIPE_VELOCITY)));
        featureData.put(COL_KEYSTROKE, cursor.getInt(cursor.getColumnIndex(COL_KEYSTROKE)));
        featureData.put(COL_PIN_LENGTH, cursor.getInt(cursor.getColumnIndex(COL_PIN_LENGTH)));
        featureData.put(COL_KEYSTROKE_DURATIONS, cursor.getInt(cursor.getColumnIndex(COL_KEYSTROKE_DURATIONS)));
        featureData.put(COL_KEYSTROKE_INTERVALS, cursor.getInt(cursor.getColumnIndex(COL_KEYSTROKE_INTERVALS)));
        featureData.put(COL_SIGNATURE, cursor.getInt(cursor.getColumnIndex(COL_SIGNATURE)));
        featureData.put(COL_SIGNATURE_START_END_POS, cursor.getInt(cursor.getColumnIndex(COL_SIGNATURE_START_END_POS)));
        featureData.put(COL_SIGNATURE_VELOCITY, cursor.getInt(cursor.getColumnIndex(COL_SIGNATURE_VELOCITY)));
        featureData.put(COL_SIGNATURE_SHAPE, cursor.getInt(cursor.getColumnIndex(COL_SIGNATURE_SHAPE)));
        featureData.put(COL_SIGNATURE_SHAPE_SEGMENTS, cursor.getInt(cursor.getColumnIndex(COL_SIGNATURE_SHAPE_SEGMENTS)));
        featureData.put(COL_RAW_DATA, cursor.getInt(cursor.getColumnIndex(COL_RAW_DATA)));
        featureData.put(COL_RAW_DATA_FREQUENCY, cursor.getInt(cursor.getColumnIndex(COL_RAW_DATA_FREQUENCY)));

        cursor.close();

        return featureData;
    }

    /**
     * Checks whether a given model is currently enabled.
     * In order to be considered enabled, a given model needs to have at least one active feature.
     * Note that in this context a model can be represented by:
     *  The set of features belonging to one of the individual parts of the interactions (Hold/Swipe/Keystroke/Signature).
     *  A combination of features coming from two or more of the individual interaction parts.
     *  A Full model containing all the available features.
     *
     * @param models List containing the individual models (Hold,Swipe,Keystroke,Signature,Full) that are part of the one currently being analysed.
     * @return True if the model has at least one feature enabled. False otherwise.
     */
    public boolean isModelEnabled(List<ModelType> models) {
        for(ModelType model : models) {
            Integer feature_count = 0;

            if (model == ModelType.HOLD || model == ModelType.FULL) {
                feature_count += getFeatureData().get(COL_ACCELERATION) +
                        getFeatureData().get(COL_ANGULAR_VELOCITY) +
                        getFeatureData().get(COL_ORIENTATION);
            }
            if (model == ModelType.SWIPE || model == ModelType.FULL) {
                feature_count += getFeatureData().get(COL_SWIPE_DURATION) +
                        getFeatureData().get(COL_SWIPE_SHAPE) +
                        getFeatureData().get(COL_SWIPE_TOUCH_SIZE) +
                        getFeatureData().get(COL_SWIPE_START_END_POS) +
                        getFeatureData().get(COL_SWIPE_VELOCITY);
            }
            if ((model == ModelType.KEYSTROKE || model == ModelType.FULL) && getFeatureData().get(COL_KEYSTROKE) == 1) {
                feature_count += getFeatureData().get(COL_KEYSTROKE_DURATIONS) +
                        getFeatureData().get(COL_KEYSTROKE_INTERVALS);
            }
            if ((model == ModelType.SIGNATURE || model == ModelType.FULL) && getFeatureData().get(COL_SIGNATURE) == 1) {
                feature_count += getFeatureData().get(COL_SIGNATURE) +
                        getFeatureData().get(COL_SIGNATURE_START_END_POS) +
                        getFeatureData().get(COL_SIGNATURE_VELOCITY) +
                        getFeatureData().get(COL_SIGNATURE_SHAPE) +
                        getFeatureData().get(COL_SIGNATURE_SHAPE_SEGMENTS);
            }

            if(feature_count == 0) { return false; }
        }

        return true;
    }

    /**
     * Saves te device resource data collected during training to the relevant table.
     *
     * @param cpu_freq Array containing the minimum, maximum and average CPU frequencies for all CPU cores.
     * @param memory_usage Array containing the minimum, maximum and average memory usage.
     * @param power_draw Total power draw in microamperes.
     * @param training_time Training time in seconds.
     * @param model_type String name of the model the training resource data refers to.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveResourceData(String[] cpu_freq, Double[] memory_usage, Double power_draw, Double training_time, String model_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_MIN_CPU_FREQ, cpu_freq[0]);
        contentValues.put(COL_MAX_CPU_FREQ, cpu_freq[1]);
        contentValues.put(COL_AVG_CPU_FREQ, cpu_freq[2]);
        contentValues.put(COL_MIN_MEMORY_USAGE, memory_usage[0]);
        contentValues.put(COL_MAX_MEMORY_USAGE, memory_usage[1]);
        contentValues.put(COL_AVG_MEMORY_USAGE, memory_usage[2]);
        contentValues.put(COL_POWER_DRAW, power_draw);
        contentValues.put(COL_TRAINING_TIME, training_time);
        contentValues.put(COL_MODEL_TYPE, model_type);

        long result = db.insert(RESOURCE_DATA, null, contentValues);
        return result != -1;
    }

    /**
     * Saves the response to the SUS questions to the relevant table.
     *
     * @param answers Array containing the answer values of the SUS questions.
     * @return True if the interaction was successfully added to the DB; False otherwise.
     */
    public boolean saveSUSData(Integer[] answers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < DEFAULT_SUS_QUESTIONS; i++) {
            contentValues.put(COL_QUESTION + i, answers[i]);
        }
        contentValues.put(COL_USER_ID, this.getUserData().get(COL_NICKNAME));

        long result = db.insert(SUS_DATA, null, contentValues);
        return result != -1;
    }

    /**
     * Determines whether the current DB contains response data for the SUS questions.
     *
     * @return True if response data exists; False otherwise
     */
    public boolean hasSUSData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SUS_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getCount()>0;
    }

    /**
     * Saves the data contained in the specified table as a .csv file.
     *
     * @param tableName The name of the table that needs to be saved as .csv.
     * @param filePath Path defining where the .csv file will be saved.
     * @param resolver Object providing access to the content model.
     */
    public synchronized void saveAsCSV(String tableName, String filePath, ContentResolver resolver) {

        FileOutputStream fos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor curCSV = db.rawQuery("SELECT * FROM " + tableName,null);
        if (curCSV.getCount() == 0) {
            return;
        }
   
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "SwipeGAN");
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filePath);
                Uri fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(fileUri));
                CSVWriter csvWrite = new CSVWriter(new OutputStreamWriter(fos));

                String[] colNames = curCSV.getColumnNames();
                csvWrite.writeNext(colNames);
                while(curCSV.moveToNext())
                {
                    int colCounts = curCSV.getColumnCount();
                    String[] arrStr = new String[colCounts];
                    for (int i = 0; i < colCounts; i++) {
                        if (colNames[i].equals(COL_USER_ID) ||
                                colNames[i].equals(COL_NICKNAME) ||
                                colNames[i].equals(COL_NATIONALITY) ||
                                colNames[i].equals(COL_MODEL_TYPE) ||
                                colNames[i].equals(COL_AUTHENTICATION) ||
                                colNames[i].equals(COL_AUTHENTICATION_TIME) ||
                                colNames[i].equals(COL_SEGMENTS_X) ||
                                colNames[i].equals(COL_SEGMENTS_Y) ||
                                colNames[i].equals(COL_KEYSTROKE_DURATIONS) ||
                                colNames[i].equals(COL_KEYSTROKE_INTERVALS) ||
                                colNames[i].equals(COL_KEYSTROKE_START_INTERVALS) ||
                                colNames[i].equals(COL_KEYSTROKE_END_INTERVALS) ||
                                colNames[i].equals(COL_SIGNATURE_SEGMENTS_X) ||
                                colNames[i].equals(COL_SIGNATURE_SEGMENTS_Y) ||
                                colNames[i].equals(COL_MIN_CPU_FREQ) ||
                                colNames[i].equals(COL_MAX_CPU_FREQ) ||
                                colNames[i].equals(COL_AVG_CPU_FREQ)) {
                            arrStr[i] = curCSV.getString(i);
                        } else {
                            arrStr[i] = Double.toString(curCSV.getDouble(i));
                        }
                    }
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
                Objects.requireNonNull(fos);
            } else {

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + filePath);

                try
                {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    String[] colNames = curCSV.getColumnNames();
                    csvWrite.writeNext(colNames);
                    while(curCSV.moveToNext())
                    {
                        int colCounts = curCSV.getColumnCount();
                        String[] arrStr = new String[colCounts];
                        for (int i = 0; i < colCounts; i++) {
                            if (colNames[i].equals(COL_USER_ID) ||
                                    colNames[i].equals(COL_NICKNAME) ||
                                    colNames[i].equals(COL_NATIONALITY) ||
                                    colNames[i].equals(COL_MODEL_TYPE) ||
                                    colNames[i].equals(COL_AUTHENTICATION) ||
                                    colNames[i].equals(COL_AUTHENTICATION_TIME) ||
                                    colNames[i].equals(COL_SEGMENTS_X) ||
                                    colNames[i].equals(COL_SEGMENTS_Y) ||
                                    colNames[i].equals(COL_KEYSTROKE_DURATIONS) ||
                                    colNames[i].equals(COL_KEYSTROKE_INTERVALS) ||
                                    colNames[i].equals(COL_KEYSTROKE_START_INTERVALS) ||
                                    colNames[i].equals(COL_KEYSTROKE_END_INTERVALS) ||
                                    colNames[i].equals(COL_SIGNATURE_SEGMENTS_X) ||
                                    colNames[i].equals(COL_SIGNATURE_SEGMENTS_Y) ||
                                    colNames[i].equals(COL_MIN_CPU_FREQ) ||
                                    colNames[i].equals(COL_MAX_CPU_FREQ) ||
                                    colNames[i].equals(COL_AVG_CPU_FREQ)) {
                                arrStr[i] = curCSV.getString(i);
                            } else {
                                arrStr[i] = Double.toString(curCSV.getDouble(i));
                            }
                        }
                        csvWrite.writeNext(arrStr);
                    }
                    csvWrite.close();
                    curCSV.close();
                }
                catch(Exception sqlEx)
                {
                    Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                }

            }

        }
        catch(Exception e)
        {
            Log.e("DatabaseHelper", e.getMessage(), e);
        }
    }

    /**
     * Triggers .csv saving procedure for all DB tables.
     *
     * @param resolver Object providing access to the content model.
     */
    public void saveAllTablesAsCSV(ContentResolver resolver){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String currentDateTime = dateFormat.format(new Date());

        this.saveAsCSV(REAL_SWIPES, currentDateTime + "_" + "realSwipes.csv", resolver);
        this.saveAsCSV(GAN_SWIPES, currentDateTime + "_" + "ganSwipes.csv", resolver);
        this.saveAsCSV(TEST_SWIPES, currentDateTime + "_" + "testSwipes.csv", resolver);
        this.saveAsCSV(REAL_SWIPES_NORMALIZED, currentDateTime + "_" + "realSwipesNormalized.csv", resolver);
        this.saveAsCSV(GAN_SWIPES_NORMALIZED, currentDateTime + "_" + "ganSwipesNormalized.csv", resolver);
        this.saveAsCSV(TEST_SWIPES_NORMALIZED, currentDateTime + "_" + "testSwipesNormalized.csv", resolver);
        this.saveAsCSV(REAL_RESULTS, currentDateTime + "_" + "realResults.csv", resolver);
        this.saveAsCSV(GAN_RESULTS, currentDateTime + "_" + "ganResults.csv", resolver);
        this.saveAsCSV(TEST_RESULTS, currentDateTime + "_" + "testResults.csv", resolver);
        this.saveAsCSV(TEST_AUTHENTICATION, currentDateTime + "_" + "testAuthentication.csv", resolver);
        this.saveAsCSV(USER_DATA, currentDateTime + "_" + "userData.csv", resolver);
        this.saveAsCSV(FEATURE_DATA, currentDateTime + "_" + "featureData.csv", resolver);
        this.saveAsCSV(RESOURCE_DATA, currentDateTime + "_" + "resourceData.csv", resolver);
        this.saveAsCSV(TRAIN_RAW_DATA, currentDateTime + "_" + "rawData.csv", resolver);
        this.saveAsCSV(SUS_DATA, currentDateTime + "_" + "SUSData.csv", resolver);
    }

    /**
     * Deletes the current DB tables.
     *
     * @param GANOnly Defines whether only the GAN-related tables should be deleted.
     */
    public void resetDB(boolean GANOnly) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + GAN_SWIPES);
        db.execSQL("DELETE FROM " + GAN_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + GAN_RESULTS);
        db.execSQL("DELETE FROM " + REAL_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + TEST_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + RESOURCE_DATA);

        if(GANOnly == false) {
            db.execSQL("DELETE FROM " + REAL_SWIPES);
            db.execSQL("DELETE FROM " + TEST_SWIPES);
            db.execSQL("DELETE FROM " + REAL_RESULTS);
            db.execSQL("DELETE FROM " + TEST_RESULTS);
            db.execSQL("DELETE FROM " + TEST_AUTHENTICATION);
            db.execSQL("DELETE FROM " + TRAIN_RAW_DATA);
            db.execSQL("DELETE FROM " + SUS_DATA);
        }
    }

}
