package com.companyx.mareu.di;

import com.companyx.mareu.data.ApiServiceCollaborateurs;
import com.companyx.mareu.data.ApiServiceReunions;
import com.companyx.mareu.data.DummyApiServiceCollaborateurs;
import com.companyx.mareu.data.DummyApiServiceReunions;

/**
 * Created by CodeurSteph on 05/06/2021
 */
public class DI_Collaborateurs {

    private static ApiServiceCollaborateurs apiCollaborateurs = new DummyApiServiceCollaborateurs();

    public static ApiServiceCollaborateurs getServiceCollaborateurs() {
        return apiCollaborateurs;
    }

    public static ApiServiceCollaborateurs getNewInstanceServiceCollaborateurs() {
        return new DummyApiServiceCollaborateurs();
    }
}
