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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.MeetingListAdapter;
import com.companyx.mareu.controller.Utils;
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
import org.jetbrains.annotations.NotNull;

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

    private MeetingListAdapter adapter = new MeetingListAdapter(mReunionsAffichees);

    Date mDateDebutFiltre;
    List<Salle> sallesFiltre;

    public String[] mDates;

    private Statut mFragmentStatut;

    public Reunion mNouvelleReunion = null;

    private ApiServiceSalles mDummyApiServiceSalles;

    public MainFragment() {
        // Required empty public constructor
    }

    private enum Statut {
        SansFiltre,
        FiltreSalle, FiltreDate, FiltreSalleEtDate
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDummyApiServiceReunions = DI_Reunions.getServiceReunions();

        mFragmentStatut = Statut.SansFiltre;

        setHasOptionsMenu(true);

        //Recevoir bundle avec réunion en provenance du fragment Addmeeting
        //Expression lambda
        getParentFragmentManager().setFragmentResultListener(Utils.NEW_MEETING_INTER_FRAGMENTS, this, (requestKey, bundle) -> {
            mNouvelleReunion = (Reunion) bundle.getSerializable(Utils.BUNDLE_EXTRA_MEETING);
            addNewMeeting(mNouvelleReunion);
            //Contrôle par le fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_other, new FragmentAccueil())
                    .commit();
        });

        //Recevoir bundle avec critères filtre en provenance du fragment Filtering
        //Classe anonyme new FragmentResultListener()
        getParentFragmentManager().setFragmentResultListener(Utils.FILTERING_INTER_FRAGMENTS, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String sequenceLieux = bundle.getString(Utils.BUNDLE_FILTER_ROOM);
                String dateDebut = bundle.getString(Utils.BUNDLE_FILTER_DATE_START);
                filterDisplayListe(sequenceLieux, dateDebut);

                refreshDisplay(mFragmentStatut);

                Log.d("MainFragment","on Filter Result");
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
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(adapter);

        refreshDisplay(mFragmentStatut);

        Log.d("MainFragment","on Resume");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tri_lieu_croissant:
                trierLieuCroissant();
                break;

            case R.id.tri_lieu_decroissant:
                trierLieuDecroissant();
                break;

            case R.id.tri_heure_croissante:
                trierHeureCroissant();
                break;

            case R.id.tri_heure_decroissante:
                trierHeureDecroissant();
                break;

            case R.id.sans_tri:
                sansTrier();
                break;
        }
        return super.onOptionsItemSelected(item);
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
     * @param event instance de DeleteMeetingEvent
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mDummyApiServiceReunions.deleteReunionItem(event.reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        miseJourAdapter("ADAPTER_DELETION", mReunionsAffichees);
    }

    public void addNewMeeting(Reunion reunion) {
        mDummyApiServiceReunions.addReunionItem(reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        miseJourAdapter("ADAPTER_ADDING", mReunionsAffichees);
    }

    //Pattern comportement
    public void noFilter() {
        mFragmentStatut = Statut.SansFiltre;
        refreshDisplay(mFragmentStatut);
    }

    public void filterRooms(List<Salle> sallesFiltre) {
        mFragmentStatut = Statut.FiltreSalle;
        mReunionsAffichees = mDummyApiServiceReunions.filterPlace(mDummyApiServiceReunions.getListeReunions(), sallesFiltre);
    }

    public void filterDate(Date mDate) {
        mFragmentStatut = Statut.FiltreDate;
        mReunionsAffichees = mDummyApiServiceReunions.filterDate(mDummyApiServiceReunions.getListeReunions(), mDate);
    }

    public void filterRoomsDate(List<Salle> sallesFiltre, Date mDate) {
        mFragmentStatut = Statut.FiltreSalleEtDate;
        mReunionsAffichees = mDummyApiServiceReunions.filterPlaceDate(mDummyApiServiceReunions.getListeReunions(), sallesFiltre, mDate);
    }

    private void miseJourAdapter(String msg, List<Reunion> reunions) {
        if (reunions.size()!= 0) {
            adapter.updateReunions(reunions);
            Log.d("MainFragment",msg);
            mDates = mDummyApiServiceReunions.getListeDate(reunions);
        } else {
            mBinding.messageAccueil.setText(R.string.message_accueil);
        }
    }

    /*private void miseJourAdapter (String msg, List<Reunion> reunions){
        if (reunions.size()!= 0) {
            adapter.updateReunions(reunions);
            Log.d("MainFragment",msg);
        } else{
            mBinding.messageAccueil.setText(R.string.message_filter_null);
        }
    }*/

    private void refreshDisplay(Statut mFragmentStatus) {
        //Pattern Etat
        switch (mFragmentStatus) {
            case SansFiltre:
                mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                miseJourAdapter("ADAPTER_INIT", mReunionsAffichees);
                break;
            case FiltreSalle:
                miseJourAdapter("ADAPTER_FILTRE_SALLE", mReunionsAffichees);
                break;
            case FiltreDate:
                miseJourAdapter("ADAPTER_FILTRE_DATE", mReunionsAffichees);
                break;
            case FiltreSalleEtDate:
                miseJourAdapter("ADAPTER_SALLE_HEURE", mReunionsAffichees);
                break;
        }
    }

    public void filterDisplayListe(String listeLieux, String dateDebut) {
        if (listeLieux!=null) {
            sallesFiltre = getListeSallesFromLieuxSequence(listeLieux);
            if (dateDebut!=null) {
                mDateDebutFiltre = new DateHeure(dateDebut).formatParseDate();
                filterRoomsDate(sallesFiltre, mDateDebutFiltre);
            } else {
                filterRooms(sallesFiltre);
            }
        } else {
            if (dateDebut!=null) {
                mDateDebutFiltre = new DateHeure(dateDebut).formatParseDate();
                filterDate(mDateDebutFiltre);
            } else {
                Log.d("ERROR_FILTRE", "Pas de critères de filtre reçus");
            }
        }
    }

    //Inspiré de com/companyx/mareu/controller/fragments/AddMeetingFragment.java:179
    private List<Salle> getListeSallesFromLieuxSequence(String ListeLieuxAvecVirgule) {
        List<Salle> listeSalles = new ArrayList<>();

        List<String> listeLieux = Arrays.asList(ListeLieuxAvecVirgule.split(", "));

        for (String lieu : listeLieux) {
            if (lieu.equals("")) {
                listeLieux.remove(lieu);
            }
            listeSalles.add(mDummyApiServiceSalles.createPlaceCatalogue().get(lieu));
        }
        return listeSalles;
    }


    public void trierLieuCroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortPlaceUp(mReunionsAffichees);
        miseJourAdapter("TRI_SALLE_CR", mReunionsTriees);
    }

    public void trierLieuDecroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortPlaceDown(mReunionsAffichees);
        miseJourAdapter("TRI_SALLE_DECR", mReunionsTriees);
    }

    public void trierHeureCroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortTimeDown(mReunionsAffichees);
        miseJourAdapter("TRI_HEURE_CR", mReunionsTriees);
    }

    public void trierHeureDecroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortTimeUp(mReunionsAffichees);
        miseJourAdapter("TRI_HEURE_DECR", mReunionsTriees);
    }

    public void sansTrier() {
        mReunionsTriees = mReunionsAffichees;
        miseJourAdapter("SANS_TRI", mReunionsTriees);
    }
}