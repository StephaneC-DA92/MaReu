package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.databinding.FragmentFilteringBinding;
import com.companyx.mareu.di.DI_Salles;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilteringFragment extends BaseFragment{

    private FragmentFilteringBinding mBinding;
//    private View mView;

    private SharedPreferences FilteringSharedPref;
    private SharedPreferences.Editor FilteringEditor;

    private ApiServiceSalles mDummyApiServiceSalles;

    private String mDateDebut;
    public String mSequenceLieux;

    private static final String ARG_DATES = "Dates";
    public String[] mDates;
    public static final String BUNDLE_DATES_KEY="BUNDLE_DATES_KEY";

    public FilteringFragment() {
        // Required empty public constructor
    }

    // --------------
    // INITIALIZATION
    // --------------

//    TODO : modifier avec dates
    public static FilteringFragment newInstance(String[] mDates) {
        FilteringFragment fragment = new FilteringFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_DATES, mDates);
        fragment.setArguments(args);
        return fragment;
    }

    // --------------
    // IMPLEMENTATION
    // --------------

    @Override
    protected void setCallback() {
        try{
            this.callback = (FragmentActionListener) getActivity();
        }catch(ClassCastException e){
            Log.d("Track FilteringFragment","No Listener found");
        }
    }

    @Override
    protected void configureFragmentSettings(Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mDates = getArguments().getStringArray(ARG_DATES);
        } else if (savedInstanceState != null){
            mDates = savedInstanceState.getStringArray(BUNDLE_DATES_KEY);
        }

        //Check pref existant?
        FilteringSharedPref = requireActivity().getSharedPreferences(getString(R.string.filtering_preference_file_key),Context.MODE_PRIVATE);
        FilteringEditor = FilteringSharedPref.edit();
    }

    @Override
    protected void configureFragmentDesign(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentFilteringBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();

        Context context = mView.getContext();

        ArrayAdapter<String> adapterDates = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mDates);
        adapterDates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.FiltreDateSpinner.setAdapter(adapterDates);

        if(FilteringSharedPref.contains(getString(R.string.filering_dateDebut_key))
//                &&FilteringSharedPref.getString(getString(R.string.filering_dateDebut_key),null)!=null
        ){mDateDebut = FilteringSharedPref.getString(getString(R.string.filering_dateDebut_key), null);
            for(int i = 0; i<mDates.length;i++){
                if(mDates[i].equals(mDateDebut)){
                    mBinding.FiltreDateSpinner.setSelection(i,true);
                    break;
                }
            }
        } else {
            mBinding.FiltreDateSpinner.setSelection(0,true);
        }

        mBinding.FiltreDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO : pas selection initiale à 0
                if (position!=0) {
                    mDateDebut = parent.getItemAtPosition(position).toString();

                    FilteringEditor.putString(getString(R.string.filering_dateDebut_key), mDateDebut).apply();

                    Toast.makeText(parent.getContext(), "Date sélectionnée : " + mDateDebut, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Aucune date sélectionnée", Toast.LENGTH_LONG).show();
            }
        });

        mDummyApiServiceSalles = DI_Salles.getServiceSalles();
        String[] lieux = mDummyApiServiceSalles.getPlaceList();

        ArrayAdapter<String> adapterLieux = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lieux);
        mBinding.FiltreSalles.setAdapter(adapterLieux);
        mBinding.FiltreSalles.setThreshold(1);
        mBinding.FiltreSalles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        if(FilteringSharedPref.getString(getString(R.string.filering_room_key), null)!=null){

            mSequenceLieux = FilteringSharedPref.getString(getString(R.string.filering_room_key), null);

            mBinding.FiltreSalles.setText(mSequenceLieux.subSequence(0,mSequenceLieux.length()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mBinding.FiltreSalles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSequenceLieux = mBinding.FiltreSalles.getText().toString();

                FilteringEditor.putString(getString(R.string.filering_room_key), mSequenceLieux).apply();
            }
        });

        //not Visible on resume in f activity!
        /*if(this.isVisible()){
            Log.d("Track FilteringFragment","isVisible on Resume");
        }*/
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_filter_actions, menu);

//        Log.d("Track FilteringFragment","onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_action_check:
                saveResultAndThenClose();
                return true;
            case R.id.filter_action_close:
                closeWithoutResult(R.string.filtering_preference_file_key,FilteringEditor);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(BUNDLE_DATES_KEY,mDates);
    }

    // --------------
    // PRIVATE METHODS
    // --------------

    @Override
    protected  void saveResultAndThenClose() {
        if (mSequenceLieux!=null || mDateDebut!=null) {
            Bundle resultat = new Bundle();
            if (mSequenceLieux!=null &&!mSequenceLieux.equals("")) {
                resultat.putString(Utils.BUNDLE_FILTER_ROOM, mSequenceLieux);
            }
            if (mDateDebut!=null&&!mDateDebut.equals("")) {
                resultat.putString(Utils.BUNDLE_FILTER_DATE_START, mDateDebut);
            }
            sendResultToFragmentManager(resultat, Utils.FILTERING_ACTIVITY_FRAGMENT, Utils.FILTERING_INTER_FRAGMENTS);

            FilteringSharedPref.edit().clear().commit(); // vider les préférences

        } else {
            Toast.makeText(mView.getContext(), "Veuillez saisir des critères", Toast.LENGTH_SHORT).show();
        }
    }

}