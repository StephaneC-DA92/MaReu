package com.companyx.mareu.data;

import com.companyx.mareu.model.Salle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeurSteph on 17/05/2021
 */
public class DummyApiServiceSalles implements ApiServiceSalles {

    private List<Salle> salles = GenerateurSalles.generateRooms();

    private Map<String, Salle> mCatalogueLieu;

    @Override
    public List<Salle> getListeSalle() {
        return salles;
    }

    public Map<String, Salle> createPlaceCatalogue() {
        mCatalogueLieu = new HashMap<String, Salle>();
        for (int i = 0; i < salles.size(); i++) {
            mCatalogueLieu.put(salles.get(i).getLieu(), salles.get(i));
        }
        return mCatalogueLieu;
    }

    public String[] getPlaceList() {
        String[] choix = new String[salles.size()];
        for (int i = 0; i < salles.size(); i++) {
            choix[i] = salles.get(i).getLieu();
        }
        return choix;
    }
}
