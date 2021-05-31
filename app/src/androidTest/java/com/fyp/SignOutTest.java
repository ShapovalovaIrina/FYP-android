package com.fyp;

import androidx.test.rule.ActivityTestRule;

import com.fyp.ui.MainActivity;
import com.fyp.utils.UIActions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignOutTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void firebaseSignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void signOut() throws InterruptedException {
        String email = "Irina130219@yandex.ru";
        String password = "123456";

        onView(withId(R.id.email_sing_in_button)).perform(click());
        UIActions.signInWithEmail(email, password);

        Thread.sleep(2000);
        onView(withId(R.id.profileFragment)).perform(click());
        onView(withId(R.id.profile_fragment_sign_out)).perform(click());
        Thread.sleep(500);
    }
}
