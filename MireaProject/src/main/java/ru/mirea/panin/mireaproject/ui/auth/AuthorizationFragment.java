package ru.mirea.panin.mireaproject.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.panin.mireaproject.MainActivity;
import ru.mirea.panin.mireaproject.databinding.FragmentAuthorizationBinding;


public class AuthorizationFragment extends Fragment {
    private FragmentAuthorizationBinding binding;
    private TextView status, detail;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        status = binding.statusView;
        detail = binding.detailsView;
        email = binding.editTextEmail;
        password = binding.editTextPassword;
        mAuth = FirebaseAuth.getInstance();
        binding.siginBtn.setOnClickListener(view
                -> signIn(email.getText().toString(), password.getText().toString()));
        binding.createaccBtn.setOnClickListener(view
                -> createAccount(email.getText().toString(), password.getText().toString()));
        binding.signoutBtn.setOnClickListener(view
                -> signOut());
        binding.verifyemailBtn.setOnClickListener(view
                -> sendVerificationEmail());
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            status.setText(String.format("Email: %s (verified: %s)",
                    user.getEmail(), user.isEmailVerified()));
            detail.setText(String.format("UID: %s", user.getUid()));

            binding.siginBtn.setVisibility(View.GONE);
            binding.editTextEmail.setVisibility(View.GONE);
            binding.createaccBtn.setVisibility(View.GONE);
            binding.editTextPassword.setVisibility(View.GONE);
            binding.verifyemailBtn.setVisibility(View.VISIBLE);
            binding.signoutBtn.setVisibility(View.VISIBLE);
        } else {
            status.setText("Signed Out");
            detail.setText(null);

            binding.siginBtn.setVisibility(View.VISIBLE);
            binding.editTextEmail.setVisibility(View.VISIBLE);
            binding.createaccBtn.setVisibility(View.VISIBLE);
            binding.editTextPassword.setVisibility(View.VISIBLE);
            binding.verifyemailBtn.setVisibility(View.GONE);
            binding.signoutBtn.setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String eml = email.getText().toString();
        if (TextUtils.isEmpty(eml)) {
            email.setError("Required.");
            valid = false;
        } else
            email.setError(null);

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else
            password.setError(null);

        return valid;
    }

    private void createAccount(String eml, String pass) {
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(eml, pass).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                Toast.makeText(requireContext(), "Authentification failed.",
                        Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    private void signIn(String eml, String pass) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(eml, pass).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
            if (!task.isSuccessful()) {
                status.setText("Authentication failed");
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Email send.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
            Toast.makeText(requireContext(), "Already verified.", Toast.LENGTH_SHORT).show();
    }
}