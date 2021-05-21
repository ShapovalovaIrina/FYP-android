package com.fyp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.response.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class UserViewModel extends ViewModel {
    private static final String TAG = "EmailPassword";

    private MutableLiveData<User> userLiveData;
    private FirebaseAuth mAuth;

    public LiveData<User> getUser() {
        System.out.println("View model get user");
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
            Log.d(TAG, "userLiveData = NULL. Get current user info");
            getCurrentUser();
        }
        return userLiveData;
    }

    private void getCurrentUser() {
        // Initialize Firebase Auth
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseCurrentUser = mAuth.getCurrentUser();
        if (firebaseCurrentUser != null) {
            User user = new User(firebaseCurrentUser.getDisplayName(), firebaseCurrentUser.getEmail());
            userLiveData.setValue(user);
        }
    }

    public void updateFirebaseUserName(final String newName) {
        // Initialize Firebase Auth
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseCurrentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        firebaseCurrentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile name updated to " + newName);
                            User newUser = new User(newName, firebaseCurrentUser.getEmail());
                            userLiveData.setValue(newUser);
                        }
                    }
                });
    }
}
