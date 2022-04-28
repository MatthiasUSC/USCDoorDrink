package com.ttco.uscdoordrink;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SellerOrdersRefreshTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> rule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    public final long SERVER_WAIT_TIME_MILLIS = 5000;

    @Test
    public void refreshTest() {
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
        onView(withId(R.id.seller_orders_button)).perform(click());

        // Check if arrived
        onView(withId(R.id.order_list)).check(matches(isDisplayed()));

        // Click refresh
        onView(withId(R.id.refresh_button)).perform(click());

        // Check if still on page and nothing has changed
        onView(withId(R.id.order_list)).check(matches(isDisplayed()));
    }
}