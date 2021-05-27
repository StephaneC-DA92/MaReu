package com.companyx.mareu.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.companyx.mareu.R;

/**
 * Created by CodeurSteph on 14/05/2021
 */

//    ViewHolder personnalisé
public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView mMeetingIcon;
    public TextView mMeetingDetailsText;
    public TextView mMeetingParticipantsText;
    public View mItemView;

    public ImageButton mDeleteButton;

    public ViewHolder(@NonNull View view) {
        super(view);

        mMeetingIcon = view.findViewById(R.id.meeting_icon);
        mMeetingDetailsText = view.findViewById(R.id.meeting_details);
        mMeetingParticipantsText = view.findViewById(R.id.meeting_participants);
        mItemView = view.findViewById(R.id.meeting_frame);
        mDeleteButton = view.findViewById(R.id.item_delete_button);

    }

    //       Getters
    public ImageView getMeetingIcon() {
        return mMeetingIcon;
    }
    public void getDrawable(int resId) {
        getMeetingIcon().setImageResource(resId);
    }

    public TextView getMeetingDetailsText() {
        return mMeetingDetailsText;
    }

    public TextView getMeetingParticipantsText() {
        return mMeetingParticipantsText;
    }
}
