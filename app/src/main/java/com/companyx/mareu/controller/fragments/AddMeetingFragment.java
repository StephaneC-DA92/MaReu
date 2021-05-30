package com.companyx.mareu.controller.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;

import com.companyx.mareu.data.DummyApiServiceCollaborateurs;
import com.companyx.mareu.data.DummyApiServiceReunions;
import com.companyx.mareu.data.DummyApiServiceSalles;
import com.companyx.mareu.databinding.FragmentAddMeetingBinding;
import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMeetingFragment extends Fragment {

    private FragmentAddMeetingBinding mBinding;

    private DummyApiServiceSalles mDummyApiServiceSalles;
    private DummyApiServiceCollaborateurs mDummyApiServiceCollaborateurs;
    private DummyApiServiceReunions mReunions;

/*    public TextInputLayout mSujetInputEditText;
    public EditText mHeureDebutEditText, mHeureFinEditText;
    public AutoCompleteTextView mSalletextview, mOrganisateurTextView;
    public ImageView mSalleIcone;
    public MultiAutoCompleteTextView mParticipantsTextview;*/

    private View mView;

    private List<Collaborateur> mParticipants;
    private Collaborateur mOrganisateur;
    private String mSujet;

    private List<String> emailList;

    private Salle salleDeReunion;

    private final Calendar mCalendrier = Calendar.getInstance();
    private int mAnnee, mMois,mJour,mHeure,mMinutes;

    private Date mDateHeureDebut, mDateHeureFin;

    private String[] mlieux;

    public AddMeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
//        mView = inflater.inflate(R.layout.fragment_add_meeting, container, false);
        Context context = mView.getContext();

/*        mSujetInputEditText = (TextInputLayout) mView.findViewById(R.id.SujetBox);

        mHeureDebutEditText = (EditText) mView.findViewById(R.id.DateHeureDebut);
        mHeureFinEditText = (EditText) mView.findViewById(R.id.DateHeureFin);

        mSalletextview = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView);
        mSalleIcone = (ImageView) mView.findViewById(R.id.Couleur);*/

        mDummyApiServiceSalles = new DummyApiServiceSalles();
        mlieux = mDummyApiServiceSalles.getListeLieu();
        ArrayAdapter<String> adapterLieux = new  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,mlieux);
        mBinding.autoCompleteTextView.setAdapter(adapterLieux);
        mBinding.autoCompleteTextView.setThreshold(1);

//        mParticipantsTextview = (MultiAutoCompleteTextView) mView.findViewById(R.id.multiAutoCompleteTextView);
        mDummyApiServiceCollaborateurs = new DummyApiServiceCollaborateurs();
        String[] participants = mDummyApiServiceCollaborateurs.getListeParticipants();
        ArrayAdapter<String> adapterParticipants = new  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,participants);
        mBinding.multiAutoCompleteTextView.setAdapter(adapterParticipants);
        mBinding.multiAutoCompleteTextView.setThreshold(1);
        mBinding.multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

//        mOrganisateurTextView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView2);
        //TODO : à modifier
        String[] organisateurs = participants;
        ArrayAdapter<String> adapterOrganisateurs = new  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,organisateurs);
        mBinding.autoCompleteTextView2.setAdapter(adapterOrganisateurs);
        mBinding.autoCompleteTextView2.setThreshold(1);

        return mView;
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
                extraireLieuSelection(salleDeReunion, parent.getItemAtPosition(position).toString());
            }
        });

        //TODO : capture saisie setOnItemSelectedListener ou empêcher saisie
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
        mBinding=null;
    }

    private List<Collaborateur> getListeParticipantsFromEmailSequence(String mailListWithComma){
        List<Collaborateur> participantList = new ArrayList<Collaborateur>();

        emailList = Arrays.asList(mailListWithComma.split(", "));

        for (String email : emailList){
            if(email==""){
                emailList.remove(email);
            }
        }

        for (String email : emailList){
            participantList.add(mDummyApiServiceCollaborateurs.creerCatalogueParticipant().get(email));
        }
        return  participantList;
    }

    private void extraireLieuSelection(Salle salle, String choix){
        salle = mDummyApiServiceSalles.creerCatalogueLieu().get(choix);
//        salleDeReunion = mDummyApiServiceSalles.creerCatalogueLieu().get(mSalletextview.getText().toString());
        mBinding.Couleur.setImageResource(salleDeReunion.getIcone().valeur());

    }

    private void onDateTimePickerDialogDebut(Context context){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        String dateDebut = jour + "/" + (mois + 1) + "/" + annee;
                        mBinding.DateHeureDebut.setText(dateDebut);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int heure,int minutes) {
                                        String heureDebut = heure + "h" + minutes;
                                        mBinding.DateHeureDebut.append(" "+heureDebut);
                                        mDateHeureDebut = new DateHeure(dateDebut,heureDebut).formatParseDateHeure();                                    }
                                }, mHeure, mMinutes, true);
                        timePickerDialog.show();
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }

    private void onDateTimePickerDialogFin(Context context){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        String dateFin = jour + "/" + (mois + 1) + "/" + annee;
                        mBinding.DateHeureFin.setText(dateFin);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int heure,
                                                          int minutes) {
                                        String heureFin = heure + "h" + minutes;
                                        mBinding.DateHeureFin.append(" "+heureFin);
                                        mDateHeureFin= new DateHeure(dateFin,heureFin).formatParseDateHeure();                                    }
                                }, mHeure, mMinutes, true);
                        timePickerDialog.show();
                    }
                }, mAnnee, mMois, mJour);
        datePickerDialog.show();
    }

    public Reunion createReunion() {
        mSujet = mBinding.SujetBox.getEditText().getText().toString();

        Reunion reunion = new Reunion(
                salleDeReunion,
                mSujet,
                mParticipants,
                mDateHeureDebut,
                mDateHeureFin,
                mOrganisateur);
//        DONE : ajout à déplacer dans Fragment
        return reunion;
    }
}