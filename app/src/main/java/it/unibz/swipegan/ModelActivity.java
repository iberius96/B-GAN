package it.unibz.swipegan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        Map<String, Integer> featureData = dbHelper.getFeatureData();

        List<List<DatabaseHelper.ModelType>> initialActiveModels = dbHelper.getActiveModels().stream().filter(s -> dbHelper.isModelEnabled(s)).collect(Collectors.toList());

        Spinner modelsSpinner = (Spinner) findViewById(R.id.modelsSpinner);
        ArrayAdapter<CharSequence> modelsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.models_combinations, android.R.layout.simple_spinner_item);
        modelsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelsSpinner.setAdapter(modelsSpinnerAdapter);
        modelsSpinner.setSelection(dbHelper.getFeatureData().get(DatabaseHelper.COL_MODELS_COMBINATIONS));
        Integer initialModelsSelection = modelsSpinner.getSelectedItemPosition();

        CheckBox rawDataCheckBox = findViewById(R.id.rawDataCheckBox);
        rawDataCheckBox.setChecked(featureData.get(DatabaseHelper.COL_RAW_DATA) == 1);
        boolean initialRawDataEnabled = rawDataCheckBox.isChecked();

        EditText rawDataFrequencyEditTextNumber = findViewById(R.id.rawDataFrequencyEditTextNumber);
        rawDataFrequencyEditTextNumber.setText(String.valueOf(featureData.get(DatabaseHelper.COL_RAW_DATA_FREQUENCY)));
        Integer initialRawDataFrequency = Integer.parseInt(String.valueOf(rawDataFrequencyEditTextNumber.getText()));

        rawDataCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rawDataFrequencyEditTextNumber.setEnabled(rawDataCheckBox.isChecked());
            }
        });

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
        ArrayAdapter<CharSequence> swipeSegmentAdapter = ArrayAdapter.createFromResource(this, R.array.swipe_segments, android.R.layout.simple_spinner_item);
        swipeSegmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        swipeSegmentSpinner.setAdapter(swipeSegmentAdapter);

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

        CheckBox signatureCheckBox = findViewById(R.id.signatureCheckBox);
        signatureCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SIGNATURE) == 1);
        boolean initialSignatureEnabled = signatureCheckBox.isChecked();

        CheckBox signatureStartEndPosCheckBox = findViewById(R.id.signatureStartEndPosCheckBox);
        signatureStartEndPosCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SIGNATURE_START_END_POS) == 1);
        signatureStartEndPosCheckBox.setEnabled(signatureCheckBox.isChecked());

        CheckBox signatureVelocityCheckBox = findViewById(R.id.signatureVelocityCheckBox);
        signatureVelocityCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SIGNATURE_VELOCITY) == 1);
        signatureVelocityCheckBox.setEnabled(signatureCheckBox.isChecked());

        CheckBox signatureShapeCheckBox = findViewById(R.id.signatureShapeCheckBox);
        signatureShapeCheckBox.setChecked(featureData.get(DatabaseHelper.COL_SIGNATURE_SHAPE) == 1);
        signatureShapeCheckBox.setEnabled(signatureCheckBox.isChecked());

        Spinner signatureSegmentsSpinner = (Spinner) findViewById(R.id.signatureSegmentsSpinner);
        ArrayAdapter<CharSequence> signatureSegmentsAdapter = ArrayAdapter.createFromResource(this, R.array.swipe_segments, android.R.layout.simple_spinner_item);
        signatureSegmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signatureSegmentsSpinner.setAdapter(signatureSegmentsAdapter);

        if(featureData.get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS) == 0) {
            signatureSegmentsSpinner.setSelection(((ArrayAdapter<String>) signatureSegmentsSpinner.getAdapter()).getPosition(String.valueOf(DatabaseHelper.DEFAULT_SEGMENTS)));
        } else {
            signatureSegmentsSpinner.setSelection(((ArrayAdapter<String>) signatureSegmentsSpinner.getAdapter()).getPosition(featureData.get(DatabaseHelper.COL_SIGNATURE_SHAPE_SEGMENTS).toString()));
        }
        signatureSegmentsSpinner.setEnabled(signatureCheckBox.isChecked() && signatureShapeCheckBox.isChecked());
        Integer initialSignatureSegmentSelection = Integer.parseInt((String) signatureSegmentsSpinner.getSelectedItem());

        signatureShapeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signatureSegmentsSpinner.setEnabled(signatureShapeCheckBox.isChecked());
            }
        });

        signatureCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signatureStartEndPosCheckBox.setEnabled(signatureCheckBox.isChecked());
                signatureVelocityCheckBox.setEnabled(signatureCheckBox.isChecked());
                signatureShapeCheckBox.setEnabled(signatureCheckBox.isChecked());
                signatureSegmentsSpinner.setEnabled(signatureCheckBox.isChecked());
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
                if(     accelerationCheckBox.isChecked() ||
                        angularVelocityCheckBox.isChecked() ||
                        orientationCheckBox.isChecked() ||
                        swipeDurationCheckBox.isChecked() ||
                        swipeShapeCheckBox.isChecked() ||
                        swipeTouchSizeCheckBox.isChecked() ||
                        swipeStartEndPosCheckBox.isChecked() ||
                        swipeVelocityCheckBox.isChecked() ||
                        (keystrokeCheckBox.isChecked() &&
                            (keystrokeDurationsCheckBox.isChecked() ||
                            keystrokeIntervalsCheckBox.isChecked())) ||
                        (signatureCheckBox.isChecked() &&
                            (signatureVelocityCheckBox.isChecked() ||
                            signatureStartEndPosCheckBox.isChecked() ||
                            signatureShapeCheckBox.isChecked()))

                ) {
                    dbHelper.saveFeatureData(
                            modelsSpinner.getSelectedItemPosition(),
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
                            keystrokeIntervalsCheckBox.isChecked() ? 1 : 0,
                            signatureCheckBox.isChecked() ? 1 : 0,
                            signatureStartEndPosCheckBox.isChecked() ? 1 : 0,
                            signatureVelocityCheckBox.isChecked() ? 1 : 0,
                            signatureShapeCheckBox.isChecked() ? 1 : 0,
                            Integer.parseInt((String) signatureSegmentsSpinner.getSelectedItem()),
                            rawDataCheckBox.isChecked() ? 1 : 0,
                            Integer.parseInt(rawDataFrequencyEditTextNumber.getText().toString())
                    );

                    Integer curModelsSelection = modelsSpinner.getSelectedItemPosition();

                    boolean curRawDataEnabled = rawDataCheckBox.isChecked();
                    Integer curRawDataFrequency = Integer.parseInt(String.valueOf(rawDataFrequencyEditTextNumber.getText()));

                    Integer curSegmentSelection = Integer.parseInt((String) swipeSegmentSpinner.getSelectedItem());
                    Integer curPinLength = Integer.parseInt((String) keystrokeLengthSpinner.getSelectedItem());
                    boolean curKeystrokeEnabled = keystrokeCheckBox.isChecked();

                    boolean curSignatureEnabled = signatureCheckBox.isChecked();
                    Integer curSignatureSegmentSelection = Integer.parseInt((String) signatureSegmentsSpinner.getSelectedItem());

                    Intent resultIntent = new Intent();

                    Map<String, Object> modelSelection = new HashMap<>();
                    modelSelection.put("initialActiveModels", initialActiveModels);

                    modelSelection.put("curModelsSelection", curModelsSelection);
                    modelSelection.put("initialModelsSelection", initialModelsSelection);

                    modelSelection.put("curRawDataEnabled", curRawDataEnabled);
                    modelSelection.put("initialRawDataEnabled", initialRawDataEnabled);
                    modelSelection.put("curRawDataFrequency", curRawDataFrequency);
                    modelSelection.put("initialRawDataFrequency", initialRawDataFrequency);

                    modelSelection.put("curSegmentSelection", curSegmentSelection);
                    modelSelection.put("initialSegmentSelection", initialSegmentSelection);

                    modelSelection.put("curKeystrokeEnabled", curKeystrokeEnabled);
                    modelSelection.put("initialKeystrokeEnabled", initialKeystrokeEnabled);
                    modelSelection.put("curPinLength", curPinLength);
                    modelSelection.put("initialPinLength", initialPinLength);

                    modelSelection.put("curSignatureEnabled", curSignatureEnabled);
                    modelSelection.put("initialSignatureEnabled", initialSignatureEnabled);
                    modelSelection.put("curSignatureSegmentSelection", curSignatureSegmentSelection);
                    modelSelection.put("initialSignatureSegmentSelection", initialSignatureSegmentSelection);

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
