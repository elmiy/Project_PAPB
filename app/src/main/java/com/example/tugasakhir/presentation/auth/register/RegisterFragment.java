package com.example.tugasakhir.presentation.auth.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugasakhir.R;
import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.databinding.FragmentRegisterBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.user.UserBody;
import com.example.tugasakhir.presentation.MainActivity;
import com.example.tugasakhir.presentation.auth.login.LoginFragment;
import com.example.tugasakhir.util.StatusCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final APIRequestData apiService = RetroServer.create();
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent main = new Intent(requireContext(), MainActivity.class);
            startActivity(main);
            requireActivity().finish();
        }

        binding.btnDaftar.setOnClickListener(v -> {
            registerToFirebase();
        });

        binding.linkLogin.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_auth, new LoginFragment())
                        .commit()
        );
    }

    private void registerToFirebase() {
        String name = binding.ptName.getText().toString().trim();
        String email = binding.ptEmail.getText().toString().trim();
        String password = binding.ptPassword.getText().toString().trim();
        String confirmPassword = binding.ptKonfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.ptName.setError("Nama tidak boleh kosong");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.ptEmail.setError("Email tidak dapat kosong");
            return;
        }
        if (TextUtils.isEmpty(password) | password.length() < 6) {
            binding.ptPassword.setError("Password harus >= 6");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword) | confirmPassword.length() < 6) {
            binding.ptKonfirmPassword.setError("Password harus >= 6");
            return;
        }
        if (!confirmPassword.equals(password) ){
            binding.ptKonfirmPassword.setError("Konfirmasi password tidak sama");
            return;
        }

        binding.btnDaftar.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (task.isSuccessful()){
                UserBody body = new UserBody(user.getUid(), email, name);
                registerToServer(body);
            }else{
                Toast.makeText(requireContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerToServer(UserBody body) {
        apiService.postUser(body).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                assert response.body() != null;
                if(response.isSuccessful()) {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    requireActivity().startActivity(intent);
                    requireActivity().finish();
                } else {
                    binding.btnDaftar.setEnabled(true);
                    Toast.makeText(requireContext(), "Error: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}