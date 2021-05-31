package com.fyp;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.fyp.utils.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.greaterThan;

public class SearchTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void searchWithSwipe() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        onView(withId(R.id.favouriteFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.searchFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profileFragment)).check(matches(isDisplayed()));

        Thread.sleep(2000);
        onView(withId(R.id.search_fragment_start_search)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.search_fragment_recycle_view)).check(withItemCount(5));
        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_favourite))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);
        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);
        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);
        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        onView(withId(R.id.search_fragment_recycle_view)).check(withItemCount(10));
    }

    @Test
    public void searchWithFilter() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";
        String shelterTitle = "Приют \"Друг\"";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        onView(withId(R.id.favouriteFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.searchFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profileFragment)).check(matches(isDisplayed()));

        Thread.sleep(2000);
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());
        onView(withId(R.id.filter_view_parent_check_box_types)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_view_parent_check_box_shelters)).check(matches(isDisplayed()));
        onView(withId(R.id.search_fragment_recycle_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Check only one shelter
        onView(withId(R.id.filter_view_parent_check_box_shelters)).perform(click());
        onView(withText(shelterTitle)).perform(click());
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());


        onView(withId(R.id.search_fragment_start_search)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.search_fragment_recycle_view)).check(withItemCount(greaterThan(0)));

        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(0, R.id.card_pet_image))
                .perform(click());
        swipeDown();
        onView(withId(R.id.fragment_pet_information_shelter_title)).check(matches(withText(shelterTitle)));
        pressBack();

        onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(1, R.id.card_pet_image))
                .perform(click());
        swipeDown();
        onView(withId(R.id.fragment_pet_information_shelter_title)).check(matches(withText(shelterTitle)));
    }
}
