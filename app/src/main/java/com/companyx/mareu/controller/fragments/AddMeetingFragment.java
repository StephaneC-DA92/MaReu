package com.companyx.mareu.controller.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.Utils;
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

public class AddMeetingFragment extends BaseFragment{

    private FragmentAddMeetingBinding mAddMeetingBinding;

    private ApiServiceSalles mDummyApiServiceSalles;
    private DummyApiServiceCollaborateurs mDummyApiServiceCollaborateurs;

    private String mSujet, dateDebut,dateFin,heureDebut,heureFin, salleDeReunionString, participantString, organisateurString;
    private Date mDateHeureDebut, mDateHeureFin;
    private Salle mSalleDeReunion;
    private List<Collaborateur> mParticipants;
    private Collaborateur mOrganisateur;

    private List<String> mEmailList;
    private String[] mLieux;

    private final Calendar mCalendrier = Calendar.getInstance();
    private int mAnnee, mMois, mJour, mHeure, mMinutes;

    private SharedPreferences AddMeetingSharedPref;
    private SharedPreferences.Editor AddMeetingEditor;

    public Reunion mReunion;

    public AddMeetingFragment() {
        // Required empty public constructor
    }

    // --------------
    // INITIALIZATION
    // --------------

    public static AddMeetingFragment newInstance(){
        return new AddMeetingFragment();
    }

    // --------------
    // IMPLEMENTATION
    // --------------

    @Override
    protected void setCallback() {
        try{
            this.callback = (FragmentActionListener) getActivity();
        }catch(ClassCastException e){
            Log.d("AddMeetingFragment","No Listener found");
        }
    }


    @Override
    protected void configureFragmentSettings(Bundle savedInstanceState) {
        mDummyApiServiceSalles = DI_Salles.getServiceSalles();

        setHasOptionsMenu(true);

        AddMeetingSharedPref = requireActivity().getSharedPreferences(getString(R.string.addmeeting_preference_file_key),Context.MODE_PRIVATE);
        AddMeetingEditor = AddMeetingSharedPref.edit();
    }

    @Override
    protected void configureFragmentDesign(LayoutInflater inflater, ViewGroup container) {
        mAddMeetingBinding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        mView = mAddMeetingBinding.getRoot();

        Context context = mView.getContext();

        mLieux = mDummyApiServiceSalles.getPlaceList();
        ArrayAdapter<String> adapterLieux = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mLieux);
        mAddMeetingBinding.autoCompleteRoom.setAdapter(adapterLieux);
        mAddMeetingBinding.autoCompleteRoom.setThreshold(1);

        mDummyApiServiceCollaborateurs = new DummyApiServiceCollaborateurs();
        String[] participants = mDummyApiServiceCollaborateurs.getListeParticipants();
        ArrayAdapter<String> adapterParticipants = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, participants);
        mAddMeetingBinding.multiAutoCompleteParticipants.setAdapter(adapterParticipants);
        mAddMeetingBinding.multiAutoCompleteParticipants.setThreshold(1);
        mAddMeetingBinding.multiAutoCompleteParticipants.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        String[] organisateurs = participants;
        ArrayAdapter<String> adapterOrganisateurs = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, organisateurs);
        mAddMeetingBinding.autoCompleteOrganisateur.setAdapter(adapterOrganisateurs);
        mAddMeetingBinding.autoCompleteOrganisateur.setThreshold(1);

        //        Configuration et éventuellement remplissage des box
        if(AddMeetingSharedPref.contains(getString(R.string.addmeeting_sujet_key))) {
            mSujet = AddMeetingSharedPref.getString(getString(R.string.addmeeting_sujet_key), "");
            dateDebut = AddMeetingSharedPref.getString(getString(R.string.addmeeting_dateDebut_key), "");
            dateFin = AddMeetingSharedPref.getString(getString(R.string.addmeeting_dateFin_key), "");
            heureDebut = AddMeetingSharedPref.getString(getString(R.string.addmeeting_heureDebut_key), "");
            heureFin = AddMeetingSharedPref.getString(getString(R.string.addmeeting_heureFin_key), "");
            salleDeReunionString = AddMeetingSharedPref.getString(getString(R.string.addmeeting_salle_key), "");
            participantString = AddMeetingSharedPref.getString(getString(R.string.addmeeting_participant_key), "");
            organisateurString = AddMeetingSharedPref.getString(getString(R.string.addmeeting_organisateur_key), "");

            mAddMeetingBinding.Sujet.setText(mSujet.subSequence(0,mSujet.length()));

            mAddMeetingBinding.DateHeureDebut.setText((dateDebut+" "+heureDebut).subSequence(0,(dateDebut+" "+heureDebut).length()));
            mDateHeureDebut = new DateHeure(dateDebut, heureDebut).formatParseDateHeure();

            mAddMeetingBinding.DateHeureFin.setText((dateFin+" "+heureFin).subSequence(0,(dateFin+" "+heureFin).length()));
            mDateHeureFin = new DateHeure(dateFin, heureFin).formatParseDateHeure();

            mAddMeetingBinding.autoCompleteRoom.setText(salleDeReunionString.subSequence(0,salleDeReunionString.length()));
            mSalleDeReunion = mDummyApiServiceSalles.createPlaceCatalogue().get(salleDeReunionString);
            if(mSalleDeReunion!=null){
                mAddMeetingBinding.Couleur.setImageResource(mSalleDeReunion.getIcone().valeur());
            }

            mAddMeetingBinding.multiAutoCompleteParticipants.setText(participantString.subSequence(0,participantString.length()));
            mParticipants = getListeParticipantsFromEmailSequence(participantString);

            mAddMeetingBinding.autoCompleteOrganisateur.setText(organisateurString.subSequence(0,organisateurString.length()));
            mOrganisateur = mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(organisateurString);

            Toast.makeText(requireContext(), "Action en cours pour ajout de réunion", Toast.LENGTH_SHORT).show();

        }
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
                closeWithoutResult(R.string.addmeeting_preference_file_key,AddMeetingEditor);
