package com.companyx.mareu.controller.activities;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.companyx.mareu.R;
import com.companyx.mareu.utils.DeleteViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.companyx.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by CodeurSteph on 28/05/2021
 */
public class MainActivityTest {
    private MainActivity mActivity;

    private static int ITEMS_COUNT = 24;
    private static int ITEMS_COUNT_FILTER_ROOM = 2;
    private String subjectReunionToDelete = "Réunion sujet 1";
    private int positionReunionToDelete = 3;

    private String salle1 = "Apple";
    private String salle2 = "Toto";
    private String date1 = "22/05/2021";
    private String date2 = "01/06/2021";

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
    public void onMainActivityReunionListShouldBeDisplayed() {
        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT));
    }

    @Test
    public void onDeleteClickReunionListShouldBeDisplayedMinusITem() {
        onView(ViewMatchers.withId(R.id.meeting_item_list)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionReunionToDelete - 1, new DeleteViewAction()));
        onView(allOf(withId(R.id.meeting_item_list), not(withText(subjectReunionToDelete)))).check(withItemCount(ITEMS_COUNT - 1));
    }

    @Test
    public void onNoFilterActionRawListShouldBeDisplayed() {
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

        ViewInteraction actionMenuItemView1 = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtrer par lieu et/ou date"),
                        isDisplayed()));
        actionMenuItemView1.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.title), withText("Supprimer les filtres"),
                        isDisplayed()));
        materialTextView2.perform(click());

        onView(withId(R.id.meeting_item_list)).check(withItemCount(ITEMS_COUNT));
    }

    @Test
    public void onRoomDownSortingActionSortedListShouldBeDisplayed() {
        ViewInteraction recyclerViewItemView1d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(0));
        recyclerViewItemView1d.check(matches(hasDescendant(not(withText(containsString(salle2))))));
        ViewInteraction recyclerViewItemView2d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 1));
        recyclerViewItemView2d.check(matches(hasDescendant(not(withText(containsString(salle1))))));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Lieu par ordre décroissant"),
                        isDisplayed()));
        materialTextView.perform(click());

        //Scrolldown et lire données
        ViewInteraction recyclerViewItemView1 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(0));
        recyclerViewItemView1.check(matches(hasDescendant(withText(containsString(salle2)))));
        ViewInteraction recyclerViewItemView2 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 1));
        recyclerViewItemView2.check(matches(hasDescendant(withText(containsString(salle1)))));
    }

    @Test
    public void onDateUpSortingActionSortedListShouldBeDisplayed() {
        ViewInteraction recyclerViewItemView1d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        recyclerViewItemView1d.check(matches(hasDescendant(not(withText(containsString(date1))))));
        ViewInteraction recyclerViewItemView2d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2));
        recyclerViewItemView2d.check(matches(hasDescendant(not(withText(containsString(date2))))));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Heure par ordre croissant"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction recyclerViewItemView1 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        recyclerViewItemView1.check(matches(hasDescendant(withText(containsString(date1)))));
        ViewInteraction recyclerViewItemView2 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2));
        recyclerViewItemView2.check(matches(hasDescendant(withText(containsString(date2)))));
    }

    @Test
    public void onDateDownSortingActionSortedListShouldBeDisplayed() {
        ViewInteraction recyclerViewItemView1d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        recyclerViewItemView1d.check(matches(hasDescendant(not(withText(containsString(date1))))));
        ViewInteraction recyclerViewItemView2d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2));
        recyclerViewItemView2d.check(matches(hasDescendant(not(withText(containsString(date2))))));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Heure par ordre décroissant"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction recyclerViewItemView1 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        recyclerViewItemView1.check(matches(hasDescendant(withText(containsString(date2)))));
        ViewInteraction recyclerViewItemView2 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2));
        recyclerViewItemView2.check(matches(hasDescendant(withText(containsString(date1)))));
    }

    @Test
    public void onNoSortingActionSortedListShouldBeDisplayed() {
        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .check(matches(hasDescendant(not(withText(containsString(date1))))));
        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2))
                .check(matches(hasDescendant(not(withText(containsString(date2))))));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Heure par ordre croissant"),
                        isDisplayed()));
        materialTextView.perform(click());

        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .check(matches(hasDescendant(withText(containsString(date1)))));
        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2))
                .check(matches(hasDescendant(withText(containsString(date2)))));


        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.title), withText("Tri par défaut"),
                        isDisplayed()));
        materialTextView2.perform(click());

        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .check(matches(hasDescendant(not(withText(containsString(date1))))));
        onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 2))
                .check(matches(hasDescendant(not(withText(containsString(date2))))));
    }
}
