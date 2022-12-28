package com.example.tugasakhir.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.R;
import com.example.tugasakhir.callback.TransactionAdapterCallback;
import com.example.tugasakhir.databinding.ItemTransactionBinding;
import com.example.tugasakhir.model.transaction.TransactionResponse;
import com.example.tugasakhir.presentation.detail.DetailTransactionActivity;
import com.example.tugasakhir.util.Constant;
import com.example.tugasakhir.util.Formatting;
import com.example.tugasakhir.util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionResponse> transactions = new ArrayList<>();
    public TransactionAdapterCallback callback;

    public void submitList(List<TransactionResponse> transactions) {
        this.transactions.clear();
        //reverse list
        for (int i = transactions.size() - 1; i >= 0; i--) {
            this.transactions.add(transactions.get(i));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private ItemTransactionBinding binding;

        public TransactionViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TransactionResponse transaction) {
            binding.tvDate.setText(transaction.getDate());
            String formattedAmount;
            if (transaction.getType().equals(TransactionType.INCOME.getType())) {
                formattedAmount = "+" + Formatting.toRupiahCurrency(transaction.getAmount());
                binding.ivMoney.setImageResource(R.drawable.money_icon_green);
            } else {
                formattedAmount = "-" + Formatting.toRupiahCurrency(transaction.getAmount());
                binding.ivMoney.setImageResource(R.drawable.money_icon_red);
            }
            binding.tvAmount.setText(formattedAmount);
            binding.tvDescription.setText(transaction.getDescription());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailTransactionActivity.class);
                intent.putExtra(Constant.EXTRA_TRANSACTION_ID, transaction.getTransactionId());
                itemView.getContext().startActivity(intent);
            });

            binding.ivDeleteData.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Hapus Catatan")
                        .setMessage("Apakah kamu yakin ingin menghapus " + transaction.getDescription() + "?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> callback.onItemDeleted(transaction.getTransactionId()))
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            });
        }
    }
}
