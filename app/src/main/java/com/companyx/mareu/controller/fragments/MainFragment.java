package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.MeetingListAdapter;
import com.companyx.mareu.data.DummyApiServiceReunions;
import com.companyx.mareu.events.DeleteMeetingEvent;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;

//Pattern création : Factory, voir FragmentFactory
public class MainFragment extends Fragment
//        implements MeetingListAdapter.OnDeleteButtonClick
{

    //    https://guides.codepath.com/android/using-the-recyclerview
    private RecyclerView mRecyclerView;
    public List<Reunion> mReunionsAffichees,mReunionsTriees;
    public DummyApiServiceReunions mDummyApiServiceReunions;
    private int mNombreReunionsAffichees;

    private TextView mTextViewMessage;

    public String[] mDates;

    private Statut mFragmentStatut;

    public MainFragment() {
        // Required empty public constructor
    }

    private enum Statut {
        SansFiltre,
        FiltreSalle,FiltreDate,FiltreSalleEtDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ON_CREATE","new MainFragment");

        mDummyApiServiceReunions = new DummyApiServiceReunions();
        //TODO : à supprimer après developpement
        mDummyApiServiceReunions.initialisationData();

        mFragmentStatut = Statut.SansFiltre;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ON_CREATEVIEW","Recycler");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
//                View view = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.meeting_item_list);
//                mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            //Séparateur vertical
            //mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mTextViewMessage = (TextView) view.findViewById(R.id.message_accueil);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
            rafraichirAffichage(mFragmentStatut);
        Log.d("ON_RESUME", "Rafraîchissement");

    }

/*    @Override
    public void onButtonClicked(View view,Reunion reunion) {
        mDummyApiServiceReunions.deleteReunionItem(reunion);
//        TODO : utilité à vérifier
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_DELETION");
//        Log.e("ADAPTER_DELETION","Nombre items : -1");
    }*/

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

    /**
     * Déclenché si on supprime une réunion
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mDummyApiServiceReunions.deleteReunionItem(event.reunion);
//        TODO : utilité à vérifier
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_DELETION",mReunionsAffichees);
    }

    public void ajouterNouvelleReunion(Reunion reunion){
        mDummyApiServiceReunions.addReunionItem(reunion);
//        Inutile.
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_ADDING",mReunionsAffichees);
    }

    //Pattern comportement : Strategy ?
    public void sansFiltrer(){
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions("ADAPTER_REINIT",mReunionsAffichees);
    }

    public void filtrerAvecSalles(List<Salle> sallesFiltre){
        mFragmentStatut = Statut.FiltreSalle;
        mReunionsAffichees =mDummyApiServiceReunions.filtrerLieu(mDummyApiServiceReunions.getListeReunions(),sallesFiltre);
    }

    public void filtrerAvecDate(Date mDate){
        mFragmentStatut = Statut.FiltreDate;
        mReunionsAffichees =mDummyApiServiceReunions.filtrerDate(mDummyApiServiceReunions.getListeReunions(),mDate);
    }

    public void filtrerAvecSallesEtDateHeure(List<Salle> sallesFiltre, Date mDate){
        mFragmentStatut = Statut.FiltreSalleEtDate;
        mReunionsAffichees =mDummyApiServiceReunions.filtrerLieuEtDate(mDummyApiServiceReunions.getListeReunions(),sallesFiltre,mDate);
    }

    private void initialisationAdapterListeReunions(String tag,List<Reunion> reunions){
        MeetingListAdapter adapter = new MeetingListAdapter(reunions);
        mRecyclerView.setAdapter(adapter);
//        adapter.setOnDeleteButtonClickListener(this);

//        Log.d(tag, "Nombre items : " + adapter.getItemCount());

        mDates = mDummyApiServiceReunions.getListeDate(mReunionsAffichees);
        mNombreReunionsAffichees = mReunionsAffichees.size();

        if (mNombreReunionsAffichees==0) {
            mTextViewMessage.setText(R.string.message_accueil);
        }
    }

    private void rafraichirAffichage(Statut mFragmentStatus) {
        //Pattern Etat
        switch (mFragmentStatus) {
            case SansFiltre:
                mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                initialisationAdapterListeReunions("ADAPTER_INIT",mReunionsAffichees);
                break;
            case FiltreSalle:
                initialisationAdapterListeReunions("ADAPTER_FILTRE_SALLE",mReunionsAffichees);
                break;
            case FiltreDate:
                initialisationAdapterListeReunions("ADAPTER_FILTRE_DATE",mReunionsAffichees);
                break;
            case FiltreSalleEtDate:
                initialisationAdapterListeReunions("ADAPTER_SALLE_HEURE",mReunionsAffichees);
                break;
           /* case SalleCroissante:
                initialisationAdapterListeReunions("TRI_SALLE_CR",mReunionsTriees);
                break;
            case SalleDécroissante:
                initialisationAdapterListeReunions("TRI_SALLE_DECR",mReunionsTriees);
                break;
            case HeureCroissante:
                initialisationAdapterListeReunions("TRI_HEURE_CR",mReunionsTriees);
                break;
            case HeureDécroissante:
                initialisationAdapterListeReunions("TRI_HEURE_DECR",mReunionsTriees);
                break;*/
           /* default :
                mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                initialisationAdapterListeReunions("ADAPTER_DEFAULT");*/
        }
    }

    public void trierLieuCroissant(){
        mReunionsTriees = mDummyApiServiceReunions.trierLieuCroissant(mReunionsAffichees);
        initialisationAdapterListeReunions("TRI_SALLE_CR",mReunionsTriees);
    }

    public void trierLieuDecroissant(){
        mReunionsTriees = mDummyApiServiceReunions.trierLieuDecroissant(mReunionsAffichees);
        initialisationAdapterListeReunions("TRI_SALLE_DECR",mReunionsTriees);
    }

    public void trierHeureCroissant(){
        mReunionsTriees = mDummyApiServiceReunions.trierHeureCroissant(mReunionsAffichees);
        initialisationAdapterListeReunions("TRI_HEURE_CR",mReunionsTriees);
    }

    public void trierHeureDecroissant(){
        mReunionsTriees = mDummyApiServiceReunions.trierHeureDecroissant(mReunionsAffichees);
        initialisationAdapterListeReunions("TRI_HEURE_DECR",mReunionsTriees);
    }

    public void sansTrier(){
        mReunionsTriees = mReunionsAffichees;
        initialisationAdapterListeReunions("SANS_TRI",mReunionsTriees);
    }
}