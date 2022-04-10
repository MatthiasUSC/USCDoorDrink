package com.ttco.uscdoordrink;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;

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
public class LoginTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> rule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void loginSuccess() {
        // Enter dummy user info
        onView(withId(R.id.name)).perform(typeText("hello"));
        onView(withId(R.id.password)).perform(typeText("world"));

        // Login
        onView(withId(R.id.login)).perform();

        // Check for success
        onView(withId(R.id.map)).check(matches(isDisplayed())); // Check if map is displayed
    }

    @Test
    public void loginFail() {
        // Enter dummy user info
        onView(withId(R.id.name)).perform(typeText("sadggffdsg"));
        onView(withId(R.id.password)).perform(typeText("sadggffdsg"));

        // Login
        onView(withId(R.id.login)).perform();

        // Check for fail
        onView(withId(R.id.login)).check(matches(not(isDisplayed()))); // Check if login button still there
    }
}