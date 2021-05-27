package com.companyx.mareu.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by CodeurSteph on 18/05/2021
 */

//        https://www.jmdoudoux.fr/java/dej/chap-utilisation_dates.htm
//        https://www.journaldev.com/17899/java-simpledateformat-java-date-format
//        https://www.baeldung.com/java-string-to-date

public class DateHeure implements Serializable {

    String mDate,mHeure;

    //Convertir Date en String
    Locale dateHeureLocale = new Locale("fr", "FR");
    String pattern = "dd/MM/yyyy HH'h'mm";
//    String pattern2 = "dd/MM/yyyy HH:mm";
    SimpleDateFormat formatDateHeure = new SimpleDateFormat(pattern);

/*    DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneOffset.UTC);
return fmtOut.format(time);*/


//    SimpleDateFormat formatDateHeure = new SimpleDateFormat(pattern,Locale.FRANCE);
//    formatDateHeure.setTimeZone(TimeZone.getDefault());

//    Convertir String en Date
/*    DateTimeFormatter formatFr = DateTimeFormatter.ofPattern("dd/MM/yyyy HH'h'mm",dateHeureLocale);
    LocalDateTime ldt = LocalDateTime.parse(string,formatFr);*/
    ParsePosition pp = new ParsePosition(0);

    public DateHeure(String date, String heure) {
        mDate = date;
        mHeure = heure;
    }

    public DateHeure() {
    }

    public Date formatParse() {
        return formatDateHeure.parse(mDate+" "+mHeure,pp);
    }

    public String convertDatetoString(Date dateHeure){
        return formatDateHeure.format(dateHeure);
    }

    //Date internationale. Ex. : Sat May 22 11:30:00 CEST 2021
//    voir https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html#parse(java.lang.String,java.text.ParsePosition)
//    https://webdevdesigner.com/q/simpledateformat-and-locale-based-format-string-14092/

    public Date convertStringtoDate(String str){
//        formatDateHeure.applyPattern(pattern);
        return formatDateHeure.parse(str,pp);
    }

/*    public int compareToDateHeure(DateHeure dateheure2){
        return formatParse().compareTo(dateheure2.formatParse());
    }*/

//TODO : date française
/*    public LocalDateTime convertStringtoDateHeureFr(String str){
        return LocalDateTime.parse(str,formatFr);
    }*/

    public String concateneStringforPrint(){
        return mDate+" "+mHeure;
    }

    @Override
    public String toString() {
        return "DateHeure{" +
                "mDate='" + mDate + '\'' +
                ", mHeure='" + mHeure + '\'' +
                '}';
    }

    /*    public DateFormat dfd = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.FRANCE);

        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(                DateFormat.MEDIUM,                DateFormat.MEDIUM);

        Date date = mediumDateFormat.parse(string,pp);
        String string = mediumDateFormat.format(date);
    }*/

    //Liste de choix pour heure
}
