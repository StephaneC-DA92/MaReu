package com.companyx.mareu.data;

import android.widget.ArrayAdapter;

import com.companyx.mareu.model.Salle;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 18/05/2021
 */
public class DummyApiServiceSallesTest {

    @Test
    public void getListeLieu() {
        DummyApiServiceSalles service = new DummyApiServiceSalles();
        List<Salle> salles = service.getListeSalle();
        String[] lieux = service.getListeLieu();
        assertEquals(salles.size(),lieux.length);
    }

    @Test
    public void getCatalogueLieu() {
        DummyApiServiceSalles service = new DummyApiServiceSalles();
        Salle salle =  new Salle("Peach","France",5, Salle.Couleur.Vert, Salle.Icone.Vert);
        Salle salleDeReunion  = service.creerCatalogueLieu().get("Peach");
        assertEquals(salle.getLieu(),salleDeReunion.getLieu());
    }
}