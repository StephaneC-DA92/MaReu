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

import java.util.Date;
import java.util.List;

public class MainFragment extends BaseFragment{
    private FragmentMainBinding mBinding;
//    private View mView;

    private RecyclerView mRecyclerView;

    private List<Reunion> mReunionsAffichees, mReunionsTriees;
    private ApiServiceReunions mDummyApiServiceReunions;

    private String sequenceLieux = null, dateDebut = null;

    private final MeetingListAdapter adapter = new MeetingListAdapter(mReunionsAffichees);

    private Date mDateDebutFiltre;
    private List<Salle> sallesFiltre;

    public String[] mDates;

    private Statut mFragmentStatut;

    private Reunion mNouvelleReunion = null;

    private ApiServiceSalles mDummyApiServiceSalles;

    private Bundle myBundle;

    public void setMyBundle(Bundle myBundle) {
        this.myBundle = myBundle;
    }

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
        try{
            this.callback = (FragmentActionListener) getActivity();
        }catch(ClassCastException e){
            Log.d("MainFragment","No Listener found");
        }
    }

    @Override
    protected void configureFragmentSettings(Bundle savedInstanceState) {
        mDummyApiServiceReunions = DI_Reunions.getServiceReunions();

        try{
            if(savedInstanceState.getString(Utils.BUNDLE_FRAGMENT_STATUS_KEY)!=null) {
                this.mFragmentStatut = Statut.valueOf(savedInstanceState.getString(Utils.BUNDLE_FRAGMENT_STATUS_KEY));
            }
        } catch (NullPointerException e) {
            Log.e("Track MainFragment","BUNDLE_FRAGMENT_STATUS_KEY exception : "+e);
        }

        if(this.mFragmentStatut == null){
            this.mFragmentStatut = Statut.SansFiltre;
        } else if(this.mFragmentStatut != Statut.SansFiltre){
            try{
                if(savedInstanceState.getBundle(Utils.BUNDLE_DISPLAYED_SPECS)!=null){

                    setMyBundle(savedInstanceState.getBundle(Utils.BUNDLE_DISPLAYED_SPECS));
                }
            } catch (NullPointerException e) {
                Log.e("Track MainFragment","BUNDLE_DISPLAYED_..._KEY exception : "+e);
            }
        }

        setHasOptionsMenu(true);

        //Recevoir bundle avec r??union en provenance du fragment Addmeeting
        //Expression lambda
        getParentFragmentManager().setFragmentResultListener(Utils.NEW_MEETING_INTER_FRAGMENTS_KEY, this, (requestKey, bundle) -> {
            mNouvelleReunion = (Reunion) bundle.getSerializable(Utils.BUNDLE_EXTRA_MEETING);
            addNewMeeting(mNouvelleReunion);

            callback.ManageOtherFragment();

            Log.d("Track MainFragment","on addNewMeeting Result");

        });

        //Recevoir bundle avec crit??res filtre en provenance du fragment Filtering
        //Classe anonyme new FragmentResultListener()
        getParentFragmentManager().setFragmentResultListener(Utils.FILTERING_INTER_FRAGMENTS_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                filterWithDefinedSpecs(bundle);

                callback.ManageOtherFragment();

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mDates = mDummyApiServiceReunions.getListeDate(mDummyApiServiceReunions.getListeReunions());

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if(mFragmentStatut!=Statut.SansFiltre){
            defineSpecsFromBundle(myBundle);
            filterWithSpecs();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshDisplay();
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
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(this.myBundle!=null)
            outState.putBundle(Utils.BUNDLE_DISPLAYED_SPECS, this.myBundle);

        outState.putString(Utils.BUNDLE_FRAGMENT_STATUS_KEY, mFragmentStatut.name());
    }

    // --------------
    // ACTION METHODS
    // --------------

    //Gestion par eventBus
    /**
     * D??clench?? si on supprime une r??union
     *
     * @param event instance de DeleteMeetingEvent
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mDummyApiServiceReunions.deleteReunionItem(event.reunion);
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        updateAdapter("ADAPTER_DELETION", mReunionsAffichees);
    }

    //Gestion par fragment manager
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

        Log.d("Track MainFragment","refreshDisplay");
    }

    private void updateAdapter(String msg, List<Reunion> reunions) {
        if (reunions.size()!= 0) {
            adapter.updateReunions(reunions);

            Log.d("MainFragment",msg);
        } else {
            mBinding.messageAccueil.setText(R.string.message_accueil);
        }
    }

    // --------------
    // FILTERING METHODS
    // --------------

    public void filterWithDefinedSpecs(Bundle bundle){
        setMyBundle(bundle);

        defineSpecsFromBundle(bundle);

        filterWithSpecs();
    }

    private void defineSpecsFromBundle(Bundle bundle){

        this.sequenceLieux = bundle.getString(Utils.BUNDLE_FILTER_ROOM);
        this.dateDebut = bundle.getString(Utils.BUNDLE_FILTER_DATE_START);
    }

    private void filterWithSpecs() {

        if (sequenceLieux!=null) {
            sallesFiltre = mDummyApiServiceSalles.getSallesFromLieux(sequenceLieux);
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
                Log.d("Track MainFragment", "Pas de crit??res de filtre re??us");
            }
        }
    }

    //Pattern comportement
    public void noFilter() {
        mFragmentStatut = Statut.SansFiltre;

        if(!myBundle.isEmpty()) {
            myBundle.clear();
        }

        refreshDisplay();
    }

    private void filterRooms(List<Salle> sallesFiltre) {
        mFragmentStatut = Statut.FiltreSalle;
        mReunionsAffichees = mDummyApiServiceReunions.filterPlace(mDummyApiServiceReunions.getListeReunions(), sallesFiltre);
    }

    private void filterDate(Date mDate) {
        mFragmentStatut = Statut.FiltreDate;
        mReunionsAffichees = mDummyApiServiceReunions.filterDate(mDummyApiServiceReunions.getListeReunions(), mDate);
    }

    private void filterRoomsDate(List<Salle> sallesFiltre, Date mDate) {
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

    public String[] getmDates() {
        return mDates;
    }

}