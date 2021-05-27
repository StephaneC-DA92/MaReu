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

    int getNombreReunions();

    List<Reunion> filtrerLieu(List<Reunion> reunions, List<Salle> salles);

    List<Reunion> filtrerHeure(List<Reunion> reunions, Date datedebut, Date datefin);

    List<Reunion> filtrerLieuEtHeure(List<Reunion> reunions, List<Salle> salles, Date datedebut, Date datefin);

    List<Reunion> trierLieuCroissant(List<Reunion> reunions);
    List<Reunion> trierLieuDecroissant(List<Reunion> reunions);

    List<Reunion> trierHeureCroissant(List<Reunion> reunions);
    List<Reunion> trierHeureDecroissant(List<Reunion> reunions);
}
