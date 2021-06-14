package com.companyx.mareu.di;

import com.companyx.mareu.data.ApiServiceReunions;
import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.data.DummyApiServiceReunions;
import com.companyx.mareu.data.DummyApiServiceSalles;

/**
 * Created by CodeurSteph on 03/06/2021
 */
public class DI_Reunions {

    private static ApiServiceReunions apiReunions = new DummyApiServiceReunions();

    public static ApiServiceReunions getServiceReunions() {
        return apiReunions;
    }

    public static ApiServiceReunions getNewInstanceServiceReunions() {
        return new DummyApiServiceReunions();
    }
}
