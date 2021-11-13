package com.example.covid_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covid_tracker.api.ApiUtil;
import com.example.covid_tracker.api.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CountryData> list;
    private ProgressDialog progressDialog;
    private EditText searchbar;
    private CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        recyclerView = findViewById(R.id.Countries);
        list = new ArrayList<>();
        searchbar = findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new CountryAdapter(this, list);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.setCancelable(false);
        progressDialog.show();


        ApiUtil.getApiInterface().getcountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(CountryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }
    private void filter(String text){
        List<CountryData> filterList = new ArrayList<>();
        for (CountryData items : list){
            if (items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
}