package com.ttco.uscdoordrink;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import android.support.test.espresso.ViewInteraction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class OrderDetailsTest {
    //OrderHistoryEntry ohe;
    OrderDetails ohe;
    @Before
//    String doc_id, String customer_name, String drink,
//    String startTime, String seller_name, String restaurant_name,
//    String endTime, Boolean isCaffeinated, String order_location
//
    public void init(){
        //orderDetails = new OrderDetails();
        //ohe = new OrderHistoryEntry(1, "bob", "cocaine");
        //ohe = new OrderDetails();
    }
    @Test
    public void getPeriod() {
        System.out.println("hi");
        OrderDetails.cycle = 1;
        String testmonth = OrderDetails.GetPeriod();

//        orderDetails.cycle = 1;
//        String test_month = orderDetails.GetPeriod();
        assertEquals(testmonth, "Year");
    }

    @Test
    public void OrderToString(){
        String res = OrderDetails.OrderToString("coke", "inandout", "never", "nowhere", "3");
            assertEquals(res, "Drink: " + "coke" + "\n"
                    + "Restaurant: " + "inandout" + "\n" +
                    "Time ordered: " + "never" + "\n" + "Place enjoyed beverage: "
                    + "nowhere"
                    + "\n" + "Time order received: " + "3");
    }
    @Test
    public void MakeRecTest(){
        String test_rec = OrderDetails.MakeRec("Spongebob");
        assertEquals(test_rec, "You visited Spongebob the most this month we recommend you visit this place again.");
    }
    @Test
    public void getDateTest(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("23/03/0323 03:23:01", dtf);

        int order_month = dateTime.getMonth().getValue();
        int order_year = dateTime.getYear();
        int order_day = dateTime.getDayOfWeek().getValue();
        int order_hour = dateTime.getHour();
        int order_minute = dateTime.getMinute();
        int order_second = dateTime.getSecond();

        HashMap dateAttr = OrderDetails.getDate(dateTime);
        assertEquals(order_month, dateAttr.get("month"));
        assertEquals(order_year, dateAttr.get("year"));
        assertEquals(order_day, dateAttr.get("day"));
        assertEquals(order_hour, dateAttr.get("hour"));
        assertEquals(order_minute, dateAttr.get("minute"));
        assertEquals(order_second, dateAttr.get("second"));


    }
    @Test
    public void mainActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.LoginLink), withText("To Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("world"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.register), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.order_history_button), withText("Order History (If customer)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.period), withText("Month"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.period), withText("DAY"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
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









}