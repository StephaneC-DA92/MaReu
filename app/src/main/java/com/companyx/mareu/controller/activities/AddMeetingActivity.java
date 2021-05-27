package com.companyx.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.MainFragment;
import com.companyx.mareu.data.DummyApiServiceReunions;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.model.Reunion;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;

    private AddMeetingFragment mAddMeetingFragment;

    public static final String BUNDLE_EXTRA_MEETING = "BUNDLE_EXTRA_MEETING";

    private Reunion mReunion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.actionToolbar);

        configurerEtAfficherAddMeetingFragment();

    }

    public static void navigateToAddMeetingActivity(Activity activity, int RequestCode){
        Intent intent = new Intent(activity,AddMeetingActivity.class);
        ActivityCompat.startActivityForResult(activity,intent, RequestCode,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_room, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                saveActivityResultAndThenClose();
                return true;
            case R.id.action_close:
                closeActivityWithoutSaving();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // --------------
    // MENU
    // --------------

//    arrayadapter pour salle

    private void saveActivityResultAndThenClose(){
        Reunion reunion = mAddMeetingFragment.createReunion();

//        DONE : seriazable à faire pour réunion KO : essayer parcelable
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRA_MEETING,reunion);
        setResult(RESULT_OK,intent);
        finish();
            };

    private void closeActivityWithoutSaving(){
        setResult(RESULT_CANCELED,null);
        finish();
    };


    // --------------
    // FRAGMENTS
    // --------------

    private void configurerEtAfficherAddMeetingFragment(){
        //  Appel au SupportFragmentManager pour trouver une fragment exostant dans le conteneur FrameLayout
        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_add_meeting, mAddMeetingFragment)
                    .commit();
        }
    }
}