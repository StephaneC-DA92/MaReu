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
        DummyApiServiceReunions service = new DummyApiServiceReunions();
        service.initialisationData();
        List<Reunion> reunions = service.getListeReunions();
        assertEquals(service.getListeDate(reunions).length,1);
    }

    @Test
    public void getListeDate() {
    }
}