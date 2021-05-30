package com.companyx.mareu.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CodeurSteph on 18/05/2021
 */

//Stocke données au format Date et fournit jour au format Date
//Convertit Date au format String selon pattern défini
//Extrait le jour d'un objet Date

public class DateHeure implements Serializable {

    final Calendar mCalendrier = Calendar.getInstance();
//    int mAnnee,mMois,mJour, mHeure,mMinutes;

    String mDateString,mHeureString;

    String pattern1 = "dd/MM/yyyy HH'h'mm";
    String pattern2 = "dd/MM/yyyy";

    SimpleDateFormat formatDateHeure = new SimpleDateFormat(pattern1);
    SimpleDateFormat formatDate = new SimpleDateFormat(pattern2);

    ParsePosition pp = new ParsePosition(0);

/*    public DateHeure(int annee, int mois,  int jour, @Nullable int heure, @Nullable int minutes) {
        mAnnee = annee;
        mMois = mois;
        mJour = jour;
        mHeure = heure;
        mMinutes = minutes;
    }*/

 /*   public Date DateHeure() {
        mCalendrier.clear();
        mCalendrier.set(Calendar.YEAR,mAnnee);
        mCalendrier.set(Calendar.MONTH,mMois-1);
        mCalendrier.set(Calendar.DAY_OF_MONTH,mJour);
        mCalendrier.set(Calendar.HOUR_OF_DAY, mHeure);
        mCalendrier.set(Calendar.MINUTE,mMinutes);
        return mCalendrier.getTime();
    }

    public String DateHeureToString() {
        return  mJour + "/" + mMois + "/" + mAnnee + " " + mHeure + "h" + mMinutes;
    }*/
    //Alternative
    public DateHeure(String date, String heure) {
        mDateString = date;
        mHeureString = heure;
    }

    public DateHeure(String date) {
        mDateString = date;
    }

    public DateHeure() {
    }

    //Date internationale. Ex. : Sat May 22 11:30:00 CEST 2021. TODO : date française
    public Date formatParseDateHeure() {
        return convertStringtoDateHeure(mDateString+" "+mHeureString);
    }
    public Date convertStringtoDateHeure(String str){
        return formatDateHeure.parse(str,pp);
    }

    //Date internationale. Ex. : Sat May 22 00:00:00 CEST 2021. TODO : date française
    public Date formatParseDate() {
        return convertStringtoDate(mDateString);
    }
    public Date convertStringtoDate(String str){
        return formatDate.parse(str,pp);
    }

    public String convertirDateHeureEnString(Date dateHeure){
        mCalendrier.clear();
        mCalendrier.setTime(dateHeure);
        return String.format("%02d",mCalendrier.get(Calendar.DAY_OF_MONTH))
                +"/"+String.format("%02d",(mCalendrier.get(Calendar.MONTH)+1))
                +"/"+mCalendrier.get(Calendar.YEAR)
                +" "+String.format("%02d",mCalendrier.get(Calendar.HOUR_OF_DAY))
                +"h"+String.format("%02d",mCalendrier.get(Calendar.MINUTE));
    }

    public String convertirDateHeureEnDateString(Date dateHeure){
        mCalendrier.clear();
        mCalendrier.setTime(dateHeure);
        return String.format("%02d",mCalendrier.get(Calendar.DAY_OF_MONTH))
                +"/"+String.format("%02d",(mCalendrier.get(Calendar.MONTH)+1))
                +"/"+mCalendrier.get(Calendar.YEAR);
    }

    public String concateneStringforPrint(){
        return mDateString+" "+mHeureString;
    }

    /*    DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneOffset.UTC);
return fmtOut.format(time);*/

/*        Locale dateHeureLocale = new Locale("fr", "FR");
        formatDateHeure.applyPattern(pattern);

    SimpleDateFormat formatDateHeure = new SimpleDateFormat(pattern,Locale.FRANCE);
    formatDateHeure.setTimeZone(TimeZone.getDefault());

    Convertir String en Date*/
/*    DateTimeFormatter formatFr = DateTimeFormatter.ofPattern("dd/MM/yyyy HH'h'mm",dateHeureLocale);
    LocalDateTime ldt = LocalDateTime.parse(string,formatFr);*/

    /*    public DateFormat dfd = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.FRANCE);
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
        Date date = mediumDateFormat.parse(string,pp);
        String string = mediumDateFormat.format(date);
    }*/
}
