package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainFragment extends BaseFragment{
    private FragmentMainBinding mBinding;
//    private View mView;

    private RecyclerView mRecyclerView;

    private List<Reunion> mReunionsAffichees, mReunionsTriees;
    private ApiServiceReunions mDummyApiServiceReunions;

    private final MeetingListAdapter adapter = new MeetingListAdapter(mReunionsAffichees);

    private Date mDateDebutFiltre;
    private List<Salle> sallesFiltre;

    private String[] mDates;

    private Statut mFragmentStatut;

    private Reunion mNouvelleReunion = null;

    private ApiServiceSalles mDummyApiServiceSalles;

    public MainFragment() {
        // Required empty public constructor
    }

    // --------------
    // INITIALIZATION
    // --------------

    public static MainFragment newInstance(){
        return new MainFragment();
    }


    private enum Statut {
        SansFiltre,
        FiltreSalle, FiltreDate, FiltreSalleEtDate
    }

    // --------------
    // IMPLEMENTATION
    // --------------

    @Override
    protected void setCallback() {

    }

    @Override
    protected void configureFragmentSettings(Bundle savedInstanceState) {
        mDummyApiServiceReunions = DI_Reunions.getServiceReunions();

        mFragmentStatut = Statut.SansFiltre;

        setHasOptionsMenu(true);

        //Recevoir bundle avec réunion en provenance du fragment Addmeeting
        //Expression lambda
        getParentFragmentManager().setFragmentResultListener(Utils.NEW_MEETING_INTER_FRAGMENTS, this, (requestKey, bundle) -> {
            mNouvelleReunion = (Reunion) bundle.getSerializable(Utils.BUNDLE_EXTRA_MEETING);
            addNewMeeting(mNouvelleReunion);
            //Contrôle par le fragment
            callback.ManageOtherFragment();
        });

        //Recevoir bundle avec critères filtre en provenance du fragment Filtering
        //Classe anonyme new FragmentResultListener()
        getParentFragmentManager().setFragmentResultListener(Utils.FILTERING_INTER_FRAGMENTS, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String sequenceLieux = bundle.getString(Utils.BUNDLE_FILTER_ROOM);
                String dateDebut = bundle.getString(Utils.BUNDLE_FILTER_DATE_START);
                filterWithSelection(sequenceLieux, dateDebut);

                refreshDisplay();

                Log.d("Track MainFragment","on Filter Result");
            }
        });

        mDummyApiServiceSalles = DI_Salles.getServiceSalles();
    }

    @Override
    protected void configureFragmentDesign(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();

        Context context = mView.getContext();
        mRecyclerView = mBinding.meetingItemList;
//        mRecyclerView = (RecyclerView) mView.findViewById(R.id.meeting_item_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mDates = mDummyApiServiceReunions.getListeDate(mDummyApiServiceReunions.getListeReunions());
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(adapter);

        refreshDisplay();

        if(this.isAdded()){
            Log.d("Track MainFragment","isAdded on Resume");
        }
        //Not visible on resume  in m activity!
        if(this.isVisible()){
            Log.d("Track MainFragment","isVisible on Resume");
        }
        //Not Detached on resume!
        if(this.isDetached()){
            Log.d("Track MainFragment","isDetached on Resume");
        }
        if(this.isInLayout()){
            Log.d("Track MainFragment","isInLayout on Resume");
        }
        if(this.isHidden()){
            Log.d("Track MainFragment","isHidden on Resume");
        }

        Log.d("Track MainFragment","MainFragment Host : "+this.getHost().toString());

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
    }

    @Override
    protected void saveResultAndThenClose() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // --------------
    // CHANGE METHODS
    // --------------

    /**
     * Déclenché si on supprime une réunion
     *
     * @param event instance de DeleteMeetingEvent
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mDummyApiServiceReunions.deleteReunionItem(event.reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        updateAdapter("ADAPTER_DELETION", mReunionsAffichees);
    }

    public void addNewMeeting(Reunion reunion) {
        mDummyApiServiceReunions.addReunionItem(reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        updateAdapter("ADAPTER_ADDING", mReunionsAffichees);
    }

    // --------------
    // ADAPTER METHODS
    // --------------

    private void refreshDisplay() {
        //Pattern Etat
        switch (mFragmentStatut) {
            case SansFiltre:
                mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                updateAdapter("ADAPTER_INIT", mReunionsAffichees);
                break;
            case FiltreSalle:
                updateAdapter("ADAPTER_FILTRE_SALLE", mReunionsAffichees);
                break;
            case FiltreDate:
                updateAdapter("ADAPTER_FILTRE_DATE", mReunionsAffichees);
                break;
            case FiltreSalleEtDate:
                updateAdapter("ADAPTER_SALLE_HEURE", mReunionsAffichees);
                break;
        }
    }

    private void updateAdapter(String msg, List<Reunion> reunions) {
        if (reunions.size()!= 0) {
            adapter.updateReunions(reunions);
            //Dates de toutes réunions existantes
//            mDates = mDummyApiServiceReunions.getListeDate(reunions);
            Log.d("MainFragment",msg);
        } else {
            mBinding.messageAccueil.setText(R.string.message_accueil);
        }
    }

//TODO :  pattern strategy

    // --------------
    // FILTERING METHODS
    // --------------

    public void filterWithSelection(String listeLieux, String dateDebut) {
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
                Log.d("Track MainFragment", "Pas de critères de filtre reçus");
            }
        }
    }

    //Pattern comportement
    public void noFilter() {
        mFragmentStatut = Statut.SansFiltre;
        refreshDisplay();
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

    // --------------
    // SORTING METHODS
    // --------------

    public void trierLieuCroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortPlaceUp(mReunionsAffichees);
        updateAdapter("TRI_SALLE_CR", mReunionsTriees);
    }

    public void trierLieuDecroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortPlaceDown(mReunionsAffichees);
        updateAdapter("TRI_SALLE_DECR", mReunionsTriees);
    }

    public void trierHeureCroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortTimeDown(mReunionsAffichees);
        updateAdapter("TRI_HEURE_CR", mReunionsTriees);
    }

    public void trierHeureDecroissant() {
        mReunionsTriees = mDummyApiServiceReunions.sortTimeUp(mReunionsAffichees);
        updateAdapter("TRI_HEURE_DECR", mReunionsTriees);
    }

    public void sansTrier() {
        mReunionsTriees = mReunionsAffichees;
        updateAdapter("SANS_TRI", mReunionsTriees);
    }

    // --------------
    // OTHER METHODS
    // --------------

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

    public String[] getListDates(){
        return mDates;
    }
}