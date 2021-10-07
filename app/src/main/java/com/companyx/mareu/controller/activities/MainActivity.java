package com.companyx.mareu.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    private FragmentManager myFragmentManager = getSupportFragmentManager();

    private MainFragment mMainFragment;
    private FragmentAccueil mFragmentAccueil;
    private AddMeetingFragment mAddMeetingFragment;
    private FilteringFragment mFilteringFragment;

    private String listeLieux, dateDebut;

    private otherFragmentType mStepStatus;

    private enum otherFragmentType{
        FragmentAccueil,
        AddMeetingFragment,
        FilteringFragment
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle pour stocker le statut
        try{
            if(savedInstanceState.getSerializable(Utils.BUNDLE_ACTIVITY_STATUS_KEY)!=null){
                this.mStepStatus = (otherFragmentType) savedInstanceState.getSerializable(Utils.BUNDLE_ACTIVITY_STATUS_KEY);
            }

        } catch (NullPointerException e) {
            Log.e("Track MainActivity","BUNDLE_STATUS_KEY exception : "+e);
        }

        //Default status
        if(this.mStepStatus == null){
            this.mStepStatus = otherFragmentType.FragmentAccueil;
        }

        configureActivityMainSettings();

        Log.d("Track MainActivity","onCreate with Task" + getTaskId());
    }

    private void configureActivityMainSettings(){
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.toolbar); // Menu

        //Bouton ajout
        mBinding.addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepStatus = otherFragmentType.AddMeetingFragment;

                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    configureAndDisplayOtherFragment();
                } else {
                    AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, Utils.NEW_MEETING_ACTIVITY_CODE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        configureAndDisplayMainFragment();

        //Mode paysage
        if(findViewById(R.id.frame_layout_other) != null){
            configureAndDisplayOtherFragment();

            Log.d("Track MainActivity","onStart Landscape");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Mode portrait : bascule vers activité correspondant à status enregistré
        if(findViewById(R.id.frame_layout_other) == null) {
            if (this.mStepStatus != otherFragmentType.FragmentAccueil) {
                //Mode portrait : bascule vers activité correspondant à status enregistré
                NavigateToOtherActivity();

                Log.d("Track MainActivity", "redirection onResume : " + mStepStatus.toString());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Track MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Track MainActivity","onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Utils.BUNDLE_ACTIVITY_STATUS_KEY, this.mStepStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrerSalleHeure:
                this.mStepStatus = otherFragmentType.FilteringFragment;

                if (findViewById(R.id.frame_layout_other) == null) {
                    FilteringActivity.navigateToFilteringActivity(MainActivity.this,
                            Utils.FILTER_ACTIVITY_CODE, Utils.BUNDLE_FILTER_DATES,
                            mMainFragment.getmDates());
                } else {
                    //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                    configureAndDisplayOtherFragment();
                }
                break;

            case R.id.sansFiltre:
                mMainFragment.noFilter();

                //Pour tablette
                this.mStepStatus = otherFragmentType.FragmentAccueil;
                configureAndDisplayOtherFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.NEW_MEETING_ACTIVITY_CODE) {
            if (resultCode == RESULT_FIRST_USER) {
                this.mStepStatus = otherFragmentType.AddMeetingFragment;
            } else {
                if (resultCode == RESULT_OK) {
                    Reunion reunion = (Reunion) data.getSerializableExtra(Utils.BUNDLE_EXTRA_MEETING);
                    mMainFragment.addNewMeeting(reunion);
                }
                this.mStepStatus = otherFragmentType.FragmentAccueil;
            }
        }
        if (requestCode == Utils.FILTER_ACTIVITY_CODE){
            if(resultCode == RESULT_FIRST_USER) {
                this.mStepStatus = otherFragmentType.FilteringFragment;
            } else {
                    if(resultCode == RESULT_OK) {
                    /*listeLieux = data.getStringExtra(Utils.BUNDLE_FILTER_ROOM);
                    dateDebut = data.getStringExtra(Utils.BUNDLE_FILTER_DATE_START);

                    mMainFragment.filterWOSelectionAndDisplay(listeLieux, dateDebut);*/

                    mMainFragment.filterWithDefinedSpecs(data.getExtras());

                    }
                    this.mStepStatus = otherFragmentType.FragmentAccueil;
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        //To manage the system handling of configuration change for fragments causing the recreation of other fragment upon orientation change (landscape <> portrait)

        String[] notVisibleFragmentTagList = new String[]{Utils.TAG_FRAGMENT_FILTERING,Utils.TAG_FRAGMENT_MEETING,Utils.TAG_FRAGMENT_ACCUEIL};

        for(String tag: notVisibleFragmentTagList) {
            BaseFragment notVisibleFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);

            if (findViewById(R.id.frame_layout_other) == null && notVisibleFragment != null){
//              notVisibleFragment.setMenuVisibility(false)
                myFragmentManager.beginTransaction().remove(notVisibleFragment).commit();

                Log.d("Track MainActivity", "onResumeFragments : notVisibleFragment "+notVisibleFragment.getClass().toString()+" removed");
            }
        }


    }


    // --------------
    // NAVIGATION
    // --------------

    @Override
    public void NavigateToOtherActivity() {
        if( this.mStepStatus == otherFragmentType.AddMeetingFragment){
            AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, Utils.NEW_MEETING_ACTIVITY_CODE);
            Toast.makeText(this, "Action en cours pour ajout de réunion", Toast.LENGTH_SHORT).show();
        }
        else if(this.mStepStatus == otherFragmentType.FilteringFragment){
            FilteringActivity.navigateToFilteringActivity(MainActivity.this,
                    Utils.FILTER_ACTIVITY_CODE, Utils.BUNDLE_FILTER_DATES,
                    mMainFragment.getmDates());
            Toast.makeText(this, "Action en cours pour filtre des réunions", Toast.LENGTH_SHORT).show();
        }
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndDisplayMainFragment() {
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MAIN);

        if (mMainFragment == null) {

            mMainFragment = new MainFragment();

            myFragmentManager.beginTransaction()
                    .add(R.id.frame_layout_main, mMainFragment, Utils.TAG_FRAGMENT_MAIN)
                    .commit();

        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_main)!=null) {
            Log.d("Track MainActivity","MainFragment "+
                    myFragmentManager.findFragmentById(R.id.frame_layout_main).getTag()+" already existed");
            }
    }

    private void configureAndDisplayOtherFragment(){
        //Fragment pour tablette sw600dp en mode paysage
        if (findViewById(R.id.frame_layout_other) != null) {
            switch (this.mStepStatus) {
                case FragmentAccueil:
                    configureAndDisplayFragmentAccueil();
                    setAddMeetingButtonVisible(true);
                    break;
                case AddMeetingFragment:
                    configureAndDisplayAddMeetingFragment();
                    setAddMeetingButtonVisible(false);
                    break;
                case FilteringFragment:
                    configureAndDisplayFilteringFragment();
                    setAddMeetingButtonVisible(false);
                    break;
                /*default:
                    configureAndDisplayFragmentAccueil();*/
            }
        }
    }

    private void setAddMeetingButtonVisible(boolean b){
        if(b){
            if(mBinding.addMeeting.getVisibility()!=View.VISIBLE)
            mBinding.addMeeting.setVisibility(View.VISIBLE);
        } else {
            mBinding.addMeeting.setVisibility(View.INVISIBLE);
        }
    }

    private void configureAndDisplayFragmentAccueil(){
            mFragmentAccueil = (FragmentAccueil) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACCUEIL);

            if (mFragmentAccueil == null) {
                if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_other)==null){
                    mFragmentAccueil = new FragmentAccueil();
                        myFragmentManager.beginTransaction()
                            .add(R.id.frame_layout_other, mFragmentAccueil, Utils.TAG_FRAGMENT_ACCUEIL)
                            .commit();
                } else {
                        myFragmentManager.beginTransaction()
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
        this.mStepStatus = otherFragmentType.FragmentAccueil;

        configureAndDisplayOtherFragment();
    }

    private void configureAndDisplayFilteringFragment(){

        mFilteringFragment = (FilteringFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_FILTERING);

            if (mFilteringFragment == null) {
                mFilteringFragment = FilteringFragment.newInstance(mMainFragment.getmDates());
                myFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_other, mFilteringFragment, Utils.TAG_FRAGMENT_FILTERING)
                        .commit();
            }
    }

    private void configureAndDisplayAddMeetingFragment(){

        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            myFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_other, mAddMeetingFragment, Utils.TAG_FRAGMENT_MEETING)
                    .commit();
        }
    }

}
