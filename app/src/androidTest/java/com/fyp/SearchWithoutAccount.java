package com.fyp;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.fyp.utils.RecyclerViewMatcher;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.fyp.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.not;

public class SearchWithoutAccount {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void searchWithoutSignIn() throws InterruptedException {
        onView(withId(R.id.without_registration)).perform(click());
        Thread.sleep(500);

        onView(withId(R.id.searchFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.favouriteFragment)).check(matches(not(isDisplayed())));
        onView(withId(R.id.profileFragment)).check(matches(not(isDisplayed())));

        onView(withId(R.id.search_fragment_start_search)).perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.search_fragment_recycle_view)).check(withItemCount(5));
        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_favourite))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_image))
                .perform(click());
        onView(withId(R.id.fragment_pet_information_favourite)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
