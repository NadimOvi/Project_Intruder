package com.informatix.intruder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.informatix.intruder.Api.Service;
import com.informatix.intruder.Model.SettingsMainModel;
import com.informatix.intruder.Model.SettingsModel;
import com.informatix.intruder.Model.WeekModels;
import com.informatix.intruder.R;
import com.informatix.intruder.SettingsActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsDataAdapter extends RecyclerView.Adapter<SettingsDataViewHolder> {
    ArrayList<SettingsModel> itemsList;
    String mainDeviceID;
    private Context context;
    LayoutInflater inflater;
    View view;
    String phone1;
    String phone2;
    String phone3;
    int checkBuzzer,checkRelay;

    public SettingsDataAdapter(ArrayList<SettingsModel> itemsList, Context context, String mainDeviceID) {
        this.itemsList=itemsList;
        this.context=context;
        this.mainDeviceID=mainDeviceID;

    }


    @NonNull
    @Override
    public SettingsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.settings_view_holder,parent,false);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SettingsDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingsDataViewHolder holder, int position) {

        SettingsModel settingsModel= itemsList.get(position);
        holder.mobile1EditText.setText(settingsModel.getPhone1());
        holder.mobile2EditText.setText(settingsModel.getPhone2());
        holder.mobile3EditText.setText(settingsModel.getPhone3());
        if (settingsModel.getBuzzer()==1){
            holder.checkboxBuzzer.setChecked(true);
            checkBuzzer = 1;
        }else{
            holder.checkboxBuzzer.setChecked(false);
            checkBuzzer = 0;
        }
        if (settingsModel.getRelay()==1){
            holder.checkboxRelay.setChecked(true);
            checkRelay = 1;
        }else{
            holder.checkboxRelay.setChecked(false);
            checkRelay = 0;
        }




        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mobile1EditText.setEnabled(true);
                holder.mobile2EditText.setEnabled(true);
                holder.mobile3EditText.setEnabled(true);
                holder.checkboxBuzzer.setEnabled(true);
                holder.checkboxRelay.setEnabled(true);
                holder.done.setVisibility(View.VISIBLE);
            }
        });

   /*     phone1 = holder.mobile1EditText.getText().toString();
        phone2 = holder.mobile2EditText.getText().toString();
        phone3 = holder.mobile3EditText.getText().toString();*/


        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone1 = holder.mobile1EditText.getText().toString();
                phone2 = holder.mobile2EditText.getText().toString();
                phone3 = holder.mobile3EditText.getText().toString();
                if (holder.checkboxBuzzer.isChecked()){
                    checkBuzzer = 1;
                }else {
                    checkBuzzer = 0;
                }
                if (holder.checkboxRelay.isChecked()){
                    checkRelay = 1;
                }else{
                    checkRelay = 0;
                }
                if (phone1.isEmpty()){
                    holder.mobile1EditText.setError("Please provide mobile number 1");
                    holder.mobile1EditText.requestFocus();
                }else if (phone2.isEmpty()){
                    holder.mobile2EditText.setError("Please provide mobile number 2");
                    holder.mobile2EditText.requestFocus();
                }else if (phone3.isEmpty()){
                    holder.mobile3EditText.setError("Please provide mobile number 3");
                    holder.mobile3EditText.requestFocus();
                }else{
                    postRequest();
                }
            }
        });

    }

    private void postRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://us.infrmtx.com/iot/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Service api = retrofit.create(Service.class);
        Call<JsonObject> call= api.getEditSettings(Integer.valueOf(mainDeviceID),phone1,phone2,phone3,checkRelay,checkBuzzer);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(context.getApplicationContext(), SettingsActivity.class);
                    context.startActivity(intent);
                }else{
                    /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Intent intent = new Intent(context.getApplicationContext(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
