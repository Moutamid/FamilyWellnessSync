package com.moutimid.familywellness.home.BMI;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.moutamid.familywellness.R;

public class MainActivity extends AppCompatActivity {
    private EditText weightKgEditText, heightCmEditText;
    private EditText weightLbsEditText, heightFtEditText, heightInEditText;
    private Button calculateButton;
    private TextView bmiTextView, categoryTextView;
    private Switch toggleUnitsButton;
    private CardView bmiResultCardView;

    private boolean inMetricUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_main);

        weightKgEditText = findViewById(R.id.activity_main_weightkgs);
        heightCmEditText = findViewById(R.id.activity_main_heightcm);

        weightLbsEditText = findViewById(R.id.activity_main_weightlbs);
        heightFtEditText = findViewById(R.id.activity_main_heightfeet);
        heightInEditText = findViewById(R.id.activity_main_heightinches);

        calculateButton = findViewById(R.id.activity_main_calculate);
        toggleUnitsButton = findViewById(R.id.activity_main_toggleunits);

        bmiTextView = findViewById(R.id.activity_main_bmi);
        categoryTextView = findViewById(R.id.activity_main_category);
        bmiResultCardView = findViewById(R.id.activity_main_resultcard);
        inMetricUnits = true;
        updateInputsVisibility();
        bmiResultCardView.setVisibility(View.GONE);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inMetricUnits) {
                    if (weightKgEditText.length() == 0 || heightCmEditText.length() == 0) {
                        Toast.makeText(MainActivity.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightInCms = Double.parseDouble(heightCmEditText.getText().toString());
                        double weightInKgs = Double.parseDouble(weightKgEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIMetric(heightInCms, weightInKgs);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            displayBMI(bmi);
                        }
                    }
                } else {
                    if (weightLbsEditText.length() == 0 || heightFtEditText.length() == 0 || heightInEditText.length() == 0) {
                        Toast.makeText(MainActivity.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightFeet = Double.parseDouble(heightFtEditText.getText().toString());
                        double heightInches = Double.parseDouble(heightInEditText.getText().toString());
                        double weightLbs = Double.parseDouble(weightLbsEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIImperial(heightFeet, heightInches, weightLbs);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            displayBMI(bmi);
                        }
                    }
                }
            }
        });

        toggleUnitsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                inMetricUnits = !inMetricUnits;
                updateInputsVisibility();
                if (b) {
                    toggleUnitsButton.setText(("Metric Units"));
                } else {
                    toggleUnitsButton.setText(("Imperial Units"));

                }
            }
        });
    }


    private void updateInputsVisibility() {
        if (inMetricUnits) {
            heightCmEditText.setVisibility(View.VISIBLE);
            weightKgEditText.setVisibility(View.VISIBLE);
            heightFtEditText.setVisibility(View.GONE);
            heightInEditText.setVisibility(View.GONE);
            weightLbsEditText.setVisibility(View.GONE);
        } else {
            heightCmEditText.setVisibility(View.GONE);
            weightKgEditText.setVisibility(View.GONE);
            heightFtEditText.setVisibility(View.VISIBLE);
            heightInEditText.setVisibility(View.VISIBLE);
            weightLbsEditText.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayBMI(double bmi) {
        bmiResultCardView.setVisibility(View.VISIBLE);

        bmiTextView.setText("BMI " + String.format("%.2f", bmi));

        String bmiCategory = BMICalcUtil.getInstance().classifyBMI(bmi);
        categoryTextView.setText("You are " + bmiCategory);

        switch (bmiCategory) {
            case BMICalcUtil.BMI_CATEGORY_UNDERWEIGHT:
                bmiTextView.setTextColor(getColor(R.color.underweight));
                break;
            case BMICalcUtil.BMI_CATEGORY_HEALTHY:
                bmiTextView.setTextColor(getColor(R.color.normal));
                break;
            case BMICalcUtil.BMI_CATEGORY_OVERWEIGHT:
                bmiTextView.setTextColor(getColor(R.color.overweight));
                break;
            case BMICalcUtil.BMI_CATEGORY_OBESE:
                bmiTextView.setTextColor(getColor(R.color.obsess));
                break;
            case BMICalcUtil.BMI_CATEGORY_EXTREMELY_OBESE:
                bmiTextView.setTextColor(getColor(R.color.extremely_obses));
                break;
        }
    }

    public void bmi(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }


    public void back(View view) {
        onBackPressed();
    }
}