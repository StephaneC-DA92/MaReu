package com.companyx.mareu.model;

import android.util.Log;

import com.companyx.mareu.data.DummyApiServiceReunions;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 14/05/2021
 */
public class ReunionTest {

    @Test
    public void creerListeDeReunions() {
        List<Reunion> reunions = new DummyApiServiceReunions().getListeReunions();
        assertEquals(24, reunions.size());
        assertEquals(1, reunions.get(0).getParticipants().size());
        assertEquals(2, reunions.get(2).getParticipants().size());
        assertEquals(3, reunions.get(4).getParticipants().size());

    }

    @Test
    public void listeDates() {
        DummyApiServiceReunions mDummyApiServiceReunions = new DummyApiServiceReunions();
        List<Reunion> reunions = mDummyApiServiceReunions.getListeReunions();
        String[] dates = mDummyApiServiceReunions.getListeDate(reunions);
        assertEquals(dates.length, 3);
    }
}