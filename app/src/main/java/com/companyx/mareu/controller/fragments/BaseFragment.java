package com.companyx.mareu.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.companyx.mareu.controller.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class BaseFragment extends Fragment { //implements AllFragmentFactory.AppFragment

//    protected ViewBinding mBinding;
    protected View mView;

    //TODO : vérifier
//    protected boolean EnableSaveInstance;

    // --------------
    // INTERFACE
    // --------------

    protected FragmentActionListener callback;

    public interface FragmentActionListener {
        public void onClickCloseMenu();
        public void NavigateToOtherActivity();
        public void ManageOtherFragment();
    }

    // --------------
    // BASE METHODS
    // --------------

//    protected abstract void configureFragmentSettings();
    protected abstract void configureFragmentSettings(Bundle savedInstanceState); //can be used for implementation for factory

    protected abstract void configureFragmentDesign(LayoutInflater inflater, ViewGroup container);

    //TODO : vérifier
//    protected abstract void writeBundle(Bundle bundle);

    protected abstract void saveResultAndThenClose();

    protected abstract void setCallback();

    // --------------
    // INITIALIZATION
    // --------------

    public static BaseFragment newInstance(){
        return null;
    }


    // -----------------
    // METHODS OVERRIDE
    // -----------------

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        setCallback();
        Log.d("Track BaseFragment","onAttach " + getClass().toString()+" to "+requireActivity().getLocalClassName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EnableSaveInstance = true;

        configureFragmentSettings(savedInstanceState);

        Log.d("Track BaseFragment","onCreate "+ getClass().toString()+" Id "+this.getId() +" in "+requireActivity().getLocalClassName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        configureFragmentDesign(inflater, container);

        return mView;
    }



    @Override
    public void onStart() {
        super.onStart();

        Log.d("Track BaseFragment","onStart " + getClass().toString()+" in "+requireActivity().getLocalClassName());

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Track BaseFragment","onPause " + getClass().toString()+" in "+requireActivity().getLocalClassName());

    }



    @Override
    public void onResume() {
        super.onResume();

        Log.d("Track BaseFragment","onResume for " + getClass().toString()+" with Id "+this.getId()+
                " in "+requireActivity().getLocalClassName()+
                " within Task "+requireActivity().getTaskId()+
                " Host : "+this.getHost().toString());
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("Track BaseFragment","onStop " + getClass().toString()+" in "+requireActivity().getLocalClassName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("Track BaseFragment","onDestroyView " + getClass().toString()+" in "+requireActivity().getLocalClassName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("Track BaseFragment","onDestroy " + getClass().toString()+" in "+requireActivity().getLocalClassName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Track BaseFragment","onDetach " + getClass().toString()+" for "+requireActivity().getLocalClassName());

    }

    // --------------
    // FRAGMENT SUPPORT
    // --------------

    //Envoi des résultats pour sauvegarde
    protected void sendResultToFragmentManager(Bundle resultat, String requestKey1, String requestKey2) {
//        if (getActivity().getClass() == activity.getClass()) {
        if (getActivity().getClass() != MainActivity.class) {
            getParentFragmentManager().setFragmentResult(requestKey1, resultat);
        } else {
            getParentFragmentManager().setFragmentResult(requestKey2, resultat);

            callback.ManageOtherFragment();
        }
    }

    /*private void saveFragmentResult(){
        //TODO : vérifier
//        EnableSaveInstance = false ;

        saveResultAndThenClose();
    }*/

/*    protected void closeWithoutResult(@StringRes int ResId, SharedPreferences.Editor editor){
        //TODO : vérifier
//        EnableSaveInstance = false ;

        closeWithoutFragmentResult(ResId, editor);
    }*/

    //Fermeture et actions de l'activité hôte
    protected void closeWithoutResult(@StringRes int ResId, SharedPreferences.Editor editor) {
//        EnableSaveInstance = false ;

        if (requireActivity().getClass() != MainActivity.class) {

        //Cannot send null result to hosting activity with getParentFragmentManager().setFragmentResult(requestKey, null);

        //Callback for activity to handle closure without result
            callback.onClickCloseMenu();

            Log.d("Track BaseFragment","closeWithoutResult " + requireActivity().getClass().toString());

        } else {
            callback.ManageOtherFragment();
        }

        editor.clear().commit(); // vider les préférences

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().deleteSharedPreferences(getString(ResId)); //supprimer les préférences
        }
    }

}