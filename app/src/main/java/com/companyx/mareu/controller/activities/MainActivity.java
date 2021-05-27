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
import com.companyx.mareu.controller.MeetingListAdapter;
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
    private MainFragment mMainFragment2;
    private FloatingActionButton mButton;

    private boolean roomFilterEnabled,timeFilterEnabled;

    public static final int NEW_MEETING_ACTIVITY_CODE = 98;
    public static final int FILTER_ACTIVITY_CODE = 90;
    public static final int FILTER_ACTIVITY_ROOM_CODE = 91;
    public static final int FILTER_ACTIVITY_START_DATE_CODE = 92;
    public static final int FILTER_ACTIVITY_START_HOUR_CODE = 93;
    public static final int FILTER_ACTIVITY_END_DATE_CODE = 94;
    public static final int FILTER_ACTIVITY_END_HOUR_CODE = 95;

    Date mDateDebutFiltre, mDateFinFiltre;
    List<Salle> sallesFiltre;

    String param1, param2;
    String methodeFiltreTriParametree;

    String ListeLieux, dateDebut, dateFin, heureDebut, heureFin ;

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
                FilteringActivity.navigateToFilteringActivity(MainActivity.this,FILTER_ACTIVITY_CODE);
            case R.id.sansFiltre :

            case R.id.tri_lieu_croissant:
                mMainFragment.trierLieuCroissant();
            case R.id.tri_lieu_decroissant:
                mMainFragment.trierLieuDecroissant();
            case R.id.tri_heure_croissante:
                mMainFragment.trierHeureCroissant();
            case R.id.tri_heure_decroissante:
                mMainFragment.trierHeureDecroissant();
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
            ListeLieux = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_ROOM);
            dateDebut = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_DATE_START);
/*            dateFin = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_DATE_END);
            heureDebut = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_HOUR_START);
            heureFin = data.getStringExtra(FilteringActivity.BUNDLE_FILTER_HOUR_END);*/

            if(ListeLieux!=""){
                sallesFiltre = getListeSallesFromLieuxSequence(ListeLieux);
                if(dateDebut!=null && heureDebut!=null){
                    mDateDebutFiltre= new DateHeure(dateDebut,dateFin).formatParse();
                        if(dateFin!=null && heureFin!=null){
                            roomFilterEnabled=true;
                            timeFilterEnabled=true;
                            mDateFinFiltre= new DateHeure(heureDebut,heureFin).formatParse();
                            mMainFragment.filterAvecSallesEtDateHeure(sallesFiltre,mDateDebutFiltre,mDateFinFiltre);
                        } else {
                            roomFilterEnabled=true;
                            timeFilterEnabled=true;
                            mDateFinFiltre = mMainFragment.dateMax();
                            mMainFragment.filterAvecSallesEtDateHeure(sallesFiltre,mDateDebutFiltre, mDateFinFiltre);
                        }
                } else {
                        if(dateFin!=null && heureFin!=null){
                            roomFilterEnabled=true;
                            timeFilterEnabled=true;
                            mDateDebutFiltre = mMainFragment.dateMin();
                            mMainFragment.filterAvecSallesEtDateHeure(sallesFiltre,mDateDebutFiltre, mDateFinFiltre);
                        } else {
                            roomFilterEnabled=true;
                            mMainFragment.filterAvecSalles(sallesFiltre);
                        }
                }
            } else {
//               TODO : à factoriser
                if(dateDebut!=null && heureDebut!=null){
                    mDateDebutFiltre= new DateHeure(dateDebut,dateFin).formatParse();
                    if(dateFin!=null && heureFin!=null){
                        timeFilterEnabled=true;
                        mDateFinFiltre= new DateHeure(heureDebut,heureFin).formatParse();
                        mMainFragment.filterAvecDateHeure(mDateDebutFiltre,mDateFinFiltre);
                    } else {
                        timeFilterEnabled=true;
                        mDateFinFiltre = mMainFragment.dateMax();
                        mMainFragment.filterAvecDateHeure(mDateDebutFiltre, mDateFinFiltre);
                    }
                } else {
                    if(dateFin!=null && heureFin!=null){
                        timeFilterEnabled=true;
                        mDateDebutFiltre = mMainFragment.dateMin();
                        mMainFragment.filterAvecDateHeure(mDateDebutFiltre, mDateFinFiltre);
                    } else {
                    Log.e("ERROR_FILTRE","Pas de critères de filtre reçus");
                    }
                }
            }
        }

//        rafraichirMainFragment(methodeFiltreTriParametree);
    }

    //Copie de com/companyx/mareu/controller/fragments/AddMeetingFragment.java:240

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

    private void rafraichirMainFragment(String methodeFiltreTri){
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);
        if (mMainFragment != null) {
//           fermer/remplacer fragment
//            voir getSupportFragmentManager().getFragmentFactory();

            //    TODO : évaluer besoin de new instance : conflit pour new DummyApiServiceReunions
            mMainFragment2 = mMainFragment.newInstance(ListeLieux, dateDebut, dateFin, heureDebut, heureFin);
            methodeFiltreTri="à faire";

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,mMainFragment2).commit();
        }
    }
}