# OpenClassrooms
Ce dépôt contient une application pour le P4 du parcours **PRFE3**.
# MaRéu

![MaRéu](app/src/main/res/ic_logo_accueil.png)

## Table des matières
1. [Informations générales]
2. [Technologies]
3. [Tests]
4. [Screenshots]
5. [Crédits]

## Informations générales
Nom du projet : **MaRéu**   
Version : 2.0
***
Application de gestion de réunions   

### Fonctionnalités :
* Affichage de liste de réunions comprenant :
	* l’heure de la réunion ;
	* le lieu de la réunion ;
	* le sujet de la réunion ;
	* la liste des participants (adresses mail).   
* Ajout d’une réunion
* Suppression d’une réunion
* Filtre des réunions par date ou par lieu
* Gestion de l’affichage responsive sur toutes les tailles de téléphone et de tablette Android, en modes portrait et paysage.


## Technologies
Liste des technologies utilisées : 
* [Android Studio](https://developer.android.com/studio/) : Version 4.1.3
* [viewBinding](https://developer.android.com/topic/libraries/view-binding) : since Android Studio 3.6
* [Gradle](https://developer.android.com/studio/releases/gradle-plugin) : Version 4.1.3
* [EventBus](https://greenrobot.org/eventbus/) : Version 3.2.0
* [JUnit](https://github.com/junit-team/junit4/wiki) : Version 4.13
* [Espresso](https://developer.android.com/training/testing/espresso) : Version 3.3.0

Notes sur les méthodes utilisées :   
* Communication entre viewHolder et fragment : via EventBus    
* Communication entre fragments : FragmentManager pour transfert de lot de données via Bundle    
* Communication entre fragment et activité : FragmentManager pour transfert de lot de données via Bundle ; via callback pour appliquer une méthode   
* Gestion de changement de configuration : SharedPreferences pour sauvegarde temporaire de données individuelles partagées   
* Stockage de données : via Bundle et SharedPreferences

### Démarrage
Version de SdK min : 21
Pour une application supportant Android 5.0 (API21) et ses versions supérieures

### Statut :
Prototype en cours de développement

#### Modifications possibles : roadmap
* Gestion des exceptions pour le formulaire du filtre  
* Gestion des tentatives de création de doublon   
* Gestion des places disponibles en salle pour création de réunion   
* Spécialisation pour les classes activités et fragments   

### Auteur :
Stéphane C

## Screenshots
 
* L'utilisateur peut accéder aux fonctionnalités de filtre ou de tri (mode portrait). 
 
    <div style="display:flex;" >
    <img src="/menu_portrait.gif" width="24%">
    </div> 
  
* L'utilisateur peut filtrer l'affichage (mode paysage). 
   
    <div style="display:flex;" >
    <img src="/filtre_paysage.gif" width="24%">
    </div>   
  
* L'utilisateur peut accéder ajouter une réunion et trier l'affichage (mode paysage). 
   
    <div style="display:flex;" >
    <img src="/ajout_paysage.gif" width="24%">
    <img src="/tri_paysage.gif" width="24%" style="margin-left:10px;" >
    </div>     

* Les données saisies par l'utilisateur sont automatiquement sauvegardées après changement d'orientation. 
   
    <div style="display:flex;" >
    <img src="/sauvegarde_portrait_paysage.gif" width="24%">
    </div>   
       
* L'utilisateur supprime une réunion (mode portrait).

    <div style="display:flex;" >
    <img src="/suppression_portrait.gif" width="24%">
    </div> 

## Tests
* Tests unitaires
    * com\companyx\mareu\data\DummyApiServiceReunionsTest.java
        * filtrerLieu()
        * filtrerPlusieursLieux()
        * filtrerDate()
        * filtrerDateEtLieu()
        * ajouterReunion()
        * supprimerReunion()            


* Tests instrumentalisés
    * com\companyx\mareu\controller\activities\MainActivityTest.java
        * onMainActivityReunionListShouldBeDisplayed()
        * onDeleteClickReunionListShouldBeDisplayedMinusITem()
        * onNoFilterActionRawListShouldBeDisplayed()

    * com\companyx\mareu\controller\activities\AddMeetingActivityTest.java
	    * onValidatingNewMeetingReunionListShouldDisplayExtraItem()

    * com\companyx\mareu\controller\activities\AddMeetingActivityTest_ForcingExecution.java
	    * onCancellingNewMeetingReunionListShouldDisplayNoExtraItem()

    * com\companyx\mareu\controller\activities\FilteringActivityTest.java
        * onOneRoomFilteredActionFilteredListShouldBeDisplayed()
        * onManyRoomFilteredActionFilteredListShouldBeDisplayed()
        * onRoomAndDateFilteredActionFilteredListShouldBeDisplayed()
        * onFilterCancellationOriginalListShouldBeDisplayed()
        * onDateFilteredActionFilteredListShouldBeDisplayed()

## Crédits
Les guides et questions/réponses disponibles aux URL suivants:
* https://devstory.net/10559/android-autocompletetextview-multiautocompletetextview
* https://www.jmdoudoux.fr/java/dej/chap-utilisation_dates.htm#utilisation_dates-2
* https://stackoverflow.com/questions/43149728/select-date-from-calendar-in-android-espresso


