package com.example.centerofcat

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.centerofcat.app.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkFirstScreenElementsDisplayed() {
        onView(withId(R.id.action_bar_tab)).check(matches(withText("Котики")))
        onView(withId(R.id.rvCatList)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerOfBreed)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerOfCategories)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerOfOrder)).check(matches(isDisplayed()))
    }
}