package ru.mirea.panin.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView status, detail;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.statusView);
        detail = findViewById(R.id.detailsView);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        findViewById(R.id.siginBtn).setOnClickListener(this);
        findViewById(R.id.createaccBtn).setOnClickListener(this);
        findViewById(R.id.signoutBtn).setOnClickListener(this);
        findViewById(R.id.verifyemailBtn).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        if (user != null){
            status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.siginBtn).setVisibility(View.GONE);
            findViewById(R.id.editTextEmail).setVisibility(View.GONE);
            findViewById(R.id.createaccBtn).setVisibility(View.GONE);
            findViewById(R.id.editTextPassword).setVisibility(View.GONE);
            findViewById(R.id.verifyemailBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.signoutBtn).setVisibility(View.VISIBLE);
        }
        else {
            status.setText(R.string.sign_out);
            detail.setText(null);

            findViewById(R.id.siginBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.createaccBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextPassword).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyemailBtn).setVisibility(View.GONE);
            findViewById(R.id.signoutBtn).setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String eml = email.getText().toString();
        if (TextUtils.isEmpty(eml)) {
            email.setError("Required.");
            valid = false;
        }
        else
            email.setError(null);

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)){
            password.setError("Required.");
            valid = false;
        }
        else
            password.setError(null);

        return valid;
    }

    private void createAccount(String eml, String pass){
        Log.d(TAG, "createAccount:" + email);
        if(!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(eml, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentification failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void signIn(String eml, String pass){
        Log.d(TAG, "signIn:" + email);
        if(!validateForm()){
            return;
        }
        mAuth.signInWithEmailAndPassword(eml, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                if (!task.isSuccessful()) {
                    status.setText(R.string.auth_failed);
                }
            }
        });
    }

    private void signOut(){
        mAuth.signOut();
        updateUI(null);
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email send.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(this, "Already verified.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.createaccBtn)
            createAccount(email.getText().toString(), password.getText().toString());
        else if (i == R.id.siginBtn)
            signIn(email.getText().toString(), password.getText().toString());
        else if (i == R.id.signoutBtn)
            signOut();
        else if (i == R.id.verifyemailBtn)
            sendVerificationEmail();
    }
}