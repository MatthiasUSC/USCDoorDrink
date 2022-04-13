package com.ttco.uscdoordrink;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;


public class feature3Test {
    @Rule
    public ActivityTestRule<OrderDetails> activityTestRule =
            new ActivityTestRule<>(OrderDetails.class);

    @Test
    public void IntializeTest(){
        //onView(withId(R.id.period)).check(matches(withText("Month")));
        System.out.println(OrderDetails.databaseComplete);
    }
}
