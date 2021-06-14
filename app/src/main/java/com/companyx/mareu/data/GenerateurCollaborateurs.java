package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.Reunion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by CodeurSteph on 11/05/2021
 */

//Optionnel
    //Service Generator
public abstract class GenerateurCollaborateurs {

   public static List<Collaborateur> COLLABORATEURS_LAMZONE = Arrays.asList(
           new Collaborateur("Alexandra","Artaud","IdCol-1","Alexandra.Artaud@Lamzone.com"),
           new Collaborateur("André","Boudet","IdCol-2","André.Boudet@Lamzone.com"),
           new Collaborateur("Béatrice","Cornet","IdCol-3","Béatrice.Cornet@Lamzone.com"),
           new Collaborateur("Bernard","Dali","IdCol-4","Bernard.Dali@Lamzone.com"),
           new Collaborateur("Caroline","Estot","IdCol-5","Caroline.Estot@Lamzone.com"),
           new Collaborateur("Carlos","Fanon","IdCol-6","Carlos.Fanon@Lamzone.com"),
           new Collaborateur("Erica","Granier","IdCol-7","Erica.Granier@Lamzone.com"),
           new Collaborateur("Eric","Hardy","IdCol-8","Eric.Hardy@Lamzone.com"),
           new Collaborateur("Fanny","Istar","IdCol-9","Fanny.Istar@Lamzone.com"),
           new Collaborateur("Francis","Jourdain","IdCol-10","Francis.Jourdain@Lamzone.com"),
           new Collaborateur("Sophie","Telle","IdCol-11","Sophie.Telle@Lamzone.com"),
           new Collaborateur("Sylvain","Dien","IdCol-12","Sylvain.Dien@Lamzone.com"),
           new Collaborateur("Zoé","Valda","IdCol-13","Zoé.Valda@Lamzone.com"),
           new Collaborateur("Zorba","Legrec","IdCol-14","Zorba.Legrec@Lamzone.com")
           );

    /*private void creerCollaborateurs (){
        for(int i=0; i<10;i++){
            COLLABORATEURS_LAMZONE.add(new Collaborateur(Prenoms.get(i),Noms.get(i),"Id"+i,Prenoms.get(i)+"."+Noms.get(i)+"@lamzone.com"));
        }
    }
*/

    static List<Collaborateur> genererCollaborateurs() {
        return new ArrayList<>(COLLABORATEURS_LAMZONE);
    }

}
