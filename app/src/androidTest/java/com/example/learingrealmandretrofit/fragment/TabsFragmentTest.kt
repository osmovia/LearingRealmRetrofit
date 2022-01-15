package com.example.learingrealmandretrofit.fragment

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learingrealmandretrofit.MainActivity
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.WaitUntilGoneAction
import org.hamcrest.CoreMatchers.allOf
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TabsFragmentTest: TestCase() {


    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)


        while (true) {
            assertCardScreen()

            waitCloseSpinner()

            openSettingsScreen()

            assertSettingsScreen()

            openDeckScreen()

            waitCloseSpinner()

            assertDeckScreen()

            openSettingsScreen()

            assertSettingsScreen()

            openCardScreen()
        }

    }

//    @Test
//    fun onLaunchCheckBottomBar() {
//        ActivityScenario.launch(MainActivity::class.java)
//
//        onView(withId(R.id.bottomNavigationView))
//
//        onLaunchIconInBottomBar()
//
//        clickSettingsTabs()
//    }
//
//    private fun onLaunchIconInBottomBar() {
//        ActivityScenario.launch(MainActivity::class.java)
//
//        onView(withId(R.id.deck_navigation))
//
//        onView(withId(R.id.card_navigation))
//
//        onView(withId(R.id.settings_navigation))
//    }
//
//    private fun clickSettingsTabs() {
//        ActivityScenario.launch(MainActivity::class.java)
//
//        onView(withId(R.id.settings_navigation))
//            .perform(click())
//            .check(matches(isDisplayed()))
//    }

    private fun assertCardScreen() {
        onView(withId(R.id.toolbarContainerCard))
            .check(matches(isDisplayed()))
    }

    private fun openSettingsScreen() {
        onView(allOf(withContentDescription(R.string.settings), isDisplayed()))
            .perform(click())
    }

    private fun assertSettingsScreen() {
        onView(withId(R.id.buttonSignOut))
            .check(matches(isDisplayed()))
    }

    private fun assertDeckScreen() {
        onView(withId(R.id.toolbarContainerDeck))
            .check(matches(isDisplayed()))
    }

    private fun openDeckScreen() {
        onView(allOf(withContentDescription(R.string.deck), isDisplayed()))
            .perform(click())
    }

    private fun openCardScreen() {
        onView(allOf(withContentDescription(R.string.card), isDisplayed()))
            .perform(click())
    }

    private fun waitUntilGone(timeout: Long): ViewAction {
        return WaitUntilGoneAction(timeout)
    }

    private fun waitCloseSpinner() {
        onView(withId(R.id.mainProgressBarHolder)).perform(waitUntilGone(999999999L))
    }


}
