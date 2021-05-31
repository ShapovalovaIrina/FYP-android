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

public class UpdatePasswordTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void updatePasswordCases() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";
        String shortPassword = "12345";

        String noCurrentPasswordError = "Для изменения введите текущий пароль";
        String incorrectCurrentPassword = "Неверный пароль";
        String noNewPasswordError = "Для изменения пароля необходимо указать пароль и повторить его";
        String newPasswordNotStrongError = "Длина пароля должна быть не меньше 6 символов";
        String passwordEqualsError = "Введенные пароли не совпадают";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);
        Thread.sleep(1000);

        // Profile tab
        onView(withId(R.id.profileFragment)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.profile_fragment_update_password)).perform(click());

        // Without current, new and repeated password
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrorText(noCurrentPasswordError)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrorText(noNewPasswordError)));

        // Without new and repeated password
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrorText(noNewPasswordError)));

        // Without repeated password
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_new_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrorText(noNewPasswordError)));

        // With length < 6
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_new_password_input_edit_text)).perform(clearText(), typeText(shortPassword));
        onView(withId(R.id.update_password_fragment_repeat_new_password_input_edit_text)).perform(clearText(), typeText(shortPassword));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrorText(newPasswordNotStrongError)));

        // Password not equals
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_new_password_input_edit_text)).perform(clearText(), typeText(password + "1"));
        onView(withId(R.id.update_password_fragment_repeat_new_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordEqualsError)));

        // With incorrect current password
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password + "1"));
        onView(withId(R.id.update_password_fragment_new_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_repeat_new_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrorText(incorrectCurrentPassword)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));

        // Correct
        onView(withId(R.id.update_password_fragment_current_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_new_password_input_edit_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.update_password_fragment_repeat_new_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.update_password_fragment_update_password)).perform(click());
        onView(withId(R.id.update_password_fragment_current_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.update_password_fragment_new_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        Thread.sleep(500);
        onView(withText("Пароль успешно обновлен")).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }
}
