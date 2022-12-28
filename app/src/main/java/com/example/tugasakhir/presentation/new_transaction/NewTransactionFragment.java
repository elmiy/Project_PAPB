package com.example.tugasakhir.presentation.new_transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.callback.ResponseCallback;
import com.example.tugasakhir.databinding.FragmentNewTransactionBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.transaction.TransactionBody;
import com.example.tugasakhir.util.StatusCode;
import com.example.tugasakhir.util.TransactionType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewTransactionFragment extends Fragment {

    private FragmentNewTransactionBinding binding;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final APIRequestData apiService = RetroServer.create();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.headerNewTransaction.tvTitle.setText("Tambah");
        binding.headerNewTransaction.ivBack.setVisibility(View.INVISIBLE);

        binding.tbIncome.setOnClickListener(v -> {
            binding.tbIncome.setChecked(true);
            binding.tbExpense.setChecked(false);
        });

        binding.tbExpense.setOnClickListener(v -> {
            binding.tbIncome.setChecked(false);
            binding.tbExpense.setChecked(true);
        });

        binding.btnSave.setOnClickListener(v -> {
            String type = binding.tbIncome.isChecked() ? TransactionType.INCOME.getType() : TransactionType.EXPENSE.getType();
            String date = binding.inputTanggal.getText().toString();
            String amount = binding.inputNominal.getText().toString();
            String description = binding.inputKeterangan.getText().toString();

            if (date.isEmpty() || amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                TransactionBody body = new TransactionBody(currentUser.getUid(), Integer.parseInt(amount), type, description, date);
                postTransaction(body);
            }
        });
    }

    private void postTransaction(TransactionBody body) {
        transactionPosted.onLoading();
        apiService.postTransaction(body).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                assert response.body() != null;
                if(response.isSuccessful()) {
                    transactionPosted.onSuccess(response.body().getData());
                } else {
                    transactionPosted.onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                transactionPosted.onError(t.getMessage());
            }
        });
    }

    private ResponseCallback<String> transactionPosted = new ResponseCallback<String>() {
        @Override
        public void onSuccess(String data) {
            Toast.makeText(getContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            binding.inputTanggal.setText("");
            binding.inputNominal.setText("");
            binding.inputKeterangan.setText("");
            binding.btnSave.setEnabled(true);
        }

        @Override
        public void onError(String message) {
            binding.btnSave.setEnabled(true);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.btnSave.setEnabled(false);
        }
    };
}