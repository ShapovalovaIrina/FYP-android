package com.fyp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.pojo.UserMock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class UserViewModel extends ViewModel {
    private static final String TAG = "EmailPassword";

    private MutableLiveData<UserMock> userLiveData;
    private FirebaseAuth mAuth;

    public LiveData<UserMock> getUser() {
        System.out.println("View model get user");
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<UserMock>();
            getCurrentUser();
        }
        return userLiveData;
    }

    private void getCurrentUser() {
        // Initialize Firebase Auth
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseCurrentUser = mAuth.getCurrentUser();
        if (firebaseCurrentUser != null) {
            UserMock userMock = new UserMock(firebaseCurrentUser.getDisplayName(), firebaseCurrentUser.getEmail());
            userLiveData.setValue(userMock);
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
                            UserMock newUserMock = new UserMock(newName, firebaseCurrentUser.getEmail());
                            userLiveData.setValue(newUserMock);
                        }
                    }
                });
    }
}
