package com.scratchapp.swipegan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GAN.db";

    private static final String REAL_SWIPES = "REAL_SWIPES";
    private static final String GAN_SWIPES = "GAN_SWIPES";

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
    private static final String COL_VAR_X_VELOCITY = "var_x_velocity";
    private static final String COL_STD_X_VELOCITY = "std_x_velocity";
    private static final String COL_MIN_Y_VELOCITY = "min_y_velocity";
    private static final String COL_MAX_Y_VELOCITY = "max_y_velocity";
    private static final String COL_AVG_Y_VELOCITY = "avg_y_velocity";
    private static final String COL_VAR_Y_VELOCITY = "var_y_velocity";
    private static final String COL_STD_Y_VELOCITY = "std_y_velocity";
    private static final String COL_MIN_PRESSURE = "min_pressure";
    private static final String COL_MAX_PRESSURE = "max_pressure";
    private static final String COL_AVG_PRESSURE = "avg_pressure";
    private static final String COL_VAR_PRESSURE = "var_pressure";
    private static final String COL_STD_PRESSURE = "std_pressure";
    private static final String COL_MIN_X_ACCELERATION = "min_x_acceleration";
    private static final String COL_MAX_X_ACCELERATION = "max_x_acceleration";
    private static final String COL_AVG_X_ACCELERATION = "avg_x_acceleration";
    private static final String COL_VAR_X_ACCELERATION = "var_x_acceleration";
    private static final String COL_STD_X_ACCELERATION = "std_x_acceleration";
    private static final String COL_MIN_Y_ACCELERATION = "min_y_acceleration";
    private static final String COL_MAX_Y_ACCELERATION = "max_y_acceleration";
    private static final String COL_AVG_Y_ACCELERATION = "avg_y_acceleration";
    private static final String COL_VAR_Y_ACCELERATION = "var_y_acceleration";
    private static final String COL_STD_Y_ACCELERATION = "std_y_acceleration";

    private static final String TEST_SWIPES = "TEST_SWIPES";
    private static final String COL_AUTHENTICATION = "AUTHENTICATION";
    private static final String COL_AUTHENTICATION_TIME = "AUTHENTICATION_TIME";

    private static final String REAL_RESULTS = "REAL_RESULTS";
    private static final String GAN_RESULTS = "GAN_RESULTS";
    private static final String TEST_RESULTS = "TEST_RESULTS";

    private static final String COL_INSTANCES = "INSTANCES";
    private static final String COL_TAR = "TAR";
    private static final String COL_FRR = "FRR";
    private static final String COL_AVG_SAMPLE_TIME = "AVG_SAMPLE_TIME";
    private static final String COL_TRAINING_TIME = "TRAINING_TIME";
    private static final String COL_GAN_TIME = "GAN_TIME";
    private static final String COL_AVG_TEST_TIME = "AVG_TEST_TIME";

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
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53))";

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
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53))";

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
                + COL_VAR_X_VELOCITY + " float(53), "
                + COL_STD_X_VELOCITY + " float(53), "
                + COL_MIN_Y_VELOCITY + " float(53), "
                + COL_MAX_Y_VELOCITY + " float(53), "
                + COL_AVG_Y_VELOCITY + " float(53), "
                + COL_VAR_Y_VELOCITY + " float(53), "
                + COL_STD_Y_VELOCITY + " float(53), "
                + COL_MIN_PRESSURE + " float(53), "
                + COL_MAX_PRESSURE + " float(53), "
                + COL_AVG_PRESSURE + " float(53), "
                + COL_VAR_PRESSURE + " float(53), "
                + COL_STD_PRESSURE + " float(53), "
                + COL_MIN_X_ACCELERATION + " float(53), "
                + COL_MAX_X_ACCELERATION + " float(53), "
                + COL_AVG_X_ACCELERATION + " float(53), "
                + COL_VAR_X_ACCELERATION + " float(53), "
                + COL_STD_X_ACCELERATION + " float(53), "
                + COL_MIN_Y_ACCELERATION + " float(53), "
                + COL_MAX_Y_ACCELERATION + " float(53), "
                + COL_AVG_Y_ACCELERATION + " float(53), "
                + COL_VAR_Y_ACCELERATION + " float(53), "
                + COL_STD_Y_ACCELERATION + " float(53), "
                + COL_AUTHENTICATION + " float(53), "
                + COL_AUTHENTICATION_TIME + " float(53))";

        String createRealResultsTable = "CREATE TABLE " + REAL_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53))";

        String createGanResultsTable = "CREATE TABLE " + GAN_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_GAN_TIME + " float(53), "
                + COL_TRAINING_TIME + " float(53))";

        String createTestResultsTable = "CREATE TABLE " + TEST_RESULTS
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INSTANCES + " float(53), "
                + COL_TAR + " float(53), "
                + COL_FRR + " float(53), "
                + COL_AVG_SAMPLE_TIME + " float(53), "
                + COL_AVG_TEST_TIME + " float(53))";

        db.execSQL(createRealSwipesTable);
        db.execSQL(createGanSwipesTable);
        db.execSQL(createTestSwipesTable);

        db.execSQL(createRealResultsTable);
        db.execSQL(createGanResultsTable);
        db.execSQL(createTestResultsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REAL_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + GAN_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_SWIPES);
        db.execSQL("DROP TABLE IF EXISTS " + REAL_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + GAN_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_RESULTS);
        onCreate(db);
    }

    public boolean addTrainRecord(Swipe swipe, String tableName) {
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
        contentValues.put(COL_VAR_X_VELOCITY, swipe.getVarXVelocity());
        contentValues.put(COL_STD_X_VELOCITY, swipe.getStdXVelocity());
        contentValues.put(COL_MIN_Y_VELOCITY, swipe.getMinYVelocity());
        contentValues.put(COL_MAX_Y_VELOCITY, swipe.getMaxYVelocity());
        contentValues.put(COL_AVG_Y_VELOCITY, swipe.getAvgYVelocity());
        contentValues.put(COL_VAR_Y_VELOCITY, swipe.getVarYVelocity());
        contentValues.put(COL_STD_Y_VELOCITY, swipe.getStdYVelocity());
        contentValues.put(COL_MIN_PRESSURE, swipe.getMinPressure());
        contentValues.put(COL_MAX_PRESSURE, swipe.getMaxPressure());
        contentValues.put(COL_AVG_PRESSURE, swipe.getAvgPressure());
        contentValues.put(COL_VAR_PRESSURE, swipe.getVarPressure());
        contentValues.put(COL_STD_PRESSURE, swipe.getStdPressure());
        contentValues.put(COL_MIN_X_ACCELERATION, swipe.getMinXAcceleration());
        contentValues.put(COL_MAX_X_ACCELERATION, swipe.getMaxXAcceleration());
        contentValues.put(COL_AVG_X_ACCELERATION, swipe.getAvgXAcceleration());
        contentValues.put(COL_VAR_X_ACCELERATION, swipe.getVarXAcceleration());
        contentValues.put(COL_STD_X_ACCELERATION, swipe.getStdXAcceleration());
        contentValues.put(COL_MIN_Y_ACCELERATION, swipe.getMinYAcceleration());
        contentValues.put(COL_MAX_Y_ACCELERATION, swipe.getMaxYAcceleration());
        contentValues.put(COL_AVG_Y_ACCELERATION, swipe.getAvgYAcceleration());
        contentValues.put(COL_VAR_Y_ACCELERATION, swipe.getVarYAcceleration());
        contentValues.put(COL_STD_Y_ACCELERATION, swipe.getStdYAcceleration());

        long result = db.insert(tableName, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTestRecord(Swipe swipe, double authentication, double authenticationTime) {
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
        contentValues.put(COL_VAR_X_VELOCITY, swipe.getVarXVelocity());
        contentValues.put(COL_STD_X_VELOCITY, swipe.getStdXVelocity());
        contentValues.put(COL_MIN_Y_VELOCITY, swipe.getMinYVelocity());
        contentValues.put(COL_MAX_Y_VELOCITY, swipe.getMaxYVelocity());
        contentValues.put(COL_AVG_Y_VELOCITY, swipe.getAvgYVelocity());
        contentValues.put(COL_VAR_Y_VELOCITY, swipe.getVarYVelocity());
        contentValues.put(COL_STD_Y_VELOCITY, swipe.getStdYVelocity());
        contentValues.put(COL_MIN_PRESSURE, swipe.getMinPressure());
        contentValues.put(COL_MAX_PRESSURE, swipe.getMaxPressure());
        contentValues.put(COL_AVG_PRESSURE, swipe.getAvgPressure());
        contentValues.put(COL_VAR_PRESSURE, swipe.getVarPressure());
        contentValues.put(COL_STD_PRESSURE, swipe.getStdPressure());
        contentValues.put(COL_MIN_X_ACCELERATION, swipe.getMinXAcceleration());
        contentValues.put(COL_MAX_X_ACCELERATION, swipe.getMaxXAcceleration());
        contentValues.put(COL_AVG_X_ACCELERATION, swipe.getAvgXAcceleration());
        contentValues.put(COL_VAR_X_ACCELERATION, swipe.getVarXAcceleration());
        contentValues.put(COL_STD_X_ACCELERATION, swipe.getStdXAcceleration());
        contentValues.put(COL_MIN_Y_ACCELERATION, swipe.getMinYAcceleration());
        contentValues.put(COL_MAX_Y_ACCELERATION, swipe.getMaxYAcceleration());
        contentValues.put(COL_AVG_Y_ACCELERATION, swipe.getAvgYAcceleration());
        contentValues.put(COL_VAR_Y_ACCELERATION, swipe.getVarYAcceleration());
        contentValues.put(COL_STD_Y_ACCELERATION, swipe.getStdYAcceleration());
        contentValues.put(COL_AUTHENTICATION, authentication);
        contentValues.put(COL_AUTHENTICATION_TIME, authenticationTime);

        long result = db.insert(TEST_SWIPES, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public ArrayList<Swipe> getAllSwipes(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        ArrayList<Swipe> swipes = new ArrayList<Swipe>();
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
            swipe.setVarXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_VELOCITY)));
            swipe.setStdXVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_VELOCITY)));

            swipe.setMinYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_VELOCITY)));
            swipe.setMaxYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_VELOCITY)));
            swipe.setAvgYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_VELOCITY)));
            swipe.setVarYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_VELOCITY)));
            swipe.setStdYVelocity(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_VELOCITY)));

            swipe.setMinPressure(cursor.getDouble(cursor.getColumnIndex(COL_MIN_PRESSURE)));
            swipe.setMaxPressure(cursor.getDouble(cursor.getColumnIndex(COL_MAX_PRESSURE)));
            swipe.setAvgPressure(cursor.getDouble(cursor.getColumnIndex(COL_AVG_PRESSURE)));
            swipe.setVarPressure(cursor.getDouble(cursor.getColumnIndex(COL_VAR_PRESSURE)));
            swipe.setStdPressure(cursor.getDouble(cursor.getColumnIndex(COL_STD_PRESSURE)));

            swipe.setMinXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ACCELERATION)));
            swipe.setMaxXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ACCELERATION)));
            swipe.setAvgXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ACCELERATION)));
            swipe.setVarXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ACCELERATION)));
            swipe.setStdXAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ACCELERATION)));

            swipe.setMinYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ACCELERATION)));
            swipe.setMaxYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ACCELERATION)));
            swipe.setAvgYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ACCELERATION)));
            swipe.setVarYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ACCELERATION)));
            swipe.setStdYAcceleration(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ACCELERATION)));

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

    public ArrayList<double[]> getTestingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES;
        ArrayList<double[]> testingData = new ArrayList<double[]>();
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
        db.execSQL("DELETE FROM " + TEST_RESULTS);
    }

    public void deleteGANData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GAN_SWIPES);
        db.execSQL("DELETE FROM " + GAN_RESULTS);
    }

    public void deleteRealResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + REAL_RESULTS);
    }

    public boolean saveRealResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);

        long result = db.insert(REAL_RESULTS, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveGANResults(double instances, double TAR, double FRR, double avgSampleTime, double trainingTime, double ganTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_TRAINING_TIME, trainingTime);
        contentValues.put(COL_GAN_TIME, ganTime);

        long result = db.insert(GAN_RESULTS, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean saveTestResults(double instances, double TAR, double FRR, double avgSampleTime, double avgTestTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_INSTANCES, instances);
        contentValues.put(COL_TAR, TAR);
        contentValues.put(COL_FRR, FRR);
        contentValues.put(COL_AVG_SAMPLE_TIME, avgSampleTime);
        contentValues.put(COL_AVG_TEST_TIME, avgTestTime);

        long result = db.insert(TEST_RESULTS, null, contentValues);
        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public synchronized void saveAsCSV(String tableName, String filePath) {

        File file = new File(filePath);
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
                for (int i = 0; i < colCounts; i++) {
                    arrStr[i] = Double.toString(curCSV.getDouble(i));
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

    public void resetDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        String realSwipesQuery = "DELETE FROM " + REAL_SWIPES;
        String ganSwipesQuery = "DELETE FROM " + GAN_SWIPES;
        String testSwipesQuery = "DELETE FROM " + TEST_SWIPES;
        String realResultsQuery = "DELETE FROM " + REAL_RESULTS;
        String ganResultsQuery = "DELETE FROM " + GAN_RESULTS;
        String testResultsQuery = "DELETE FROM " + TEST_RESULTS;
        db.execSQL(realSwipesQuery);
        db.execSQL(ganSwipesQuery);
        db.execSQL(testSwipesQuery);
        db.execSQL(realResultsQuery);
        db.execSQL(ganResultsQuery);
        db.execSQL(testResultsQuery);
    }

}
