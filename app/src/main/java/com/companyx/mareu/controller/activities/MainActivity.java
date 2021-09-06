package com.companyx.mareu.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.companyx.mareu.AllFragmentFactory;
import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.BaseFragment;
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.controller.fragments.FragmentAccueil;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.databinding.ActivityMainBinding;
import com.companyx.mareu.model.Reunion;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity  implements BaseFragment.FragmentActionListener{

    private ActivityMainBinding mBinding;

    private FragmentManager myFM = getSupportFragmentManager();

    private MainFragment mMainFragment;
    private FragmentAccueil mFragmentAccueil;
    private AddMeetingFragment mAddMeetingFragment;
    private FilteringFragment mFilteringFragment;

    private String[] mDates;

    private final static String BUNDLE_DATES_KEY = "BUNDLE_DATES_KEY";

    private final static String BUNDLE_STATUS_KEY = "BUNDLE_STATUS_KEY";
    private final static String BUNDLE_OTHER_STATUS_KEY = "BUNDLE_OTHER_STATUS_KEY";

//    private FragmentTransaction mActivityFragmentTransaction;

    private AllFragmentFactory.FragmentType mStepStatus;

    private String listeLieux, dateDebut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Default status
        //TODO : revoir utilisation fragm vs activ
        this.mStepStatus = AllFragmentFactory.FragmentType.FragmentAccueil;

        //TODO : revoir priorités
        //Bundle envoyé par Filtering ou sauvegardé par Main
        try{
            if(savedInstanceState.getSerializable(BUNDLE_OTHER_STATUS_KEY)!=null){
                this.mStepStatus = (AllFragmentFactory.FragmentType) savedInstanceState.getSerializable(BUNDLE_STATUS_KEY);

            Log.d("Track MainActivity",BUNDLE_OTHER_STATUS_KEY);
            } else
                if(savedInstanceState.getSerializable(BUNDLE_STATUS_KEY)!=null){
                    this.mStepStatus = (AllFragmentFactory.FragmentType) savedInstanceState.getSerializable(BUNDLE_STATUS_KEY);

                Log.d("Track MainActivity",BUNDLE_STATUS_KEY);
                }

        } catch (NullPointerException e) {
            Log.e("Track MainActivity","BUNDLE_STATUS_KEY exception : "+e);
        }

        try {
            if (savedInstanceState.getStringArray(BUNDLE_DATES_KEY) != null && this.mDates == null) {

                this.mDates = savedInstanceState.getStringArray(BUNDLE_DATES_KEY);

                Log.d("Track MainActivity", BUNDLE_DATES_KEY);
            }
        } catch (NullPointerException e2) {
            Log.e("Track MainActivity","BUNDLE_DATES_KEY exception : "+e2);
        }

        configureActivityMainSettings();

        Log.d("Track MainActivity","onCreate with Task" + getTaskId());
    }

    private void configureActivityMainSettings(){
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.toolbar); // Menu

//        configureAndDisplayMainFragment();

        //Bouton ajout
        mBinding.addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    mStepStatus = AllFragmentFactory.FragmentType.AddMeetingFragment;

                    configureAndDisplayOtherFragment(mStepStatus);
                } else {
                    AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, Utils.NEW_MEETING_ACTIVITY_CODE);
                }
            }
        });
