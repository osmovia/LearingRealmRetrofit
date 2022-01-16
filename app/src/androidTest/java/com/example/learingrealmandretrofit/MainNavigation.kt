package com.example.learingrealmandretrofit

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learingrealmandretrofit.card.RecyclerAdapterCard
import com.example.learingrealmandretrofit.deck.RecyclerAdapterDeck
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainNavigation {

    @Test
    fun fullNavigationApp() {
        ActivityScenario.launch(MainActivity::class.java)

        waitForClosingProgressBar()

        clickCreateCardButtonCardFragment()

        pressBack()

        openItemCardRecycler()

        clickButtonChangeCardShowDetailsCardFragment()

        pressBack()

        pressBack()

        waitForClosingProgressBar()

        swipeToLeftCardItemRecycler()

        clickButtonNo()

        swipeToRight()

        pressBack()

        waitForClosingProgressBar()

        openDeckScreen()

        waitForClosingProgressBar()

        clickCreateDeckButtonDeckFragment()

        pressBack()

        openItemDeckRecycler()

        waitForClosingProgressBar()

        clickCreateCardButtonCardFragment()

        pressBack()

        clickChangeDeck()

        pressBack()

        swipeToLeftCardItemRecycler()

        clickButtonNo()

        openItemCardRecycler()

        clickButtonChangeCardShowDetailsCardFragment()

        pressBack()

        pressBack()

        waitForClosingProgressBar()

        pressBack()

        swipeToLeftDeck()

        pressBack()

        openSettingsScreen()

        openCardScreen()
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
            .perform(WaitUntilGoneAction())
    }

    private fun openItemCardRecycler() {
        onView(withId(R.id.recyclerCard))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0, click()))
    }

    private fun clickButtonChangeCardShowDetailsCardFragment() {
        onView(withId(R.id.buttonChangeCard))
            .perform(click())
    }

    private fun clickCreateCardButtonCardFragment() {
        onView(withId(R.id.buttonCreateCard))
            .perform(click())
    }

    private fun swipeToLeftCardItemRecycler() {
        onView(withId(R.id.recyclerCard))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0,
                swipeLeft()))
    }

    private fun clickButtonNo() {
        onView(withId(R.id.button_no))
            .perform(click())
    }

    private fun swipeToRight() {
        onView(withId(R.id.recyclerCard))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterCard.CardViewHolder>(0,
                swipeRight()))
    }

    private fun clickCreateDeckButtonDeckFragment() {
        onView(withId(R.id.buttonCreateDeck))
            .perform(click())
    }

    private fun openItemDeckRecycler() {
        onView(withId(R.id.recyclerDeck))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterDeck.DeckViewHolder>(0, click()))
    }

    private fun clickChangeDeck() {
        onView(withId(R.id.renameItem))
            .perform(click())
    }

    private fun swipeToLeftDeck() {
        onView(withId(R.id.recyclerDeck))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapterDeck.DeckViewHolder>(0, swipeLeft()))
    }
}
