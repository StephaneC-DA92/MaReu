package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;

import java.util.List;
import java.util.Map;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public interface ApiServiceCollaborateurs {

    List<Collaborateur> getListeCollaborateur();

    Map<String, Collaborateur> creerCatalogueParticipant();

    String[] getListeParticipants();
}
