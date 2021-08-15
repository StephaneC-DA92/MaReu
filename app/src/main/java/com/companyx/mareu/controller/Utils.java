package com.companyx.mareu.controller;

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

    public static final int NEW_MEETING_ACTIVITY_CODE = 98;
    public static final int FILTER_ACTIVITY_CODE = 90;

    public static final String BUNDLE_FILTER_REUNIONS = "BUNDLE_FILTER_REUNIONS";
    public static final String BUNDLE_EXTRA_MEETING = "BUNDLE_EXTRA_MEETING";

    public static final String NEW_MEETING_INTER_FRAGMENTS = "NEW_MEETING_INTER_FRAGMENTS";
    public static final String NEW_MEETING_ACTIVITY_FRAGMENT = "NEW_MEETING_ACTIVITY_FRAGMENT";

    public static final String FILTERING_INTER_FRAGMENTS = "FILTERING_INTER_FRAGMENTS";
    public static final String FILTERING_ACTIVITY_FRAGMENT = "FILTERING_ACTIVITY_FRAGMENT";

    public static final String BUNDLE_FILTER_ROOM = "BUNDLE_FILTER_ROOM";
    public static final String BUNDLE_FILTER_DATE_START = "BUNDLE_FILTER_DATE_START";
}
