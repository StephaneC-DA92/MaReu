package com.companyx.mareu.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.companyx.mareu.R;
import com.companyx.mareu.events.DeleteMeetingEvent;
import com.companyx.mareu.model.DateHeure;
import com.companyx.mareu.model.Reunion;
import com.companyx.mareu.view.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by CodeurSteph on 14/05/2021
 */
public class MeetingListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Reunion> mListeDeReunions;

    //    Constructeur de l'adapter
    public MeetingListAdapter(List<Reunion> listeDeReunions) {
        mListeDeReunions = listeDeReunions;
    }

    //Crée les vues appelées par le layout manager
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reunion_item, parent, false);
        return new ViewHolder(view);
    }

    //    Fournit le contenu de la vue VH
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Reunion reunion = mListeDeReunions.get(position);

        viewHolder.setDrawable(reunion.getSalle().getIcone().valeur());

        viewHolder.getMeetingDetailsText()
                .setText(reunion.getSujet() + " - " + new DateHeure().convertirDateHeureEnString(reunion.getHeureDebut()) + " - " + reunion.getSalle().getLieu());
//        Log.d("Détails réunion", reunion.getSujet() + " - " + new DateHeure().convertirDateHeureEnString(reunion.getHeureDebut()) + " - " + reunion.getSalle().getLieu());

        viewHolder.getMeetingParticipantsText()
                .setText(reunion.getListeStringParticipants());
//        Log.d("Liste participants", reunion.getListeStringParticipants());

        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(reunion));
//                Log.d("Suppression", "Réunion item : -1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListeDeReunions.size();
    }
}
