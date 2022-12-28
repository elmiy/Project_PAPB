package com.example.tugasakhir.presentation.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tugasakhir.adapter.TransactionAdapter;
import com.example.tugasakhir.api.APIRequestData;
import com.example.tugasakhir.api.RetroServer;
import com.example.tugasakhir.callback.ResponseCallback;
import com.example.tugasakhir.callback.TransactionAdapterCallback;
import com.example.tugasakhir.databinding.FragmentHomeBinding;
import com.example.tugasakhir.model.BaseResponse;
import com.example.tugasakhir.model.transaction.TransactionResponse;
import com.example.tugasakhir.model.wallet.WalletResponse;
import com.example.tugasakhir.util.Formatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TransactionAdapter adapter;
    private final APIRequestData apiService = RetroServer.create();
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.homeHeader.tvTitle.setText("Home");
        binding.homeHeader.ivBack.setVisibility(View.INVISIBLE);

        adapter = new TransactionAdapter();
        binding.rvTransaction.setAdapter(adapter);
        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter.callback = new TransactionAdapterCallback() {
            @Override
            public void onItemDeleted(String transactionId) {
                deleteTransaction(transactionId);
            }
        };

        fetchIncomeExpense();
        fetchLastTransactions();
    }

    /** fetching **/
    private void fetchIncomeExpense() {
        apiService.fetchWallet(currentUser.getUid()).enqueue(new Callback<BaseResponse<WalletResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<WalletResponse>> call, Response<BaseResponse<WalletResponse>> response) {
                assert response.body() != null;
                if(response.isSuccessful()) {
                    incomeExpenseFetchedCallback.onSuccess(response.body().getData());
                } else {
                    incomeExpenseFetchedCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<WalletResponse>> call, Throwable t) {
                incomeExpenseFetchedCallback.onError(t.getMessage());
            }
        });
    }

    private void fetchLastTransactions() {
        lastTransactionsFetchedCallback.onLoading();
        apiService.fetchTransactions(currentUser.getUid()).enqueue(new Callback<BaseResponse<List<TransactionResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<TransactionResponse>>> call, Response<BaseResponse<List<TransactionResponse>>> response) {
                assert response.body() != null;
                if(response.isSuccessful()) {
                    List<TransactionResponse> data = response.body().getData();
                    if (data.isEmpty()) {
                        lastTransactionsFetchedCallback.onEmpty();
                    } else {
                        lastTransactionsFetchedCallback.onSuccess(data);
                    }
                } else {
                    lastTransactionsFetchedCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<TransactionResponse>>> call, Throwable t) {
                lastTransactionsFetchedCallback.onError(t.getMessage());
            }
        });
    }

    private void deleteTransaction(String transactionId) {
        apiService.deleteTransaction(transactionId).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if(response.isSuccessful()) {
                    transactionDeletedCallback.onSuccess(response.body().getData());
                } else {
                    transactionDeletedCallback.onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                transactionDeletedCallback.onError(t.getMessage());
            }
        });
    }

    /** callbacks **/
    private final ResponseCallback<WalletResponse> incomeExpenseFetchedCallback = new ResponseCallback<WalletResponse>() {
        @Override
        public void onSuccess(WalletResponse data) {
            binding.tvTotalIncome.setText("Rp" + Formatting.toRupiahCurrency(data.getIncome()) + ",00");
            binding.tvTotalExpense.setText("Rp" + Formatting.toRupiahCurrency(data.getExpense()) + ",00");
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final ResponseCallback<List<TransactionResponse>> lastTransactionsFetchedCallback = new ResponseCallback<List<TransactionResponse>>() {
        @Override
        public void onLoading() {
            binding.rvTransaction.setVisibility(View.INVISIBLE);
            binding.tvEmptyTransaction.setVisibility(View.INVISIBLE);
            binding.pbTransaction.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(List<TransactionResponse> data) {
            binding.rvTransaction.setVisibility(View.VISIBLE);
            binding.tvEmptyTransaction.setVisibility(View.INVISIBLE);
            binding.pbTransaction.setVisibility(View.INVISIBLE);
            adapter.submitList(data);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            binding.rvTransaction.setVisibility(View.INVISIBLE);
            binding.tvEmptyTransaction.setVisibility(View.VISIBLE);
            binding.pbTransaction.setVisibility(View.INVISIBLE);
        }
    };

    private final ResponseCallback<String> transactionDeletedCallback = new ResponseCallback<String>() {
        @Override
        public void onSuccess(String data) {
            fetchIncomeExpense();
            fetchLastTransactions();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        fetchLastTransactions();
        fetchIncomeExpense();
    }
}
