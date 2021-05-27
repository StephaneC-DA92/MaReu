package com.companyx.mareu.data;

import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 25/05/2021
 */
public class DummyApiServiceReunionsTest {

    @Test
    public void filtrerLieu() {
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        List<Salle> salles = Arrays.asList(
                new Salle("Peach","France",5, Salle.Couleur.Vert, Salle.Icone.Vert));
        List<Reunion> reunions = service.getListeReunions();
        List<Reunion> reunionsf = service.filtrerLieu(reunions,salles);
        assertEquals(reunionsf.size(),1);
    }

    @Test
    public void filtrerHeure() {
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        Date datedebut = new DateHeure("22/05/2021", "1h30").formatParse();
        Date datefin = new DateHeure("22/05/2021", "11h30").formatParse();
        List<Reunion> reunions = service.getListeReunions();
        List<Reunion> reunionsf = service.filtrerHeure(reunions,datedebut, datefin);
        assertEquals(reunionsf.size(),1);
    }

    @Test
    public void filtrerLieuEtHeure() {
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        List<Salle> salles = Arrays.asList(
                new Salle("Peach","France",5, Salle.Couleur.Vert, Salle.Icone.Vert));
        List<Salle> salleso = Arrays.asList(
                new Salle("Mario","France",20, Salle.Couleur.Orange, Salle.Icone.Orange));
        Date datedebut = new DateHeure("22/05/2021", "1h30").formatParse();
        Date datefin = new DateHeure("22/05/2021", "11h30").formatParse();
        List<Reunion> reunions = service.getListeReunions();
        List<Reunion> reunionsf = service.filtrerLieuEtHeure(reunions,salles,datedebut, datefin);
        assertEquals(reunionsf.size(),0);
        List<Reunion> reunionsfo = service.filtrerLieuEtHeure(reunions,salleso,datedebut, datefin);
        assertEquals(reunionsfo.size(),1);
    }

    @Test
    public void trierLieuCroissant() {
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        List<Reunion> reunions = service.getListeReunions();
        List<Reunion> reunionsf = service.trierLieuCroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getSalle().getCouleur().valeur() > reunionsf.get(0).getSalle().getCouleur().valeur());
    }

    @Test
    public void trierLieuDecroissant() {
    }

    @Test
    public void trierHeureDecroissant() {
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        List<Reunion> reunions = service.getListeReunions();
        List<Reunion> reunionsf = service.trierHeureDecroissant(reunions);
        assertTrue(reunionsf.get(reunions.size()-1).getHeureDebut().compareTo(reunionsf.get(0).getHeureDebut()) < 0);
    }

    @Test
    public void trierHeureCroissant() {

    }

}