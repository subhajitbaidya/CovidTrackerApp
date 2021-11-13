package com.example.covid_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_tracker.api.ApiUtil;
import com.example.covid_tracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm,totalActive,totalRecover,totalDeath,totalTest;
    private TextView todayConfirm,todayRecover,todayDeath,dateTV;
    private PieChart pieChart;

    private List<CountryData> list;
    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if (getIntent().getStringExtra("country") != null)
            country = getIntent().getStringExtra("country");

        init();

        TextView cname = findViewById(R.id.cname);
        cname.setText(country);

        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CountryActivity.class)));

        ApiUtil.getApiInterface().getcountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i = 0; i<list.size(); i++){
                    if (list.get(i).getCountry().equals(country)){
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int deaths = Integer.parseInt(list.get(i).getDeaths());

                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalRecover.setText(NumberFormat.getInstance().format(recovered));
                        totalDeath.setText(NumberFormat.getInstance().format(deaths));

                        todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayRecover.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        totalTest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Active", confirm, getResources().getColor(R.color.blue)));
                        pieChart.addPieSlice(new PieModel("Recovered", confirm, getResources().getColor(R.color.green)));
                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.red)));

                        pieChart.startAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd,yyyy");

        long milliSeconds = Long.parseLong(updated);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        dateTV.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void init() {

        totalConfirm=findViewById(R.id.totalConfirm);
        totalActive=findViewById(R.id.totalActive);
        totalRecover=findViewById(R.id.totalRecover);
        totalDeath=findViewById(R.id.totalDeath);
        totalTest=findViewById(R.id.totalTest);
        todayConfirm=findViewById(R.id.todayConfirm);
        todayRecover=findViewById(R.id.todayRecover);
        todayDeath=findViewById(R.id.todayDeath);
        pieChart=findViewById(R.id.pieChart);
        totalTest=findViewById(R.id.totalTest);
        dateTV=findViewById(R.id.date);
    }
}