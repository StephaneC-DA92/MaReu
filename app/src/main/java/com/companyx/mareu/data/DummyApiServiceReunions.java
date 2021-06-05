package com.companyx.mareu.data;

import android.util.Log;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public class DummyApiServiceReunions implements ApiServiceReunions{
    private List<Reunion> mReunions=new ArrayList<Reunion>();
    private Map<String, Date> mCatalogueDate;

    public void initialisationData(){
        mReunions = creerListeDeReunions(12);
    }

    @Override
    public List<Reunion> getListeReunions() {
        return mReunions;
    }

    @Override
    public void addReunionItem(Reunion reunion) {
        mReunions.add(reunion);
    }

    @Override
    public void deleteReunionItem(Reunion reunion) {
        mReunions.remove(reunion);
    }

    //Pattern Etat
    @Override
    public List<Reunion> filtrerLieu(List<Reunion> reunions,List<Salle> salles) {
        List<Reunion> reunionsFiltreesLieu=new ArrayList<Reunion>();
        for(Salle salle : salles) {
            for (Reunion reunion : reunions) {
                if (reunion.getSalle().getLieu()==salle.getLieu()) {
                    reunionsFiltreesLieu.add(reunion);
                }
            }
        }
        return reunionsFiltreesLieu;
    }

    @Override
    public List<Reunion> filtrerDate(List<Reunion> reunions, Date date) {
        List<Reunion> reunionsFiltreesSalle=new ArrayList<Reunion>();
        for(Reunion reunion : reunions){

            DateHeure dh = new DateHeure();
            dh.convertirDateHeureEnDateString(reunion.getHeureDebut());
            Date datedebut = new DateHeure(dh.convertirDateHeureEnDateString(reunion.getHeureDebut())).formatParseDate();

            if (datedebut.compareTo(date)==0) {
                reunionsFiltreesSalle.add(reunion);
            }
        }
        return reunionsFiltreesSalle;
    }

    @Override
    public List<Reunion> filtrerLieuEtDate(List<Reunion> reunions, List<Salle> salles, Date date) {
        return filtrerDate(filtrerLieu(reunions,salles),date);
    }

    @Override
    public List<Reunion> trierLieuCroissant(List<Reunion> reunions) {
//        initial capacity of ten new ArrayList<Reunion>()
        List<Reunion> reunionsTriees=new ArrayList<Reunion>();
        reunionsTriees.addAll(reunions);
        for(int i=0;i<reunionsTriees.size()-1;i++){
            for(int j=0;j<reunionsTriees.size()-1-i;j++) {
                if (reunionsTriees.get(j).getSalle().getCouleur().valeur() > reunionsTriees.get(j+1).getSalle().getCouleur().valeur()) {
                    Collections.swap(reunionsTriees, j, j+1);
                }
            }
        }
        return reunionsTriees;
    }

    @Override
    public List<Reunion> trierLieuDecroissant(List<Reunion> reunions) {
        List<Reunion> reunionsTriees=new ArrayList<Reunion>();
        reunionsTriees.addAll(reunions);
//        Reunion[] tableauReunionsTriees = new Reunion[reunions.size()];
        for(int i=0;i<reunionsTriees.size()-1;i++){
            for(int j=0;j<reunionsTriees.size()-1-i;j++) {
                if (reunionsTriees.get(j).getSalle().getCouleur().valeur() < reunionsTriees.get(j+1).getSalle().getCouleur().valeur()) {
                    Collections.swap(reunionsTriees, j, j+1);
/*                    tableauReunionsTriees[j-1]=reunions.get(j);
                    tableauReunionsTriees[j]=reunions.get(j-1);
                } else {
                    tableauReunionsTriees[j-1]=reunions.get(j-1);
                    tableauReunionsTriees[j]=reunions.get(j);*/
                }
            }
        }
//        List<Reunion> reunionsTriees=Arrays.asList(tableauReunionsTriees);
        return reunionsTriees;
    }

    @Override
    public List<Reunion> trierHeureDecroissant(List<Reunion> reunions) {
        List<Reunion> reunionsTriees=new ArrayList<Reunion>();
        reunionsTriees.addAll(reunions);
        for(int i=0;i<reunionsTriees.size()-1;i++){
            for(int j=0;j<reunionsTriees.size()-1-i;j++) {
                if (reunionsTriees.get(j).getHeureDebut().compareTo(reunionsTriees.get(j+1).getHeureDebut())<0) {
                    Collections.swap(reunionsTriees, j, j+1);
                }
            }
        }
        return reunionsTriees;
    }

    @Override
    public List<Reunion> trierHeureCroissant(List<Reunion> reunions) {
        List<Reunion> reunionsTriees=new ArrayList<Reunion>();
        reunionsTriees.addAll(reunions);
        for(int i=0;i<reunionsTriees.size()-1;i++){
            for(int j=0;j<reunionsTriees.size()-1-i;j++) {
                if (reunionsTriees.get(j).getHeureDebut().compareTo(reunionsTriees.get(j+1).getHeureDebut())>0) {
                    Collections.swap(reunionsTriees, j, j+1);
                }
            }
        }
        return reunionsTriees;
    }

    public static List<Reunion> creerListeDeReunions(int numReunions) {
        List<Reunion> reunions = new ArrayList<Reunion>();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
        ParsePosition pp = new ParsePosition(0);
/*        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        Date mDate = df.parse("3:30pm",pp);*/

        DummyApiServiceSalles salleApiService = new DummyApiServiceSalles();
        DummyApiServiceCollaborateurs collaborateurApiService = new DummyApiServiceCollaborateurs();

        for (int i = 0; i < numReunions; i++) {
//            Salle salle = new Salle("Peach"+i,"France",i+2, Salle.Couleur.Vert);
            int n;
            n=i/10;
            int m = i-n*10;
            Salle salle = salleApiService.getListeSalle().get(m);

                List<Collaborateur> Participants = new ArrayList<Collaborateur>();
//                List<Participant> Participants = new ArrayList<Participant>();
                for (int j = 0; j <= i; j++) {
                    Participants.add(collaborateurApiService.getListeCollaborateur().get(j));
//                    Participants.add((Participant) collaborateurApiService.getListeCollaborateur().get(j));
                }
//            Organisateur organisateur = new Organisateur("OrganisaTest"+i,"DeTesteur"+i,"IdCol-0"+i,i+"Test.DeTesteur@Lamzone.com");
            Collaborateur organisateur = collaborateurApiService.getListeCollaborateur().get(i);

            Reunion reunion1 = new Reunion(salle, "Réunion sujet "+i,Participants, new DateHeure("22/05/2021", i+"h30").formatParseDateHeure(),new DateHeure("22/05/2021", i+1+"h30").formatParseDateHeure(),organisateur);
            Reunion reunion2 = new Reunion(salle, "Réunion sujet "+i+"bis",Participants, new DateHeure("01/06/2021", i+"h30").formatParseDateHeure(),new DateHeure("01/06/2021", i+1+"h30").formatParseDateHeure(),organisateur);

            reunions.add(reunion1);
            reunions.add(reunion2);
/*            Log.e("Réunion générée :",reunions.get(i).getSujet());
            Log.e("Participants générés :",reunions.get(i).getParticipants().toString());*/
        }
        return reunions;
    }

    public String[] getListeDate(List<Reunion> reunions){
        List<Reunion> reunionst = trierHeureCroissant(reunions);
        int n=reunionst.size();
        List<String> lChoix = new ArrayList<String>();

        DateHeure dh = new DateHeure();

        lChoix.add(dh.convertirDateHeureEnDateString(reunionst.get(0).getHeureDebut()));
        if(reunionst.size()>1) {
            for (int i = 1; i < reunionst.size(); i++) {
                if (dh.convertirDateHeureEnDateString(reunionst.get(i ).getHeureDebut()).compareTo(dh.convertirDateHeureEnDateString(reunionst.get(i-1).getHeureDebut()))==0) {
                    n--;
                    continue;
                }
                lChoix.add(dh.convertirDateHeureEnDateString(reunionst.get(i).getHeureDebut()));
            }
        }

        String[] choix = new String[n+1];
        lChoix.add(0,"");
        lChoix.toArray(choix);
//        Log.d("DUMMY_REUNIONS","getListeDate : "+choix.length+"# items : "+(choix.length-1));
        return choix;
    }

}
