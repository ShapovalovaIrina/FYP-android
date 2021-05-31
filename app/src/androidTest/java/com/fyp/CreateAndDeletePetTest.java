package com.fyp;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.rule.ActivityTestRule;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Shelter;
import com.fyp.response.Type;
import com.fyp.ui.MainActivity;
import com.fyp.utils.CustomMatcher;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.utils.CustomMatcher.hasTextInputLayoutErrorText;
import static com.fyp.utils.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class CreateAndDeletePetTest {
    public String name;
    public String shelter;
    public String type;
    public String source;
    public String photo;

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUpPet() throws IOException {
        ServerAPI serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
        List<Shelter> shelters = serverAPI.getAllShelters().execute().body();
        List<Type> types = serverAPI.getAllTypes().execute().body();
        assertThat(shelters, notNullValue());
        assertThat(types, notNullValue());
        assertThat(shelters, hasSize(greaterThanOrEqualTo(1)));
        assertThat(types, hasSize(greaterThanOrEqualTo(1)));

        name = "Cutie";
        shelter = shelters.get(0).getTitle();
        type = types.get(0).getType();
        source = "https://vk.com/bemeeraculous";
        photo = "https://i.pinimg.com/564x/15/ab/3b/15ab3b34693faa42f767ae9cae0e2044.jpg";
    }

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void createPet() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";

        String shelterRequiredError = "Необходимо указать приют";
        String photoRequiredError = "Необходимо добавить фотографию";
        String sourceRequiredError = "Необходимо указать ссылку на источник с питомцем";
        String nameRequiredError = "Необходимо указать имя";
        String typeRequiredError = "Необходимо указать тип";
        String success = "Питомец успешно добавлен";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        // Go to shelter tab and click on create shelter button
        Thread.sleep(1000);
        onView(withId(R.id.profileFragment)).perform(click());
        onView(withId(R.id.profile_fragment_create_pet)).perform(click());

        // Without required fields
        onView(withId(R.id.create_pet_fragment_scroll_view)).perform(swipeUp());
        Thread.sleep(500);
        onView(withId(R.id.create_pet_fragment_create_pet)).perform(click());
        onView(withId(R.id.create_pet_fragment_source_text_input_layout)).check(matches(hasTextInputLayoutErrorText(sourceRequiredError)));
        onView(withId(R.id.create_pet_fragment_name_text_input_layout)).check(matches(hasTextInputLayoutErrorText(nameRequiredError)));
        onView(withId(R.id.create_pet_fragment_shelter_text_input_layout)).check(matches(hasTextInputLayoutErrorText(shelterRequiredError)));
        onView(withId(R.id.create_pet_fragment_type_text_input_layout)).check(matches(hasTextInputLayoutErrorText(typeRequiredError)));
        onView(withId(R.id.create_pet_fragment_photo_text_input_layout)).check(matches(hasTextInputLayoutErrorText(photoRequiredError)));

        // Go to start
        onView(withId(R.id.create_pet_fragment_scroll_view)).perform(swipeDown());
        Thread.sleep(500);

        // Fill source and name
        onView(withId(R.id.create_pet_fragment_source_text_input_edit_text)).perform(typeText(source));
        onView(withId(R.id.create_pet_fragment_name_text_input_edit_text)).perform(typeText(name));
        closeSoftKeyboard();

        // Select shelter
        onView(withId(R.id.create_pet_fragment_shelter_auto_complete_text_view)).perform(click());
        Thread.sleep(500);
        onView(withText(shelter))
                .inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        Thread.sleep(500);

        // Select type
        onView(withId(R.id.create_pet_fragment_type_auto_complete_text_view)).perform(click());
        Thread.sleep(500);
        onView(withText(type))
                .inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        Thread.sleep(500);

        // Go to bottom and fill photo
        onView(withId(R.id.create_pet_fragment_scroll_view)).perform(swipeUp());
        onView(withId(R.id.create_pet_fragment_photo_text_input_edit_text)).perform(clearText(), typeText(photo));
        //onView(withId(R.id.create_pet_fragment_photo_text_input_edit_text)).perform(clearText(), typeText(photo));
        closeSoftKeyboard();
        onView(withId(R.id.create_pet_fragment_submit_photo)).perform(click());

        // Create pet
        onView(withId(R.id.create_pet_fragment_scroll_view)).perform(swipeUp());
        Thread.sleep(500);
        onView(withId(R.id.create_pet_fragment_create_pet)).perform(click());
        onView(withText("Создание питомца")).check(matches(isDisplayed()));
        Thread.sleep(1000);
        onView(withText("ДА")).perform(click());

        Thread.sleep(1000);
        onView(withText(success)).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));




        // DELETE PET
        onView(withId(R.id.searchFragment)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());
        onView(withId(R.id.filter_view_parent_check_box_types)).perform(click());
        onView(withId(R.id.filter_view_parent_check_box_shelters)).perform(click());
        onView(withText(type)).perform(click());
        onView(withText(shelter)).perform(click());
        onView(withId(R.id.search_fragment_show_more_button)).perform(click());

        onView(withId(R.id.search_fragment_start_search)).perform(click());
        Thread.sleep(1000);
        try {
            onView(withId(R.id.search_fragment_recycle_view)).check(matches(isDisplayed()));

            int i = 0;
            String currentPetName = CustomMatcher.getText(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(i, R.id.card_pet_name));
            while (!currentPetName.equals(name)) {
                onView(withId(R.id.search_fragment_recycle_view)).perform(swipeLeft());
                Thread.sleep(1000);
                i++;
                currentPetName = CustomMatcher.getText(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(i, R.id.card_pet_name));
            }

            if (i > CustomMatcher.getCountFromRecyclerView(R.id.search_fragment_recycle_view)) {
                return;
            }
            onView(withRecyclerView(R.id.search_fragment_recycle_view).atPositionOnView(i, R.id.card_pet_image)).perform(click());
            onView(withId(R.id.fragment_pet_nested_scroll_view)).perform(swipeUp());
            onView(withId(R.id.fragment_pet_information_delete_pet)).perform(click());
            Thread.sleep(500);
            onView(withText("Питомец успешно удален")).
                    inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                    check(matches(isDisplayed()));
        } catch (NoMatchingViewException | AssertionFailedError e) {
        }
    }
}
