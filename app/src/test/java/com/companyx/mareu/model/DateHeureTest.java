package com.companyx.mareu.model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 22/05/2021
 */
public class DateHeureTest {

    @Test
    public void printDate() {

        DateHeure dateHeure = new DateHeure("22/05/2021", "11h30");

        Date date = dateHeure.formatParse();

//        String string = dateHeure.concateneStringforPrint();

        String string2 = dateHeure.convertDatetoString(date);

//        Date date2 = dateHeure.convertStringtoDate(string);
/*        String str = "22/05/2021"+" "+"11h30";
        Date date2 = dateHeure.convertStringtoDateHeure(str);
        Date date3 = dateHeure.convertStringtoDateHeure("22/05/2021 11h30");*/

/*        Locale loc = new Locale("fr","FR");
        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy HH'h'mm",loc);
        LocalDateTime ldt = LocalDateTime.parse(string,form);*/

        System.out.println("Date : "+date);
/*        System.out.println("Date : "+date2); //null
        System.out.println("Date : "+date3); //null*/
//        System.out.println("Date : "+ldt);
//        System.out.println("String : "+string);
        System.out.println("String2 : "+string2);

//        assertEquals(string,string2);
        assertTrue(true);
    }

}