package com.example.tugasakhir.presentation.profile;


import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tugasakhir.R;
import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.callback.ResponseCallback;
import com.example.tugasakhir.databinding.FragmentProfileBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.user.AvatarBody;
import com.example.tugasakhir.model.user.UserResponse;
import com.example.tugasakhir.presentation.auth.AuthActivity;
import com.example.tugasakhir.util.StatusCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private final APIRequestData apiService = RetroServer.create();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private Integer photoMax = 1;
    private Uri photoLocation = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.headerProfile.tvTitle.setText("Profil");
        binding.headerProfile.ivBack.setVisibility(View.INVISIBLE);

        binding.tvLogout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
            requireActivity().finish();
        });

        fetchUser();

        binding.ivAvatar.setOnClickListener(v -> findPhoto());
    }

    private void findPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photoMax && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoLocation = data.getData();
            updateAvatar();
        }
    }

    private void fetchUser() {
        profileFetchedCallback.onLoading();
        assert currentUser != null;
        apiService.fetchUser(currentUser.getUid()).enqueue(new Callback<BaseResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponse>> call, Response<BaseResponse<UserResponse>> response) {
                assert response.body() != null;
                if(response.isSuccessful()) {
                    profileFetchedCallback.onSuccess(response.body().getData());
                } else {
                    profileFetchedCallback.onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponse>> call, Throwable t) {
                profileFetchedCallback.onError(t.getMessage());
            }
        });
    }

    private void updateAvatar() {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(photoLocation));
        StorageReference storageReference = firebaseStorage.getReference().child("avatar_url");
        StorageReference storage = storageReference.child(String.valueOf(System.currentTimeMillis()) + "." + fileExtension);

        avatarUpdatedCallback.onLoading();
        storage.putFile(photoLocation)
                .addOnCompleteListener(requireActivity(), task -> {
                    if(task.isSuccessful()) {
                        storage.getDownloadUrl().addOnCompleteListener(requireActivity(), downloadUrl -> {
                            if(downloadUrl.isSuccessful()) {
                                String avatarUrl = downloadUrl.getResult().toString();
                                AvatarBody body = new AvatarBody(avatarUrl);

                                assert currentUser != null;
                                apiService.putAvatar(currentUser.getUid(), body).enqueue(new Callback<BaseResponse<String>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                        assert response.body() != null;
                                        if(response.isSuccessful()) {
                                            avatarUpdatedCallback.onSuccess(response.body().getData());
                                        } else {
                                            avatarUpdatedCallback.onError(response.body().getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                                        avatarUpdatedCallback.onError(t.getMessage());
                                    }
                                });
                            } else {
                                avatarUpdatedCallback.onError(downloadUrl.getException().getMessage());
                            }
                        });
                    } else {
                        avatarUpdatedCallback.onError(task.getException().getMessage());
                    }
                });
    }

    private final ResponseCallback<UserResponse> profileFetchedCallback = new ResponseCallback<UserResponse>() {
        @Override
        public void onSuccess(UserResponse data) {
            Glide.with(requireContext())
                    .load(data.getAvatar())
                    .placeholder(R.drawable.ic_default_ava)
                    .circleCrop()
                    .into(binding.ivAvatar);
            binding.tvNameValue.setText(data.getName());
            binding.tvEmailValue.setText(data.getEmail());
            binding.pbProfile.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.pbProfile.setVisibility(View.VISIBLE);
        }
    };

    private final ResponseCallback<String> avatarUpdatedCallback = new ResponseCallback<String>() {
        @Override
        public void onSuccess(String data) {
            fetchUser();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.pbProfile.setVisibility(View.VISIBLE);
        }
    };
}