//                closeWithoutResult(Utils.NEW_MEETING_ACTIVITY_FRAGMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Bordure pour vue multifragments
        int size = (int) (getResources().getDimension(R.dimen.tablet_size)/getResources().getDisplayMetrics().density);

        if (getResources().getConfiguration().screenWidthDp >= size &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mAddMeetingBinding.addMeetingBorder.setVisibility(View.VISIBLE);
        }

        Context context = mView.getContext();

        setCalendarProps();

        //TODO : SharedPreferences à partager entre MainActivity et AddMeetingActivity
        mAddMeetingBinding.Sujet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //A valider

            @Override
            public void afterTextChanged(Editable editable) {
                mSujet = mAddMeetingBinding.SujetBox.getEditText().getText().toString();
                    if(!mSujet.equals("")){
                        AddMeetingEditor.putString(getString(R.string.addmeeting_sujet_key), mSujet).apply();
                    }
            }
        });


        mAddMeetingBinding.DateHeureDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onDateTimePickerDialogDebut(context);
                SetDateTimeOnDateTimePickerDialog(context, mAddMeetingBinding.DateHeureDebut);
            }
        });

        mAddMeetingBinding.DateHeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onDateTimePickerDialogFin(context);
                SetDateTimeOnDateTimePickerDialog(context, mAddMeetingBinding.DateHeureFin);
            }
        });

        mAddMeetingBinding.autoCompleteRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                salleDeReunionString = parent.getItemAtPosition(position).toString();
                    if(!salleDeReunionString.equals("")) {
                        AddMeetingEditor.putString(getString(R.string.addmeeting_salle_key), salleDeReunionString).apply();
                    }
                mSalleDeReunion = mDummyApiServiceSalles.createPlaceCatalogue().get(parent.getItemAtPosition(position).toString());

                mAddMeetingBinding.Couleur.setImageResource(mSalleDeReunion.getIcone().valeur());
            }
        });

        mAddMeetingBinding.multiAutoCompleteParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                participantString = mAddMeetingBinding.multiAutoCompleteParticipants.getText().toString();
                    if(!participantString.equals("")) {
                        AddMeetingEditor.putString(getString(R.string.addmeeting_participant_key), participantString).apply();
                    }
                mParticipants = getListeParticipantsFromEmailSequence(participantString);
            }
        });

        mAddMeetingBinding.autoCompleteOrganisateur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                organisateurString = parent.getItemAtPosition(position).toString();
                if(!organisateurString.equals("")) {
                    AddMeetingEditor.putString(getString(R.string.addmeeting_organisateur_key), organisateurString).apply();
                }
                mOrganisateur = mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(organisateurString);
            }
        });
    }

    //    Clean up any references to the binding class instance, because Fragments outlive their views
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAddMeetingBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setHasOptionsMenu(false);

        Log.d("AddMeetingFragment trk","destroy");

    }

    // --------------
    // PICKERS
    // --------------

    private void SetDateTimeOnDateTimePickerDialog(Context context, EditText textDateTime) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        String setDate = jour + "/" + (mois + 1) + "/" + annee;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int heure,
                                                          int minutes) {
                                        String setTime = heure + "h" + minutes;

                                        textDateTime.setText(setDate + " " + setTime);

                                        if (textDateTime.getId() == mAddMeetingBinding.DateHeureDebut.getId()) {
                                            dateDebut = setDate;
                                            heureDebut = setTime;
                                                if(!dateDebut.equals("")&&!heureDebut.equals("")) {
                                                    AddMeetingEditor.putString(getString(R.string.addmeeting_dateDebut_key), dateDebut).apply();
                                                    AddMeetingEditor.putString(getString(R.string.addmeeting_heureDebut_key), heureDebut).apply();
                                                }
                                            mDateHeureDebut = new DateHeure(dateDebut, heureDebut).formatParseDateHeure();
                                        } else if (textDateTime.getId() == mAddMeetingBinding.DateHeureFin.getId()) {
                                            dateFin = setDate;
                                            heureFin = setTime;
                                                if(!dateFin.equals("")&&!heureFin.equals("")) {
                                                    AddMeetingEditor.putString(getString(R.string.addmeeting_dateFin_key), dateFin).apply();
                                                    AddMeetingEditor.putString(getString(R.string.addmeeting_heureFin_key), heureFin).apply();
                                                }
                                            mDateHeureFin = new DateHeure(dateFin, heureFin).formatParseDateHeure();
                                        }
                                    }
                                }, mHeure, mMinutes, true);
                        timePickerDialog.show();
                    }
                }, mAnnee, mMois, mJour);

        datePickerDialog.show();
    }

    // --------------
    // OTHER ACTIONS
    // --------------

    private void setCalendarProps(){
        mAnnee = mCalendrier.get(Calendar.YEAR);
        mMois = mCalendrier.get(Calendar.MONTH);
        mJour = mCalendrier.get(Calendar.DAY_OF_MONTH);
        mHeure = mCalendrier.get(Calendar.HOUR_OF_DAY);
        mMinutes = mCalendrier.get(Calendar.MINUTE);
    }

    private List<Collaborateur> getListeParticipantsFromEmailSequence(String mailListWithComma) {
        List<Collaborateur> participantList = new ArrayList<>();

        if (mailListWithComma.contains(",")) {
            mEmailList = Arrays.asList(mailListWithComma.split(", "));

            for (String email : mEmailList) {
//            if (email == "") {
                if (email.isEmpty()) {
                    mEmailList.remove(email);
                }
            }

            for (String email : mEmailList) {
                participantList.add(mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(email));
            }
        }
        return participantList;
    }

    // --------------
    // PRIVATE METHODS
    // --------------

    @Override
    protected void saveResultAndThenClose() {
//        mSujet = mBinding.SujetBox.getEditText().getText().toString();

        if(mSalleDeReunion == null||mSujet.isEmpty()||mParticipants==null||mDateHeureDebut==null||mDateHeureFin==null||mOrganisateur==null){
            Toast.makeText(getContext(), "Veuillez saisir tous les détails de la réunion", Toast.LENGTH_LONG).show();
        } else {
            mReunion = new Reunion(
                    mSalleDeReunion,
                    mSujet,
                    mParticipants,
                    mDateHeureDebut,
                    mDateHeureFin,
                    mOrganisateur);

            //TODO : vérif
            Bundle resultat = new Bundle();
            resultat.putSerializable(Utils.BUNDLE_EXTRA_MEETING, mReunion);

            sendResultToFragmentManager(resultat, Utils.NEW_MEETING_ACTIVITY_FRAGMENT, Utils.NEW_MEETING_INTER_FRAGMENTS);

            ManageSharedPreferences();
        }
    }

    private void ManageSharedPreferences(){
        AddMeetingEditor.clear().commit(); // vider les préférences

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().deleteSharedPreferences(getString(R.string.addmeeting_preference_file_key)); //supprimer les préférences
        }
    }

}