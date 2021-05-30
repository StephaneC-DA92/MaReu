package com.companyx.mareu.controller.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.data.DummyApiServiceSalles;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainFragment mMainFragment;
    private FloatingActionButton mButton;

    private boolean roomFilterEnabled,timeFilterEnabled;

    public static final int NEW_MEETING_ACTIVITY_CODE = 98;
    public static final int FILTER_ACTIVITY_CODE = 90;

    public static final String BUNDLE_FILTER_REUNIONS = "BUNDLE_FILTER_REUNIONS";

    Date mDateDebutFiltre;
    List<Salle> sallesFiltre;

    String listeLieux, dateDebut ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        configurerEtAfficherMainFragment();

        mButton = (FloatingActionButton) findViewById(R.id.add_meeting);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this,NEW_MEETING_ACTIVITY_CODE);
            }
        });

        roomFilterEnabled=false;
        timeFilterEnabled=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        montrer menu contextuel avec choix de filtre;
        switch(item.getItemId()){
            case R.id.filtrerSalleHeure :
                FilteringActivity.navigateToFilteringActivity(MainActivity.this,FILTER_ACTIVITY_CODE,BUNDLE_FILTER_REUNIONS,mMainFragment.mDates);
                break;

            case R.id.sansFiltre :
                mMainFragment.sansFiltrer();
                break;

            case R.id.tri_lieu_croissant:
                mMainFragment.trierLieuCroissant();
                break;

            case R.id.tri_lieu_decroissant:
                mMainFragment.trierLieuDecroissant();
                break;

            case R.id.tri_heure_croissante:
                mMainFragment.trierHeureCroissant();
                break;

            case R.id.tri_heure_decroissante:
                mMainFragment.trierHeureDecroissant();
                break;

            case R.id.sans_tri:
                mMainFragment.sansTrier();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NEW_MEETING_ACTIVITY_CODE && resultCode == RESULT_OK){
            //DONE : transferer reunion dans MainFragment
            Reunion reunion = (Reunion) data.getSerializableExtra(AddMeetingActivity.BUNDLE_EXTRA_MEETING);
            mMainFragment.ajouterNouvelleReunion(reunion);
        }
        if(requestCode==FILTER_ACTIVITY_CODE && resultCode == RESULT_OK){
            listeLieux = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_ROOM);
            dateDebut = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_DATE_START);
            filtrerDansMainFragment(listeLieux,dateDebut);
        }
    }

    //Inspiré de com/companyx/mareu/controller/fragments/AddMeetingFragment.java:179

    private List<Salle> getListeSallesFromLieuxSequence(String ListeLieuxAvecVirgule){
        List<Salle> listeSalles = new ArrayList<Salle>();

        List<String> listeLieux = Arrays.asList(ListeLieuxAvecVirgule.split(", "));

        for (String lieu : listeLieux){
            if(lieu==""){
                listeLieux.remove(lieu);
            }
        }

        //TODO : instance unique mDummyApiServiceSalles du MainFragment?
        for (String lieu : listeLieux){
            listeSalles.add(new DummyApiServiceSalles().creerCatalogueLieu().get(lieu));
        }
        return listeSalles;
    }

/*    private List<Object> getListofObjectsFromSequence(Object object, String SequenceWithComma, Map<String,Object> catalogue){
        List<Object> listObjects = new ArrayList<Object>();
        List<String> listObjectFields = Arrays.asList(SequenceWithComma.split(", "));

        for (String objectField : listObjectFields){
            if(objectField==""){
                listObjectFields.remove(objectField);
            }
        }

        for (String objectField : listObjectFields){
            listObjects.add(catalogue.get(objectField));
        }
        return listObjects;
    }*/

// --------------
    // FRAGMENTS
    // --------------

    private void configurerEtAfficherMainFragment(){
        //  Appel au SupportFragmentManager pour trouver une fragment existant dans le conteneur FrameLayout
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, mMainFragment)
                    .commit();
        }
    }

    private void filtrerDansMainFragment(String listeLieux,String dateDebut){
        if(listeLieux.compareTo("")!= 0){
            sallesFiltre = getListeSallesFromLieuxSequence(listeLieux);
            if(dateDebut.compareTo("")!= 0){
                mDateDebutFiltre= new DateHeure(dateDebut).formatParseDate();
                roomFilterEnabled=true;
                timeFilterEnabled=true;
                mMainFragment.filtrerAvecSallesEtDateHeure(sallesFiltre,mDateDebutFiltre);
            } else {
                roomFilterEnabled=true;
                mMainFragment.filtrerAvecSalles(sallesFiltre);
            }
        } else {
//               TODO : à factoriser
            if(dateDebut.compareTo("")!= 0){
                mDateDebutFiltre= new DateHeure(dateDebut).formatParseDate();
                timeFilterEnabled=true;
                mMainFragment.filtrerAvecDate(mDateDebutFiltre);
            } else {
                Log.d("ERROR_FILTRE","Pas de critères de filtre reçus");
            }
        }
    }
}