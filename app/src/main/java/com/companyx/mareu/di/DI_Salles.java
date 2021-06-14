package com.companyx.mareu.di;

import com.companyx.mareu.data.ApiServiceSalles;
import com.companyx.mareu.data.DummyApiServiceSalles;

/**
 * Created by CodeurSteph on 03/06/2021
 */
public class DI_Salles {

    private static ApiServiceSalles apiSalles = new DummyApiServiceSalles();

    public static ApiServiceSalles getServiceSalles() {
        return apiSalles;
    }

    public static ApiServiceSalles getNewInstanceServiceSalles() {
        return new DummyApiServiceSalles();
    }

}
