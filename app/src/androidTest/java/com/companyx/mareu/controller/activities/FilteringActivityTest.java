package com.companyx.mareu.controller.activities;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;

import com.companyx.mareu.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.companyx.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 28/05/2021
 */
public class FilteringActivityTest {
    private MainActivity mActivity;

    private static int ITEMS_COUNT = 24;
    private static int ITEMS_COUNT_FILTER_ROOM = 4;
    private static int ITEMS_COUNT_FILTER_ROOM2 = 10;
    private static int ITEMS_COUNT_FILTER_DATE = 12;
    private static int ITEMS_COUNT_FILTER_ROOM_DATE = 5;

    private String salle1 = "Peach";
    private String salle2 = "Pomme";
    private String salle3 = "Mario";
    private String date = "22/05/2021";

    private int positionDateToFilter = 1;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onOneRoomFilteredActionFilteredListShouldBeDisplayed() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Filtre par lieu ou par date"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatMultiAutoCompleteTextView = onView(withId(R.id.FiltreSalles));
        appCompatMultiAutoCompleteTextView.perform(typeText(salle1));
        onView(withText(containsString(salle1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.check(matches(withText(containsString(salle1))));

        ViewInteraction actionFilterView = onView(
                allOf(withId(R.id.filter_action_check), withContentDescription("Valider"),
                        isDisplayed()));
        actionFilterView.perform(click());

        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT_FILTER_ROOM));
    }

    @Test
    public void onManyRoomFilteredActionFilteredListShouldBeDisplayed() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Filtre par lieu ou par date"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatMultiAutoCompleteTextView = onView(withId(R.id.FiltreSalles));
        appCompatMultiAutoCompleteTextView.perform(typeText(salle1));
        onView(withText(containsString(salle1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.perform(typeTextIntoFocusedView(salle2));
        onView(withText(containsString(salle2))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.perform(typeTextIntoFocusedView(salle3));
        onView(withText(containsString(salle3))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.check(matches(withText(containsString(salle1 + ", " + salle2 + ", " + salle3))));

        ViewInteraction actionFilterView = onView(
                allOf(withId(R.id.filter_action_check), withContentDescription("Valider"),
                        isDisplayed()));
        actionFilterView.perform(click());

        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT_FILTER_ROOM2));
    }

    @Test
    public void onRoomAndDateFilteredActionFilteredListShouldBeDisplayed() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Filtre par lieu ou par date"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.FiltreDateSpinner),
                        isDisplayed()));
        appCompatSpinner.perform(click());
        onView(withText(containsString(date))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatSpinner.check(matches(hasDescendant(withText(containsString(date)))));

        ViewInteraction appCompatMultiAutoCompleteTextView = onView(withId(R.id.FiltreSalles));
        appCompatMultiAutoCompleteTextView.perform(typeText(salle1));
        onView(withText(containsString(salle1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.perform(typeTextIntoFocusedView(salle2));
        onView(withText(containsString(salle2))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.perform(typeTextIntoFocusedView(salle3));
        onView(withText(containsString(salle3))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.check(matches(withText(containsString(salle1 + ", " + salle2 + ", " + salle3))));

        ViewInteraction actionFilterView = onView(
                allOf(withId(R.id.filter_action_check), withContentDescription("Valider"),
                        isDisplayed()));
        actionFilterView.perform(click());

        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT_FILTER_ROOM_DATE));
    }

    @Test
    public void onFilterCancellationOriginalListShouldBeDisplayed() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Filtre par lieu ou par date"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.FiltreDateSpinner),
                        isDisplayed()));
        appCompatSpinner.perform(click());
        onView(withText(containsString(date))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatSpinner.check(matches(hasDescendant(withText(containsString(date)))));

        ViewInteraction appCompatMultiAutoCompleteTextView = onView(withId(R.id.FiltreSalles));
        appCompatMultiAutoCompleteTextView.perform(typeText(salle1));
        onView(withText(containsString(salle1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        appCompatMultiAutoCompleteTextView.check(matches(withText(containsString(salle1))));

        ViewInteraction actionFilterView = onView(
                allOf(withId(R.id.filter_action_close), withContentDescription("Fermer"),
                        isDisplayed()));
        actionFilterView.perform(click());

        onView(allOf(withId(R.id.meeting_item_list))).check(withItemCount(ITEMS_COUNT));
    }

    @Test
    public void onDateFilteredActionFilteredListShouldBeDisplayed() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Filtre par lieu ou par date"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.FiltreDateSpinner),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        onView(withText(containsString(date))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        appCompatSpinner.check(matches(hasDescendant(withText(containsString(date)))));

        ViewInteraction actionFilterView = onView(
                allOf(withId(R.id.filter_action_check), withContentDescription("Valider"),
                        isDisplayed()));
        actionFilterView.perform(click());

        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT_FILTER_DATE));
    }
}