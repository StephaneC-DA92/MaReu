package com.companyx.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.data.DummyApiServiceCollaborateurs;
import com.companyx.mareu.data.DummyApiServiceSalles;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.databinding.ActivityFilteringBinding;
import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FilteringActivity extends AppCompatActivity {

    private ActivityFilteringBinding mBinding;

    //Essayer DialogFragment au lieu de Fragment
    private final Calendar mCalendrier = Calendar.getInstance();
    private int mAnnee, mMois,mJour,mHeure,mMinutes;

    public DummyApiServiceSalles mDummyApiServiceSalles;

    private String maDateDebut
//            ,monHeureDebut
            ;
//    private String maDateFin,monHeureFin;

    public String mSequenceLieuxFiltre;

    public static final String BUNDLE_FILTER_ROOM = "BUNDLE_FILTER_ROOM";
    public static final String BUNDLE_FILTER_DATE_START = "BUNDLE_FILTER_DATE_START";
    public static final String BUNDLE_FILTER_DATE_END = "BUNDLE_FILTER_DATE_END";
    public static final String BUNDLE_FILTER_HOUR_START = "BUNDLE_FILTER_HOUR_START";
    public static final String BUNDLE_FILTER_HOUR_END = "BUNDLE_FILTER_HOUR_END";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityFilteringBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_filtering);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.filter_action_toolbar);
        setSupportActionBar(mBinding.filterActionToolbar);

/*        mFiltreHeureDebutEditText = (EditText) findViewById(R.id.FiltreDateHeureDebut);
        mFiltreFinEditText = (EditText) findViewById(R.id.FiltreDateHeureFin);*/

        mAnnee = mCalendrier.get(Calendar.YEAR);
        mMois = mCalendrier.get(Calendar.MONTH);
        mJour = mCalendrier.get(Calendar.DAY_OF_MONTH);

        mHeure = mCalendrier.get(Calendar.HOUR_OF_DAY);
        mMinutes = mCalendrier.get(Calendar.MINUTE);

//        mFiltreHeureDebutEditText.setOnClickListener(new View.OnClickListener() {
        mBinding.FiltreDateHeureDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDatePickerDialogDebut(mBinding.FiltreDateHeureDebut.getContext());
            }
        });

//        mFiltreFinEditText remplacé par  mBinding.FiltreDateHeureFin
/*        mBinding.FiltreDateHeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateTimePickerDialogFin(mBinding.FiltreDateHeureFin.getContext());
            }
        });*/

//        mFiltreSalletextview = (MultiAutoCompleteTextView) findViewById(R.id.FiltreSalles);
        //TODO : instance unique mDummyApiServiceSalles du MainFragment?
        mDummyApiServiceSalles = new DummyApiServiceSalles();
        String[] lieux = mDummyApiServiceSalles.getListeLieu();
        ArrayAdapter<String> adapterLieux = new  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lieux);
        mBinding.FiltreSalles.setAdapter(adapterLieux);
        mBinding.FiltreSalles.setThreshold(1);
        mBinding.FiltreSalles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    public static void navigateToFilteringActivity(Activity activity, int RequestCode){
        Intent intent = new Intent(activity,FilteringActivity.class);
        ActivityCompat.startActivityForResult(activity,intent, RequestCode,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_actions, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
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

    private void onDatePickerDialogDebut(Context context){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        maDateDebut = jour + "-" + (mois + 1) + "-" + annee;
                        mBinding.FiltreDateHeureDebut.setText(maDateDebut);
       /*                 TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int heure, int minutes) {
                                        monHeureDebut = heure + "h" + minutes;
                                        mBinding.FiltreDateHeureDebut.append(" "+monHeureDebut);
                                    }
                                }, mHeure, mMinutes, false);
                        timePickerDialog.show();*/
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }

/*
    private void onDateTimePickerDialogFin(Context context){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        maDateFin = jour + "-" + (mois + 1) + "-" + annee;
                        mBinding.FiltreDateHeureFin.setText(maDateFin);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int heure,
                                                          int minutes) {
                                        monHeureFin = heure + "h" + minutes;
                                        mBinding.FiltreDateHeureFin.append(" "+monHeureFin);
                                    }
                                }, mHeure, mMinutes, false);
                        timePickerDialog.show();
// Calendar .set nne/m/ .. heure. Puis .gettime.
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }
*/

    private void saveActivityResultAndThenClose(){
        mSequenceLieuxFiltre =mBinding.FiltreSalles.getText().toString();
    if(mSequenceLieuxFiltre!=""||maDateDebut!=null
//            &&monHeureDebut!=null||maDateFin!=null&&monHeureFin!=null
    ) {
        Intent intent = new Intent();
        if (mSequenceLieuxFiltre != "") {
            //        DONE : seriazable à faire pour réunion KO : essayer parcelable
            intent.putExtra(BUNDLE_FILTER_ROOM, mSequenceLieuxFiltre);
        }

        if (maDateDebut != null
//                && monHeureDebut != null
        ) {
            //        DONE : seriazable à faire pour réunion KO : essayer parcelable
            intent.putExtra(BUNDLE_FILTER_DATE_START, maDateDebut);
//            intent.putExtra(BUNDLE_FILTER_HOUR_START, monHeureDebut);
        }
/*        if (maDateFin != null && monHeureFin != null) {
            //        DONE : seriazable à faire pour réunion KO : essayer parcelable
            intent.putExtra(BUNDLE_FILTER_DATE_END, maDateFin);
            intent.putExtra(BUNDLE_FILTER_HOUR_END, monHeureFin);
        }*/
        setResult(RESULT_OK, intent);
        finish();
    } else {
        Toast.makeText(this,"Veuillez saisir des critères",Toast.LENGTH_SHORT).show();
        }
    };

    private void closeActivityWithoutSaving(){
        setResult(RESULT_CANCELED,null);
        finish();
    };
}