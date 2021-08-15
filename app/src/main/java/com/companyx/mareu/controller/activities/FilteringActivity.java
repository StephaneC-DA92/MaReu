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
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.databinding.ActivityFilteringBinding;

public class FilteringActivity extends AppCompatActivity {

    private ActivityFilteringBinding mBinding;

    private FilteringFragment mFilteringFragment;

/*    private ApiServiceSalles mDummyApiServiceSalles;

    private String mDateDebut;
    public String mSequenceLieux;*/

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFilteringBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.filterActionToolbar);

        configureAndDisplayFilteringFragment();

        //Capter l'intent à l'origine du lancement de l'activité
        data = getIntent();
        mFilteringFragment.mDates = data.getStringArrayExtra(Utils.BUNDLE_FILTER_REUNIONS);

  /*      String[] dates = data.getStringArrayExtra(MainActivity.BUNDLE_FILTER_REUNIONS);
        ArrayAdapter<String> adapterDates = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        adapterDates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.FiltreDateSpinner.setAdapter(adapterDates);

        mBinding.FiltreDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDateDebut = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Aucune date sélectionnée", Toast.LENGTH_LONG).show();
            }
        });

        mDummyApiServiceSalles = DI_Salles.getServiceSalles();
        String[] lieux = mDummyApiServiceSalles.getListeLieu();

        ArrayAdapter<String> adapterLieux = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lieux);
        mBinding.FiltreSalles.setAdapter(adapterLieux);
        mBinding.FiltreSalles.setThreshold(1);
        mBinding.FiltreSalles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());*/

        //Recevoir bundle avec date et réunion en provenance du fragment Filtering
        getSupportFragmentManager().setFragmentResultListener(Utils.FILTERING_ACTIVITY_FRAGMENT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                if (bundle == null) {
                    setResult(RESULT_CANCELED, null);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public static void navigateToFilteringActivity(Activity activity, int RequestCode, String name, String[] value) {
        Intent intent = new Intent(activity, FilteringActivity.class);
        intent.putExtra(name, value);
        ActivityCompat.startActivityForResult(activity, intent, RequestCode, null);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_action_check:
                saveActivityResultAndThenClose();
                return true;
            case R.id.filter_action_close:
                closeActivityWithoutSaving();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndDisplayFilteringFragment() {
        //  Appel au SupportFragmentManager pour trouver une fragment exostant dans le conteneur FrameLayout
        mFilteringFragment = (FilteringFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_FRAGMENT_ACTIVITY_FILTERING);

        if (mFilteringFragment == null) {
            mFilteringFragment = new FilteringFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_filtering, mFilteringFragment, Utils.TAG_FRAGMENT_ACTIVITY_FILTERING)
                    .commit();
        }
    }
}