package com.companyx.mareu.controller.activities;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;

import com.companyx.mareu.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.companyx.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by CodeurSteph on 28/05/2021
 */
public class AddMeetingActivityTest_ForcingExecution {
    private MainActivity mActivity;

    private static int ITEMS_COUNT = 24;

    private String sujet = "Reunion test";
    private String salle = "Peach";

    private String nom1 = "Alexandra.Artaud";
    private String nom2 = "Bernard.Dali";
    private String nom3 = "Caroline.Estot";
    private String email1 = "Alexandra.Artaud@Lamzone.com";
    private String email2 = "Bernard.Dali@Lamzone.com";
    private String email3 = "Caroline.Estot@Lamzone.com";


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        //Essayer DI ici
      /*  Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);*/
        mActivity = mActivityTestRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @After
    public void tearDown() throws Exception {
        /*mActivityTestRule.finishActivity();
        mActivity = mActivityTestRule.getActivity();
        assertThat(mActivity, nullValue());*/
    }


    /**
     * Vérifier que : l'utilisateur peut annuler la création d'une nouvelle réunion en suivant ces étapes :
     * saisie de données
     * annulation
     */
    @Test
    public void onCancellingNewMeetingReunionListShouldDisplayNoExtraItem() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_meeting),
                        isDisplayed()));
        floatingActionButton.perform(click());

        //Saisie
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.Sujet)));
        textInputEditText.perform(scrollTo(), replaceText(sujet), closeSoftKeyboard());


        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.DateHeureDebut)));
        appCompatEditText.perform(scrollTo(), click());

        ViewAction dateDebutPickerAction = PickerActions.setDate(2021, 6, 8);
        ViewAction heureDebutPickerAction = PickerActions.setTime(10, 30);

        ViewInteraction vueDatePicker1 = onView(allOf(
                withClassName(equalTo(DatePicker.class.getName())),
                isDisplayed()
                )
        );
        vueDatePicker1.perform(dateDebutPickerAction);
        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        isDisplayed()));
        materialButton.perform(click());


        ViewInteraction vueTimePicker1 = onView(allOf(
                withClassName(equalTo(TimePicker.class.getName())),
                isDisplayed()));
        vueTimePicker1.perform(heureDebutPickerAction);
        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.DateHeureFin)));
        appCompatEditText2.perform(scrollTo(), click());

        ViewAction dateFinPickerAction = PickerActions.setDate(2021, 6, 8);
        ViewAction heureFinPickerAction = PickerActions.setTime(11, 30);

        ViewInteraction vueDatePicker2 = onView(allOf(
                withClassName(equalTo(DatePicker.class.getName())),
                isDisplayed()));
        vueDatePicker2.perform(dateFinPickerAction);
        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        isDisplayed()));
        materialButton3.perform(click());
        ViewInteraction vueTimePicker2 = onView(allOf(
                withClassName(equalTo(TimePicker.class.getName())),
                isDisplayed()));
        vueTimePicker2.perform(heureFinPickerAction);
        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialAutoCompleteTextView = onView(withId(R.id.autoCompleteTextView));
        materialAutoCompleteTextView.perform(scrollTo(), typeText(salle));
        onView(withText(containsString(salle))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        materialAutoCompleteTextView.check(matches(withText(containsString(salle))));

        ViewInteraction appCompatMultiAutoCompleteTextView = onView(withId(R.id.multiAutoCompleteTextView));
        appCompatMultiAutoCompleteTextView.perform(scrollTo(), typeText(nom1));
        onView(withText(containsString(email1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.perform(typeTextIntoFocusedView(nom2));
        onView(withText(containsString(email2))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.check(matches(withText(containsString(email1 + ", " + email2))));

        ViewInteraction materialAutoCompleteTextView2 = onView(withId(R.id.autoCompleteTextView2));
        materialAutoCompleteTextView2.perform(scrollTo(), typeText(nom3));
        onView(withText(containsString(email3))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        materialAutoCompleteTextView2.check(matches(withText(containsString(email3))));

        //Annulation
        ViewInteraction floatingActionButton2 = onView(allOf(withId(R.id.cancel_button)));
        floatingActionButton2.perform(scrollTo(), click());

        //Liste non-modifiée
        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT));
    }
}