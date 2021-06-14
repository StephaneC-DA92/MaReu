package com.companyx.mareu.data;

import android.widget.ArrayAdapter;

import com.companyx.mareu.di.DI_Reunions;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.model.Salle;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 18/05/2021
 */
public class DummyApiServiceSallesTest {
    private ApiServiceSalles serviceSalles;
    private ApiServiceReunions serviceReunions;

    @Before
    public void setup() {
        serviceReunions = DI_Reunions.getNewInstanceServiceReunions();
        serviceSalles = DI_Salles.getNewInstanceServiceSalles();
    }

    @Ignore
    @Test
    public void getListeLieu() {
        List<Salle> salles = serviceSalles.getListeSalle();
        String[] lieux = serviceSalles.getListeLieu();
        assertEquals(salles.size(), lieux.length);
    }

    @Ignore
    @Test
    public void getCatalogueLieu() {
        Salle salle = new Salle("Peach", "France", 5, Salle.Couleur.Vert, Salle.Icone.Vert);
        Salle salleDeReunion = serviceSalles.creerCatalogueLieu().get("Peach");
        assertEquals(salle.getLieu(), salleDeReunion.getLieu());
    }
}