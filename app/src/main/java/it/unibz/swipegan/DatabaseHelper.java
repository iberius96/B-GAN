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
    private static final String COL_AUTHENTICATION_FULL = "AUTHENTICATION_FULL";

    private static final String COL_AUTHENTICATION_TIME_HOLD = "AUTHENTICATION_TIME_HOLD";
    private static final String COL_AUTHENTICATION_TIME_SWIPE = "AUTHENTICATION_TIME_SWIPE";
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

    private static final String COL_MIN_CPU_TEMP = "min_cpu_temp";
    private static final String COL_MAX_CPU_TEMP = "max_cpu_temp";
    private static final String COL_AVG_CPU_TEMP = "avg_cpu_temp";
    private static final String COL_MIN_MEMORY_USAGE = "min_memory_usage";
    private static final String COL_MAX_MEMORY_USAGE = "max_memory_usage";
    private static final String COL_AVG_MEMORY_USAGE = "avg_memory_usage";
    private static final String COL_POWER_DRAW = "power_draw";

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

    public static enum ModelType {
        HOLD,
        SWIPE,
        FULL
    };

    public static final Integer BASE_FEATURES = 66; // TODO: Change this hardcoded value
    public static final Integer DEFAULT_SEGMENTS = 10;
    public static final Integer DEFAULT_PIN_LENGTH = 8;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

        swipes_base += COL_HOLDING_POSITION + " float(53), " + COL_USER_ID + " varchar(20))";

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
                + COL_SWIPE_VELOCITY + " integer(1),"
                + COL_KEYSTROKE + " integer(1),"
                + COL_PIN_LENGTH + " integer(1))";

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

        String[] swipes_tables = {REAL_SWIPES, GAN_SWIPES, TEST_SWIPES, REAL_SWIPES_NORMALIZED, GAN_SWIPES_NORMALIZED, TEST_SWIPES_NORMALIZED};
        for(int i = 0; i < swipes_tables.length; i++) {
            String create_statement = swipes_base.replace("BASE_TABLE", swipes_tables[i]);
            if(swipes_tables[i] == TEST_SWIPES) {
                create_statement = create_statement.replace(
                        COL_USER_ID,
                        COL_AUTHENTICATION_HOLD + " float(53), " +
                                    COL_AUTHENTICATION_SWIPE + " float(53), " +
                                    COL_AUTHENTICATION_FULL + " float(53), " +
                                    COL_AUTHENTICATION_TIME_HOLD + " float(53), " +
                                    COL_AUTHENTICATION_TIME_SWIPE + " float(53), " +
                                    COL_AUTHENTICATION_TIME_FULL + " float(53), " +
                                    COL_CLASSIFIER_SAMPLES + " float(53), " +
                                    COL_USER_ID
                );
            }
            db.execSQL(create_statement);
        }

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
                    COL_KEYSTROKE, COL_PIN_LENGTH};
            for(String feature_col : feature_cols) {
                if (feature_col == COL_SWIPE_SHAPE_SEGMENTS) {
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

        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
        contentValues.put(COL_USER_ID, swipe.getUserId());

        if(tableName == TEST_SWIPES) {
            contentValues.put(COL_AUTHENTICATION_HOLD, swipe.getAuthentication(ModelType.HOLD));
            contentValues.put(COL_AUTHENTICATION_SWIPE, swipe.getAuthentication(ModelType.SWIPE));
            contentValues.put(COL_AUTHENTICATION_FULL, swipe.getAuthentication(ModelType.FULL));

            contentValues.put(COL_AUTHENTICATION_TIME_HOLD, swipe.getAuthenticationTime(ModelType.HOLD));
            contentValues.put(COL_AUTHENTICATION_TIME_SWIPE, swipe.getAuthenticationTime(ModelType.SWIPE));
            contentValues.put(COL_AUTHENTICATION_TIME_FULL, swipe.getAuthenticationTime(ModelType.FULL));

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
                if(head_feature == DatabaseHelper.COL_SEGMENTS_X || head_feature == DatabaseHelper.COL_SEGMENTS_Y) {
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
            double[] testingValues = new double[6];
            testingValues[0] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_HOLD));
            testingValues[1] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_SWIPE));
            testingValues[2] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_FULL));

            testingValues[3] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_HOLD));
            testingValues[4] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_SWIPE));
            testingValues[5] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME_FULL));

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
            int keystroke, int pin_length) {
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

        cursor.close();

        return featureData;
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
                                colNames[i].equals(COL_SEGMENTS_Y)) {
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
                                    colNames[i].equals(COL_SEGMENTS_Y)) {
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
