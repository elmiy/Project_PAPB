package com.example.tugasakhir.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.API.APIRequestData;
import com.example.tugasakhir.API.RetroServer;
import com.example.tugasakhir.Adapter.AdapterData;
import com.example.tugasakhir.Model.DataModel;
import com.example.tugasakhir.Model.ResponseModel;
import com.example.tugasakhir.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPengeluaran extends AppCompatActivity {
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pengeluaran);

        rvData = findViewById(R.id.rv_data);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);
        retrieveData();
    }

    public void retrieveData() {
        APIRequestData ard = RetroServer.connectRetro().create(APIRequestData.class);
        Call<ResponseModel> crm = ard.ardRetrieveData();

        crm.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                Toast.makeText(ListPengeluaran.this, "Kode="+kode+" Pesan="+pesan, Toast.LENGTH_SHORT).show();

                listData = response.body().getData();
                adData = new AdapterData(ListPengeluaran.this, listData);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ListPengeluaran.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
