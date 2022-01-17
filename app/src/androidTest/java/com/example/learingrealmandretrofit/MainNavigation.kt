package com.example.learingrealmandretrofit

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.learingrealmandretrofit.card.RecyclerAdapterCard
import com.example.learingrealmandretrofit.deck.RecyclerAdapterDeck
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

class MainNavigation {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun fullNavigationApp() {

        waitForClosingProgressBar()

        assertCardFragment()

        clickCreateCardButtonCardFragment()

        assertCardCreateDialog()

        pressBack()

        assertCardFragment()

        openItemCardRecycler()

        assertShowDetailsCardFragment()

        clickButtonChangeCardShowDetailsCardFragment()

        pressBack()

        assertShowDetailsCardFragment()

        pressBack()

        waitForClosingProgressBar()

        assertCardFragment()

        swipeToLeftCardItemRecycler()

        assertCardDeleteDialog()

        clickButtonNo()

        assertCardFragment()

        swipeToRight()

        assertDeckFragment()

        pressBack()

        waitForClosingProgressBar()

        assertCardFragment()

        assertBottomBar()

        openDeckScreen()

        waitForClosingProgressBar()

        assertDeckFragment()

        clickCreateDeckButtonDeckFragment()

        assertDeckCreateOrUpdateDialog()

        pressBack()

        assertDeckFragment()

        openItemDeckRecycler()

        waitForClosingProgressBar()

        assertCardDeckFragment()

        clickCreateCardButtonCardFragment()

        assertCardCreateDialog()

        pressBack()

        assertCardDeckFragment()

        clickChangeDeck()

        assertDeckCreateOrUpdateDialog()

        pressBack()

        assertCardDeckFragment()

        swipeToLeftCardItemRecycler()

        assertCardDeleteDialog()

        clickButtonNo()

        assertCardDeckFragment()

        openItemCardRecycler()

        assertShowDetailsCardFragment()

        clickButtonChangeCardShowDetailsCardFragment()

        pressBack()

        assertShowDetailsCardFragment()

        pressBack()

        waitForClosingProgressBar()

        assertCardDeckFragment()

        pressBack()

        waitForClosingProgressBar()

        assertDeckFragment()

        swipeToLeftDeck()

        assertDeckDeleteDialog()

        pressBack()

        assertDeckFragment()

        assertBottomBar()

        openSettingsScreen()

        assertSettingsFragment()

        assertBottomBar()

        openCardScreen()
    }

    private fun assertBottomBar() {
        onView(withId(R.id.deck_navigation))
            .check(matches(isDisplayed()))

        onView(withId(R.id.card_navigation))
            .check(matches(isDisplayed()))

        onView(withId(R.id.settings_navigation))
            .check(matches(isDisplayed()))
    }

    private fun openSettingsScreen() {
        onView(allOf(withContentDescription(R.string.settings), isDisplayed()))
            .perform(click())
    }

    private fun openDeckScreen() {
        onView(allOf(withContentDescription(R.string.deck), isDisplayed()))
            .perform(click())
    }

    private fun openCardScreen() {
        onView(allOf(withContentDescription(R.string.card), isDisplayed()))
            .perform(click())
    }

    private fun waitForClosingProgressBar() {
        onView(withId(R.id.mainProgressBarHolder))
            .check(matches(isDisplayed()))
            .perform(WaitUntilGoneAction())
    }

    private fun openItemCardRecycler() {
        onView(withId(R.id.recyclerCard))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0, click()))
    }

    private fun clickButtonChangeCardShowDetailsCardFragment() {
        onView(withId(R.id.buttonChangeCard))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun clickCreateCardButtonCardFragment() {
        onView(withId(R.id.buttonCreateCard))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun swipeToLeftCardItemRecycler() {
        onView(withId(R.id.recyclerCard))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0,
                swipeLeft()))
    }

    private fun clickButtonNo() {
        onView(withId(R.id.button_no))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun swipeToRight() {
        onView(withId(R.id.recyclerCard))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0,
                swipeRight()))
    }

    private fun clickCreateDeckButtonDeckFragment() {
        onView(withId(R.id.buttonCreateDeck))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun openItemDeckRecycler() {
        onView(withId(R.id.recyclerDeck))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterDeck.DeckViewHolder>(0, click()))
    }

    private fun clickChangeDeck() {
        onView(withId(R.id.renameItem))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun swipeToLeftDeck() {
        onView(withId(R.id.recyclerDeck))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterDeck.DeckViewHolder>(0, swipeLeft()))
    }

    private fun assertCardFragment() {
        onView(withId(R.id.cardFragmentLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertSettingsFragment() {
        onView(withId(R.id.settingsFragmentLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertDeckFragment() {
        onView(withId(R.id.deckFragmentLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertCardDeckFragment() {
        onView(withId(R.id.cardFragmentLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertShowDetailsCardFragment() {
        onView(withId(R.id.showDetailsCardLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertCardCreateDialog() {
        onView(withId(R.id.cardCreatedDialogLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertDeckCreateOrUpdateDialog() {
        onView(withId(R.id.deckCreateOrUpdateDialogLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertCardDeleteDialog() {
        onView(withId(R.id.cardDeleteDialogLayoutId))
            .check(matches(isDisplayed()))
    }

    private fun assertDeckDeleteDialog() {
        onView(withId(R.id.deckDeleteDialogLayoutId))
            .check(matches(isDisplayed()))
    }
}
