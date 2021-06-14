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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.companyx.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by CodeurSteph on 28/05/2021
 */
public class MainActivityTest_ForcingExecution {
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
    public void onRoomUpSortingActionSortedListShouldBeDisplayed() {
        ViewInteraction recyclerViewItemView1d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(0));
        recyclerViewItemView1d.check(matches(hasDescendant(not(withText(containsString(salle1))))));
        ViewInteraction recyclerViewItemView2d = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 1));
        recyclerViewItemView2d.check(matches(hasDescendant(not(withText(containsString(salle2))))));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.sorting), withContentDescription("Options de tri"),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Lieu par ordre croissant"),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction recyclerViewItemView1 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(0));
        recyclerViewItemView1.check(matches(hasDescendant(withText(containsString(salle1)))));
        ViewInteraction recyclerViewItemView2 = onView(ViewMatchers.withId(R.id.meeting_item_list))
                .perform(RecyclerViewActions.scrollToPosition(ITEMS_COUNT - 1));
        recyclerViewItemView2.check(matches(hasDescendant(withText(containsString(salle2)))));
    }
}
