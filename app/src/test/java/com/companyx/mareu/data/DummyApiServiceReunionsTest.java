package com.companyx.mareu.data;

import com.companyx.mareu.di.DI_Collaborateurs;
import com.companyx.mareu.di.DI_Reunions;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by CodeurSteph on 25/05/2021
 */
public class DummyApiServiceReunionsTest {
    private ApiServiceSalles serviceSalles;
    private ApiServiceReunions serviceReunions;
    private ApiServiceCollaborateurs serviceCollaborateurs;

    private static int ITEMS_COUNT = 24;
    private static int ITEMS_COUNT_SALLE = 4;
    private static int ITEMS_COUNT_DEUX_SALLES = 8;
    private static int ITEMS_COUNT_DATE = 12;
    private static int ITEMS_COUNT_SALLE_DATE = 2;

    private static String dateFiltre = "22/05/2021";


    @Before
    public void setup() {
        serviceReunions = DI_Reunions.getNewInstanceServiceReunions();
        serviceSalles = DI_Salles.getNewInstanceServiceSalles();
        serviceCollaborateurs = DI_Collaborateurs.getNewInstanceServiceCollaborateurs();
    }

    @Test
    public void filtrerLieu() {
        List<Salle> salles = serviceSalles.getListeSalle().subList(0,1);
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.filtrerLieu(reunions,salles);
        assertEquals(reunionsf.size(),ITEMS_COUNT_SALLE);
    }

    @Test
    public void filtrerPlusieursLieux() {
        List<Salle> salles = serviceSalles.getListeSalle().subList(0,2);
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.filtrerLieu(reunions,salles);
        assertEquals(reunionsf.size(),ITEMS_COUNT_DEUX_SALLES);
    }

    @Test
    public void filtrerDate() {
        Date mDateFiltre= new DateHeure(dateFiltre).formatParseDate();
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.filtrerDate(reunions,mDateFiltre);
        assertEquals(reunionsf.size(),ITEMS_COUNT_DATE);
    }

    @Test
    public void filtrerDateEtLieu() {
        List<Salle> salles = serviceSalles.getListeSalle().subList(0,1);
        Date mDateFiltre= new DateHeure(dateFiltre).formatParseDate();
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.filtrerLieuEtDate(reunions,salles,mDateFiltre);
        assertEquals(reunionsf.size(),ITEMS_COUNT_SALLE_DATE);
    }

    @Test
    public void trierLieuCroissant() {
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.trierLieuCroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getSalle().getCouleur().valeur() > reunionsf.get(0).getSalle().getCouleur().valeur());
    }

    @Test
    public void trierLieuDecroissant() {
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.trierLieuDecroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getSalle().getCouleur().valeur() < reunionsf.get(0).getSalle().getCouleur().valeur());
    }

    @Test
    public void trierDateDecroissant() {
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.trierHeureDecroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getHeureDebut().compareTo(reunionsf.get(0).getHeureDebut()) < 0);
    }

    @Test
    public void trierDateCroissant() {
        List<Reunion> reunions = serviceReunions.getListeReunions();
        List<Reunion> reunionsf = serviceReunions.trierHeureCroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getHeureDebut().compareTo(reunionsf.get(0).getHeureDebut()) > 0);
    }

    /*//Test traitement de données pour spinner
    @Ignore
    @Test
    public void getListeDate() {
        int nDates =2;
        String[] listeDates = serviceReunions.getListeDate(serviceReunions.getListeReunions());
        assertEquals(listeDates.length,nDates+1);
    }

    //Test jeux de données
    @Ignore
    @Test
    public void creerListeDeReunions() {
        Reunion reunion0 = serviceReunions.getListeReunions().get(0);
        Reunion reunion11 = serviceReunions.getListeReunions().get(23);
        Salle salle0 = serviceSalles.getListeSalle().get(0);
        Salle salle11 = serviceSalles.getListeSalle().get(1);
        assertEquals(reunion0.getSalle().getLieu(),salle0.getLieu());
        assertEquals(reunion11.getSalle().getLieu(),salle11.getLieu());
    }*/

    @Test
    public void ajouterReunion() {
        Salle salle = serviceSalles.getListeSalle().get(0);
        List<Collaborateur> Participants = serviceCollaborateurs.getListeCollaborateur().subList(0,2);
        Collaborateur organisateur = serviceCollaborateurs.getListeCollaborateur().get(0);
        Reunion reunion = new Reunion(salle, "Réunion test ",Participants, new DateHeure("05/06/2021", "10h30").formatParseDateHeure(),new DateHeure("05/06/2021", "11h30").formatParseDateHeure(),organisateur);
        serviceReunions.addReunionItem(reunion);
        assertEquals(serviceReunions.getListeReunions().size(),ITEMS_COUNT+1);
    }

    @Test
    public void supprimerReunion() {
        Reunion reunion = serviceReunions.getListeReunions().get(0);
        serviceReunions.deleteReunionItem(reunion);
        assertEquals(serviceReunions.getListeReunions().size(),ITEMS_COUNT-1);
    }
}