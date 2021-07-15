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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GAN.db";

    private static final String REAL_SWIPES = "REAL_SWIPES";
    private static final String REAL_SWIPES_NORMALIZED = "REAL_SWIPES_NORMALIZED";
    private static final String GAN_SWIPES = "GAN_SWIPES";
    private static final String GAN_SWIPES_NORMALIZED = "GAN_SWIPES_NORMALIZED";

    private static final String COL_ID = "id";
    private static final String COL_DURATION = "duration";
    private static final String COL_AVG_SIZE = "avg_size";
    private static final String COL_DOWN_SIZE = "down_size";
    private static final String COL_DOWN_PRESSURE = "down_pressure";
    private static final String COL_START_X = "start_x";
    private static final String COL_START_Y = "start_y";
    private static final String COL_END_X = "end_x";
    private static final String COL_END_Y = "end_y";
    private static final String COL_MIN_X_VELOCITY = "min_x_velocity";
    private static final String COL_MAX_X_VELOCITY = "max_x_velocity";
    private static final String COL_AVG_X_VELOCITY = "avg_x_velocity";
    private static final String COL_STD_X_VELOCITY = "std_x_velocity";
    private static final String COL_VAR_X_VELOCITY = "var_x_velocity";
    private static final String COL_MIN_Y_VELOCITY = "min_y_velocity";
    private static final String COL_MAX_Y_VELOCITY = "max_y_velocity";
    private static final String COL_AVG_Y_VELOCITY = "avg_y_velocity";
    private static final String COL_STD_Y_VELOCITY = "std_y_velocity";
    private static final String COL_VAR_Y_VELOCITY = "var_y_velocity";
    private static final String COL_MIN_X_ACCELERATION = "min_x_acceleration";
    private static final String COL_MAX_X_ACCELERATION = "max_x_acceleration";
    private static final String COL_AVG_X_ACCELERATION = "avg_x_acceleration";
    private static final String COL_STD_X_ACCELERATION = "std_x_acceleration";
    private static final String COL_VAR_X_ACCELERATION = "var_x_acceleration";
    private static final String COL_MIN_Y_ACCELERATION = "min_y_acceleration";
    private static final String COL_MAX_Y_ACCELERATION = "max_y_acceleration";
    private static final String COL_AVG_Y_ACCELERATION = "avg_y_acceleration";
    private static final String COL_STD_Y_ACCELERATION = "std_y_acceleration";
    private static final String COL_VAR_Y_ACCELERATION = "var_y_acceleration";
    private static final String COL_MIN_PRESSURE = "min_pressure";
    private static final String COL_MAX_PRESSURE = "max_pressure";
    private static final String COL_AVG_PRESSURE = "avg_pressure";
    private static final String COL_STD_PRESSURE = "std_pressure";
    private static final String COL_VAR_PRESSURE = "var_pressure";

    private static final String TEST_SWIPES = "TEST_SWIPES";
    private static final String TEST_SWIPES_NORMALIZED = "TEST_SWIPES_NORMALIZED";
    private static final String COL_AUTHENTICATION = "AUTHENTICATION";
    private static final String COL_AUTHENTICATION_TIME = "AUTHENTICATION_TIME";

    private static final String REAL_RESULTS = "REAL_RESULTS";
    private static final String GAN_RESULTS = "GAN_RESULTS";
    private static final String TEST_RESULTS = "TEST_RESULTS";

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRealSwipesTable = "CREATE TABLE " + REAL_SWIPES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createGanSwipesTable = "CREATE TABLE " + GAN_SWIPES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createTestSwipesTable = "CREATE TABLE " + TEST_SWIPES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_AUTHENTICATION + " float(53), "
                + COL_AUTHENTICATION_TIME + " float(53), "
                + COL_USER_ID + " varchar(20), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        String createRealSwipesNormalizedTable = "CREATE TABLE " + REAL_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createGanSwipesNormalizedTable = "CREATE TABLE " + GAN_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createTestSwipesNormalizedTable = "CREATE TABLE " + TEST_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
                + COL_DOWN_PRESSURE + " float(53), "
                + COL_START_X + " float(53), "
                + COL_START_Y + " float(53), "
                + COL_END_X + " float(53), "
                + COL_END_Y + " float(53), "
                + COL_MIN_X_VELOCITY + " float(53), "
                + COL_MAX_X_VELOCITY + " float(53), "
                + COL_AVG_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_AUTHENTICATION + " float(53), "
                + COL_AUTHENTICATION_TIME + " float(53), "
                + COL_USER_ID + " varchar(20), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        String createRealResultsTable = "CREATE TABLE " + REAL_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        String createGanResultsTable = "CREATE TABLE " + GAN_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_GAN_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        String createTestResultsTable = "CREATE TABLE " + TEST_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_TRR + " float(53), "
                + COL_FAR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_AVG_TEST_TIME + " float(53), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        db.execSQL(createRealSwipesTable);
        db.execSQL(createGanSwipesTable);
        db.execSQL(createTestSwipesTable);

        db.execSQL(createRealSwipesNormalizedTable);
        db.execSQL(createGanSwipesNormalizedTable);
        db.execSQL(createTestSwipesNormalizedTable);

        db.execSQL(createRealResultsTable);
        db.execSQL(createGanResultsTable);
        db.execSQL(createTestResultsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REAL_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + GAN_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + REAL_SWIPES_NORMALIZED);
        db.execSQL("DROP TABLE IF EXISTS " + GAN_SWIPES_NORMALIZED);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_SWIPES_NORMALIZED);
        db.execSQL("DROP TABLE IF EXISTS " + REAL_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + GAN_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_RESULTS);
        onCreate(db);
    }

    private boolean addSwipe(Swipe swipe, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipe.getDuration());
        contentValues.put(COL_AVG_SIZE, swipe.getAvgSize());
        contentValues.put(COL_DOWN_SIZE, swipe.getDownSize());
        contentValues.put(COL_DOWN_PRESSURE, swipe.getDownPressure());
        contentValues.put(COL_START_X, swipe.getStartX());
        contentValues.put(COL_START_Y, swipe.getStartY());
        contentValues.put(COL_END_X, swipe.getEndX());
        contentValues.put(COL_END_Y, swipe.getEndY());
        contentValues.put(COL_MIN_X_VELOCITY, swipe.getMinXVelocity());
        contentValues.put(COL_MAX_X_VELOCITY, swipe.getMaxXVelocity());
        contentValues.put(COL_AVG_X_VELOCITY, swipe.getAvgXVelocity());
        contentValues.put(COL_STD_X_VELOCITY, swipe.getStdXVelocity());
        contentValues.put(COL_VAR_X_VELOCITY, swipe.getVarXVelocity());
        contentValues.put(COL_MIN_Y_VELOCITY, swipe.getMinYVelocity());
        contentValues.put(COL_MAX_Y_VELOCITY, swipe.getMaxYVelocity());
        contentValues.put(COL_AVG_Y_VELOCITY, swipe.getAvgYVelocity());
        contentValues.put(COL_STD_Y_VELOCITY, swipe.getStdYVelocity());
        contentValues.put(COL_VAR_Y_VELOCITY, swipe.getVarYVelocity());
        contentValues.put(COL_MIN_X_ACCELERATION, swipe.getMinXAcceleration());
        contentValues.put(COL_MAX_X_ACCELERATION, swipe.getMaxXAcceleration());
        contentValues.put(COL_AVG_X_ACCELERATION, swipe.getAvgXAcceleration());
        contentValues.put(COL_STD_X_ACCELERATION, swipe.getStdXAcceleration());
        contentValues.put(COL_VAR_X_ACCELERATION, swipe.getVarXAcceleration());
        contentValues.put(COL_MIN_Y_ACCELERATION, swipe.getMinYAcceleration());
        contentValues.put(COL_MAX_Y_ACCELERATION, swipe.getMaxYAcceleration());
        contentValues.put(COL_AVG_Y_ACCELERATION, swipe.getAvgYAcceleration());
        contentValues.put(COL_STD_Y_ACCELERATION, swipe.getStdYAcceleration());
        contentValues.put(COL_VAR_Y_ACCELERATION, swipe.getVarYAcceleration());
        contentValues.put(COL_MIN_PRESSURE, swipe.getMinPressure());
        contentValues.put(COL_MAX_PRESSURE, swipe.getMaxPressure());
        contentValues.put(COL_AVG_PRESSURE, swipe.getAvgPressure());
        contentValues.put(COL_STD_PRESSURE, swipe.getStdPressure());
        contentValues.put(COL_VAR_PRESSURE, swipe.getVarPressure());
        contentValues.put(COL_USER_ID, swipe.getUserId());

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTrainRecord(Swipe swipe) {
        return this.addSwipe(swipe, REAL_SWIPES) && this.addSwipeNormalized(swipe, REAL_SWIPES_NORMALIZED);
    }

    public boolean addGANRecord(Swipe swipe) {
        return this.addSwipe(swipe, GAN_SWIPES) && this.addSwipeNormalized(swipe, GAN_SWIPES_NORMALIZED);
    }

    public boolean[] addGANRecords(ArrayList<Swipe> swipes) {
        boolean [] result = new boolean[swipes.size()];
        for (int i=0; i < swipes.size(); i++) {
            result[i] = this.addGANRecord(swipes.get(i));
        }
        return result;
    }

    public boolean addTestSwipe(Swipe swipe, double authentication, double authenticationTime, String tableName, int classifierSamples) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipe.getDuration());
        contentValues.put(COL_AVG_SIZE, swipe.getAvgSize());
        contentValues.put(COL_DOWN_SIZE, swipe.getDownSize());
        contentValues.put(COL_DOWN_PRESSURE, swipe.getDownPressure());
        contentValues.put(COL_START_X, swipe.getStartX());
        contentValues.put(COL_START_Y, swipe.getStartY());
        contentValues.put(COL_END_X, swipe.getEndX());
        contentValues.put(COL_END_Y, swipe.getEndY());
        contentValues.put(COL_MIN_X_VELOCITY, swipe.getMinXVelocity());
        contentValues.put(COL_MAX_X_VELOCITY, swipe.getMaxXVelocity());
        contentValues.put(COL_AVG_X_VELOCITY, swipe.getAvgXVelocity());
        contentValues.put(COL_STD_X_VELOCITY, swipe.getStdXVelocity());
        contentValues.put(COL_VAR_X_VELOCITY, swipe.getVarXVelocity());
        contentValues.put(COL_MIN_Y_VELOCITY, swipe.getMinYVelocity());
        contentValues.put(COL_MAX_Y_VELOCITY, swipe.getMaxYVelocity());
        contentValues.put(COL_AVG_Y_VELOCITY, swipe.getAvgYVelocity());
        contentValues.put(COL_STD_Y_VELOCITY, swipe.getStdYVelocity());
        contentValues.put(COL_VAR_Y_VELOCITY, swipe.getVarYVelocity());
        contentValues.put(COL_MIN_X_ACCELERATION, swipe.getMinXAcceleration());
        contentValues.put(COL_MAX_X_ACCELERATION, swipe.getMaxXAcceleration());
        contentValues.put(COL_AVG_X_ACCELERATION, swipe.getAvgXAcceleration());
        contentValues.put(COL_STD_X_ACCELERATION, swipe.getStdXAcceleration());
        contentValues.put(COL_VAR_X_ACCELERATION, swipe.getVarXAcceleration());
        contentValues.put(COL_MIN_Y_ACCELERATION, swipe.getMinYAcceleration());
        contentValues.put(COL_MAX_Y_ACCELERATION, swipe.getMaxYAcceleration());
        contentValues.put(COL_AVG_Y_ACCELERATION, swipe.getAvgYAcceleration());
        contentValues.put(COL_STD_Y_ACCELERATION, swipe.getStdYAcceleration());
        contentValues.put(COL_VAR_Y_ACCELERATION, swipe.getVarYAcceleration());
        contentValues.put(COL_MIN_PRESSURE, swipe.getMinPressure());
        contentValues.put(COL_MAX_PRESSURE, swipe.getMaxPressure());
        contentValues.put(COL_AVG_PRESSURE, swipe.getAvgPressure());
        contentValues.put(COL_STD_PRESSURE, swipe.getStdPressure());
        contentValues.put(COL_VAR_PRESSURE, swipe.getVarPressure());
        contentValues.put(COL_AUTHENTICATION, authentication);
        contentValues.put(COL_AUTHENTICATION_TIME, authenticationTime);
        contentValues.put(COL_USER_ID, swipe.getUserId());
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTestRecord(Swipe swipe, double authentication, double authenticationTime, int classifierSamples) {
        return this.addTestSwipe(swipe, authentication, authenticationTime, TEST_SWIPES, classifierSamples)  && this.addTestSwipeNormalized(swipe, authentication, authenticationTime, TEST_SWIPES_NORMALIZED, classifierSamples);
    }

    private boolean addSwipeNormalized(Swipe swipe, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        double[] swipeValues = swipe.getNormalizedValues();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipeValues[0]);
        contentValues.put(COL_AVG_SIZE, swipeValues[1]);
        contentValues.put(COL_DOWN_SIZE, swipeValues[2]);
        contentValues.put(COL_DOWN_PRESSURE, swipeValues[3]);
        contentValues.put(COL_START_X, swipeValues[4]);
        contentValues.put(COL_START_Y, swipeValues[5]);
        contentValues.put(COL_END_X, swipeValues[6]);
        contentValues.put(COL_END_Y, swipeValues[7]);
        contentValues.put(COL_MIN_X_VELOCITY, swipeValues[8]);
        contentValues.put(COL_MAX_X_VELOCITY, swipeValues[9]);
        contentValues.put(COL_AVG_X_VELOCITY, swipeValues[10]);
        contentValues.put(COL_STD_X_VELOCITY, swipeValues[11]);
        contentValues.put(COL_VAR_X_VELOCITY, swipeValues[12]);
        contentValues.put(COL_MIN_Y_VELOCITY, swipeValues[13]);
        contentValues.put(COL_MAX_Y_VELOCITY, swipeValues[14]);
        contentValues.put(COL_AVG_Y_VELOCITY, swipeValues[15]);
        contentValues.put(COL_STD_Y_VELOCITY, swipeValues[16]);
        contentValues.put(COL_VAR_Y_VELOCITY, swipeValues[17]);
        contentValues.put(COL_MIN_X_ACCELERATION, swipeValues[18]);
        contentValues.put(COL_MAX_X_ACCELERATION, swipeValues[19]);
        contentValues.put(COL_AVG_X_ACCELERATION, swipeValues[20]);
        contentValues.put(COL_STD_X_ACCELERATION, swipeValues[21]);
        contentValues.put(COL_VAR_X_ACCELERATION, swipeValues[22]);
        contentValues.put(COL_MIN_Y_ACCELERATION, swipeValues[23]);
        contentValues.put(COL_MAX_Y_ACCELERATION, swipeValues[24]);
        contentValues.put(COL_AVG_Y_ACCELERATION, swipeValues[25]);
        contentValues.put(COL_STD_Y_ACCELERATION, swipeValues[26]);
        contentValues.put(COL_VAR_Y_ACCELERATION, swipeValues[27]);
        contentValues.put(COL_MIN_PRESSURE, swipeValues[28]);
        contentValues.put(COL_MAX_PRESSURE, swipeValues[29]);
        contentValues.put(COL_AVG_PRESSURE, swipeValues[30]);
        contentValues.put(COL_STD_PRESSURE, swipeValues[31]);
        contentValues.put(COL_VAR_PRESSURE, swipeValues[32]);
        contentValues.put(COL_USER_ID, swipe.getUserId());

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTrainRecordNormalized(Swipe swipe) {
        return this.addSwipeNormalized(swipe, REAL_SWIPES_NORMALIZED);
    }

    public boolean addGANRecordNormalized(Swipe swipe) {
        return this.addSwipeNormalized(swipe, GAN_SWIPES_NORMALIZED);
    }

    public boolean[] addGANRecordsNormalized(ArrayList<Swipe> swipes) {
        boolean [] result = new boolean[swipes.size()];
        for (int i=0; i < swipes.size(); i++) {
            result[i] = this.addGANRecordNormalized(swipes.get(i));
        }
        return result;
    }

    public boolean addTestSwipeNormalized(Swipe swipe, double authentication, double authenticationTime, String tableName, int classifierSamples) {
        SQLiteDatabase db = this.getWritableDatabase();

        double[] swipeValues = swipe.getNormalizedValues();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipeValues[0]);
        contentValues.put(COL_AVG_SIZE, swipeValues[1]);
        contentValues.put(COL_DOWN_SIZE, swipeValues[2]);
        contentValues.put(COL_DOWN_PRESSURE, swipeValues[3]);
        contentValues.put(COL_START_X, swipeValues[4]);
        contentValues.put(COL_START_Y, swipeValues[5]);
        contentValues.put(COL_END_X, swipeValues[6]);
        contentValues.put(COL_END_Y, swipeValues[7]);
        contentValues.put(COL_MIN_X_VELOCITY, swipeValues[8]);
        contentValues.put(COL_MAX_X_VELOCITY, swipeValues[9]);
        contentValues.put(COL_AVG_X_VELOCITY, swipeValues[10]);
        contentValues.put(COL_STD_X_VELOCITY, swipeValues[11]);
        contentValues.put(COL_VAR_X_VELOCITY, swipeValues[12]);
        contentValues.put(COL_MIN_Y_VELOCITY, swipeValues[13]);
        contentValues.put(COL_MAX_Y_VELOCITY, swipeValues[14]);
        contentValues.put(COL_AVG_Y_VELOCITY, swipeValues[15]);
        contentValues.put(COL_STD_Y_VELOCITY, swipeValues[16]);
        contentValues.put(COL_VAR_Y_VELOCITY, swipeValues[17]);
        contentValues.put(COL_MIN_X_ACCELERATION, swipeValues[18]);
        contentValues.put(COL_MAX_X_ACCELERATION, swipeValues[19]);
        contentValues.put(COL_AVG_X_ACCELERATION, swipeValues[20]);
        contentValues.put(COL_STD_X_ACCELERATION, swipeValues[21]);
        contentValues.put(COL_VAR_X_ACCELERATION, swipeValues[22]);
        contentValues.put(COL_MIN_Y_ACCELERATION, swipeValues[23]);
        contentValues.put(COL_MAX_Y_ACCELERATION, swipeValues[24]);
        contentValues.put(COL_AVG_Y_ACCELERATION, swipeValues[25]);
        contentValues.put(COL_STD_Y_ACCELERATION, swipeValues[26]);
        contentValues.put(COL_VAR_Y_ACCELERATION, swipeValues[27]);
        contentValues.put(COL_MIN_PRESSURE, swipeValues[28]);
        contentValues.put(COL_MAX_PRESSURE, swipeValues[29]);
        contentValues.put(COL_AVG_PRESSURE, swipeValues[30]);
        contentValues.put(COL_STD_PRESSURE, swipeValues[31]);
        contentValues.put(COL_VAR_PRESSURE, swipeValues[32]);
        contentValues.put(COL_AUTHENTICATION, authentication);
        contentValues.put(COL_AUTHENTICATION_TIME, authenticationTime);
        contentValues.put(COL_USER_ID, swipe.getUserId());
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public ArrayList<Swipe> getAllSwipes(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        ArrayList<Swipe> swipes = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Swipe swipe = new Swipe();
            swipe.setDuration(cursor.getDouble(cursor.getColumnIndex(COL_DURATION)));
            swipe.setAvgSize(cursor.getDouble(cursor.getColumnIndex(COL_AVG_SIZE)));
            swipe.setDownSize(cursor.getDouble(cursor.getColumnIndex(COL_DOWN_SIZE)));
            swipe.setDownPressure(cursor.getDouble(cursor.getColumnIndex(COL_DOWN_PRESSURE)));
            swipe.setStartX(cursor.getDouble(cursor.getColumnIndex(COL_START_X)));
            swipe.setStartY(cursor.getDouble(cursor.getColumnIndex(COL_START_Y)));
            swipe.setEndX(cursor.getDouble(cursor.getColumnIndex(COL_END_X)));
            swipe.setEndY(cursor.getDouble(cursor.getColumnIndex(COL_END_Y)));

            swipe.setMinXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_VELOCITY)));
            swipe.setMaxXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_VELOCITY)));
            swipe.setAvgXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_VELOCITY)));
            swipe.setStdXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_VELOCITY)));
            swipe.setVarXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_VELOCITY)));

            swipe.setMinYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_VELOCITY)));
            swipe.setMaxYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_VELOCITY)));
            swipe.setAvgYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_VELOCITY)));
            swipe.setStdYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_VELOCITY)));
            swipe.setVarYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_VELOCITY)));

            swipe.setMinXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ACCELERATION)));
            swipe.setMaxXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ACCELERATION)));
            swipe.setAvgXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ACCELERATION)));
            swipe.setStdXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ACCELERATION)));
            swipe.setVarXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ACCELERATION)));

            swipe.setMinYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ACCELERATION)));
            swipe.setMaxYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ACCELERATION)));
            swipe.setAvgYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ACCELERATION)));
            swipe.setStdYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ACCELERATION)));
            swipe.setVarYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ACCELERATION)));

            swipe.setMinPressure(cursor.getDouble(cursor.getColumnIndex(COL_MIN_PRESSURE)));
            swipe.setMaxPressure(cursor.getDouble(cursor.getColumnIndex(COL_MAX_PRESSURE)));
            swipe.setAvgPressure(cursor.getDouble(cursor.getColumnIndex(COL_AVG_PRESSURE)));
            swipe.setStdPressure(cursor.getDouble(cursor.getColumnIndex(COL_STD_PRESSURE)));
            swipe.setVarPressure(cursor.getDouble(cursor.getColumnIndex(COL_VAR_PRESSURE)));

            swipe.setUserId(cursor.getString(cursor.getColumnIndex(COL_USER_ID)));

            swipes.add(swipe);

            cursor.move(1);
        }
        cursor.close();

        return swipes;
    }

    public ArrayList<Swipe> getTestSwipesFromClassifier(int classifierIndex) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES + " WHERE " + COL_CLASSIFIER_SAMPLES + " == " + (classifierIndex + 1) * 5;
        ArrayList<Swipe> swipes = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Swipe swipe = new Swipe();
            swipe.setDuration(cursor.getDouble(cursor.getColumnIndex(COL_DURATION)));
            swipe.setAvgSize(cursor.getDouble(cursor.getColumnIndex(COL_AVG_SIZE)));
            swipe.setDownSize(cursor.getDouble(cursor.getColumnIndex(COL_DOWN_SIZE)));
            swipe.setDownPressure(cursor.getDouble(cursor.getColumnIndex(COL_DOWN_PRESSURE)));
            swipe.setStartX(cursor.getDouble(cursor.getColumnIndex(COL_START_X)));
            swipe.setStartY(cursor.getDouble(cursor.getColumnIndex(COL_START_Y)));
            swipe.setEndX(cursor.getDouble(cursor.getColumnIndex(COL_END_X)));
            swipe.setEndY(cursor.getDouble(cursor.getColumnIndex(COL_END_Y)));

            swipe.setMinXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_VELOCITY)));
            swipe.setMaxXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_VELOCITY)));
            swipe.setAvgXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_VELOCITY)));
            swipe.setStdXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_VELOCITY)));
            swipe.setVarXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_VELOCITY)));

            swipe.setMinYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_VELOCITY)));
            swipe.setMaxYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_VELOCITY)));
            swipe.setAvgYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_VELOCITY)));
            swipe.setStdYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_VELOCITY)));
            swipe.setVarYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_VELOCITY)));

            swipe.setMinXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ACCELERATION)));
            swipe.setMaxXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ACCELERATION)));
            swipe.setAvgXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ACCELERATION)));
            swipe.setStdXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ACCELERATION)));
            swipe.setVarXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ACCELERATION)));

            swipe.setMinYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ACCELERATION)));
            swipe.setMaxYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ACCELERATION)));
            swipe.setAvgYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ACCELERATION)));
            swipe.setStdYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ACCELERATION)));
            swipe.setVarYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ACCELERATION)));

            swipe.setMinPressure(cursor.getDouble(cursor.getColumnIndex(COL_MIN_PRESSURE)));
            swipe.setMaxPressure(cursor.getDouble(cursor.getColumnIndex(COL_MAX_PRESSURE)));
            swipe.setAvgPressure(cursor.getDouble(cursor.getColumnIndex(COL_AVG_PRESSURE)));
            swipe.setStdPressure(cursor.getDouble(cursor.getColumnIndex(COL_STD_PRESSURE)));
            swipe.setVarPressure(cursor.getDouble(cursor.getColumnIndex(COL_VAR_PRESSURE)));

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

    public ArrayList<double[]> getTestingData(int classifierSamples, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES + " WHERE " + COL_CLASSIFIER_SAMPLES + " == " + classifierSamples + " AND " + COL_USER_ID + " == \'" + userId + "\'";
        ArrayList<double[]> testingData = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            double[] testingValues = new double[2];
            testingValues[0] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION));
            testingValues[1] = cursor.getDouble(cursor.getColumnIndex(COL_AUTHENTICATION_TIME));

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

    public void deleteRealResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REAL_RESULTS);
    }

    public boolean saveRealResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime, int classifierSamples) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);

        long result = db.insert(REAL_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveGANResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime, double ganTime, int classifierSamples) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_GAN_TIME, ganTime);
        contentValues.put(COL_CLASSIFIER_SAMPLES, classifierSamples);

        long result = db.insert(GAN_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveTestResults(double instances, double TAR, double FRR, double TRR, double FAR, double avgSampleTime, double avgTestTime, int classifierSamples) {
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

        long result = db.insert(TEST_RESULTS, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public synchronized void saveAsCSV(String tableName, String filePath, ContentResolver resolver) {

        FileOutputStream fos;
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
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor curCSV = db.rawQuery("SELECT * FROM " + tableName,null);

                String[] colNames = curCSV.getColumnNames();
                csvWrite.writeNext(colNames);
                while(curCSV.moveToNext())
                {
                    int colCounts = curCSV.getColumnCount();
                    String[] arrStr = new String[colCounts];
                    for (int i = 0; i < colCounts; i++) {
                        if (colNames[i].equals("USER_ID")) {
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

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + filePath);

                try
                {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    SQLiteDatabase db = this.getReadableDatabase();
                    Cursor curCSV = db.rawQuery("SELECT * FROM " + tableName,null);
                    csvWrite.writeNext(curCSV.getColumnNames());
                    while(curCSV.moveToNext())
                    {
                        int colCounts = curCSV.getColumnCount();
                        String[] arrStr = new String[colCounts];
                        for (int i = 0; i < colCounts - 1; i++) {
                            arrStr[i] = Double.toString(curCSV.getDouble(i));
                        }
                        arrStr[colCounts - 1] = curCSV.getString(colCounts - 1);
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

    }

    public void resetDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REAL_SWIPES);
        db.execSQL("DELETE FROM " + GAN_SWIPES);
        db.execSQL("DELETE FROM " + TEST_SWIPES);
        db.execSQL("DELETE FROM " + REAL_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + GAN_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + TEST_SWIPES_NORMALIZED);
        db.execSQL("DELETE FROM " + REAL_RESULTS);
        db.execSQL("DELETE FROM " + GAN_RESULTS);
        db.execSQL("DELETE FROM " + TEST_RESULTS);
    }

}
