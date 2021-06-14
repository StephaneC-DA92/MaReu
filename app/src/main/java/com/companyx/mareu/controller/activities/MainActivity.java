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
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.FragmentAccueil;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.data.DummyApiServiceSalles;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.databinding.ActivityMainBinding;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private MainFragment mMainFragment;
    private FragmentAccueil mFragmentAccueil;
    private AddMeetingFragment mAddMeetingFragment;

    private static final String TAG_FRAGMENT_MAIN = "TAG_FRAGMENT_MAIN";
    private static final String TAG_FRAGMENT_MEETING = "TAG_FRAGMENT_MEETING";
    private static final String TAG_FRAGMENT_ACCUEIL = "TAG_FRAGMENT_ACCUEIL";

    public static final int NEW_MEETING_ACTIVITY_CODE = 98;

    public static final int FILTER_ACTIVITY_CODE = 90;

    public static final String BUNDLE_FILTER_REUNIONS = "BUNDLE_FILTER_REUNIONS";
    private String listeLieux, dateDebut ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.toolbar);

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        Log.d("ON_CREATE_ACTIVITY","MainActivity");

        configurerEtAfficherMainFragment();

        configurerEtAfficherFragmentAccueil();

        mBinding.addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    configurerEtAfficherFragmentAddMeeting();
                } else {
//                    AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this);
                    //TODO : start Activity for result : resultat AddMeeting, nouvelle réunion
                    AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, NEW_MEETING_ACTIVITY_CODE);
                }
            }
        });

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
                FilteringActivity.navigateToFilteringActivity(MainActivity.this, FILTER_ACTIVITY_CODE, BUNDLE_FILTER_REUNIONS, mMainFragment.mDates);
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
            mMainFragment.filtrerAffichageListe(listeLieux,dateDebut);
        }
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
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, mMainFragment,TAG_FRAGMENT_MAIN)
                    .commit();
        }
    }

    public void configurerEtAfficherFragmentAccueil() {
        if (findViewById(R.id.frame_layout_other) != null) {
            mFragmentAccueil = (FragmentAccueil) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_ACCUEIL);

            //Fragment accueil pour tablette sw600dp en mode paysage
            if (mFragmentAccueil == null) {
                mFragmentAccueil = new FragmentAccueil();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_other, mFragmentAccueil, TAG_FRAGMENT_ACCUEIL)
                        .commit();
            }
        }
    }

    private void configurerEtAfficherFragmentAddMeeting(){
        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_other, mAddMeetingFragment,TAG_FRAGMENT_MEETING)
                    .commit();
        }
    }

}