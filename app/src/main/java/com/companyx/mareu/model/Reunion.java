package com.companyx.mareu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by CodeurSteph on 08/05/2021
 */
public class Reunion implements Serializable {
    Salle mSalle;
    String mSujet;
    List<Collaborateur> mParticipants;
    Date mHeureDebut;
    Date mHeureFin;
    Collaborateur mOrganisateur;

    public Reunion(Salle salle, String sujet, List<Collaborateur> participants, Date heureDebut, Date heureFin, Collaborateur organisateur) {
        mSalle = salle;
        mSujet = sujet;
        mParticipants = participants;
        mHeureDebut = heureDebut;
        mHeureFin = heureFin;
        mOrganisateur = organisateur;
    }

    public Salle getSalle() {
        return mSalle;
    }

    public String getSujet() {
        return mSujet;
    }

    //toString() donne [com.....]
    public List<Collaborateur> getParticipants() {
        return mParticipants;
    }

    public String getListeStringParticipants() {
        String str = mParticipants.get(0).getEmail();
        if(mParticipants.size()>1) {
            for (int i = 1; i < mParticipants.size(); i++) {
                str += ", " + mParticipants.get(i).getEmail();
            }
        }
        return str;
    }

    public Date getHeureDebut() {
        return mHeureDebut;
    }

    public Date getHeureFin() {
        return mHeureFin;
    }

    public Collaborateur getOrganisateur() {
        return mOrganisateur;
    }

}
