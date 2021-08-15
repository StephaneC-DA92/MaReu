package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.companyx.mareu.controller.activities.FilteringActivity;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.databinding.FragmentFilteringBinding;
import com.companyx.mareu.di.DI_Salles;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilteringFragment extends Fragment {

    private FragmentFilteringBinding mBinding;
    private View mView;

    private ApiServiceSalles mDummyApiServiceSalles;

    private String mDateDebut;
    public String mSequenceLieux;

    public String[] mDates;

    //Setter
    public void setmDates(String[] mDates) {
        this.mDates = mDates;
    }

    public FilteringFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFilteringBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
        Context context = mView.getContext();

        //Capter l'intent à l'origine du lancement de l'activité

        ArrayAdapter<String> adapterDates = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mDates);
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
        String[] lieux = mDummyApiServiceSalles.getPlaceList();

        ArrayAdapter<String> adapterLieux = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lieux);
        mBinding.FiltreSalles.setAdapter(adapterLieux);
        mBinding.FiltreSalles.setThreshold(1);
        mBinding.FiltreSalles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_filter_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_action_check:
                saveResultAndThenClose();
                return true;
            case R.id.filter_action_close:
                closeWithoutSaving();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveResultAndThenClose() {
        mSequenceLieux = mBinding.FiltreSalles.getText().toString();

        if (!mSequenceLieux.equals("") || !mDateDebut.equals("")) {
            Bundle resultat = new Bundle();
            if (!mSequenceLieux.equals("")) {
                resultat.putString(Utils.BUNDLE_FILTER_ROOM, mSequenceLieux);
            }
            if (!mDateDebut.equals("")) {
                resultat.putString(Utils.BUNDLE_FILTER_DATE_START, mDateDebut);
            }
            sendResultToFragmentManager(resultat);
        } else {
            Toast.makeText(mView.getContext(), "Veuillez saisir des critères", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeWithoutSaving() {
        closeWithoutResult();
    }

    private void sendResultToFragmentManager(Bundle resultat) {
        if (getActivity().getClass() == FilteringActivity.class) {
            getParentFragmentManager().setFragmentResult(Utils.FILTERING_ACTIVITY_FRAGMENT, resultat);
        } else {
            getParentFragmentManager().setFragmentResult(Utils.FILTERING_INTER_FRAGMENTS, resultat);
            replaceByFragmentAccueil();
        }
    }

    private void closeWithoutResult() {
        //Send result to FilteringActivity for closure
        if (getActivity().getClass() == FilteringActivity.class) {
            getParentFragmentManager().setFragmentResult(Utils.FILTERING_ACTIVITY_FRAGMENT, null);
        } else {
            replaceByFragmentAccueil();
        }
    }

    public final void replaceByFragmentAccueil(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_other, new FragmentAccueil())
                .commit();
    }
}