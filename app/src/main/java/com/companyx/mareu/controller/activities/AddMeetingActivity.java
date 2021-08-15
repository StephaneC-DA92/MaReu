package com.companyx.mareu.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentResultListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.model.Reunion;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;

    private AddMeetingFragment mAddMeetingFragment;

    private Reunion mNouvelleReunion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.actionToolbar);

        configurerEtAfficherAddMeetingFragment();

        //Recevoir bundle avec réunion en provenance du fragment AddMeeting
        getSupportFragmentManager().setFragmentResultListener(Utils.NEW_MEETING_ACTIVITY_FRAGMENT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                if (bundle == null) {
                    closeActivityWithoutSaving();
                } else {
                    mNouvelleReunion = (Reunion) bundle.getSerializable(Utils.BUNDLE_EXTRA_MEETING);
                    saveActivityResultAndThenClose();
                }
            }
        });
    }

    public static void navigateToAddMeetingActivity(Activity activity, int RequestCode) {
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, RequestCode, null);
    }

    // --------------
    // MENU
    // --------------

    //Envoyer résultat succès à activité appelante
    private void saveActivityResultAndThenClose() {
        if (mNouvelleReunion != null) {
            Intent intent = new Intent();
            intent.putExtra(Utils.BUNDLE_EXTRA_MEETING, mNouvelleReunion);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //Envoyer résultat échec à activité appelante
    private void closeActivityWithoutSaving() {
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
        }
    }
}