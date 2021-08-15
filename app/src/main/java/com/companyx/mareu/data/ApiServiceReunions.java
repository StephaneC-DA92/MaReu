package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.util.Date;
import java.util.List;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public interface ApiServiceReunions {

    List<Reunion> getListeReunions();

    void addReunionItem(Reunion reunion);

    void deleteReunionItem(Reunion reunion);

    List<Reunion> filterPlace(List<Reunion> reunions, List<Salle> salles);

    List<Reunion> filterDate(List<Reunion> reunions, Date date);

    List<Reunion> filterPlaceDate(List<Reunion> reunions, List<Salle> salles, Date date);

    List<Reunion> sortPlaceUp(List<Reunion> reunions);

    List<Reunion> sortPlaceDown(List<Reunion> reunions);

    List<Reunion> sortTimeDown(List<Reunion> reunions);

    List<Reunion> sortTimeUp(List<Reunion> reunions);

    String[] getListeDate(List<Reunion> reunions);
}
