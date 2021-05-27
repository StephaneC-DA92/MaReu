package com.companyx.mareu.model;

import java.io.Serializable;

/**
 * Created by CodeurSteph on 08/05/2021
 */
public class Collaborateur implements Serializable {
    String mPrenom;
    String mNom;
    String mId;
    String mEmail;

    public Collaborateur(String prenom, String nom, String id, String email) {
        this.mPrenom = prenom;
        this.mNom = nom;
        this.mId = id;
        this.mEmail = email;
    }

    public String getPrenom() {
        return mPrenom;
    }

    public void setPrenom(String prenom) {
        mPrenom = prenom;
    }

    public String getNom() {
        return mNom;
    }

    public void setNom(String nom) {
        this.mNom = nom;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
