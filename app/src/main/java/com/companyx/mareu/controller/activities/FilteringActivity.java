package com.companyx.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.databinding.ActivityFilteringBinding;
import com.companyx.mareu.di.DI_Salles;

import java.util.Calendar;

public class FilteringActivity extends AppCompatActivity {

    private ActivityFilteringBinding mBinding;

    private ApiServiceSalles mDummyApiServiceSalles;

    private String mDateDebut;
    public String mSequenceLieux;

    public static final String BUNDLE_FILTER_ROOM = "BUNDLE_FILTER_ROOM";
    public static final String BUNDLE_FILTER_DATE_START = "BUNDLE_FILTER_DATE_START";

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFilteringBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.filterActionToolbar);

        data = getIntent();
        String[] dates = data.getStringArrayExtra(MainActivity.BUNDLE_FILTER_REUNIONS);
//        Log.d("SPINNER", "Liste : " + dates.length);
        ArrayAdapter<String> adapterDates = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        adapterDates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.FiltreDateSpinner.setAdapter(adapterDates);

        mBinding.FiltreDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDateDebut = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Aucune date sélectionnée", Toast.LENGTH_LONG).show();
            }
        });

        mDummyApiServiceSalles = DI_Salles.getServiceSalles();
        String[] lieux = mDummyApiServiceSalles.getListeLieu();

        ArrayAdapter<String> adapterLieux = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lieux);
        mBinding.FiltreSalles.setAdapter(adapterLieux);
        mBinding.FiltreSalles.setThreshold(1);
        mBinding.FiltreSalles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    public static void navigateToFilteringActivity(Activity activity, int RequestCode, String name, String[] value) {
        Intent intent = new Intent(activity, FilteringActivity.class);
        intent.putExtra(name, value);
        ActivityCompat.startActivityForResult(activity, intent, RequestCode, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_action_check:
                saveActivityResultAndThenClose();
                return true;
            case R.id.filter_action_close:
                closeActivityWithoutSaving();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveActivityResultAndThenClose() {
        mSequenceLieux = mBinding.FiltreSalles.getText().toString();
        if (mSequenceLieux != "" || mDateDebut != "") {
            Intent intent = new Intent();
            if (mSequenceLieux != "") {
                intent.putExtra(BUNDLE_FILTER_ROOM, mSequenceLieux);
            }
            if (mDateDebut != "") {
                intent.putExtra(BUNDLE_FILTER_DATE_START, mDateDebut);
            }
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Veuillez saisir des critères", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeActivityWithoutSaving() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}