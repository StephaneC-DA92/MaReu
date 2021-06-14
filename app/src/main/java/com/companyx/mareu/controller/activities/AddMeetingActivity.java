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
import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.databinding.ActivityAddMeetingBinding;
import com.companyx.mareu.model.Reunion;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;

    private AddMeetingFragment mAddMeetingFragment;
    private static final String TAG_FRAGMENT_ACTIVITY_MEETING = "TAG_FRAGMENT_ACTIVITY_MEETING";

    public static final String BUNDLE_EXTRA_MEETING = "BUNDLE_EXTRA_MEETING";
    private static final String NEW_MEETING_ACTIVITY_FRAGMENT="NEW_MEETING_ACTIVITY_FRAGMENT";

    private Reunion mNouvelleReunion =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.actionToolbar);

        configurerEtAfficherAddMeetingFragment();

        getSupportFragmentManager().setFragmentResultListener(NEW_MEETING_ACTIVITY_FRAGMENT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                if(bundle==null){
                    closeActivityWithoutSaving();
                } else {
                    mNouvelleReunion = (Reunion) bundle.getSerializable(BUNDLE_EXTRA_MEETING);
                    saveActivityResultAndThenClose();
                }
            }
        });

        }

/*    public static void navigateToAddMeetingActivity(Activity activity){
        Intent intent = new Intent(activity,AddMeetingActivity.class);
        ActivityCompat.startActivity(activity,intent,null);
    }*/

    public static void navigateToAddMeetingActivity(Activity activity, int RequestCode){
        Intent intent = new Intent(activity,AddMeetingActivity.class);
        ActivityCompat.startActivityForResult(activity,intent, RequestCode,null);
    }

   /* @Override
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
    }*/

    // --------------
    // MENU
    // --------------

    private void saveActivityResultAndThenClose(){
        if(mNouvelleReunion !=null) {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_EXTRA_MEETING, mNouvelleReunion);
            setResult(RESULT_OK, intent);
            finish();
        }
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
        mAddMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_ACTIVITY_MEETING);

        if (mAddMeetingFragment == null) {
            mAddMeetingFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_add_meeting, mAddMeetingFragment,TAG_FRAGMENT_ACTIVITY_MEETING)
                    .commit();
        }
    }
}