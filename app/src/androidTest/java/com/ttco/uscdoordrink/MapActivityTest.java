package com.ttco.uscdoordrink;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.maps.android.Context.getApplicationContext;
import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import static org.junit.Assert.assertTrue;

import com.google.android.gms.maps.model.Marker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void checkOpenStoreMenuTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button6), withText("Open Menu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        button2.perform(click());

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.store_name), withText("Hello"),
                        withParent(allOf(withId(R.id.top_linear_layout),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("Hello")));
    }

    @Test
    public void orderSuccesfulTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        ViewInteraction button = onView(
                allOf(withId(R.id.button6), withText("Open Menu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        button.perform(click());

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        ViewInteraction button2 = onView(
                allOf(withText("chocolate (Decaffeinated) $2"),
                        childAtPosition(
                                allOf(withId(R.id.store_menu),
                                        childAtPosition(
                                                withId(R.id.scroll_layout),
                                                0)),
                                3)));
        button2.perform(scrollTo(), click());

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.message), withText("Order created succesfully!"),
                        isDisplayed()));
        textView.check(matches(withText("Order created succesfully!")));
    }

    @Test
    public void walkingToStoreTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        ViewInteraction button = onView(
                allOf(withId(R.id.button4), withText("Walking"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        button.perform(click());

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector()
                .descriptionContains("Delivery Time (walking): 7 mins"));
        assertTrue(marker.exists());
    }

    @Test
    public void drivingToStoreTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector()
                .descriptionContains("Delivery Time (driving): 4 mins"));
        assertTrue(marker.exists());
    }

    @Test
    public void cyclingToStoreTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        ViewInteraction button = onView(
                allOf(withId(R.id.button5), withText("Cycling"),
                        isDisplayed()));
        button.perform(click());

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector()
                .descriptionContains("Delivery Time (bicycling): 3 mins"));
        assertTrue(marker.exists());
    }

    @Test
    public void markersForAllStoresTest() {
        logIn("julio", "cesar");


        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // TODO: Change to all stores in DB !
        String storeNames[] = {"Hello", "hello"};
        UiObject marker;

        for(String name : storeNames){
             marker = device.findObject(new UiSelector()
                    .descriptionContains(name));
            assertTrue(marker.exists());
        }
    }

    @Test
    public void menuItemsDisplayedTest() {
        logIn("julio", "cesar");

        clickMarker("Hello");

        ViewInteraction button = onView(
                allOf(withId(R.id.button6), withText("Open Menu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withText("CSCI310 (DECAFFEINATED) $1"),
                        withParent(allOf(withId(R.id.store_menu),
                                withParent(withId(R.id.scroll_layout)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withText("CHOCOLATE (DECAFFEINATED) $2"),
                        withParent(allOf(withId(R.id.store_menu),
                                withParent(withId(R.id.scroll_layout)))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));
    }

    private void clickMarker(String storeName) {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(storeName));

        try {
            marker.click();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void logIn(String username, String password){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(username), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(password), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        try {
            Thread.sleep(5000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
