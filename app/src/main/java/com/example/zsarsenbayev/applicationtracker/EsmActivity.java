package com.example.zsarsenbayev.applicationtracker;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aware.Aware;
import com.aware.Aware_Preferences;

import java.util.ArrayList;

public class EsmActivity extends AppCompatActivity {

    private ArrayList<RadioGroup> radioGroups = new ArrayList<>();
    private Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esm);

        fillPanas();

        finishBtn = findViewById(R.id.finishBtn);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void fillPanas() {
        ArrayList<Object> panasItems = new ArrayList<>();
        panasItems.add("MiserablePleased");
        panasItems.add("SleepyAroused");

        LinearLayout rootLayout = findViewById(R.id.PanasItemsLinearLayout);

        for (int i = 0; i < panasItems.size(); i++) {
            LinearLayout panasContainer = new LinearLayout(this);
            panasContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams panasContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            panasContainerParams.setMargins(0, 0, 0, 50);
            panasContainer.setLayoutParams(panasContainerParams);

            RadioGroup panasRatingRadioGroup = new RadioGroup(this);
            panasRatingRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
            panasRatingRadioGroup.setGravity(Gravity.CENTER_HORIZONTAL);

            panasRatingRadioGroup.setContentDescription((CharSequence) panasItems.get(i));

            radioGroups.add(panasRatingRadioGroup);

            for (int j = 0; j < 5; j++) {
                RadioButton radioButton = new RadioButton(this);
                panasRatingRadioGroup.addView(radioButton);

                final float scale = this.getResources().getDisplayMetrics().density;
                int pixels = (int) (50 * scale + 0.5f);

                radioButton.getLayoutParams().width = pixels;
                radioButton.getLayoutParams().height = pixels;

                TypedValue typedValue = new TypedValue();
                this.getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, typedValue, true);
                if (typedValue.resourceId != 0) {
                    radioButton.setButtonDrawable(null);
                    radioButton.setBackgroundResource(typedValue.resourceId);
                }
            }

            if (i == 0) {
                // Legend
                LinearLayout panasLegendLL = new LinearLayout(this);
                panasLegendLL.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams panasLegendLLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                panasLegendLL.setLayoutParams(panasLegendLLParams);

                TextView minLabel = new TextView(this);
                minLabel.setText("Miserable");
                minLabel.setTextSize(20);
                minLabel.setGravity(Gravity.LEFT);
                LinearLayout.LayoutParams minLabelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                minLabelParams.setMargins(56, 0, 0, 4);
                minLabel.setLayoutParams(minLabelParams);

                panasLegendLL.addView(minLabel);

                TextView maxLabel = new TextView(this);
                maxLabel.setText("Pleased");
                maxLabel.setTextSize(20);
                maxLabel.setGravity(Gravity.RIGHT);
                LinearLayout.LayoutParams maxLabelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                maxLabelParams.setMargins(0, 0, 56, 4);
                maxLabel.setLayoutParams(maxLabelParams);

                panasLegendLL.addView(maxLabel);

                rootLayout.addView(panasLegendLL);
            }

            LinearLayout panasItemLL = new LinearLayout(this);
            panasItemLL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams panasItemLLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            panasItemLL.setLayoutParams(panasItemLLParams);

            panasContainer.addView(panasItemLL);
            panasContainer.addView(panasRatingRadioGroup);

            if (i == panasItems.size() - 1) {
                // Legend
                LinearLayout panasLegendLL = new LinearLayout(this);
                panasLegendLL.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams panasLegendLLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                panasLegendLL.setLayoutParams(panasLegendLLParams);

                TextView minLabel = new TextView(this);
                minLabel.setText("Sleepy");
                minLabel.setTextSize(20);
                minLabel.setGravity(Gravity.LEFT);
                LinearLayout.LayoutParams minLabelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                minLabelParams.setMargins(56, 40, 0, 20);
                minLabel.setLayoutParams(minLabelParams);

                panasLegendLL.addView(minLabel);

                TextView maxLabel = new TextView(this);
                maxLabel.setText("Aroused");
                maxLabel.setTextSize(20);
                maxLabel.setGravity(Gravity.RIGHT);
                LinearLayout.LayoutParams maxLabelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                maxLabelParams.setMargins(20, 40, 56, 10);
                maxLabel.setLayoutParams(maxLabelParams);

                panasLegendLL.addView(maxLabel);

                rootLayout.addView(panasLegendLL);
            }

            rootLayout.addView(panasContainer);
        }
    }

    private void saveData(){
        int groupsCount = radioGroups.size();
        String collectedAnswers = "";

        for (int i = 0; i < groupsCount; i++) {
            RadioGroup radioGroup = radioGroups.get(i);
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

            if (selectedRadioButtonId == -1) {
                Snackbar incompleteSnackbar = Snackbar.make(findViewById(R.id.LayoutPANAS), "Please complete all fields.", Snackbar.LENGTH_SHORT);
                incompleteSnackbar.show();
                return;
            } else {
                // Take modulus because ID can be any multiple of 10.
                Log.d("Niels", radioGroup.getContentDescription() + ":" + (Math.abs(selectedRadioButtonId - (i * 5)) % 10));
                collectedAnswers = collectedAnswers.concat(radioGroup.getContentDescription() + ":" + (Math.abs(selectedRadioButtonId - (i * 5)) % 10) + ";");
            }
        }
        Log.d("COLLECTED", System.currentTimeMillis()+"");
        Log.d("COLLECTED", Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID)+"");
        Log.d("COLLECTED", collectedAnswers);

        ContentValues esmData = new ContentValues();
        esmData.put(EsmProvider.EsmTable.ESM_TIMESTAMP, System.currentTimeMillis());
        esmData.put(EsmProvider.EsmTable.ESM_DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        esmData.put(EsmProvider.EsmTable.ESM_ANSWER, collectedAnswers);
        getApplicationContext().getContentResolver().insert(EsmProvider.EsmTable.ESM_CONTENT_URI, esmData);
        // TODO: stop esm service

        Intent intent = new Intent(EsmActivity.this, EsmService.class);
        stopService(intent);
        finish();
    }

}
