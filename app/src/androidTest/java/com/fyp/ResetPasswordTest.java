package com.fyp;

import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
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
import static com.fyp.CustomMatcher.hasTextInputLayoutErrorText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ResetPasswordTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void resetPasswordCases() throws InterruptedException {
        String nonexistentEmail = "nonexistentEmail@mail.com";
        String email = "Irina130219@yandex.ru";
        String emailError = "Для восстановления пароля необходимо указать почту";
        String nonexistentEmailError = "Пользователь с указанной почтой не зарегистрирован";
        String success = "Письмо успешно отправлено";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        onView(withId(R.id.sign_in_fragment_reset_password)).perform(click());

        // Without email
        onView(withId(R.id.reset_password_fragment_button)).perform(click());
        onView(withId(R.id.reset_password_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(emailError)));

        // Nonexistent email
        onView(withId(R.id.reset_password_fragment_email_input_edit_text)).perform(clearText(), typeText(nonexistentEmail));
        closeSoftKeyboard();
        onView(withId(R.id.reset_password_fragment_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.reset_password_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(nonexistentEmailError)));

        // Correct
        onView(withId(R.id.reset_password_fragment_email_input_edit_text)).perform(clearText(), typeText(email));
        closeSoftKeyboard();
        onView(withId(R.id.reset_password_fragment_button)).perform(click());
        Thread.sleep(1000);

        // Navigated back
        onView(withText(success)).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_fragment_reset_password)).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }
}
