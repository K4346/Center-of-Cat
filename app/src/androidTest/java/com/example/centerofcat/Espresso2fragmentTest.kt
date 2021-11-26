package com.example.centerofcat

import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.centerofcat.app.ui.catFavourites.FavouritesCatsFragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.centerofcat.app.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class Espresso2fragmentTest {
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java
    )

    @Test
    fun check1FirstScreenElementsDisplayed() {
        launchFragmentInContainer<FavouritesCatsFragment>()
        onView(withId(R.id.action_bar_tab)).check(matches(withText("Избранное")))
    }


}
