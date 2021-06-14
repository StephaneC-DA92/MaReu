package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.Salle;

import java.util.List;
import java.util.Map;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public interface ApiServiceSalles {

    List<Salle> getListeSalle();

    Map<String, Salle> creerCatalogueLieu();

    String[] getListeLieu();

}
