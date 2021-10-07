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
import com.companyx.mareu.controller.fragments.BaseFragment;
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.databinding.ActivityFilteringBinding;

public class FilteringActivity extends AppCompatActivity implements BaseFragment.FragmentActionListener{

    private ActivityFilteringBinding mBinding;

    private FilteringFragment mFilteringFragment;

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFilteringBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.filterActionToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        //Set action bar arrow
        ab.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        //ContentDescription : "Navigate up" par défaut
        ab.setHomeActionContentDescription("Revenir à la liste des réunions");

        //Capter l'intent à l'origine du lancement de l'activité
        data = this.getIntent();
        configureAndDisplayFilteringFragment(data.getStringArrayExtra(Utils.BUNDLE_FILTER_DATES));

        //Recevoir bundle avec date et réunion en provenance du fragment Filtering
        getSupportFragmentManager().setFragmentResultListener(Utils.FILTERING_ACTIVITY_FRAGMENT_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                saveActivityResultAndThenClose(bundle);
            }
        });

        Log.d("Track FilteringActivity","onCreate with Task" + getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        int size = (int) (getResources().getDimension(R.dimen.tablet_size)/getResources().getDisplayMetrics().density);

        // Mode landscape tablette : bascule de FilteringActivity à MainActivity avec FilteringFragment
        if(getResources().getConfiguration().screenWidthDp >= size &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d("Track FilteringActivity","onStart");

            NavigateToOtherActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Track FilteringActivity","onDestroy");
    }

    // --------------
    // NAVIGATION
    // --------------

    public static void navigateToFilteringActivity(Activity activity, int RequestCode, String name, String[] value) {
        Intent intent = new Intent(activity, FilteringActivity.class);
        intent.putExtra(name, value);
        ActivityCompat.startActivityForResult(activity, intent, RequestCode, null);
    }

    @Override
    public void NavigateToOtherActivity() {
        setResult(RESULT_FIRST_USER,null);
        finish();

        Log.d("Track FilteringActivity","back?");
    }

    // --------------
    // ACTIONS
    // --------------

    //Envoyer résultat succès à activité appelante
    private void saveActivityResultAndThenClose(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClickCloseMenu() {
        //Envoyer résultat échec à activité appelante
        setResult(RESULT_CANCELED, null);
        finish();
        
    }

    @Override
    public void ManageOtherFragment() {

    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndDisplayFilteringFragment(String[] mDates) {
        //  Appel au SupportFragmentManager pour trouver une fragment exostant dans le conteneur FrameLayout
        mFilteringFragment = (FilteringFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACTIVITY_FILTERING);

        if (mFilteringFragment == null) {
            mFilteringFragment = FilteringFragment.newInstance(mDates);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_filtering, mFilteringFragment, Utils.TAG_FRAGMENT_ACTIVITY_FILTERING)
                    .commit();
        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_filtering)==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_filtering, mFilteringFragment, Utils.TAG_FRAGMENT_ACTIVITY_FILTERING)
                    .commit();
        } else if(getSupportFragmentManager().findFragmentById(R.id.frame_layout_filtering)!=null){
            Log.d("Track FilteringActivity","mFilteringFragment not null already in frame_layout_filtering" + mFilteringFragment.getId());
        }

    }
}