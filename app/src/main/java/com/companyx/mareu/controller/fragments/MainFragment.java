package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.MeetingListAdapter;
import com.companyx.mareu.data.ApiServiceReunions;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.databinding.FragmentMainBinding;
import com.companyx.mareu.di.DI_Reunions;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.events.DeleteMeetingEvent;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment{
    private FragmentMainBinding mBinding;
    private View mView;

    private RecyclerView mRecyclerView;
    public List<Reunion> mReunionsAffichees, mReunionsTriees;
    private ApiServiceReunions mDummyApiServiceReunions;
    private int mNombreReunionsAffichees;

    Date mDateDebutFiltre;
    List<Salle> sallesFiltre;

    public String[] mDates;

    private Statut mFragmentStatut;

    public Reunion mNouvelleReunion = null;

    private ApiServiceSalles mDummyApiServiceSalles;

    private static final String BUNDLE_EXTRA_MEETING = "BUNDLE_EXTRA_MEETING";
    private static final String NEW_MEETING_INTER_FRAGMENTS = "NEW_MEETING_INTER_FRAGMENTS";

    public MainFragment() {
        // Required empty public constructor
    }

    private enum Statut {
        SansFiltre,
        FiltreSalle, FiltreDate, FiltreSalleEtDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDummyApiServiceReunions = DI_Reunions.getServiceReunions();
        mDummyApiServiceReunions.getListeReunions();

        mFragmentStatut = Statut.SansFiltre;

        getParentFragmentManager().setFragmentResultListener(NEW_MEETING_INTER_FRAGMENTS, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                mNouvelleReunion = (Reunion) bundle.getSerializable(BUNDLE_EXTRA_MEETING);
                ajouterNouvelleReunion(mNouvelleReunion);
                //Contrôle par le fragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_other, new FragmentAccueil())
                        .commit();
            }
        });

        mDummyApiServiceSalles = DI_Salles.getServiceSalles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
        Context context = mView.getContext();
        mRecyclerView = mBinding.meetingItemList;
//        mRecyclerView = (RecyclerView) mView.findViewById(R.id.meeting_item_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        rafraichirAffichage(mFragmentStatut);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("ON_STOP_MAINFRAGMENT", "MainFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        Log.d("ON_DESTROY_MAINFRAGMENT", "MainFragment");
    }

    /**
     * Déclenché si on supprime une réunion
     *
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mDummyApiServiceReunions.deleteReunionItem(event.reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_DELETION", mReunionsAffichees);
    }

    public void ajouterNouvelleReunion(Reunion reunion) {
        mDummyApiServiceReunions.addReunionItem(reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_ADDING", mReunionsAffichees);
    }

    //Pattern comportement
    public void sansFiltrer() {
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_REINIT", mReunionsAffichees);
    }

    public void filtrerAvecSalles(List<Salle> sallesFiltre) {
        mFragmentStatut = Statut.FiltreSalle;
        mReunionsAffichees = mDummyApiServiceReunions.filtrerLieu(mDummyApiServiceReunions.getListeReunions(), sallesFiltre);
    }

    public void filtrerAvecDate(Date mDate) {
        mFragmentStatut = Statut.FiltreDate;
        mReunionsAffichees = mDummyApiServiceReunions.filtrerDate(mDummyApiServiceReunions.getListeReunions(), mDate);
    }

    public void filtrerAvecSallesEtDateHeure(List<Salle> sallesFiltre, Date mDate) {
        mFragmentStatut = Statut.FiltreSalleEtDate;
        mReunionsAffichees = mDummyApiServiceReunions.filtrerLieuEtDate(mDummyApiServiceReunions.getListeReunions(), sallesFiltre, mDate);
    }

    private void initialisationAdapterListeReunions(String tag, List<Reunion> reunions) {
        MeetingListAdapter adapter = new MeetingListAdapter(reunions);
        mRecyclerView.setAdapter(adapter);

        mDates = mDummyApiServiceReunions.getListeDate(mReunionsAffichees);

        mNombreReunionsAffichees = mReunionsAffichees.size();
        if (mNombreReunionsAffichees == 0) {
            mBinding.messageAccueil.setText(R.string.message_accueil);
        }
    }

    private void rafraichirAffichage(Statut mFragmentStatus) {
        //Pattern Etat
        switch (mFragmentStatus) {
            case SansFiltre:
                mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                initialisationAdapterListeReunions("ADAPTER_INIT", mReunionsAffichees);
                break;
            case FiltreSalle:
                initialisationAdapterListeReunions("ADAPTER_FILTRE_SALLE", mReunionsAffichees);
                break;
            case FiltreDate:
                initialisationAdapterListeReunions("ADAPTER_FILTRE_DATE", mReunionsAffichees);
                break;
            case FiltreSalleEtDate:
                initialisationAdapterListeReunions("ADAPTER_SALLE_HEURE", mReunionsAffichees);
                break;
        }
    }


    public void filtrerAffichageListe(String listeLieux, String dateDebut) {
        if (listeLieux.compareTo("") != 0) {
            sallesFiltre = getListeSallesFromLieuxSequence(listeLieux);
            if (dateDebut.compareTo("") != 0) {
                mDateDebutFiltre = new DateHeure(dateDebut).formatParseDate();
                filtrerAvecSallesEtDateHeure(sallesFiltre, mDateDebutFiltre);
            } else {
                filtrerAvecSalles(sallesFiltre);
            }
        } else {
            if (dateDebut.compareTo("") != 0) {
                mDateDebutFiltre = new DateHeure(dateDebut).formatParseDate();
                filtrerAvecDate(mDateDebutFiltre);
            } else {
                Log.d("ERROR_FILTRE", "Pas de critères de filtre reçus");
            }
        }
    }

    //Inspiré de com/companyx/mareu/controller/fragments/AddMeetingFragment.java:179
    private List<Salle> getListeSallesFromLieuxSequence(String ListeLieuxAvecVirgule) {
        List<Salle> listeSalles = new ArrayList<Salle>();

        List<String> listeLieux = Arrays.asList(ListeLieuxAvecVirgule.split(", "));

        for (String lieu : listeLieux) {
            if (lieu == "") {
                listeLieux.remove(lieu);
            }
            listeSalles.add(mDummyApiServiceSalles.creerCatalogueLieu().get(lieu));
        }
        return listeSalles;
    }
}