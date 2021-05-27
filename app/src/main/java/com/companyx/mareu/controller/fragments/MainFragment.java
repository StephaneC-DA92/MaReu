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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//Pattern création : Factory, voir FragmentFactory
public class MainFragment extends Fragment
//        implements MeetingListAdapter.OnDeleteButtonClick
{

    //    https://guides.codepath.com/android/using-the-recyclerview
    private RecyclerView mRecyclerView;
    private List<Reunion> mReunionsAffichees;
    public DummyApiServiceReunions mDummyApiServiceReunions;
    private int mNombreReunionsAffichees;

    TextView mTextViewMessage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILTER_ROOM = "filter_room";
    private static final String ARG_FILTER_DATE_START = "date_start";
    private static final String ARG_FILTER_DATE_END = "date_end";
    private static final String ARG_FILTER_FILTER_HOUR_START = "hour_start";
    private static final String ARG_FILTER_FILTER_HOUR_END = "hour_end";

    // TODO: Rename and change types of parameters
    private String mFiltreSalles;
    private String mFiltreDateDebut;
    private String mFiltreDateFin;
    private String mFiltreHeureDebut;
    private String mFiltreHeureFin;

    private Status mFragmentStatus;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param FiltreSalles Parameter 1.
     * @param FiltreDateDebut Parameter 2.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String FiltreSalles, String FiltreDateDebut, String FiltreHeureDebut, String FiltreDateFin, String FiltreHeureFin) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_ROOM, FiltreSalles);
        args.putString(ARG_FILTER_DATE_START, FiltreDateDebut);
        args.putString(ARG_FILTER_FILTER_HOUR_START, FiltreHeureDebut);
        args.putString(ARG_FILTER_DATE_END, FiltreDateFin);
        args.putString(ARG_FILTER_FILTER_HOUR_END, FiltreHeureFin);
        fragment.setArguments(args);
        return fragment;
    }

    private enum Status{
        SansFiltre, FiltreTri;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ON_CREATE","new MainFragment");
        mFragmentStatus = Status.SansFiltre;

        if (getArguments() != null) {
            mFiltreSalles = getArguments().getString(ARG_FILTER_ROOM);
            mFiltreDateDebut = getArguments().getString(ARG_FILTER_DATE_START);
/*            mFiltreHeureDebut = getArguments().getString(ARG_FILTER_FILTER_HOUR_START);
            mFiltreDateFin = getArguments().getString(ARG_FILTER_DATE_END);
            mFiltreHeureFin = getArguments().getString(ARG_FILTER_FILTER_HOUR_END);*/

            mFragmentStatus = Status.FiltreTri;
        }

        mDummyApiServiceReunions = new DummyApiServiceReunions();
        //Pour developpement
        mDummyApiServiceReunions.initialisationData();

        mNombreReunionsAffichees = mDummyApiServiceReunions.getNombreReunions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ON_CREATEVIEW","Recycler");
        //TODO : revoir le cas 0. onResume seulement avec Toast ou Dialog
        switch (mNombreReunionsAffichees) {
            case 0: {
                View view = inflater.inflate(R.layout.fragment_accueil, container, false);
                mTextViewMessage = (TextView) view.findViewById(R.id.message_accueil);
            }
            default: {
                // Inflate the layout for this fragment
                View view = inflater.inflate(R.layout.fragment_main, container, false);
                    //mRecyclerView = (RecyclerView) findViewById(R.id.meeting_item_list);
                Context context = view.getContext();
                mRecyclerView = (RecyclerView) view;
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    //Séparateur vertical
                    //mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                return view;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//Faire Reset
        //Boucle if
        switch (mNombreReunionsAffichees) {
            case 0: {
                mTextViewMessage.setText(R.string.message_accueil);
            }
            default: {
                //Pattern Etat
                switch(mFragmentStatus) {
                    case SansFiltre:
//        Initialisation de la liste de réunions : post-rafraichissement écran
                    mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
                    initialisationAdapterListeReunions(mReunionsAffichees, "ADAPTER_INIT");
                    case FiltreTri:
                    initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_FILTRE_SALLE");
                }
            }
        }
    }

    private void initialisationAdapterListeReunions(List<Reunion> reunions, String tag){
        MeetingListAdapter adapter = new MeetingListAdapter(reunions);
        mRecyclerView.setAdapter(adapter);
//        adapter.setOnDeleteButtonClickListener(this);
        Log.e(tag,"Nombre items : "+adapter.getItemCount());
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
        initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_DELETION");
    }

    public void ajouterNouvelleReunion(Reunion reunion){
        mDummyApiServiceReunions.addReunionItem(reunion);
//        Inutile. TODO : réfléchir à new Fragment
        mReunionsAffichees = mDummyApiServiceReunions.getListeReunions();
        initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_ADDING");
    }

    //Pattern comportement : Strategy ?
    public void filterAvecSalles(List<Salle> sallesFiltre){
        mFragmentStatus = Status.FiltreTri;
        mReunionsAffichees =mDummyApiServiceReunions.filtrerLieu(mDummyApiServiceReunions.getListeReunions(),sallesFiltre);
//        initialisationAdapterListeReunions(mReunions,"ADAPTER_FILTRE_SALLE");
    }

    public void filterAvecDateHeure(Date mDateHeureDebutFiltre, Date mDateHeureFinFiltre){
        mReunionsAffichees =mDummyApiServiceReunions.filtrerHeure(mDummyApiServiceReunions.getListeReunions(),mDateHeureDebutFiltre,mDateHeureFinFiltre);
        initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_FILTRE_DATE");
    }


    public void filterAvecSallesEtDateHeure(List<Salle> sallesFiltre, Date mDateHeureDebutFiltre, Date mDateHeureFinFiltre){
        mReunionsAffichees =mDummyApiServiceReunions.filtrerLieuEtHeure(mDummyApiServiceReunions.getListeReunions(),sallesFiltre,mDateHeureDebutFiltre,mDateHeureFinFiltre);
        initialisationAdapterListeReunions(mReunionsAffichees,"ADAPTER_SALLE_HEURE");
    }

    public void trierLieuCroissant(){
        mReunionsAffichees = mDummyApiServiceReunions.trierLieuCroissant(mDummyApiServiceReunions.getListeReunions());
        initialisationAdapterListeReunions(mReunionsAffichees,"TRI_SALLE_CR");
    }

    public void trierLieuDecroissant(){
        mReunionsAffichees = mDummyApiServiceReunions.trierLieuDecroissant(mDummyApiServiceReunions.getListeReunions());
        initialisationAdapterListeReunions(mReunionsAffichees,"TRI_SALLE_DECR");
    }

    public void trierHeureCroissant(){
        mReunionsAffichees = mDummyApiServiceReunions.trierHeureCroissant(mDummyApiServiceReunions.getListeReunions());
        initialisationAdapterListeReunions(mReunionsAffichees,"TRI_HEURE_CR");


    }
    public void trierHeureDecroissant(){
        mReunionsAffichees = mDummyApiServiceReunions.trierHeureDecroissant(mDummyApiServiceReunions.getListeReunions());
        initialisationAdapterListeReunions(mReunionsAffichees,"TRI_HEURE_DECR");
    }

    public Date dateMax(){
        return mDummyApiServiceReunions.valeurDateMax();
    }

    public Date dateMin(){
        return mDummyApiServiceReunions.valeurDateMin();
    }
}