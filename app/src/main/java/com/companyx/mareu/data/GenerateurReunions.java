package com.companyx.mareu.data;

import com.companyx.mareu.di.DI_Collaborateurs;
import com.companyx.mareu.di.DI_Salles;
import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeurSteph on 05/06/2021
 */
public abstract class GenerateurReunions {

    private static ApiServiceSalles salleApiService;
    private static ApiServiceCollaborateurs collaborateurApiService;

    public static List<Reunion> REUNIONS_LAMZONE = createMeetingList(12);

    private static List<Reunion> createMeetingList(int numReunions) {
        List<Reunion> reunions = new ArrayList<Reunion>();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
        ParsePosition pp = new ParsePosition(0);

        salleApiService = DI_Salles.getServiceSalles();
        collaborateurApiService = DI_Collaborateurs.getServiceCollaborateurs();

        for (int i = 0; i < numReunions; i++) {

            Salle salle = generateRoom(i);

            List<Collaborateur> Participants = generateParticipants(i);

            Collaborateur organisateur = collaborateurApiService.getListeCollaborateur().get(i);

            Reunion reunion1 = new Reunion(salle, "Réunion A" + i, Participants, new DateHeure("22/05/2021", i + "h30").formatParseDateHeure(), new DateHeure("22/05/2021", i + 1 + "h30").formatParseDateHeure(), organisateur);
            Reunion reunion2 = new Reunion(salle, "Réunion B" + i, Participants, new DateHeure("01/06/2021", i + "h30").formatParseDateHeure(), new DateHeure("01/06/2021", i + 1 + "h30").formatParseDateHeure(), organisateur);

            reunions.add(reunion1);
            reunions.add(reunion2);

        }
        return reunions;
    }

    static List<Reunion> generateMeetings() {
        return new ArrayList<>(REUNIONS_LAMZONE);
    }

    private static Salle generateRoom(int i){
        int n;
        n = i / 10;
        int m = i - n * 10;
        Salle salle = salleApiService.getListeSalle().get(m);

        return salle;
    }

    private static List<Collaborateur> generateParticipants(int i){
        List<Collaborateur> Participants = new ArrayList<Collaborateur>();
        for (int j = 0; j <= i; j++) {
            Participants.add(collaborateurApiService.getListeCollaborateur().get(j));
        }
        return Participants;
    }
}