//        Log.d("Track MainActivity","configureActivityMainSettings");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Priorité à affichage d'une activité avec données déjà saisies et  stockées : AddMeeting puis Filtering si possible
        if(updateStatusForStoredData()){
            navigateToOtherActivity();

            Log.d("Track MainActivity","redirection onStart : "+mStepStatus.toString());
        } else {
            configureAndDisplayMainFragment();

            this.mDates = mMainFragment.getListDates();

            if(findViewById(R.id.frame_layout_other) != null){
                //For tablet : other Fragment
                configureAndDisplayOtherFragment(mStepStatus);
            }

            Log.d("Track MainActivity","onStart");
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull @NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.getStringArray(BUNDLE_DATES_KEY)!=null && this.mDates == null){

            this.mDates = savedInstanceState.getStringArray(BUNDLE_DATES_KEY);

            Log.d("Track MainActivity",BUNDLE_DATES_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        configureAndDisplayMainFragment();

        Log.d("Track MainActivity","onResume");

    }

   /* @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Layout adapté
        configureActivityMainSettings();
        //Vers activité ou fragment adapté
//        onResume();
//        Log.d("Track MainActivity","onConfigurationChanged");
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Track MainActivity","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Track MainActivity","destroy");
        //TODO : destroy quand filtering changement orientation puis save/close?!
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_STATUS_KEY, this.mStepStatus);
        outState.putStringArray(BUNDLE_DATES_KEY, this.mDates);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
//        Log.d("Track MainActivity","onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrerSalleHeure:
                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    this.mStepStatus = AllFragmentFactory.FragmentType.FilteringFragment;
                    configureAndDisplayOtherFragment(mStepStatus);
                } else {
                    this.mStepStatus = AllFragmentFactory.FragmentType.FilteringFragment;
                    FilteringActivity.navigateToFilteringActivity(MainActivity.this,
                            Utils.FILTER_ACTIVITY_CODE, Utils.BUNDLE_FILTER_REUNIONS,
                            mMainFragment.getListDates());
                }
                break;

            case R.id.sansFiltre:
                mMainFragment.noFilter();

                //For tablet
                this.mStepStatus = AllFragmentFactory.FragmentType.FragmentAccueil;
                configureAndDisplayOtherFragment(mStepStatus);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.NEW_MEETING_ACTIVITY_CODE && resultCode == RESULT_OK) {
            Reunion reunion = (Reunion) data.getSerializableExtra(Utils.BUNDLE_EXTRA_MEETING);
            mMainFragment.addNewMeeting(reunion);
        }
        if (requestCode == Utils.FILTER_ACTIVITY_CODE){
            if(resultCode == RESULT_FIRST_USER) {
                this.mStepStatus = AllFragmentFactory.FragmentType.FilteringFragment;
//                configureAndDisplayOtherFragment(mStepStatus);
            } else {
                    if(resultCode == RESULT_OK) {
                    listeLieux = data.getStringExtra(Utils.BUNDLE_FILTER_ROOM);
                    dateDebut = data.getStringExtra(Utils.BUNDLE_FILTER_DATE_START);
                    mMainFragment.filterWithSelection(listeLieux, dateDebut);
                    }
                    this.mStepStatus = AllFragmentFactory.FragmentType.FragmentAccueil;
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        //To manage the system handling of configuration change for fragments causing the recreation of other fragment when land -> portrait

        String[] notVisibleFragmentTagList = new String[]{Utils.TAG_FRAGMENT_FILTERING,Utils.TAG_FRAGMENT_MEETING,Utils.TAG_FRAGMENT_ACCUEIL};

        for(String tag: notVisibleFragmentTagList) {
            BaseFragment notVisibleFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);

            if (findViewById(R.id.frame_layout_other) == null && notVisibleFragment != null){ //&& !noDetailFragment.isVisible()
//                notVisibleFragment.setMenuVisibility(false);
                myFM.beginTransaction().remove(notVisibleFragment).commit();
                //                mActivityFragmentTransaction.detach(mFilteringFragment).commit();
                //                mActivityFragmentTransaction.hide(mFilteringFragment).commit();

                Log.d("Track MainActivity", "updateFragmentDesign : notVisibleFragment(s) removed");
            }
        }
    }


    // --------------
    // NAVIGATION
    // --------------

    //TODO : à supprimer
    public static void navigateToMainActivity(Context context,AllFragmentFactory.FragmentType type) {
        Intent intent = new Intent(context, MainActivity.class);
        // the (parent) MainActivity is brought to the top of the stack, and its state is preserved
//        https://developer.android.com/guide/topics/manifest/activity-element.html#lmode
//        https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_SINGLE_TOP
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_OTHER_STATUS_KEY,type);
        ActivityCompat.startActivity(context, intent, bundle);
    }

    //TODO : to be updated
    @Override
    public void navigateToOtherActivity() {
        if( this.mStepStatus == AllFragmentFactory.FragmentType.AddMeetingFragment){
            AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, Utils.NEW_MEETING_ACTIVITY_CODE);
        }
        else if(this.mStepStatus == AllFragmentFactory.FragmentType.FilteringFragment){
            FilteringActivity.navigateToFilteringActivity(MainActivity.this,
                    Utils.FILTER_ACTIVITY_CODE, Utils.BUNDLE_FILTER_REUNIONS,
                    mDates);
        }
    }

    // --------------
    // FRAGMENTS
    // --------------

//    TODO : individualiser et remplacer factory

/*    private void configureAndDisplayFragment(int layout, AllFragmentFactory.AppFragment mFragment, AllFragmentFactory.FragmentType type, String tag) {

            mFragment = (AllFragmentFactory.AppFragment) getSupportFragmentManager().findFragmentByTag(tag);

        if (mFragment == null) {
            mFragment = AllFragmentFactory.makeFragment(type);
            getSupportFragmentManager().beginTransaction()
                    .add(layout,(Fragment) mFragment, tag)
                    .commit();
        }
    }*/

    private void configureAndDisplayMainFragment() {
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MAIN);

        if (mMainFragment == null) {

            mMainFragment = new MainFragment();

            myFM.beginTransaction()
                    .add(R.id.frame_layout_main, mMainFragment, Utils.TAG_FRAGMENT_MAIN)
                    .commit();

        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main)!=null) {

            if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main).isVisible()){
                Log.d("Track MainActivity","MainFragment "+
                        myFM.findFragmentById(R.id.frame_layout_main).getTag()+" isVisible");
            } else
                if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main).isHidden()){
                    Log.d("Track MainActivity","MainFragment "+
                            myFM.findFragmentById(R.id.frame_layout_main).getTag()+" isHidden");
            } else
                if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main).isDetached()){
                Log.d("Track MainActivity","MainFragment "+
                        myFM.findFragmentById(R.id.frame_layout_main).getTag()+" isDetached");
            } else
                if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main).isAdded()){
                Log.d("Track MainActivity","MainFragment byId, Tag "+
                        myFM.findFragmentById(R.id.frame_layout_main).getTag()+" isAdded");
            }

            if(getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MAIN).isAdded()){
                Log.d("Track MainActivity","MainFragment byTag, Id "+
                        myFM.findFragmentById(R.id.frame_layout_main).getId()+" isAdded");
            }

               /* getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_main, mMainFragment, Utils.TAG_FRAGMENT_MAIN)
                        .commit();*/

            /*myFM.beginTransaction().remove(mMainFragment).commit();

            MainFragment mainFragment = new MainFragment();
            myFM.beginTransaction()
                    .add(R.id.frame_layout_main, mainFragment, Utils.TAG_FRAGMENT_MAIN)
                    .commit();*/
                }

