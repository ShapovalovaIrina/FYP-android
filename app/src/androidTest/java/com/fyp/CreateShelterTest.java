package com.fyp;

import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.utils.CustomMatcher.hasTextInputLayoutErrorText;
import static com.fyp.utils.CustomMatcher.hasTextInputLayoutErrors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CreateShelterTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void createShelter() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";
        String shelterTitle = "Shelter \"Leaves\"";
        String vkLink = "https://vk.com/bemeeraculous";
        String siteLink = "https://yandex.ru/";

        String titleRequiredError = "Необходимо указать название";
        String linkRequiredError = "Необходимо указать ссылку либо на сообщество во ВКонтакте, либо на веб-сайт, либо оба варианта";
        String success = "Приют успешно добавлен";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        // Go to shelter tab and click on create shelter button
        Thread.sleep(1000);
        onView(withId(R.id.profileFragment)).perform(click());
        onView(withId(R.id.profile_fragment_create_shelter)).perform(click());

        // Without title and links
        onView(withId(R.id.create_shelter_fragment_submit_button)).perform(click());
        onView(withId(R.id.create_shelter_fragment_title_text_input_layout)).check(matches(hasTextInputLayoutErrorText(titleRequiredError)));
        onView(withId(R.id.create_shelter_fragment_vk_text_input_layout)).check(matches(hasTextInputLayoutErrorText(linkRequiredError)));
        onView(withId(R.id.create_shelter_fragment_site_text_input_layout)).check(matches(hasTextInputLayoutErrorText(linkRequiredError)));

        // Without links
        onView(withId(R.id.create_shelter_fragment_title_text_input_edit_text)).perform(clearText(), typeText(shelterTitle));
        closeSoftKeyboard();
        onView(withId(R.id.create_shelter_fragment_submit_button)).perform(click());
        onView(withId(R.id.create_shelter_fragment_title_text_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.create_shelter_fragment_vk_text_input_layout)).check(matches(hasTextInputLayoutErrorText(linkRequiredError)));
        onView(withId(R.id.create_shelter_fragment_site_text_input_layout)).check(matches(hasTextInputLayoutErrorText(linkRequiredError)));

        // Correct
        onView(withId(R.id.create_shelter_fragment_title_text_input_edit_text)).perform(clearText(), typeText(shelterTitle));
        onView(withId(R.id.create_shelter_fragment_vk_text_input_edit_text)).perform(clearText(), typeText(vkLink));
        onView(withId(R.id.create_shelter_fragment_site_text_input_edit_text)).perform(clearText(), typeText(siteLink));
        closeSoftKeyboard();
        onView(withId(R.id.create_shelter_fragment_submit_button)).perform(click());
        Thread.sleep(500);
        onView(withText(success)).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }
}
