package com.companyx.mareu.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentResultListener;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.BaseFragment;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.model.Reunion;

public class AddMeetingActivity extends AppCompatActivity implements BaseFragment.FragmentActionListener {

    private ActivityAddMeetingBinding mBinding;

    private AddMeetingFragment mAddMeetingFragment;

    private Reunion mNouvelleReunion = null;

    private Bundle myBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.actionToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        //Set action bar arrow
        ab.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        //ContentDescription : "Navigate up" par défaut
        ab.setHomeActionContentDescription("Revenir à la liste des réunions");

        configurerEtAfficherAddMeetingFragment();

        //Recevoir bundle avec réunion en provenance du fragment AddMeeting
        getSupportFragmentManager().setFragmentResultListener(Utils.NEW_MEETING_ACTIVITY_FRAGMENT_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                //mNouvelleReunion par construction de AddMeetingFragment

                    myBundle = bundle;
                    saveActivityResultAndThenClose();
            }
        });

        Log.d("Track AddMeetingActivit","onCreate with Task" + getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        int size = (int) (getResources().getDimension(R.dimen.tablet_size)/getResources().getDisplayMetrics().density);

        // Mode landscape tablette : bascule de AddMeetingActivity à MainActivity avec AddMeetingFragment
        if(getResources().getConfiguration().screenWidthDp >= size &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d("Track AddMeetingActivit","onStart");

            NavigateToOtherActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Track AddMeetingActivit","destroy");
    }

    // --------------
    // NAVIGATION
    // --------------

    public static void navigateToAddMeetingActivity(Activity activity, int RequestCode) {
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, RequestCode, null);
    }

    @Override
    public void NavigateToOtherActivity() {
        setResult(RESULT_FIRST_USER,null);
        finish();

        Log.d("Track AddMeetingActivit","back?");
    }

    @Override
    public void ManageOtherFragment() {

    }

    // --------------
    // ACTIONS
    // --------------

    //Envoyer résultat succès à activité appelante
    private void saveActivityResultAndThenClose() {
            Intent intent = new Intent();

            intent.putExtras(myBundle);
            setResult(RESULT_OK, intent);
            finish();
    }

    @Override
    public void onClickCloseMenu() {
        //Envoyer résultat échec à activité appelante
        setResult(RESULT_CANCELED, null);
        finish();
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configurerEtAfficherAddMeetingFragment() {
        //  Appel au SupportFragmentManager pour trouver une fragment existant dans le conteneur FrameLayout
        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACTIVITY_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout_add_meeting, mAddMeetingFragment, Utils.TAG_FRAGMENT_ACTIVITY_MEETING)
                .commit();
        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting)==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_add_meeting, mAddMeetingFragment, Utils.TAG_FRAGMENT_ACTIVITY_MEETING)
                    .commit();
        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting)!=null){
            Log.d("Track AddMeetingActivit","mAddMeetingFragment not null already in frame_layout_add_meeting" + mAddMeetingFragment.getId());
        }

    }
}