package com.fyp;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.fyp.CustomMatcher.hasTextInputLayoutErrorText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class RegistrationTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void registrationCases() throws InterruptedException {
        String email = "registrationCases@mail.com";
        String emailError = "Для регистрации необходимо указать почту";
        String passwordError = "Для регистрации необходимо указать пароль и повторить его";
        String passwordRepeatError = "Введенные пароли не совпадают";

        onView(withId(R.id.no_account_button)).perform(click());

        onView(withId(R.id.sign_up_fragment_sign_in_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.sign_up_fragment_not_receive_mail)).check(matches(not(isDisplayed())));

        // Without email and password
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(emailError)));
        onView(withId(R.id.sign_up_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordError)));

        // Without password
        onView(withId(R.id.sign_up_fragment_email_input_edit_text)).perform(typeText(email));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_layout)).check(matches(not(hasTextInputLayoutErrorText(emailError))));
        onView(withId(R.id.sign_up_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordError)));

        // Without password repeat
        onView(withId(R.id.sign_up_fragment_password_input_edit_text)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_layout)).check(matches(not(hasTextInputLayoutErrorText(emailError))));
        onView(withId(R.id.sign_up_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordError)));

        // Incorrect repeat
        onView(withId(R.id.sign_up_fragment_repeat_password_input_edit_text)).perform(typeText("1234567"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_layout)).check(matches(not(hasTextInputLayoutErrorText(emailError))));
        onView(withId(R.id.sign_up_fragment_password_input_layout)).check(matches(hasTextInputLayoutErrorText(passwordRepeatError)));

        // Correct
        onView(withId(R.id.sign_up_fragment_repeat_password_input_edit_text)).perform(clearText(), typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withText("Создан пользователь с почтой " + email)).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        Thread.sleep(5000);
        onView(withId(R.id.sign_up_fragment_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.sign_up_fragment_sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_up_fragment_not_receive_mail)).check(matches(isDisplayed()));

        // Send one more verification mail
        onView(withId(R.id.sign_up_fragment_not_receive_mail)).perform(click());
        onView(withText("Письмо повторно отправлено на почту " + email)).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void registrationWithExistingUserEmail() throws InterruptedException {
        String emailError = "Пользователь с указанной почтой уже существует";

        onView(withId(R.id.no_account_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_edit_text)).perform(typeText("registrationWithExistingUserEmail@mail.com"));
        onView(withId(R.id.sign_up_fragment_password_input_edit_text)).perform(clearText(), typeText("123456"));
        onView(withId(R.id.sign_up_fragment_repeat_password_input_edit_text)).perform(clearText(), typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());

        Thread.sleep(5000);

        pressBack();

        onView(withId(R.id.no_account_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_edit_text)).perform(typeText("registrationWithExistingUserEmail@mail.com"));
        onView(withId(R.id.sign_up_fragment_password_input_edit_text)).perform(clearText(), typeText("123456"));
        onView(withId(R.id.sign_up_fragment_repeat_password_input_edit_text)).perform(clearText(), typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_fragment_button)).perform(click());
        onView(withId(R.id.sign_up_fragment_email_input_layout)).check(matches(hasTextInputLayoutErrorText(emailError)));
    }
}