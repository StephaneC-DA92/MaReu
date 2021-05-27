package com.companyx.mareu.data;

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
import java.util.List;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public class DummyApiServiceReunions implements ApiServiceReunions{
    private List<Reunion> mReunions=new ArrayList<Reunion>();

    public void initialisationData(){
        mReunions = creerListeDeReunions(3);
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

    @Override
    public int getNombreReunions() {
        return mReunions.size();
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
    public List<Reunion> filtrerHeure(List<Reunion> reunions, Date datedebut, Date datefin) {
        List<Reunion> reunionsFiltreesSalle=new ArrayList<Reunion>();
        for(Reunion reunion : reunions){
            if (reunion.getHeureDebut().compareTo(datedebut)>=0 && reunion.getHeureFin().compareTo(datefin)<=0) {
                reunionsFiltreesSalle.add(reunion);
            }
        }
        return reunionsFiltreesSalle;
    }

    @Override
    public List<Reunion> filtrerLieuEtHeure(List<Reunion> reunions, List<Salle> salles, Date datedebut, Date datefin) {
        return filtrerHeure(filtrerLieu(reunions,salles),datedebut,datefin);
    }

    //    https://openclassrooms.com/fr/courses/4366701-decouvrez-le-fonctionnement-des-algorithmes/4385150-triez-des-informations
    @Override
    public List<Reunion> trierLieuCroissant(List<Reunion> reunions) {
//        initial capacity of ten new ArrayList<Reunion>()
//        https://www.baeldung.com/java-list-capacity-array-size
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
                    tableauReunionsTriees[j]=reunions.get(j-1);*/
                } /*else {
                    tableauReunionsTriees[j-1]=reunions.get(j-1);
                    tableauReunionsTriees[j]=reunions.get(j);
                }*/
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
            Salle salle =salleApiService.getListeSalle().get(i);

                List<Collaborateur> Participants = new ArrayList<Collaborateur>();
//                List<Participant> Participants = new ArrayList<Participant>();
                for (int j = 0; j <= i; j++) {
                    Participants.add(collaborateurApiService.getListeCollaborateur().get(j));
//                    Participants.add((Participant) collaborateurApiService.getListeCollaborateur().get(j));
                }
//            Organisateur organisateur = new Organisateur("OrganisaTest"+i,"DeTesteur"+i,"IdCol-0"+i,i+"Test.DeTesteur@Lamzone.com");
            Collaborateur organisateur = collaborateurApiService.getListeCollaborateur().get(i);

            Reunion reunion = new Reunion(salle, "Réunion sujet "+i,Participants, new DateHeure("22/05/2021", i+"h30").formatParse(),new DateHeure("22/05/2021", "1"+i+"h30").formatParse(),organisateur);

            reunions.add(reunion);
/*            Log.e("Réunion générée :",reunions.get(i).getSujet());
            Log.e("Participants générés :",reunions.get(i).getParticipants().toString());*/
        }
        return reunions;
    }

    public Date valeurDateMin(){
        Date date = null;
        for(int i=0;i<mReunions.size()-1;i++){
            if(mReunions.get(i).getHeureDebut().compareTo(mReunions.get(i+1).getHeureDebut())>0){
                date = mReunions.get(i+1).getHeureDebut();
            }
        }
        return date;
    }

    public Date valeurDateMax(){
        Date date = null;
        for(int i=0;i<mReunions.size()-1;i++){
            if(mReunions.get(i).getHeureDebut().compareTo(mReunions.get(i+1).getHeureDebut())<0){
                date = mReunions.get(i+1).getHeureDebut();
            }
        }
        return date;
    }

}
