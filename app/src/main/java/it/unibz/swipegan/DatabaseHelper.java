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
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "GAN.db";

    private static final String REAL_SWIPES = "REAL_SWIPES";
    private static final String REAL_SWIPES_NORMALIZED = "REAL_SWIPES_NORMALIZED";
    private static final String GAN_SWIPES = "GAN_SWIPES";
    private static final String GAN_SWIPES_NORMALIZED = "GAN_SWIPES_NORMALIZED";
    private static final String TEST_SWIPES = "TEST_SWIPES";
    private static final String TEST_SWIPES_NORMALIZED = "TEST_SWIPES_NORMALIZED";

    private static final String REAL_RESULTS = "REAL_RESULTS";
    private static final String GAN_RESULTS = "GAN_RESULTS";
    private static final String TEST_RESULTS = "TEST_RESULTS";

    private static final String USER_DATA = "USER_DATA";
    private static final String FEATURE_DATA = "FEATURE_DATA";
    private static final String RESOURCE_DATA = "RESOURCE_DATA";

    private static final String COL_AUTHENTICATION_HOLD = "AUTHENTICATION_HOLD";
    private static final String COL_AUTHENTICATION_SWIPE = "AUTHENTICATION_SWIPE";
    private static final String COL_AUTHENTICATION_KEYSTROKE = "AUTHENTICATION_KEYSTROKE";
    private static final String COL_AUTHENTICATION_SIGNATURE = "AUTHENTICATION_SIGNATURE";
    private static final String COL_AUTHENTICATION_FULL = "AUTHENTICATION_FULL";

    private static final String COL_AUTHENTICATION_TIME_HOLD = "AUTHENTICATION_TIME_HOLD";
    private static final String COL_AUTHENTICATION_TIME_SWIPE = "AUTHENTICATION_TIME_SWIPE";
    private static final String COL_AUTHENTICATION_TIME_KEYSTROKE = "AUTHENTICATION_TIME_KEYSTROKE";
    private static final String COL_AUTHENTICATION_TIME_SIGNATURE = "AUTHENTICATION_TIME_SIGNATURE";
    private static final String COL_AUTHENTICATION_TIME_FULL = "AUTHENTICATION_TIME_FULL";

    private static final String COL_DURATION = "duration";
    private static final String COL_LENGTH = "length";
    public static final String COL_SEGMENTS_X = "segments_x";
    public static final String COL_SEGMENTS_Y = "segments_y";
    private static final String COL_MIN_SIZE = "min_size";
    private static final String COL_MAX_SIZE = "max_size";
    private static final String COL_AVG_SIZE = "avg_size";
    private static final String COL_DOWN_SIZE = "down_size";
    private static final String COL_UP_SIZE = "up_size";
    private static final String COL_START_X = "start_x";
    private static final String COL_START_Y = "start_y";
    private static final String COL_END_X = "end_x";
    private static final String COL_END_Y = "end_y";
    private static final String COL_HOLDING_POSITION = "holding_position";

    private static final String COL_INSTANCES = "INSTANCES";
    private static final String COL_TAR = "TAR";
    private static final String COL_FRR = "FRR";
    private static final String COL_TRR = "TRR";
    private static final String COL_FAR = "FAR";
    private static final String COL_AVG_SAMPLE_TIME = "AVG_SAMPLE_TIME";
    private static final String COL_TRAINING_TIME = "TRAINING_TIME";
    private static final String COL_GAN_TIME = "GAN_TIME";
    private static final String COL_AVG_TEST_TIME = "AVG_TEST_TIME";
    private static final String COL_USER_ID = "USER_ID";
    private static final String COL_CLASSIFIER_SAMPLES = "CLASSIFIER_SAMPLES";
    private static final String COL_MODEL_TYPE = "MODEL_TYPE";

    public static final String COL_NICKNAME = "nickname";
    public static final String COL_GENDER = "gender";
    public static final String COL_AGE = "age";
    public static final String COL_NATIONALITY = "nationality";
    public static final String COL_HOLDING_HAND = "holding_hand";

    // Feature data columns
    public static final String COL_ACCELERATION = "acceleration";
    public static final String COL_ANGULAR_VELOCITY = "angular_velocity";
    public static final String COL_ORIENTATION = "orientation";
    public static final String COL_SWIPE_DURATION = "swipe_duration";
    public static final String COL_SWIPE_SHAPE = "swipe_shape";
    public static final String COL_SWIPE_SHAPE_SEGMENTS = "swipe_shape_segments";
    public static final String COL_SWIPE_TOUCH_SIZE = "swipe_touch_size";
    public static final String COL_SWIPE_START_END_POS = "swipe_start_end_pos";
    public static final String COL_SWIPE_VELOCITY = "swipe_velocity";
    public static final String COL_KEYSTROKE = "keystroke";
    public static final String COL_PIN_LENGTH = "pin_length";
    public static final String COL_SIGNATURE = "signature";
    public static final String COL_SIGNATURE_START_END_POS = "signature_start_end_pos";
    public static final String COL_SIGNATURE_VELOCITY = "signature_velocity";
    public static final String COL_SIGNATURE_SHAPE = "signature_shape";
    public static final String COL_SIGNATURE_SHAPE_SEGMENTS = "signature_shape_segments";

    // Resource columns
    private static final String COL_MIN_CPU_TEMP = "min_cpu_temp";
    private static final String COL_MAX_CPU_TEMP = "max_cpu_temp";
    private static final String COL_AVG_CPU_TEMP = "avg_cpu_temp";
    private static final String COL_MIN_MEMORY_USAGE = "min_memory_usage";
    private static final String COL_MAX_MEMORY_USAGE = "max_memory_usage";
    private static final String COL_AVG_MEMORY_USAGE = "avg_memory_usage";
    private static final String COL_POWER_DRAW = "power_draw";

    // Keystrokes columns
    public static final String COL_KEYSTROKE_DURATIONS = "keystroke_durations";
    public static final String COL_KEYSTROKE_INTERVALS = "keystroke_intervals";
    public static final String COL_KEYSTROKE_START_INTERVALS = "keystroke_start_intervals";
    public static final String COL_KEYSTROKE_END_INTERVALS = "keystroke_end_intervals";
    public static final String COL_KEYSTROKE_FULL_DURATION = "keystroke_full_duration";

    // Signature columns
    public static final String COL_SIGNATURE_START_X = "signature_start_x";
    public static final String COL_SIGNATURE_START_Y = "signature_start_y";
    public static final String COL_SIGNATURE_END_X = "signature_end_x";
    public static final String COL_SIGNATURE_END_Y = "signature_end_y";
    public static final String COL_SIGNATURE_STD_X = "signature_std_x";
    public static final String COL_SIGNATURE_STD_Y = "signature_std_y";
    public static final String COL_SIGNATURE_DIFF_X = "signature_diff_x";
    public static final String COL_SIGNATURE_DIFF_Y = "signature_diff_y";
    public static final String COL_SIGNATURE_EUCLIDEAN_DISTANCE = "signature_euclidean_distance";
    public static final String COL_SIGNATURE_AVG_X_VELOCITY = "signature_avg_x_velocity";
    public static final String COL_SIGNATURE_AVG_Y_VELOCITY = "signature_avg_y_velocity";
    public static final String COL_SIGNATURE_MAX_X_VELOCITY = "signature_max_x_velocity";
    public static final String COL_SIGNATURE_MAX_Y_VELOCITY = "signature_max_y_velocity";
    public static final String COL_SIGNATURE_SEGMENTS_X = "signature_segments_x";
    public static final String COL_SIGNATURE_SEGMENTS_Y = "signature_segments_y";

    public static final String[] head_features = {
            COL_DURATION,
            COL_LENGTH,
            COL_SEGMENTS_X, COL_SEGMENTS_Y,
            COL_MIN_SIZE, COL_MAX_SIZE, COL_AVG_SIZE, COL_DOWN_SIZE, COL_UP_SIZE,
            COL_START_X, COL_START_Y,
            COL_END_X, COL_END_Y,
    };

    // Used to reference velocity- and all swipe- related features
    public static final String[] features = {"Velocity", "Accelerometer", "Gyroscope", "Orientation"};
    public static final String[] metrics = {"Min", "Max", "Avg", "Var", "Std"};
    public static final String[] dimensions = {"X", "Y", "Z"};

    public static final String[] keystroke_features = {
            COL_KEYSTROKE_DURATIONS,
            COL_KEYSTROKE_INTERVALS,
            COL_KEYSTROKE_START_INTERVALS,
            COL_KEYSTROKE_END_INTERVALS,
            COL_KEYSTROKE_FULL_DURATION
    };

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

    public static enum ModelType {
        HOLD,
        SWIPE,
        KEYSTROKE,
        SIGNATURE,
        FULL
    };

    public static final Integer BASE_FEATURES = 66; // TODO: Change this hardcoded value
    public static final Integer DEFAULT_SEGMENTS = 10;
    public static final Integer DEFAULT_PIN_LENGTH = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRealResultsTable = "CREATE TABLE " + REAL_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53),"
                + COL_MODEL_TYPE + " varchar(20))";

        String createGanResultsTable = "CREATE TABLE " + GAN_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
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
                + COL_GENDER + " float(53), "
                + COL_AGE + " float(53), "
                + COL_NATIONALITY + " varchar(20), "
                + COL_HOLDING_HAND + " float(53))";

        String createFeatureDataTable = "CREATE TABLE " + FEATURE_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
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
                + COL_SIGNATURE_SHAPE_SEGMENTS + " integer(2))";

        String createResourceDataTable = "CREATE TABLE " + RESOURCE_DATA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MIN_CPU_TEMP + " float(53), "
                + COL_MAX_CPU_TEMP + " float(53), "
                + COL_AVG_CPU_TEMP + " float(53), "
                + COL_MIN_MEMORY_USAGE + " float(53), "
                + COL_MAX_MEMORY_USAGE + " float(53), "
                + COL_AVG_MEMORY_USAGE + " float(53), "
                + COL_POWER_DRAW + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_MODEL_TYPE + " varchar(20))";

        this.generateSwipesTables(db,false, true, true);

        db.execSQL(createRealResultsTable);
        db.execSQL(createGanResultsTable);
        db.execSQL(createTestResultsTable);

        db.execSQL(createUserDataTable);
        db.execSQL(createFeatureDataTable);
        db.execSQL(createResourceDataTable);

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
                    COL_ACCELERATION, COL_ANGULAR_VELOCITY, COL_ORIENTATION,
                    COL_SWIPE_DURATION, COL_SWIPE_SHAPE, COL_SWIPE_SHAPE_SEGMENTS, COL_SWIPE_TOUCH_SIZE, COL_SWIPE_START_END_POS, COL_SWIPE_VELOCITY,
                    COL_KEYSTROKE, COL_PIN_LENGTH, COL_KEYSTROKE_DURATIONS, COL_KEYSTROKE_INTERVALS,
                    COL_SIGNATURE, COL_SIGNATURE_START_END_POS, COL_SIGNATURE_VELOCITY, COL_SIGNATURE_SHAPE, COL_SIGNATURE_SHAPE_SEGMENTS};

            for(String feature_col : feature_cols) {
                if (feature_col == COL_SWIPE_SHAPE_SEGMENTS || feature_col == COL_SIGNATURE_SHAPE_SEGMENTS) {
                    contentValues.put(feature_col, DEFAULT_SEGMENTS);
                } else if(feature_col == COL_PIN_LENGTH) {
                    contentValues.put(feature_col, DEFAULT_PIN_LENGTH);
                } else {
                    contentValues.put(feature_col, 1);
                }
            }
            db.insert(FEATURE_DATA, null, contentValues);
        }
    }

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
                        COL_AUTHENTICATION_HOLD + " float(53), " +
                                COL_AUTHENTICATION_SWIPE + " float(53), " +
                                (hasKeystrokes ? (COL_AUTHENTICATION_KEYSTROKE + " float(53), ") : "") +
                                (hasSignature ? (COL_AUTHENTICATION_SIGNATURE + " float(53), ") : "") +
                                COL_AUTHENTICATION_FULL + " float(53), " +
                                COL_AUTHENTICATION_TIME_HOLD + " float(53), " +
                                COL_AUTHENTICATION_TIME_SWIPE + " float(53), " +
                                (hasKeystrokes ? (COL_AUTHENTICATION_TIME_KEYSTROKE + " float(53), ") : "") +
                                (hasSignature ? (COL_AUTHENTICATION_TIME_SIGNATURE + " float(53), ") : "") +
                                COL_AUTHENTICATION_TIME_FULL + " float(53), " +
                                COL_CLASSIFIER_SAMPLES + " float(53), " +
                                COL_USER_ID
                );
            }
            db.execSQL(create_statement);
        }
    }

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
            USER_DATA,
            FEATURE_DATA,
            RESOURCE_DATA
        };

        for(int i = 0; i < upgrade_tables.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + upgrade_tables[i]);
        }

        onCreate(db);
    }

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
            contentValues.put(COL_AUTHENTICATION_HOLD, swipe.getAuthentication(ModelType.HOLD));
            contentValues.put(COL_AUTHENTICATION_SWIPE, swipe.getAuthentication(ModelType.SWIPE));
            contentValues.put(COL_AUTHENTICATION_FULL, swipe.getAuthentication(ModelType.FULL));

            contentValues.put(COL_AUTHENTICATION_TIME_HOLD, swipe.getAuthenticationTime(ModelType.HOLD));
            contentValues.put(COL_AUTHENTICATION_TIME_SWIPE, swipe.getAuthenticationTime(ModelType.SWIPE));
            contentValues.put(COL_AUTHENTICATION_TIME_FULL, swipe.getAuthenticationTime(ModelType.FULL));

            if(this.getFeatureData().get(COL_KEYSTROKE) == 1) {
                contentValues.put(COL_AUTHENTICATION_KEYSTROKE, swipe.getAuthentication(ModelType.KEYSTROKE));
                contentValues.put(COL_AUTHENTICATION_TIME_KEYSTROKE, swipe.getAuthenticationTime(ModelType.KEYSTROKE));
            }

            if(this.getFeatureData().get(COL_SIGNATURE) == 1) {
                contentValues.put(COL_AUTHENTICATION_SIGNATURE, swipe.getAuthentication(ModelType.SIGNATURE));
                contentValues.put(COL_AUTHENTICATION_TIME_SIGNATURE, swipe.getAuthenticationTime(ModelType.SIGNATURE));
            }

            contentValues.put(COL_CLASSIFIER_SAMPLES, swipe.getClassifierSamples());
        }

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTrainRecord(Swipe swipe) {
        return this.addSwipe(swipe, REAL_SWIPES);
    }

    public boolean addGANRecord(Swipe swipe) {
        return this.addSwipe(swipe, GAN_SWIPES);
    }

    public boolean[] addGANRecords(ArrayList<Swipe> swipes) {
        boolean [] result = new boolean[swipes.size()];
        for (int i=0; i < swipes.size(); i++) {
            result[i] = this.addGANRecord(swipes.get(i));
        }
        return result;
    }

    public boolean addTestRecord(Swipe swipe) {
        return this.addSwipe(swipe, TEST_SWIPES);
    }

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

    public int getRecordsCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // REFACTOR
    public ArrayList<double[]> getTestingData(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES + " WHERE " + COL_USER_ID + " == '" + userId + "'";
        ArrayList<double[]> testingData = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            double[] testingValues = new double[10];
            testingValues[0] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_HOLD));
            testingValues[1] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_SWIPE));
            testingValues[2] = this.getFeatureData().get(COL_KEYSTROKE) == 1 ? cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_KEYSTROKE)) : 0;
            testingValues[3] = this.getFeatureData().get(COL_SIGNATURE) == 1 ? cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_SIGNATURE)) : 0;
            testingValues[4] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_FULL));

            testingValues[5] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_HOLD));
            testingValues[6] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_SWIPE));
            testingValues[7] = this.getFeatureData().get(COL_KEYSTROKE) == 1 ? cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_KEYSTROKE)) : 0;
            testingValues[8] = this.getFeatureData().get(COL_SIGNATURE) == 1 ? cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_SIGNATURE)) : 0;
            testingValues[9] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_FULL));

            testingData.add(testingValues);
            cursor.move(1);
        }
        cursor.close();
        return testingData;

    }

    public void deleteTestingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TEST_SWIPES);
        db.execSQL("DELETE FROM " + TEST_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + TEST_RESULTS);
    }

    public void deleteGANData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GAN_SWIPES);
        db.execSQL("DELETE FROM " + GAN_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + GAN_RESULTS);
    }

    public void deleteResourceData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RESOURCE_DATA);
    }

    public void deleteRealResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REAL_RESULTS);
    }

    public boolean saveRealResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime, int classifierSamples, ModelType modelType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);
        contentValues.put(COL_MODEL_TYPE, modelType.name());

        long result = db.insert(REAL_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveGANResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime, double ganTime, int classifierSamples, ModelType modelType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_GAN_TIME, ganTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);
        contentValues.put(COL_MODEL_TYPE, modelType.name());

        long result = db.insert(GAN_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveTestResults(double instances, double TAR, double FRR, double TRR, double FAR, double avgSampleTime, double avgTestTime, int classifierSamples, ModelType modelType) {
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
        contentValues.put(COL_MODEL_TYPE, modelType.name());

        long result = db.insert(TEST_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveUserData(String nickname, double gender, double age, String nationality, double holdingHand) {
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

    public Map<String, String> getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Map<String, String> userData = new HashMap<>();

        userData.put(COL_NICKNAME, cursor.getString(cursor.getColumnIndex(COL_NICKNAME)));
        userData.put(COL_GENDER, Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_GENDER))));
        userData.put(COL_AGE, Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_AGE))));
        userData.put(COL_NATIONALITY, cursor.getString(cursor.getColumnIndex(COL_NATIONALITY)));
        userData.put(COL_HOLDING_HAND, Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_HOLDING_HAND))));

        cursor.close();

        return userData;
    }

    public boolean saveFeatureData(
            int acceleration, int angular_velocity, int orientation,
            int swipe_duration, int swipe_shape, int swipe_shape_segments, int swipe_touch_size, int swipe_start_end_pos, int swipe_velocity,
            int keystroke, int pin_length, int keystroke_durations, int keystroke_intervals,
            int signature, int signature_start_end_pos, int signature_velocity, int signature_shape, int signature_shape_segments) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FEATURE_DATA);

        ContentValues contentValues = new ContentValues();

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

        long result = db.insert(FEATURE_DATA, null, contentValues);
        return result != -1;
    }

    public Map<String, Integer> getFeatureData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FEATURE_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Map<String, Integer> featureData = new HashMap<>();

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

        cursor.close();

        return featureData;
    }

    public Integer getEnabledFeatureTypesCount(ModelType model) {
        Integer ret = 0;

        if(model == ModelType.HOLD || model == ModelType.FULL) {
            ret +=  getFeatureData().get(COL_ACCELERATION) +
                    getFeatureData().get(COL_ANGULAR_VELOCITY) +
                    getFeatureData().get(COL_ORIENTATION);
        }
        if(model == ModelType.SWIPE || model == ModelType.FULL) {
            ret +=  getFeatureData().get(COL_SWIPE_DURATION) +
                    getFeatureData().get(COL_SWIPE_SHAPE) +
                    getFeatureData().get(COL_SWIPE_TOUCH_SIZE) +
                    getFeatureData().get(COL_SWIPE_START_END_POS) +
                    getFeatureData().get(COL_SWIPE_VELOCITY);
        }
        if((model == ModelType.KEYSTROKE || model == ModelType.FULL) && getFeatureData().get(COL_KEYSTROKE) == 1) {
            ret +=  getFeatureData().get(COL_KEYSTROKE_DURATIONS) +
                    getFeatureData().get(COL_KEYSTROKE_INTERVALS);
        }
        if((model == ModelType.SIGNATURE || model == ModelType.FULL) && getFeatureData().get(COL_SIGNATURE) == 1) {
            ret +=  getFeatureData().get(COL_SIGNATURE) +
                    getFeatureData().get(COL_SIGNATURE_START_END_POS) +
                    getFeatureData().get(COL_SIGNATURE_VELOCITY) +
                    getFeatureData().get(COL_SIGNATURE_SHAPE) +
                    getFeatureData().get(COL_SIGNATURE_SHAPE_SEGMENTS);
        }

        return ret;
    }

    public boolean saveResourceData(Double[] cpu_temps, Double[] memory_usage, Double power_draw, Double training_time, String model_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_MIN_CPU_TEMP, cpu_temps[0]);
        contentValues.put(COL_MAX_CPU_TEMP, cpu_temps[1]);
        contentValues.put(COL_AVG_CPU_TEMP, cpu_temps[2]);
        contentValues.put(COL_MIN_MEMORY_USAGE, memory_usage[0]);
        contentValues.put(COL_MAX_MEMORY_USAGE, memory_usage[1]);
        contentValues.put(COL_AVG_MEMORY_USAGE, memory_usage[2]);
        contentValues.put(COL_POWER_DRAW, power_draw);
        contentValues.put(COL_TRAINING_TIME, training_time);
        contentValues.put(COL_MODEL_TYPE, model_type);

        long result = db.insert(RESOURCE_DATA, null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getResourceData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RESOURCE_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        ArrayList<String> resourceData = new ArrayList<>();

        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_MIN_CPU_TEMP))));
        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_MAX_CPU_TEMP))));
        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_AVG_CPU_TEMP))));
        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_MIN_MEMORY_USAGE))));
        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_MAX_MEMORY_USAGE))));
        resourceData.add(Float.toString(cursor.getFloat(cursor.getColumnIndex(COL_AVG_MEMORY_USAGE))));
        resourceData.add(Long.toString(cursor.getLong(cursor.getColumnIndex(COL_POWER_DRAW))));
        resourceData.add(Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_TRAINING_TIME))));
        resourceData.add(cursor.getString(cursor.getColumnIndex(COL_MODEL_TYPE)));

        cursor.close();

        return resourceData;
    }

    public synchronized void saveAsCSV(String tableName, String filePath, ContentResolver resolver, String downloadPath) {

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
                                colNames[i].equals(COL_NICKNAME) ||
                                colNames[i].equals(COL_MODEL_TYPE) ||
                                colNames[i].equals(COL_SEGMENTS_X) ||
                                colNames[i].equals(COL_SEGMENTS_Y) ||
                                colNames[i].equals(COL_KEYSTROKE_DURATIONS) ||
                                colNames[i].equals(COL_KEYSTROKE_INTERVALS) ||
                                colNames[i].equals(COL_KEYSTROKE_START_INTERVALS) ||
                                colNames[i].equals(COL_KEYSTROKE_END_INTERVALS) ||
                                colNames[i].equals(COL_SIGNATURE_SEGMENTS_X) ||
                                colNames[i].equals(COL_SIGNATURE_SEGMENTS_Y)) {
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
                                    colNames[i].equals(COL_NICKNAME) ||
                                    colNames[i].equals(COL_MODEL_TYPE) ||
                                    colNames[i].equals(COL_SEGMENTS_X) ||
                                    colNames[i].equals(COL_SEGMENTS_Y) ||
                                    colNames[i].equals(COL_KEYSTROKE_DURATIONS) ||
                                    colNames[i].equals(COL_KEYSTROKE_INTERVALS) ||
                                    colNames[i].equals(COL_KEYSTROKE_START_INTERVALS) ||
                                    colNames[i].equals(COL_KEYSTROKE_END_INTERVALS) ||
                                    colNames[i].equals(COL_SIGNATURE_SEGMENTS_X) ||
                                    colNames[i].equals(COL_SIGNATURE_SEGMENTS_Y)) {
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

    public void saveAllTablesAsCSV(ContentResolver resolver, String downloadPath){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String currentDateTime = dateFormat.format(new Date());

        this.saveAsCSV(REAL_SWIPES, currentDateTime + "_" + "realSwipes.csv", resolver, downloadPath);
        this.saveAsCSV(GAN_SWIPES, currentDateTime + "_" + "ganSwipes.csv", resolver, downloadPath);
        this.saveAsCSV(TEST_SWIPES, currentDateTime + "_" + "testSwipes.csv", resolver, downloadPath);
        this.saveAsCSV(REAL_SWIPES_NORMALIZED, currentDateTime + "_" + "realSwipesNormalized.csv", resolver, downloadPath);
        this.saveAsCSV(GAN_SWIPES_NORMALIZED, currentDateTime + "_" + "ganSwipesNormalized.csv", resolver, downloadPath);
        this.saveAsCSV(TEST_SWIPES_NORMALIZED, currentDateTime + "_" + "testSwipesNormalized.csv", resolver, downloadPath);
        this.saveAsCSV(REAL_RESULTS, currentDateTime + "_" + "realResults.csv", resolver, downloadPath);
        this.saveAsCSV(GAN_RESULTS, currentDateTime + "_" + "ganResults.csv", resolver, downloadPath);
        this.saveAsCSV(TEST_RESULTS, currentDateTime + "_" + "testResults.csv", resolver, downloadPath);
        this.saveAsCSV(USER_DATA, currentDateTime + "_" + "userData.csv", resolver, downloadPath);
        this.saveAsCSV(FEATURE_DATA, currentDateTime + "_" + "featureData.csv", resolver, downloadPath);
        this.saveAsCSV(RESOURCE_DATA, currentDateTime + "_" + "resourceData.csv", resolver, downloadPath);
    }

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
        }
    }

}
