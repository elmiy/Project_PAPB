package com.example.tugasakhir.presentation.auth.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tugasakhir.R;
import com.example.tugasakhir.databinding.FragmentLoginBinding;
import com.example.tugasakhir.presentation.MainActivity;
import com.example.tugasakhir.presentation.auth.register.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            Intent main = new Intent(requireContext(), MainActivity.class);
            startActivity(main);
            requireActivity().finish();
        }

        binding.btnMasuk.setOnClickListener(v -> {

            String email = binding.ptEmailLogin.getText().toString().trim();
            String password = binding.ptPasswordLogin.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.ptEmailLogin.setError("Email tidak dapat kosong");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.ptPasswordLogin.setError("Password tidak dapat kosong");
                return;
            }

            binding.btnMasuk.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }else{
                    binding.btnMasuk.setEnabled(true);
                    Toast.makeText(requireContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        binding.linkDaftar.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_auth, new RegisterFragment())
                        .addToBackStack(null)
                        .commit()
        );

    }
}