package com.udp.paul.izisurvey.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.model.User;

public class register_user extends AppCompatActivity implements View.OnClickListener {

    private EditText nameView;
    private EditText lastNameView;
    private EditText userNameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText repeatPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
    private Button signUpBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        nameView = (EditText) findViewById(R.id.register_user_name);
        lastNameView = (EditText) findViewById(R.id.register_user_last_name);
        userNameView = (EditText) findViewById(R.id.register_user_user_name);
        emailView = (EditText) findViewById(R.id.register_user_email);
        passwordView = (EditText) findViewById(R.id.register_user_password);
        repeatPasswordView = (EditText) findViewById(R.id.register_user_repeat_password);
        signUpBtn = (Button) findViewById(R.id.email_sign_up_button);
        signUpBtn.setOnClickListener(this);

        mProgressView = findViewById(R.id.register_user_progress);
        mRegisterFormView = findViewById(R.id.register_user_form);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mFirebaseDatabase = mFirebaseInstance.getReference("users");

                    String name = nameView.getText().toString().trim();
                    String lastName = lastNameView.getText().toString().trim();
                    String userName = userNameView.getText().toString().trim();
                    String email = emailView.getText().toString().trim();
                    long longCreatedAt = System.currentTimeMillis();

                    User userDB = new User(name, lastName, userName, email, "Basico", longCreatedAt);

                    mFirebaseDatabase.child(user.getUid()).setValue(userDB);

                    addUserChangeListener(user.getUid());

                    Log.d("SIGN_IN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("SIGN_OUT", "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_up_button:
                attemptRegister();
                break;
            default:
                break;
        }
    }

    private void attemptRegister() {
        // Reset errors.
        nameView.setError(null);
        lastNameView.setError(null);
        userNameView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        repeatPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = nameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();
        String userName = userNameView.getText().toString().trim();
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String repeatPassword = repeatPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for valid fields

        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.error_field_required));
            focusView = nameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameView.setError(getString(R.string.error_field_required));
            focusView = lastNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)) {
            userNameView.setError(getString(R.string.error_field_required));
            focusView = userNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(repeatPassword)) {
            repeatPasswordView.setError(getString(R.string.error_field_required));
            focusView = repeatPasswordView;
            cancel = true;
        } else if (!repeatPassword.equals(password)) {
            passwordView.setError(getString(R.string.error_match_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("CREATE_USER", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            showProgress(false);

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                String error = ((FirebaseAuthException) task.getException()).getErrorCode();
                                if (error.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                    emailView.setError(getString(R.string.error_email_in_use));
                                    emailView.requestFocus();
                                }
                                Log.d("FIREBASE_AUTH_ERROR", ((FirebaseAuthException) task.getException()).getErrorCode());
                            }
                        }
                    });
        }

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void addUserChangeListener(String userId) {
        // User data change listener
        mFirebaseDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e("USER_NULL", "User data is null!");
                    return;
                }

                Log.e("USER_CHANGED", "User data is changed!" + user.getName() + ", " + user.getEmail());

                Toast.makeText(getApplicationContext(), "Registrado exitosamente! Bienvenido " + user.getUserName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), home.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("USER_FAILED_READ", "Failed to read user", error.toException());
            }
        });
    }
}