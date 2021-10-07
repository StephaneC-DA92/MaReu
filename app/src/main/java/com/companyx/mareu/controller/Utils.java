package com.companyx.mareu.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.companyx.mareu.R;
import com.companyx.mareu.controller.activities.AddMeetingActivity;
import com.companyx.mareu.controller.fragments.FragmentAccueil;

public class Utils {
    public static final String TAG_FRAGMENT_MAIN = "TAG_FRAGMENT_MAIN";
    public static final String TAG_FRAGMENT_MEETING = "TAG_FRAGMENT_MEETING";
    public static final String TAG_FRAGMENT_FILTERING = "TAG_FRAGMENT_FILTERING";
    public static final String TAG_FRAGMENT_ACCUEIL = "TAG_FRAGMENT_ACCUEIL";
    public static final String TAG_FRAGMENT_ACTIVITY_MEETING = "TAG_FRAGMENT_ACTIVITY_MEETING";
    public static final String TAG_FRAGMENT_ACTIVITY_FILTERING = "TAG_FRAGMENT_ACTIVITY_FILTERING";

    //Activity codes
    public static final int NEW_MEETING_ACTIVITY_CODE = 98;
    public static final int FILTER_ACTIVITY_CODE = 90;

    //Bundle for navigation to filtering activity
    public static final String BUNDLE_FILTER_DATES = "BUNDLE_FILTER_DATES";

    //Bundle for new meeting : communication of fragment result
    public static final String BUNDLE_EXTRA_MEETING = "com.companyx.mareu.BUNDLE_EXTRA_MEETING";

    //Request keys
    public static final String NEW_MEETING_INTER_FRAGMENTS_KEY = "NEW_MEETING_INTER_FRAGMENTS";
    public static final String NEW_MEETING_ACTIVITY_FRAGMENT_KEY = "NEW_MEETING_ACTIVITY_FRAGMENT";

    public static final String FILTERING_INTER_FRAGMENTS_KEY = "FILTERING_INTER_FRAGMENTS";
    public static final String FILTERING_ACTIVITY_FRAGMENT_KEY = "FILTERING_ACTIVITY_FRAGMENT";

    //Data storage for restoration
    public static final String BUNDLE_FILTER_ROOM = "BUNDLE_FILTER_ROOM";
    public static final String BUNDLE_FILTER_DATE_START = "BUNDLE_FILTER_DATE_START";

    //Data storage for restoration
    public static final String BUNDLE_DISPLAYED_SPECS ="BUNDLE_DISPLAYED_SPECS";

    //Status keys
    public static final String BUNDLE_FRAGMENT_STATUS_KEY ="BUNDLE_FRAGMENT_STATUS_KEY";
    public static final String BUNDLE_ACTIVITY_STATUS_KEY = "BUNDLE_ACTIVITY_STATUS_KEY";
}
