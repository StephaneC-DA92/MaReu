package com.companyx.mareu.data;

import com.companyx.mareu.model.Collaborateur;
import com.companyx.mareu.model.Salle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public class DummyApiServiceCollaborateurs implements ApiServiceCollaborateurs {

    private List<Collaborateur> collaborateurs = GenerateurCollaborateurs.genererCollaborateurs();

    private Map<String, Collaborateur> mCatalogueParticipant;

    @Override
    public List<Collaborateur> getListeCollaborateur() {
        return collaborateurs;
    }

    public Map<String, Collaborateur> creerCatalogueParticipant() {
        mCatalogueParticipant = new HashMap<String, Collaborateur>();
        for (int i = 0; i < collaborateurs.size(); i++) {
            mCatalogueParticipant.put(collaborateurs.get(i).getEmail(), collaborateurs.get(i));
        }
        return mCatalogueParticipant;
    }

    public String[] getListeParticipants() {
        String[] choix = new String[collaborateurs.size()];
        for (int i = 0; i < collaborateurs.size(); i++) {
            choix[i] = collaborateurs.get(i).getEmail();
        }
        return choix;
    }
}
