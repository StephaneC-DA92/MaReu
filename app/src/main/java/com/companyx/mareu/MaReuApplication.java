package com.companyx.mareu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class MaReuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try{
            ManageSharedPreferences();
        } catch (Exception e) {
            Log.e("MaReuApplication","onCreate : "+e);
        }
    }

    // --------------
    // REINIT
    // --------------

    private void ManageSharedPreferences(){

        if(getSharedPreferences(getString(R.string.filtering_preference_file_key), Context.MODE_PRIVATE)!=null) {
            SharedPreferences FilteringSharedPref = getSharedPreferences(getString(R.string.filtering_preference_file_key), Context.MODE_PRIVATE);

            if (FilteringSharedPref.contains(getString(R.string.filering_dateDebut_key))
                    || FilteringSharedPref.contains(getString(R.string.filering_room_key))) {

                FilteringSharedPref.edit().clear().commit(); // vider les préférences

                Toast.makeText(this, "Initialisation en cours 1", Toast.LENGTH_LONG).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    deleteSharedPreferences(getString(R.string.filtering_preference_file_key)); //supprimer les préférences
                }
            }
        }

        if(getSharedPreferences(getString(R.string.addmeeting_preference_file_key),Context.MODE_PRIVATE)!=null) {
            SharedPreferences AddMeetingSharedPref = getSharedPreferences(getString(R.string.addmeeting_preference_file_key), Context.MODE_PRIVATE);

            if (AddMeetingSharedPref.contains(getString(R.string.addmeeting_sujet_key))) {

                AddMeetingSharedPref.edit().clear().commit(); // vider les préférences

                Toast.makeText(this, "Initialisation en cours 2", Toast.LENGTH_LONG).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    deleteSharedPreferences(getString(R.string.addmeeting_preference_file_key)); //supprimer les préférences
                }
            }
        }

    }

}
