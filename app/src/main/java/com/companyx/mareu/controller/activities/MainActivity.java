package com.companyx.mareu.controller.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.controller.fragments.FragmentAccueil;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.databinding.ActivityMainBinding;
import com.companyx.mareu.model.Reunion;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private MainFragment mMainFragment;
    private FragmentAccueil mFragmentAccueil;
    private AddMeetingFragment mAddMeetingFragment;
    private FilteringFragment mFilteringFragment;

    private String listeLieux, dateDebut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.toolbar);

        configureAndDisplayMainFragment();

        configureAndDisplayFragmentAccueil();

        mBinding.addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    configureAndDisplayFragmentAddMeeting();
                } else {
                    AddMeetingActivity.navigateToAddMeetingActivity(MainActivity.this, Utils.NEW_MEETING_ACTIVITY_CODE);
                }
            }
        });

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
                //Fragment AddMeetingFragment pour tablette sw600dp en mode paysage
                if (findViewById(R.id.frame_layout_other) != null) {
                    configureAndDisplayFragmentFiltering();
                } else {
                FilteringActivity.navigateToFilteringActivity(MainActivity.this, Utils.FILTER_ACTIVITY_CODE, Utils.BUNDLE_FILTER_REUNIONS, mMainFragment.mDates);
                }
                break;

            case R.id.sansFiltre:
                mMainFragment.noFilter();
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
        if (requestCode == Utils.FILTER_ACTIVITY_CODE && resultCode == RESULT_OK) {
            listeLieux = data.getStringExtra(Utils.BUNDLE_FILTER_ROOM);
            dateDebut = data.getStringExtra(Utils.BUNDLE_FILTER_DATE_START);
            mMainFragment.filterDisplayListe(listeLieux, dateDebut);
        }
    }

// --------------
    // FRAGMENTS
    // --------------

    private void configureAndDisplayMainFragment() {
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, mMainFragment, Utils.TAG_FRAGMENT_MAIN)
                    .commit();
        }
    }

    public void configureAndDisplayFragmentAccueil() {
        if (findViewById(R.id.frame_layout_other) != null) {
            mFragmentAccueil = (FragmentAccueil) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACCUEIL);

            //Fragment accueil pour tablette sw600dp en mode paysage
            if (mFragmentAccueil == null) {
                mFragmentAccueil = new FragmentAccueil();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_other, mFragmentAccueil, Utils.TAG_FRAGMENT_ACCUEIL)
                        .commit();
            }
        }
    }

    private void configureAndDisplayFragmentAddMeeting() {
        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_other, mAddMeetingFragment, Utils.TAG_FRAGMENT_MEETING)
                    .commit();
        }
    }

    private void configureAndDisplayFragmentFiltering(){
//        if (findViewById(R.id.frame_layout_other) != null) {
            mFilteringFragment = (FilteringFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_FILTERING);

            if (mFilteringFragment == null) {
                mFilteringFragment = new FilteringFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_other, mFilteringFragment, Utils.TAG_FRAGMENT_FILTERING)
                        .commit();

                mFilteringFragment.setmDates(mMainFragment.mDates);
            }
//        }
    }

}