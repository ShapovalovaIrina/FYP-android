package com.fyp.utils;

import com.fyp.R;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UIActions {
    public static void signInWithEmail(String email, String password) throws InterruptedException {
        onView(withId(R.id.sign_in_fragment_email_input_edit_text)).perform(clearText(), typeText(email));
        onView(withId(R.id.sign_in_fragment_password_input_edit_text)).perform(clearText(), typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_fragment_button)).perform(click());
        Thread.sleep(500);
    }
}
