package com.companyx.mareu.controller.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
import com.companyx.mareu.controller.activities.AddMeetingActivity;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.data.DummyApiServiceCollaborateurs;
import com.companyx.mareu.databinding.FragmentAddMeetingBinding;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMeetingFragment extends Fragment {

    private FragmentAddMeetingBinding mBinding;
    private View mView;

    private ApiServiceSalles mDummyApiServiceSalles;
    private DummyApiServiceCollaborateurs mDummyApiServiceCollaborateurs;

    private String mSujet;
    private Date mDateHeureDebut, mDateHeureFin;
    private Salle mSalleDeReunion;
    private List<Collaborateur> mParticipants;
    private Collaborateur mOrganisateur;

    private List<String> mEmailList;
    private String[] mLieux;

    private final Calendar mCalendrier = Calendar.getInstance();
    private int mAnnee, mMois, mJour, mHeure, mMinutes;

    public Reunion mReunion;

    public AddMeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDummyApiServiceSalles = DI_Salles.getServiceSalles();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
        Context context = mView.getContext();

        mLieux = mDummyApiServiceSalles.getPlaceList();
        ArrayAdapter<String> adapterLieux = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mLieux);
        mBinding.autoCompleteTextView.setAdapter(adapterLieux);
        mBinding.autoCompleteTextView.setThreshold(1);

        mDummyApiServiceCollaborateurs = new DummyApiServiceCollaborateurs();
        String[] participants = mDummyApiServiceCollaborateurs.getListeParticipants();
        ArrayAdapter<String> adapterParticipants = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, participants);
        mBinding.multiAutoCompleteTextView.setAdapter(adapterParticipants);
        mBinding.multiAutoCompleteTextView.setThreshold(1);
        mBinding.multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        String[] organisateurs = participants;
        ArrayAdapter<String> adapterOrganisateurs = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, organisateurs);
        mBinding.autoCompleteTextView2.setAdapter(adapterOrganisateurs);
        mBinding.autoCompleteTextView2.setThreshold(1);

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
                if(checkReunion()){
                    sendResultToFragmentManager();

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout_other, new FragmentAccueil())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Veuillez saisir tous les détails de la réunion", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.filter_action_close:
                closeWithoutResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = mView.getContext();

        mAnnee = mCalendrier.get(Calendar.YEAR);
        mMois = mCalendrier.get(Calendar.MONTH);
        mJour = mCalendrier.get(Calendar.DAY_OF_MONTH);

        mHeure = mCalendrier.get(Calendar.HOUR_OF_DAY);
        mMinutes = mCalendrier.get(Calendar.MINUTE);

        mBinding.DateHeureDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateTimePickerDialogDebut(context);
            }
        });
        mBinding.DateHeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateTimePickerDialogFin(context);
            }
        });

        mBinding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSalleDeReunion = mDummyApiServiceSalles.createPlaceCatalogue().get(parent.getItemAtPosition(position).toString());
                mBinding.Couleur.setImageResource(mSalleDeReunion.getIcone().valeur());
            }
        });

        mBinding.multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mParticipants = getListeParticipantsFromEmailSequence(mBinding.multiAutoCompleteTextView.getText().toString());
            }
        });

        mBinding.autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOrganisateur = mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(parent.getItemAtPosition(position).toString());
            }
        });
    }

    //    Clean up any references to the binding class instance, because Fragments outlive their views
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private List<Collaborateur> getListeParticipantsFromEmailSequence(String mailListWithComma) {
        List<Collaborateur> participantList = new ArrayList<Collaborateur>();

        mEmailList = Arrays.asList(mailListWithComma.split(", "));

        for (String email : mEmailList) {
            if (email == "") {
                mEmailList.remove(email);
            }
        }

        for (String email : mEmailList) {
            participantList.add(mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(email));
        }
        return participantList;
    }

    private void onDateTimePickerDialogDebut(Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        String dateDebut = jour + "/" + (mois + 1) + "/" + annee;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int heure, int minutes) {
                                        String heureDebut = heure + "h" + minutes;
                                        mBinding.DateHeureDebut.setText(dateDebut+" " + heureDebut);
                                        mDateHeureDebut = new DateHeure(dateDebut, heureDebut).formatParseDateHeure();
                                    }
                                }, mHeure, mMinutes, true);
                        timePickerDialog.show();
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }

    private void onDateTimePickerDialogFin(Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        String dateFin = jour + "/" + (mois + 1) + "/" + annee;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int heure,
                                                          int minutes) {
                                        String heureFin = heure + "h" + minutes;
                                        mBinding.DateHeureFin.setText(dateFin +" " + heureFin);
                                        mDateHeureFin = new DateHeure(dateFin, heureFin).formatParseDateHeure();
                                    }
                                }, mHeure, mMinutes, true);
                        timePickerDialog.show();
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }

    // --------------
    // ACTIONS
    // --------------

    private void sendResultToFragmentManager() {
        mReunion = createReunion();

        Bundle resultat = new Bundle();
        resultat.putSerializable(Utils.BUNDLE_EXTRA_MEETING, mReunion);
        if (getActivity().getClass() == AddMeetingActivity.class) {
            getParentFragmentManager().setFragmentResult(Utils.NEW_MEETING_ACTIVITY_FRAGMENT, resultat);
        } else {
            getParentFragmentManager().setFragmentResult(Utils.NEW_MEETING_INTER_FRAGMENTS, resultat);

            replaceByFragmentAccueil();
        }
    }

    private void closeWithoutResult() {
        //Send result to AddMeetingActivity for closure
        if (getActivity().getClass() == AddMeetingActivity.class) {
            getParentFragmentManager().setFragmentResult(Utils.NEW_MEETING_ACTIVITY_FRAGMENT, null);
        } else {
            replaceByFragmentAccueil();
        }
    }

    public final void replaceByFragmentAccueil(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_other, new FragmentAccueil())
                .commit();
    }

    public Reunion createReunion() {
        mSujet = mBinding.SujetBox.getEditText().getText().toString();

        Reunion reunion = new Reunion(
                mSalleDeReunion,
                mSujet,
                mParticipants,
                mDateHeureDebut,
                mDateHeureFin,
                mOrganisateur);
        return reunion;
    }

    private boolean checkReunion(){
        if(mSalleDeReunion == null||mSujet==""||mParticipants==null||mDateHeureDebut==null||mDateHeureFin==null||mOrganisateur==null){
            return false;
        } else {
            return true;
        }
    }
}