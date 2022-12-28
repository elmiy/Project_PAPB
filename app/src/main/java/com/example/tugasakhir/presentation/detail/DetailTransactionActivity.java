package com.example.tugasakhir.presentation.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tugasakhir.R;
import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.callback.ResponseCallback;
import com.example.tugasakhir.databinding.ActivityDetailTransactionBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.transaction.TransactionResponse;
import com.example.tugasakhir.presentation.edit_transaction.EditTransactionActivity;
import com.example.tugasakhir.util.Constant;
import com.example.tugasakhir.util.Formatting;
import com.example.tugasakhir.util.TransactionType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionActivity extends AppCompatActivity {

    private ActivityDetailTransactionBinding binding;
    private final APIRequestData apiService = RetroServer.create();

    private String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.headerDetailTransaction.tvTitle.setText("Detail");
        binding.headerDetailTransaction.ivBack.setOnClickListener(v -> finish());

        transactionId = getIntent().getStringExtra(Constant.EXTRA_TRANSACTION_ID);
        fetchTransactionDetail();

        binding.btnChange.setOnClickListener(v -> {
            Intent intent = new Intent(DetailTransactionActivity.this, EditTransactionActivity.class);
            intent.putExtra(Constant.EXTRA_TRANSACTION_ID, transactionId);
            startActivity(intent);
        });

        binding.ivDeleteData.setOnClickListener(v -> {
            deleteTransaction();
        });
    }

    private void fetchTransactionDetail() {
        transactionFetchedCallback.onLoading();
        apiService.fetchTransaction(transactionId).enqueue(new Callback<BaseResponse<TransactionResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<TransactionResponse>> call, Response<BaseResponse<TransactionResponse>> response) {
                if(response.isSuccessful()) {
                    transactionFetchedCallback.onSuccess(response.body().getData());
                } else {
                    transactionFetchedCallback.onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<TransactionResponse>> call, Throwable t) {
                transactionFetchedCallback.onError(t.getMessage());
            }
        });
    }

    private void deleteTransaction() {
        apiService.deleteTransaction(transactionId).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailTransactionActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetailTransactionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(DetailTransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ResponseCallback<TransactionResponse> transactionFetchedCallback = new ResponseCallback<TransactionResponse>() {
        @Override
        public void onSuccess(TransactionResponse data) {
            if(data.getType().equals(TransactionType.INCOME.getType())) {
                binding.tvTransactionType.setText("Pemasukan");
                binding.tvTransactionType.setTextColor(getResources().getColor(R.color.info));
                Glide.with(DetailTransactionActivity.this)
                        .load(R.drawable.money_icon_green)
                        .into(binding.ivTransactionType);
            } else {
                binding.tvTransactionType.setText("Pengeluaran");
                binding.tvTransactionType.setTextColor(getResources().getColor(R.color.danger));
                Glide.with(DetailTransactionActivity.this)
                        .load(R.drawable.money_icon_red)
                        .into(binding.ivTransactionType);
            }
            binding.textTanggal.setText(data.getDate());
            binding.textMoney.setText(Formatting.toRupiahCurrency(data.getAmount()));
            binding.textKeterangan.setText(data.getDescription());
            binding.btnChange.setEnabled(true);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(DetailTransactionActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading() {
            binding.btnChange.setEnabled(false);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        fetchTransactionDetail();
    }
}