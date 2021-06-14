package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.model.Salle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CodeurSteph on 11/05/2021
 */
public abstract class GenerateurSalles {

    public static List<Salle> SALLES_LAMZONE = Arrays.asList(
            new Salle("Peach","France",5, Salle.Couleur.Vert, Salle.Icone.Vert),
            new Salle("Mario","France",20, Salle.Couleur.Orange, Salle.Icone.Orange),
            new Salle("Luigi","France",10, Salle.Couleur.Rouge, Salle.Icone.Rouge),
            new Salle("Apple","France",3, Salle.Couleur.Jaune, Salle.Icone.Jaune),
            new Salle("Pomme","Etats-Unis",15, Salle.Couleur.Bleu, Salle.Icone.Bleu),
            new Salle("Pêche","Etats-Unis",50, Salle.Couleur.Orange, Salle.Icone.Orange),
            new Salle("Smurf","Etats-Unis",10, Salle.Couleur.Vert, Salle.Icone.Vert),
            new Salle("Toto","Etats-Unis",100, Salle.Couleur.Rouge, Salle.Icone.Rouge),
            new Salle("Room1","Etats-Unis",3, Salle.Couleur.Jaune, Salle.Icone.Jaune),
            new Salle("DasRoom","Allemagne",12, Salle.Couleur.Bleu, Salle.Icone.Bleu)
            );

    static List<Salle> genererSalles() {
        return new ArrayList<>(SALLES_LAMZONE);
    }
}
