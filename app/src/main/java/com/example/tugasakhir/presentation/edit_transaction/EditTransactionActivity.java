package com.example.tugasakhir.presentation.edit_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.callback.ResponseCallback;
import com.example.tugasakhir.databinding.ActivityEditTransactionBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.transaction.TransactionBody;
import com.example.tugasakhir.model.transaction.TransactionResponse;
import com.example.tugasakhir.util.Constant;
import com.example.tugasakhir.util.TransactionType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTransactionActivity extends AppCompatActivity {

    private ActivityEditTransactionBinding binding;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final APIRequestData apiService = RetroServer.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.headerEditTransaction.tvTitle.setText("Ubah");
        binding.headerEditTransaction.ivBack.setOnClickListener(v -> finish());

        String transactionId = getIntent().getStringExtra(Constant.EXTRA_TRANSACTION_ID);

        fetchTransactionDetail(transactionId);

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
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                TransactionBody body = new TransactionBody(currentUser.getUid(), Integer.parseInt(amount), type, description, date);
                updateTransaction(transactionId, body);
            }
        });
    }

    private void fetchTransactionDetail(String transactionId) {
        transactionFetchedCallback.onLoading();
        apiService.fetchTransaction(transactionId).enqueue(new Callback<BaseResponse<TransactionResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<TransactionResponse>> call, Response<BaseResponse<TransactionResponse>> response) {
                if(response.isSuccessful()) {
                    TransactionResponse transactionResponse = response.body().getData();
                    transactionFetchedCallback.onSuccess(transactionResponse);
                } else {
                    transactionFetchedCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<TransactionResponse>> call, Throwable t) {
                transactionFetchedCallback.onError(t.getMessage());
            }
        });
    }

    private void updateTransaction(String transactionId, TransactionBody body) {
        transactionUpdatedCallback.onLoading();
        apiService.putTransaction(transactionId, body).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if(response.isSuccessful()) {
                    transactionUpdatedCallback.onSuccess(response.body().getData());
                } else {
                    transactionUpdatedCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                transactionUpdatedCallback.onError(t.getMessage());
            }
        });
    }

    private final ResponseCallback<TransactionResponse> transactionFetchedCallback = new ResponseCallback<TransactionResponse>() {
        @Override
        public void onSuccess(TransactionResponse data) {
            binding.btnSave.setEnabled(true);
            if (data.getType().equals(TransactionType.INCOME.getType())) {
                binding.tbIncome.setChecked(true);
            } else {
                binding.tbExpense.setChecked(true);
            }
            binding.inputTanggal.setText(data.getDate());
            binding.inputKeterangan.setText(data.getDescription());
            binding.inputNominal.setText(String.valueOf(data.getAmount()));
        }

        @Override
        public void onError(String message) {
            Toast.makeText(EditTransactionActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.btnSave.setEnabled(false);
        }
    };

    private final ResponseCallback<String> transactionUpdatedCallback = new ResponseCallback<String>() {
        @Override
        public void onSuccess(String data) {
            Toast.makeText(EditTransactionActivity.this, "Transaksi berhasil diubah", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(String message) {
            binding.btnSave.setEnabled(true);
            Toast.makeText(EditTransactionActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.btnSave.setEnabled(false);
        }
    };
}