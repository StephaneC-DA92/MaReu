package com.companyx.mareu.model;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 22/05/2021
 */
public class DateHeureTest {

    @Test
    public void formatParseDateHeure() {
        DateHeure dh = new DateHeure("22/05/2021", "01h02");
        Date date1 = dh.formatParseDateHeure();

        Calendar mCalendrier = Calendar.getInstance();
        mCalendrier.clear();
        mCalendrier.set(Calendar.YEAR, 2021);
        mCalendrier.set(Calendar.MONTH, 5 - 1);
        mCalendrier.set(Calendar.DAY_OF_MONTH, 22);
        mCalendrier.set(Calendar.HOUR_OF_DAY, 1);
        mCalendrier.set(Calendar.MINUTE, 2);
        Date date2 = mCalendrier.getTime();

        assertTrue(date1.compareTo(date2) == 0);
    }

    @Test
    public void formatParseDate() {
        DateHeure dh2 = new DateHeure("22/05/2021");
        Date date1 = dh2.formatParseDate();

        Calendar mCalendrier = Calendar.getInstance();
        mCalendrier.clear();
        mCalendrier.set(Calendar.YEAR, 2021);
        mCalendrier.set(Calendar.MONTH, 5 - 1);
        mCalendrier.set(Calendar.DAY_OF_MONTH, 22);
        Date date2 = mCalendrier.getTime();

        assertTrue(date1.compareTo(date2) == 0);
    }

    @Test
    public void convertirDateHeureEnString() {
        DateHeure dh = new DateHeure("22/05/2021", "01h02");
        Date date1 = dh.formatParseDateHeure();
        String str1 = dh.convertirDateHeureEnString(date1);

        assertEquals(str1, "22/05/2021 01h02");
    }

    @Test
    public void convertirDateHeureEnDateString() {
        DateHeure dh = new DateHeure("22/05/2021", "01h02");
        Date date1 = dh.formatParseDateHeure();
        String str2 = dh.convertirDateHeureEnDateString(date1);

        assertEquals(str2, "22/05/2021");
    }

    @Test
    public void concateneStringforPrint() {
        DateHeure dh = new DateHeure("22/05/2021", "01h02");
        String str3 = dh.concateneStringforPrint();

        assertEquals(str3, "22/05/2021 01h02");
    }
}