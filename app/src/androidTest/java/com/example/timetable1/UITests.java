package com.example.timetable1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import static org.junit.Assert.*;


import java.util.Collection;
import java.util.Iterator;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UITests {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.timetable1", appContext.getPackageName());
    }
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);



    @Test
    public void cantAddWrongHours() {
        onView(withId(R.id.addSubject)).perform(click());
        onView(withId(R.id.startHour))
                .perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.finishHour))
                .perform(typeText("1235"), closeSoftKeyboard());
        onView(withId(R.id.buttonAdd)).perform(click());

        onView(withId(R.id.eventTextView))
                .check(matches(withText(R.string.incHour)));

    }

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }
   /* @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void clickOnYourNavigationItem_ShowsYourScreen() throws InterruptedException {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_home));

        // Check that you Activity was opened.

    }*/

    @Test
    public void testPack1_addSubject() {
        onView(withId(R.id.addSubject)).perform(click());
        onView(withId(R.id.editSubjectName))
                .perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.startHour))
                .perform(typeText("12:34"), closeSoftKeyboard());
        onView(withId(R.id.finishHour))
                .perform(typeText("12:35"), closeSoftKeyboard());
        onView(withId(R.id.buttonAdd)).perform(click());

        ///dodac testy czy się widok otwiera
        onView(withId(R.id.eventTextView))
                .check(matches(withText(R.string.added)));



    }

   public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
       //checkNotNull(itemMatcher);
       return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
           @Override
           public void describeTo(org.hamcrest.Description description) {
                description.appendText("has item at position " + position + ": ");
               itemMatcher.describeTo(description);
           }

           @Override
           protected boolean matchesSafely(final RecyclerView view) {
               RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
               if (viewHolder == null) {
                   return false;
               }
               return itemMatcher.matches(viewHolder.itemView);
           }
       };
   }
    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
    public void moveToDay(int rersourceId){
        TextView t = getActivityInstance().findViewById(R.id.weekDay);

        while (!(t.getText().equals(getResourceString(rersourceId)))){
            onView(withId(R.id.nextDay)).perform(click());
        }
    }
    @Test
    public void testPack2_checkSubject(){

       moveToDay(R.string.sunday);
        onView(withId(R.id.subjectsView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.subjectsView))
                .check(matches(atPosition(0, hasDescendant(withText("Test")))));


    }

    @Test
    public void testPack3_deleteSubject() throws InterruptedException {
        moveToDay(R.string.sunday);
        onView(withId(R.id.subjectsView))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.subjectsView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.delButton)));

        onView(withId(R.id.subjectsView)).check(matches(hasChildCount(0)));
    }
}