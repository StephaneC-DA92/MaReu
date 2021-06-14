package com.companyx.mareu.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.companyx.mareu.R;
import com.companyx.mareu.databinding.FragmentAccueilBinding;
import com.companyx.mareu.databinding.FragmentAddMeetingBinding;

public class FragmentAccueil extends Fragment {

    private FragmentAccueilBinding mBinding;
    private View mView;

    public FragmentAccueil() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAccueilBinding.inflate(inflater, container, false);
        mView = mBinding.getRoot();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}