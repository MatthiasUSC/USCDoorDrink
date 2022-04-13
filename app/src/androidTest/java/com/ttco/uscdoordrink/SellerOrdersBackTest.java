package com.ttco.uscdoordrink;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.os.SystemClock;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SellerOrdersBackTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> rule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    public final long SERVER_WAIT_TIME_MILLIS = 5000;

    @Test
    public void backButtonTest() {
        // Enter dummy user info
        onView(withId(R.id.name)).perform(typeText("test"));
        onView(withId(R.id.password)).perform(typeText("seller"));

        // Close keyboard
        onView(isRoot()).perform(closeSoftKeyboard());
        // Login
        onView(withId(R.id.login)).perform(click());

        // Wait for response
        SystemClock.sleep(SERVER_WAIT_TIME_MILLIS);

        // Check for success
        onView(withId(R.id.map)).check(matches(isDisplayed())); // Check if map is displayed

        // Go to seller orders
        onView(withId(R.id.seller_orders)).perform(click());

        // Check if arrived
        onView(withId(R.id.order_list)).check(matches(isDisplayed()));

        // Click back
        onView(withId(R.id.back_button)).perform(click());

        // Check if back at map page
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}