//        Log.d("Track MainActivity","configureAndDisplayMainFragment"); 09-05 16:51:36.480 ; 09-05 16:52:00.453
    }

    private void configureAndDisplayOtherFragment(AllFragmentFactory.FragmentType mType){
        //TODO : à revoir
        //Fragment pour tablette sw600dp en mode paysage
        if (findViewById(R.id.frame_layout_other) != null) {
            switch (mType) {
                case FragmentAccueil:
                    configureAndDisplayFragmentAccueil();
                    break;
                case AddMeetingFragment:
                    configureAndDisplayAddMeetingFragment();
                    break;
                case FilteringFragment:
                    configureAndDisplayFilteringFragment();
                    break;
                /*default:
                    configureAndDisplayFragmentAccueil();*/ //TODO : revoir
            }
        }
    }

    //TODO : add button invisible pour FilteringFragment

    private void configureAndDisplayFragmentAccueil(){
            mFragmentAccueil = (FragmentAccueil) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACCUEIL);

            if (mFragmentAccueil == null) {
                if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_other)==null){
                    mFragmentAccueil = new FragmentAccueil();
                        myFM.beginTransaction()
                            .add(R.id.frame_layout_other, mFragmentAccueil, Utils.TAG_FRAGMENT_ACCUEIL)
                            .commit();
                } else {
                        myFM.beginTransaction()
                            .replace(R.id.frame_layout_other, new FragmentAccueil())
                            .commit();
                }
            }
    }

    @Override
    public void onClickCloseMenu() {
        //Empty
    }

    @Override
    public void ManageOtherFragment() {
        configureAndDisplayFragmentAccueil();
    }

    private boolean updateStatusForStoredData(){
        boolean toOtherActivity = false;

        if (getSharedPreferences(getString(R.string.addmeeting_preference_file_key), Context.MODE_PRIVATE)!=null){
            //Data stored in AddMeetingFragment
            SharedPreferences AddMeetingSharedPref = getSharedPreferences(getString(R.string.addmeeting_preference_file_key), Context.MODE_PRIVATE);

            if (AddMeetingSharedPref.contains(getString(R.string.addmeeting_sujet_key))) {
                this.mStepStatus = AllFragmentFactory.FragmentType.AddMeetingFragment;
                toOtherActivity = true;

                Toast.makeText(this,"Saisie en cours pour ajout de réunion",Toast.LENGTH_SHORT).show();
            } else if(getSharedPreferences(getString(R.string.filtering_preference_file_key), Context.MODE_PRIVATE)!=null) {
                    //Data stored in FilteringFragment
                    SharedPreferences FilteringSharedPref = getSharedPreferences(getString(R.string.filtering_preference_file_key), Context.MODE_PRIVATE);

                        if ((FilteringSharedPref.contains(getString(R.string.filering_dateDebut_key))
        //                        &&FilteringSharedPref.getString(getString(R.string.filering_dateDebut_key), null)!=""
                        )
                                || FilteringSharedPref.contains(getString(R.string.filering_room_key))) {
                            this.mStepStatus = AllFragmentFactory.FragmentType.FilteringFragment;
                            toOtherActivity = true;

                            Toast.makeText(this,"Saisie en cours pour filtre des réunions",Toast.LENGTH_SHORT).show();
                        }
                    }
        }

        return toOtherActivity;
    }

    private void configureAndDisplayFilteringFragment(){

        mFilteringFragment = (FilteringFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_FILTERING);

            if (mFilteringFragment == null) {
                mFilteringFragment = FilteringFragment.newInstance(mDates);
                myFM.beginTransaction()
                        .replace(R.id.frame_layout_other, mFilteringFragment, Utils.TAG_FRAGMENT_FILTERING)
                        .commit();
            }

            if(mFilteringFragment.isVisible()){
                Log.d("Track MainActivity","configureAndDisplayFilteringFragment isVisible");
            }
    }

    private void configureAndDisplayAddMeetingFragment(){

        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            myFM.beginTransaction()
                    .replace(R.id.frame_layout_other, mAddMeetingFragment, Utils.TAG_FRAGMENT_MEETING)
                    .commit();
        }
    }
}