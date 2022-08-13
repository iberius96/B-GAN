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
    private static final String TEST_SWIPES = "TEST_SWIPES";
    private static final String TEST_SWIPES_NORMALIZED = "TEST_SWIPES_NORMALIZED";

    private static final String REAL_RESULTS = "REAL_RESULTS";
    private static final String GAN_RESULTS = "GAN_RESULTS";
    private static final String TEST_RESULTS = "TEST_RESULTS";

    private static final String USER_DATA = "USER_DATA";

    private static final String FEATURE_DATA = "FEATURE_DATA";

    private static final String COL_AUTHENTICATION = "AUTHENTICATION";
    private static final String COL_AUTHENTICATION_TIME = "AUTHENTICATION_TIME";

    private static final String COL_ID = "id";
    private static final String COL_DURATION = "duration";
    private static final String COL_AVG_SIZE = "avg_size";
    private static final String COL_DOWN_SIZE = "down_size";
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
    private static final String COL_MIN_X_ACCELEROMETER = "min_x_accelerometer";
    private static final String COL_MAX_X_ACCELEROMETER = "max_x_accelerometer";
    private static final String COL_AVG_X_ACCELEROMETER = "avg_x_accelerometer";
    private static final String COL_STD_X_ACCELEROMETER = "std_x_accelerometer";
    private static final String COL_VAR_X_ACCELEROMETER = "var_x_accelerometer";
    private static final String COL_MIN_Y_ACCELEROMETER = "min_y_accelerometer";
    private static final String COL_MAX_Y_ACCELEROMETER = "max_y_accelerometer";
    private static final String COL_AVG_Y_ACCELEROMETER = "avg_y_accelerometer";
    private static final String COL_STD_Y_ACCELEROMETER = "std_y_accelerometer";
    private static final String COL_VAR_Y_ACCELEROMETER = "var_y_accelerometer";
    private static final String COL_MIN_Z_ACCELEROMETER = "min_z_accelerometer";
    private static final String COL_MAX_Z_ACCELEROMETER = "max_z_accelerometer";
    private static final String COL_AVG_Z_ACCELEROMETER = "avg_z_accelerometer";
    private static final String COL_STD_Z_ACCELEROMETER = "std_z_accelerometer";
    private static final String COL_VAR_Z_ACCELEROMETER = "var_z_accelerometer";
    private static final String COL_MIN_X_GYROSCOPE = "min_x_gyroscope";
    private static final String COL_MAX_X_GYROSCOPE = "max_x_gyroscope";
    private static final String COL_AVG_X_GYROSCOPE = "avg_x_gyroscope";
    private static final String COL_STD_X_GYROSCOPE = "std_x_gyroscope";
    private static final String COL_VAR_X_GYROSCOPE = "var_x_gyroscope";
    private static final String COL_MIN_Y_GYROSCOPE = "min_y_gyroscope";
    private static final String COL_MAX_Y_GYROSCOPE = "max_y_gyroscope";
    private static final String COL_AVG_Y_GYROSCOPE = "avg_y_gyroscope";
    private static final String COL_STD_Y_GYROSCOPE = "std_y_gyroscope";
    private static final String COL_VAR_Y_GYROSCOPE = "var_y_gyroscope";
    private static final String COL_MIN_Z_GYROSCOPE = "min_z_gyroscope";
    private static final String COL_MAX_Z_GYROSCOPE = "max_z_gyroscope";
    private static final String COL_AVG_Z_GYROSCOPE = "avg_z_gyroscope";
    private static final String COL_STD_Z_GYROSCOPE = "std_z_gyroscope";
    private static final String COL_VAR_Z_GYROSCOPE = "var_z_gyroscope";
    private static final String COL_MIN_X_ORIENTATION = "min_x_orientation";
    private static final String COL_MAX_X_ORIENTATION = "max_x_orientation";
    private static final String COL_AVG_X_ORIENTATION = "avg_x_orientation";
    private static final String COL_STD_X_ORIENTATION = "std_x_orientation";
    private static final String COL_VAR_X_ORIENTATION = "var_x_orientation";
    private static final String COL_MIN_Y_ORIENTATION = "min_y_orientation";
    private static final String COL_MAX_Y_ORIENTATION = "max_y_orientation";
    private static final String COL_AVG_Y_ORIENTATION = "avg_y_orientation";
    private static final String COL_STD_Y_ORIENTATION = "std_y_orientation";
    private static final String COL_VAR_Y_ORIENTATION = "var_y_orientation";
    private static final String COL_MIN_Z_ORIENTATION = "min_z_orientation";
    private static final String COL_MAX_Z_ORIENTATION = "max_z_orientation";
    private static final String COL_AVG_Z_ORIENTATION = "avg_z_orientation";
    private static final String COL_STD_Z_ORIENTATION = "std_z_orientation";
    private static final String COL_VAR_Z_ORIENTATION = "var_z_orientation";
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

    private static final String COL_NICKNAME = "nickname";
    private static final String COL_GENDER = "gender";
    private static final String COL_AGE = "age";
    private static final String COL_NATIONALITY = "nationality";
    private static final String COL_HOLDING_HAND = "holding_hand";

    private static final String COL_ACCELERATION = "acceleration";
    private static final String COL_ANGULAR_VELOCITY = "angular_velocity";
    private static final String COL_ORIENTATION = "orientation";
    private static final String COL_SWIPE_DURATION = "swipe_duration";
    private static final String COL_SWIPE_START_END_POS = "swipe_start_end_pos";
    private static final String COL_SWIPE_VELOCITY = "swipe_velocity";

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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createGanSwipesTable = "CREATE TABLE " + GAN_SWIPES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createTestSwipesTable = "CREATE TABLE " + TEST_SWIPES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
                + COL_AUTHENTICATION + " float(53), "
                + COL_AUTHENTICATION_TIME + " float(53), "
                + COL_USER_ID + " varchar(20), "
                + COL_CLASSIFIER_SAMPLES + " float(53))";

        String createRealSwipesNormalizedTable = "CREATE TABLE " + REAL_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createGanSwipesNormalizedTable = "CREATE TABLE " + GAN_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
                + COL_USER_ID + " varchar(20))";

        String createTestSwipesNormalizedTable = "CREATE TABLE " + TEST_SWIPES_NORMALIZED
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DURATION + " float(53), "
                + COL_AVG_SIZE + " float(53), "
                + COL_DOWN_SIZE + " float(53), "
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
                + COL_MIN_X_ACCELEROMETER + " float(53), "
                + COL_MAX_X_ACCELEROMETER + " float(53), "
                + COL_AVG_X_ACCELEROMETER + " float(53), "
                + COL_STD_X_ACCELEROMETER + " float(53), "
                + COL_VAR_X_ACCELEROMETER + " float(53), "
                + COL_MIN_Y_ACCELEROMETER + " float(53), "
                + COL_MAX_Y_ACCELEROMETER + " float(53), "
                + COL_AVG_Y_ACCELEROMETER + " float(53), "
                + COL_STD_Y_ACCELEROMETER + " float(53), "
                + COL_VAR_Y_ACCELEROMETER + " float(53), "
                + COL_MIN_Z_ACCELEROMETER + " float(53), "
                + COL_MAX_Z_ACCELEROMETER + " float(53), "
                + COL_AVG_Z_ACCELEROMETER + " float(53), "
                + COL_STD_Z_ACCELEROMETER + " float(53), "
                + COL_VAR_Z_ACCELEROMETER + " float(53), "
                + COL_MIN_X_GYROSCOPE + " float(53), "
                + COL_MAX_X_GYROSCOPE + " float(53), "
                + COL_AVG_X_GYROSCOPE + " float(53), "
                + COL_STD_X_GYROSCOPE + " float(53), "
                + COL_VAR_X_GYROSCOPE + " float(53), "
                + COL_MIN_Y_GYROSCOPE + " float(53), "
                + COL_MAX_Y_GYROSCOPE + " float(53), "
                + COL_AVG_Y_GYROSCOPE + " float(53), "
                + COL_STD_Y_GYROSCOPE + " float(53), "
                + COL_VAR_Y_GYROSCOPE + " float(53), "
                + COL_MIN_Z_GYROSCOPE + " float(53), "
                + COL_MAX_Z_GYROSCOPE + " float(53), "
                + COL_AVG_Z_GYROSCOPE + " float(53), "
                + COL_STD_Z_GYROSCOPE + " float(53), "
                + COL_VAR_Z_GYROSCOPE + " float(53), "
                + COL_MIN_X_ORIENTATION + " float(53), "
                + COL_MAX_X_ORIENTATION + " float(53), "
                + COL_AVG_X_ORIENTATION + " float(53), "
                + COL_STD_X_ORIENTATION + " float(53), "
                + COL_VAR_X_ORIENTATION + " float(53), "
                + COL_MIN_Y_ORIENTATION + " float(53), "
                + COL_MAX_Y_ORIENTATION + " float(53), "
                + COL_AVG_Y_ORIENTATION + " float(53), "
                + COL_STD_Y_ORIENTATION + " float(53), "
                + COL_VAR_Y_ORIENTATION + " float(53), "
                + COL_MIN_Z_ORIENTATION + " float(53), "
                + COL_MAX_Z_ORIENTATION + " float(53), "
                + COL_AVG_Z_ORIENTATION + " float(53), "
                + COL_STD_Z_ORIENTATION + " float(53), "
                + COL_VAR_Z_ORIENTATION + " float(53), "
                + COL_HOLDING_POSITION + " float(53), "
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
                + COL_SWIPE_START_END_POS + " integer(1), "
                + COL_SWIPE_VELOCITY + " integer(1))";

        db.execSQL(createRealSwipesTable);
        db.execSQL(createGanSwipesTable);
        db.execSQL(createTestSwipesTable);

        db.execSQL(createRealSwipesNormalizedTable);
        db.execSQL(createGanSwipesNormalizedTable);
        db.execSQL(createTestSwipesNormalizedTable);

        db.execSQL(createRealResultsTable);
        db.execSQL(createGanResultsTable);
        db.execSQL(createTestResultsTable);

        db.execSQL(createUserDataTable);

        db.execSQL(createFeatureDataTable);

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

            contentValues.put(COL_ACCELERATION, 1);
            contentValues.put(COL_ANGULAR_VELOCITY, 1);
            contentValues.put(COL_ORIENTATION, 1);
            contentValues.put(COL_SWIPE_DURATION, 1);
            contentValues.put(COL_SWIPE_START_END_POS, 1);
            contentValues.put(COL_SWIPE_VELOCITY, 1);

            db.insert(FEATURE_DATA, null, contentValues);
        }

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
        db.execSQL("DROP TABLE IF EXISTS " + USER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + FEATURE_DATA);
        onCreate(db);
    }

    private boolean addSwipe(Swipe swipe, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipe.getDuration());
        contentValues.put(COL_AVG_SIZE, swipe.getAvgSize());
        contentValues.put(COL_DOWN_SIZE, swipe.getDownSize());
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
        contentValues.put(COL_MIN_X_ACCELEROMETER, swipe.getMinXAccelerometer());
        contentValues.put(COL_MAX_X_ACCELEROMETER, swipe.getMaxXAccelerometer());
        contentValues.put(COL_AVG_X_ACCELEROMETER, swipe.getAvgXAccelerometer());
        contentValues.put(COL_STD_X_ACCELEROMETER, swipe.getStdXAccelerometer());
        contentValues.put(COL_VAR_X_ACCELEROMETER, swipe.getVarXAccelerometer());
        contentValues.put(COL_MIN_Y_ACCELEROMETER, swipe.getMinYAccelerometer());
        contentValues.put(COL_MAX_Y_ACCELEROMETER, swipe.getMaxYAccelerometer());
        contentValues.put(COL_AVG_Y_ACCELEROMETER, swipe.getAvgYAccelerometer());
        contentValues.put(COL_STD_Y_ACCELEROMETER, swipe.getStdYAccelerometer());
        contentValues.put(COL_VAR_Y_ACCELEROMETER, swipe.getVarYAccelerometer());
        contentValues.put(COL_MIN_Z_ACCELEROMETER, swipe.getMinZAccelerometer());
        contentValues.put(COL_MAX_Z_ACCELEROMETER, swipe.getMaxZAccelerometer());
        contentValues.put(COL_AVG_Z_ACCELEROMETER, swipe.getAvgZAccelerometer());
        contentValues.put(COL_STD_Z_ACCELEROMETER, swipe.getStdZAccelerometer());
        contentValues.put(COL_VAR_Z_ACCELEROMETER, swipe.getVarZAccelerometer());
        contentValues.put(COL_MIN_X_GYROSCOPE, swipe.getMinXGyroscope());
        contentValues.put(COL_MAX_X_GYROSCOPE, swipe.getMaxXGyroscope());
        contentValues.put(COL_AVG_X_GYROSCOPE, swipe.getAvgXGyroscope());
        contentValues.put(COL_STD_X_GYROSCOPE, swipe.getStdXGyroscope());
        contentValues.put(COL_VAR_X_GYROSCOPE, swipe.getVarXGyroscope());
        contentValues.put(COL_MIN_Y_GYROSCOPE, swipe.getMinYGyroscope());
        contentValues.put(COL_MAX_Y_GYROSCOPE, swipe.getMaxYGyroscope());
        contentValues.put(COL_AVG_Y_GYROSCOPE, swipe.getAvgYGyroscope());
        contentValues.put(COL_STD_Y_GYROSCOPE, swipe.getStdYGyroscope());
        contentValues.put(COL_VAR_Y_GYROSCOPE, swipe.getVarYGyroscope());
        contentValues.put(COL_MIN_Z_GYROSCOPE, swipe.getMinZGyroscope());
        contentValues.put(COL_MAX_Z_GYROSCOPE, swipe.getMaxZGyroscope());
        contentValues.put(COL_AVG_Z_GYROSCOPE, swipe.getAvgZGyroscope());
        contentValues.put(COL_STD_Z_GYROSCOPE, swipe.getStdZGyroscope());
        contentValues.put(COL_VAR_Z_GYROSCOPE, swipe.getVarZGyroscope());
        contentValues.put(COL_MIN_X_ORIENTATION, swipe.getMinXOrientation());
        contentValues.put(COL_MAX_X_ORIENTATION, swipe.getMaxXOrientation());
        contentValues.put(COL_AVG_X_ORIENTATION, swipe.getAvgXOrientation());
        contentValues.put(COL_STD_X_ORIENTATION, swipe.getStdXOrientation());
        contentValues.put(COL_VAR_X_ORIENTATION, swipe.getVarXOrientation());
        contentValues.put(COL_MIN_Y_ORIENTATION, swipe.getMinYOrientation());
        contentValues.put(COL_MAX_Y_ORIENTATION, swipe.getMaxYOrientation());
        contentValues.put(COL_AVG_Y_ORIENTATION, swipe.getAvgYOrientation());
        contentValues.put(COL_STD_Y_ORIENTATION, swipe.getStdYOrientation());
        contentValues.put(COL_VAR_Y_ORIENTATION, swipe.getVarYOrientation());
        contentValues.put(COL_MIN_Z_ORIENTATION, swipe.getMinZOrientation());
        contentValues.put(COL_MAX_Z_ORIENTATION, swipe.getMaxZOrientation());
        contentValues.put(COL_AVG_Z_ORIENTATION, swipe.getAvgZOrientation());
        contentValues.put(COL_STD_Z_ORIENTATION, swipe.getStdZOrientation());
        contentValues.put(COL_VAR_Z_ORIENTATION, swipe.getVarZOrientation());
        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
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
        contentValues.put(COL_MIN_X_ACCELEROMETER, swipe.getMinXAccelerometer());
        contentValues.put(COL_MAX_X_ACCELEROMETER, swipe.getMaxXAccelerometer());
        contentValues.put(COL_AVG_X_ACCELEROMETER, swipe.getAvgXAccelerometer());
        contentValues.put(COL_STD_X_ACCELEROMETER, swipe.getStdXAccelerometer());
        contentValues.put(COL_VAR_X_ACCELEROMETER, swipe.getVarXAccelerometer());
        contentValues.put(COL_MIN_Y_ACCELEROMETER, swipe.getMinYAccelerometer());
        contentValues.put(COL_MAX_Y_ACCELEROMETER, swipe.getMaxYAccelerometer());
        contentValues.put(COL_AVG_Y_ACCELEROMETER, swipe.getAvgYAccelerometer());
        contentValues.put(COL_STD_Y_ACCELEROMETER, swipe.getStdYAccelerometer());
        contentValues.put(COL_VAR_Y_ACCELEROMETER, swipe.getVarYAccelerometer());
        contentValues.put(COL_MIN_Z_ACCELEROMETER, swipe.getMinZAccelerometer());
        contentValues.put(COL_MAX_Z_ACCELEROMETER, swipe.getMaxZAccelerometer());
        contentValues.put(COL_AVG_Z_ACCELEROMETER, swipe.getAvgZAccelerometer());
        contentValues.put(COL_STD_Z_ACCELEROMETER, swipe.getStdZAccelerometer());
        contentValues.put(COL_VAR_Z_ACCELEROMETER, swipe.getVarZAccelerometer());
        contentValues.put(COL_MIN_X_GYROSCOPE, swipe.getMinXGyroscope());
        contentValues.put(COL_MAX_X_GYROSCOPE, swipe.getMaxXGyroscope());
        contentValues.put(COL_AVG_X_GYROSCOPE, swipe.getAvgXGyroscope());
        contentValues.put(COL_STD_X_GYROSCOPE, swipe.getStdXGyroscope());
        contentValues.put(COL_VAR_X_GYROSCOPE, swipe.getVarXGyroscope());
        contentValues.put(COL_MIN_Y_GYROSCOPE, swipe.getMinYGyroscope());
        contentValues.put(COL_MAX_Y_GYROSCOPE, swipe.getMaxYGyroscope());
        contentValues.put(COL_AVG_Y_GYROSCOPE, swipe.getAvgYGyroscope());
        contentValues.put(COL_STD_Y_GYROSCOPE, swipe.getStdYGyroscope());
        contentValues.put(COL_VAR_Y_GYROSCOPE, swipe.getVarYGyroscope());
        contentValues.put(COL_MIN_Z_GYROSCOPE, swipe.getMinZGyroscope());
        contentValues.put(COL_MAX_Z_GYROSCOPE, swipe.getMaxZGyroscope());
        contentValues.put(COL_AVG_Z_GYROSCOPE, swipe.getAvgZGyroscope());
        contentValues.put(COL_STD_Z_GYROSCOPE, swipe.getStdZGyroscope());
        contentValues.put(COL_VAR_Z_GYROSCOPE, swipe.getVarZGyroscope());
        contentValues.put(COL_MIN_X_ORIENTATION, swipe.getMinXOrientation());
        contentValues.put(COL_MAX_X_ORIENTATION, swipe.getMaxXOrientation());
        contentValues.put(COL_AVG_X_ORIENTATION, swipe.getAvgXOrientation());
        contentValues.put(COL_STD_X_ORIENTATION, swipe.getStdXOrientation());
        contentValues.put(COL_VAR_X_ORIENTATION, swipe.getVarXOrientation());
        contentValues.put(COL_MIN_Y_ORIENTATION, swipe.getMinYOrientation());
        contentValues.put(COL_MAX_Y_ORIENTATION, swipe.getMaxYOrientation());
        contentValues.put(COL_AVG_Y_ORIENTATION, swipe.getAvgYOrientation());
        contentValues.put(COL_STD_Y_ORIENTATION, swipe.getStdYOrientation());
        contentValues.put(COL_VAR_Y_ORIENTATION, swipe.getVarYOrientation());
        contentValues.put(COL_MIN_Z_ORIENTATION, swipe.getMinZOrientation());
        contentValues.put(COL_MAX_Z_ORIENTATION, swipe.getMaxZOrientation());
        contentValues.put(COL_AVG_Z_ORIENTATION, swipe.getAvgZOrientation());
        contentValues.put(COL_STD_Z_ORIENTATION, swipe.getStdZOrientation());
        contentValues.put(COL_VAR_Z_ORIENTATION, swipe.getVarZOrientation());
        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
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
        contentValues.put(COL_START_X, swipeValues[3]);
        contentValues.put(COL_START_Y, swipeValues[4]);
        contentValues.put(COL_END_X, swipeValues[5]);
        contentValues.put(COL_END_Y, swipeValues[6]);
        contentValues.put(COL_MIN_X_VELOCITY, swipeValues[7]);
        contentValues.put(COL_MAX_X_VELOCITY, swipeValues[8]);
        contentValues.put(COL_AVG_X_VELOCITY, swipeValues[9]);
        contentValues.put(COL_STD_X_VELOCITY, swipeValues[10]);
        contentValues.put(COL_VAR_X_VELOCITY, swipeValues[11]);
        contentValues.put(COL_MIN_Y_VELOCITY, swipeValues[12]);
        contentValues.put(COL_MAX_Y_VELOCITY, swipeValues[13]);
        contentValues.put(COL_AVG_Y_VELOCITY, swipeValues[14]);
        contentValues.put(COL_STD_Y_VELOCITY, swipeValues[15]);
        contentValues.put(COL_VAR_Y_VELOCITY, swipeValues[16]);
        contentValues.put(COL_MIN_X_ACCELEROMETER, swipeValues[17]);
        contentValues.put(COL_MAX_X_ACCELEROMETER, swipeValues[18]);
        contentValues.put(COL_AVG_X_ACCELEROMETER, swipeValues[19]);
        contentValues.put(COL_STD_X_ACCELEROMETER, swipeValues[20]);
        contentValues.put(COL_VAR_X_ACCELEROMETER, swipeValues[21]);
        contentValues.put(COL_MIN_Y_ACCELEROMETER, swipeValues[22]);
        contentValues.put(COL_MAX_Y_ACCELEROMETER, swipeValues[23]);
        contentValues.put(COL_AVG_Y_ACCELEROMETER, swipeValues[24]);
        contentValues.put(COL_STD_Y_ACCELEROMETER, swipeValues[25]);
        contentValues.put(COL_VAR_Y_ACCELEROMETER, swipeValues[26]);
        contentValues.put(COL_MIN_Z_ACCELEROMETER, swipeValues[27]);
        contentValues.put(COL_MAX_Z_ACCELEROMETER, swipeValues[28]);
        contentValues.put(COL_AVG_Z_ACCELEROMETER, swipeValues[29]);
        contentValues.put(COL_STD_Z_ACCELEROMETER, swipeValues[30]);
        contentValues.put(COL_VAR_Z_ACCELEROMETER, swipeValues[31]);
        contentValues.put(COL_MIN_X_GYROSCOPE, swipeValues[32]);
        contentValues.put(COL_MAX_X_GYROSCOPE, swipeValues[33]);
        contentValues.put(COL_AVG_X_GYROSCOPE, swipeValues[34]);
        contentValues.put(COL_STD_X_GYROSCOPE, swipeValues[35]);
        contentValues.put(COL_VAR_X_GYROSCOPE, swipeValues[36]);
        contentValues.put(COL_MIN_Y_GYROSCOPE, swipeValues[37]);
        contentValues.put(COL_MAX_Y_GYROSCOPE, swipeValues[38]);
        contentValues.put(COL_AVG_Y_GYROSCOPE, swipeValues[39]);
        contentValues.put(COL_STD_Y_GYROSCOPE, swipeValues[40]);
        contentValues.put(COL_VAR_Y_GYROSCOPE, swipeValues[41]);
        contentValues.put(COL_MIN_Z_GYROSCOPE, swipeValues[42]);
        contentValues.put(COL_MAX_Z_GYROSCOPE, swipeValues[43]);
        contentValues.put(COL_AVG_Z_GYROSCOPE, swipeValues[44]);
        contentValues.put(COL_STD_Z_GYROSCOPE, swipeValues[45]);
        contentValues.put(COL_VAR_Z_GYROSCOPE, swipeValues[46]);
        contentValues.put(COL_MIN_X_ORIENTATION, swipeValues[47]);
        contentValues.put(COL_MAX_X_ORIENTATION, swipeValues[48]);
        contentValues.put(COL_AVG_X_ORIENTATION, swipeValues[49]);
        contentValues.put(COL_STD_X_ORIENTATION, swipeValues[50]);
        contentValues.put(COL_VAR_X_ORIENTATION, swipeValues[51]);
        contentValues.put(COL_MIN_Y_ORIENTATION, swipeValues[52]);
        contentValues.put(COL_MAX_Y_ORIENTATION, swipeValues[53]);
        contentValues.put(COL_AVG_Y_ORIENTATION, swipeValues[54]);
        contentValues.put(COL_STD_Y_ORIENTATION, swipeValues[55]);
        contentValues.put(COL_VAR_Y_ORIENTATION, swipeValues[56]);
        contentValues.put(COL_MIN_Z_ORIENTATION, swipeValues[57]);
        contentValues.put(COL_MAX_Z_ORIENTATION, swipeValues[58]);
        contentValues.put(COL_AVG_Z_ORIENTATION, swipeValues[59]);
        contentValues.put(COL_STD_Z_ORIENTATION, swipeValues[60]);
        contentValues.put(COL_VAR_Z_ORIENTATION, swipeValues[61]);
        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
        contentValues.put(COL_USER_ID, swipe.getUserId());

        long result = db.insert(tableName, null, contentValues);
        //if inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean addTestSwipeNormalized(Swipe swipe, double authentication, double authenticationTime, String tableName, int classifierSamples) {
        SQLiteDatabase db = this.getWritableDatabase();

        double[] swipeValues = swipe.getNormalizedValues();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_DURATION, swipeValues[0]);
        contentValues.put(COL_AVG_SIZE, swipeValues[1]);
        contentValues.put(COL_DOWN_SIZE, swipeValues[2]);
        contentValues.put(COL_START_X, swipeValues[3]);
        contentValues.put(COL_START_Y, swipeValues[4]);
        contentValues.put(COL_END_X, swipeValues[5]);
        contentValues.put(COL_END_Y, swipeValues[6]);
        contentValues.put(COL_MIN_X_VELOCITY, swipeValues[7]);
        contentValues.put(COL_MAX_X_VELOCITY, swipeValues[8]);
        contentValues.put(COL_AVG_X_VELOCITY, swipeValues[9]);
        contentValues.put(COL_STD_X_VELOCITY, swipeValues[10]);
        contentValues.put(COL_VAR_X_VELOCITY, swipeValues[11]);
        contentValues.put(COL_MIN_Y_VELOCITY, swipeValues[12]);
        contentValues.put(COL_MAX_Y_VELOCITY, swipeValues[13]);
        contentValues.put(COL_AVG_Y_VELOCITY, swipeValues[14]);
        contentValues.put(COL_STD_Y_VELOCITY, swipeValues[15]);
        contentValues.put(COL_VAR_Y_VELOCITY, swipeValues[16]);
        contentValues.put(COL_MIN_X_ACCELEROMETER, swipeValues[17]);
        contentValues.put(COL_MAX_X_ACCELEROMETER, swipeValues[18]);
        contentValues.put(COL_AVG_X_ACCELEROMETER, swipeValues[19]);
        contentValues.put(COL_STD_X_ACCELEROMETER, swipeValues[20]);
        contentValues.put(COL_VAR_X_ACCELEROMETER, swipeValues[21]);
        contentValues.put(COL_MIN_Y_ACCELEROMETER, swipeValues[22]);
        contentValues.put(COL_MAX_Y_ACCELEROMETER, swipeValues[23]);
        contentValues.put(COL_AVG_Y_ACCELEROMETER, swipeValues[24]);
        contentValues.put(COL_STD_Y_ACCELEROMETER, swipeValues[25]);
        contentValues.put(COL_VAR_Y_ACCELEROMETER, swipeValues[26]);
        contentValues.put(COL_MIN_Z_ACCELEROMETER, swipeValues[27]);
        contentValues.put(COL_MAX_Z_ACCELEROMETER, swipeValues[28]);
        contentValues.put(COL_AVG_Z_ACCELEROMETER, swipeValues[29]);
        contentValues.put(COL_STD_Z_ACCELEROMETER, swipeValues[30]);
        contentValues.put(COL_VAR_Z_ACCELEROMETER, swipeValues[31]);
        contentValues.put(COL_MIN_X_GYROSCOPE, swipeValues[32]);
        contentValues.put(COL_MAX_X_GYROSCOPE, swipeValues[33]);
        contentValues.put(COL_AVG_X_GYROSCOPE, swipeValues[34]);
        contentValues.put(COL_STD_X_GYROSCOPE, swipeValues[35]);
        contentValues.put(COL_VAR_X_GYROSCOPE, swipeValues[36]);
        contentValues.put(COL_MIN_Y_GYROSCOPE, swipeValues[37]);
        contentValues.put(COL_MAX_Y_GYROSCOPE, swipeValues[38]);
        contentValues.put(COL_AVG_Y_GYROSCOPE, swipeValues[39]);
        contentValues.put(COL_STD_Y_GYROSCOPE, swipeValues[40]);
        contentValues.put(COL_VAR_Y_GYROSCOPE, swipeValues[41]);
        contentValues.put(COL_MIN_Z_GYROSCOPE, swipeValues[42]);
        contentValues.put(COL_MAX_Z_GYROSCOPE, swipeValues[43]);
        contentValues.put(COL_AVG_Z_GYROSCOPE, swipeValues[44]);
        contentValues.put(COL_STD_Z_GYROSCOPE, swipeValues[45]);
        contentValues.put(COL_VAR_Z_GYROSCOPE, swipeValues[46]);
        contentValues.put(COL_MIN_X_ORIENTATION, swipeValues[47]);
        contentValues.put(COL_MAX_X_ORIENTATION, swipeValues[48]);
        contentValues.put(COL_AVG_X_ORIENTATION, swipeValues[49]);
        contentValues.put(COL_STD_X_ORIENTATION, swipeValues[50]);
        contentValues.put(COL_VAR_X_ORIENTATION, swipeValues[51]);
        contentValues.put(COL_MIN_Y_ORIENTATION, swipeValues[52]);
        contentValues.put(COL_MAX_Y_ORIENTATION, swipeValues[53]);
        contentValues.put(COL_AVG_Y_ORIENTATION, swipeValues[54]);
        contentValues.put(COL_STD_Y_ORIENTATION, swipeValues[55]);
        contentValues.put(COL_VAR_Y_ORIENTATION, swipeValues[56]);
        contentValues.put(COL_MIN_Z_ORIENTATION, swipeValues[57]);
        contentValues.put(COL_MAX_Z_ORIENTATION, swipeValues[58]);
        contentValues.put(COL_AVG_Z_ORIENTATION, swipeValues[59]);
        contentValues.put(COL_STD_Z_ORIENTATION, swipeValues[60]);
        contentValues.put(COL_VAR_Z_ORIENTATION, swipeValues[61]);
        contentValues.put(COL_HOLDING_POSITION, swipe.getHoldingPosition());
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

            swipe.setMinXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ACCELEROMETER)));
            swipe.setMaxXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ACCELEROMETER)));
            swipe.setAvgXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ACCELEROMETER)));
            swipe.setStdXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ACCELEROMETER)));
            swipe.setVarXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ACCELEROMETER)));

            swipe.setMinYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ACCELEROMETER)));
            swipe.setMaxYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ACCELEROMETER)));
            swipe.setAvgYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ACCELEROMETER)));
            swipe.setStdYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ACCELEROMETER)));
            swipe.setVarYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ACCELEROMETER)));

            swipe.setMinZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_ACCELEROMETER)));
            swipe.setMaxZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_ACCELEROMETER)));
            swipe.setAvgZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_ACCELEROMETER)));
            swipe.setStdZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_ACCELEROMETER)));
            swipe.setVarZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_ACCELEROMETER)));

            swipe.setMinXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_GYROSCOPE)));
            swipe.setMaxXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_GYROSCOPE)));
            swipe.setAvgXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_GYROSCOPE)));
            swipe.setStdXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_GYROSCOPE)));
            swipe.setVarXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_GYROSCOPE)));

            swipe.setMinYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_GYROSCOPE)));
            swipe.setMaxYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_GYROSCOPE)));
            swipe.setAvgYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_GYROSCOPE)));
            swipe.setStdYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_GYROSCOPE)));
            swipe.setVarYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_GYROSCOPE)));

            swipe.setMinZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_GYROSCOPE)));
            swipe.setMaxZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_GYROSCOPE)));
            swipe.setAvgZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_GYROSCOPE)));
            swipe.setStdZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_GYROSCOPE)));
            swipe.setVarZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_GYROSCOPE)));

            swipe.setMinXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ORIENTATION)));
            swipe.setMaxXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ORIENTATION)));
            swipe.setAvgXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ORIENTATION)));
            swipe.setStdXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ORIENTATION)));
            swipe.setVarXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ORIENTATION)));

            swipe.setMinYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ORIENTATION)));
            swipe.setMaxYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ORIENTATION)));
            swipe.setAvgYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ORIENTATION)));
            swipe.setStdYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ORIENTATION)));
            swipe.setVarYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ORIENTATION)));

            swipe.setMinZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_ORIENTATION)));
            swipe.setMaxZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_ORIENTATION)));
            swipe.setAvgZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_ORIENTATION)));
            swipe.setStdZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_ORIENTATION)));
            swipe.setVarZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_ORIENTATION)));

            swipe.setHoldingPosition(cursor.getDouble(cursor.getColumnIndex(COL_HOLDING_POSITION)));

            swipe.setUserId(cursor.getString(cursor.getColumnIndex(COL_USER_ID)));

            swipes.add(swipe);

            cursor.move(1);
        }
        cursor.close();

        return swipes;
    }

    // REFACTOR
    public ArrayList<Swipe> getTestSwipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEST_SWIPES;
        ArrayList<Swipe> swipes = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Swipe swipe = new Swipe();
            swipe.setDuration(cursor.getDouble(cursor.getColumnIndex(COL_DURATION)));
            swipe.setAvgSize(cursor.getDouble(cursor.getColumnIndex(COL_AVG_SIZE)));
            swipe.setDownSize(cursor.getDouble(cursor.getColumnIndex(COL_DOWN_SIZE)));
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

            swipe.setMinXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ACCELEROMETER)));
            swipe.setMaxXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ACCELEROMETER)));
            swipe.setAvgXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ACCELEROMETER)));
            swipe.setStdXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ACCELEROMETER)));
            swipe.setVarXAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ACCELEROMETER)));

            swipe.setMinYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ACCELEROMETER)));
            swipe.setMaxYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ACCELEROMETER)));
            swipe.setAvgYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ACCELEROMETER)));
            swipe.setStdYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ACCELEROMETER)));
            swipe.setVarYAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ACCELEROMETER)));

            swipe.setMinZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_ACCELEROMETER)));
            swipe.setMaxZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_ACCELEROMETER)));
            swipe.setAvgZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_ACCELEROMETER)));
            swipe.setStdZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_ACCELEROMETER)));
            swipe.setVarZAccelerometer(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_ACCELEROMETER)));

            swipe.setMinXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_GYROSCOPE)));
            swipe.setMaxXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_GYROSCOPE)));
            swipe.setAvgXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_GYROSCOPE)));
            swipe.setStdXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_GYROSCOPE)));
            swipe.setVarXGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_GYROSCOPE)));

            swipe.setMinYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_GYROSCOPE)));
            swipe.setMaxYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_GYROSCOPE)));
            swipe.setAvgYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_GYROSCOPE)));
            swipe.setStdYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_GYROSCOPE)));
            swipe.setVarYGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_GYROSCOPE)));

            swipe.setMinZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_GYROSCOPE)));
            swipe.setMaxZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_GYROSCOPE)));
            swipe.setAvgZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_GYROSCOPE)));
            swipe.setStdZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_GYROSCOPE)));
            swipe.setVarZGyroscope(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_GYROSCOPE)));

            swipe.setMinXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_X_ORIENTATION)));
            swipe.setMaxXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_X_ORIENTATION)));
            swipe.setAvgXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_X_ORIENTATION)));
            swipe.setStdXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_X_ORIENTATION)));
            swipe.setVarXOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_X_ORIENTATION)));

            swipe.setMinYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Y_ORIENTATION)));
            swipe.setMaxYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Y_ORIENTATION)));
            swipe.setAvgYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Y_ORIENTATION)));
            swipe.setStdYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_Y_ORIENTATION)));
            swipe.setVarYOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Y_ORIENTATION)));

            swipe.setMinZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MIN_Z_ORIENTATION)));
            swipe.setMaxZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_MAX_Z_ORIENTATION)));
            swipe.setAvgZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_AVG_Z_ORIENTATION)));
            swipe.setStdZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_STD_Z_ORIENTATION)));
            swipe.setVarZOrientation(cursor.getDouble(cursor.getColumnIndex(COL_VAR_Z_ORIENTATION)));

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

    public ArrayList<String> getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        ArrayList<String> userData = new ArrayList<>();

        userData.add(cursor.getString(cursor.getColumnIndex(COL_NICKNAME)));
        userData.add(Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_GENDER))));
        userData.add(Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_AGE))));
        userData.add(cursor.getString(cursor.getColumnIndex(COL_NATIONALITY)));
        userData.add(Double.toString(cursor.getDouble(cursor.getColumnIndex(COL_HOLDING_HAND))));

        cursor.close();

        return userData;
    }

    public boolean saveFeatureData(int acceleration, int angular_velocity, int orientation, int swipe_duration, int swipe_start_end_pos, int swipe_velocity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FEATURE_DATA);

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_ACCELERATION, acceleration);
        contentValues.put(COL_ANGULAR_VELOCITY, angular_velocity);
        contentValues.put(COL_ORIENTATION, orientation);
        contentValues.put(COL_SWIPE_DURATION, swipe_duration);
        contentValues.put(COL_SWIPE_START_END_POS, swipe_start_end_pos);
        contentValues.put(COL_SWIPE_VELOCITY, swipe_velocity);

        long result = db.insert(FEATURE_DATA, null, contentValues);
        return result != -1;
    }

    public ArrayList<Integer> getFeatureData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FEATURE_DATA;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        ArrayList<Integer> featureData = new ArrayList<>();

        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_ACCELERATION)));
        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_ANGULAR_VELOCITY)));
        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_ORIENTATION)));
        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_SWIPE_DURATION)));
        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_SWIPE_START_END_POS)));
        featureData.add(cursor.getInt(cursor.getColumnIndex(COL_SWIPE_VELOCITY)));

        cursor.close();

        return featureData;
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
                        if (colNames[i].equals("USER_ID") || colNames[i].equals("nickname") || colNames[i].equals("nationality")) {
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
                            if (colNames[i].equals("USER_ID") || colNames[i].equals("nickname") || colNames[i].equals("nationality")) {
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
