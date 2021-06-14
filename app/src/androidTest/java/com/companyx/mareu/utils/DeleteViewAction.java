package com.companyx.mareu.utils;


import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;


import com.companyx.mareu.R;

import org.hamcrest.Matcher;

public class DeleteViewAction implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Click on Delete button";
    }

    @Override
    public void perform(UiController uiController, View view) {
        View button = view.findViewById(R.id.item_delete_button);
        // Maybe check for null
        if (button != null) {
            button.performClick();
        } else {
            Log.e("Test DeleteViewAction", "No found button!");
        }
    }
}