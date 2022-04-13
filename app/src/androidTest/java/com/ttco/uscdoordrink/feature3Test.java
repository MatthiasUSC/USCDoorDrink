package com.ttco.uscdoordrink;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
