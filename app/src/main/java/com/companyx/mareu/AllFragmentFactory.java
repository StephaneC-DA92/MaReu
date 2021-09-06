package com.companyx.mareu;

import android.util.Log;

import com.companyx.mareu.controller.fragments.AddMeetingFragment;
import com.companyx.mareu.controller.fragments.FilteringFragment;
import com.companyx.mareu.controller.fragments.FragmentAccueil;
import com.companyx.mareu.controller.fragments.MainFragment;

import java.io.Serializable;

public class AllFragmentFactory implements Serializable {

    public enum FragmentType{
        MainFragment,
        FragmentAccueil,
        AddMeetingFragment,
        FilteringFragment
    }

    /*public interface AppFragment {
        public void configureFragmentSettings();
    }

    public static AppFragment makeFragment(FragmentType type){
        AppFragment appFragment = null;
        switch(type){
            case MainFragment:
                appFragment = new MainFragment();
                break;
            case FragmentAccueil:
                appFragment =  new FragmentAccueil();
                break;
            case AddMeetingFragment:
                appFragment =  new AddMeetingFragment();
                break;
            case FilteringFragment:
                appFragment =  new FilteringFragment();
                break;
        }

        Log.d("Factory","new Fragment " + appFragment.getClass().toString());

        return appFragment;
    }*/

}
