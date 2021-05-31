package com.fyp;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.fyp.utils.CustomMatcher;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.fyp.utils.RecyclerViewMatcher.withRecyclerView;

public class FavouritePetsTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void addFavouriteFromSearch() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        // Go to favourite tab and check 0 pets
        Thread.sleep(1000);
        onView(withId(R.id.favouriteFragment)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.favourite_fragment_recycle_view)).check(withItemCount(0));

        // Go to search
        onView(withId(R.id.searchFragment)).perform(click());
        onView(withId(R.id.search_fragment_start_search)).perform(click());
        Thread.sleep(2000);

        // Add zero pet from card
        String zeroPetName = CustomMatcher.getText(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_name));
        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_favourite))
                .perform(click());

        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);

        // Add first pet from profile
        String firstPetName = CustomMatcher.getText(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(1, R.id.card_pet_name));
        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(1, R.id.card_pet_image))
                .perform(click());
        onView(withId(R.id.fragment_pet_information_favourite)).perform(click());
        pressBack();

        // Go to favourite and check 2 favourite pets
        onView(withId(R.id.favouriteFragment)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.favourite_fragment_recycle_view)).check(withItemCount(2));

        // Check zero pet name
        onView(withRecyclerView(R.id.favourite_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_name))
                .check(matches(withText(zeroPetName)));

        // Check first pet name
        onView(withId(R.id.favourite_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.favourite_fragment_recycle_view).atPositionOnView(1, R.id.card_pet_name))
                .check(matches(withText(firstPetName)));


        // Delete from favourite by card
        onView(withRecyclerView(R.id.favourite_fragment_recycle_view).atPositionOnView(1, R.id.card_pet_favourite))
                .perform(click());
        Thread.sleep(500);

        // Delete from favourite by profile
        onView(withRecyclerView(R.id.favourite_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_image))
                .perform(click());
        onView(withId(R.id.fragment_pet_information_favourite)).perform(click());
        pressBack();

        // Check zero pets again
        Thread.sleep(500);
        onView(withId(R.id.favourite_fragment_recycle_view)).check(withItemCount(0));
    }
}
