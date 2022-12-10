package it.unibz.swipegan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * The user profile activity.
 * Handles the logic responsible for correctly populating and updating the user profile view and its related fields.
 * <p>
 * The user profile view allows to set the user's:
 * <ul>
 *     <li>Nickname.
 *     <li>Gender (DWTD, Male, Female).
 *     <li>Age range (DWTD, 0-20, 21-40, 41-60, 60+).
 *     <li>Nationality.
 *     <li>Hand used for holding the device (DWTD, Right, Left, Both).
 * </ul>
 */
public class ProfileActivity extends AppCompatActivity {
    /**
     * Called upon the creation of the Activity, it populates the user profile fields based on the data currently stored in the DB.<p>
     * Additionally, it sets up the Save button event listener which allows the system to save any changes made to the user profile to the DB.
     *
     * @param savedInstanceState Reference to the bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        Map<String, String> userData = dbHelper.getUserData();

        EditText nicknameEditText = findViewById(R.id.nicknameEditText);
        nicknameEditText.setText(userData.get(DatabaseHelper.COL_NICKNAME));

        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        ArrayList<Integer> genderRadioGroupIndices = new ArrayList<Integer>();
        genderRadioGroupIndices.add(new Integer(R.id.genderNoneRadioButton));
        genderRadioGroupIndices.add(new Integer(R.id.genderMaleRadioButton));
        genderRadioGroupIndices.add(new Integer(R.id.genderFemaleRadioButton));
        int genderIdx = Integer.parseInt(userData.get(DatabaseHelper.COL_GENDER));
        genderRadioGroup.check(genderRadioGroupIndices.get(genderIdx));

        RadioGroup ageRadioGroup = findViewById(R.id.ageRadioGroup);
        ArrayList<Integer> ageRadioGroupIndices = new ArrayList<Integer>();
        ageRadioGroupIndices.add(new Integer(R.id.ageNoneRadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age20RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age40RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age60RadioButton));
        ageRadioGroupIndices.add(new Integer(R.id.age80RadioButton));
        int ageIdx = Integer.parseInt(userData.get(DatabaseHelper.COL_AGE));
        ageRadioGroup.check(ageRadioGroupIndices.get(ageIdx));

        EditText nationalityEditText = findViewById(R.id.nationalityEditText);
        nationalityEditText.setText(userData.get(DatabaseHelper.COL_NATIONALITY));

        RadioGroup holdingRadioGroup = findViewById(R.id.holdingRadioGroup);
        ArrayList<Integer> holdingRadioGroupIndices = new ArrayList<Integer>();
        holdingRadioGroupIndices.add(new Integer(R.id.holdingNoneRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingRightRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingLeftRadioButton));
        holdingRadioGroupIndices.add(new Integer(R.id.holdingBothRadioButton));
        int holdingIdx = Integer.parseInt(userData.get(DatabaseHelper.COL_HOLDING_HAND));
        holdingRadioGroup.check(holdingRadioGroupIndices.get(holdingIdx));

        Button saveProfileButton = findViewById(R.id.saveProfileButton);
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

                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
