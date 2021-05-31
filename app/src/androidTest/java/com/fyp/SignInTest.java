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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.fyp.utils.CustomMatcher.hasTextInputLayoutErrorText;
import static com.fyp.utils.CustomMatcher.hasTextInputLayoutErrors;

public class SignInTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void signInCases() throws InterruptedException {
        String nonexistentEmail = "nonexistentEmail@mail.com";
        String email = "Irina130219@yandex.ru";
        String password = "123456";
        String emailError = "Для входа в аккаунт необходимо указать почту";
        String passwordError = "Для входа в аккаунт необходимо указать пароль";
        String incorrectPasswordError = "Неверный пароль";
        String nonexistentEmailError = "Пользователь с данной почтой не существует";

        onView(withId(R.id.email_sing_in_button)).perform(click());

        // Without email and password
        onView(withId(R.id.sign_in_fragment_button)).perform(click());
        onView(withId(R.id.sign_in_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(emailError)));
        onView(withId(R.id.sign_in_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordError)));

        // Without password
        onView(withId(R.id.sign_in_fragment_email_input_edit_text)).perform(typeText(email));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_fragment_button)).perform(click());
        onView(withId(R.id.sign_in_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.sign_in_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordError)));

        // Incorrect password
        onView(withId(R.id.sign_in_fragment_password_input_edit_text)).perform(typeText(password + "1"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_fragment_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.sign_in_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrors(false)));
        onView(withId(R.id.sign_in_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(incorrectPasswordError)));

        // Nonexistent email
        onView(withId(R.id.sign_in_fragment_email_input_edit_text)).perform(clearText(), typeText(nonexistentEmail));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_fragment_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.sign_in_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(nonexistentEmailError)));
        onView(withId(R.id.sign_in_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrors(false)));

        // Correct
        UIActions.signInWithEmail(email, password);

        // Navigated to search fragment
        onView(withId(R.id.search_fragment_show_more_button)).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }
}
