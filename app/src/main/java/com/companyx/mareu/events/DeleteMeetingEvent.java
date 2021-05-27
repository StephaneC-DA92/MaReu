package com.companyx.mareu.events;

/**
 * Created by CodeurSteph on 27/05/2021
 */

import com.companyx.mareu.model.Reunion;

/**
 * Evènement déclenché quand on supprime une réunion
 */
public class DeleteMeetingEvent {
    public Reunion reunion;

    public DeleteMeetingEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
