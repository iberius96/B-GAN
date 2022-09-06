package it.unibz.swipegan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        Map<String, Integer> featureData = dbHelper.getFeatureData();

        CheckBox accelerationCheckBox = findViewById(R.id.accelerationCheckBox);
        accelerationCheckBox.setChecked(featureData.get(DatabaseHelper.COL_ACCELERATION) == 1);

        CheckBox angularVelocityCheckBox = findViewById(R.id.angularVelocityCheckBox);
        angularVelocityCheckBox.setChecked(featureData.get(DatabaseHelper.COL_ANGULAR_VELOCITY) == 1);

        CheckBox orientationCheckBox = findViewById(R.id.orientationCheckBox);
        orientationCheckBox.setChecked(featureData.get(DatabaseHelper.COL_ORIENTATION) == 1);

        CheckBox swipeDurationCheckBox = findViewById(R.id.swipeDurationCheckBox);
        swipeDurationCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SWIPE_DURATION) == 1);

        CheckBox swipeShapeCheckBox = findViewById(R.id.swipeShapeCheckBox);
        swipeShapeCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SWIPE_SHAPE) == 1);

        Spinner swipeSegmentSpinner = (Spinner) findViewById(R.id.swipeSegmentsSpinner);
        ArrayAdapter<CharSequence> swipeSegmentdapter = ArrayAdapter.createFromResource(this, R.array.swipe_segments, android.R.layout.simple_spinner_item);
        swipeSegmentdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        swipeSegmentSpinner.setAdapter(swipeSegmentdapter);

        if(featureData.get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS) == 0) {
            swipeSegmentSpinner.setSelection(((ArrayAdapter<String>) swipeSegmentSpinner.getAdapter()).getPosition(String.valueOf(DatabaseHelper.DEFAULT_SEGMENTS)));
        } else {
            swipeSegmentSpinner.setSelection(((ArrayAdapter<String>) swipeSegmentSpinner.getAdapter()).getPosition(featureData.get(DatabaseHelper.COL_SWIPE_SHAPE_SEGMENTS).toString()));
        }
        swipeSegmentSpinner.setEnabled(swipeShapeCheckBox.isChecked());
        Integer initialSegmentSelection = Integer.parseInt((String) swipeSegmentSpinner.getSelectedItem());

        swipeShapeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                swipeSegmentSpinner.setEnabled(swipeShapeCheckBox.isChecked());
            }
        });

        CheckBox swipeTouchSizeCheckBox = findViewById(R.id.swipeTouchSizeCheckBox);
        swipeTouchSizeCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SWIPE_TOUCH_SIZE) == 1);

        CheckBox swipeStartEndPosCheckBox = findViewById(R.id.swipeStartEndPosCheckBox);
        swipeStartEndPosCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SWIPE_START_END_POS) == 1);

        CheckBox swipeVelocityCheckBox = findViewById(R.id.swipeVelocityCheckBox);
        swipeVelocityCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SWIPE_VELOCITY) == 1);

        CheckBox keystrokeCheckBox = findViewById(R.id.keystrokeCheckBox);
        keystrokeCheckBox.setChecked(featureData.get(DatabaseHelper.COL_KEYSTROKE) == 1);
        boolean initialKeystrokeEnabled = keystrokeCheckBox.isChecked();

        Spinner keystrokeLengthSpinner = (Spinner) findViewById(R.id.keystrokeLengthSpinner);
        ArrayAdapter<CharSequence> keystrokeLengthAdapter = ArrayAdapter.createFromResource(this, R.array.pin_length, android.R.layout.simple_spinner_item);
        keystrokeLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keystrokeLengthSpinner.setAdapter(keystrokeLengthAdapter);
        if(featureData.get(DatabaseHelper.COL_PIN_LENGTH) == 0) {
            keystrokeLengthSpinner.setSelection(((ArrayAdapter<String>) keystrokeLengthSpinner.getAdapter()).getPosition(String.valueOf(DatabaseHelper.DEFAULT_PIN_LENGTH)));
        } else {
            keystrokeLengthSpinner.setSelection(((ArrayAdapter<String>) keystrokeLengthSpinner.getAdapter()).getPosition(featureData.get(DatabaseHelper.COL_PIN_LENGTH).toString()));
        }
        keystrokeLengthSpinner.setEnabled(keystrokeCheckBox.isChecked());
        Integer initialPinLength = Integer.parseInt((String) keystrokeLengthSpinner.getSelectedItem());

        CheckBox keystrokeDurationsCheckBox = findViewById(R.id.keystrokeDurationsCheckBox);
        keystrokeDurationsCheckBox.setChecked(featureData.get(DatabaseHelper.COL_KEYSTROKE_DURATIONS) == 1);
        keystrokeDurationsCheckBox.setEnabled(keystrokeCheckBox.isChecked());

        CheckBox keystrokeIntervalsCheckBox = findViewById(R.id.keystrokeIntervalsCheckBox);
        keystrokeIntervalsCheckBox.setChecked(featureData.get(DatabaseHelper.COL_KEYSTROKE_INTERVALS) == 1);
        keystrokeIntervalsCheckBox.setEnabled(keystrokeCheckBox.isChecked());

        keystrokeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                keystrokeLengthSpinner.setEnabled(keystrokeCheckBox.isChecked());
                keystrokeDurationsCheckBox.setEnabled(keystrokeCheckBox.isChecked());
                keystrokeIntervalsCheckBox.setEnabled(keystrokeCheckBox.isChecked());
            }
        });

        Button saveProfileButton = findViewById(R.id.saveProfileButton);
        class MyListener implements View.OnClickListener {
            private ModelActivity modelActivity;

            public MyListener(ModelActivity modelActivity) {
                super();
                this.modelActivity = modelActivity;
            }

            // TODO: Change checked logic
            @Override
            public void onClick(View v) {
                if( accelerationCheckBox.isChecked() ||
                        angularVelocityCheckBox.isChecked() ||
                        orientationCheckBox.isChecked() ||
                        swipeDurationCheckBox.isChecked() ||
                        swipeShapeCheckBox.isChecked() ||
                        swipeTouchSizeCheckBox.isChecked() ||
                        swipeStartEndPosCheckBox.isChecked() ||
                        swipeVelocityCheckBox.isChecked() ||
                        keystrokeCheckBox.isChecked() ||
                        keystrokeDurationsCheckBox.isChecked() ||
                        keystrokeIntervalsCheckBox.isChecked()
                ) {
                    dbHelper.saveFeatureData(
                            accelerationCheckBox.isChecked() ? 1 : 0,
                            angularVelocityCheckBox.isChecked() ? 1 : 0,
                            orientationCheckBox.isChecked() ? 1 : 0,
                            swipeDurationCheckBox.isChecked() ? 1 : 0,
                            swipeShapeCheckBox.isChecked() ? 1 : 0,
                            Integer.parseInt((String) swipeSegmentSpinner.getSelectedItem()),
                            swipeTouchSizeCheckBox.isChecked() ? 1 : 0,
                            swipeStartEndPosCheckBox.isChecked() ? 1 : 0,
                            swipeVelocityCheckBox.isChecked() ? 1 : 0,
                            keystrokeCheckBox.isChecked() ? 1 : 0,
                            Integer.parseInt((String) keystrokeLengthSpinner.getSelectedItem()),
                            keystrokeDurationsCheckBox.isChecked() ? 1 : 0,
                            keystrokeIntervalsCheckBox.isChecked() ? 1 : 0
                    );

                    Integer curSegmentSelection = Integer.parseInt((String) swipeSegmentSpinner.getSelectedItem());
                    Integer curPinLength = Integer.parseInt((String) keystrokeLengthSpinner.getSelectedItem());
                    boolean curKeystrokeEnabled = keystrokeCheckBox.isChecked();

                    Intent resultIntent = new Intent();

                    Map<String, Object> modelSelection = new HashMap<>();
                    modelSelection.put("curSegmentSelection", curSegmentSelection);
                    modelSelection.put("initialSegmentSelection", initialSegmentSelection);
                    modelSelection.put("curKeystrokeEnabled", curKeystrokeEnabled);
                    modelSelection.put("initialKeystrokeEnabled", initialKeystrokeEnabled);
                    modelSelection.put("curPinLength", curPinLength);
                    modelSelection.put("initialPinLength", initialPinLength);


                    resultIntent.putExtra("modelSelection", (Serializable) modelSelection);
                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModelActivity.this);
                    builder.setMessage("Please select at least one feature type").setTitle("No feature selected");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
                    builder.show();
                }
            }
        }
        saveProfileButton.setOnClickListener(new MyListener(this));

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
