package com.companyx.mareu.model;

import com.companyx.mareu.R;

import java.io.Serializable;

/**
 * Created by CodeurSteph on 08/05/2021
 */
public class Salle implements Serializable {
    String mLieu;
    String mPays;
    int mPlaces;
    Couleur mCouleur;
    Icone mIcone;

    public enum Couleur {
        Vert(1), Orange(2), Rouge(3), Jaune(4), Bleu(5);

        //        correspondance : couleur - valeur entier
        int mCouleur;

        //        Constructeur
        private Couleur(int valeur) {
            mCouleur = valeur;
        }

        //        Méthode
        public int valeur() {
            return mCouleur;
        }
    }

    public enum Icone {
        Vert(R.drawable.ic_baseline_circle_green_48), Orange(R.drawable.ic_baseline_circle_orange_48),
        Rouge(R.drawable.ic_baseline_circle_red_48), Jaune(R.drawable.ic_baseline_circle_yellow_48),
        Bleu(R.drawable.ic_baseline_circle_blue_48);

        //        correspondance : couleur - valeur entier
        int mIcone;

        //        Constructeur
        private Icone(int valeur) {
            mIcone = valeur;
        }

        //        Méthode
        public int valeur() {
            return mIcone;
        }
    }

    public Salle(String lieu, String pays, int places, Couleur couleur, Icone icone) {
        mLieu = lieu;
        mPays = pays;
        mPlaces = places;
        mCouleur = couleur;
        mIcone = icone;
    }

    public Couleur getCouleur() {
        return mCouleur;
    }

    public String getLieu() {
        return mLieu;
    }

    public Icone getIcone() {
        return mIcone;
    }

    public int getPlaces() {
        return mPlaces;
    }
}
