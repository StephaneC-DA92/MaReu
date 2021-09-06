package com.companyx.mareu.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.companyx.mareu.databinding.FragmentAccueilBinding;

public class FragmentAccueil extends BaseFragment{

    private FragmentAccueilBinding mBinding;

    // --------------
    // INITIALIZATION
    // --------------

    public static FragmentAccueil newInstance(){
        return new FragmentAccueil();
    }

    // --------------
    // IMPLEMENTATION
    // --------------


    @Override
    protected void setCallback() {

    }

    @Override
    protected void configureFragmentSettings(Bundle savedInstanceState) {

    }

    @Override
    protected void configureFragmentDesign(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentAccueilBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
    }

    @Override
    protected void saveResultAndThenClose() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}