package com.informatix.intruder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.informatix.intruder.Adapters.SettingsDataAdapter;
import com.informatix.intruder.Api.Service;
import com.informatix.intruder.Model.MainWeekModels;
import com.informatix.intruder.Model.SettingsMainModel;
import com.informatix.intruder.Model.SettingsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity {

    ImageView ClickMenu,ClickClose;
    DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Integer device_id;
    String mainDeviceID;
    TextView profileNameID,totalBalance, ClickWeek, ClickDay, ClickSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userID= sharedPreferences.getString("userID",null);
        String password= sharedPreferences.getString("password",null);
        String name= sharedPreferences.getString("name",null);
        mainDeviceID= sharedPreferences.getString("device_id","");
        String valid_till= sharedPreferences.getString("valid_till",null);

      /*  Toast.makeText(this,userID+"\n"+password+"\n"+name+"\n"+mainDeviceID+"\n"+valid_till, Toast.LENGTH_SHORT).show();*/

        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        drawerLayout = findViewById(R.id.drawer);
        ClickMenu = findViewById(R.id.ClickMenu);
        ClickClose = findViewById(R.id.ClickClose);
        ClickWeek = findViewById(R.id.ClickWeek);
        ClickDay = findViewById(R.id.ClickDay);
        ClickSetting = findViewById(R.id.ClickSetting);
        drawerLayout = findViewById(R.id.drawer);
        profileNameID = findViewById(R.id.profileNameID);
        totalBalance = findViewById(R.id.totalBalance);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        profileNameID.setText(name);
        totalBalance.setText(valid_till);
        listShow();

        ClickMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        ClickClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        ClickWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });
        ClickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,DayListActivity.class);
                startActivity(intent);
            }
        });
        ClickSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
        /*view.setClickable(false);*/
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            ClickClose.setClickable(true);
        }
    }


    private void listShow() {
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://us.infrmtx.com/iot/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service api = retrofit.create(Service.class);
        Call<SettingsMainModel> call= api.getSettingModels(Integer.valueOf(mainDeviceID));
        call.enqueue(new Callback<SettingsMainModel>() {
            @Override
            public void onResponse(Call<SettingsMainModel> call, Response<SettingsMainModel> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    ArrayList<SettingsModel> settingsModels = response.body().getSettings();
                    saveShowAdapter(settingsModels,mainDeviceID);
                }else{
                    progressDialog.dismiss();
                  /*  Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onFailure(Call<SettingsMainModel> call, Throwable t) {
                progressDialog.dismiss();
              /*  Toast.makeText(SettingsActivity.this, "Failed", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void saveShowAdapter(ArrayList<SettingsModel> list, String mainDeviceID) {
        SettingsDataAdapter settingsDataAdapter = new SettingsDataAdapter(list,getApplicationContext(),mainDeviceID);
        recyclerView.setAdapter(settingsDataAdapter);
    }
}