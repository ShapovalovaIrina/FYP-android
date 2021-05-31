package com.fyp;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Shelter;
import com.fyp.ui.MainActivity;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.utils.RecyclerViewMatcher.withRecyclerView;

public class SupportedSheltersTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void checkSupportedSheltersList() throws IOException, InterruptedException {
        ServerAPI serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
        List<Shelter> shelters = serverAPI.getAllShelters().execute().body();
        if (shelters == null) shelters = new ArrayList<>();

        String email = "Irina130219@yandex.ru";
        String password = "123456";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);
        Thread.sleep(1000);

        // Check shelters in filter
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());
        onView(withText(shelters.get(0).getTitle())).check(matches(isDisplayed()));
        onView(withText(shelters.get(1).getTitle())).check(matches(isDisplayed()));
        // Don't want to do swipe down :(
//        for (Shelter shelter : shelters) {
//            onView(withText(shelter.getTitle())).check(matches(isDisplayed()));
//        }
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());

        // Check shelters in profile
        onView(withId(R.id.profileFragment)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.profile_fragment_show_shelters)).perform(click());
        for (int i = 0; i < shelters.size(); i++) {
            onView(
                    withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                            .atPositionOnView(i, R.id.card_shelter_title))
                    .check(matches(withText(shelters.get(i).getTitle())));
        }

        // Check contact expand view
        onView(withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                .atPositionOnView(0, R.id.card_shelter_vk_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                .atPositionOnView(0, R.id.card_shelter_site_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                .atPositionOnView(0, R.id.card_shelter_contacts_button))
                .perform(click());
        onView(withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                .atPositionOnView(0, R.id.card_shelter_vk_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withRecyclerView(R.id.supported_shelters_fragment_shelter_recycler_view)
                .atPositionOnView(0, R.id.card_shelter_site_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
