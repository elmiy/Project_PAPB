package com.example.tugasakhir.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.Model.DataModel;
import com.example.tugasakhir.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context ctx;
    private List<DataModel> listExpense;

    public AdapterData(Context ctx, List<DataModel> listExpense) {
        this.ctx = ctx;
        this.listExpense = listExpense;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengeluaran, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dataModel = listExpense.get(position);

        holder.tvId.setText(String.valueOf(dataModel.getId()));
        holder.tvAccid.setText(String.valueOf(dataModel.getAccid()));
        holder.tvTanggal.setText(dataModel.getTanggal());
        holder.tvKeterangan.setText(dataModel.getKeterangan());
        holder.tvNominal.setText(String.valueOf(dataModel.getNominal()));
        holder.icMoney.setImageAlpha(dataModel.getKategori());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, tvKeterangan, tvNominal, tvAccid;
        ImageView icMoney;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvAccid = itemView.findViewById(R.id.tv_accid);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvNominal = itemView.findViewById(R.id.tv_nominal);
            icMoney = itemView.findViewById(R.id.ic_money);
        }
    }
}
